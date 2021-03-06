package com.ekosp.bakingapps.helper;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ekosp.bakingapps.R;
import com.ekosp.bakingapps.models.Step;
import com.squareup.picasso.Picasso;

import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by eko.purnomo on 04/08/2017.
 * You can contact me at : ekosetyopurnomo@gmail.com
 * or for more detail at  : http://ekosp.com
 */

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepViewHolder> {

    private List<Step> mStepList;
    private Context mContext;
    private stepCallbacks mStepCallbacks;

    public StepAdapter(Context mContext, stepCallbacks mStepCallbacks) {
        this.mContext = mContext;
        this.mStepCallbacks = mStepCallbacks;
    }

    public interface stepCallbacks {
        void open(Step step);
    }

    @Override
    public StepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(
                parent.getContext()).inflate(R.layout.step_items, parent, false);
        return new StepViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final StepViewHolder holder, int position) {
        Step mStep = mStepList.get(position);
        holder.mShortDescription.setText(mStep.getShortDescription());
        holder.mLinearContainer.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                int pos = holder.getAdapterPosition();
                Step steps = mStepList.get(pos);
                mStepCallbacks.open(steps);
            }
        });
        // set recipe image if exist
        if (!TextUtils.isEmpty(mStep.getThumbnailURL()))
            Picasso.with(mContext)
                    .load(mStep.getThumbnailURL())
                    .placeholder(R.color.colorPrimary)
                    .into(holder.mImageThumbnail);
    }

    @Override
    public int getItemCount() {
        return (mStepList == null) ? 0 : mStepList.size();
    }

    public class StepViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.shortDescription)
        TextView mShortDescription;
        @BindView(R.id.step_detail_container)
        LinearLayout mLinearContainer;
        @BindView(R.id.step_thumbnail)
        ImageView mImageThumbnail;

        public StepViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public List<Step> getmStepList() {
        return mStepList;
    }

    public void setmStepList(List<Step> mStepList) {
        this.mStepList = mStepList;
        notifyDataSetChanged();
    }
}