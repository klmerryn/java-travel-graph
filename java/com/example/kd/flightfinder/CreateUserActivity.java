package com.example.kd.flightfinder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import managers.AccountManager;
import managers.BookingManager;

public class CreateUserActivity extends Activity {

    private boolean isAdmin = false;

    private BookingManager bookingManager;
    private AccountManager accountManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        // Kael added getIntent and get bM/aM from intent
        Intent intent = getIntent();

        accountManager = (AccountManager) intent.getSerializableExtra("accountManagerKey");
        bookingManager = (BookingManager) intent.getSerializableExtra("bookingManagerKey");

    }

    /**
     * Creates new account and saves user information to serialized file. Default account
     * created is Client privilege.
     * @param view
     */
    public void createAccount(View view){
        // @TODO:
        // if this.isAdmin is true: create admin account.
        // else: create Client account.
        Intent regIntent;

        EditText emailEditText = (EditText) findViewById(R.id.email_address);
        EditText passwordEditText = (EditText) findViewById(R.id.password);

        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        // Store user info
        String[] userInfo = {email, password};
        // May need to store userInfo in a file

        if (this.isAdmin == true) {
            regIntent = new Intent(this, AdminDashboardActivity.class);
            regIntent.putExtra("userKey", userInfo);
        }
        else {
            // Pass info to new screen:
            regIntent = new Intent(this, ClientDashboardActivity.class);
            regIntent.putExtra("userKey", userInfo);
        }

        startActivity(regIntent);

    }

    /**
     * Returns user to home login screen.
     * @param view
     */
    public void goToMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private boolean isAdmin() {
        //@TODO:
        // check if true!
        this.isAdmin = true;
        return this.isAdmin;
    }


}
