package com.example.first;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class SimplePairingActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextView statusText;
    private MaterialButton cancelButton;
    private Handler handler = new Handler();
    private int progress = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_success);

        progressBar = findViewById(R.id.progressBar);
        statusText = findViewById(R.id.tvPairingStatus);
        cancelButton = findViewById(R.id.btnCancelPairing);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        startPairing();
    }

    private void startPairing() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (progress < 100) {
                    progress += 10;
                    progressBar.setProgress(progress);

                    // Update status text
                    if (progress < 30) {
                        statusText.setText("Connecting...");
                    } else if (progress < 60) {
                        statusText.setText("Pairing...");
                    } else if (progress < 90) {
                        statusText.setText("Syncing data...");
                    } else {
                        statusText.setText("Almost done...");
                    }

                    // Continue progress
                    handler.postDelayed(this, 500);
                } else {
                    // Pairing complete
                    Intent intent = new Intent(SimplePairingActivity.this, SimpleSuccessActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}