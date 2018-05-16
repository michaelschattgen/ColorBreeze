package com.reverp.colorbreeze;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Set;

public class GrayscaleAlarmReceiver extends BroadcastReceiver {
    public static final String ENABLE_GRAYSCALE_CODE = "ENABLE_GRAYSCALE";
    public static final String DISABLE_GRAYSCALE_CODE = "DISABLE_GRAYSCALE";

    private Context _context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this._context = context;

        if(!IsSelectedDay())
        {
            return;
        }

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

    private boolean IsSelectedDay()
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this._context);
        Set<String> selectedDays = prefs.getStringSet("pref_selected_days", null);

        String todayString = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(Calendar.getInstance().getTime());
        if(!selectedDays.isEmpty())
        {
            return selectedDays.contains(todayString);
        }

        return false;
    }
}