package com.example.sendrecive.Models;

import android.util.Log;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {
    private static Retrofit ourInstance;


    public static Retrofit getInstance(String BASE_URL) {

        if (ourInstance == null)
            ourInstance = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();

        Log.e("ourInstance",""+ourInstance.toString());
        return ourInstance;
    }

    //
    private RetrofitInstance() {

    }
}
