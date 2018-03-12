package com.ravitheja.bakingapp;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.ravitheja.bakingapp.Models.RecipeNames;
import com.ravitheja.bakingapp.Models.Step;
import com.ravitheja.bakingapp.adapter.RecipeDetailAdapter;
import com.ravitheja.bakingapp.fragments.RecipeDetailFragment;
import com.ravitheja.bakingapp.fragments.RecipeDetailVideoFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ReciepeDetailActivity extends AppCompatActivity implements RecipeDetailAdapter.onStepClickListener,
        RecipeDetailVideoFragment.ListItemClickListener {

    @BindView(R.id.recipeFragmentRecycler)
    RecyclerView mRecyclerView;

    private ArrayList<RecipeNames> mRecipe;
    private static final String SELECTED_RECIPE = "Selected_recipe";
    static String SELECTED_INDEX = "Selected_Index";
    static String SELECTED_STEPS = "Selected_Steps";
    static String STACK_RECIPE_DETAIL = "STACK_RECIPE_DETAIL";
    static String STACK_RECIPE_STEP_DETAIL = "STACK_RECIPE_STEP_DETAIL";
    public String mRecipeName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reciepe_detail);

        if (savedInstanceState == null) {

            Bundle selectedRecipeBundle = getIntent().getExtras();

            mRecipe = new ArrayList<>();
            mRecipe = selectedRecipeBundle.getParcelableArrayList(SELECTED_RECIPE);
            mRecipeName = mRecipe.get(0).getName();

            RecipeDetailFragment frag = new RecipeDetailFragment();
            frag.setArguments(selectedRecipeBundle);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container1, frag)
                    .addToBackStack(STACK_RECIPE_DETAIL)
                    .commit();

           if (findViewById(R.id.recipeDetailActivity_Linear).getTag()!=null &&
                    findViewById(R.id.recipeDetailActivity_Linear).getTag().equals("tablet-port")) {

                RecipeDetailVideoFragment fragment2 = new RecipeDetailVideoFragment();
                fragment2.setArguments(selectedRecipeBundle);
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container2, fragment2).addToBackStack(STACK_RECIPE_STEP_DETAIL)
                        .commit();
            }


        }else{
            mRecipeName=savedInstanceState.getString("Title");
        }

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(mRecipeName);
    }

    @Override
    public void onStepClick(List<Step> stepsOut, int clickedItemIndex, String recipeName) {

        RecipeDetailVideoFragment fragment = new RecipeDetailVideoFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();

        getSupportActionBar().setTitle(recipeName);

        Bundle stepBundle = new Bundle();
        stepBundle.putParcelableArrayList(SELECTED_STEPS, (ArrayList<Step>) stepsOut);
        stepBundle.putInt(SELECTED_INDEX, clickedItemIndex);
        stepBundle.putString("Title", recipeName);
        fragment.setArguments(stepBundle);

       if (findViewById(R.id.recipeDetailActivity_Linear).getTag()!=null
                && findViewById(R.id.recipeDetailActivity_Linear).getTag().equals("tablet-port")) {
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container2, fragment).addToBackStack(STACK_RECIPE_STEP_DETAIL)
                    .commit();

        }else {
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container1, fragment)
                    .addToBackStack(STACK_RECIPE_STEP_DETAIL)
                    .commit();
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        int count = fm.getBackStackEntryCount();
        // if (findViewById(R.id.fragment_container2)==null) {

        if (count > 1) {
            //go back to "Recipe Detail" screen
            fm.popBackStack(STACK_RECIPE_DETAIL, 0);
        } else {
            //go back to "Recipe" screen
            finish();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putString("Title", mRecipeName);
    }
}