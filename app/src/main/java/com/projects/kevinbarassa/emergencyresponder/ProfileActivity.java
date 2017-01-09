package com.projects.kevinbarassa.emergencyresponder;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.projects.kevinbarassa.emergencyresponder.helper.SQLiteHandler;
import com.projects.kevinbarassa.emergencyresponder.helper.SessionManager;

import java.util.HashMap;


public class ProfileActivity extends AppCompatActivity {

    private TextView txtPhone;
    private TextView txtLocation;
    private TextView txtEmail;
    private TextView txtBio;

    private SQLiteHandler db;
    private SessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.profile_toolbar);
        txtEmail = (TextView) findViewById(R.id.email);
        txtLocation = (TextView) findViewById(R.id.location);
        txtBio = (TextView) findViewById(R.id.bio);
        txtPhone = (TextView) findViewById(R.id.phone);





        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        // Fetching user details from sqlite
        HashMap<String, String> user = db.getUserDetails();

        String name = user.get("name");
        String email = user.get("email");
        String bio = user.get("bio");
        String location = user.get("location");
        String phone = user.get("phone");

        if (email.isEmpty()) {
            logoutUser();
        }

        // Displaying the user details on the screen
        collapsingToolbar.setTitle(name);
        txtEmail.setText(email);
        txtBio.setText(bio);
        txtPhone.setText(phone);
        txtLocation.setText(location);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Activating edit mode", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                // Launching the edit activity
                Intent intent = new Intent(ProfileActivity.this, ProfileEditActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            logoutUser();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     * */
    private void logoutUser() {
        session.setLogin(false);

        db.dropDB();

        // Launching the login activity
        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
