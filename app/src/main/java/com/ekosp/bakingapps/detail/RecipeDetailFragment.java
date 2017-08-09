package com.ekosp.bakingapps.detail;

/**
 * Created by eko.purnomo on 07/08/2017.
 * You can contact me at : ekosetyopurnomo@gmail.com
 * or for more detail at  : http://ekosp.com
 */

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ekosp.bakingapps.R;
import com.ekosp.bakingapps.helper.Converter;
import com.ekosp.bakingapps.helper.RecipeAdapter;
import com.ekosp.bakingapps.helper.SimpleDividerItemDecoration;
import com.ekosp.bakingapps.helper.StepAdapter;
import com.ekosp.bakingapps.models.Recipe;
import com.ekosp.bakingapps.models.Steps;

public class RecipeDetailFragment extends Fragment implements StepAdapter.stepCallbacks{

    public static String PARAM_RECIPE_ID = "PARAM_RECIPE_ID";
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
        view = inflater.inflate(R.layout.step_detail, container, false);
        ingredientList = (TextView) view.findViewById(R.id.ingredient_list);
        ingredientList.setText(Converter.IngredientToString(mRecipe.getIngredientList()));

        //set recycle step detail
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_step);
        mLinearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity().getApplicationContext()));

        mStepAdapter = new StepAdapter(getActivity(),this);
        mRecyclerView.setAdapter(mStepAdapter);

        loadSteps();

        return view;
    }

    private void loadSteps() {
        mStepAdapter.setmStepList(mRecipe.getStepList());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(RecipeDetailFragment.PARAM_RECIPE_ID)) {
            mRecipe = getArguments().getParcelable(RecipeDetailFragment.PARAM_RECIPE_ID);
        }
    }

    @Override
    public void open(Steps step) {
        Toast.makeText(getActivity().getApplicationContext(), "Anda menekan step :"+step.getDescription(), Toast.LENGTH_SHORT).show();
    }
}