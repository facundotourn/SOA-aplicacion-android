package com.example.soa2020ea3;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.soa2020ea3.model.AuthTokens;
import com.example.soa2020ea3.model.LoginRequestBody;
import com.example.soa2020ea3.services.AuthService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.example.soa2020ea3.network.RetrofitInstance.getRetrofitInstance;

public class LoginFragment extends Fragment {
    private EditText et_email;
    private EditText et_password;
    private Button login_button;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        this.et_email = (EditText) rootView.findViewById(R.id.et_login_email);
        this.et_password = (EditText) rootView.findViewById(R.id.et_login_password);
        this.login_button = (Button) rootView.findViewById(R.id.btn_login);

        login_button.setOnClickListener((val) -> {
            loguear(et_email.getText().toString(), et_password.getText().toString());
            Toast.makeText(getActivity(),"Ingresando...",Toast.LENGTH_SHORT).show();

            login_button.setClickable(false);
            login_button.setEnabled(false);
        });

        return rootView;
    }

    public void loguear(String email, String password) {
        Retrofit retrofit = getRetrofitInstance("http://so-unlam.net.ar/api/api/");
        AuthService authService = retrofit.create(AuthService.class);

        Call<AuthTokens> call = authService.login(new LoginRequestBody(email, password));
        call.enqueue(new Callback<AuthTokens>() {
            @Override
            public void onResponse(Call<AuthTokens> call, Response<AuthTokens> response) {
                if (response.body() != null) {
                    AuthTokens res = (AuthTokens) response.body();

                    Toast.makeText(getActivity(),"Se ingresó correctamente",Toast.LENGTH_SHORT).show();

                    SharedPreferences preferences = getActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("token", res.getToken());
                    editor.putString("refreshToken", res.getRefreshToken());
                    editor.apply();

                    enviarEvento("INGRESO", "El usuario inició sesión en su cuenta");

                    Intent home = new Intent(getActivity(), MainActivity.class);
                    startActivity(home);
                    getActivity().finish();
                } else
                    Toast.makeText(getActivity(),"Error en las credenciales",Toast.LENGTH_SHORT).show();

                login_button.setClickable(true);
                login_button.setEnabled(true);
            }

            @Override
            public void onFailure(Call<AuthTokens> call, Throwable t) {
                Toast.makeText(getActivity(),"Error interno del servidor",Toast.LENGTH_SHORT).show();

                login_button.setClickable(true);
                login_button.setEnabled(true);
            }
        });
    }

    public void enviarEvento(String type, String description) {
        AuthActivity authActivity = (AuthActivity) getActivity();
        authActivity.enviarEvento(type, description);
    }
}