package com.komsi.lab.kjurseller.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    //private static final String BASE_URL_API = "http://10.33.35.204/k-jur/public/api/penjual/";
    //private static final String BASE_URL_API = "http://10.0.2.2:8000/api/penjual/";
    //private static final String BASE_URL_API = "http://k-jur.apotech.id/api/penjual/";
    private static final String BASE_URL_API = "http://10.33.70.59/k-jur/public/api/penjual/";
    private static RetrofitClient mInstance;
    private Retrofit retrofit;

    Gson gson = new GsonBuilder()
            .setLenient()
            .create();

    private RetrofitClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_API)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    public static synchronized RetrofitClient getInstance() {
        if (mInstance == null) {
            mInstance = new RetrofitClient();
        }
        return mInstance;
    }

    public BaseApiService getApi() {
        return retrofit.create(BaseApiService.class);
    }

}
