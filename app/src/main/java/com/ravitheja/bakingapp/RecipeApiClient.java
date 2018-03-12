package com.ravitheja.bakingapp;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;


public class RecipeApiClient {
   private final static String API_BASE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";
    private static final String LOG_TAG = RecipeApiClient.class.getSimpleName();

    public static Retrofit getClient() {

        Gson gson = new GsonBuilder().create();

        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .callFactory(httpClientBuilder.build())
                .build();

        Timber.d( LOG_TAG + "url formed is  ",retrofit );
        Log.d(LOG_TAG,"url formed is " + retrofit);

        return retrofit;



    }


}
