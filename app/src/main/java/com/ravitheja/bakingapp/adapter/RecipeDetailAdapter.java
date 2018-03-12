package com.ravitheja.bakingapp.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ravitheja.bakingapp.Models.RecipeNames;
import com.ravitheja.bakingapp.Models.Step;
import com.ravitheja.bakingapp.R;

import java.util.List;


public class RecipeDetailAdapter extends RecyclerView.Adapter<RecipeDetailAdapter.RecyclerViewHolder> {

    private List<Step> mSteps;
    private  String recipeName;
    private Context mContext;

    private onStepClickListener mListener;

    public void setStepsData(List<RecipeNames> recipe, Context context) {
        mSteps = recipe.get(0).getSteps();
        mContext = context;

        recipeName = recipe.get(0).getName();
        notifyDataSetChanged();

    }

    public  interface onStepClickListener{
        public void onStepClick(List<Step> stepsOut,int clickedItemIndex,String recipeName);
    }

    public RecipeDetailAdapter (onStepClickListener listener){
        mListener = listener;
    }



    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cv = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_detail, parent, false);
        return new RecyclerViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder,final int position) {

        CardView cardView = holder.cardView;
        TextView textView = (TextView) cardView.findViewById(R.id.detailTextView);
        textView.setText( mSteps.get(position).getShortDescription());
    }

    @Override
    public int getItemCount() {
        return  mSteps != null ? mSteps.size():0;
    }


    public  class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private CardView cardView;
        public RecyclerViewHolder(CardView v){
            super(v);
            cardView=v;

            cardView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mListener.onStepClick(mSteps,clickedPosition,recipeName);
        }
    }
}
