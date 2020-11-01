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
import androidx.lifecycle.Lifecycle;

import android.service.autofill.TextValueSanitizer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.soa2020ea3.model.EventRequestBody;
import com.example.soa2020ea3.model.EventResponse;
import com.example.soa2020ea3.services.EventDispatcher;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    public SensorsFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            mActivity = (Activity) context;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSensorManager = (SensorManager) mActivity.getSystemService(Context.SENSOR_SERVICE);
        mSensorAcelerometro = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorLuz = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        mSensorManager.registerListener(this, mSensorAcelerometro, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mSensorLuz, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return super.getLifecycle();
    }

    @Override
    public void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mSensorAcelerometro, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mSensorLuz, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
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

            Callback<EventResponse> callback = new Callback<EventResponse>() {
                @Override
                public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                    Toast.makeText(getActivity(), "Evento enviado.", Toast.LENGTH_SHORT).show();

                    btn_acelerometro.setClickable(true);
                    btn_acelerometro.setEnabled(true);
                }
                @Override
                public void onFailure(Call<EventResponse> call, Throwable t) {
                    Toast.makeText(getActivity(), "Ocurrió un error al intentar enviar el evento", Toast.LENGTH_SHORT).show();

                    btn_acelerometro.setClickable(true);
                    btn_acelerometro.setEnabled(true);
                }
            };

            MainActivity main = (MainActivity) getActivity();
            EventDispatcher.enviarEvento(getActivity(), new EventRequestBody(type, description), callback);

            btn_acelerometro.setClickable(false);
            btn_acelerometro.setEnabled(false);
        });

        btn_luz.setOnClickListener((val) -> {
            String type = "LIGHT_VALUES";
            String description = lumenValue.getText().toString() + " lumen por metro cuadrado";

            Callback<EventResponse> callback = new Callback<EventResponse>() {
                @Override
                public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                    Toast.makeText(getActivity(), "Evento enviado.", Toast.LENGTH_SHORT).show();

                    btn_luz.setClickable(true);
                    btn_luz.setEnabled(true);
                }
                @Override
                public void onFailure(Call<EventResponse> call, Throwable t) {
                    Toast.makeText(getActivity(), "Ocurrió un error al intentar enviar el evento", Toast.LENGTH_SHORT).show();

                    btn_luz.setClickable(true);
                    btn_luz.setEnabled(true);
                }
            };

            MainActivity main = (MainActivity) getActivity();
            EventDispatcher.enviarEvento(getActivity(), new EventRequestBody(type, description), callback);

            btn_luz.setClickable(false);
            btn_luz.setEnabled(false);
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