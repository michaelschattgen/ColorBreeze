package com.reverp.colorbreeze;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;

public class EnableGrayscaleService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Settings.Secure.putInt(getContentResolver(), "accessibility_display_daltonizer", 0);
        Settings.Secure.putInt(getContentResolver(), "accessibility_display_daltonizer_enabled", 1);

        return super.onStartCommand(intent, flags, startId);
    }
}
