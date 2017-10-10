package com.jc.sb.sb_customer_app;

import android.content.Intent;
import android.content.res.Resources;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.jc.sb.adapter.Product;
import com.jc.sb.adapter.ProductsAdapter;
import com.jc.sb.helper.AppConfig;
import com.jc.sb.helper.ItemBorderSpacing;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProductMenuScreenActivity extends AppCompatActivity {

    private static final String TAG = ProductMenuScreenActivity.class.getSimpleName();

    private RecyclerView pRecyclerView;
    private ProductsAdapter pAdapter;
    private List<Product> pList;
    private Product mProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_menu_screen);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initCollapsingToolbar();

        pRecyclerView = (RecyclerView) findViewById(R.id.product_recycler_view);

        pList = new ArrayList<>();
        pAdapter = new ProductsAdapter(this, pList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        pRecyclerView.setLayoutManager(mLayoutManager);
        pRecyclerView.addItemDecoration(new ItemBorderSpacing(2, dpToPx(10), true));
        pRecyclerView.setItemAnimator(new DefaultItemAnimator());
        pRecyclerView.setAdapter(pAdapter);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle!=null) {
            int cID = bundle.getInt("cID");
            //addProductsByCategory(cID);
            getProductsByCategoryID(cID);

            // Get Category Image By ID
            String id = Integer.toString(cID);
            if(id.equals("4")){
                id = "1";
            }
            String categoryImage = "category" + id + "_cover";

            // Check if we catch some error on loading image
            try {
                Glide.with(this).load(getResources().getIdentifier(categoryImage, "drawable", getPackageName())).into((ImageView) findViewById(R.id.backdrop));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Adding some Products for testing
     */
    /*
    private void addProducts() {
        int[] pImages = new int[]{
                R.drawable.product1,
                R.drawable.product2,
                R.drawable.product3,
                R.drawable.product4,
                R.drawable.product5,
                R.drawable.product6
        };


        product = new Product(1, 1, "Espresso", 13.5, pImages[0]);
        pList.add(product);

        product = new Product(2, 2, "Americano", 8.0, pImages[1]);
        pList.add(product);

        product = new Product(3, 3, "Hot-Chocolate", 11.1, pImages[2]);
        pList.add(product);

        product = new Product(4, 4, "Hot-Chocolate 2", 12.3, pImages[3]);
        pList.add(product);

        product = new Product(5, 5, "Speciality 1", 14.5, pImages[4]);
        pList.add(product);

        product = new Product(6, 6, "Speciality 2", 1.5, pImages[5]);
        pList.add(product);

        pAdapter.notifyDataSetChanged();
    }
*/
    /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar title on scroll
     */
    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.app_name));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    /**
     * Adding some Products for testing
     */
    /*
    private void addProductsByCategory(int cID) {
        // Product ID
        int[] pID = new int[]{
                1,
                2,
                3,
                4,
                5,
                6
        };
        // Product Category ID
        int[] categoryID = new int[]{
                1,
                2,
                3,
                4
        };
        // Product Title
        String [] pTitle = new String[]{
                "Product 1",
                "Product 2",
                "Product 3",
                "Product 4",
                "Product 5",
                "Product 6",
        };
        // Product Price
        double[] pPrice = new double[]{
                1.20,
                1.30,
                1.50,
                1.60,
                1.70,
                1.80
        };
        // Product Image
        int[] pImage = new int[]{
                R.drawable.product1,
                R.drawable.product2,
                R.drawable.product3,
                R.drawable.product4,
                R.drawable.product5,
                R.drawable.product6
        };

        // Add Products to the Product List by Category
        switch (cID) {
            case 1:
                product = new Product(pID[0], categoryID[0], pTitle[0], pPrice[0], pImage[0]);
                pList.add(product);

                product = new Product(pID[1], categoryID[0], pTitle[1], pPrice[1], pImage[1]);
                pList.add(product);

                break;

            case 2:
                product = new Product(pID[2], categoryID[1], pTitle[2], pPrice[2], pImage[2]);
                pList.add(product);

                product = new Product(pID[3], categoryID[1], pTitle[3], pPrice[3], pImage[3]);
                pList.add(product);

                break;

            case 3:
                product = new Product(pID[4], categoryID[2], pTitle[4], pPrice[4], pImage[4]);
                pList.add(product);

                product = new Product(pID[5], categoryID[2], pTitle[5], pPrice[5], pImage[5]);
                pList.add(product);

                break;

            case 4:
                product = new Product(pID[0], categoryID[3], pTitle[0], pPrice[0], pImage[0]);
                pList.add(product);

                product = new Product(pID[1], categoryID[3], pTitle[1], pPrice[1], pImage[1]);
                pList.add(product);

                product = new Product(pID[2], categoryID[3], pTitle[2], pPrice[2], pImage[2]);
                pList.add(product);

                product = new Product(pID[3], categoryID[3], pTitle[3], pPrice[3], pImage[3]);
                pList.add(product);

                product = new Product(pID[4], categoryID[3], pTitle[4], pPrice[4], pImage[4]);
                pList.add(product);

                product = new Product(pID[5], categoryID[3], pTitle[5], pPrice[5], pImage[5]);
                pList.add(product);

                break;
        }

        pAdapter.notifyDataSetChanged();
    }
*/
    /**
     * Getting products (webserver)
     * */
    private void getProductsByCategoryID(int cID){
        // Tag used to cancel the request
        String tag_string_req = "req_products_by_category";

//        pDialog.setMessage("Loading...");
//        showDialog();

        // Creating a JSON Object request
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                AppConfig.API_URL + AppConfig.CATEGORIES + "/" + AppConfig.CATEGORY + "/" + cID + "/"+ AppConfig.PRODUCTS, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "Categories Response: " + response.toString());
                //hideDialog();

                try {
                    // Getting the 'products by category' json array of objects
                    JSONArray products_by_category = response.getJSONArray("category-products");

                    boolean error = response.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        // Looping through all products nodes and storing
                        // them in array list
                        for (int i = 0; i < products_by_category.length(); i++) {

                            // Getting the objects from the array
                            JSONObject product = (JSONObject) products_by_category.get(i);

                            String product_id = product.getString("id");
                            String product_category_id = product.getString("category_id");
                            String product_title = product.getString("title");
                            String product_description = product.getString("description");
                            String product_price = product.getString("price");
                            String product_image = product.getString("image");
                            String product_created_at = product.getString("created_at");
                            String product_updated_at = product.getString("updated_at");

                            // Casting into 'int' variables
                            final int pID = Integer.parseInt(product_id);
                            final int categoryID = Integer.parseInt(product_category_id);
                            final double pPrice = Double.parseDouble(product_price);

                            // Just to keep the same consistency as before (don't really need...)
                            String pImage = product_image;

                            mProduct = new Product(pID, categoryID, product_title, pPrice, pImage);
                            pList.add(mProduct);
                        }
                        // Notifying adapter about data changes, so the
                        // list renders with new data
                        pAdapter.notifyDataSetChanged();

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

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, CategoryMenuScreenActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
        finish();
    }

}
