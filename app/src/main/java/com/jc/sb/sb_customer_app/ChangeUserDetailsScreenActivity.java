package com.jc.sb.sb_customer_app;

import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.jc.sb.controller.SQLiteHandler;

import java.util.HashMap;

public class ChangeUserDetailsScreenActivity extends AppCompatActivity
{
    private static final String TAG = LoginScreenActivity.class.getSimpleName();

    // Defining views
    private EditText editTextCurrentPassword;
    private EditText editTextNewPassword;
    private EditText editTextConfirmNewPassword;
    private AppCompatButton buttonUpdate;
    private SQLiteHandler db;

    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_user_details_screen);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.changeDetailsActivityCoordinatorLayout);

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Getting user details from SQLite db
        HashMap<String, String> user = db.getUserDetails();

        // Initializing views
        editTextCurrentPassword = (EditText) findViewById(R.id.editTextCurrentPassword);
        editTextNewPassword = (EditText) findViewById(R.id.editTextNewPassword);
        editTextConfirmNewPassword = (EditText) findViewById(R.id.editTextConfirmPassword);
        buttonUpdate = (AppCompatButton) findViewById(R.id.buttonUpdate);

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeDetails();
            }
        });

    }

    private void changeDetails()
    {
        View focusView = null;
        // Store values at the time of the password change attempt
        final String currentPassword = editTextCurrentPassword.getText().toString().trim();
        final String newPassword = editTextNewPassword.getText().toString().trim();
        final String confirmNewPassword = editTextConfirmNewPassword.getText().toString().trim();

        if (TextUtils.isEmpty(currentPassword))
        {
            editTextCurrentPassword.setError(getString(R.string.error_field_required));
            focusView = editTextCurrentPassword;
            focusView.requestFocus();
        }
        else if (TextUtils.isEmpty(newPassword))
        {
            editTextNewPassword.setError(getString(R.string.error_field_required));
            focusView = editTextNewPassword;
            focusView.requestFocus();
        }
        else if (TextUtils.isEmpty(confirmNewPassword))
        {
            editTextNewPassword.setError(getString(R.string.error_field_required));
            focusView = editTextConfirmNewPassword;
            focusView.requestFocus();
        }
        else if(!newPassword.equals(confirmNewPassword))
        {
            Snackbar snackbar = Snackbar.make(coordinatorLayout, "Your new password doesn't match, try again!", Snackbar.LENGTH_LONG);
            snackbar.show();

            editTextNewPassword.setText("");
            editTextConfirmNewPassword.setText("");
            focusView = editTextNewPassword;
            focusView.requestFocus();
        }
        else
        {
            // Update password
        }
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(ChangeUserDetailsScreenActivity.this, CategoryMenuScreenActivity.class);
        startActivity(intent);
        finish();
    }

}
