package com.example.soa2020ea3;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.soa2020ea3.model.Gif;
import com.example.soa2020ea3.model.GifSearchResponse;
import com.example.soa2020ea3.services.GifsService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.example.soa2020ea3.network.RetrofitInstance.getRetrofitInstance;

public class GifsFragment extends Fragment {
    private EditText et_query;
    private Button btn_search_gifs;
    private TextView txt_prueba;

    public GifsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_gifs, container, false);

        et_query = (EditText) rootView.findViewById(R.id.et_gif_query);
        btn_search_gifs = (Button) rootView.findViewById(R.id.btn_search_gifs);
        txt_prueba = (TextView) rootView.findViewById(R.id.txt_prueba);

        btn_search_gifs.setOnClickListener((val) -> {
            if (et_query.getText().toString() != "") {
                buscarGifs(et_query.getText().toString());
                Toast.makeText(getActivity(),"Buscando...",Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }

    private void buscarGifs(String query) {
        Retrofit retrofit = getRetrofitInstance("https://api.giphy.com/v1/");
        GifsService gifsService = retrofit.create(GifsService.class);

        Call<GifSearchResponse> call = gifsService.getGifs(query, "6rg6Inygj83MvK6971NWzkhVshHgYPa5", "5", "en");
        call.enqueue(new Callback<GifSearchResponse>() {
            @Override
            public void onResponse(Call<GifSearchResponse> call, Response<GifSearchResponse> response) {
                Toast.makeText(getActivity(),"Llegaron los gifs?",Toast.LENGTH_SHORT).show();

                ArrayList<Gif> gifs = (ArrayList<Gif>) response.body().getGifs();
                txt_prueba.setText("title: " + gifs.get(0).getTitle() + ", url: " + gifs.get(0).getImage().getMedium().getUrl());
            }

            @Override
            public void onFailure(Call<GifSearchResponse> call, Throwable t) {
                Toast.makeText(getActivity(),"Explotó todo",Toast.LENGTH_SHORT).show();

            }
        });
    }
}