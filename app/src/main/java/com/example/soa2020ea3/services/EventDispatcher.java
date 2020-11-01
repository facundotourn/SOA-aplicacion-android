package com.example.soa2020ea3.services;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.soa2020ea3.model.EventRequestBody;
import com.example.soa2020ea3.model.EventResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

import static com.example.soa2020ea3.network.RetrofitInstance.getRetrofitInstance;

public class EventDispatcher {
    public static void enviarEvento(Context ctx, EventRequestBody requestBody, Callback<EventResponse> callback) {
        Retrofit retrofit = getRetrofitInstance("http://so-unlam.net.ar/api/api/");
        EventService eventService = retrofit.create(EventService.class);

        SharedPreferences preferences = ctx.getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        String token = preferences.getString("token",null);

        Call<EventResponse> call = eventService.postEvent(requestBody, "Bearer " + token);
        call.enqueue(callback);
    }
}
