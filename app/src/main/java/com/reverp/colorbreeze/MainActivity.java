package com.reverp.colorbreeze;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_SECURE_SETTINGS);
        if(permissionCheck == PackageManager.PERMISSION_GRANTED)
        {
            Toast.makeText(this, "Success!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "nope :(", Toast.LENGTH_LONG).show();
        }

    }
}
