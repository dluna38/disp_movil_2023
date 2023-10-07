package com.example.logintaller.models.apiModels;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchResponse {
    public int page;
    @SerializedName(value = "total_pages")
    public int totalPages;
    public List<Pelicula> results;
}
