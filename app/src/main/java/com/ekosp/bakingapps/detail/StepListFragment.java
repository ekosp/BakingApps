package com.ekosp.bakingapps.detail;

/**
 * Created by eko.purnomo on 07/08/2017.
 * You can contact me at : ekosetyopurnomo@gmail.com
 * or for more detail at  : http://ekosp.com
 */

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ekosp.bakingapps.R;
import com.ekosp.bakingapps.RecipeListActivity;
import com.ekosp.bakingapps.helper.Converter;
import com.ekosp.bakingapps.helper.SimpleDividerItemDecoration;
import com.ekosp.bakingapps.helper.StepAdapter;
import com.ekosp.bakingapps.models.Recipe;
import com.ekosp.bakingapps.models.Step;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepListFragment extends Fragment implements StepAdapter.stepCallbacks {

    public static String PARAM_RECIPE_ID = "PARAM_RECIPE_ID";
    public static String PARAM_TAG_FRAGMENNT_STEP_LIST =  "TAG_STEP_LIST";
    View view;
    @BindView(R.id.ingredient_list)
    TextView ingredientList;
    Recipe mRecipe;
    @BindView(R.id.recycler_step)
    RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private StepAdapter mStepAdapter;
    private boolean mIsTablet ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_step_list, container, false);
        ButterKnife.bind(this, view);
       ingredientList.setText(Converter.IngredientToString(mRecipe.getIngredientList()));

        // testing html as string
        //ingredientList.setText(Converter.IngredientToHtmlAsString(mRecipe.getIngredientList()));

     //  String tets_string = "Brownis";
      // ingredientList.setText(tets_string);

      //  Log.i("test", ""+ingredientList.getText());


        // get from : https://stackoverflow.com/questions/26998455/how-to-get-toolbar-from-fragment
        // and from:  https://freakycoder.com/android-notes-24-how-to-add-back-button-at-toolbar-941e6577418e
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.tool_bar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //set recycle step detail
        mLinearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity().getApplicationContext()));

        mStepAdapter = new StepAdapter(getActivity(),this);
        mRecyclerView.setAdapter(mStepAdapter);

        loadSteps();
        getActivity().setTitle(mRecipe.getName());
        return view;
    }

    private void loadSteps() {
        mStepAdapter.setmStepList(mRecipe.getStepList());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(StepListFragment.PARAM_RECIPE_ID)) {
            mRecipe = getArguments().getParcelable(StepListFragment.PARAM_RECIPE_ID);
        }
    }

   @Override
    public void open(Step step) {
       mIsTablet  = (getResources().getBoolean(R.bool.isTab)) ;
       Bundle arguments = new Bundle();
       arguments.putParcelableArrayList (StepDetailFragment.PARAM_LIST_STEP, listToArrayList(mRecipe.getStepList()) );
       arguments.putInt(StepDetailFragment.PARAM_DETAIL_STEP_ID, step.getId());

       if (mIsTablet) {
           // set step detail fragment
           android.support.v4.app.Fragment fragment = new StepDetailFragment();
           fragment.setArguments(arguments);
           android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
           fm.beginTransaction()
                   .replace(R.id.flDetailContainer, fragment, PARAM_TAG_FRAGMENNT_STEP_LIST)
                   .addToBackStack(null)
                   .commit();
       } else {
           // set step detail fragment
           android.support.v4.app.Fragment fragment = new StepDetailFragment();
           fragment.setArguments(arguments);
           android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
           fm.beginTransaction()
                   .replace(R.id.frameLayout, fragment, PARAM_TAG_FRAGMENNT_STEP_LIST)
                   .addToBackStack(null)
                   .commit();
       }
   }

    private ArrayList<Step> listToArrayList(List<Step> stepList) {
        ArrayList<Step> arrayStep = new ArrayList<>(stepList.size());
        arrayStep.addAll(stepList);
        return arrayStep;
    }

}