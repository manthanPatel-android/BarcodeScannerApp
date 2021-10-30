package com.vasyERP.example.barcodescannerapp;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface Users {


    @GET("products")
//    Call<Barcodescan_response> barcodescanAPicall(@Path("barcode") String barcode,@Path("key") String key);
    Call<Barcodescan_response> barcodescanAPicall(@QueryMap(encoded=true) Map<String, String> callRang);

}
