package com.example.kd.flightfinder;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import managers.AccountManager;
import managers.BookingManager;

public class uploadClientsSucessful extends Activity {

    private AccountManager accountManager; // all existing user info
    private BookingManager bookingManager; //  all uploaded flights
    private String[] adminInfo; // login credentials of current admin

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_clients_sucessful);

        Intent intent = getIntent();

        accountManager = (AccountManager)
                intent.getSerializableExtra("accountManagerKey");
        bookingManager = (BookingManager)
                intent.getSerializableExtra("bookingManagerKey");
        //adminInfo = (String[]) intent.getSerializableExtra("userKey");

        // Display a String representation of the AccountManager object
        // in the TextView identified as newly_added_field
        TextView newlyRegistered = (TextView)
                findViewById(R.id.newly_added_field);
        newlyRegistered.setMovementMethod(new ScrollingMovementMethod());
        newlyRegistered.setText("Registered Clients: \n" +
                accountManager.toString());
    }
}
