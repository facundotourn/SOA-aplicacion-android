package com.example.soa2020ea3.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class GifSearchResponse {
    @SerializedName("data")
    private List<Gif> gifs;

    public GifSearchResponse(List<Gif> gifs) {
        this.gifs = gifs;
    }

    public List<Gif> getGifs() {
        return gifs;
    }

    public void setGifs(List<Gif> gifs) {
        this.gifs = gifs;
    }
}
