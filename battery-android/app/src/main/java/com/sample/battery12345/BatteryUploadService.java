package com.sample.battery12345;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.BatteryManager;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class BatteryUploadService extends Service {
    public static final String CHANNEL_ID = "ForegroundServiceChannel";
    private static String endpoint = "";
    private RequestQueue queue;
    private Timer timer = new Timer();
    private static int intervalInMins = 1;

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        queue = Volley.newRequestQueue(getApplicationContext());
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                uploadToServer();
            }
        }, 0, intervalInMins*60*1000);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createNotificationChannel();
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Beaming battery status")
                .setContentText(endpoint)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);
        //do heavy work on a background thread
        //stopSelf();

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static void setServerDetails(String endpoint) {
        BatteryUploadService.endpoint = endpoint;
        Log.d("set new endpoint", endpoint);
    }

    public void uploadToServer () {
        BatteryManager bm = (BatteryManager) getSystemService(BATTERY_SERVICE);
        int batteryLevel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);

        String product = Build.PRODUCT;
        String model = Build.MODEL;
        String manufacturer = Build.MANUFACTURER;

        JSONObject params = new JSONObject();
        try {
            params.put("battery", batteryLevel);
            params.put("product", product);
            params.put("model", model);
            params.put("manufacturer", manufacturer);
        } catch (Exception e) {
            Log.e("error", e.toString());
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, endpoint, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("response", response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", error.toString());
            }
        });
        queue.add(request);
    }
}
