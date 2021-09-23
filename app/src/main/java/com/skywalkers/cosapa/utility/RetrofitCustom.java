package com.skywalkers.cosapa.utility;

import com.skywalkers.cosapa.models.doctor.Doctor;
import com.skywalkers.cosapa.models.lab.Lab;
import com.skywalkers.cosapa.models.BecknRequestBody;
import com.skywalkers.cosapa.models.store.Store;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitCustom {

    @POST("bap/trigger/search")
    Call<ArrayList<Lab>> getLabsByNameOfTest(@Body BecknRequestBody obj);

    @POST("bap/trigger/search")
    Call<ArrayList<Store>> getStoresByNameOfMedicine(@Body BecknRequestBody obj);

    @POST("bap/trigger/search")
    Call<ArrayList<Doctor>> getDoctorsByNameOfSymptom(@Body BecknRequestBody obj);

}
