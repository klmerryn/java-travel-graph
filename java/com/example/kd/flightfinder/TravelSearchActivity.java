package com.example.kd.flightfinder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.ToggleButton;

import java.util.ArrayList;

import flights.Flight;
import flights.Itinerary;
import managers.AccountManager;
import managers.BookingManager;
import users.User;

public class TravelSearchActivity extends Activity {

    private boolean flightsOnly;
    private AccountManager accountManager; // all existing user info
    private BookingManager bookingManager; //  all uploaded flights
    private String[] userInfo; // login credentials of current client
    private boolean searchByCost; // Default is search by time (ascending).
    private ToggleButton searchParameterToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_search);

        // Default the search parameter to searching by time.
        searchParameterToggle = (ToggleButton) findViewById(R.id.search_toggle);
        searchParameterToggle.setChecked(false);
        searchByCost = false;

        // Outline ToggleButton behaviour when toggled.
        searchParameterToggle.setOnCheckedChangeListener
                (new CompoundButton.OnCheckedChangeListener()
                 {
                     public void onCheckedChanged
                             (CompoundButton buttonView,boolean isChecked){
                         if (isChecked) {
                             searchByCost = true;
                         }
                     }
                 }
                );

        // Default to searching full itineraries instead of single flights
        CheckBox rbutton = (CheckBox) findViewById(R.id.find_flight_only);
        rbutton.setChecked(false);
        flightsOnly = false;

        Intent intent = getIntent();

        userInfo = (String[]) intent.getSerializableExtra("userKey");
        accountManager = (AccountManager)
                intent.getSerializableExtra("accountManagerKey");
        bookingManager = (BookingManager)
                intent.getSerializableExtra("bookingManagerKey");
    }

    /**
     * Called when checkbox for 'single flights' is ticked,
     * meaning users will not search itineraries with layovers.
     */
    public void singleFlightEnable(View view) {
        flightsOnly = true;
    }

    /**
     * Call Booking Manager's generateItineraries method
     * using the user-entered parameters in this activity.
     *
     * @param view
     */
    public void searchForFlights(View view) {

        // New arrayList<Itinerary> holds sorted results of itinerary search
        ArrayList<Itinerary> sortedSearch = new ArrayList<>();

        // Find all the user-entered fields, then call generateItineraries.
        EditText depDate = (EditText) findViewById(R.id.departure_date);
        String departureDate = depDate.getText().toString();

        EditText depCity = (EditText) findViewById(R.id.departure_city);
        String departureCity = depCity.getText().toString();

        EditText arrCity = (EditText) findViewById(R.id.arrival_city);
        String arrivalCity = arrCity.getText().toString();

        Intent intent = new Intent(this, DisplaySearchResults.class);
        ArrayList<Itinerary> flightsList = new ArrayList<>();

        if (flightsOnly) {
            ArrayList<Flight> singleFlights = new ArrayList<>();
            ArrayList<Flight> arrayListFlight = new ArrayList<>();
            bookingManager.findFlights(departureCity, arrivalCity,
                    departureDate, singleFlights);

            /**
             * Convert ArrayList<Flight> to ArrayList<ArrayList<Flight>>
             * so that single-item itineraries are supported.
             */
            for (Flight f : singleFlights) {
                arrayListFlight.add(f);
                Itinerary i = new Itinerary(arrayListFlight);
                flightsList.add(i);
                arrayListFlight.clear();
            }

        } else { //if flightsOnly == false, Search whole itineraries
            flightsList = bookingManager.getTrips(departureCity,
                    arrivalCity, departureDate);
            //intent.putExtra("resultsKey", flightsList);
        }

        if (searchByCost) {
            sortedSearch = bookingManager.sortItinerariesByCost(flightsList);
        }
        else {
            sortedSearch = bookingManager.sortItinerariesByTime(flightsList);
        }

        intent.putExtra("searchResultsKey", sortedSearch);
        intent.putExtra("bookingManagerKey", bookingManager);
        intent.putExtra("accountManagerKey", accountManager);
        intent.putExtra("userKey", userInfo);

        startActivity(intent);
    }


    /**
     * Returns User to their Dashboard based on credentials.
     * @param view
     */
    public void returnToDashboard(View view) {
        // email of active user
        String activeEmail = userInfo[0];
        boolean isAdmin = !(accountManager.getUsers().
                containsKey(activeEmail));

        // Check whether admin or client to start appropriate dashboard
        if (isAdmin) {
            Intent intent = new Intent(this, AdminDashboardActivity.class);
            intent.putExtra("userKey", userInfo); // User who viewed screen
            intent.putExtra("accountManagerKey", accountManager);
            intent.putExtra("bookingManagerKey", bookingManager);
            startActivity(intent);

        } else {
            Intent intent = new Intent(this, ClientDashboardActivity.class);
            intent.putExtra("userKey", userInfo); // User who viewed screen
            intent.putExtra("accountManagerKey", accountManager);
            intent.putExtra("bookingManagerKey", bookingManager);
            startActivity(intent);
        }
    }
}
