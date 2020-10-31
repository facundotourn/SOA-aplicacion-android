package com.example.soa2020ea3.services;

import com.example.soa2020ea3.model.AuthTokens;
import com.example.soa2020ea3.model.LoginRequestBody;
import com.example.soa2020ea3.model.RegisterRequestBody;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthService {
    @POST("register")
    Call<AuthTokens> register(@Body RegisterRequestBody registerRequestBody);

    @POST("login")
    Call<AuthTokens> login(@Body LoginRequestBody loginRequestBody);
}
