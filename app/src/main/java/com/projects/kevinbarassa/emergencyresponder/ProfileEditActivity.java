package com.projects.kevinbarassa.emergencyresponder;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.projects.kevinbarassa.emergencyresponder.app.AppController;
import com.projects.kevinbarassa.emergencyresponder.helper.SQLiteHandler;
import com.projects.kevinbarassa.emergencyresponder.helper.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class ProfileEditActivity extends AppCompatActivity {

    private TextView txtEmail;
    private SQLiteHandler db;
    private SessionManager session;
    private ProgressDialog pDialog;
    private EditText inputBlood;
    private EditText inputAllergy;
    private EditText inputPhone;
    private EditText inputCondition;
    private Button btnEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_edit);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        CollapsingToolbarLayout collapsingToolbar =
//                (CollapsingToolbarLayout) findViewById(R.id.profile_toolbar);
//        txtEmail = (TextView) findViewById(R.id.email);

        inputBlood = (EditText) findViewById(R.id.blood_profile);
        inputPhone = (EditText) findViewById(R.id.phone_profile);
        inputAllergy = (EditText) findViewById(R.id.allergy_profile);
        inputCondition = (EditText) findViewById(R.id.problem_profile);
        btnEdit = (Button) findViewById(R.id.edit_btn);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }



        // Edit Button Click event
        btnEdit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String blood = inputBlood.getText().toString().trim();
                String phone = inputPhone.getText().toString().trim();
                String allergy = inputAllergy.getText().toString().trim();
                String problem = inputCondition.getText().toString().trim();

                // Fetching email from sqlite
                HashMap<String, String> user = db.getUserDetails();
                String email = user.get("email");
                //Check to see user has filled all fields
                if (!blood.isEmpty() && !phone.isEmpty() && !allergy.isEmpty() && !problem.isEmpty()) {
                    Log.d("Post data","Email: "+email + "  Blood: "+blood + "  Phone: "+phone + "  Alllergy: "+allergy+" Condition: "+problem);
                    updateUser(email, blood, phone, allergy, problem);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please fill your details before updating!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });


    }

    /**
     * Function to store user in MySQL database will post params(tag, name,
     * email, password) to register url
     * */
    private void updateUser(final String email, final String blood,
                            final String phone, final String allergy, final String problem) {
        // Tag used to cancel the request
        String tag_string_req = "req_edit_user";

        pDialog.setMessage("Updating your info ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_EDIT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Server Comms: ", "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        String uid = jObj.getString("uid");

                        JSONObject user = jObj.getJSONObject("user");
                        String name = user.getString("name");
                        String email = user.getString("email");
                        String created_at = user
                                .getString("created_at");
                        String updated_at = user
                                .getString("updated_at");
                        String blood = user
                                .getString("blood");
                        String phone = user
                                .getString("phone");
                        String allergy = user
                                .getString("allergy");
                        String problem = user
                                .getString("problem");

                        // Delete existing data first
                        db.deleteUsers();

                        // Inserting row in users table
                        db.addUser(name, email, uid, blood, phone, allergy, problem, updated_at, created_at);
                        Toast.makeText(getApplicationContext(), "You info has successfully been updated!", Toast.LENGTH_LONG).show();

                        // Launch login activity
                        Intent intent = new Intent(
                                ProfileEditActivity.this,
                                ProfileActivity.class);
                        startActivity(intent);
                        finish();
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to edit url
                Map<String, String> params = new HashMap<String, String>();
                params.put("phone", phone);
                params.put("email", email);
                params.put("blood", blood);
                params.put("allergy", allergy);
                params.put("problem", problem);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
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

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(ProfileEditActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
