package com.example.iotdemo;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class BeforeActualPrroduct extends AppCompatActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_before_actual_prroduct);

        // Initialize the WebView
        webView = findViewById(R.id.webView);

        // Enable JavaScript
        webView.getSettings().setJavaScriptEnabled(true);

        // Set WebViewClient to open URL in the WebView
        webView.setWebViewClient(new WebViewClient());

        // Load the URL
        webView.loadUrl("http://192.168.112.145");

        // Handle buttons below
        findViewById(R.id.addProduct).setOnClickListener(v -> {
            // Button 1 action
//            this will capture the image which is shown in webview and scan its barcode and fetch the product details from firebase under products reference
//            webView.evaluateJavascript(
//                    "(function() { return document.getElementById('product-image').src; })();",
//                    value -> {
//                        String imageUrl = value.replace("\"", "");
//                        String[] parts = imageUrl.split("/");
//                        String scannedId = parts[parts.length - 1].replace(".jpg", "");
            Intent intent = new Intent(this, ActualCart.class);
            intent.putExtra("SCANNED_ID", "3");
            startActivity(intent);
//                    }
//            );

        });

        findViewById(R.id.imageAddProduct).setOnClickListener(v -> {
            // Button 1 action

            Intent intent = new Intent(this, ActualCart.class);
            intent.putExtra("SCANNED_ID", "3");
            startActivity(intent);
        });


        findViewById(R.id.goToList).setOnClickListener(v -> {
            // Button 2 action
            startActivity(new Intent(this, ActualCart.class));
        });

        findViewById(R.id.imageGoToList).setOnClickListener(v -> {
            // Button 2 action
            startActivity(new Intent(this, ActualCart.class));
        });


    }

}
