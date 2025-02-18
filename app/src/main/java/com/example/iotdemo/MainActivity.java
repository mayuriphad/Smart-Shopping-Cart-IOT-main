package com.example.iotdemo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final int SPLASH_SCREEN_DURATION = 3000; // Duration of splash screen (3 seconds)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Ensure this matches your XML file name

        // Initialize views
        ImageView imageLogo = findViewById(R.id.imageLogo);
        TextView textViewAppName = findViewById(R.id.textViewAppName);

        // Load animations
        Animation fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_down);
        Animation slideUpAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_up);

        // Set animations
        imageLogo.startAnimation(fadeInAnimation);
        textViewAppName.startAnimation(slideUpAnimation);

        // Transition to next activity after the splash screen duration
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(MainActivity.this, Menu.class);
            startActivity(intent);
            finish(); // Close the splash screen activity
        }, SPLASH_SCREEN_DURATION);
    }
}
