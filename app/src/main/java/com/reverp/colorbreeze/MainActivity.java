package com.reverp.colorbreeze;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

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

        headerTextView.setText(String.format("Hey %s, this is Color Breeze", getEmojiByUnicode(wavingHandEmojiUnicode)));
        checkPermissionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(UserGrantedPermission())
                {
                    Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                    getApplicationContext().startActivity(intent);
                }
            }
        });

        enableGrayscaleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean enable) {
                if(enable)
                    EnableGrayscale();
                else
                    DisableGrayscale();
            }
        });
    }

    public boolean UserGrantedPermission()
    {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_SECURE_SETTINGS);
        boolean grantedPermission = permissionCheck == PackageManager.PERMISSION_GRANTED;
        this.userGrantedPermission = grantedPermission;

        if(grantedPermission)
        {
            enableGrayscaleSwitch.setVisibility(View.VISIBLE);
            subheaderTextView.setText(R.string.subheader_has_permission);
            checkPermissionButton.setText(getResources().getString(R.string.button_has_permission));
        } else {
            subheaderTextView.setText(R.string.subheader_no_permission);
            enableGrayscaleSwitch.setVisibility(View.INVISIBLE);
            checkPermissionButton.setText(getResources().getString(R.string.button_no_permission));
        }

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
    }
}
