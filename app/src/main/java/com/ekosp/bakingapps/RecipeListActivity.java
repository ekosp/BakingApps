package com.ekosp.bakingapps;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.recipe_list);

        storIOSQLite = DefaultStorIOSQLite.builder()
                .sqliteOpenHelper(new DbOpenHelper(this))
                .addTypeMapping(IngredientData.class, new IngredientDataSQLiteTypeMapping())
                .build();

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

                new addRecipeToContentProvider().execute(mListRecipes);
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
                                   // .where("timestamp <= ?")
                                   // .whereArgs(System.currentTimeMillis() - 86400) // No need to write String.valueOf()
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
                   // Toast.makeText(this, R.string.app_name, Toast.LENGTH_LONG).show();

                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
               updateFavoriteButtons();
            }
        } //.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


    private void updateFavoriteButtons() {
        Toast.makeText(this, "berhasil add recipe dummy to database!", Toast.LENGTH_SHORT).show();
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
