package com.reverp.colorbreeze;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import mehdi.sakout.fancybuttons.FancyButton;

public class MainActivity extends AppCompatActivity {
    int wavingHandEmojiUnicode = 0x1F44B;

    TextView headerTextView;
    FancyButton checkPermissionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InitializeControls();

        headerTextView.setText(String.format("Hey %s, this is Color Breeze", getEmojiByUnicode(wavingHandEmojiUnicode)));
        checkPermissionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckPermission();
            }
        });
    }

    public void CheckPermission()
    {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_SECURE_SETTINGS);
        if(permissionCheck == PackageManager.PERMISSION_GRANTED)
        {
            Toast.makeText(this, "Success!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "nope :(", Toast.LENGTH_LONG).show();
        }
    }

    public void InitializeControls()
    {
        headerTextView = findViewById(R.id.tvHeader);
        checkPermissionButton = findViewById(R.id.btnCheckPermission);
    }

    public String getEmojiByUnicode(int unicode){
        return new String(Character.toChars(unicode));
    }
}
