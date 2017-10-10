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

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginScreenActivity extends AppCompatActivity {

    /**
     * @startuml
     * skinparam classAttributeIconSize 0
     * class Activity
     * Activity : +startActivity(intent : Intent) : void
     * Activity : #onCreate(savedInstanceState : Bundle) : void
     * class SplashActivity
     * Activity <|-- SplashActivity
     * SplashActivity : +task : TimerTask
     * SplashActivity : +intent : Intent
     * SplashActivity : #onCreate(savedInstanceState : Bundle) : void
     * SplashActivity : - onBackPressed() : void
     * class TimerTask --() Runnable
     * TimerTask : +schedule(task : TimerTask, delay : long) : void
     * SplashActivity "1" *-- "1" TimerTask
     * SplashActivity "1" *-- "1..*" Intent
     * class Intent --()Parcelable
     * class Intent ---()Cloneable
     * Intent : +Intent(packageContext : Context, cls : Class<?>)
     * class LoginActivity
     * LoginActivity : - editTextEmail : EditText
     * LoginActivity : - editTextPassword : EditText
     * LoginActivity : - buttonLogin : AppCompatButton
     * LoginActivity : - buttonRegister : AppCompatButton
     * LoginActivity : - session : SessionManager
     * LoginActivity : - db : SQLiteHandler
     * LoginActivity : - coordinatorLayout : CoordinatorLayout
     * LoginActivity : - user : JSONObject
     * LoginActivity : # onCreate(savedInstanceState : Bundle) : void
     * LoginActivity : - attemptLogin() : void
     * LoginActivity : - login(email : String, password : String) : void
     * LoginActivity : - isEmailValid(email : String) : boolean
     * LoginActivity : - isPasswordValid(password : String) : boolean
     * LoginActivity : - onBackPressed() : void
     * Activity <|-- LoginActivity
     * LoginActivity "1" -- "1" User
     * LoginActivity "1" *-- "1..*" Intent
     * class RegisterActivity
     * RegisterActivity : - buttonLogin : AppCompatButton
     * RegisterActivity : - buttonRegister : AppCompatButton
     * RegisterActivity : - inputEmail : EditText
     * RegisterActivity : - inputFirstName : EditText
     * RegisterActivity : - inputLastName : EditText
     * RegisterActivity : - inputPassword : EditText
     * RegisterActivity : - inputConfirmPassword : EditText
     * RegisterActivity : - inputSubscriptionType : Spinner
     * RegisterActivity : - dateOfBirth : DatePickerDialog
     * RegisterActivity : - isOver13 : boolean
     * RegisterActivity : - user : JSONObject
     * RegisterActivity : - session : SessionManager
     * RegisterActivity : - db : SQLiteHandler
     * RegisterActivity : # onCreate(savedInstanceState : Bundle) : void
     * RegisterActivity : - attemptRegister() : void
     * RegisterActivity : - isEmailValid(email : String) : boolean
     * RegisterActivity : - isPasswordValid(password : String) : boolean
     * RegisterActivity : - isPasswordConfirmed(password : String, confirmPassword : String) : boolean
     * RegisterActivity : - register(email : String, password : String, firstName : String, lastName : String, subscriptionType : String, dateOfBirth : String) : void
     * RegisterActivity : - onBackPressed() : void
     * Activity <|-- RegisterActivity
     * RegisterActivity "1" *-- "1..*" Intent
     * RegisterActivity "1" -- "1" User
     * class User
     * User : - email : String
     * User : - password : String
     * User : - firstName : String
     * User : - lastName: String
     * User : - subscriptionType: String
     * User : - dateOfBirth: String
     * User : + setEmail(email : String) : void
     * User : + getEmail() : String
     * User : + setPassword(password : String) : void
     * User : + getPassword() : String
     * User : + setFirstName(firstName : String) : void
     * User : + getFirstName() : String
     * User : + setLastName(lastName : String) : void
     * User : + getLastName() : String
     * User : + setSubscriptionType(subscriptionType: String) : void
     * User : + getSubscriptionType() : String
     * User : + setDateOfBirth(setDateOfBirth: String) : void
     * User : + getDateOfBirth() : String
     * class UpdateDetailsActivity
     * UpdateDetailsActivity : - editTextCurrentPassword : EditText
     * UpdateDetailsActivity : - editTextNewPassword : EditText
     * UpdateDetailsActivity : - editTextConfirmNewPassword : EditText
     * UpdateDetailsActivity : - editTextNewEmail: EditText
     * UpdateDetailsActivity : - editTextConfirmNewEmail: EditText
     * UpdateDetailsActivity : - buttonUpdate : AppCompatButton
     * UpdateDetailsActivity : - db : SQLiteHandler
     * UpdateDetailsActivity : - coordinatorLayout : CoordinatorLayout
     * UpdateDetailsActivity : # onCreate(savedInstanceState : Bundle) : void
     * UpdateDetailsActivity : - attemptChangeDetails() : void
     * UpdateDetailsActivity : - isPasswordValid(password : String) : boolean
     * UpdateDetailsActivity : - isNewPasswordValid(newPassword : String) : boolean
     * UpdateDetailsActivity : - isNewPasswordConfirmed(newPassword : String, confirmNewPassword : String) : boolean
     * UpdateDetailsActivity : - isNewEmailValid(email : String) : boolean
     * UpdateDetailsActivity : - changeDetails(newPassword : String) : void
     * UpdateDetailsActivity : - changeDetails(newEmail : String) : void
     * UpdateDetailsActivity : - changeDetails(newPassword : String, newEmail : String) : void
     * UpdateDetailsActivity : - onBackPressed() : void
     * Activity <|-- UpdateDetailsActivity
     * UpdateDetailsActivity "1" *-- "1..*" Intent
     * class NewsFeedActivity
     * NewsFeedActivity : - newsListView : ListView
     * NewsFeedActivity : - relativeLayout : RelativeLayout
     * NewsFeedActivity : - newsList : ArrayList<HashMap<String, News>>
     * NewsFeedActivity : - news : News
     * NewsFeedActivity : # onCreate(savedInstanceState : Bundle) : void
     * NewsFeedActivity : - listNews() : void
     * NewsFeedActivity : - onListViewItemSelected() : void
     * NewsFeedActivity : - onBackPressed() : void
     * class News
     * News : - title : String
     * News : - shortDescription : String
     * News : - url : String
     * News : + setTitle(title : String) : void
     * News : + getTitle() : String
     * News : + setShortDescription(description : String) : void
     * News : + getShortDescription() : String
     * News : + setUrl(url : String) : void
     * News : + getUrl() : String
     * NewsFeedActivity "1" o-- "0..*" News
     * Activity <|-- NewsFeedActivity
     * NewsFeedActivity "1" *-- "1..*" Intent
     * class TeamInfoActivity
     * TeamInfoActivity : - playersListView : ListView
     * TeamInfoActivity : - relativeLayout : RelativeLayout
     * TeamInfoActivity : - db : SQLiteHandler
     * TeamInfoActivity : - playersList : ArrayList<HashMap<String, Player>>
     * TeamInfoActivity : - player : Player
     * TeamInfoActivity : # onCreate(savedInstanceState : Bundle) : void
     * TeamInfoActivity : - listPlayers() : void
     * TeamInfoActivity : - onBackPressed() : void
     * class Player
     * Player : - name : String
     * Player : - position : String
     * Player : - number : int
     * Player : - goals : int
     * Player : - yellowCards : int
     * Player : - redCards : int
     * Player : + setName(name : String) : void
     * Player : + getName() : String
     * Player : + setPosition(position : String) : void
     * Player : + getPosition() : String
     * Player : + setNumber(number : int) : void
     * Player : + getNumber() : int
     * Player : + setGoals(goals : int) : void
     * Player : + getGoals() : int
     * Player : + setYellowCards(yellowCards : int) : void
     * Player : + getYellowCards() : int
     * Player : + setRedCards(redCards : int) : void
     * Player : + getRedCards() : int
     * TeamInfoActivity "1" o-- "11..*" Player
     * note on link #yellow
     * Team must have
     * at least 11 players
     * end note
     * Activity <|-- TeamInfoActivity
     * TeamInfoActivity "1" *-- "1..*" Intent
     * class MatchInfoActivity
     * MatchInfoActivity : - fixturesListView : ListView
     * MatchInfoActivity : - relativeLayout : RelativeLayout
     * MatchInfoActivity : - fixturesList : ArrayList<HashMap<String, Match>>
     * MatchInfoActivity : - fixture : Match
     * MatchInfoActivity : # onCreate(savedInstanceState : Bundle) : void
     * MatchInfoActivity : - listFixtures() : void
     * MatchInfoActivity : - onBackPressed() : void
     * Activity <|-- MatchInfoActivity
     * MatchInfoActivity "1" *-- "1..*" Intent
     * class Match
     * MatchInfoActivity "1" o-- "0..*" Match
     * Match : - dateAndTime : Date
     * Match : - league : String
     * Match : - venue : String
     * Match : - homeTeam : String
     * Match : - awayTeam : String
     * Match : - homeGoals : int
     * Match : - awayGoals : int
     * Match : + setDateAndTime(date : Date) : void
     * Match : + getDateAndTime() : Date
     * Match : + setLeague(league : String) : void
     * Match : + getLeague() : String
     * Match : + setVenue(venue : String) : void
     * Match : + getVenue() : String
     * Match : + setHomeTeam(homeTeam : String) : void
     * Match : + getHomeTeam() : String
     * Match : + setAwayTeam(awayTeam : String) : void
     * Match : + getAwayTeam() : String
     * Match : + setHomeGoals(homeGoals : int) : void
     * Match : + getHomeGoals() : int
     * Match : + setAwayGoals(awayGoals : int) : void
     * Match : + getAwayGoals() : int
     * class LeagueTableActivity
     * LeagueTableActivity : - tableListView : ListView
     * LeagueTableActivity : - coordinatorLayout : CoordinatorLayout
     * LeagueTableActivity : - teamList : ArrayList<HashMap<String, Team>>
     * LeagueTableActivity : - team : Team
     * LeagueTableActivity : - displayTable() : void
     * LeagueTableActivity : - onBackPressed() : void
     * class Team
     * LeagueTableActivity "1" o-- "10" Team
     * note on link #yellow
     * There are 10 teams
     * in Scottish Championship
     * end note
     * Team : - name : String
     * Team : - points : int
     * Team : - wins : int
     * Team : - draws : int
     * Team : - losses : int
     * Team : - goalsScored : int
     * Team : - goalsConceded : int
     * Team : - goalDifference : int
     * Team : + setName(name : String) : void
     * Team : + getName() : String
     * Team : + setPoints(points : int) : void
     * Team : + getPoints() : String
     * Team : + setWins(wins : int) : void
     * Team : + getWins() : String
     * Team : + setDraws(draws : int) : void
     * Team : + getDraws() : String
     * Team : + setLosses(losses : int) : void
     * Team : + getLosses() : String
     * Team : + setGoalsScored(goalsScored : int) : void
     * Team : + getGoalsScored() : String
     * Team : + setGoalsConceded(goalsConceded : int) : void
     * Team : + getGoalsConceded() : String
     * Team : + setGoalDifference(goalDifference : int) : void
     * Team : + getGoalDifference() : String
     * Activity <|-- LeagueTableActivity
     * LeagueTableActivity "1" *-- "1..*" Intent
     * class SessionManager
     * SessionManager : # pref : SharedPreferences
     * SessionManager : # editor : Editor
     * SessionManager : # context : Context
     * SessionManager : # private_mode : int
     * SessionManager : - PREF_NAME : String = "sf-api"
     * SessionManager : - KEY_IS_LOGGED_IN : String = "isLoggedIn"
     * SessionManager : + SessionManager(context : Context)
     * SessionManager : + setLogin(isLoggedIn : boolean) : void
     * SessionManager : + isLoggedIn() : void
     * LoginActivity "1" o-- "1" SessionManager
     * @enduml
     */

    private static final String TAG = LoginScreenActivity.class.getSimpleName();

    // Defining views
    private EditText editTextEmail;
    private EditText editTextPassword;
    private AppCompatButton buttonLogin;
    private AppCompatButton buttonRegister;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;

    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.loginActivityCoordinatorLayout);

        // Initializing views
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);

        buttonLogin = (AppCompatButton) findViewById(R.id.buttonLogin);
        buttonRegister = (AppCompatButton) findViewById(R.id.buttonLinkToRegister);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Session manager
        session = new SessionManager(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(LoginScreenActivity.this, CategoryMenuScreenActivity.class);
            startActivity(intent);
            finish();
        }

        // Adding click listeners
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Calling the login function
                attemptLogin();
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Calling the Register Screen Activity
                Intent intent = new Intent(LoginScreenActivity.this, RegisterScreenActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    /**
     * Attempts to sign in the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        // Store values at the time of the login attempt.
        final String email = editTextEmail.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError(getString(R.string.error_field_required));
            focusView = editTextEmail;
            cancel = true;

        } else if (!isEmailValid(email)) {
            editTextEmail.setError(getString(R.string.error_invalid_email));
            focusView = editTextEmail;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();

        } else {
            // Check for a valid password.
            if (TextUtils.isEmpty(password)) {
                editTextPassword.setError(getString(R.string.error_field_required));
                focusView = editTextPassword;
                focusView.requestFocus();

            } else if (!isPasswordValid(password)) {
                editTextPassword.setError(getString(R.string.error_invalid_password));
                focusView = editTextPassword;
                focusView.requestFocus();

            } else{
                // Check Internet connection before login attempt
                checkInternetConnection(email, password);
            }
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    private void checkInternetConnection(String email, String password){
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
            login(email, password);

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

    private void login(String email, String password){
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("Logging in ...");
        showDialog();

        // Getting values from edit texts
        final String mEmail = email;
        final String mPassword = password;

        // Creating a string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.API_URL + AppConfig.LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Login Response: " + response.toString());
                        hideDialog();

                        try {
                            JSONObject jObj = new JSONObject(response);
                            JSONArray user = jObj.getJSONArray("user");

                            boolean error = jObj.getBoolean("error");

                            // Check for error node in json
                                if (!error) {
                                // user successfully logged in
                                // Create login session
                                session.setLogin(true);

                                // Now store the user in SQLite DB

                                // Parsing JSON Object from (webserver) response
                                JSONObject mUser = user.getJSONObject(0);
                                String user_id = mUser.getString("id");
                                String user_name = mUser.getString("user_name");
                                String first_name = mUser.getString("first_name");
                                String last_name = mUser.getString("last_name");
                                String email = mUser.getString("email");
                                String address = mUser.getString("address");
                                String contact_number = mUser.getString("contact_number");
                                String balance = mUser.getString("balance");
                                String api_key = mUser.getString("api_key");
                                String status = mUser.getString("status");
                                String created_at = mUser.getString("created_at");
                                String updated_at = mUser.getString("updated_at");

                                // Inserting row in 'user' table
                                db.addUser(user_id, user_name, first_name, last_name, email, address, contact_number, balance, api_key, status, created_at, updated_at);

                                // Launch main activity
                                Intent intent = new Intent(LoginScreenActivity.this, CategoryMenuScreenActivity.class);
                                startActivity(intent);
                                finish();

                            } else {
                                // Error in login. Get the error message
                                String errorMsg = jObj.getString("error_msg");
                                Log.e(TAG, "Error Msg: " + errorMsg);
                                // If the server response is not success
                                // Displaying an error message on a Snackbar
                                Snackbar snackbar = Snackbar.make(coordinatorLayout, "User not logged in!", Snackbar.LENGTH_LONG);
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
                        Log.e(TAG, "Login Error: " + error.getMessage());
                        hideDialog();
                        // Show Warning Snackbar
                        Snackbar snackbar = Snackbar.make(coordinatorLayout, "Error... Check your internet connection!", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                // Adding parameters to request
                params.put(AppConfig.KEY_EMAIL, mEmail);
                params.put(AppConfig.KEY_PASSWORD, mPassword);

                // Returning parameter
                return params;
            }
        };

        // Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed(){
        //Creating an alert dialog to confirm exit app
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setIcon(R.drawable.logo);
        alertDialogBuilder.setTitle("Exit Smart-Barista...");
        alertDialogBuilder.setMessage("Are you sure?");

        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface exitDialog, int which) {
                exitDialog.dismiss();
                finish();
            }
        });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface exitDialog, int which) {
                exitDialog.dismiss();
            }
        });

        //Showing the alert dialog
        AlertDialog exitAlertDialog = alertDialogBuilder.create();
        exitAlertDialog.show();
    }

}
