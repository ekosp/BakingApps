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
import com.ekosp.bakingapps.models.Steps;

import java.util.List;

/**
 * Created by eko.purnomo on 04/08/2017.
 * You can contact me at : ekosetyopurnomo@gmail.com
 * or for more detail at  : http://ekosp.com
 */

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepViewHolder> {

    private List<Steps> mStepList;
    private Context mContext;
    private stepCallbacks mStepCallbacks;

    public StepAdapter(Context mContext, stepCallbacks mStepCallbacks) {
        this.mContext = mContext;
        this.mStepCallbacks = mStepCallbacks;
    }

    public interface stepCallbacks {
        void open(Steps step);
    }

    @Override
    public StepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(
                parent.getContext()).inflate(R.layout.step_detail_items, parent, false);
        return new StepViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final StepViewHolder holder, int position) {
        Steps mStep = mStepList.get(position);
        holder.mShortDescription.setText(mStep.getShortDescription());
      //  holder.mDescription.setText(mStep.getDescription());

        holder.mLinearContainer.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                int pos = holder.getAdapterPosition();
                Steps steps = mStepList.get(pos);
                Log.i("detail step","id : "+steps.getId());
                mStepCallbacks.open(steps);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (mStepList == null) ? 0 : mStepList.size();
    }

    public class StepViewHolder extends RecyclerView.ViewHolder {
        protected TextView mShortDescription;
        protected LinearLayout mLinearContainer;
       // protected TextView mDescription;

        public StepViewHolder(final View itemView) {
            super(itemView);
            mShortDescription = (TextView) itemView.findViewById(R.id.shortDescription);
         //   mDescription = (TextView) itemView.findViewById(R.id.description);
            mLinearContainer = (LinearLayout) itemView.findViewById(R.id.step_detail_container);
        }
    }

    public List<Steps> getmStepList() {
        return mStepList;
    }

    public void setmStepList(List<Steps> mStepList) {
        this.mStepList = mStepList;
        notifyDataSetChanged();
    }
}