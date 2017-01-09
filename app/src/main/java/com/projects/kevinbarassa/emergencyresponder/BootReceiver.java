package com.projects.kevinbarassa.emergencyresponder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Kevin Barassa on 09-Jan-17.
 */

public class BootReceiver extends BroadcastReceiver {
    public static final int REQUEST_CODE = 12345;
    public static final String ACTION = "com.projects.kevinbarassa.emergencyresponder";
    @Override
    public void onReceive(Context context, Intent intent) {
        //If boot complete, Emergency Service is initiated
        if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {
            Intent serviceIntent = new Intent(context, EmergencyService.class);
            context.startService(serviceIntent);
        }
    }
}
