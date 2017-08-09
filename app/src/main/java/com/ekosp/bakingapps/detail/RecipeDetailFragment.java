package com.ekosp.bakingapps.detail;

/**
 * Created by eko.purnomo on 07/08/2017.
 * You can contact me at : ekosetyopurnomo@gmail.com
 * or for more detail at  : http://ekosp.com
 */

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ekosp.bakingapps.R;
import com.ekosp.bakingapps.helper.Converter;
import com.ekosp.bakingapps.models.Recipe;

public class RecipeDetailFragment extends Fragment {

    public static String PARAM_RECIPE_ID = "PARAM_RECIPE_ID";
    View view;
    Button firstButton;
    TextView ingredientList;
    Recipe mRecipe;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_first, container, false);
        firstButton = (Button) view.findViewById(R.id.firstButton);
        ingredientList = (TextView) view.findViewById(R.id.ingredient_list);

        firstButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "First Fragment", Toast.LENGTH_LONG).show();
            }
        });

        ingredientList.setText(Converter.IngredientToString(mRecipe.getIngredientList()));

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(RecipeDetailFragment.PARAM_RECIPE_ID)) {
            mRecipe = getArguments().getParcelable(RecipeDetailFragment.PARAM_RECIPE_ID);
        }
      //  setHasOptionsMenu(true);
    }
}