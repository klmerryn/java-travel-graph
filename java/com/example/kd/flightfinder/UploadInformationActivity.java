package com.example.kd.flightfinder;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.File;

import constants.Constants;
import managers.AccountManager;
import managers.BookingManager;


public class UploadInformationActivity extends Activity
{
    private BookingManager bookingManager; // contains all uploaded flights
    private AccountManager accountManager; // contains all existing user info
    private String[] userInfo; // login credentials of current admin

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_information);

        Intent intent = getIntent();
        accountManager = (AccountManager)
                intent.getSerializableExtra("accountManagerKey");
        bookingManager = (BookingManager)
                intent.getSerializableExtra("bookingManagerKey");
        userInfo = (String[]) intent.getSerializableExtra("userKey");
    }

    /*
    OnClick for flight info button
    Gets the path for a CSV file from flight_info_path editable text.
    If the file exists and in the right format, adds flights
    to bookingManager
    FILE PATH TO BE USED:
    /data/data/com.example.kd.flightfinder/files/flights1.txt
    */
    public void uploadFlightInfo(View view)
    {
        //gets Text that is in the flight info path text field
        EditText flightPath = (EditText) findViewById(R.id.flight_info_path);

        String givenPath = flightPath.getText().toString();

        String uploadInfo = bookingManager.uploadFlightInfo(givenPath);

        showSimplePopUp(uploadInfo);
    }

    private void showSimplePopUp(String toDisplay) {

        AlertDialog.Builder helpBuilder = new AlertDialog.Builder(this);
        helpBuilder.setTitle("Upload Information");
        helpBuilder.setMessage(toDisplay);
        helpBuilder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                    }
                });

        // Remember, create doesn't show the dialog
        AlertDialog helpDialog = helpBuilder.create();
        helpDialog.show();
    }

    /**
     * FILE PATH TO BE USED: /data/user/0/com.example.kd.flightfinder/files/*
     *
     */
    public void uploadClientInfo(View view)
    {
        //Specifies the next activity to move to: uploadClientsSucessful
        Intent intent = new Intent(this, uploadClientsSucessful.class);

        //gets Text that is in the flight info path text field
        EditText clientPath = (EditText) findViewById(R.id.client_info_path);
        String givenPath = clientPath.getText().toString();

        //Upload info with given path
        accountManager.uploadClientInfo(givenPath);

        //Puts the AccountManager into Intent to pass to next activity.
        intent.putExtra(Constants.ACCOUNTMANAGER_KEY, accountManager);

        //starts the UploadCLientInfoSucessful Activity
        startActivity(intent);
    }

    public void saveAndBack(View view)
    {
        Intent intent = new Intent(this, AdminDashboardActivity.class);

        intent.putExtra("userKey", userInfo);
        intent.putExtra("accountManagerKey", accountManager);
        intent.putExtra("bookingManagerKey", bookingManager);
        startActivity(intent);
    }

}
