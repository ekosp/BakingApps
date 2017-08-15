package com.ekosp.bakingapps;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.ekosp.bakingapps.detail.StepDetailFragment;
import com.ekosp.bakingapps.detail.StepListFragment;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public static final String PARAM_RECIPE_ID = "PARAM_RECIPE_ID";
    public static final String TAG = "TAG_DETAIL";
    private static final String BACK_STACK_ROOT_TAG = "root_fragment";
    private static final String TAG_FRAGMENT = "TAG_FRAGMENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // passing movie_id to fragment
        Bundle arguments = new Bundle();
        arguments.putParcelable(StepListFragment.PARAM_RECIPE_ID,
                getIntent().getParcelableExtra(DetailActivity.PARAM_RECIPE_ID));
        // only load new fragment if savestate is empty recrete fragment on rotate
        if (savedInstanceState == null) {
            // set step list fragment
            android.support.v4.app.Fragment fragment = new StepListFragment();
            fragment.setArguments(arguments);
            android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction()
                    .replace(R.id.frameLayout, fragment)
                    // removed as udacity's reviewer issue's solution
                    //  .addToBackStack(BACK_STACK_ROOT_TAG)
                    .commit();
        }


    }


}
