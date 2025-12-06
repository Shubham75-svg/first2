package com.example.first;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText fullNameEditText, emailEditText, passwordEditText, confirmPasswordEditText;
    private MaterialButton registerButton;
    private TextView loginText;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();
        initializeViews();
        setupListeners();
    }

    private void initializeViews() {
        fullNameEditText = findViewById(R.id.fullNameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.registerPasswordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        registerButton = findViewById(R.id.registerButton);
        loginText = findViewById(R.id.loginText);
    }

    private void setupListeners() {
        registerButton.setOnClickListener(v -> createAccount());

        loginText.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void createAccount() {
        String fullName = fullNameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        // Validation
        if (TextUtils.isEmpty(fullName)) {
            fullNameEditText.setError("Enter full name");
            return;
        }

        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Enter email address");
            return;
        }

        if (!isValidEmail(email)) {
            emailEditText.setError("Enter valid email address");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Enter password");
            return;
        }

        if (password.length() < 6) {
            passwordEditText.setError("Password must be at least 6 characters");
            return;
        }

        if (!isValidPassword(password)) {
            passwordEditText.setError("Password must contain letters and numbers");
            return;
        }

        if (TextUtils.isEmpty(confirmPassword)) {
            confirmPasswordEditText.setError("Confirm your password");
            return;
        }

        if (!password.equals(confirmPassword)) {
            confirmPasswordEditText.setError("Passwords do not match");
            return;
        }

        // Show loading state
        registerButton.setEnabled(false);
        registerButton.setText("Creating Account...");

        // Create user with Firebase
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    // Reset button state
                    registerButton.setEnabled(true);
                    registerButton.setText("Create Account");

                    if (task.isSuccessful()) {
                        // Registration successful
                        Toast.makeText(RegisterActivity.this,
                                "Account created successfully!", Toast.LENGTH_SHORT).show();

                        // You can optionally save the full name to Firebase Realtime Database or Firestore
                        // saveUserData(fullName, email);

                        // Redirect to login page
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        intent.putExtra("email", email); // Optional: Pass email to pre-fill login
                        startActivity(intent);
                        finish();
                    } else {
                        // Registration failed
                        String errorMessage = task.getException().getMessage();
                        if (errorMessage.contains("email address is already in use")) {
                            emailEditText.setError("Email already registered");
                        } else if (errorMessage.contains("invalid email")) {
                            emailEditText.setError("Invalid email format");
                        } else {
                            Toast.makeText(RegisterActivity.this,
                                    "Error: " + errorMessage, Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidPassword(String password) {
        // Password should contain at least one letter and one number
        boolean hasLetter = false;
        boolean hasDigit = false;

        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) hasLetter = true;
            if (Character.isDigit(c)) hasDigit = true;
            if (hasLetter && hasDigit) return true;
        }
        return hasLetter && hasDigit;
    }

    // Optional: Save additional user data to Firebase Database
    /*
    private void saveUserData(String fullName, String email) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("users");
            User userData = new User(fullName, email);
            databaseRef.child(user.getUid()).setValue(userData);
        }
    }
    */
}