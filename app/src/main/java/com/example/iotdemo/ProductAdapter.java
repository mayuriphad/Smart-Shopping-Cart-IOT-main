//package com.example.iotdemo;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.iotdemo.models.Product;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//
//import java.util.List;
//
//public class ProductAdapter extends RecyclerView.Adapter<ProductViewHolder> {
//    private Context context;
//    private List<Product> productList;
//
//    public ProductAdapter(Context context, List<Product> productList) {
//        this.context = context;
//        this.productList = productList;
//    }
//
//    @NonNull
//    @Override
//    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
//        return new ProductViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
//        Product product = productList.get(position);
//        holder.productName.setText(product.getProductName());
//        holder.productPrice.setText(product.getProductPrice());
//
//        holder.removeButton.setOnClickListener(v -> {
//            // Handle remove action
//            removeProductFromFirebase(product);
//        });
//    }
//
//    private void removeProductFromFirebase(Product product) {
//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("products");
//        databaseReference.child(product.getProductName()).removeValue();
//    }
//
//    @Override
//    public int getItemCount() {
//        return productList.size();
//    }
//}

package com.example.iotdemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iotdemo.models.Product;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private Context context;
    private List<Product> productList;
    private RemoveProductListener removeProductListener;

    public ProductAdapter(Context context, List<Product> productList, RemoveProductListener removeProductListener) {
        this.context = context;
        this.productList = productList;
        this.removeProductListener = removeProductListener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.textViewProductName.setText(product.getProductName());
        holder.textViewProductPrice.setText(product.getProductPrice());

        holder.buttonRemove.setOnClickListener(v -> {
            // Get the price of the product being removed
            double price = Double.parseDouble(product.getProductPrice());

            // Remove the product from the list
            productList.remove(position);
            notifyItemRemoved(position);

            // Notify ShoppingList to update the total amount
            removeProductListener.onProductRemoved(price);
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView textViewProductName, textViewProductPrice;
        ImageButton buttonRemove;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewProductName = itemView.findViewById(R.id.textViewProductName);
            textViewProductPrice = itemView.findViewById(R.id.textViewProductPrice);
            buttonRemove = itemView.findViewById(R.id.buttonRemove);
        }
    }

    public interface RemoveProductListener {
        void onProductRemoved(double price);
    }
}