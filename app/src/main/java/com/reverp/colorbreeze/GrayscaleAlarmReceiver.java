package com.reverp.colorbreeze;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;

public class GrayscaleAlarmReceiver extends BroadcastReceiver {
    public static final String ENABLE_GRAYSCALE_CODE = "ENABLE_GRAYSCALE";
    public static final String DISABLE_GRAYSCALE_CODE = "DISABLE_GRAYSCALE";
    public static final String STOP_RUNNING_SERVICES_CODE = "STOP_RUNNING_SERVICES";

    // Triggered by the Alarm periodically (starts the service to run task)
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = null;
        switch(intent.getAction())
        {
            case ENABLE_GRAYSCALE_CODE:
                i = new Intent(context, EnableGrayscaleService.class);
                break;

            case DISABLE_GRAYSCALE_CODE:
                Settings.Secure.putInt(context.getContentResolver(), "accessibility_display_daltonizer_enabled", 0);
                i = new Intent(context, EnableGrayscaleService.class);
                context.stopService(i);
                return;
/*                break;
            case STOP_RUNNING_SERVICES_CODE:
                i = new Intent(context, DisableGrayscaleService.class);
                context.stopService(i);
                return;*/
        }

        if(i != null)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(i);
            }
            else {
                context.startService(i);
            }
        }
    }
}