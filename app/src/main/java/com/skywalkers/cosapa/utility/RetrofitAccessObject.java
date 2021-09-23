package com.skywalkers.cosapa.utility;

import com.skywalkers.cosapa.models.BecknRequestBody;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitAccessObject {
    private static RetrofitCustom retrofitCustom;
    public static final String BASE_URL = "http://13.235.139.60/sandbox/";
    private static BecknRequestBody bodyStore, bodyLab, bodyDoctor;

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
}
