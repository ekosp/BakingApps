package com.ekosp.bakingapps.detail;

/**
 * Created by eko.purnomo on 07/08/2017.
 * You can contact me at : ekosetyopurnomo@gmail.com
 * or for more detail at  : http://ekosp.com
 */

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ekosp.bakingapps.R;
import com.ekosp.bakingapps.helper.Converter;
import com.ekosp.bakingapps.helper.SimpleDividerItemDecoration;
import com.ekosp.bakingapps.helper.StepAdapter;
import com.ekosp.bakingapps.models.Recipe;
import com.ekosp.bakingapps.models.Step;

import java.util.ArrayList;
import java.util.List;

public class StepListFragment extends Fragment implements StepAdapter.stepCallbacks {

    public static String PARAM_RECIPE_ID = "PARAM_RECIPE_ID";
    public static String PARAM_TAG_FRAGMENNT_STEP_LIST =  "TAG_STEP_LIST";
    View view;
    Button firstButton;
    TextView ingredientList;
    Recipe mRecipe;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private StepAdapter mStepAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_step_list, container, false);
        ingredientList = (TextView) view.findViewById(R.id.ingredient_list);
        ingredientList.setText(Converter.IngredientToString(mRecipe.getIngredientList()));

        //set recycle step detail
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_step);
        mLinearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity().getApplicationContext()));

        mStepAdapter = new StepAdapter(getActivity(),this);
       // mStepAdapter = new StepAdapter(getActivity().getApplicationContext(),null);
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
           Bundle arguments = new Bundle();
         arguments.putParcelableArrayList (StepDetailFragment.PARAM_LIST_STEP, listToArrayList(mRecipe.getStepList()) );
      arguments.putInt(StepDetailFragment.PARAM_DETAIL_STEP_ID, step.getId());

    // set step detail fragment
    android.support.v4.app.Fragment fragment = new StepDetailFragment();
    fragment.setArguments(arguments);
    android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
    fm.beginTransaction()
            .replace(R.id.frameLayout, fragment, PARAM_TAG_FRAGMENNT_STEP_LIST)
           // .addToBackStack(null)
            .commit();

    Log.i("TAG BACKSTACK 2", String.valueOf(getActivity().getSupportFragmentManager().getBackStackEntryCount()));

   }

    private ArrayList<Step> listToArrayList(List<Step> stepList) {
        ArrayList<Step> arrayStep = new ArrayList<>(stepList.size());
        arrayStep.addAll(stepList);
        return arrayStep;
    }
}