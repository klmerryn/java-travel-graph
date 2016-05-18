package com.example.kd.flightfinder;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Scanner;

import constants.Constants;
import managers.AccountManager;
import managers.BookingManager;

public class MainActivity extends AppCompatActivity {

    private String email = "invalid email";
    private String password = "invalid password";
    private Intent intent;
    private boolean isAdmin; // true iff current user is an Admin
    private HashMap<String, String[]> usernameToPassword;
    private static final String PASSWORDS = "passwords.txt";
    private BookingManager bookingManager; // contains all uploaded flights
    private AccountManager accountManager; // contains all existing user info
    public static final String USERDATADIR = "userdata"; //@TODO: decide if can remove from uploadactivity
    public static final String USERSFILE = "users.ser"; //@TODO: decide if can remove from uploadactivity
    public static final String FLIGHTSFILE = "flights.ser";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Populate map of usernames to passwords from passwords.txt
        // Find out where this application stores files, and get
        // the directory called USERDATADIR, or if it doesn't exist,
        // create it.

        try {
            File pwdData = this.getApplicationContext().getFilesDir(); //@TODO: verify that accomplished above
            File pwdFile = new File(pwdData, PASSWORDS);
            Scanner scanner = new Scanner(pwdFile);
            usernameToPassword = new HashMap<String, String[]>();
            String[] credentials;

            while (scanner.hasNextLine()) {
                credentials = scanner.nextLine().split(",");
                String[] pwdAndUserType = new String[2];
                pwdAndUserType[0] = credentials[1]; // the password
                pwdAndUserType[1] = credentials[2]; // user type (admin/client)
                usernameToPassword.put(credentials[0], pwdAndUserType);
            }
            scanner.close();
        } catch (java.io.IOException e)
        {
            System.out.println("Existing Users File not found");
            e.printStackTrace();
        }

        // Find out where this application stores files, and get
        // the directory called USERDATADIR, or if it doesn't exist,
        // create it.
        File userdata = this.getApplicationContext().getDir(USERDATADIR, MODE_PRIVATE);

        //Creates a file for the flights.ser file
        File flightsFile = new File(userdata, FLIGHTSFILE);
        File usersFile = new File(userdata, USERSFILE);

        // Create new Booking Manager and Account Manager.
        // These will be passed throughout app.
        bookingManager = new BookingManager(flightsFile);
        accountManager = new AccountManager(usersFile);
    }


    /**
     * Logs users with valid accounts into the app. Detects whether user is
     * Client or Admin and assigns privileges accordingly
     * @param view
     */
    public void doLogin(View view){
        // TODO:
        // check if Admin or Client
        // if Admin: pass Admin intent, to 'welcome admin' screen
        // if Client: pass Client intent and direct to client options or
        // 'search' screen

        // step 1: Get strings of fields to check what kind of user it is.
        EditText userEditText = (EditText) findViewById(R.id.email_address);
        EditText passwordEditText = (EditText) findViewById(R.id.password);

        email = userEditText.getText().toString();
        password = passwordEditText.getText().toString();

        String[] userInfo = {email, password};

        // Search records of registered clients
        if (isRegisteredUser(email, password))
        {
            if (isAdmin(email)) {
                 intent = new Intent(this, AdminDashboardActivity.class);
            } else {
                intent = new Intent(this, ClientDashboardActivity.class);
            }

            // Booking Manager info, Account Manager info, and client info into intent
            intent.putExtra("userKey", userInfo);
            intent.putExtra(Constants.BOOKINGMANAGER_KEY, bookingManager);
            intent.putExtra(Constants.ACCOUNTMANAGER_KEY, accountManager);
            startActivity(intent);
        }
        else
        {
            incorrectLoginPopUp();
        }
    }


    /**
     * Checks if User is already registered in the system.
     * ***NOTE: A better way *might* be to return the User's class (Client
     * or Admin), check whether it equals() Client or Admin,
     * and throw a NoSuchUser error if they aren't registered.
     * Then try/catch the error in the login method, and if the user is not
     * registered, prompt them to re-enter credentials or
     * create a new account.
     *
     * @param email
     * @param password
     * @return Return True if user is registered already.
     */
    private boolean isRegisteredUser(String email, String password) {
        boolean registered = false;
        String pwd;
        if (usernameToPassword.containsKey(email))
        {
            pwd = usernameToPassword.get(email)[0];
            if (pwd.equals(password))
            {
                registered = true;
            }
        }
        return registered;
    }

    /**
     * Return True if the given email is an admin
     * @param email
     * @return  if given email is an admin
     */
    private boolean isAdmin(String email) {
        this.isAdmin = false;
        String userType;
        if (usernameToPassword.containsKey(email))
        {
            userType = usernameToPassword.get(email)[1];
            if (userType.equals("a")) {
                this.isAdmin = true;
            }
        }
        return this.isAdmin;
    }

    private void incorrectLoginPopUp() {

        AlertDialog.Builder helpBuilder = new AlertDialog.Builder(this);
        helpBuilder.setTitle("Login Failed");
        helpBuilder.setMessage("Invalid Login");
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
}
