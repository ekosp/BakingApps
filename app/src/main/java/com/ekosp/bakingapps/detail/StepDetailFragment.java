package com.ekosp.bakingapps.detail;

/**
 * Created by eko.purnomo on 07/08/2017.
 * You can contact me at : ekosetyopurnomo@gmail.com
 * or for more detail at  : http://ekosp.com
 */

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ekosp.bakingapps.DetailActivity;
import com.ekosp.bakingapps.R;
import com.ekosp.bakingapps.RecipeListActivity;
import com.ekosp.bakingapps.models.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepDetailFragment extends Fragment {

    View view;
    @Nullable
    @BindView(R.id.stepDescription)
    TextView mStepdescription;
    @Nullable
    @BindView(R.id.stepPosition)
    TextView mStepPos;
    public static String PARAM_LIST_STEP = "PARAM_LIST_STEP";
    public static String PARAM_DETAIL_STEP_ID = "PARAM_DETAIL_STEP_ID";
    ArrayList<Step> stepArrayList;
    int mStepId;
    @Nullable
    @BindView(R.id.nextStepButon)
    Button nextStepBtn;
    @Nullable
    @BindView(R.id.prevStepButon)
    Button prevStepBtn;
    private SimpleExoPlayer player;
    @Nullable
    @BindView(R.id.video_view)
    SimpleExoPlayerView playerView;

    private long playbackPosition;
    private int currentWindow;
    private boolean playWhenReady = true;
    private boolean mIsTablet ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIsTablet  = (getResources().getBoolean(R.bool.isTab)) ;

        if (getArguments().containsKey(StepDetailFragment.PARAM_LIST_STEP)) {
            stepArrayList = getArguments().getParcelableArrayList(StepDetailFragment.PARAM_LIST_STEP);
        }
        if (getArguments().containsKey(StepDetailFragment.PARAM_DETAIL_STEP_ID)) {
            mStepId = getArguments().getInt(StepDetailFragment.PARAM_DETAIL_STEP_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_step_detail, container, false);
        ButterKnife.bind(this, view);
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(PARAM_LIST_STEP))
                stepArrayList = savedInstanceState.getParcelableArrayList(PARAM_LIST_STEP);
            if (savedInstanceState.containsKey(PARAM_DETAIL_STEP_ID))
                mStepId = savedInstanceState.getInt(PARAM_DETAIL_STEP_ID);
        }

        getActivity().setTitle(stepArrayList.get(mStepId).getShortDescription());
        setToolbar();

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT || mIsTablet ){
            mStepdescription.setText(stepArrayList.get(mStepId).getDescription());
            nextStepBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    mStepId = mStepId +1;
                    openStep(mStepId, stepArrayList);
                }
            });
            prevStepBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    mStepId = mStepId -1;
                    openStep(mStepId, stepArrayList);
                }
            });

            showHideButton(mStepId, stepArrayList.size());

        } else {
            playerView = (SimpleExoPlayerView) view.findViewById(R.id.video_view);
        }

        // almost all exoplayer implementation code using
        // code from : https://codelabs.developers.google.com/codelabs/exoplayer-intro/
        initializePlayer(stepArrayList.get(mStepId).getVideoURL());

        return view;
    }

    private void setToolbar() {
        // get from : https://stackoverflow.com/questions/26998455/how-to-get-toolbar-from-fragment
        // and from:  https://freakycoder.com/android-notes-24-how-to-add-back-button-at-toolbar-941e6577418e
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.tool_bar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(PARAM_DETAIL_STEP_ID,mStepId);
        if (stepArrayList != null) outState.putParcelableArrayList(PARAM_LIST_STEP ,stepArrayList);
        outState.putBoolean("stepDetail", true);
    }

    public void openStep(int stepId, ArrayList<Step> arrayListStep ) {
        showHideButton(stepId, arrayListStep.size());
        releasePlayer();
        if (stepId > 0 || stepId < arrayListStep.size())
        mStepdescription.setText(arrayListStep.get(stepId).getDescription());
        initializePlayer(arrayListStep.get(stepId).getVideoURL());
    }

    private void showHideButton (int id, int size ){
        if (id == size-1) {
            nextStepBtn.setVisibility(View.INVISIBLE);
        } else if ( id == 0 ) {
            prevStepBtn.setVisibility(View.INVISIBLE);
        } else {
            nextStepBtn.setVisibility(View.VISIBLE);
            prevStepBtn.setVisibility(View.VISIBLE);
        }
        mStepPos.setText(id+"/"+(size-1));
    }


    private void initializePlayer(String videoURL) {
        // to avoid the player is initialized multiple times and leaving unreleased instances behind
        if (player == null) {
            player = ExoPlayerFactory.newSimpleInstance(
                    getActivity().getApplicationContext(),
                    new DefaultTrackSelector(), new DefaultLoadControl());
            playerView.setPlayer(player);
            player.setPlayWhenReady(playWhenReady);
            player.seekTo(currentWindow, playbackPosition);

            Uri uri = Uri.parse(videoURL);
            MediaSource mediaSource = buildMediaSource(uri);
            player.prepare(mediaSource, true, false);
        }

    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource(uri,
                new DefaultHttpDataSourceFactory("ua"),
                new DefaultExtractorsFactory(), null, null);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer(stepArrayList.get(mStepId).getVideoURL());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        hideSystemUi();
        if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer(stepArrayList.get(mStepId).getVideoURL());
        }
    }

    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    private void releasePlayer() {
        if (player != null) {
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();
            player.release();
            player = null;
        }
    }



}