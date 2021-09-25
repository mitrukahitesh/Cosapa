package com.skywalkers.cosapa.utility;

import com.skywalkers.cosapa.models.doctor.Doctor;
import com.skywalkers.cosapa.models.doctorConfirm.DoctorConfirm;
import com.skywalkers.cosapa.models.doctorSelect.DoctorSelect;
import com.skywalkers.cosapa.models.doctorStatus.DoctorStatus;
import com.skywalkers.cosapa.models.lab.Lab;
import com.skywalkers.cosapa.models.BecknRequestBody;
import com.skywalkers.cosapa.models.store.Store;

import org.json.JSONObject;

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
    Call<ArrayList<Doctor>> getDoctorsByNameOfCategory(@Body BecknRequestBody obj);

    @POST("bap/trigger/select")
    Call<ArrayList<DoctorSelect>> selectDoctorService(@Body BecknRequestBody obj);

    @POST("bap/trigger/confirm")
    Call<ArrayList<DoctorConfirm>> confirmDoctorService(@Body BecknRequestBody obj);

    @POST("bap/trigger/status")
    Call<ArrayList<DoctorStatus>> checkDoctorBookingStatus(@Body BecknRequestBody obj);

}
