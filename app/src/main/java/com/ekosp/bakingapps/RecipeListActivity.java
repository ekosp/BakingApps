package com.ekosp.bakingapps;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.ekosp.bakingapps.data.DbOpenHelper;
import com.ekosp.bakingapps.data.IngredientData;
import com.ekosp.bakingapps.data.IngredientDataSQLiteTypeMapping;
import com.ekosp.bakingapps.data.IngredientTable;
import com.ekosp.bakingapps.helper.RecipeAdapter;
import com.ekosp.bakingapps.helper.ApiClient;
import com.ekosp.bakingapps.helper.ApiInterface;
import com.ekosp.bakingapps.models.Ingredients;
import com.ekosp.bakingapps.models.Recipe;
import com.ekosp.bakingapps.models.Step;
import com.pushtorefresh.storio.StorIOException;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.impl.DefaultStorIOSQLite;
import com.pushtorefresh.storio.sqlite.operations.put.PutResults;
import com.pushtorefresh.storio.sqlite.queries.DeleteQuery;

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
    private StorIOSQLite storIOSQLite;
    private TextView emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.recipe_list);

        storIOSQLite = DefaultStorIOSQLite.builder()
                .sqliteOpenHelper(new DbOpenHelper(this))
                .addTypeMapping(IngredientData.class, new IngredientDataSQLiteTypeMapping())
                .build();

        emptyView = (TextView) findViewById(R.id.empty_view);
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
            List<Recipe> listRecipes = new ArrayList<>();
            for (int i=0; i< ar.size(); i++) {
                listRecipes.add(ar.get(i));
            }
            mRecipeAdapter.setmRecipeList(listRecipes);
        } else {
            if (isNetworkConnected()) {
                loadRecipes();
            }
            mRecyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
            Toast.makeText(this, "Please enable yout internet connection", Toast.LENGTH_SHORT).show();

        }
    }

    private boolean isNetworkConnected() {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            return cm.getActiveNetworkInfo() != null;
    }

    public void loadRecipes() {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<List<Recipe>> call = apiService.getRecipeDetails();
        call.enqueue(new Callback<List<Recipe>>() {

            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                mListRecipes = response.body();

                if (mListRecipes.isEmpty()) {
                    mRecyclerView.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                }
                else {
                    mRecyclerView.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.GONE);
                    mRecipeAdapter.setmRecipeList(mListRecipes);
                    new addRecipeToContentProvider().execute(mListRecipes);
                }


            }
            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
    }



    private class addRecipeToContentProvider extends AsyncTask<List<Recipe>, Void, Void> {


            @Override
            protected Void doInBackground(List<Recipe>... params) {

                List<Recipe> listRecipe = params[0];
                final List<IngredientData> ingredients = new ArrayList<IngredientData>();
                long recipe_id;
                String recipe_name ;

                for (Recipe r : listRecipe) {
                    recipe_id = r.getId();
                    recipe_name = r.getName();
                    for (Ingredients ing : r.getIngredientList()) {
                        ingredients.add(IngredientData.newIngredient(
                                String.valueOf(recipe_id),recipe_name,
                                ing.getQuantity()+" "+ing.getMeasure()+" "+ing.getIngredient()
                        ));
                    }
                }

             try {

                 // delete content to get latest recipe
                    storIOSQLite
                            .delete()
                            .byQuery(DeleteQuery.builder()
                                    .table("ingredient")
                                    .build())
                            .prepare()
                            .executeAsBlocking();

                    // set latest recipe to db
                   PutResults<IngredientData> results = storIOSQLite
                            .put()
                            .objects(ingredients)
                            .prepare()
                            .executeAsBlocking();

                } catch (StorIOException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
              // do nothing for now
            }
        }

    @Override
    public void open(Recipe recipe) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(DetailActivity.PARAM_RECIPE_ID, recipe);
        startActivity(intent);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (mListRecipes != null ) {
            outState.putParcelableArrayList( TAG_PARAM_RECIPE_LIST, listToArrayList(mListRecipes));
        }
    }

    private ArrayList<Recipe> listToArrayList(List<Recipe> recipeList) {
        ArrayList<Recipe> arrayRecipe = new ArrayList<>(recipeList.size());
        arrayRecipe.addAll(recipeList);
      //  Log.i("listToArrayList", "size = "+String.valueOf(arrayRecipe.size()));
        return arrayRecipe;
    }

    // to calculate number of recycle in row
    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        return (int) (dpWidth / 300);
    }

}
