package com.example.iotdemo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iotdemo.models.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;


public class ShoppingList extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList;
    private DatabaseReference databaseReference;
    private TextView totalAmountTextView, totalWeightTextView;
    private double totalAmount = 0.0;
    private double totalWeight = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        initializeViews();
        setupRecyclerView();
        initializeDatabaseReference();
        setOnClickListeners();

        findViewById(R.id.buttonAddProductManually).setOnClickListener(v -> openNumberInputDialog());
    }

    private void initializeViews() {
        recyclerView = findViewById(R.id.recyclerViewProducts);
        totalAmountTextView = findViewById(R.id.textViewTotalAmount);
        totalWeightTextView = findViewById(R.id.textViewTotalWeight);

    }

    private void setupRecyclerView() {
        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(this, productList, this::updateTotalAmountOnRemove);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(productAdapter);
    }

    private void initializeDatabaseReference() {
        databaseReference = FirebaseDatabase.getInstance().getReference("products");
    }

    private void setOnClickListeners() {
        findViewById(R.id.buttonScanMore).setOnClickListener(v -> startBarcodeScanner());
        findViewById(R.id.imageScanProducts).setOnClickListener(v -> startBarcodeScanner());
        findViewById(R.id.buttonUPI).setOnClickListener(v -> initiateUPIPayment());
        findViewById(R.id.imageRupee).setOnClickListener(v -> initiateUPIPayment());
    }

    private void updateTotalAmount(double price) {
        totalAmount += price;
        totalAmountTextView.setText(String.format("Total Amount: ₹%.2f", totalAmount));

    }

    private void updateTotalAmountOnRemove(double price) {
        totalAmount -= price;
        totalAmountTextView.setText(String.format("Total Amount: ₹%.2f", totalAmount));
    }

    private void updateTotalWeight(double weight) {
        totalWeight += weight;
        totalWeightTextView.setText(String.format("Total Weight: %.2f kg", totalWeight));

    }

    // The rest of your existing methods remain the same...
    private void startBarcodeScanner() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scan a barcode or QR code");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(true);
        integrator.setBarcodeImageEnabled(false);
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                String scannedResult = result.getContents();
                fetchProductDetails(scannedResult);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

    private void fetchProductDetails(String productId) {
        databaseReference.child(productId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    handleProductData(dataSnapshot);
                } else {
                    Toast.makeText(ShoppingList.this, "No product found for the scanned result", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ShoppingList.this, "Failed to fetch product details", Toast.LENGTH_SHORT).show();
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
                updateTotalWeight(weight);
            } catch (NumberFormatException e) {
                Toast.makeText(ShoppingList.this, "Invalid price or weight format", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(ShoppingList.this, "Product details are incomplete", Toast.LENGTH_SHORT).show();
        }
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

//        if (intent.resolveActivity(getPackageManager()) != null) {
        if (amount.equals("0.00")) {
            Toast.makeText(this, "No items in the cart", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Total Amount: ₹" + amount, Toast.LENGTH_SHORT).show();
            startActivityForResult(intent, 1);
        }
//        } else {
//            Toast.makeText(this, "Google Pay is not installed", Toast.LENGTH_SHORT).show();
//        }
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
                Toast.makeText(ShoppingList.this, "No input provided", Toast.LENGTH_SHORT).show();
            }
        });

        // Set the Negative button to handle Cancel click
        dialogBuilder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        // Show the dialog
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

}