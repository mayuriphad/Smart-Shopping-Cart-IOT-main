package com.example.iotdemo;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ProductViewHolder extends RecyclerView.ViewHolder {
    public TextView productName, productPrice;
    public ImageButton removeButton;

    public ProductViewHolder(View itemView) {
        super(itemView);
        productName = itemView.findViewById(R.id.textViewProductName);
        productPrice = itemView.findViewById(R.id.textViewProductPrice);
        removeButton = itemView.findViewById(R.id.buttonRemove);
    }
}
