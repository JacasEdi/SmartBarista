package com.jc.sb.sb_customer_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jc.sb.controller.SQLiteHandler;
import com.jc.sb.helper.AppConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class OrderHistoryScreenActivity extends AppCompatActivity
{
    private static final String TAG = OrderHistoryScreenActivity.class.getSimpleName();

    RelativeLayout rlayout;
    ListView lv;

    SQLiteHandler db;

    private static int u_id;

    ArrayList<HashMap<String, String>> orderList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history_screen);

        //Defining views
        rlayout = (RelativeLayout) findViewById(R.id.activity_order_history_screen);
        lv = (ListView) findViewById(R.id.orders_list);

        //ArrayList for storing orders from API
        orderList = new ArrayList<>();

        // SQLite db handler
        db = new SQLiteHandler(getApplicationContext());

        // Getting user details from SQLite db
        HashMap<String, String> user = db.getUserDetails();

        // Used to compare current user's ID to the user IDs in individual orders in the API
        // so that they can be filtered and only current user's orders will be displayed
        u_id = Integer.parseInt(user.get("user_id"));

        // Display user's order history on a screen
        displayOrders();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                onListViewItemSelected();
            }
        });
    }

    private void onListViewItemSelected()
    {
        Snackbar s = Snackbar.make(rlayout, "Order details currently unavailable", Snackbar.LENGTH_LONG);
        s.show();
    }

    // Used to display the orders in a ListView
    private void displayOrders()
    {
        // Tag used to cancel the request
        String tag_string_req = "req_history";

        // Creating a string request
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConfig.API_URL + AppConfig.ORDERS,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        Log.d(TAG, "Response: " + response.toString());

                        try
                        {
                            JSONObject jsonObj = new JSONObject(response);

                            // Getting JSON Array node
                            JSONArray orders = jsonObj.getJSONArray("orders");

                            // looping through all orders
                            for (int i = 0; i < orders.length(); i++)
                            {
                                JSONObject o = orders.getJSONObject(i);

                                int id = o.getInt("id");
                                int user_id = o.getInt("user_id");
                                double amount = o.getDouble("amount");
                                String date = (String) o.get("created_at");

                                if(user_id == u_id)
                                {
                                    // tmp hash map for single order
                                    HashMap<String, String> order = new HashMap<>();

                                    // adding each child node to HashMap key => value
                                    order.put("date", date);
                                    order.put("amount", String.valueOf(amount));

                                    // adding order to order list
                                    orderList.add(order);
                                }
                            }


                            // Display orders in a ListView
                            ListAdapter adapter = new SimpleAdapter(
                                    OrderHistoryScreenActivity.this, orderList, R.layout.list_item,
                                    new String[]{"date", "amount"},
                                    new int[]{R.id.order_date, R.id.amount});

                            lv.setAdapter(adapter);
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                            Log.e(TAG, "Json Error: " + e.getMessage());
                        }

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Log.e(TAG, "Retrieving Error: " + error.getMessage());
                    }
                }) {
        };

        // Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(OrderHistoryScreenActivity.this, CategoryMenuScreenActivity.class);
        startActivity(intent);
        finish();
    }
}
