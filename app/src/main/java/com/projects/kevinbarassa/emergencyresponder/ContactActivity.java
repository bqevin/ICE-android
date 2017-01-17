package com.projects.kevinbarassa.emergencyresponder;

import android.app.ProgressDialog;
import android.os.Bundle;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class ContactActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ContactAdapter adapter;
    private ProgressDialog p;
    private List<ContactItem> contacts;
    private static final String URL_DATA = "http://agrigender.net/emergency/users/ice_contact.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        recyclerView = (RecyclerView)findViewById(R.id.contact_recycler);
        //Ensures every item on recycler view has fixed size
        recyclerView.setHasFixedSize(true);

        //Use LinearLayout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Create data for the contact
        contacts = new ArrayList<>();

        //Initiate network action with screen message
        p = new ProgressDialog(this);

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

                }
            });

            // Adding request to volley request queue
            AppController.getInstance().addToRequestQueue(jsonReq);
        }


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


}
