package com.example.first;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.telephony.SmsManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

public class BillingReceiptActivity extends AppCompatActivity {

    // UI Components
    private MaterialButton panicButton;
    private MaterialCardView panicButtonCard;
    private MaterialCardView healthScoreCard; // Added Health Score Card
    private ImageView menuIcon;
    private ImageView panicButtonImage;
    private TextView appTitle;

    // Emergency features
    private MediaPlayer alarmSound;
    private Vibrator vibrator;
    private SmsManager smsManager;

    // Permissions
    private static final int PERMISSION_REQUEST_CODE = 100;

    // Emergency contacts (replace with actual family member numbers)
    private final String[] FAMILY_CONTACTS = {
            "9356981969", // Replace with actual family contact 1
            "9356981969"  // Replace with actual family contact 2
    };

    // Current user
    private String currentUsername = "User";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing_receipt);

        // Get username from login
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        if (username != null && !username.isEmpty()) {
            currentUsername = username;
        }

        initializeViews();
        setupClickListeners();
        initializeEmergencyFeatures();
        checkPermissions();

        // Welcome user
        if (username != null && !username.isEmpty()) {
            Toast.makeText(this, "Welcome " + username + "!", Toast.LENGTH_SHORT).show();
        }
    }

    private void initializeViews() {
        // Initialize UI components
        panicButton = findViewById(R.id.panicButton);
        panicButtonCard = findViewById(R.id.panicButtonCard);
        healthScoreCard = findViewById(R.id.healthScoreCard); // Initialize Health Score Card
        menuIcon = findViewById(R.id.menuIcon);
        panicButtonImage = findViewById(R.id.panicButtonImage);
        appTitle = findViewById(R.id.appTitle);
    }

    private void setupClickListeners() {
        // Panic Button Click Listener
        panicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                triggerEmergencyAlert();
            }
        });

        // Panic Card Click Listener (for larger click area)
        panicButtonCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                triggerEmergencyAlert();
            }
        });

        // Health Score Card Click Listener - NEW
        healthScoreCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHealthScoreDashboard();
            }
        });

        // Menu Icon Click Listener - Show dropdown menu
        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenuPopup(v);
            }
        });
    }

    // NEW METHOD: Open Health Score Dashboard
    private void openHealthScoreDashboard() {
        Intent healthIntent = new Intent(BillingReceiptActivity.this, HealthScoreActivity.class);
        healthIntent.putExtra("username", currentUsername);
        startActivity(healthIntent);

        // Optional: Add animation
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    private void showMenuPopup(View anchorView) {
        // Inflate the menu layout
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View menuView = inflater.inflate(R.layout.menu_popup_layout, null);

        // Create the popup window
        PopupWindow popupWindow = new PopupWindow(
                menuView,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                true
        );

        // Set background and elevation
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.background_gradient));
        popupWindow.setElevation(20f);

        // Initialize menu items
        TextView profileName = menuView.findViewById(R.id.profileName);
        LinearLayout profileItem = menuView.findViewById(R.id.profileItem);
        LinearLayout settingsItem = menuView.findViewById(R.id.settingsItem);
        LinearLayout privacyItem = menuView.findViewById(R.id.privacyItem);
        LinearLayout homeItem = menuView.findViewById(R.id.homeItem);
        LinearLayout logoutItem = menuView.findViewById(R.id.logoutItem);

        // Set profile name
        profileName.setText(currentUsername);

        // Set click listeners for menu items
        profileItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProfile();
                popupWindow.dismiss();
            }
        });

        settingsItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSettings();
                popupWindow.dismiss();
            }
        });

        privacyItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPrivacyPolicy();
                popupWindow.dismiss();
            }
        });

        homeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToHome();
                popupWindow.dismiss();
            }
        });

        logoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
                popupWindow.dismiss();
            }
        });

        // Show the popup window
        popupWindow.showAsDropDown(anchorView, 0, 0, Gravity.START);
    }

    private void showProfile() {
        // Show user profile
        Toast.makeText(this, "Profile: " + currentUsername, Toast.LENGTH_SHORT).show();

        // You can implement a profile activity here
        // Intent profileIntent = new Intent(this, ProfileActivity.class);
        // startActivity(profileIntent);
    }

    private void openSettings() {
        // Open emergency settings
        Intent settingsIntent = new Intent(this, EmergencySettingsActivity.class);
        startActivity(settingsIntent);
    }

    private void showPrivacyPolicy() {
        // Show privacy policy
        Toast.makeText(this, "Privacy Policy", Toast.LENGTH_SHORT).show();

        // You can open a web view or another activity for privacy policy
        // Intent privacyIntent = new Intent(this, PrivacyPolicyActivity.class);
        // startActivity(privacyIntent);

        // Or open in browser
        try {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://yourapp.com/privacy"));
            startActivity(browserIntent);
        } catch (Exception e) {
            Toast.makeText(this, "Cannot open browser", Toast.LENGTH_SHORT).show();
        }
    }

    private void goToHome() {
        // Go to home/main screen
        Toast.makeText(this, "Already on home screen", Toast.LENGTH_SHORT).show();
        // If you have multiple activities, you can navigate to main home
        // Intent homeIntent = new Intent(this, MainActivity.class);
        // startActivity(homeIntent);
        // finish();
    }

    private void logout() {
        // Logout user and go back to login
        Toast.makeText(this, "Logging out...", Toast.LENGTH_SHORT).show();

        // Clear any user data if needed
        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();

        // Go back to login activity
        Intent loginIntent = new Intent(this, LoginActivity.class);
        startActivity(loginIntent);
        finish();
    }

    // Rest of your existing emergency methods remain the same...
    private void initializeEmergencyFeatures() {
        // Initialize SMS Manager
        smsManager = SmsManager.getDefault();

        // Initialize alarm sound - use system default notification sound
        try {
            alarmSound = MediaPlayer.create(this, android.provider.Settings.System.DEFAULT_NOTIFICATION_URI);
        } catch (Exception e) {
            // Fallback: create a basic media player
            alarmSound = new MediaPlayer();
        }

        // Initialize vibrator
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
    }

    private void checkPermissions() {
        String[] requiredPermissions = {
                Manifest.permission.SEND_SMS,
                Manifest.permission.VIBRATE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };

        boolean allPermissionsGranted = true;
        for (String permission : requiredPermissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                allPermissionsGranted = false;
                break;
            }
        }

        if (!allPermissionsGranted) {
            ActivityCompat.requestPermissions(this, requiredPermissions, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            boolean allGranted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allGranted = false;
                    break;
                }
            }

            if (allGranted) {
                Toast.makeText(this, "All emergency permissions granted!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Some emergency features may not work without permissions", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void triggerEmergencyAlert() {
        // Visual feedback
        flashScreen();
        animateButtonPress();

        // Sound alarm
        playAlarmSound();

        // Vibrate phone
        vibratePhone();

        // Send emergency SMS to family members
        sendEmergencySMS();

        // Open nearby hospitals in Google Maps
        openNearbyHospitals();

        // Show confirmation
        showEmergencyActivated();
    }

    private void flashScreen() {
        // Flash screen red temporarily
        panicButtonCard.setCardBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
        panicButtonCard.postDelayed(new Runnable() {
            @Override
            public void run() {
                panicButtonCard.setCardBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
            }
        }, 200);
    }

    private void animateButtonPress() {
        // Scale animation for button press feedback
        panicButton.animate().scaleX(0.95f).scaleY(0.95f).setDuration(100)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        panicButton.animate().scaleX(1f).scaleY(1f).setDuration(100).start();
                    }
                }).start();
    }

    private void playAlarmSound() {
        if (alarmSound != null) {
            try {
                if (!alarmSound.isPlaying()) {
                    alarmSound.start();
                }

                // Stop alarm after 10 seconds
                panicButton.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (alarmSound != null && alarmSound.isPlaying()) {
                            alarmSound.pause();
                        }
                    }
                }, 10000);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Emergency alert activated!", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void vibratePhone() {
        if (vibrator != null && vibrator.hasVibrator()) {
            long[] vibrationPattern = {0, 1000, 500, 1000, 500, 1000};

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createWaveform(vibrationPattern, 0));
            } else {
                vibrator.vibrate(vibrationPattern, 0);
            }

            // Stop vibration after 10 seconds
            panicButton.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (vibrator != null) {
                        vibrator.cancel();
                    }
                }
            }, 10000);
        }
    }

    private void sendEmergencySMS() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                == PackageManager.PERMISSION_GRANTED) {

            String emergencyMessage = "🚨 EMERGENCY ALERT! 🚨\n\n" +
                    "I need immediate medical help!\n" +
                    "Emergency alert activated from Made Alert app.\n" +
                    "Please contact me immediately and come to my location.\n\n" +
                    "My approximate location: " + getLastKnownLocation() + "\n" +
                    "Time: " + java.text.DateFormat.getDateTimeInstance().format(new java.util.Date()) + "\n\n" +
                    "Sent via Made Alert Emergency System";

            boolean smsSent = false;
            for (String contact : FAMILY_CONTACTS) {
                try {
                    if (!contact.trim().isEmpty() && contact.length() > 5) {
                        smsManager.sendTextMessage(contact, null, emergencyMessage, null, null);
                        smsSent = true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (smsSent) {
                Toast.makeText(this, "Emergency SMS sent to family members", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "No valid family contacts configured", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "SMS permission required to send alerts", Toast.LENGTH_SHORT).show();
        }
    }

    private void openNearbyHospitals() {
        try {
            // Create URI for nearby hospitals search in Google Maps
            Uri gmmIntentUri = Uri.parse("geo:0,0?q=hospitals");

            // Create intent to open Google Maps
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");

            // Check if Google Maps is installed
            if (mapIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(mapIntent);
                Toast.makeText(this, "Opening nearby hospitals in Google Maps", Toast.LENGTH_SHORT).show();
            } else {
                // Fallback: open in browser
                Uri webIntentUri = Uri.parse("https://www.google.com/maps/search/hospitals");
                Intent webIntent = new Intent(Intent.ACTION_VIEW, webIntentUri);
                startActivity(webIntent);
                Toast.makeText(this, "Opening nearby hospitals in browser", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Could not open maps app", Toast.LENGTH_SHORT).show();
        }
    }

    private String getLastKnownLocation() {
        return "Location services activated. Please check GPS coordinates in maps.";
    }

    private void showEmergencyActivated() {
        Toast.makeText(this, "🚨 EMERGENCY ALERT ACTIVATED! 🚨\nHospitals map opened!\nFamily notified!", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Clean up resources
        if (alarmSound != null) {
            alarmSound.release();
        }

        if (vibrator != null) {
            vibrator.cancel();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Stop alarm when app goes to background
        if (alarmSound != null && alarmSound.isPlaying()) {
            alarmSound.pause();
        }
    }
}