package com.projects.kevinbarassa.emergencyresponder;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.projects.kevinbarassa.emergencyresponder.app.AppController;
import com.projects.kevinbarassa.emergencyresponder.helper.SQLiteHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class ContactActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ContactAdapter adapter;
    private ProgressDialog p;
    private List<ContactItem> contacts;
    //Swipe to refresh
    SwipeRefreshLayout refresherL;

    //Init SQLite
    private SQLiteHandler db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        recyclerView = (RecyclerView)findViewById(R.id.contact_recycler);
        //Swipe refresh layout init
        refresherL = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);

        //Ensures every item on recycler view has fixed size
        recyclerView.setHasFixedSize(true);
        //Use LinearLayout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Create data for the contact
        contacts = new ArrayList<>();

        //Initiate network action with screen message
        p = new ProgressDialog(this);

        //Satisfy condition to fetch contact relating to you only
        db = new SQLiteHandler(getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();
        String email = user.get("email");
        String URL_DATA = "http://agrigender.net/emergency/users/ice_contact.php?parent="+email;

        //Render contact list
        // We first check for cached request
        Cache cache = AppController.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(URL_DATA);
        if (entry != null) {
            // fetch the data from cache
            try {
                String data = new String(entry.data, "UTF-8");
                try {
                    parseContact(new JSONObject(data));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        } else {
            p.setMessage("Fetching Contacts");
            p.show();
            newContactRequest();
        }

                /*
         *
         * Refreshes Articles
         */
        //Sets animation color
        refresherL.setColorSchemeColors(Color.parseColor("#ed0202"), Color.parseColor("#c40053"),Color.parseColor("#ffffff"), Color.parseColor("#51af50"));
        refresherL.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        //Checks if there is internet before making a data call
                        if (isNetworkConnected()){
                            //Ensures consistency of data is observed
                            adapter.notifyDataSetChanged();
                            //Clears previous fetched data
                            contacts.clear();
                            // Makes a new network call
                            newContactRequest();
                            //Stop loading animation
                            refresherL.setRefreshing(false);
                        }else {
                            //Activate internet error Snackbar
                            Toast.makeText(getApplicationContext(),"No internet connection! Please reconnect and try again",Toast.LENGTH_LONG).show();
                            //Stop refreshing animation
                            refresherL.setRefreshing(false);
                        }
                    }
                });
    }


    //Checks internet connection
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    /**
     * Parsing local storage data to Contact adapter
     * */
    private void parseContact(JSONObject response) {
        try {
            JSONArray feedArray = response.getJSONArray("contacts");

            for (int i = 0; i < feedArray.length(); i++) {

                //fetch individual objects
                JSONObject o = (JSONObject) feedArray.get(i);

                //Contact objects
                ContactItem item = new ContactItem();

                item.setIce_name(o.getString("name"));
                item.setIce_residence(o.getString("residence"));
                item.setIce_blood(o.getString("blood"));
                item.setIce_email(o.getString("email"));
                item.setIce_phone(o.getString("phone"));
                item.setIce_created(o.getString("created_at"));

                contacts.add(item);
            }
            //Create adapter object
            adapter = new ContactAdapter(contacts, getApplicationContext());
            //Set adapter
            recyclerView.setAdapter(adapter);
            // notify data changes to recycler
            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void newContactRequest(){
        /**
         * TODO: Implement DRY later on
         */
        db = new SQLiteHandler(getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();
        String email = user.get("email");
        String URL_DATA = "http://agrigender.net/emergency/users/ice_contact.php?parent="+email;

        // making fresh volley request and getting json
        JsonObjectRequest jsonReq = new JsonObjectRequest(Request.Method.GET,
                URL_DATA, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                //VolleyLog.d(TAG, "Response: " + response.toString());
                if (response != null) {
                    parseContact(response);
                    p.dismiss();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // VolleyLog.d(TAG, "Error: " + error.getMessage());
                //Dismiss dialog
                p.dismiss();
                //Error connecting
                Toast.makeText(getApplicationContext(),"Error connection to internet",Toast.LENGTH_LONG).show();

            }
        });

        // Adding request to volley request queue
        AppController.getInstance().addToRequestQueue(jsonReq);
    }


}
