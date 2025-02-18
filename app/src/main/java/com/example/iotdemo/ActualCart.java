package com.example.iotdemo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iotdemo.models.Product;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ActualCart extends AppCompatActivity implements ProductAdapter.RemoveProductListener {

    private TextView textViewTotalAmount, textViewTotalWeight;
    private RecyclerView recyclerViewProducts;
    private ProductAdapter productAdapter;
    private List<Product> productList = new ArrayList<>();
    private double totalAmount = 0.0;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actual_cart);

        textViewTotalAmount = findViewById(R.id.textViewTotalAmount);
        textViewTotalWeight = findViewById(R.id.textViewTotalWeight2);
        recyclerViewProducts = findViewById(R.id.recyclerViewProducts);

        recyclerViewProducts.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the ProductAdapter with a listener
        productAdapter = new ProductAdapter(this, productList, this);
        recyclerViewProducts.setAdapter(productAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("products");

        // Receive scanned ID from intent
        String scannedId = getIntent().getStringExtra("SCANNED_ID");
//        String scannedId = "2";
        if (scannedId != null) {
            fetchProductData(scannedId);
        }

        findViewById(R.id.buttonAddProductManually).setOnClickListener(v -> openNumberInputDialog());

        // Set up UPI payment button
        MaterialButton buttonUPI = findViewById(R.id.buttonUPI);
        buttonUPI.setOnClickListener(v -> initiateUPIPayment());
    }

    private void fetchProductData(String scannedId) {
        databaseReference.child(scannedId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                handleProductData(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ActualCart.this, "Error fetching data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleProductData(DataSnapshot dataSnapshot) {
        String name = dataSnapshot.child("name").getValue(String.class);
        String priceString = dataSnapshot.child("price").getValue(String.class);
        String weightString = dataSnapshot.child("weight").getValue(String.class);

        if (name != null && priceString != null) {
            try {
                double price = Double.parseDouble(priceString);
                double weight = weightString != null ? Double.parseDouble(weightString) : 0.0;

                Product product = new Product(name, priceString, weightString);
                productList.add(product);
                productAdapter.notifyDataSetChanged();
                updateTotalAmount(price);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid price or weight format", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Product details are incomplete", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateTotalAmount(double price) {
        totalAmount += price;
        textViewTotalAmount.setText("Total Amount: ₹" + String.format("%.2f", totalAmount));
    }

    @Override
    public void onProductRemoved(double price) {
        totalAmount -= price;
        textViewTotalAmount.setText("Total Amount: ₹" + String.format("%.2f", totalAmount));
    }

    private void initiateUPIPayment() {
        String amount = String.format("%.2f", totalAmount);
        String upiId = "mayuriphad5@oksbi"; // Replace with your UPI ID

        Uri uri = new Uri.Builder()
                .scheme("upi")
                .authority("pay")
                .appendQueryParameter("pa", upiId) // Payee UPI ID
                .appendQueryParameter("pn", "Receiver Name") // Optional: Payee Name
                .appendQueryParameter("mc", "") // Optional: Merchant Code
                .appendQueryParameter("tid", "") // Optional: Transaction ID
                .appendQueryParameter("tt", "02") // Optional: Transaction Type
                .appendQueryParameter("am", amount) // Amount
                .appendQueryParameter("cu", "INR") // Currency Code
                .appendQueryParameter("url", "") // Optional: URL
                .build();

        Intent intent = new Intent(Intent.ACTION_VIEW, uri);

        if (amount.equals("0.00")) {
            Toast.makeText(this, "No items in the cart", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Total Amount: ₹" + amount, Toast.LENGTH_SHORT).show();
            startActivityForResult(intent, 1);
        }
    }

    private void openNumberInputDialog() {
        // Create an EditText to accept user input
        EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER); // Restrict input to numbers

        // Create the AlertDialog
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Enter barcode id: ");
        dialogBuilder.setView(input);

        // Set the Positive button to handle OK click
        dialogBuilder.setPositiveButton("OK", (dialog, which) -> {
            // Get the input value
            String numberInput = input.getText().toString();
            if (!numberInput.isEmpty()) {
//                Toast.makeText(ShoppingList.this, "Entered: " + numberInput, Toast.LENGTH_SHORT).show();
                fetchProductDetails(numberInput);

            } else {
                Toast.makeText(ActualCart.this, "No input provided", Toast.LENGTH_SHORT).show();
            }
        });

        // Set the Negative button to handle Cancel click
        dialogBuilder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        // Show the dialog
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

    private void fetchProductDetails(String productId) {
        databaseReference.child(productId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    handleProductData(dataSnapshot);
                } else {
                    Toast.makeText(ActualCart.this, "No product found for the scanned result", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ActualCart.this, "Failed to fetch product details", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
