package com.ekosp.bakingapps;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ekosp.bakingapps.detail.FirstFragment;
import com.ekosp.bakingapps.detail.SecondFragment;

public class DetailActivity extends AppCompatActivity {

    Button firstFragment, secondFragment;

    public static final String PARAM_RECIPE_ID  = "PARAM_RECIPE_ID";
    public static final String TAG  = "TAG_DETAIL";
    private static final String TAG_FRAGMENT = "TAG_FRAGMENT";
    TextView tvText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // passing movie_id to fragment
        Bundle arguments = new Bundle();
        arguments.putParcelable(DetailActivity.PARAM_RECIPE_ID,
                getIntent().getParcelableExtra(DetailActivity.PARAM_RECIPE_ID));
        // set fragment programatically

        Fragment fragment = new FirstFragment();
        fragment.setArguments(arguments);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();


    }



}
