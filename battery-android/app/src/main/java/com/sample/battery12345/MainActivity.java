package com.sample.battery12345;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startService (View v) {
        Intent serviceIntent = new Intent(this, BatteryUploadService.class);
        serviceIntent.putExtra("inputExtra", "Battery upload service running");

        ContextCompat.startForegroundService(this, serviceIntent);
    }

    public void stopService (View v) {
        Intent serviceIntent = new Intent(this, BatteryUploadService.class);
        stopService(serviceIntent);
    }

    public void updateServerDetails (View v) {
        TextView endpointText = (TextView) findViewById(R.id.endpoint);
        try {
            BatteryUploadService.setServerDetails(endpointText.getText().toString());
        } catch (Exception e) {
            Log.d("updateServerDetails", e.getMessage());
            Log.d("updateServerDetails", e.toString());
        }
    }
}
