package com.ekosp.bakingapps;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.ekosp.bakingapps.helper.RecipeAdapter;
import com.ekosp.bakingapps.helper.ApiClient;
import com.ekosp.bakingapps.helper.ApiInterface;
import com.ekosp.bakingapps.models.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeListActivity extends AppCompatActivity implements RecipeAdapter.recipeCallbacks{

    private static final String TAG = RecipeListActivity.class.getSimpleName();
    private List<Recipe> mListRecipes;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private RecipeAdapter mRecipeAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.recipe_list);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mLinearLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        mRecipeAdapter = new RecipeAdapter(this,this);
        mRecyclerView.setAdapter(mRecipeAdapter);

        loadRecipes();
    }

    public void loadRecipes() {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<List<Recipe>> call = apiService.getRecipeDetails();
        call.enqueue(new Callback<List<Recipe>>() {

            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                mListRecipes = response.body();
                mRecipeAdapter.setmRecipeList(mListRecipes);
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
    }

    @Override
    public void open(Recipe recipe) {
        Toast.makeText(this, "open recipe id :"+recipe.getId(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(DetailActivity.PARAM_RECIPE_ID, recipe);
        startActivity(intent);
    }
}
