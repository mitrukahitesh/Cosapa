package com.skywalkers.cosapa.utility;

import com.skywalkers.cosapa.models.BecknRequestBody;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitAccessObject {
    private static RetrofitCustom retrofitCustom;
    public static final String BASE_URL = "http://13.235.139.60/sandbox/";
    private static BecknRequestBody bodyStore, bodyLab, bodyDoctor, bodyDoctorSelect, bodyDoctorConfirm, bodyDoctorStatus;

    private RetrofitAccessObject() {
    }

    public static RetrofitCustom getRetrofitAccessObject() {
        if (retrofitCustom == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            retrofitCustom = retrofit.create(RetrofitCustom.class);
        }
        return retrofitCustom;
    }

    public static BecknRequestBody getBodyStore() {
        if (bodyStore == null) {
            bodyStore = new BecknRequestBody();
            bodyStore.setDomain("healthcare-pharmacy");
            bodyStore.setUseCase("on_search/sending_a_list_of_pharmacies");
            bodyStore.setTtl(1000);
        }
        return bodyStore;
    }

    public static BecknRequestBody getBodyLab() {
        if (bodyLab == null) {
            bodyLab = new BecknRequestBody();
            bodyLab.setDomain("healthcare-diagnostics");
            bodyLab.setUseCase("on_search/sending_a_list_of_diagnostic_service_options");
            bodyLab.setTtl(1000);
        }
        return bodyLab;
    }

    public static BecknRequestBody getBodyDoctor() {
        if (bodyDoctor == null) {
            bodyDoctor = new BecknRequestBody();
            bodyDoctor.setDomain("healthcare-consultation");
            bodyDoctor.setUseCase("on_search/sending_a_list_of_consultation_options");
            bodyDoctor.setTtl(1000);
        }
        return bodyDoctor;
    }

    public static BecknRequestBody getBodyDoctorSelect() {
        if (bodyDoctorSelect == null) {
            bodyDoctorSelect = new BecknRequestBody();
            bodyDoctorSelect.setDomain("healthcare-consultation");
            bodyDoctorSelect.setUseCase("on_select/sending_the_updated_quote");
            bodyDoctorSelect.setTtl(1000);
            bodyDoctorSelect.setBppUri("http://13.235.139.60/sandbox/bpp1");
            bodyDoctorSelect.setTransactionId("1239890342");
        }
        return bodyDoctorSelect;
    }

    public static BecknRequestBody getBodyDoctorConfirm() {
        if (bodyDoctorConfirm == null) {
            bodyDoctorConfirm = new BecknRequestBody();
            bodyDoctorConfirm.setDomain("healthcare-consultation");
            bodyDoctorConfirm.setUseCase("on_confirm/confirmation_of_a_prepaid_consultation_appointment");
            bodyDoctorConfirm.setTtl(1000);
            bodyDoctorConfirm.setBppUri("http://13.235.139.60/sandbox/bpp1");
            bodyDoctorConfirm.setTransactionId("1239890342");
        }
        return bodyDoctorConfirm;
    }

    public static BecknRequestBody getBodyDoctorStatus() {
        if (bodyDoctorStatus == null) {
            bodyDoctorStatus = new BecknRequestBody();
            bodyDoctorStatus.setDomain("healthcare-consultation");
            bodyDoctorStatus.setUseCase("on_status/sending_the_latest_status_of_an_ongoing_consultation");
            bodyDoctorStatus.setTtl(1000);
            bodyDoctorStatus.setBppUri("http://13.235.139.60/sandbox/bpp1");
            bodyDoctorStatus.setTransactionId("1239890342");
        }
        return bodyDoctorStatus;
    }
}
