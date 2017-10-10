package com.jc.sb.sb_customer_app;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jc.sb.controller.AppConnectivity;
import com.jc.sb.controller.SQLiteHandler;
import com.jc.sb.controller.SessionManager;
import com.jc.sb.helper.AppConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TopUpScreenActivity extends AppCompatActivity {

    private static final String TAG = TopUpScreenActivity.class.getSimpleName();

    // Defining views
    private EditText topupAmountText;
    private AppCompatButton buttonTopup;
    private ProgressDialog pDialog;

    private String user_id, amount;

    private SQLiteHandler db;
    private SessionManager session;

    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_up_screen);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.topupActivityCoordinatorLayout);

        // Initializing views
        topupAmountText = (EditText) findViewById(R.id.editTextTopupAmount);

        buttonTopup = (AppCompatButton) findViewById(R.id.buttonTopup);

        // SQLite db handler
        db = new SQLiteHandler(getApplicationContext());

        // Session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logout();
        }

        // Getting user details from SQLite db
        HashMap<String, String> user = db.getUserDetails();

        user_id = user.get("user_id");
        amount = user.get("balance");

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Adding click listener
        buttonTopup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Calling the topup function
                topupAttempt();
            }

        });
    }

    private void topupAttempt() {
        // Storing topup value.
        final String topupAmount = topupAmountText.getText().toString().trim();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid email address.
        if (TextUtils.isEmpty(topupAmount)) {
            topupAmountText.setError(getString(R.string.error_field_required));
            focusView = topupAmountText;
            cancel = true;

        }

        if (cancel) {
            // There was an error; don't attempt topup and focus the
            // form field with an error.
            focusView.requestFocus();

        }else {
            // Check Internet connection before topup attempt
            checkInternetConnection(topupAmount);
        }
    }

    private void checkInternetConnection(String topupAmount){
        // Flag for Internet Connection Status
        Boolean isInternetConn;

        // Connection Detector Class
        AppConnectivity connection;

        connection = new AppConnectivity(getApplicationContext());

        // Get Internet status
        isInternetConn = connection.isConnectingToInternet();

        // If we have Internet Connect
        if(isInternetConn){
            // Send user details to login
            topup(topupAmount);

        }else{
            // Show Warning Snackbar
            Snackbar snackbar = Snackbar.make(coordinatorLayout, "Error... Check your internet connection!", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void topup(String topupAmount){
        // Tag used to cancel the request
        String tag_string_req = "req_topup";

        pDialog.setMessage("Connecting to user account...");
        showDialog();

        final String sTopupAmount = topupAmount;

        // Getting the String values and cast them into double values
        final double dTopupAmount= Double.parseDouble(sTopupAmount);
        final double initialBalance = Double.parseDouble(amount);

        // Adding the topup to the user inital balance
        final double finalBalace = initialBalance + dTopupAmount;

        // Casting the user final balance again into String before send it
        // to the live server
        final String mTopupAmount = Double.toString(finalBalace);

        // Creating a string request
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, AppConfig.API_URL + AppConfig.USER_TOPUP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Topup Response: " + response.toString());
                        hideDialog();

                        try {
                            JSONObject jObj = new JSONObject(response);

                            boolean error = jObj.getBoolean("error");

                            // Check for error node in json
                            if (!error) {
                                // User credits successfully updated
                                // Force user to logout to update the new account balance
                                logout();

                            } else {
                                // Error on topup. Get the error message
                                String errorMsg = jObj.getString("error_msg");
                                Log.e(TAG, "Error Msg: " + errorMsg);
                                // If the server response is not success
                                // Displaying an error message on a Snackbar
                                Snackbar snackbar = Snackbar.make(coordinatorLayout, "Error... Top-up amount not sent!", Snackbar.LENGTH_LONG);
                                snackbar.show();
                            }
                        } catch (JSONException e) {
                            // JSON error
                            e.printStackTrace();
                            Log.e(TAG, "Json Error: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Topup Error: " + error.getMessage());
                        hideDialog();
                        // Show Warning Snackbar
                        Snackbar snackbar = Snackbar.make(coordinatorLayout, "Error... Top-up amount not sent!", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                // Adding parameters to request
                params.put(AppConfig.KEY_USER_ID, user_id);
                params.put(AppConfig.KEY_TOPUP_AMOUNT, mTopupAmount);

                // Returning parameter
                return params;
            }
        };

        // Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    //Logout function
    private void logout(){
        //Creating an alert dialog to force user to logout
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setIcon(R.drawable.logo);
        alertDialogBuilder.setTitle("Smart-Barista");
        alertDialogBuilder.setMessage("Top-up Success! You need to login again to update your new account balance!");
        alertDialogBuilder.setCancelable(false);

        alertDialogBuilder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface exitDialog, int which) {

                        session.setLogin(false);

                        db.deleteUser();

                        exitDialog.dismiss();

                        //Going back to 'Login Screen Activity'
                        Intent intent = new Intent(TopUpScreenActivity.this, LoginScreenActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });

        AlertDialog exitAlertDialog = alertDialogBuilder.create();
        exitAlertDialog.show();

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(TopUpScreenActivity.this, CategoryMenuScreenActivity.class);
        startActivity(intent);
        finish();
    }
}
