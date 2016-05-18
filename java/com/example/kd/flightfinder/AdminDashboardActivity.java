package com.example.kd.flightfinder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import constants.Constants;
import managers.AccountManager;
import managers.BookingManager;

public class AdminDashboardActivity extends Activity {

    private BookingManager bookingManager; // contains all uploaded flights
    private AccountManager accountManager; // contains all existing user info
    private String[] adminInfo; // login credentials of current admin

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        // Get the Intent that conveys registration (or login) information
        // from admin
        // Pull their username
        Intent intent = getIntent();
        adminInfo = (String[]) intent.getSerializableExtra("userKey");
        accountManager = (AccountManager)
                intent.getSerializableExtra("accountManagerKey");
        bookingManager = (BookingManager)
                intent.getSerializableExtra("bookingManagerKey");


        TextView adminName = (TextView) findViewById(R.id.username);
        adminName.setText(adminInfo[0]);


    }


    /**
     * Returns to main screen and clears stored information on active user.
     * @param view
     */
    public void logout(View view) {
        Intent intent = new Intent(this, MainActivity.class);

        startActivity(intent);
    }


    /*
    Starts an UploadInformationActivity.
     */
    public void uploadInformation(View view)
    {
        Intent intent = new Intent(this, UploadInformationActivity.class);

        intent.putExtra("userKey", adminInfo);
        intent.putExtra("bookingManagerKey", bookingManager);
        intent.putExtra("accountManagerKey", accountManager);

        startActivity(intent);
    }

    /*
    Starts a ViewClientsActivity.
     */
    public void viewClients(View view)
    {
        Intent intent = new Intent(this, ViewClientsAdmin.class);
        intent.putExtra(Constants.BOOKINGMANAGER_KEY, bookingManager);
        intent.putExtra(Constants.ACCOUNTMANAGER_KEY, accountManager);
        intent.putExtra("userKey", adminInfo);
        startActivity(intent);
    }


    /*
    Starts a TravelSearchActivity.
     */
    public void startSearch(View view) {
        Intent searchIntent = new Intent(this, TravelSearchActivity.class);

        searchIntent.putExtra("accountManagerKey", accountManager);
        searchIntent.putExtra("bookingManagerKey", bookingManager);
        searchIntent.putExtra("userKey", adminInfo);

        startActivity(searchIntent);
    }


}
