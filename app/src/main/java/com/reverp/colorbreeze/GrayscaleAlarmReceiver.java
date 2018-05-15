package com.reverp.colorbreeze;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;

public class GrayscaleAlarmReceiver extends BroadcastReceiver {
    public static final String ENABLE_GRAYSCALE_CODE = "ENABLE_GRAYSCALE";
    public static final String DISABLE_GRAYSCALE_CODE = "DISABLE_GRAYSCALE";

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = null;
        String actionString = intent.getAction();

        if (actionString == null) {
            return;
        }

        if (actionString.equals(ENABLE_GRAYSCALE_CODE)) {
            serviceIntent = new Intent(context, EnableGrayscaleService.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(serviceIntent);
            } else {
                context.startService(serviceIntent);
            }

        } else if (actionString.equals(DISABLE_GRAYSCALE_CODE)) {
            Settings.Secure.putInt(context.getContentResolver(), "accessibility_display_daltonizer_enabled", 0);
            serviceIntent = new Intent(context, EnableGrayscaleService.class);
            context.stopService(serviceIntent);
        }
    }
}