package com.example.iot_app;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.iot_app.home_page.HomeFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StatusService extends Service {
    private DatabaseReference sosRef, callStatusRef, idSkypeRef;
    private NotificationManager notificationManager;
    private Ringtone ringtone;
    private final String notificationChannelId = "SOSStatusChannel";
    private final int sosStatusNotificationId = 1;

    public void initiateSkypeUri(Context myContext, String mySkypeUri) {

        // Make sure the Skype for Android client is installed.
        if (!isSkypeClientInstalled(myContext)) {
            goToMarket(myContext);
            return;
        }

        // Create the Intent from our Skype URI.
        Uri skypeUri = Uri.parse(mySkypeUri);
        Intent myIntent = new Intent(Intent.ACTION_VIEW, skypeUri);

        // Check if there is an activity available that can handle the intent
        if (myIntent.resolveActivity(myContext.getPackageManager()) != null) {
            myContext.startActivity(myIntent);
        } else {
            Toast.makeText(myContext, "No application can handle this request."
                    + " Please install a webbrowser",  Toast.LENGTH_LONG).show();
            myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.skype.com"));
        }
        return;
    }

    public boolean isSkypeClientInstalled(Context myContext) {
        PackageManager myPackageMgr = myContext.getPackageManager();
        try {
            myPackageMgr.getPackageInfo("com.skype.raider", PackageManager.GET_ACTIVITIES);
        }
        catch (PackageManager.NameNotFoundException e) {
            return (false);
        }
        return (true);
    }

    public void goToMarket(Context myContext) {
        Uri marketUri = Uri.parse("market://details?id=com.skype.raider");
        Intent myIntent = new Intent(Intent.ACTION_VIEW, marketUri);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        myContext.startActivity(myIntent);

        return;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sosRef = FirebaseDatabase.getInstance().getReference("robot").child("SOS");
        callStatusRef = FirebaseDatabase.getInstance().getReference("robot").child("call_status");
        idSkypeRef = FirebaseDatabase.getInstance().getReference("robot").child("id_skype");

        Log.d("đã connect với Service", String.valueOf(sosRef));
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Uri notificationSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        ringtone = RingtoneManager.getRingtone(getApplicationContext(), notificationSoundUri);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    notificationChannelId,
                    "SOS Status Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription("Channel for sos status notifications");
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), notificationChannelId)
                .setContentTitle("SOS Status")
                .setContentText("Checking SOS status...")
                .setSmallIcon(R.drawable.cold_storage)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        startForeground(sosStatusNotificationId, notificationBuilder.build());
        sosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // Get the data from the snapshot
                String sosStatus = snapshot.getValue(String.class);
                Log.d("value_powerStatus", "Value is: " + sosStatus);
                if ("true".equals(sosStatus)) {
                    Intent intent = new Intent(getApplicationContext(), HomeFragment.class);
                    PendingIntent pendingIntent = PendingIntent.getActivity(
                            getApplicationContext(),
                            0,
                            intent,
                            PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
                    );
                    NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), notificationChannelId)
                            .setContentTitle("SOS Status")
                            .setContentText("The gas is exceeding the allowable threshold")
                            .setSmallIcon(R.drawable.error)
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            .setContentIntent(pendingIntent);
                    notificationManager.notify(sosStatusNotificationId, notificationBuilder.build());

                     if (!ringtone.isPlaying()) {
                         ringtone.play();
                     }
                } else {
                    notificationBuilder.setContentText("The gas is within the allowable range");
                    notificationBuilder.setSmallIcon(R.drawable.cold_storage);
                    notificationManager.notify(sosStatusNotificationId, notificationBuilder.build());

                     if (ringtone.isPlaying()) {
                         ringtone.stop();
                     }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Error Ringtone: " + error, Toast.LENGTH_SHORT).show();
            }
        });

        idSkypeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // Get the data from the snapshot
                String idSkype = snapshot.getValue(String.class);
                if (idSkype != null && !idSkype.equals("null")) {
                    // Initiate Skype call
                    initiateSkypeUri(getApplicationContext(), "skype:" + idSkype + "?call&video=true");
                    // After the call, update the callStatusRef value in Firebase to true
                    callStatusRef.setValue("true");
                    idSkypeRef.setValue("null");
                } else {
                    // If the call was not made, update the callStatusRef value in Firebase to false
//                    callStatusRef.setValue("false");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Error: " + error, Toast.LENGTH_SHORT).show();
            }
        });

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

