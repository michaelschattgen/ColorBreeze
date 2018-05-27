package com.reverp.colorbreeze;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
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
        Log.d("Receiver", "onreceive"); // log to make sure that boot completed action is received

        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Log.d("Boot", "completed"); // log to make sure that boot completed action is received

            setGrayscaleAlarms(context);
        }


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
            if(!IsEnabled()){
                return;
            }

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
            if(isMyServiceRunning(EnableGrayscaleService.class, context))
            {

            }
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass, Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private boolean IsEnabled()
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this._context);
        Boolean isEnabled = prefs.getBoolean("enable_switch", false);

        return isEnabled;
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

    private void setGrayscaleAlarms(Context mContext) {
        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(mContext, GrayscaleAlarmReceiver.class);
        intent.setAction(GrayscaleAlarmReceiver.ENABLE_GRAYSCALE_CODE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 0, intent, 0);

        Intent disableIntent = new Intent(mContext, GrayscaleAlarmReceiver.class);
        disableIntent.setAction(GrayscaleAlarmReceiver.DISABLE_GRAYSCALE_CODE);
        PendingIntent disablePendingIntent = PendingIntent.getBroadcast(mContext, 0, disableIntent, 0);

        // Reset previous pending intent
        alarmManager.cancel(pendingIntent);
        alarmManager.cancel(disablePendingIntent);

        // Set the alarm to start at approximately 08:00 morning.
        Calendar beginCalendar = Calendar.getInstance();
        beginCalendar.setTimeInMillis(System.currentTimeMillis());
        beginCalendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(read("starttime_hour", "0")));
        beginCalendar.set(Calendar.MINUTE, Integer.parseInt(read("starttime_minute", "0")));
        beginCalendar.set(Calendar.SECOND, 0);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTimeInMillis(System.currentTimeMillis());
        endCalendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(read("stoptime_hour", "0")));
        endCalendar.set(Calendar.MINUTE, Integer.parseInt(read("stoptime_minute", "0")));
        endCalendar.set(Calendar.SECOND, 0);

        // If the scheduler date is passed, move scheduler time to tomorrow
        if (System.currentTimeMillis() > beginCalendar.getTimeInMillis()) {
            beginCalendar.add(Calendar.DAY_OF_YEAR, 1);
            endCalendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, beginCalendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, endCalendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, disablePendingIntent);

    }

    public String read(String valueKey, String valueDefault) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(_context);
        return prefs.getString(valueKey, valueDefault);
    }
}