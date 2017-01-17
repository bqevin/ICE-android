package com.projects.kevinbarassa.emergencyresponder;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.projects.kevinbarassa.emergencyresponder.app.AppController;
import com.projects.kevinbarassa.emergencyresponder.helper.SQLiteHandler;
import com.projects.kevinbarassa.emergencyresponder.helper.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddICEActivity extends AppCompatActivity {
    private EditText inputName;
    private EditText inputPhone;
    private EditText inputEmail;
    private EditText inputResidence;
    private EditText inputBlood;
    private Button btnEdit;
    private ProgressDialog pDialog;
    private SQLiteHandler db;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ice);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Get values
        inputName = (EditText) findViewById(R.id.ice_name);
        inputPhone = (EditText) findViewById(R.id.ice_phone);
        inputEmail = (EditText) findViewById(R.id.ice_email);
        inputResidence = (EditText) findViewById(R.id.ice_residence);
        inputBlood = (EditText) findViewById(R.id.ice_blood);
        FloatingActionButton done_ice = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.ice_done);

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




//        done_ice.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                Intent intent = new Intent(AddICEActivity.this,MapsActivity.class);
////                startActivity(intent);
//                Intent intent = new Intent(AddICEActivity.this,ContactActivity.class);
//                startActivity(intent);
//                Snackbar.make(view, "Done adding ICE", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });


        // Add ICE Button Click event
        done_ice.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String blood = inputBlood.getText().toString().trim();
                String phone = inputPhone.getText().toString().trim();
                String residence = inputResidence.getText().toString().trim();
                String name = inputName.getText().toString().trim();
                String email = inputEmail.getText().toString().trim();


                //Check to see user has filled all fields
                if (!blood.isEmpty() && !phone.isEmpty() && !residence.isEmpty() && !email.isEmpty() && !name.isEmpty()) {
                    Log.d("Post data","Email: "+email + "  Blood: "+blood + "  Phone: "+phone + "  Residence: "+residence+" Name: "+name );
                    updateICE(name, email, blood, phone,residence);
                } else {
                    Snackbar.make(view, "Please fill your details before updating!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
//                    Toast.makeText(getApplicationContext(),
//                            "Please fill your details before updating!", Toast.LENGTH_LONG)
//                            .show();
                }
            }
        });

    }

    /**
     * Function to store ICE in MySQL database will post params(tag, name,
     * email, phone, residence, blood) to add_contact url
     * */
    private void updateICE(final String name, final String email, final String blood,
                            final String phone, final String residence) {
        // Tag used to cancel the request
        String tag_string_req = "req_ice_user";

        pDialog.setMessage("Adding ICE Contact ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_ADD_ICE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Server Comms: ", "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // ICE Contact successfully stored in MySQL
                        // Now store the ICE in sqlite
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
                        String residence = user
                                .getString("residence");



                        // Inserting row in ICE table
                        db.addICE(name, email, uid, blood, phone, residence, updated_at, created_at);
                        Toast.makeText(getApplicationContext(), "You have successfully added an Emergency Contact!", Toast.LENGTH_LONG).show();
                        // Launch Process ICE activity
                        Intent intent = new Intent(
                                AddICEActivity.this,
                                ContactActivity.class);
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
                // Posting params to add_ice url
                Map<String, String> params = new HashMap<String, String>();
                params.put("phone", phone);
                params.put("email", email);
                params.put("blood", blood);
                params.put("name", name);
                params.put("residence", residence);

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

    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     * */
    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // redirect to login activity
        Intent intent = new Intent(AddICEActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}
