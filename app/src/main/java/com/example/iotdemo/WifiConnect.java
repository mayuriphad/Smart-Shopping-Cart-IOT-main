package com.example.iotdemo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class WifiConnect extends AppCompatActivity {

    private TextView connectingText;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_connect);

        connectingText = findViewById(R.id.connecting_text);
        handler = new Handler();

        startBreathingAnimation();

        // Start a delayed task to display "Connected with cart" after 5 seconds
        handler.postDelayed(this::displayConnectedWithCart, 5000);
    }

    // Method to start the breathing animation
    private void startBreathingAnimation() {
        Animation breathingAnimation = AnimationUtils.loadAnimation(this, R.anim.breathing_animation);
        connectingText.startAnimation(breathingAnimation);
    }

    // Method to display "Connected with cart" and navigate to BeforeActualProduct
    private void displayConnectedWithCart() {
        connectingText.setText("Connected with cart");
//        Toast.makeText(WifiConnect.this, "Connected with cart", Toast.LENGTH_SHORT).show();

        // Start BeforeActualProduct activity after displaying the message
        Intent intent = new Intent(WifiConnect.this, BeforeActualPrroduct.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove any pending callbacks to avoid memory leaks
        handler.removeCallbacksAndMessages(null);
    }
}
