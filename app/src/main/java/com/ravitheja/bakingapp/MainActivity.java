package com.ravitheja.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;

import com.ravitheja.bakingapp.IdlingResource.SimpleIdlingResource;
import com.ravitheja.bakingapp.Models.RecipeNames;
import com.ravitheja.bakingapp.adapter.RecipeAdapter;

import java.util.ArrayList;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements RecipeAdapter.ListItemClickListener {

    private static  final String SELECTED_RECIPE = "Selected_recipe";


    @Nullable
    private SimpleIdlingResource mIdlingResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Timber.plant(new Timber.DebugTree());


    }

    @Override
    public void onListItemClick(RecipeNames clickedItemIndex) {
        Bundle selectedRecipe = new Bundle();
        ArrayList<RecipeNames> selectedRecipeArray = new ArrayList<>();
        selectedRecipeArray.add(clickedItemIndex);
        selectedRecipe.putParcelableArrayList(SELECTED_RECIPE,selectedRecipeArray);

        Intent intent = new Intent(this, ReciepeDetailActivity.class);
        intent.putExtras(selectedRecipe);
        startActivity(intent);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    /**
     * Only called from test, creates and returns a new {@link SimpleIdlingResource}.
     */
    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }
}
