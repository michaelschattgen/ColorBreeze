package com.reverp.colorbreeze;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.JobIntentService;
import android.util.Log;

public class DisableGrayscaleService extends JobIntentService {

    public DisableGrayscaleService() {
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
        Notification.Builder builder = new Notification.Builder(this)
                .setContentTitle("Stopping grayscale mode")
                .setContentText("Shutting down... Please wait.")
                .setSmallIcon(R.drawable.ic_ink)
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId("channel1");
            builder.setTimeoutAfter(3000);

        }

        Notification runningNotification = builder.build();
        runningNotification.flags |= Notification.FLAG_AUTO_CANCEL;

        startForeground(1, runningNotification);

        Settings.Secure.putInt(getContentResolver(), "accessibility_display_daltonizer_enabled", 0);
        //super.onCreate();

    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {

        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();

        stopSelf();
        notificationManager.cancel(1);
        super.onDestroy();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }
}
