package com.ravitheja.bakingapp;

import com.ravitheja.bakingapp.Models.RecipeNames;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;


public interface GetReciepeApiInterface {

    @GET("baking.json")
    Call<ArrayList<RecipeNames>> getAllRecipe();
}


