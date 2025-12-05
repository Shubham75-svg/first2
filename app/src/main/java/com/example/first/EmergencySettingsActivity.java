package com.example.first;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.material.button.MaterialButton;

public class EmergencySettingsActivity extends AppCompatActivity {

    private EditText contact1EditText, contact2EditText, messageEditText;
    private MaterialButton saveButton, testButton, watchConnectButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_settings);

        // Initialize views
        initializeViews();

        // Set up click listeners
        setupClickListeners();
    }

    private void initializeViews() {
        contact1EditText = findViewById(R.id.contact1EditText);
        contact2EditText = findViewById(R.id.contact2EditText);
        messageEditText = findViewById(R.id.messageEditText);
        saveButton = findViewById(R.id.saveButton);
        testButton = findViewById(R.id.testButton);
        watchConnectButton = findViewById(R.id.watchConnectButton);
    }

    private void setupClickListeners() {
        // Save button click
        saveButton.setOnClickListener(v -> saveEmergencySettings());

        // Test button click
        testButton.setOnClickListener(v -> testEmergencyAlert());

        // Smart Watch Connect button click
        watchConnectButton.setOnClickListener(v -> openSmartWatchConnect());
    }

    private void saveEmergencySettings() {
        String contact1 = contact1EditText.getText().toString().trim();
        String contact2 = contact2EditText.getText().toString().trim();
        String message = messageEditText.getText().toString().trim();

        // Basic validation
        if (contact1.isEmpty() && contact2.isEmpty()) {
            Toast.makeText(this, "Please add at least one emergency contact", Toast.LENGTH_SHORT).show();
            return;
        }

        if (message.isEmpty()) {
            Toast.makeText(this, "Please enter an emergency message", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save contacts and message to SharedPreferences or database
        saveToSharedPreferences(contact1, contact2, message);

        Toast.makeText(this, "Emergency settings saved successfully!", Toast.LENGTH_SHORT).show();
    }

    private void saveToSharedPreferences(String contact1, String contact2, String message) {
        // Save to SharedPreferences
        getSharedPreferences("EmergencySettings", MODE_PRIVATE)
                .edit()
                .putString("emergency_contact_1", contact1)
                .putString("emergency_contact_2", contact2)
                .putString("emergency_message", message)
                .apply();
    }

    private void testEmergencyAlert() {
        // Simulate test alert
        Toast.makeText(this, "Test alert triggered! Checking system...", Toast.LENGTH_SHORT).show();

        // Simulate some processing
        new android.os.Handler().postDelayed(
                () -> Toast.makeText(this, "Test completed successfully!", Toast.LENGTH_LONG).show(),
                2000
        );
    }

    private void openSmartWatchConnect() {
        // Create intent to open SmartWatchConnectActivity
        Intent intent = new Intent(EmergencySettingsActivity.this, SmartWatchConnectActivity.class);
        startActivity(intent);

        // Optional: Add transition animation
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    // Optional: Load saved settings when activity starts
    @Override
    protected void onResume() {
        super.onResume();
        loadSavedSettings();
    }

    private void loadSavedSettings() {
        // Load from SharedPreferences
        String savedContact1 = getSharedPreferences("EmergencySettings", MODE_PRIVATE)
                .getString("emergency_contact_1", "");
        String savedContact2 = getSharedPreferences("EmergencySettings", MODE_PRIVATE)
                .getString("emergency_contact_2", "");
        String savedMessage = getSharedPreferences("EmergencySettings", MODE_PRIVATE)
                .getString("emergency_message", "");

        // Set the loaded values to EditText fields
        contact1EditText.setText(savedContact1);
        contact2EditText.setText(savedContact2);
        messageEditText.setText(savedMessage);
    }

    // Handle back button press
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}