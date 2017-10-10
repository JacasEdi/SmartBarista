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

import com.jc.sb.sb_customer_app.R;
import com.jc.sb.sb_customer_app.SingleProductScreenActivity;

import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.RecyclerViewHolder>{

    private Context mContext;
    private List<Product> productList;

    public ProductsAdapter(Context mContext, List<Product> productList){
        this.mContext = mContext;
        this.productList = productList;
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder{

        public ImageView thumbnail;
        public TextView title, price;

        public RecyclerViewHolder(View view) {
            super(view);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            title = (TextView) view.findViewById(R.id.title);
            price = (TextView) view.findViewById(R.id.price);

        }
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_card, viewGroup, false);

        return new RecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder rviewHolder, final int position) {

        Product product = productList.get(position);

        // Get the Product ID
        final int pID = product.getProductID();

        // Get the Product Price
        final double pPrice = product.getProductPrice();

        // Get the Category ID
        final int cID = product.getCategoryID();

        // Get the Product Tile
        rviewHolder.title.setText(product.getProductTitle());

        // Get the Product Price
        rviewHolder.price.setText("Cr." + product.getProductPrice());

        // Get Category Image
        // Loading Category Image using Glide library
        Glide.with(mContext).load(product.getProductThumbnail()).into(rviewHolder.thumbnail);

        rviewHolder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Just a debug Toast...
                Toast.makeText(mContext, "Product ID: " + pID, Toast.LENGTH_SHORT).show();

                // Create a new set of info to send to the 'Single Product Screen'
                Bundle bundle = new Bundle();
                // Send the Product ID to the 'Single Product Screen'
                bundle.putInt("pID", pID);
                // Send the Product Price to the 'Single Product Screen'
                bundle.putDouble("pPrice", pPrice);
                // Send the Category ID to the 'Single Product Screen'
                bundle.putInt("cID", cID);
                // Create a new Intent
                Intent intent = new Intent(mContext, SingleProductScreenActivity.class);
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
        return productList.size();
    }
}
