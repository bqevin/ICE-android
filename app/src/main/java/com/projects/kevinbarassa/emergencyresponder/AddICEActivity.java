package com.projects.kevinbarassa.emergencyresponder;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import com.getbase.floatingactionbutton.FloatingActionButton;

public class AddICEActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ice);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton done_ice = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.done_button);
        done_ice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Done adding ICE", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

}
