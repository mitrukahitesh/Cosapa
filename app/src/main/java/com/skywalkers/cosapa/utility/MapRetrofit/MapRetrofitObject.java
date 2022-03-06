package com.skywalkers.cosapa.utility.MapRetrofit;

import com.skywalkers.cosapa.models.BecknRequestBody;
import com.skywalkers.cosapa.utility.RetrofitCustom;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapRetrofitObject {
    private static MapRetrofit retrofitCustom;
    public static final String BASE_URL = "https://maps.googleapis.com/";

    private MapRetrofitObject() {
    }

    public static MapRetrofit getRetrofitAccessObject() {
        if (retrofitCustom == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            retrofitCustom = retrofit.create(MapRetrofit.class);
        }
        return retrofitCustom;
    }
}
