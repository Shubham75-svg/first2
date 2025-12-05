package com.example.first;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class HealthScoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.health_score_dashboard);  // MUST MATCH YOUR XML NAME

        setupClickListeners();

        Toast.makeText(this, "Health Dashboard Opened", Toast.LENGTH_SHORT).show();
    }

    private void setupClickListeners() {

        // Make sure these IDs exist in your XML EXACTLY spelled same

        // Panic Alert
        View panic = findViewById(R.id.panicAlertItem);
        if (panic != null) {
            panic.setOnClickListener(v ->
                    Toast.makeText(this, "Emergency assistance activated", Toast.LENGTH_SHORT).show());
        }

        // Hearts (Spelling check: hoartsItem in XML)
        View hoarts = findViewById(R.id.hoartsItem);
        if (hoarts != null) {
            hoarts.setOnClickListener(v ->
                    Toast.makeText(this, "Opening sports topics", Toast.LENGTH_SHORT).show());
        }

        // Live Walk
        View liveWalk = findViewById(R.id.liveWalkItem);
        if (liveWalk != null) {
            liveWalk.setOnClickListener(v ->
                    Toast.makeText(this, "Starting women's live walk", Toast.LENGTH_SHORT).show());
        }

        // Alert History
        View history = findViewById(R.id.alertHistoryItem);
        if (history != null) {
            history.setOnClickListener(v ->
                    Toast.makeText(this, "Showing alert history", Toast.LENGTH_SHORT).show());
        }
    }
}
