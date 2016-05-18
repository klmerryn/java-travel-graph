package com.example.kd.flightfinder;

import android.accounts.Account;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import flights.Flight;
import flights.Itinerary;
import managers.AccountManager;
import managers.BookingManager;

public class ClientDashboardActivity extends Activity {

    private BookingManager bookingManager; // contains all uploaded flights
    private AccountManager accountManager; // contains all existing user info
    private String[] clientInfo; // login credentials of current client

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_dashboard);
        Intent intent = getIntent();
        clientInfo = (String[]) intent.getSerializableExtra("userKey");

        accountManager = (AccountManager)
                intent.getSerializableExtra("accountManagerKey");
        bookingManager = (BookingManager)
                intent.getSerializableExtra("bookingManagerKey");

        TextView clientName = (TextView) findViewById(R.id.username);
        clientName.setText(clientInfo[0]);
    }

    /*
    Starts a TravelSearchActivity.
     */
    public void startSearch(View view) {
        Intent intent = new Intent(this, TravelSearchActivity.class);
        intent.putExtra("accountManagerKey", accountManager);
        intent.putExtra("bookingManagerKey", bookingManager);
        intent.putExtra("userKey", clientInfo);

        startActivity(intent);
    }


    /*
    Starts a ClientProfileActivity.
     */
    public void viewProfile(View view) {
        Intent intent = new Intent(this, ClientProfileActivity.class);
        intent.putExtra("userKey", clientInfo);
        intent.putExtra("email", clientInfo[0]);
        intent.putExtra("accountManagerKey", accountManager);
        intent.putExtra("bookingManagerKey", bookingManager);
        startActivity(intent);
    }

    /**
     * Returns to main screen and clears stored information
     * on which is active user.
     * @param view
     */
    public void logout(View view) {
        Intent intent = new Intent(this, MainActivity.class);

        startActivity(intent);
    }


}
