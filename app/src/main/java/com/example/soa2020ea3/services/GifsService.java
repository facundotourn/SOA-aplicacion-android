package com.example.soa2020ea3.services;

import com.example.soa2020ea3.model.Gif;
import com.example.soa2020ea3.model.GifSearchResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GifsService {
    @GET("gifs/search")
    Call<GifSearchResponse> getGifs(@Query("q") String query, @Query("api_key") String api_key, @Query("limit") String limit, @Query("lang") String lang);
}
