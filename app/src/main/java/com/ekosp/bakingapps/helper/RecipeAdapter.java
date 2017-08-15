package com.ekosp.bakingapps.helper;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.ekosp.bakingapps.R;
import com.ekosp.bakingapps.models.Recipe;
import com.squareup.picasso.Picasso;

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
                parent.getContext()).inflate(R.layout.recipe_items, parent, false);
        return new RecipeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RecipeViewHolder holder, int position) {
        Recipe mRecipe = mRecipeList.get(position);
        holder.mName.setText(mRecipe.getName());
         holder.mServings.setText(mContext.getString(R.string.text_servings)+mRecipe.getServings());

        // set recipe image if exist
       // if (mRecipe.getImage() != null && !mRecipe.getImage().isEmpty() )
        if (!TextUtils.isEmpty(mRecipe.getImage()))
        Picasso.with(mContext)
                .load(mRecipe.getImage())
                .placeholder(R.color.colorPrimary)
                .into(holder.mRecipeImage);

        holder.mCardRecipeView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                int pos = holder.getAdapterPosition();
                Recipe recipe = mRecipeList.get(pos);
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
        protected CardView mCardRecipeView;
        protected TextView mServings;
        protected ImageView mRecipeImage;

        public RecipeViewHolder(final View itemView) {
            super(itemView);
            mName = (TextView) itemView.findViewById(R.id.name);
            mServings = (TextView) itemView.findViewById(R.id.servings);
            mCardRecipeView = (CardView) itemView.findViewById(R.id.card_recipe_view);
            mRecipeImage = (ImageView) itemView.findViewById(R.id.imageViewKu);
        }
    }

    public List<Recipe> getmRecipeList() {
        return mRecipeList;
    }

    public void setmRecipeList(List<Recipe> mRecipeList) {
        this.mRecipeList = mRecipeList;
        notifyDataSetChanged();
    }
}