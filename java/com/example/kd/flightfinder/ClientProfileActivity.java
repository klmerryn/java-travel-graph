package com.example.kd.flightfinder;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import flights.Itinerary;
import managers.AccountManager;
import managers.BookingManager;
import users.Client;
import users.User;

/**
 * Activity Class that allows for editing and viewing individual
 * client profiles. Keeps track of current state of booking and
 * account managers, as well as of credentials of user looking
 * at this screen.
 * (Note: 'User credentials' are not necessarily those of the client
 * whose profile is being viewed - they may also be an administrator's).
 *
 */
public class ClientProfileActivity extends Activity {

    private AccountManager accountManager; // contains all existing user info
    private BookingManager bookingManager; // contains all uploaded flights
    private String[] activeUserInfo; // login credentials of current user.
    private boolean isAdmin = false;
    private String clientEmail;
    private Client client; // current client;
    //private String[] clientInfo; // new! does not refer to same as old.

    // directory storing serializable files
    public static final String USERDATADIR = "userdata";

    // serialized file containing user information
    public static final String USERSFILE = "users.ser";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_profile);

        Intent intent = getIntent();

        accountManager = (AccountManager)
                intent.getSerializableExtra("accountManagerKey");
        bookingManager = (BookingManager)
                intent.getSerializableExtra("bookingManagerKey");

        // User who is viewing this screen: could be admin or client.
        activeUserInfo = (String[])
                intent.getSerializableExtra("userKey");
        String username = activeUserInfo[0];

        // Client whose profile is being viewed
        clientEmail = (String) intent.getSerializableExtra("email");
        User user = accountManager.getUsers().get(clientEmail); // originally (user).
        client = (Client) user;

        // Determine if user is Client (viewing own profile) or Admin
        if (!activeUserInfo[0].equals(clientEmail)) {
            isAdmin = true;
        }

        //Populate first name text field with current user information.
        EditText firstName = (EditText) findViewById(R.id.edit_first_name);
        firstName.setText(client.getFirst());

        // Sets new first name in accountManager to reflect changes and
        // saves edits made to first name in serialized users file.

        firstName.setOnEditorActionListener(new TextView.
                OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                    EditText editedFirstName = (EditText)
                            findViewById(R.id.edit_first_name);
                    String newFirstName = editedFirstName.getText().toString();
                    client.setFirst(newFirstName);
                    saveAccManagerToFile();
                    return true;
            }
        });

        //Repeat above process for all remaining editable text fields.
        EditText lastName = (EditText) findViewById(R.id.edit_last_name);
        lastName.setText(client.getLast());

        lastName.setOnEditorActionListener(new TextView.
                OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                EditText editedLastName = (EditText)
                        findViewById(R.id.edit_last_name);
                String newLastName = editedLastName.getText().toString();
                client.setLast(newLastName);
                saveAccManagerToFile();
                return true;
            }
        });

        EditText email = (EditText) findViewById(R.id.edit_email);
        email.setText(client.getEmail());

        email.setOnEditorActionListener(new TextView.
                OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                EditText editedEmail = (EditText)
                        findViewById(R.id.edit_email);
                String newEmail = editedEmail.getText().toString();
                String oldEmail = client.getEmail();
                client.setEmail(newEmail);

                // Add new mapping.
                accountManager.getUsers().put(newEmail, client);

                //Remove old mapping.
                accountManager.getUsers().remove(oldEmail);
                saveAccManagerToFile();
                return true;
            }
        });

        EditText address = (EditText) findViewById(R.id.edit_address);
        address.setText(client.getAddress());

        address.setOnEditorActionListener(new TextView.
                OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                EditText editedAddress = (EditText)
                        findViewById(R.id.edit_address);
                String newAddress = editedAddress.getText().toString();
                client.setAddress(newAddress);
                saveAccManagerToFile();
                return true;
            }
        });

        EditText creditCardNum = (EditText)
                findViewById(R.id.edit_credit_card_num);
        creditCardNum.setText(client.getCreditCardNumber());

        creditCardNum.setOnEditorActionListener(new TextView.
                OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                EditText editedCredCard = (EditText)
                        findViewById(R.id.edit_credit_card_num);
                String newCredCard = editedCredCard.getText().toString();
                client.setCreditCardNumber(newCredCard);
                saveAccManagerToFile();
                return true;
            }
        });

        EditText expDate = (EditText) findViewById(R.id.edit_exp_date);
        expDate.setText(client.getExpiryDate());

        expDate.setOnEditorActionListener(new TextView.
                OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                EditText editedExpDate = (EditText)
                        findViewById(R.id.edit_exp_date);
                String newExpDate = editedExpDate.getText().toString();
                client.setExpiryDate(newExpDate);
                saveAccManagerToFile();
                return true;
            }
        });

        TextView itineraries = (TextView)
                findViewById(R.id.cur_itins);
        String result = new String();
        List<Itinerary> bookedItins = client.getBookedItineraries();

        if (bookedItins.size() > 0) {
            for (Itinerary i : bookedItins) {
                result += i.toString();
            }
        }
        itineraries.setText(result);
    }

    /**
     * Saves account manager to users.ser.
     */
    private void saveAccManagerToFile() {
        File userdata = getApplicationContext().
                getDir(USERDATADIR, MODE_PRIVATE);
        File usersFile = new File(userdata, USERSFILE);
        accountManager.saveToFile(usersFile.getAbsolutePath());
    }

    /**
     * Returns User to their Dashboard, which is ClientDashboard or
     * AdminDashboard depending on their credentials.
     * @param view  Current view
     */
    public void back(View view) {

        // Check whether admin or client is current user.
        if (isAdmin) {
            Intent intent = new Intent(this, AdminDashboardActivity.class);
            intent.putExtra("userKey", activeUserInfo);
            intent.putExtra("accountManagerKey", accountManager);
            intent.putExtra("bookingManagerKey", bookingManager);
            startActivity(intent);

        } else {
            Intent intent = new Intent(this, ClientDashboardActivity.class);
            intent.putExtra("userKey", activeUserInfo);
            intent.putExtra("accountManagerKey", accountManager);
            intent.putExtra("bookingManagerKey", bookingManager);
            startActivity(intent);
        }
    }
}
