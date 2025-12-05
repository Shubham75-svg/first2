package com.example.first;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MenuPopup {

    private Context context;
    private Dialog dialog;
    private String userName;

    public MenuPopup(Context context, String userName) {
        this.context = context;
        this.userName = userName;
        createDialog();
    }

    private void createDialog() {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.menu_popup_layout);

        // Set user name
        TextView profileName = dialog.findViewById(R.id.profileName);
        if (userName != null && !userName.isEmpty()) {
            profileName.setText(userName);
        }

        setupClickListeners();
    }

    private void setupClickListeners() {
        // Profile item click
        LinearLayout profileItem = dialog.findViewById(R.id.profileItem);
        profileItem.setOnClickListener(v -> {
            Toast.makeText(context, "Profile Clicked", Toast.LENGTH_SHORT).show();
            dismiss();
        });

        // Settings item click
        LinearLayout settingsItem = dialog.findViewById(R.id.settingsItem);
        settingsItem.setOnClickListener(v -> {
            Toast.makeText(context, "Settings Clicked", Toast.LENGTH_SHORT).show();
            dismiss();
        });

        // Privacy item click
        LinearLayout privacyItem = dialog.findViewById(R.id.privacyItem);
        privacyItem.setOnClickListener(v -> {
            Toast.makeText(context, "Privacy Policy Clicked", Toast.LENGTH_SHORT).show();
            dismiss();
        });

        // Home item click - OPEN HEALTH SCORE ACTIVITY
        LinearLayout homeItem = dialog.findViewById(R.id.homeItem);
        homeItem.setOnClickListener(v -> {
            try {
                // Open HealthScoreActivity when home is clicked
                Intent intent = new Intent(context, HealthScoreActivity.class);
                context.startActivity(intent);
                dismiss();
                Toast.makeText(context, "Opening Health Dashboard", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(context, "Error opening Health Dashboard", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });

        // Logout item click
        LinearLayout logoutItem = dialog.findViewById(R.id.logoutItem);
        logoutItem.setOnClickListener(v -> showLogoutConfirmation());
    }

    private void showLogoutConfirmation() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout?");
        builder.setPositiveButton("Yes", (dialogInterface, which) -> {
            performLogout();
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void performLogout() {
        Toast.makeText(context, "Logged out successfully", Toast.LENGTH_SHORT).show();
        dismiss();
    }

    public void show() {
        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
        }
    }

    public void dismiss() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public boolean isShowing() {
        return dialog != null && dialog.isShowing();
    }
}