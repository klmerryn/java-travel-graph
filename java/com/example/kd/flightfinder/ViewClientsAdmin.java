package com.example.kd.flightfinder;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import constants.Constants;
import managers.AccountManager;
import managers.BookingManager;
import users.User;

public class ViewClientsAdmin extends ListActivity {

    private AccountManager accountManager; // all existing user info
    // private BookingManager bookingManager; //
    private String[] userInfo;
    private BookingManager bookingManager;

    Map<String, User> usersMap; // map of emails to Clients

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_clients_admin);


        //unpack intent and populate accountManager
        Intent intent = getIntent();
        accountManager = (AccountManager)
                intent.getSerializableExtra("accountManagerKey");
        bookingManager = (BookingManager) intent
                .getSerializableExtra("bookingManagerKey");

        //gets a Map of Map<Client Email, User>
        usersMap = accountManager.getUsers();

        // Retrieve admin credentials
        userInfo = (String[]) getIntent().getSerializableExtra("userKey");

        //gets a set of all the keys in usersMap
        //converts the set into an array of strings
        String[] listOfClients =  usersMap.keySet().
                toArray(new String[usersMap.size()]);

        //Converts the array of strings to ArrayAdapter for use in ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, listOfClients);
        //gets the ListView for the Activity
        ListView viewList = getListView();
        viewList.setAdapter(adapter);
        viewList.setOnItemClickListener(new AdapterView
                .OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v,
                                    int position, long arg3)
            {
                String email = (String) adapter.getItemAtPosition(position);
                viewClientProfile(email);
            }
        });
    }

    /**
     * Start ClientProfileActivity to view an individual client.
     * @param email
     */
    /**
     * Takes the email given by the onItemClickListener and
     * Makes a new ClientProfileActivity based on the email
     * @param email clients email to open
     */
    public void viewClientProfile(String email)
    {
        String[] clientInfo = {email, "password"};
        Intent intent = new Intent(this, ClientProfileActivity.class);
        intent.putExtra("accountManagerKey", accountManager);
        intent.putExtra("userKey", userInfo);
        intent.putExtra("email", email);
        startActivity(intent);
    }

}
