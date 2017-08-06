package com.ekosp.bakingapps;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.ekosp.bakingapps.helper.Adapter;
import com.ekosp.bakingapps.helper.ApiClient;
import com.ekosp.bakingapps.helper.ApiInterface;
import com.ekosp.bakingapps.models.Recipe;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private List<Recipe> mListRecipes;
    private ArrayList<String> list = new ArrayList<>();


    RecyclerView mRecyclerView;
    LinearLayoutManager mLinearLayoutManager;
    Adapter mTestAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_main);

// Get Recycler View by id from layout file
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        // Create Linear Layout Manager which defines how it will be shown on the screen
        mLinearLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        // Set Layout Manager in the RecyclerView
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        // Create Adapter object from the data by calling default Constructor
        loadRecipes();
    }

    public void loadRecipes() {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<List<Recipe>> call = apiService.getRecipeDetails();
        call.enqueue(new Callback<List<Recipe>>() {

            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                Log.d(TAG, "Number of recipes received: " + response.body().toString());
                mListRecipes = response.body();

                for(Recipe r : mListRecipes) {
                    Log.i("Recipe Detail","name "+r.getName());
                    list.add(r.getName());
                    Log.i("Recipe Detail","list ingredients"+r.getIngredientList().get(0).getIngredient());
                }

                mTestAdapter = new Adapter(list);
                // Set RecyclerView Adapter
                mRecyclerView.setAdapter(mTestAdapter);
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
    }


}
