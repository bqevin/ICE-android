package com.projects.kevinbarassa.emergencyresponder;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.github.javiersantos.bottomdialogs.BottomDialog;
import com.projects.kevinbarassa.emergencyresponder.helper.SQLiteHandler;
import com.projects.kevinbarassa.emergencyresponder.helper.SessionManager;


import static android.view.View.*;


public class MainActivity extends AppCompatActivity implements View.OnClickListener  {

    private SessionManager session;
    private ShakeListener mShaker;
    private SQLiteHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        FabOptions fabOptions = (FabOptions) findViewById(R.id.fab_options);
//        fabOptions.setButtonsMenu(this, R.menu.menu);
//        fabOptions.setOnClickListener(this);

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());
        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        if (savedInstanceState != null) {
            return;
        }
        //Shaking params
        final Vibrator vibe = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

        mShaker = new ShakeListener(this);
        mShaker.setOnShakeListener(new ShakeListener.OnShakeListener () {
            public void onShake()
            {
                vibe.vibrate(100);
                sendSMS();
            }
        });





        //Fab and button
        FloatingActionButton add_ice = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.add_button);
        Button sos = (Button) findViewById(R.id.send_sos);

        //Add ICE on Fab
        add_ice.setOnClickListener(new OnClickListener(){
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AddICEActivity.class);
                startActivity(intent);
            }

        });


        sos.setOnClickListener(new OnClickListener(){
            public void onClick(View v) {

            }

        });

    }
    private void sendSMS(){
        //Send SMS on SOS
        String messageToSend = "Am in emergency situation. Kindly call ^Kevin";
        String ice1 = "+254719747908"; //Joram Mwashighadi number
       // SmsManager.getDefault().sendTextMessage(ice1, null, messageToSend, null,null);
        if(!((Activity) this).isFinishing())
        {
        new BottomDialog.Builder(this)
                .setTitle("Broadcast Alert!")
                .setContent("You broadcasted SOS to ur ICE Contact List")
                .setIcon(R.drawable.call)
                .show();
        }


        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.call)
                        .setContentTitle("Broadcast Alert!")
                        .setContentText("Your ICE Contact has been informed of your emergency")
                        .setStyle(new NotificationCompat.BigTextStyle().bigText("Keep calm and wait for the response. You can shake your phone once more to repeat SOS broadcasting!"));
        //Update notification
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(3, mBuilder.build());
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


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.faboptions_add:
                Intent intent = new Intent(MainActivity.this,AddICEActivity.class);
                startActivity(intent);
                break;

            case R.id.faboptions_view:
                Toast.makeText(MainActivity.this, "View ICE", Toast.LENGTH_SHORT).show();
                break;


            case R.id.faboptions_edit:
                Toast.makeText(MainActivity.this, "Edit ICE", Toast.LENGTH_SHORT).show();
                break;

            case R.id.faboptions_share:
                Toast.makeText(MainActivity.this, "Share ICE", Toast.LENGTH_SHORT).show();
                break;

            default:
                // no-op
        }
    }

    private void logoutUser() {
        session.setLogin(false);

        db.dropDB();

        // Launching the login activity
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
