package com.projects.kevinbarassa.emergencyresponder;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v4.app.NotificationCompat;

/**
 * Created by Kevin Barassa on 09-Jan-17.
 */

public class EmergencyService extends IntentService {
    public EmergencyService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (android.os.Build.VERSION.SDK_INT > 9) {

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()

                    .permitAll().build();

            StrictMode.setThreadPolicy(policy);

        }

        //Tell people they are covered
        createNotification();
    }

    //Setup notification
    public void createNotification() {
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
}
