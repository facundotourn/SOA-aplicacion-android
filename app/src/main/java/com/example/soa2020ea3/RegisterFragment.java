package com.example.soa2020ea3;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.soa2020ea3.model.AuthTokens;
import com.example.soa2020ea3.model.EventRequestBody;
import com.example.soa2020ea3.model.EventResponse;
import com.example.soa2020ea3.model.RegisterRequestBody;
import com.example.soa2020ea3.model.Usuario;
import com.example.soa2020ea3.services.AuthService;
import com.example.soa2020ea3.services.EventDispatcher;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.example.soa2020ea3.network.RetrofitInstance.getRetrofitInstance;

public class RegisterFragment extends Fragment {
    private EditText et_name;
    private EditText et_lastname;
    private EditText et_dni;
    private EditText et_commission;
    private EditText et_email;
    private EditText et_password;
    private Button register_button;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_register, container, false);

        this.et_name = (EditText) rootView.findViewById(R.id.et_name);
        this.et_lastname = (EditText) rootView.findViewById(R.id.et_lastname);
        this.et_dni = (EditText) rootView.findViewById(R.id.et_dni);
        this.et_commission = (EditText) rootView.findViewById(R.id.et_commission);
        this.et_email = (EditText) rootView.findViewById(R.id.et_email);
        this.et_password = (EditText) rootView.findViewById(R.id.et_password);
        this.register_button = (Button) rootView.findViewById(R.id.btn_register);

        register_button.setOnClickListener((val) -> {
            Usuario usr = new Usuario();
            usr.setName(et_name.getText().toString());
            usr.setLastname(et_lastname.getText().toString());
            usr.setDni(et_dni.getText().toString());
            usr.setCommission(Integer.valueOf(et_commission.getText().toString()));
            usr.setEmail(et_email.getText().toString());
            usr.setPassword(et_password.getText().toString());

            registrar(usr);
            Toast.makeText(getActivity(),"Registrando...",Toast.LENGTH_SHORT).show();

            register_button.setEnabled(false);
            register_button.setClickable(false);
        });

        return rootView;
    }

    public void registrar(Usuario nuevoUsuario) {
        Retrofit retrofit = getRetrofitInstance("http://so-unlam.net.ar/api/api/");
        AuthService authService = retrofit.create(AuthService.class);

        Call<AuthTokens> call = authService.register(new RegisterRequestBody(nuevoUsuario));
        call.enqueue(new Callback<AuthTokens>() {
            @Override
            public void onResponse(Call<AuthTokens> call, Response<AuthTokens> response) {
                if (response.body() != null) {
                    AuthTokens res = (AuthTokens) response.body();

                    Toast.makeText(getActivity(),"Se registró correctamente",Toast.LENGTH_SHORT).show();

                    SharedPreferences preferences = getActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("token", res.getToken());
                    editor.putString("refreshToken", res.getRefreshToken());
                    editor.apply();

                    enviarEvento("REGISTRO", "Se registro el nuevo usuario");

                    Intent home = new Intent(getActivity(), MainActivity.class);
                    startActivity(home);
                    getActivity().finish();
                }
                else
                    Toast.makeText(getActivity(),"Error en los datos del nuevo usuario",Toast.LENGTH_SHORT).show();

                register_button.setEnabled(true);
                register_button.setClickable(true);
            }

            @Override
            public void onFailure(Call<AuthTokens> call, Throwable t) {
                Toast.makeText(getActivity(),"Error interno del servidor",Toast.LENGTH_SHORT).show();

                register_button.setEnabled(true);
                register_button.setClickable(true);
            }
        });
    }

    public void enviarEvento(String type, String description) {
        AuthActivity authActivity = (AuthActivity) getActivity();
        authActivity.enviarEvento(type, description);
    }
}