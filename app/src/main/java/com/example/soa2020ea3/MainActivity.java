package com.example.soa2020ea3;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.soa2020ea3.model.AuthTokens;
import com.example.soa2020ea3.model.EventRequestBody;
import com.example.soa2020ea3.model.EventResponse;
import com.example.soa2020ea3.services.EventService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.soa2020ea3.ui.main.SectionsPagerAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.example.soa2020ea3.network.RetrofitInstance.getRetrofitInstance;

public class MainActivity extends AppCompatActivity {
    //private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager viewPager = findViewById(R.id.view_pager);

        MainPagerAdapter sectionsPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        sectionsPagerAdapter.addFragment(new GifsFragment());
        sectionsPagerAdapter.addFragment(new SensorsFragment());
        viewPager.setAdapter(sectionsPagerAdapter);

        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_logout:
                // accion
                SharedPreferences preferences = getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
                preferences.edit().remove("token").remove("refreshToken").commit();

                Intent i = new Intent(this, AuthActivity.class);
                startActivity(i);
                finish();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    class MainPagerAdapter extends FragmentPagerAdapter {
        private ArrayList<Fragment> fragmentList = new ArrayList<>();

        public MainPagerAdapter(FragmentManager fm) {
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

        public void addFragment(Fragment f) {
            fragmentList.add(f);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String title = null;
            if (position == 0)
            {
                title = "GIFs";
            }
            else if (position == 1)
            {
                title = "Sensores";
            }
            return title;
        }
    }

    public void enviarEvento(String type, String description) {
        Retrofit retrofit = getRetrofitInstance("http://so-unlam.net.ar/api/api/");
        EventService eventService = retrofit.create(EventService.class);

        SharedPreferences preferences = this.getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        String token = preferences.getString("token",null);

        Call<EventResponse> call = eventService.postEvent(new EventRequestBody("PROD", type, description), "Bearer " + token);
        call.enqueue(new Callback<EventResponse>() {
            @Override
            public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                Toast.makeText(MainActivity.this, "Evento enviado.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<EventResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Ocurrió un error al intentar enviar el evento", Toast.LENGTH_SHORT).show();
            }
        });
    }
}