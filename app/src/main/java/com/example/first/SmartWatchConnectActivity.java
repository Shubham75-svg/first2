package com.example.first;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.first.R;
import com.google.android.material.button.MaterialButton;

public class SmartWatchConnectActivity extends AppCompatActivity {

    private MaterialButton btnAppleWatch, btnSamsungWatch, btnWearOs, btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.smart_watch_connection); // Replace with your XML layout name

        // Initialize buttons
        initializeViews();

        // Set up click listeners
        setupClickListeners();
    }

    private void initializeViews() {
        btnAppleWatch = findViewById(R.id.btnAppleWatch);
        btnSamsungWatch = findViewById(R.id.btnSamsungWatch);
        btnWearOs = findViewById(R.id.btnWearOs);
        btnBack = findViewById(R.id.btnBack);
    }

    private void setupClickListeners() {
        // Apple Watch button click
        btnAppleWatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAppStore("apple");
            }
        });

        // Samsung Watch button click
        btnSamsungWatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAppStore("samsung");
            }
        });

        // Wear OS button click
        btnWearOs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAppStore("wearos");
            }
        });

        // Back button click
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Close this activity and go back
            }
        });
    }

    private void openAppStore(String storeType) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        String storeUrl = "";
        String toastMessage = "";

        switch (storeType) {
            case "apple":
                storeUrl = "https://apps.apple.com/app/yourapp";
                toastMessage = "Opening Apple App Store...";
                break;

            case "samsung":
                storeUrl = "https://galaxystore.samsung.com/app/yourapp";
                toastMessage = "Opening Samsung Galaxy Store...";
                break;

            case "wearos":
                storeUrl = "https://play.google.com/store/apps/details?id=yourapp.watch";
                toastMessage = "Opening Google Play Store...";
                break;

            default:
                storeUrl = "https://play.google.com/store/apps/details?id=yourapp.watch";
                toastMessage = "Opening app store...";
                break;
        }

        // Show toast message
        Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show();

        // Set the URI and start activity
        intent.setData(Uri.parse(storeUrl));

        // Check if there's an app that can handle this intent
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            // If no app can handle the intent, show error message
            Toast.makeText(this, "No app store found on your device", Toast.LENGTH_LONG).show();
        }
    }

    // Optional: Handle back button press
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}