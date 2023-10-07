package com.example.logintaller.services;

import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.example.logintaller.models.apiModels.SearchResponse;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TmbdService {
    final static String LANG_CODE="es-CO";
    final static String BASE_URL="https://api.themoviedb.org/3/";
    final static String BASE_URL_IMAGE="https://image.tmdb.org/t/p/w780";
    private final TmbdApi tmbdApi;
    //private final TmbdApi tmbdImage;
    public TmbdService() {

        tmbdApi = new Retrofit.Builder()
                .baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build().create(TmbdApi.class);
        //tmbdImage = new Retrofit.Builder().baseUrl(BASE_URL_IMAGE).build().create(TmbdApi.class);
    }

    public Call<SearchResponse> searchMedias(String query, int page){
        return tmbdApi.searchMovies(query,page,LANG_CODE);
    }

    public static GlideUrl getGlideUrl(String url){
       return new GlideUrl(BASE_URL_IMAGE+url,new LazyHeaders.Builder()
                .addHeader("Authorization",TmbdApi.AUTH_TOKEN)
                .build());
    }
}
