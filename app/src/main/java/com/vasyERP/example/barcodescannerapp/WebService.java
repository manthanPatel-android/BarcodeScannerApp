package com.vasyERP.example.barcodescannerapp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class WebService {

//    static Gson gson = new GsonBuilder()
//            .setLenient()
//            .create();
    static Retrofit restAdapter = new Retrofit.Builder().baseUrl("https://api.barcodelookup.com/v3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private static Users User;

    public static Users users() {
        if (User==null)
            if (User == null) User = restAdapter.create(Users.class);
        return User;
    }
}
