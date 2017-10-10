package com.jc.sb.sb_customer_app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.jc.sb.controller.SQLiteHandler;
import com.jc.sb.controller.SessionManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

public class SingleProductScreenActivity extends AppCompatActivity {

    private Context mContext;
    private Button btnOrderNow, btnScheduleOrder;
    private int pID, pQty, cID;
    private double pPrice, pTotalPrice;

    private String user_id, amount, cart_code, product_id, product_single_price, product_quantity, product_total_price, created_at;

    private ProgressDialog pDialog;

    private SQLiteHandler db;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_product_screen);

        mContext = getApplicationContext();

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

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle!=null) {
            pID = bundle.getInt("pID");
            pPrice = bundle.getDouble("pPrice");

            // JUST FOR TESTING (NEEDS TO BE AN INPUT FIELD)
            pQty = 1;

            pTotalPrice = pQty * pPrice;

            cID = bundle.getInt("cID");

            // Getting the fields to send into the db

            // Casting the 'product_id', 'product_single_price', 'product_quantity', 'product_total_price'
            // into String before send them to the db
            product_id = Integer.toString(pID);
            product_single_price = Double.toString(pPrice);
            product_quantity = Integer.toString(pQty);
            product_total_price = Double.toString(pTotalPrice);

            // Generating a random code for the cart
            char[] chars1 = "ABCDEFGHIJKLMNOPQRSTUVXYZ0123456789".toCharArray();
            StringBuilder sb1 = new StringBuilder();
            Random random1 = new Random();
            for (int i = 0; i < 10; i++) {
                char c1 = chars1[random1.nextInt(chars1.length)];
                sb1.append(c1);
            }
            cart_code = sb1.toString();

            // Getting system Date/Time
            Date curDate = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            created_at = format.format(curDate);


        }

        btnOrderNow = (Button) findViewById(R.id.btnOrderNow);
        btnScheduleOrder = (Button) findViewById(R.id.btnScheduleOrder);

        btnOrderNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pDialog.setMessage("Adding to cart...");
                showDialog();

                // Inserting row in 'cart' table
                db.addProduct(cart_code, user_id, product_id, product_single_price, product_quantity, product_total_price, created_at);

                hideDialog();

                Toast.makeText(mContext, "Your Order Have Been Placed.", Toast.LENGTH_LONG).show();
                categoryCallBack();
            }
        });

        btnScheduleOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(mContext, ScheduleOrderScreenActivity.class);
                //startActivity(intent);
                //overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

                //Create a new set of info to send to the 'Single Product Screen'
                Bundle bundle = new Bundle();
                // Send the Product ID to the 'Single Product Screen' Activity
                bundle.putInt("pID", pID);
                // Send the Category ID to the 'Single Product Screen' Activity
                bundle.putInt("cID", cID);
                // Create a new Intent
                Intent intent = new Intent(mContext, ScheduleOrderScreenActivity.class);
                // Send the Bundle
                intent.putExtras(bundle);
                // Call the Activity
                startActivity(intent);
                // Activity Transition Effect
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                // Kill the Activity on Exit
                finish();

            }
        });
    }

    // Info to send back again to the 'Product Menu Screen'
    private void categoryCallBack(){
        // Just a debug Toast...
        Toast.makeText(mContext, "Category ID: " + cID, Toast.LENGTH_SHORT).show();
        // Create a new set of info to send back to the 'Product Menu Screen'
        Bundle bundle = new Bundle();
        // Send back again the current Category ID to the 'Product Menu Screen' Activity
        bundle.putInt("cID", cID);
        // Create a new Intent
        Intent intent = new Intent(mContext, ProductMenuScreenActivity.class);
        // Send the Bundle
        intent.putExtras(bundle);
        // Call the Activity
        startActivity(intent);
        // Activity Transition Effect
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
        // Kill the Activity on Exit
        finish();
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
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
                        Intent intent = new Intent(SingleProductScreenActivity.this, LoginScreenActivity.class);
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

    @Override
    public void onBackPressed(){
        categoryCallBack();
    }
}