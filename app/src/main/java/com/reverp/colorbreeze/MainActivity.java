package com.reverp.colorbreeze;

import android.Manifest;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.io.DataOutputStream;
import java.io.IOException;

import mehdi.sakout.fancybuttons.FancyButton;

public class MainActivity extends AppCompatActivity {
    int wavingHandEmojiUnicode = 0x1F44B;

    TextView headerTextView;
    TextView subheaderTextView;
    FancyButton checkPermissionButton;
    Switch enableGrayscaleSwitch;

    boolean userGrantedPermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InitializeControls();
        UserGrantedPermission();
        CreateNotificationChannel();

        headerTextView.setText(String.format("Hey %s, this is Color Breeze", getEmojiByUnicode(wavingHandEmojiUnicode)));
        checkPermissionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(UserGrantedPermission())
                {
                    Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                    getApplicationContext().startActivity(intent);
                } else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                    alert.setTitle("No permission");
                    String message = "This app doesn't have access to the secure settings of " +
                            "your device. Find out how to grant the correct permissions here: " +
                            "<a href=\"https://reverp.github.io/ColorBreeze\">https://reverp.github.io/ColorBreeze</a>";
                    alert.setMessage(Html.fromHtml(message));
                    alert.setNegativeButton("Ok", null);

                    AlertDialog dialog = alert.create();
                    dialog.show();
                    ((TextView)dialog.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());

                }
            }
        });
        enableGrayscaleSwitch.setChecked(IsGrayscaled());

        enableGrayscaleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean enable) {
                if(enable)
                    EnableGrayscale();
                else
                    DisableGrayscale();
            }
        });

        MobileAds.initialize(this, "ca-app-pub-9499836165206516~3588197975");
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("78184CD5062BF37A140AF21C857744BA").build();
        mAdView.loadAd(adRequest);

    }

    private void CreateNotificationChannel() {
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        String id = "channel1";

        CharSequence name = "Background job notification";

        String description = "Notifications in this channel will only be used to ...";

        int importance = NotificationManager.IMPORTANCE_LOW;

        NotificationChannel mChannel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel(id, name,importance);
            mChannel.setDescription(description);
            mNotificationManager.createNotificationChannel(mChannel);

        }
    }

    public boolean UserGrantedPermission()
    {
        this.userGrantedPermission = HasPermission();

        if(userGrantedPermission)
        {
            enableGrayscaleSwitch.setVisibility(View.VISIBLE);
            subheaderTextView.setText(R.string.subheader_has_permission);
            checkPermissionButton.setText(getResources().getString(R.string.button_has_permission));
        } else {
            subheaderTextView.setText(R.string.subheader_no_permission);
            enableGrayscaleSwitch.setVisibility(View.INVISIBLE);
            checkPermissionButton.setText(getResources().getString(R.string.button_no_permission));
        }

        return userGrantedPermission;
    }

    public boolean HasPermission()
    {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_SECURE_SETTINGS);
        boolean grantedPermission = permissionCheck == PackageManager.PERMISSION_GRANTED;

        return grantedPermission;
    }

    public void InitializeControls()
    {
        headerTextView = findViewById(R.id.tvHeader);
        subheaderTextView = findViewById(R.id.tvSubheader);
        checkPermissionButton = findViewById(R.id.btnCheckPermission);
        enableGrayscaleSwitch = findViewById(R.id.swEnableGrayscale);
    }

    public String getEmojiByUnicode(int unicode){
        return new String(Character.toChars(unicode));
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

    public boolean IsGrayscaled()
    {
        if(!HasPermission())
        {
            return false;
        }

        int enabled;
        try
        {
            enabled = Settings.Secure.getInt(getContentResolver(), "accessibility_display_daltonizer_enabled");
        } catch (Settings.SettingNotFoundException e)
        {
            e.printStackTrace();

            Toast.makeText(this, "Can't get current grayscale state", Toast.LENGTH_SHORT).show();
            return false;
        }

        return enabled != 0;
    }

    public void CheckRoot() {
        Process p;
        try {
            p = Runtime.getRuntime().exec("su");
            DataOutputStream su = new DataOutputStream(p.getOutputStream());
            su.writeBytes("pm grant com.reverp.colorbreeze android.permission.WRITE_SECURE_SETTINGS\n");
            su.writeBytes("exit\n");
            su.flush();
            try {
                p.waitFor();
                if (p.exitValue() != 1) {
                    Log.d(this.getPackageName(), "success getting root");
                }
                else {
                    Log.d(this.getPackageName(), "failing getting root");
                }
            } catch (InterruptedException e) {
                Log.d(this.getPackageName(), "failing getting root");
            }
        } catch (IOException e) {
            Log.d(this.getPackageName(), "failing getting root");
        }
    }
}
