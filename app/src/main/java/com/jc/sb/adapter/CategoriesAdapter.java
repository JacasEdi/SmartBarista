package com.jc.sb.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jc.sb.sb_customer_app.ProductMenuScreenActivity;
import com.jc.sb.sb_customer_app.R;

import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.RecyclerViewHolder>{

    private Context mContext;
    private List<Category> categoryList;

    public CategoriesAdapter(Context mContext, List<Category> categoryList){
        this.mContext = mContext;
        this.categoryList = categoryList;
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder{

        public ImageView thumbnail;
        public TextView title;

        public RecyclerViewHolder(View view) {
            super(view);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            title = (TextView) view.findViewById(R.id.title);

        }
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.category_card, viewGroup, false);

        return new RecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder rvHolder, final int position) {

        Category category = categoryList.get(position);

        // Get the Category ID
        final int cID = category.getCategoryID();

        // Get the Category Tile
        rvHolder.title.setText(category.getCategoryTitle());

        // Get Category Image
        // Loading Category Image using Glide library
        Glide.with(mContext).load(category.getCategoryImage()).into(rvHolder.thumbnail);

        rvHolder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Just a debug Toast...
                Toast.makeText(mContext, "Category ID: " + cID, Toast.LENGTH_SHORT).show();

                // Create a new set of info to send to the 'Product Menu Screen'
                Bundle bundle = new Bundle();
                // Send the Category ID to the 'Product Menu Screen'
                bundle.putInt("cID", cID);
                // Create a new Intent
                Intent intent = new Intent(mContext, ProductMenuScreenActivity.class);
                // Send the Bundle
                intent.putExtras(bundle);
                // Call the Activity
                mContext.startActivity(intent);
                // mContext needs to be casted into an Activity
                // Activity Transition Effect
                // mContext needs to be casted into an activity
                if (mContext instanceof Activity) {
                    ((Activity) mContext).overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                    ((Activity) mContext).finish();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }
}
