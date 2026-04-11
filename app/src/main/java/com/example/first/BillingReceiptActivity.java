package com.example.first;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.telephony.SmsManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

public class BillingReceiptActivity extends AppCompatActivity {

    // UI Components
    private MaterialButton panicButton;
    private MaterialCardView panicButtonCard;
    private MaterialCardView healthScoreCard;
    private ImageView menuIcon;
    private ImageView panicButtonImage;
    private TextView appTitle;

    // Emergency features
    private MediaPlayer alarmSound;
    private SmsManager smsManager;

    // Location services
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private Location currentLocation;
    private boolean isGettingLocation = false;

    // Permissions
    private static final int PERMISSION_REQUEST_CODE = 100;

    // Emergency contact number
    private final String EMERGENCY_CONTACT = "9356981969";

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
        initializeLocationServices();
        checkPermissions();

        // Welcome user
        if (username != null && !username.isEmpty()) {
            Toast.makeText(this, "Welcome " + username + "!", Toast.LENGTH_SHORT).show();
        }
    }

    private void initializeViews() {
        panicButton = findViewById(R.id.panicButton);
        panicButtonCard = findViewById(R.id.panicButtonCard);
        healthScoreCard = findViewById(R.id.healthScoreCard);
        menuIcon = findViewById(R.id.menuIcon);
        panicButtonImage = findViewById(R.id.panicButtonImage);
        appTitle = findViewById(R.id.appTitle);
    }

    private void setupClickListeners() {
        panicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                triggerEmergencyAlert();
            }
        });

        panicButtonCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                triggerEmergencyAlert();
            }
        });

        healthScoreCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHealthScoreDashboard();
            }
        });

        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenuPopup(v);
            }
        });
    }

    private void openHealthScoreDashboard() {
        Intent healthIntent = new Intent(BillingReceiptActivity.this, HealthScoreActivity.class);
        healthIntent.putExtra("username", currentUsername);
        startActivity(healthIntent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    private void showMenuPopup(View anchorView) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View menuView = inflater.inflate(R.layout.menu_popup_layout, null);

        PopupWindow popupWindow = new PopupWindow(
                menuView,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                true
        );

        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.background_gradient));
        popupWindow.setElevation(20f);

        TextView profileName = menuView.findViewById(R.id.profileName);
        LinearLayout profileItem = menuView.findViewById(R.id.profileItem);
        LinearLayout settingsItem = menuView.findViewById(R.id.settingsItem);
        LinearLayout privacyItem = menuView.findViewById(R.id.privacyItem);
        LinearLayout homeItem = menuView.findViewById(R.id.homeItem);
        LinearLayout logoutItem = menuView.findViewById(R.id.logoutItem);

        profileName.setText(currentUsername);

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

        popupWindow.showAsDropDown(anchorView, 0, 0, Gravity.START);
    }

    private void showProfile() {
        Toast.makeText(this, "Profile: " + currentUsername, Toast.LENGTH_SHORT).show();
    }

    private void openSettings() {
        Intent settingsIntent = new Intent(this, EmergencySettingsActivity.class);
        startActivity(settingsIntent);
    }

    private void showPrivacyPolicy() {
        Toast.makeText(this, "Privacy Policy", Toast.LENGTH_SHORT).show();
        try {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://yourapp.com/privacy"));
            startActivity(browserIntent);
        } catch (Exception e) {
            Toast.makeText(this, "Cannot open browser", Toast.LENGTH_SHORT).show();
        }
    }

    private void goToHome() {
        Toast.makeText(this, "Already on home screen", Toast.LENGTH_SHORT).show();
    }

    private void logout() {
        Toast.makeText(this, "Logging out...", Toast.LENGTH_SHORT).show();

        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();

        Intent loginIntent = new Intent(this, LoginActivity.class);
        startActivity(loginIntent);
        finish();
    }

    private void initializeEmergencyFeatures() {
        smsManager = SmsManager.getDefault();

        try {
            alarmSound = MediaPlayer.create(this, android.provider.Settings.System.DEFAULT_NOTIFICATION_URI);
        } catch (Exception e) {
            alarmSound = new MediaPlayer();
        }
    }

    private void initializeLocationServices() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    currentLocation = location;
                    if (isGettingLocation) {
                        isGettingLocation = false;
                        sendEmergencySMSWithLocation();
                        stopLocationUpdates();
                    }
                }
            }
        };
    }

    private void checkPermissions() {
        String[] requiredPermissions = {
                Manifest.permission.SEND_SMS,
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

        // Play alarm sound
        playAlarmSound();

        // Get live location and send SMS
        getLiveLocationAndSendSMS();

        // Open nearby hospitals in Google Maps
        openNearbyHospitals();

        // Show confirmation
        showEmergencyActivated();
    }

    private void getLiveLocationAndSendSMS() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Location permission required to send live location", Toast.LENGTH_SHORT).show();
            sendEmergencySMSWithoutLocation();
            return;
        }

        isGettingLocation = true;
        Toast.makeText(this, "Getting your live location...", Toast.LENGTH_SHORT).show();

        LocationRequest locationRequest = new LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY,
                5000
        )
                .setWaitForAccurateLocation(true)
                .setMinUpdateIntervalMillis(2000)
                .setMaxUpdateDelayMillis(10000)
                .build();

        fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
        );

        // Timeout after 10 seconds
        panicButton.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isGettingLocation) {
                    isGettingLocation = false;
                    stopLocationUpdates();
                    sendEmergencySMSWithoutLocation();
                    Toast.makeText(BillingReceiptActivity.this,
                            "Could not get live location. Sending emergency alert without location.",
                            Toast.LENGTH_LONG).show();
                }
            }
        }, 10000);
    }

    private void stopLocationUpdates() {
        if (fusedLocationClient != null && locationCallback != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }

    private void sendEmergencySMSWithLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                == PackageManager.PERMISSION_GRANTED) {

            String mapsUrl = "";
            String coordinates = "";

            if (currentLocation != null) {
                double latitude = currentLocation.getLatitude();
                double longitude = currentLocation.getLongitude();
                coordinates = latitude + "," + longitude;
                mapsUrl = "http://maps.google.com/?q=" + coordinates;
            }

            String emergencyMessage = "EMERGENCY! Please help. My location: " + mapsUrl;

            // Also send a second message with just coordinates
            String coordinatesMessage = "EMERGENCY! My coordinates: " + coordinates;

            try {
                // Send SMS to the emergency contact number
                smsManager.sendTextMessage(EMERGENCY_CONTACT, null, emergencyMessage, null, null);

                // Send second message with coordinates only
                if (!coordinates.isEmpty()) {
                    smsManager.sendTextMessage(EMERGENCY_CONTACT, null, coordinatesMessage, null, null);
                }

                Toast.makeText(this, "Emergency SMS sent to " + EMERGENCY_CONTACT, Toast.LENGTH_LONG).show();

                // Also log for debugging
                android.util.Log.d("EmergencyAlert", "SMS sent to: " + EMERGENCY_CONTACT);
                android.util.Log.d("EmergencyAlert", "Message: " + emergencyMessage);

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to send SMS: " + e.getMessage(), Toast.LENGTH_LONG).show();
                android.util.Log.e("EmergencyAlert", "Error sending SMS", e);
            }
        } else {
            Toast.makeText(this, "SMS permission required to send alerts", Toast.LENGTH_SHORT).show();
            requestSMSPermission();
        }
    }

    private void sendEmergencySMSWithoutLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                == PackageManager.PERMISSION_GRANTED) {

            String emergencyMessage = "EMERGENCY! Please help. I need immediate assistance. Location services are unavailable. Please call me immediately!";

            try {
                smsManager.sendTextMessage(EMERGENCY_CONTACT, null, emergencyMessage, null, null);
                Toast.makeText(this, "Emergency SMS sent to " + EMERGENCY_CONTACT, Toast.LENGTH_LONG).show();
                android.util.Log.d("EmergencyAlert", "SMS sent to: " + EMERGENCY_CONTACT);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to send SMS: " + e.getMessage(), Toast.LENGTH_LONG).show();
                android.util.Log.e("EmergencyAlert", "Error sending SMS", e);
            }
        } else {
            Toast.makeText(this, "SMS permission required to send alerts", Toast.LENGTH_SHORT).show();
            requestSMSPermission();
        }
    }

    private void requestSMSPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.SEND_SMS},
                PERMISSION_REQUEST_CODE);
    }

    private void flashScreen() {
        panicButtonCard.setCardBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
        panicButtonCard.postDelayed(new Runnable() {
            @Override
            public void run() {
                panicButtonCard.setCardBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
            }
        }, 200);
    }

    private void animateButtonPress() {
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

    private void openNearbyHospitals() {
        try {
            Uri gmmIntentUri = Uri.parse("geo:0,0?q=hospitals");
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");

            if (mapIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(mapIntent);
                Toast.makeText(this, "Opening nearby hospitals in Google Maps", Toast.LENGTH_SHORT).show();
            } else {
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

    private void showEmergencyActivated() {
        Toast.makeText(this, "🚨 EMERGENCY ALERT ACTIVATED! 🚨\nLive location sent to " + EMERGENCY_CONTACT + "!\nHospitals map opened!", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (alarmSound != null) {
            alarmSound.release();
        }
        stopLocationUpdates();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (alarmSound != null && alarmSound.isPlaying()) {
            alarmSound.pause();
        }
    }
}