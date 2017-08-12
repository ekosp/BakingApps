package com.ekosp.bakingapps;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.ekosp.bakingapps.helper.RecipeAdapter;
import com.ekosp.bakingapps.helper.ApiClient;
import com.ekosp.bakingapps.helper.ApiInterface;
import com.ekosp.bakingapps.models.Recipe;
import com.ekosp.bakingapps.models.Step;

import java.util.ArrayList;
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
    private static String TAG_PARAM_RECIPE_LIST = "TAG_PARAM_RECIPE_LIST";
    private boolean mIsTablet ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.recipe_list);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mLinearLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecipeAdapter = new RecipeAdapter(this,this);
        mRecyclerView.setAdapter(mRecipeAdapter);

        mIsTablet  = (getResources().getBoolean(R.bool.isTab)) ;
        if (mIsTablet) {
            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
                mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            } else {
                int mNoOfColumns = calculateNoOfColumns(getApplicationContext());
                mRecyclerView.setLayoutManager(new GridLayoutManager(this, mNoOfColumns));
            }
        } else {
            mRecyclerView.setLayoutManager(mLinearLayoutManager);
        }

        if (savedInstanceState != null && savedInstanceState.containsKey(TAG_PARAM_RECIPE_LIST)) {
            // load existing recipe
            ArrayList<Recipe> ar = savedInstanceState.getParcelableArrayList(TAG_PARAM_RECIPE_LIST);
            Log.i("CEK SAVESTAED", String.valueOf(ar.isEmpty()));
            /*for (Recipe r : ar) {
                mListRecipes.add(r);
            }
            */
            for (int i=0; i< ar.size(); i++) {
                mListRecipes.add(ar.get(i));
                }
            if (ar != null) Log.i("YAYAAAY", "berhasil isi oii!... size = "+ar.size());
            if (mListRecipes == null) Log.i("mListRecipes", "null -------------");

             mRecipeAdapter.setmRecipeList(mListRecipes);
        } else {
            loadRecipes();
        }
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
      //  Toast.makeText(this, "open recipe id :"+recipe.getId(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(DetailActivity.PARAM_RECIPE_ID, recipe);
        startActivity(intent);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

       // if (mListRecipes == null ) Log.i("CEK MLISTRECIPES", "kosong???!!!!");
        if (mListRecipes != null ) {
          //  Log.i("CEK MLISTRECIPES", "isi kok ...........!!!");
            outState.putParcelableArrayList( TAG_PARAM_RECIPE_LIST, listToArrayList(mListRecipes));
        }
    }

    private ArrayList<Recipe> listToArrayList(List<Recipe> recipeList) {

        ArrayList<Recipe> arrayRecipe = new ArrayList<>(recipeList.size());
        arrayRecipe.addAll(recipeList);
        Log.i("listToArrayList", "size = "+String.valueOf(arrayRecipe.size()));

        return arrayRecipe;
    }

    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        return (int) (dpWidth / 300);
    }

}
