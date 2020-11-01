package com.example.soa2020ea3.services;

import com.example.soa2020ea3.model.EventRequestBody;
import com.example.soa2020ea3.model.EventResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface EventService {
    @POST("event")
    Call<EventResponse> postEvent(@Body EventRequestBody eventRequestBody, @Header("Authorization") String authorization);
}