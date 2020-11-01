package com.example.soa2020ea3;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.soa2020ea3.model.Gif;
import com.example.soa2020ea3.model.GifSearchResponse;
import com.example.soa2020ea3.services.GifsService;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

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
    private ArrayList<ImageView> gif_results;

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

        gif_results = new ArrayList<ImageView>();
        gif_results.add((ImageView) rootView.findViewById(R.id.gif_result_1));
        gif_results.add((ImageView) rootView.findViewById(R.id.gif_result_2));
        gif_results.add((ImageView) rootView.findViewById(R.id.gif_result_3));
        gif_results.add((ImageView) rootView.findViewById(R.id.gif_result_4));
        gif_results.add((ImageView) rootView.findViewById(R.id.gif_result_5));

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

        Call<GifSearchResponse> call = gifsService.getGifs(query, "6rg6Inygj83MvK6971NWzkhVshHgYPa5", "5", "es");
        call.enqueue(new Callback<GifSearchResponse>() {
            @Override
            public void onResponse(Call<GifSearchResponse> call, Response<GifSearchResponse> response) {
                Toast.makeText(getActivity(),"Mostrando los 5 gifs más populares",Toast.LENGTH_SHORT).show();

                ArrayList<Gif> gifs = (ArrayList<Gif>) response.body().getGifs();

                for (int i = 0; i < gifs.size(); i++) {
                    String url = gifs.get(i).getImage().getMedium().getUrl();

                    Glide.with(getActivity())
                            .load(url)
                            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                            .into(gif_results.get(i));
                }
            }

            @Override
            public void onFailure(Call<GifSearchResponse> call, Throwable t) {
                Toast.makeText(getActivity(),"Explotó todo",Toast.LENGTH_SHORT).show();

            }
        });
    }
}