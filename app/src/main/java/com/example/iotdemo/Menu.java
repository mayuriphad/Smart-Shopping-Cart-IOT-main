package com.example.iotdemo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class Menu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        findViewById(R.id.buttonScanProducts).setOnClickListener(v -> {
            startActivity(new Intent(this, ShoppingList.class));
        });
        findViewById(R.id.imageScanProducts).setOnClickListener(v -> {
            startActivity(new Intent(this, ShoppingList.class));
        });
        findViewById(R.id.buttonCartConnect).setOnClickListener(v -> {
            startActivity(new Intent(this, WifiConnect.class));
        });
        findViewById(R.id.imageCartConnect).setOnClickListener(v -> {
            startActivity(new Intent(this, WifiConnect.class));
        });
    }
}