package com.projects.kevinbarassa.emergencyresponder;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.StrictMode;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;

/**
 * Created by Kevin Barassa on 09-Jan-17.
 */

public class EmergencyService extends IntentService {
    public EmergencyService() {
        super("EmergencyService");
    }

    //Emergency listener
    private ShakeListener mShaker;

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
        //Send SMS on SOS
        String messageToSend = "Am in emergency situation. Kindly call back ASAP";
        String ice1 = "+254719747908"; //Joram Mwashighadi number
        // SmsManager.getDefault().sendTextMessage(ice1, null, messageToSend, null,null);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.call)
                        .setContentTitle("Broadcast Alert!")
                        .setContentText("You have broadcasted SOS to your ICE Contact List")
                        .setStyle(new NotificationCompat.BigTextStyle().bigText("Keep calm and wait for the response. You can shake your phone once more to repeat SOS broadcasting!"));
        //Update notification
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(3, mBuilder.build());
    }

    //Setup notification
    public void welcomeNotification() {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.call)
                        .setContentTitle("Emergency Responder")
                        .setContentText("Emergency responder is auto-activated")
                        .setStyle(new NotificationCompat.BigTextStyle().bigText("Thank you for choosing safe life. Make sure you have min of 5/- or unlimited 200 SMS @10/- for efficiency."));
        //Update notification
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(3, mBuilder.build());
    }

    //Persistent Notification
    public void reminderNotification() {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.call)
                        .setContentTitle("Emergency Responder")
                        .setContentText("Emergency responder was already activated on phone boot")
                        .setStyle(new NotificationCompat.BigTextStyle().bigText("Thank you for choosing safe life. Make sure you have min of 5/- or unlimited 200 SMS @10/- for efficiency."));
        //Update notification
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(3, mBuilder.build());
    }

}
