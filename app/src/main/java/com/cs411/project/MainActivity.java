package com.cs411.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.security.ProviderInstaller;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Timer;
import java.util.TimerTask;

import javax.net.ssl.SSLContext;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Intent selectIntent = new Intent(this, LoginActivity.class);
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                startActivity(selectIntent);
                finish();
            }
        };
        timer.schedule(task, 3000);
    }

}
