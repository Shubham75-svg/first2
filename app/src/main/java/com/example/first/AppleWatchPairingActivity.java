package com.example.first;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class AppleWatchPairingActivity extends AppCompatActivity {

    private ImageView ivStep1, ivStep2, ivStep3, ivStep4;
    private ProgressBar pbStep1, pbStep2, pbStep3, pbStep4;
    private TextView tvPairingStatus, tvStep1, tvStep2, tvStep3, tvStep4;
    private MaterialButton btnCancelPairing;

    private Handler handler;
    private int currentStep = 0;
    private static final int TOTAL_STEPS = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_pairing);

        initializeViews();
        setupClickListeners();
        startPairingProcess();
    }

    private void initializeViews() {
        // Progress indicators
        ivStep1 = findViewById(R.id.ivStep1);
        ivStep2 = findViewById(R.id.ivStep2);
        ivStep3 = findViewById(R.id.ivStep3);
        ivStep4 = findViewById(R.id.ivStep4);

        pbStep1 = findViewById(R.id.pbStep1);
        pbStep2 = findViewById(R.id.pbStep2);
        pbStep3 = findViewById(R.id.pbStep3);
        pbStep4 = findViewById(R.id.pbStep4);

        tvPairingStatus = findViewById(R.id.tvPairingStatus);
        tvStep1 = findViewById(R.id.tvStep1);
        tvStep2 = findViewById(R.id.tvStep2);
        tvStep3 = findViewById(R.id.tvStep3);
        tvStep4 = findViewById(R.id.tvStep4);

        btnCancelPairing = findViewById(R.id.btnCancelPairing);

        handler = new Handler();
    }

    private void setupClickListeners() {
        btnCancelPairing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelPairing();
            }
        });
    }

    private void startPairingProcess() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (currentStep < TOTAL_STEPS) {
                    currentStep++;
                    updatePairingStep(currentStep);

                    // Continue to next step
                    handler.postDelayed(this, 2000); // 2 seconds between steps
                } else {
                    pairingCompleted();
                }
            }
        }, 1000); // Start after 1 second
    }

    private void updatePairingStep(int step) {
        switch (step) {
            case 1:
                completeStep(pbStep1, ivStep1, tvStep1, "Establishing connection");
                tvPairingStatus.setText("Connection established successfully");
                break;

            case 2:
                startStep(pbStep2, tvStep2, "Verifying device authenticity");
                tvPairingStatus.setText("Verifying Apple Watch...");

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        completeStep(pbStep2, ivStep2, tvStep2, "Verifying device authenticity");
                        tvPairingStatus.setText("Device verified successfully");
                    }
                }, 1500);
                break;

            case 3:
                startStep(pbStep3, tvStep3, "Syncing initial data");
                tvPairingStatus.setText("Syncing data with Apple Watch...");

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        completeStep(pbStep3, ivStep3, tvStep3, "Syncing initial data");
                        tvPairingStatus.setText("Data sync completed");
                    }
                }, 2500);
                break;

            case 4:
                startStep(pbStep4, tvStep4, "Finalizing setup");
                tvPairingStatus.setText("Finalizing connection...");

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        completeStep(pbStep4, ivStep4, tvStep4, "Finalizing setup");
                        tvPairingStatus.setText("Setup completed successfully");
                    }
                }, 1500);
                break;
        }
    }

    private void startStep(ProgressBar progressBar, TextView textView, String stepText) {
        progressBar.setVisibility(View.VISIBLE);
        textView.setTextColor(getResources().getColor(android.R.color.white));
    }

    private void completeStep(ProgressBar progressBar, ImageView imageView, TextView textView, String stepText) {
        progressBar.setVisibility(View.GONE);
        imageView.setVisibility(View.VISIBLE);
        textView.setTextColor(getResources().getColor(android.R.color.white));
    }

    private void pairingCompleted() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Navigate to success screen
                Intent intent = new Intent(AppleWatchPairingActivity.this, SimpleSuccessActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        }, 1000);
    }

    private void cancelPairing() {
        // Remove any pending callbacks
        handler.removeCallbacksAndMessages(null);

        // Show cancellation message
        tvPairingStatus.setText("Pairing cancelled");
        tvPairingStatus.setTextColor(getResources().getColor(android.R.color.holo_red_light));

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 1500);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }
}