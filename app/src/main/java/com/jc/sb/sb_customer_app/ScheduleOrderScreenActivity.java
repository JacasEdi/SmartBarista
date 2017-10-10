package com.jc.sb.sb_customer_app;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class ScheduleOrderScreenActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mContext;
    private Button btnDatePicker, btnTimePicker, btnSetOrder;
    private EditText txtDate, txtTime;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private int pID, cID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_order_screen);

        mContext = getApplicationContext();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle!=null) {
            pID = bundle.getInt("pID");
            cID = bundle.getInt("cID");
        }

        btnDatePicker = (Button)findViewById(R.id.btnDate);
        btnTimePicker = (Button)findViewById(R.id.btnTime);
        btnSetOrder = (Button)findViewById(R.id.btnSetOrder);
        txtDate = (EditText)findViewById(R.id.in_date);
        txtTime = (EditText)findViewById(R.id.in_time);

        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);
        btnSetOrder.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        if (view == btnDatePicker) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                            txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        }
                    }, mYear, mMonth, mDay);

            datePickerDialog.show();
        }

        if (view == btnTimePicker) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            txtTime.setText(hourOfDay + ":" + minute);
                        }
                    }, mHour, mMinute, false);

            timePickerDialog.show();
        }

        if (view == btnSetOrder) {
            Toast.makeText(mContext, "Your Order Have Been Scheduled.", Toast.LENGTH_LONG).show();
            categoryCallBack();
        }
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

    // Info to send back again to the 'Single Product Screen'
    private void productCallBack(){
        // Just a debug Toast...
        Toast.makeText(mContext, "Product ID: " + pID, Toast.LENGTH_SHORT).show();
        //Create a new set of info to send back again to the 'Single Product Screen'
        Bundle bundle = new Bundle();
        // Send the Product ID back to the 'Single Product Screen' Activity
        bundle.putInt("pID", pID);
        // Send the Category ID back to the 'Single Product Screen' Activity
        bundle.putInt("cID", cID);
        // Create a new Intent
        Intent intent = new Intent(mContext, SingleProductScreenActivity.class);
        // Send the Bundle
        intent.putExtras(bundle);
        // Call the Activity
        startActivity(intent);
        // Activity Transition Effect
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
        // Kill the Activity on Exit
        finish();
    }

    @Override
    public void onBackPressed(){
        productCallBack();
    }
}
