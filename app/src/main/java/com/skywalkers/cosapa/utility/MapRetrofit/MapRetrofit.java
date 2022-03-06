package com.skywalkers.cosapa.utility.MapRetrofit;

import com.skywalkers.cosapa.models.mapSearch.SearchResult;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MapRetrofit {
    @GET("maps/api/place/textsearch/json")
    Call<SearchResult> getNearby(@Query(encoded = true, value = "key") String key, @Query(encoded = true, value = "query") String query, @Query(encoded = true, value = "location") String location, @Query(encoded = true, value = "radius") Long radius);
}
