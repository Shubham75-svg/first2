package com.example.first;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class SimpleSuccessActivity extends AppCompatActivity {

    private MaterialButton btnFinish, btnSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_pairing);

        btnFinish = findViewById(R.id.btnFinish);
        btnSettings = findViewById(R.id.btnSettings);

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go back to main screen
                Intent intent = new Intent(SimpleSuccessActivity.this, SimplePairingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SimpleSuccessActivity.this, "Settings will open here", Toast.LENGTH_SHORT).show();
            }
        });
    }
}