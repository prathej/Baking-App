package com.ravitheja.bakingapp.fragments;


import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.ravitheja.bakingapp.Models.RecipeNames;
import com.ravitheja.bakingapp.Models.Step;
import com.ravitheja.bakingapp.R;
import com.ravitheja.bakingapp.ReciepeDetailActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RecipeDetailVideoFragment extends Fragment {

    private static final java.lang.String SELECTED_POSITION = "position";
    private static final String AUTO_PLAY = "auto_play" ;
    @BindView (R.id.descrTextView)
     TextView mStepsTextView;

    private Button  mNextButton;
    private Button mPreviousButton;


    private SimpleExoPlayer mPlayer;

    @BindView(R.id.playerView)
     SimpleExoPlayerView simpleExoPlayerView;

    private BandwidthMeter mBandwidthMeter;

    private Handler mainHandler;

    private ArrayList<Step> mSteps = new ArrayList<>();
    private int mSelectedIndex;

    ArrayList<RecipeNames> mRecipe = new ArrayList<>();
    String mRecipeName;

    private ReciepeDetailActivity itemClickListener;
    private long mResumePosition;
    private String mVideoURL;
    private boolean mAutoPlay;


    public interface ListItemClickListener{
        void onStepClick(List<Step> allSteps, int Index, String recipeName);
    }

    private static final String SELECTED_RECIPE = "Selected_recipe";
    static String SELECTED_INDEX = "Selected_Index";
    static String SELECTED_STEPS = "Selected_Steps";



    public RecipeDetailVideoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_recipe_detail_video, container, false);
        ButterKnife.bind(this,rootView);

        mNextButton = (Button) rootView.findViewById(R.id.next_button);
        mPreviousButton = (Button) rootView.findViewById(R.id.previous_button);

        itemClickListener =(ReciepeDetailActivity)getActivity();
        mainHandler = new Handler();
        mResumePosition= C.TIME_UNSET;
        mAutoPlay=true;

        if(savedInstanceState != null) {
            mSteps = savedInstanceState.getParcelableArrayList(SELECTED_STEPS);
            mSelectedIndex = savedInstanceState.getInt(SELECTED_INDEX);
            mRecipeName = savedInstanceState.getString("Title");
            mResumePosition = savedInstanceState.getLong(SELECTED_POSITION, C.TIME_UNSET);
            mAutoPlay = savedInstanceState.getBoolean(AUTO_PLAY);
        }

        else {
            mSteps =getArguments().getParcelableArrayList(SELECTED_STEPS);
            if (mSteps!=null) {
                mSteps =getArguments().getParcelableArrayList(SELECTED_STEPS);
                mSelectedIndex=getArguments().getInt(SELECTED_INDEX);
                mRecipeName=getArguments().getString("Title");
            }
            else {
                mRecipe =getArguments().getParcelableArrayList(SELECTED_RECIPE);
                //casting List to ArrayList
                mSteps=(ArrayList<Step>)mRecipe.get(0).getSteps();
                mSelectedIndex=0;
            }

        }

        mStepsTextView.setText(mSteps.get(mSelectedIndex).getDescription());
        mStepsTextView.setVisibility(View.VISIBLE);

     if(rootView.findViewWithTag("phone_port")!=null) {


         mPreviousButton.setOnClickListener(new View.OnClickListener() {
             public void onClick(View view) {
                 if (mSteps.get(mSelectedIndex).getId() > 0) {
                 /*   if (player!=null){
                        player.stop();
                    }*/
                     itemClickListener.onStepClick(mSteps,
                             mSteps.get(mSelectedIndex).getId() - 1,
                             mRecipeName);
                 } else {
                     Toast.makeText(getActivity(), "You are in the First step of the recipe",
                             Toast.LENGTH_SHORT).show();

                 }
             }
         });

         mNextButton.setOnClickListener(new View.OnClickListener() {
             public void onClick(View view) {

                 int lastIndex = mSteps.size() - 1;
                 if (mSteps.get(mSelectedIndex).getId() < mSteps.get(lastIndex).getId()) {
                  /*  if (player!=null){
                        player.stop();
                    }*/
                     itemClickListener.onStepClick(mSteps,
                             mSteps.get(mSelectedIndex).getId() + 1,
                             mRecipeName);
                 } else {
                     Toast.makeText(getContext(), "You already are in the Last step of the recipe",
                             Toast.LENGTH_SHORT).show();

                 }
             }
         });
     }

        //simpleExoPlayerView = (SimpleExoPlayerView) rootView.findViewById(R.id.playerView);
        simpleExoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);

       // String videoURL = mSteps.get(mSelectedIndex).getVideoURL();
         mVideoURL = mSteps.get(mSelectedIndex).getVideoURL();

     /*   if (rootView.findViewWithTag("sw600dp-port-recipe_step_detail")!=null) {
            mRecipeName=((ReciepeDetailActivity) getActivity()).mRecipeName;
            ((ReciepeDetailActivity) getActivity()).getSupportActionBar().setTitle(mRecipeName);
        }*/

        String imageUrl=mSteps.get(mSelectedIndex).getThumbnailURL();
        if (imageUrl!="") {
            Uri builtUri = Uri.parse(imageUrl).buildUpon().build();
            ImageView thumbImage = (ImageView) rootView.findViewById(R.id.thumbImage);
            Picasso.with(getContext()).load(builtUri).into(thumbImage);
        }

        if (!mVideoURL.isEmpty()) {


            initializePlayer(Uri.parse(mVideoURL));

          /*  if (rootView.findViewWithTag("sw600dp-land-recipe_step_detail")!=null) {
                getActivity().findViewById(R.id.fragment_container2).setLayoutParams(new LinearLayout.LayoutParams(-1,-2));
                simpleExoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH);
            }
            else */
                if (isInLandscapeMode(getContext())){
                mStepsTextView.setVisibility(View.GONE);

                View decorView = getActivity().getWindow().getDecorView();
                int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN;
                decorView.setSystemUiVisibility(uiOptions);

                ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
            }
        }
        else {
            mPlayer=null;
            simpleExoPlayerView.setForeground(ContextCompat.getDrawable(getContext(), R.drawable.ic_visibility_off_white_36dp));
            simpleExoPlayerView.setLayoutParams(new LinearLayout.LayoutParams(300, 300));
        }

        return rootView;
    }

    private void initializePlayer(Uri mediaUri) {
        if (mPlayer == null) {
            TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveVideoTrackSelection.Factory(mBandwidthMeter);
            DefaultTrackSelector trackSelector = new DefaultTrackSelector(mainHandler, videoTrackSelectionFactory);
            LoadControl loadControl = new DefaultLoadControl();

            mPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            simpleExoPlayerView.setPlayer(mPlayer);

            String userAgent = Util.getUserAgent(getContext(), "Baking App");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri,
                    new DefaultDataSourceFactory(getContext(), userAgent),
                    new DefaultExtractorsFactory(), null, null);
            if (mResumePosition != C.TIME_UNSET) {
                mPlayer.seekTo(mResumePosition);
            }
            mPlayer.prepare(mediaSource);
            mPlayer.setPlayWhenReady(mAutoPlay);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle currentState) {
        super.onSaveInstanceState(currentState);
        currentState.putParcelableArrayList(SELECTED_STEPS,mSteps);
        currentState.putInt(SELECTED_INDEX,mSelectedIndex);
        currentState.putString("Title",mRecipeName);
        currentState.putLong(SELECTED_POSITION, mResumePosition);
        currentState.putBoolean(AUTO_PLAY, mAutoPlay);

    }

    public boolean isInLandscapeMode( Context context ) {
        return (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mPlayer!=null) {
            mPlayer.stop();
            mPlayer.release();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mPlayer!=null) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer=null;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mPlayer!=null) {
            mPlayer.stop();
            mPlayer.release();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mPlayer!=null) {
            mResumePosition= mPlayer.getCurrentPosition();
            mAutoPlay=mPlayer.getPlayWhenReady();
            mPlayer.stop();
            mPlayer.release();
            mPlayer=null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mVideoURL != null)
            initializePlayer(Uri.parse(mVideoURL));
    }

}
