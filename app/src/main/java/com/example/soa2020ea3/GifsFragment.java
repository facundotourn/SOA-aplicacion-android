package com.example.soa2020ea3;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.example.soa2020ea3.model.EventRequestBody;
import com.example.soa2020ea3.model.EventResponse;
import com.example.soa2020ea3.model.Gif;
import com.example.soa2020ea3.model.GifSearchResponse;
import com.example.soa2020ea3.services.EventDispatcher;
import com.example.soa2020ea3.services.GifsService;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.util.ArrayList;
import java.util.Arrays;
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

        gif_results = new ArrayList<ImageView>();
        gif_results.add((ImageView) rootView.findViewById(R.id.gif_result_1));
        gif_results.add((ImageView) rootView.findViewById(R.id.gif_result_2));
        gif_results.add((ImageView) rootView.findViewById(R.id.gif_result_3));
        gif_results.add((ImageView) rootView.findViewById(R.id.gif_result_4));
        gif_results.add((ImageView) rootView.findViewById(R.id.gif_result_5));

        SharedPreferences preferences = getActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        Gif[] gifArray = new Gson().fromJson(preferences.getString("lastGifs", null), Gif[].class);
        if (gifArray != null) cargarGifs(Arrays.asList(gifArray));

        String lastQuery = preferences.getString("lastQuery", null);
        if (lastQuery != null) et_query.setText(lastQuery);

        btn_search_gifs.setOnClickListener((val) -> {
            if (!et_query.getText().toString().matches("") && !et_query.getText().toString().matches(lastQuery != null ? lastQuery : "")) {
                buscarGifs(et_query.getText().toString());
                btn_search_gifs.setEnabled(false);
                btn_search_gifs.setClickable(false);
                Toast.makeText(getActivity(),"Cargando...",Toast.LENGTH_SHORT).show();
                hideKeyboardFrom(getActivity(), this.getView());
            } else {
                Toast.makeText(getActivity(),"Ingrese un término para buscar gifs",Toast.LENGTH_SHORT).show();
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
                ArrayList<Gif> gifs = (ArrayList<Gif>) response.body().getGifs();

                saveObject(getActivity(), "lastGifs", gifs);
                saveString(getActivity(), "lastQuery", query);
                cargarGifs(gifs);

                Callback<EventResponse> callback = new Callback<EventResponse>() {
                    @Override
                    public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                    }
                    @Override
                    public void onFailure(Call<EventResponse> call, Throwable t) {
                    }
                };

                String type = "CONSULTA_GIFS";
                String description = "El usuario consulto gifs.";

                MainActivity main = (MainActivity) getActivity();
                EventDispatcher.enviarEvento(getActivity(), new EventRequestBody(type, description), callback);

                btn_search_gifs.setEnabled(true);
                btn_search_gifs.setClickable(true);
            }

            @Override
            public void onFailure(Call<GifSearchResponse> call, Throwable t) {
                Toast.makeText(getActivity(),"Ocurrió un error al intentar consultar los GIFs",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveObject(Context ctx, String key, Object obj) {
        SharedPreferences preferences = ctx.getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        String jsonGifs = new Gson().toJson(obj);
        editor.putString(key, jsonGifs);
        editor.commit();
    }

    private void saveString(Context ctx, String key, String str) {
        SharedPreferences preferences = ctx.getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, str);
        editor.commit();
    }

    public void cargarGifs(List<Gif> gifs) {
        for (int i = 0; i < gifs.size(); i++) {
            String url = gifs.get(i).getImage().getMedium().getUrl();

            Glide.with(getActivity())
                    .load(url)
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                    .into(gif_results.get(i));
        }
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}