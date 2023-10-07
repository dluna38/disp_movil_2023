package com.example.logintaller.services;

import com.example.logintaller.models.apiModels.SearchResponse;
import com.example.logintaller.utils.MyEnv;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TmbdApi {
    String AUTH_TOKEN= MyEnv.BEARER_TOKEN;
    @Headers({AUTH_TOKEN})
    @GET("search/multi")
    Call<SearchResponse> searchMovies(@Query("query") String query, @Query("page") int page,@Query("language") String language);
    //w780
    @Headers({AUTH_TOKEN})
    @GET("{size}/{uri}")
    Call<SearchResponse> getImage(@Path("size")String size,@Path("uri") String uri);

}
