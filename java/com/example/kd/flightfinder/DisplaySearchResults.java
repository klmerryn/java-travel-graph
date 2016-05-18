package com.example.kd.flightfinder;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;


import flights.Flight;
import flights.Itinerary;
import flights.Travels;
import managers.AccountManager;
import managers.BookingManager;
import users.Client;
import users.User;

/**
 * Screen to display search results when users search for flights
 * or itineraries.
 * Adapts arrays (of either Flight object or Itinerary object) to
 * display a list of itineraries or flights available for booking.
 */
public class DisplaySearchResults extends Activity {

    private ArrayList<Itinerary> arraySearchResults;
    private ListView displayList;

    private BookingManager bookingManager;
    private AccountManager accountManager;
    private String[] userInfo;
    public static final String FLIGHTSFILE = "flights.ser";

    /**
     * Creates the Display Results screen. This screen view is populated
     * by Itineraries. Users are able to click an itinerary, where they are
     * given the option to book it as a future trip. Administrators cannot
     * book trips for themselves, but can be prompted to book a trip on
     * behalf of a Client.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_search_results);

        Intent intent = getIntent();

        // Retrieve Account and Booking Manager, and search results
        accountManager = (AccountManager)
                intent.getSerializableExtra("accountManagerKey");
        bookingManager = (BookingManager)
                intent.getSerializableExtra("bookingManagerKey");

        arraySearchResults = (ArrayList<Itinerary>)
                intent.getSerializableExtra("searchResultsKey");
        userInfo = (String[]) intent.getSerializableExtra("userKey");

        /**
         * Associate ListView with the search results found above.
         */
        ListView displayList = (ListView) findViewById(R.id.listView);
            /**
             * Creates a new ArrayAdapter. This takes Array elements and puts
             * them into a format that the ListView can then display
             * (representations are based on the Array elements'
             * toStrings). simple_list_item_1 is a default list layout
             * provided by AS.
             */
            final ArrayAdapter<Itinerary> adapterSearchResults =
                    new ArrayAdapter<Itinerary>(this,
                            android.R.layout.simple_list_item_1,
                            arraySearchResults);

            displayList.setAdapter(adapterSearchResults);

        // OnClickListener to respond to user selection.
        displayList.setOnItemClickListener(new AdapterView.
                OnItemClickListener() {

            // Override abstract onItemClick method.
            // Required when adding OnItemClickListener.
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // Get the value of selected clicked item
                Itinerary selectedItin = (Itinerary)
                        adapterSearchResults.getItem(position);

                // Show pop-up with Itinerary string, confirming user wants to book
                showBookingPopUp(selectedItin.toString(), selectedItin);
            }
        });
    }

    /**
     * Creates a popup dialogue confirming the user's wish to book the
     * selected itinerary.
     * If user is a Client, itinerary is added to their list of itineraries.
     * If user is an Admin, they are prompted to designate a client (by entering
     * his/her email address) for whom the trip is to be booked.
     * Pressing "cancel" cancels the booking.
     *
     * @param toDisplay     Message to show in body of dialog window
     * @param selectedItin  Itinerary to be booked
     */
    private void showBookingPopUp(String toDisplay,
                                  final Itinerary selectedItin) {

        AlertDialog.Builder helpBuilder = new AlertDialog.Builder(this);
        helpBuilder.setTitle("Book This Trip");
        helpBuilder.setMessage(toDisplay);
        helpBuilder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        // Book the itinerary and close the dialogue
                        String username = userInfo[0];
                        User user = accountManager.getUsers().get(username);

                        // Client users can add trips to their own itineraries.
                        // Admin can enter a client's email to book them a flight.

                        try {
                            if (Client.class.isInstance(user)) {
                                Client client = (Client) user;
                                client.addItinerary(selectedItin);

                                // Save the changes to the Client's itinerary
                                saveBookingManagerToFile();

                            } else { // User is not a valid client in clients.txt
                                //showToastNoItin();
                                promptClientEmail(selectedItin);
                            }

                        } catch (Exception e) {

                        // Not anticipated; print unexpected result to console.
                            e.printStackTrace();
                        }
                    }
                });

        helpBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Close dialogue box
                    }
                });

        // Show the dialog
        AlertDialog helpDialog = helpBuilder.create();
        helpDialog.show();
    }

    private void promptClientEmail(final Itinerary selectedItin) {

        // Client's email will be typed here
        final EditText editEmail = new EditText(this);
        editEmail.setHint(R.string.client_email);

        AlertDialog.Builder helpBuilder = new AlertDialog.Builder(this);
        helpBuilder.setTitle(R.string.book_for_client);
        helpBuilder.setMessage(R.string.enter_client_email_prompt);
        helpBuilder.setView(editEmail);
        helpBuilder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        String email = editEmail.getText().toString();
                        User user = accountManager.getUsers().get(email);
                        if (Client.class.isInstance(user)) {
                            Client client = (Client) user;
                            client.addItinerary(selectedItin);

                            // Save changes to flights.ser
                            saveBookingManagerToFile();

                        } else {
                            showToastInvalidEmail();
                        }
                    }

                });

        // Set the "cancel" button
        helpBuilder.setNegativeButton(R.string.cancel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Close dialogue
                    }
                });

        // Show the dialog
                    AlertDialog helpDialog = helpBuilder.create();
                    helpDialog.show();
                }

    /**
     * Returns to Dashboard screen based on User's credentials.
     * @param view
     */
    public void goToDashboard(View view){
        // email of active user
            String activeEmail = userInfo[0];
            boolean isAdmin = !(accountManager.getUsers().
                    containsKey(activeEmail));

            // Decide if admin or client and start appropriate dashboard
            if (isAdmin) {
                Intent intent = new Intent
                        (this, AdminDashboardActivity.class);
                intent.putExtra("userKey", userInfo);
                intent.putExtra("accountManagerKey", accountManager);
                intent.putExtra("bookingManagerKey", bookingManager);
                startActivity(intent);

            } else {
                Intent intent = new Intent
                        (this, ClientDashboardActivity.class);
                intent.putExtra("userKey", userInfo);
                intent.putExtra("accountManagerKey", accountManager);
                intent.putExtra("bookingManagerKey", bookingManager);
                startActivity(intent);
            }
        }

    /**
     * Brief pop-up (toast) message indicating
     * an invalid email address.
     */
    public void showToastInvalidEmail() {
        Toast.makeText(this, R.string.no_such_email,
                Toast.LENGTH_LONG).show();
    }
    /**
     * Brief pop-up (toast) message indicating that
     * administrators can only
     * book flights for Clients.
     */
    public void showToastNoItin() {
        Toast.makeText(this, R.string.admin_not_eligible_itin,
                Toast.LENGTH_LONG).show();
    }

    /**
     * Brief pop-up (toast) message indicating that a flight cannot be
     * booked because it's full.
     */
    public void showToastCannotBook(){
        Toast.makeText(this, R.string.cannot_book_flight,
                Toast.LENGTH_LONG).show();
    }


    /**
     * Saves BookingManager to flights.ser. Called after any change (i.e.
     * an itinerary or flight booking) is made to a Client's itineraries.
     **/
    private void saveBookingManagerToFile() {
        File userdata = getApplicationContext().
                getDir(FLIGHTSFILE, MODE_PRIVATE);
        File bookingManagerFile = new File(userdata, FLIGHTSFILE);
        bookingManager.saveToFile(bookingManagerFile.getAbsolutePath());
    }

}
