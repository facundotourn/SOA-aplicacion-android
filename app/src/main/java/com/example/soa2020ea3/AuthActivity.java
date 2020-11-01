package com.example.soa2020ea3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.BatteryManager;
import android.os.Bundle;
import android.widget.TextView;

import com.example.soa2020ea3.network.CheckNetwork;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class AuthActivity extends AppCompatActivity {
    private TextView tv_connection_status;
    private TextView tv_batery_status;

    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context ctxt, Intent intent) {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            tv_batery_status.setText(String.valueOf(level) + "% de batería");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (isUserLoggedIn()) {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            finish();
        }

        setContentView(R.layout.activity_auth);

        ViewPager viewPager = findViewById(R.id.viewPager);
        tv_connection_status = (TextView) findViewById(R.id.text_connection_status);
        tv_batery_status = (TextView) findViewById(R.id.text_batery_status);

        if (CheckNetwork.isInternetAvailable(this)) {
            tv_connection_status.setText("Conexión a internet disponible.");
            tv_connection_status.setTextColor(Color.parseColor("#888888"));
        } else {
            tv_connection_status.setText("Conexión a internet no disponible.");
            tv_connection_status.setTextColor(Color.RED);
        }

        AuthenticationPagerAdapter pagerAdapter = new AuthenticationPagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(new LoginFragment());
        pagerAdapter.addFragment(new RegisterFragment());
        viewPager.setAdapter(pagerAdapter);

        this.registerReceiver(this.mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    class AuthenticationPagerAdapter extends FragmentPagerAdapter {
        private ArrayList<Fragment> fragmentList = new ArrayList<>();

        public AuthenticationPagerAdapter(FragmentManager fm) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        void addFragment(Fragment fragment) {
            fragmentList.add(fragment);
        }
    }

    public boolean isInternetAvailable() {
        try {
            InetAddress address = InetAddress.getByName("www.google.com");
            return !address.equals("");
        } catch (UnknownHostException e) {
            // Log error
        }
        return false;
    }

    public boolean isUserLoggedIn() {
        SharedPreferences preferences = this.getSharedPreferences("MY_APP",Context.MODE_PRIVATE);
        String retrivedToken  = preferences.getString("token",null);

        return retrivedToken != null;
    }
}