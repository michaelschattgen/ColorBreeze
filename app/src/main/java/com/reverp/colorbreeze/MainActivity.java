package com.reverp.colorbreeze;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    int wavingHandEmojiUnicode = 0x1F44B;

    TextView headerTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InitializeControls();

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_SECURE_SETTINGS);
        if(permissionCheck == PackageManager.PERMISSION_GRANTED)
        {
            Toast.makeText(this, "Success!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "nope :(", Toast.LENGTH_LONG).show();
        }

        headerTextView.setText(String.format("Hey %s, this is Color Breeze", getEmojiByUnicode(wavingHandEmojiUnicode)));
    }

    public void InitializeControls()
    {
        headerTextView = findViewById(R.id.tvHeader);
    }

    public String getEmojiByUnicode(int unicode){
        return new String(Character.toChars(unicode));
    }
}
