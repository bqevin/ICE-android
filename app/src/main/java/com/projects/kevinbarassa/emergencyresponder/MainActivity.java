package com.projects.kevinbarassa.emergencyresponder;

import android.content.Context;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;

import static android.view.View.*;


public class MainActivity extends AppCompatActivity {

    private ShakeListener mShaker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Shaking params
        final Vibrator vibe = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

        mShaker = new ShakeListener(this);
        mShaker.setOnShakeListener(new ShakeListener.OnShakeListener () {
            public void onShake()
            {
                vibe.vibrate(100);
                Toast.makeText(getApplicationContext(), "Just got shaken", Toast.LENGTH_LONG).show();

            }
        });

        //Fab and button
        FloatingActionButton add_ice = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.add_button);
        Button sos = (Button) findViewById(R.id.send_sos);

        //Send SMS method
        final String messageToSend = "Am in emergency situation. Kindly call ^Kevin";
        final String ice1 = "+254719747908"; //Joram Mwashighadi number


        sos.setOnClickListener(new OnClickListener(){
            public void onClick(View v) {
                SmsManager.getDefault().sendTextMessage(ice1, null, messageToSend, null,null);
                Toast.makeText(getApplicationContext(), "You broadcasted SOS to Joram Mwashighadi in ur ICE Contact List", Toast.LENGTH_LONG).show();
            }

        });

    }
    @Override
    public void onResume()
    {
        mShaker.resume();
        super.onResume();
    }
    @Override
    public void onPause()
    {
        mShaker.pause();
        super.onPause();
    }

}
