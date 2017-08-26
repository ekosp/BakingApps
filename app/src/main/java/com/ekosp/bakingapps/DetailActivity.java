package com.ekosp.bakingapps;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.ekosp.bakingapps.detail.StepListFragment;

/**
 * Created by eko.purnomo on 28/07/2017.
 * You can contact me at : ekosetyopurnomo@gmail.com
 * or for more detail at  : http://ekosp.com
 */
public class DetailActivity extends AppCompatActivity {

    public static final String PARAM_RECIPE_ID = "PARAM_RECIPE_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Bundle arguments = new Bundle();
        arguments.putParcelable(StepListFragment.PARAM_RECIPE_ID,
                getIntent().getParcelableExtra(DetailActivity.PARAM_RECIPE_ID));
        // only load new fragment if savestate is empty recrete fragment on rotate
        if (savedInstanceState == null) {
            android.support.v4.app.Fragment fragment = new StepListFragment();
            fragment.setArguments(arguments);
            android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction()
                    .replace(R.id.frameLayout, fragment)
                    .commit();
        }


    }




}
