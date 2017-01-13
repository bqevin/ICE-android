package com.projects.kevinbarassa.emergencyresponder;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProcessICEActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_ice);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView textView = (TextView) findViewById(R.id.contact_name);
        CircleImageView avatar = (CircleImageView) findViewById(R.id.contact_avatar);


    }


}
