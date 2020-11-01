package com.example.soa2020ea3;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.service.autofill.TextValueSanitizer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static android.content.Context.SENSOR_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;

public class SensorsFragment extends Fragment implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mSensorAcelerometro;
    private Sensor mSensorLuz;
    private Activity mActivity;

    private TextView xValue;
    private TextView yValue;
    private TextView zValue;
    private Button btn_acelerometro;

    private TextView lumenValue;
    private Button btn_luz;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SensorsFragment() {

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            mActivity = (Activity) context;
        }
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SensorsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SensorsFragment newInstance(String param1, String param2) {
        SensorsFragment fragment = new SensorsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSensorManager = (SensorManager) mActivity.getSystemService(Context.SENSOR_SERVICE);
        mSensorAcelerometro = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorLuz = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        mSensorManager.registerListener(this, mSensorAcelerometro, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mSensorLuz, SensorManager.SENSOR_DELAY_NORMAL);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_sensors, container, false);

        xValue = (TextView) rootView.findViewById(R.id.txt_x_value);
        yValue = (TextView) rootView.findViewById(R.id.txt_y_value);
        zValue = (TextView) rootView.findViewById(R.id.txt_z_value);
        lumenValue  = (TextView) rootView.findViewById(R.id.txt_lx_value);

        btn_acelerometro = (Button) rootView.findViewById(R.id.btn_evt_acelerometro);
        btn_luz = (Button) rootView.findViewById(R.id.btn_evt_light);

        btn_acelerometro.setOnClickListener((val) -> {
            String type = "ACELEROMETRO_VALUES";
            String description = "Valor en X: " + xValue.getText().toString() + ", valor en Y: " + yValue.getText().toString() + ", valor en Z: " + zValue.getText().toString();

            MainActivity main = (MainActivity) getActivity();
            main.enviarEvento(type, description, true);
        });

        btn_luz.setOnClickListener((val) -> {
            String type = "LIGHT_VALUES";
            String description = lumenValue.getText().toString() + " lumen por metro cuadrado";

            MainActivity main = (MainActivity) getActivity();
            main.enviarEvento(type, description, true);
        });

        return rootView;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float[] values = event.values;

        switch (event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                xValue.setText(String.format("%.2f", values[0]));
                yValue.setText(String.format("%.2f", values[1]));
                zValue.setText(String.format("%.2f", values[2]));

                break;
            case Sensor.TYPE_LIGHT:
                lumenValue.setText(String.format("%.2f", values[0]));
                break;
            default:
                break;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}