package com.example.first;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText usernameEditText, passwordEditText;
    private MaterialButton loginButton;
    private TextView forgotPasswordText, signUpText, signUpPrompt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Matches your XML

        initializeViews();
        setupClickListeners();

        Toast.makeText(this, "Welcome to Smart Billing System", Toast.LENGTH_SHORT).show();
    }

    private void initializeViews() {
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        forgotPasswordText = findViewById(R.id.forgotPasswordText);
        signUpText = findViewById(R.id.signUpText);
        signUpPrompt = findViewById(R.id.signUpPrompt);
    }

    private void setupClickListeners() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });

        forgotPasswordText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "Password reset feature coming soon!", Toast.LENGTH_SHORT).show();
            }
        });

        signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "Navigate to Sign Up page", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void attemptLogin() {
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            usernameEditText.setError("Please enter username or email");
            usernameEditText.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Please enter password");
            passwordEditText.requestFocus();
            return;
        }

        if (password.length() < 6) {
            passwordEditText.setError("Password must be at least 6 characters");
            passwordEditText.requestFocus();
            return;
        }

        if (isValidCredentials(username, password)) {
            Toast.makeText(this, "Login successful! Welcome Dr. Peshant Teake", Toast.LENGTH_SHORT).show();
            navigateToBillingReceipt();
        } else {
            Toast.makeText(this, "Invalid credentials. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isValidCredentials(String username, String password) {
        // Demo credentials
        String demoUsername = "doctor";
        String demoPassword = "password123";
        return (username.equals(demoUsername) && password.equals(demoPassword)) ||
                (username.contains("@") && password.length() >= 6);
    }

    private void navigateToBillingReceipt() {
        Intent intent = new Intent(LoginActivity.this, BillingReceiptActivity.class);
        intent.putExtra("username", usernameEditText.getText().toString().trim());
        startActivity(intent);
        finish();
    }
}
