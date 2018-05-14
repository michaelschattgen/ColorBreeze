package com.reverp.colorbreeze;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class GrayscaleAlarmReceiver extends BroadcastReceiver {
    public static final String ENABLE_GRAYSCALE_CODE = "ENABLE_GRAYSCALE";
    public static final String DISABLE_GRAYSCALE_CODE = "DISABLE_GRAYSCALE";

    // Triggered by the Alarm periodically (starts the service to run task)
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = null;
        switch(intent.getAction())
        {
            case "ENABLE_GRAYSCALE":
                i = new Intent(context, EnableGrayscaleService.class);
                break;

            case "DISABLE_GRAYSCALE":
                i = new Intent(context, DisableGrayscaleService.class);
        }

        i = new Intent(context, EnableGrayscaleService.class);
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