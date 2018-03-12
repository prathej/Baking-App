package com.ravitheja.bakingapp.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ravitheja.bakingapp.Models.Ingredients;
import com.ravitheja.bakingapp.Models.RecipeNames;
import com.ravitheja.bakingapp.R;
import com.ravitheja.bakingapp.ReciepeDetailActivity;
import com.ravitheja.bakingapp.adapter.RecipeDetailAdapter;
import com.ravitheja.bakingapp.widget.UpdateBakingService;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeDetailFragment extends Fragment {

    private String[] mIngredients;
    private String[] mMeasure;
    private final String LOG_TAG = RecipeDetailFragment.class.getSimpleName();
    private static final String SELECTED_RECIPE = "Selected_recipe";

    @BindView(R.id.recipeDetail)
    RecyclerView mRecyclerView;

     @BindView(R.id.textView1)
    TextView mTextView1;


    ArrayList<RecipeNames> mRecipe;
    private String mRecipeName;
    private RecipeDetailAdapter mAdapter;


    public RecipeDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
          ButterKnife.bind(this,rootView);

        ArrayList<String> recipeIngredientsForWidgets= new ArrayList<>();

        mRecipe = new ArrayList<>();


        if (savedInstanceState != null) {
            mRecipe = savedInstanceState.getParcelableArrayList(SELECTED_RECIPE);

        } else {
            mRecipe = getArguments().getParcelableArrayList(SELECTED_RECIPE);
        }
        mRecipeName = mRecipe.get(0).getName();
        List<Ingredients> ingredients = mRecipe.get(0).getIngredients();



        ingredients.forEach((a) ->
        { /* for (int i = 0; i < ingredients.size(); i++) {
            mTextView1.setText((ingredients.get(i).getIngredient()));

        }
        for (int i = 0; i < ingredients.size(); i++) {
            mTextView2.setText((ingredients.get(i).getQuantity()) + " " + (ingredients.get(i).getMeasure()));
        }*/
           /* mTextView1.append((a.getIngredient()) + "\n\n" );

                mTextView2.append(a.getQuantity().toString() + " " + a.getMeasure() + "\n\n");*/

            mTextView1.append("\u2022 "+ a.getIngredient()+"\n");
            mTextView1.append("\t\t\t Quantity: "+a.getQuantity().toString()+"\t" + a.getMeasure() +"\n\n" );
           // mTextView1.append("\t\t\t Measure: "+a.getMeasure()+"\n\n");

            recipeIngredientsForWidgets.add(a.getIngredient()+"\n"+
                    "Quantity: "+a.getQuantity().toString()+"\n"+
                    "Measure: "+a.getMeasure()+"\n");

        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter = new RecipeDetailAdapter((ReciepeDetailActivity)getActivity());
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setStepsData(mRecipe,getContext());


        UpdateBakingService.startBakingService(getContext(),recipeIngredientsForWidgets);

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(SELECTED_RECIPE, mRecipe);
        outState.putString("Title", mRecipeName);
    }


}
