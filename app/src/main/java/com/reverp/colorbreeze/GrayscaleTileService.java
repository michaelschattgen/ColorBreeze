package com.reverp.colorbreeze;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

@RequiresApi(api = Build.VERSION_CODES.N)
public class GrayscaleTileService extends TileService {
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onTileAdded() {
        super.onTileAdded();
    }

    @Override
    public void onTileRemoved() {
        super.onTileRemoved();
    }

    @Override
    public void onStartListening() {
        super.onStartListening();

        Tile tile = getQsTile();
        if (HasPermission())
        {
            tile.setState(IsGrayscaled() ? Tile.STATE_ACTIVE : Tile.STATE_INACTIVE);
        }
        else
        {
            tile.setState(Tile.STATE_UNAVAILABLE);
            tile.setLabel("No permission");
        }

        tile.updateTile();
    }

    @Override
    public void onStopListening() {
        super.onStopListening();
    }

    @Override
    public void onClick() {
        super.onClick();

        Tile tile = getQsTile();
        if (HasPermission())
        {
            if (IsGrayscaled())
            {
                DisableGrayscale();
                tile.setState(Tile.STATE_INACTIVE);
            } else
            {
                EnableGrayscale();
                tile.setState(Tile.STATE_ACTIVE);
            }
        }
        else
        {
            tile.setState(Tile.STATE_UNAVAILABLE);
            tile.setLabel("No permission");
        }
        tile.updateTile();
    }

    public void EnableGrayscale()
    {
        Settings.Secure.putInt(getContentResolver(), "accessibility_display_daltonizer", 0);
        Settings.Secure.putInt(getContentResolver(), "accessibility_display_daltonizer_enabled", 1);
    }

    public void DisableGrayscale()
    {
        Settings.Secure.putInt(getContentResolver(), "accessibility_display_daltonizer_enabled", 0);
        Intent serviceIntent = new Intent(this, EnableGrayscaleService.class);
        stopService(serviceIntent);
    }

    public boolean HasPermission()
    {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_SECURE_SETTINGS);
        boolean grantedPermission = permissionCheck == PackageManager.PERMISSION_GRANTED;

        return grantedPermission;
    }

    public boolean IsGrayscaled()
    {
        int enabled;
        try
        {
            enabled = Settings.Secure.getInt(getContentResolver(), "accessibility_display_daltonizer_enabled");
        } catch (Settings.SettingNotFoundException e)
        {
            e.printStackTrace();
            return false;
        }

        return enabled != 0;
    }
}