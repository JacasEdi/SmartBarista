package com.jc.sb.sb_customer_app;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.jc.sb.adapter.Category;
import com.jc.sb.adapter.CategoriesAdapter;
import com.jc.sb.controller.SQLiteHandler;
import com.jc.sb.controller.SessionManager;
import com.jc.sb.helper.AppConfig;
import com.jc.sb.helper.ItemBorderSpacing;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class CategoryMenuScreenActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = CategoryMenuScreenActivity.class.getSimpleName();

    private TextView txtName;
    private TextView txtEmail;

    //private static GlobalConstants gc = new GlobalConstants();
    //private static double userInitialCredits = gc.USER_INITIAL_CREDITS;

    private ProgressDialog pDialog;

    private SQLiteHandler db;
    private SessionManager session;

    private RecyclerView cRecyclerView;
    private CategoriesAdapter cAdapter;
    private List<Category> cList;
    private Category mCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_menu_screen);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "The shopping cart is empty!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Set the header view on the drawer
        View header = navigationView.getHeaderView(0);

        // Set user name and email fields
        txtName = (TextView) header.findViewById(R.id.userFullName);
        txtEmail = (TextView) header.findViewById(R.id.userEmailAddress);

        // SQLite db handler
        db = new SQLiteHandler(getApplicationContext());

        // Session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logout();
        }

        // Getting user details from SQLite db
        HashMap<String, String> user = db.getUserDetails();

        String user_name = user.get("user_name");
        String email = user.get("email");
        String balance = user.get("balance");

        // Displaying the user details on the drawer
        txtName.setText(user_name);
        txtEmail.setText(email);

        // Get the menu from navigationView
        Menu menu = navigationView.getMenu();

        // Find MenuItem Credits Amount
        MenuItem nav_credit_amount = menu.findItem(R.id.menuCreditsOption);

        // Set new title to the MenuItem
        nav_credit_amount.setTitle("Credits: " + balance);

        // Populating the Activity with content
        cRecyclerView = (RecyclerView) findViewById(R.id.category_recycler_view);

        cList = new ArrayList<>();
        cAdapter = new CategoriesAdapter(this, cList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        cRecyclerView.setLayoutManager(mLayoutManager);
        cRecyclerView.addItemDecoration(new ItemBorderSpacing(2, dpToPx(10), true));
        cRecyclerView.setItemAnimator(new DefaultItemAnimator());
        cRecyclerView.setAdapter(cAdapter);

        //addCategories();

        getCategories();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.menuCreditsOption)
        {
            Intent intent = new Intent(CategoryMenuScreenActivity.this, TopUpScreenActivity.class);
            startActivity(intent);
            finish();
        }
        else if (id == R.id.menuMoviesOption)
        {
            Intent intent = new Intent(CategoryMenuScreenActivity.this, VideoViewScreenActivity.class);
            startActivity(intent);
            finish();
        }
        else if (id == R.id.menuChangeUserDetailsOption)
        {
            Intent intent = new Intent(CategoryMenuScreenActivity.this, ChangeUserDetailsScreenActivity.class);
            startActivity(intent);
            finish();
        }
        else if (id == R.id.menuOrderHistory)
        {
            Intent intent = new Intent(CategoryMenuScreenActivity.this, OrderHistoryScreenActivity.class);
            startActivity(intent);
            finish();
        }
        else if (id == R.id.menuLogoutOption)
        {
            // Logout User
            logout();
        }
        else if (id == R.id.menuShareOption)
        {
            final String URL = "https://play.google.com/store?hl=en_GB";
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.putExtra(Intent.EXTRA_TEXT, URL);

            startActivity(Intent.createChooser(share, "Share the app using"));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //Logout function
    private void logout(){
        //Creating an alert dialog to confirm logout
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setIcon(R.drawable.logo);
        alertDialogBuilder.setTitle("Logout Smart-Barista...");
        alertDialogBuilder.setMessage("Are you sure you want to logout?");

        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface exitDialog, int which) {

                        session.setLogin(false);

                        db.deleteUser();

                        exitDialog.dismiss();

                        //Going back to 'Login Screen Activity'
                        Intent intent = new Intent(CategoryMenuScreenActivity.this, LoginScreenActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });

        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface exitDialog, int which) {
                        exitDialog.dismiss();
                    }
                });

        //Showing the alert dialog
        AlertDialog exitAlertDialog = alertDialogBuilder.create();
        exitAlertDialog.show();

    }

    /**
     * Getting categories (webserver)
     * */
    private void getCategories(){
        // Tag used to cancel the request
        String tag_string_req = "req_categories";

//        pDialog.setMessage("Loading...");
//        showDialog();

        // Creating a JSON Object request
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                AppConfig.API_URL + AppConfig.CATEGORIES, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "Categories Response: " + response.toString());
                //hideDialog();

                try {
                    // Getting the 'categories' json array of objects
                    JSONArray categories = response.getJSONArray("categories");

                    boolean error = response.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        // Looping through all categories nodes and storing
                        // them in array list
                        for (int i = 0; i < categories.length(); i++) {

                            // Getting the objects from the array
                            JSONObject category = (JSONObject) categories.get(i);

                            String id = category.getString("id");
                            String title = category.getString("title");
                            String description = category.getString("description");
                            String image = category.getString("image");
                            String cover_image = category.getString("cover_image");
                            String created_at = category.getString("created_at");

                            // Casting into 'int' Category ID
                            final int cast_cID = Integer.parseInt(id);

                            mCategory = new Category(cast_cID, title, description, image, cover_image, created_at);

                            cList.add(mCategory);
                        }
                        // Notifying adapter about data changes, so the
                        // list renders with new data
                        cAdapter.notifyDataSetChanged();

                    } else {
                        // Error connecting to the server. Get the error message
                        String errorMsg = response.getString("error_msg");
                        Log.e(TAG, "Error Msg: " + errorMsg);
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Log.e(TAG, "Json Error: " + e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                //hideDialog();
                // Show Warning Snackbar
                //Snackbar snackbar = Snackbar.make(coordinatorLayout, "Error... Check your internet connection!", Snackbar.LENGTH_LONG);
                //snackbar.show();
            }
        });

        // Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjReq);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    /**
     * Adding some Categories for testing
     */
    /*
    private void addCategories() {
        // Category ID
        int[] cID = new int[]{
                1,
                2,
                3,
                4
        };
        // Category Title
        String [] cTitle = new String[]{
                "Coffee",
                "Hot Chocolate",
                "Speciality Drinks",
                "All Products"
        };
        // Category Image
        int[] cImage = new int[]{
                R.drawable.category1,
                R.drawable.category2,
                R.drawable.category3,
                R.drawable.category1
        };

        mCategory = new Category(cID[0], cTitle[0], cImage[0]);
        cList.add(mCategory);

        mCategory = new Category(cID[1], cTitle[1], cImage[1]);
        cList.add(mCategory);

        mCategory = new Category(cID[2], cTitle[2], cImage[2]);
        cList.add(mCategory);

        mCategory = new Category(cID[3], cTitle[3], cImage[3]);
        cList.add(mCategory);


        cAdapter.notifyDataSetChanged();
    }
    */

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            // Show Exit Alert Dialog
            exitDialog();
        }
    }

    private void exitDialog() {

        // Show Exit Alert Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.logo);
        builder.setTitle("Exit without Logout...");
        builder.setMessage("          Are You Sure?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface exitDialog, int which) {
                exitDialog.dismiss();
                finish();
                //Kills the processes in memory before exiting the App
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface exitDialog, int which) {
                exitDialog.dismiss();
            }
        });
        AlertDialog exitAlertDialog = builder.create();
        exitAlertDialog.show();
    }

}
