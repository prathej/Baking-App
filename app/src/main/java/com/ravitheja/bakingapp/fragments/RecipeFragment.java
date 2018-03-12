package com.ravitheja.bakingapp.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ravitheja.bakingapp.GetReciepeApiInterface;
import com.ravitheja.bakingapp.IdlingResource.SimpleIdlingResource;
import com.ravitheja.bakingapp.MainActivity;
import com.ravitheja.bakingapp.Models.RecipeNames;
import com.ravitheja.bakingapp.R;
import com.ravitheja.bakingapp.RecipeApiClient;
import com.ravitheja.bakingapp.adapter.RecipeAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeFragment extends Fragment{

    private final String LOG_TAG = RecipeFragment.class.getSimpleName();
    static String ALL_RECIPES="All_Recipes";



    //@BindView(R.id.recipeFragmentRecycler)
    RecyclerView mRecyclerView;

    GetReciepeApiInterface mClient;

    RecipeAdapter mAdapter;



    public RecipeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_recipe, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recipeFragmentRecycler);
       // ButterKnife.bind(this,mRecyclerView);

        mClient = RecipeApiClient.getClient().create(GetReciepeApiInterface.class);

        Call<ArrayList<RecipeNames>> call = mClient.getAllRecipe();

        SimpleIdlingResource idlingResource = (SimpleIdlingResource)((MainActivity)getActivity()).getIdlingResource();

        if (idlingResource != null) {
            idlingResource.setIdleState(false);
        }

        call.enqueue(new Callback<ArrayList<RecipeNames>>() {
            @Override
            public void onResponse(Call<ArrayList<RecipeNames>> call, Response<ArrayList<RecipeNames>> response) {
                try {
                    Integer statusCode = response.code();
                    Timber.d("status code: " +  statusCode.toString());

                    Timber.d(  "response URL is " + response.toString() );

                    ArrayList<RecipeNames> recipeData = response.body();


                    Bundle recipesBundle = new Bundle();
                    recipesBundle.putParcelableArrayList(ALL_RECIPES, recipeData);

                    mAdapter = new RecipeAdapter((MainActivity)getActivity());
                    mAdapter.setRecipeData (recipeData,getContext());
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                    mRecyclerView.setLayoutManager(layoutManager);
                    mRecyclerView.setAdapter(mAdapter);

                    if (idlingResource != null) {
                        idlingResource.setIdleState(true);
                    }

                    }catch (Exception e) {
                    //Log.d("onResponse", "There is an error");
                    Timber.d("error in onResponse");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<RecipeNames>> call, Throwable t) {
                Timber.d("couldn't make the connection");
            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }

}
