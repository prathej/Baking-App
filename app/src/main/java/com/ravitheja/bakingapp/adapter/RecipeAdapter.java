package com.ravitheja.bakingapp.adapter;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ravitheja.bakingapp.Models.RecipeNames;
import com.ravitheja.bakingapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import timber.log.Timber;


public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecyclerViewHolder> {

    private  Context context;
    private  ArrayList<RecipeNames> captions;

     private ListItemClickListener mOnClickListener;

    public interface ListItemClickListener {
        void onListItemClick(RecipeNames clickedItemIndex);
    }


    public void setRecipeData(ArrayList<RecipeNames> captions, Context context ){
        this.captions= captions;
        this.context = context;
    }

    public RecipeAdapter(ListItemClickListener listener){
        mOnClickListener=listener;
    }


    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        CardView cv = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_image, parent, false);
        return new RecyclerViewHolder(cv);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(RecipeAdapter.RecyclerViewHolder holder, final int position) {
        CardView cardView = holder.cardView;
        ImageView imageView = cardView.findViewById(R.id.info_image);

        String imageUrl=captions.get(position).getImage();

        if (imageUrl!="") {
            Uri builtUri = Uri.parse(imageUrl).buildUpon().build();
            Picasso.with(context).load(builtUri).into(imageView);
        }


        TextView textView = cardView.findViewById(R.id.info_text);
        textView.setText(captions.get(position).getName());
    }

    @Override
    public int getItemCount() {

        return captions !=null ? captions.size():0 ;
    }

     class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private CardView cardView;
        public RecyclerViewHolder(CardView v){
            super(v);
            cardView=v;

          cardView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            Timber.d("clicked position = " + clickedPosition);
            mOnClickListener.onListItemClick(captions.get(clickedPosition));
        }
    }
}
