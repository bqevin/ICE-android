package com.projects.kevinbarassa.emergencyresponder;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.StrictMode;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.arsy.maps_library.MapRipple;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Kevin Barassa on 09-Jan-17.
 */

public class EmergencyService extends IntentService {
    public EmergencyService() {
        super("EmergencyService");
    }

    //Emergency listener
    private ShakeListener mShaker;
    //GPS object
   // GPSTracker gps;

    @Override
    protected void onHandleIntent(Intent intent) {
        if (android.os.Build.VERSION.SDK_INT > 9) {

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()

                    .permitAll().build();

            StrictMode.setThreadPolicy(policy);

        }

        //Tell people they are covered
        welcomeNotification();

        //Shaking params
        final Vibrator vibe = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

        mShaker = new ShakeListener(this);
        mShaker.setOnShakeListener(new ShakeListener.OnShakeListener () {
            public void onShake()
            {
                vibe.vibrate(100);
                alertSMS();

            }
        });

    }


    //Setup ALert notification
    public void alertSMS() {
        //Location
        // create class object
//        gps = new GPSTracker(EmergencyService.this);
//        // check if GPS enabled
//        if (gps.canGetLocation()) {
//            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + gps.getLatitude() + "\nLong: " + gps.getLongitude(), Toast.LENGTH_LONG).show();
//
//        } else {
//            // can't get location
//            // GPS or Network is not enabled
//            // Ask user to enable GPS/network in settings
//            gps.showSettingsAlert();
//        }

        //Send SMS on SOS
        String messageToSend = "Am in emergency situation. Kindly call back ASAP";
        String ice1 = "+254719747908"; //Joram Mwashighadi number
        // SmsManager.getDefault().sendTextMessage(ice1, null, messageToSend, null,null);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.call)
                        .setContentTitle("Emergency Alert!")
                        .setContentText("You have declared you are in dangerous situation.")
                        .setStyle(new NotificationCompat.BigTextStyle().bigText("Keep calm and wait for help!"));
        //Update notification
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(3, mBuilder.build());
    }

    //Setup notification
    public void welcomeNotification() {
        // Ensure GPS enabled on phone boot
//        if (!gps.canGetLocation()) {
//            // can't get location
//            // GPS or Network is not enabled
//            // Ask user to enable GPS/network in settings
//            gps.showSettingsAlert();
//        }

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.call)
                        .setContentTitle("Emergency Responder")
                        .setContentText("Safety checker is active")
                        .setStyle(new NotificationCompat.BigTextStyle().bigText("In-case of anything, emergency message will be send. Thank you for using Swahilipot Hub product. "));
        //Update notification
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(3, mBuilder.build());
    }


}
