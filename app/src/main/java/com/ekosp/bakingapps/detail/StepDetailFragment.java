package com.ekosp.bakingapps.detail;

/**
 * Created by eko.purnomo on 07/08/2017.
 * You can contact me at : ekosetyopurnomo@gmail.com
 * or for more detail at  : http://ekosp.com
 */

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ekosp.bakingapps.R;
import com.ekosp.bakingapps.models.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
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

public class StepDetailFragment extends Fragment implements android.support.v4.app.FragmentManager.OnBackStackChangedListener {

    View view;
    TextView mStepdescription, mStepPos;
    public static String PARAM_TAG_FRAGMENNT_STEP_DETAIL =  "TAG_STEP_DETAIL";
    public static String PARAM_LIST_STEP = "PARAM_LIST_STEP";
    public static String PARAM_DETAIL_STEP_ID = "PARAM_DETAIL_STEP_ID";
    ArrayList<Step> stepArrayList;
    int mStepId;// id ;
    Button nextStepBtn, prevStepBtn;
    private SimpleExoPlayer player;
    private SimpleExoPlayerView playerView;

    private long playbackPosition;
    private int currentWindow;
    private boolean playWhenReady = true;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(StepDetailFragment.PARAM_LIST_STEP)) {
            stepArrayList = getArguments().getParcelableArrayList(StepDetailFragment.PARAM_LIST_STEP);
          //  Log.i("TAG STEP DeTAIL",String.valueOf(stepArrayList.size()));
        }
        if (getArguments().containsKey(StepDetailFragment.PARAM_DETAIL_STEP_ID)) {
            mStepId = getArguments().getInt(StepDetailFragment.PARAM_DETAIL_STEP_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_step_detail, container, false);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(PARAM_LIST_STEP))
                stepArrayList = savedInstanceState.getParcelableArrayList(PARAM_LIST_STEP);
            if (savedInstanceState.containsKey(PARAM_DETAIL_STEP_ID))
                mStepId = savedInstanceState.getInt(PARAM_DETAIL_STEP_ID);

        /*    android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
            fm.beginTransaction()
                    .remove(fm.findFragmentByTag(StepListFragment.PARAM_TAG_FRAGMENNT_STEP_LIST))
                    .commit();*/

        }

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            //Do some stuff
            mStepdescription = (TextView) view.findViewById(R.id.stepDescription);
            playerView = (SimpleExoPlayerView) view.findViewById(R.id.video_view);
            mStepPos = (TextView) view.findViewById(R.id.stepPosition);
            mStepdescription.setText(stepArrayList.get(mStepId).getDescription());
            nextStepBtn = (Button) view.findViewById(R.id.nextStepButon);
            prevStepBtn = (Button) view.findViewById(R.id.prevStepButon);

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

        Log.i("VIDEO URL ", stepArrayList.get(mStepId).getVideoURL());
        initializePlayer(stepArrayList.get(mStepId).getVideoURL());

        return view;
    }

    /*@Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Fragment f = getActivity().getSupportFragmentManager().findFragmentByTag(StepListFragment.PARAM_TAG_FRAGMENNT_STEP_LIST);
        if (f != null) getActivity().getSupportFragmentManager().beginTransaction()
            .remove(f).commit();
    }*/

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // save id step
        outState.putInt(PARAM_DETAIL_STEP_ID,mStepId);
        // save stepArrayList
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


    @Override
    public void onBackStackChanged() {
       // shouldDisplayHomeUp();
    }

    private void initializePlayer(String videoURL) {
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

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource(uri,
                new DefaultHttpDataSourceFactory("ua"),
                new DefaultExtractorsFactory(), null, null);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
         //   initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        hideSystemUi();
        if ((Util.SDK_INT <= 23 || player == null)) {
           // initializePlayer();
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