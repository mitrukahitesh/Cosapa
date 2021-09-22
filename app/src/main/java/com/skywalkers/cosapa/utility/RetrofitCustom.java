package com.skywalkers.cosapa.utility;

import com.skywalkers.cosapa.models.lab.Lab;
import com.skywalkers.cosapa.models.lab.RequestBody;

import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitCustom {

    @POST("bap/trigger/search")
    Call<ArrayList<Lab>> getLabsByNameOfTest(@Body RequestBody obj);

}
