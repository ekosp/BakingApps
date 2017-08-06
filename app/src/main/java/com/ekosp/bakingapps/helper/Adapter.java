package com.ekosp.bakingapps.helper;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ekosp.bakingapps.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by eko.purnomo on 04/08/2017.
 * You can contact me at : ekosetyopurnomo@gmail.com
 * or for more detail at  : http://ekosp.com
 */

public class Adapter extends RecyclerView.Adapter<Adapter.TestViewHolder> {

    private ArrayList<String> myList;
    //private final Context mContext;
    public Adapter(ArrayList<String> list) {
        this.myList = list;
    }


    @Override
    public TestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(
                parent.getContext()).inflate(R.layout.recyclerview_items, parent, false);



        return new TestViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TestViewHolder holder, int position) {
        String itemText = myList.get(position);
        holder.vText.setText(itemText);

       /* Picasso.with(Context)
                .load("")
                .placeholder(R.color.colorPrimary)
                .into(holder.imgView);*/
    }

    @Override
    public int getItemCount() {
        return myList.size();
    }

    public class TestViewHolder extends RecyclerView.ViewHolder {
        protected TextView vText;
        protected ImageView imgView;

        public TestViewHolder(View itemView) {
            super(itemView);
            vText = (TextView) itemView.findViewById(R.id.text);
            imgView = (ImageView) itemView.findViewById(R.id.imageViewKu);
        }
    }
}