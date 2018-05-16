package com.reverp.colorbreeze;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.JobIntentService;
import android.support.v4.app.NotificationCompat;

public class EnableGrayscaleService extends JobIntentService {

    public EnableGrayscaleService() {
        super();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        //The intent to launch when the user clicks the expanded notification
        Intent intent = new Intent(this, GrayscaleAlarmReceiver.class);
        intent.setAction(GrayscaleAlarmReceiver.DISABLE_GRAYSCALE_CODE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder builder = new Notification.Builder(this)
                .setContentTitle("Grayscale mode activated")
                .setContentText("Press here to deactivate and bring back your colors.")
                .setSmallIcon(R.drawable.ic_ink)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setOngoing(true);

            builder.setChannelId("channel1");
            Notification runningNotification = builder.build();
             runningNotification.flags |= Notification.FLAG_AUTO_CANCEL;
            startForeground(1, runningNotification);
        } else {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setContentTitle("Grayscale mode activated")
                    .setContentText("Press here to deactivate and bring back your colors.")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true);

            Notification runningNotification = builder.build();
             runningNotification.flags |= Notification.FLAG_AUTO_CANCEL;
            startForeground(1, runningNotification);
        }

        Settings.Secure.putInt(getContentResolver(), "accessibility_display_daltonizer", 0);
        Settings.Secure.putInt(getContentResolver(), "accessibility_display_daltonizer_enabled", 1);

        super.onCreate();
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {

    }
}
