package com.ekosp.bakingapps.helper;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.ekosp.bakingapps.R;
import com.ekosp.bakingapps.models.Recipe;
import java.util.List;

/**
 * Created by eko.purnomo on 04/08/2017.
 * You can contact me at : ekosetyopurnomo@gmail.com
 * or for more detail at  : http://ekosp.com
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private List<Recipe> mRecipeList;
    private Context mContext;
    private recipeCallbacks mRecipeCallbacks;

    public RecipeAdapter(Context mContext, recipeCallbacks mRecipeCallbacks) {
        this.mContext = mContext;
        this.mRecipeCallbacks = mRecipeCallbacks;
    }



    public interface recipeCallbacks {
        void open(Recipe recipe);
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(
                parent.getContext()).inflate(R.layout.recyclerview_items, parent, false);
        return new RecipeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RecipeViewHolder holder, int position) {
        Recipe mRecipe = mRecipeList.get(position);
        holder.mName.setText(mRecipe.getName());
         holder.mServings.setText("servings:"+mRecipe.getServings());

        holder.mLinearContainer.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                int pos = holder.getAdapterPosition();
                Recipe recipe = mRecipeList.get(pos);
                Log.i("Recipe","id : "+recipe.getId());
                mRecipeCallbacks.open(recipe);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (mRecipeList == null) ? 0 : mRecipeList.size();
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder {
        protected TextView mName;
        protected LinearLayout mLinearContainer;
        protected TextView mServings;

        public RecipeViewHolder(final View itemView) {
            super(itemView);
            mName = (TextView) itemView.findViewById(R.id.name);
            mServings = (TextView) itemView.findViewById(R.id.servings);
            mLinearContainer = (LinearLayout) itemView.findViewById(R.id.recipe_container);
        }
    }

    public List<Recipe> getmRecipeList() {
        return mRecipeList;
    }

    public void setmRecipeList(List<Recipe> mRecipeList) {

        if (mRecipeList == null) Log.i("recipe list", "lho kok recipe list balikan online nya null?");
//        if  (this.mRecipeList.size() != 0) this.mRecipeList.clear();
        int ukuran = mRecipeList.size();
        Log.i("recipe list", "jumlah list ada : "+String.valueOf(ukuran));
        this.mRecipeList = mRecipeList;
       // this.mRecipeList = mRecipeList;
        notifyDataSetChanged();
    }
}