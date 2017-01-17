package com.projects.kevinbarassa.emergencyresponder;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

/**
 * Created by Kevin Barassa on 17-Jan-17.
 */

public class ContactDetailed extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_details);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        //Init transfered data
        Bundle extras = getIntent().getExtras();

        //Display only if not null
        if(extras != null)

            collapsingToolbar.setTitle(extras.getString("EXTRA_NAME"));
        loadBackdrop();

        //Phone section
        TextView phone = (TextView) findViewById(R.id.simu);
        phone.setText(extras.getString("EXTRA_PHONE"));

        //Email section
        TextView email = (TextView) findViewById(R.id.pepe);
        email.setText(extras.getString("EXTRA_EMAIL"));

        //Residence Section
        TextView res = (TextView) findViewById(R.id.kuishi);
        res.setText(extras.getString("EXTRA_RESIDENCE"));

    }


    private void loadBackdrop() {
        //Load backdrop as clicked image
        final ImageView imageView = (ImageView) findViewById(R.id.backdrop);
        Glide.with(this).load(R.drawable.back).centerCrop().into(imageView);
    }
}
