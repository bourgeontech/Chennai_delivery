package com.turtillion.estoredelivery.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.turtillion.estoredelivery.NewOrder;
import com.turtillion.estoredelivery.R;
import com.turtillion.estoredelivery.MainActivity;


/**
 * Created by GraceJoe on 31/05/17.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "PushNot-->";
    public static final String CHANNEL_ONE_ID = "1";
    public static final String CHANNEL_ONE_NAME = "Estore_Shop_Channel";

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);

        Log.d(TAG, "new token" + s);
        storeRegIdInPref(s);
        sendRegistrationToServer(s);
        Intent registrationComplete = new Intent("Registration_complete");
        registrationComplete.putExtra("token", s);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }


    private void sendRegistrationToServer(final String token) {
        // sending gcm token to server
        Log.d(TAG, "sendRegistrationToServer: " + token);

    }

    private void storeRegIdInPref(String token) {

        SharedPreferences pref = this.getSharedPreferences("my_pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("firebase_notification_token", token);
        editor.apply();

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        /* Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Common.showlog(TAG, "From: " + remoteMessage.getFrom());
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationHelper = new NotificationHelper(this);
        }
         */

//        if (remoteMessage.getData().size() > 0) {
//            Log.d(TAG, "payload:" + remoteMessage.getData());
//            String type = remoteMessage.getData().get("type");
//            String title = remoteMessage.getData().get("title");
//            String message = remoteMessage.getData().get("message");
//            showNotification(title, message, type);
//        } else
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "payload:2" + remoteMessage.getNotification().getTitle() + " " + remoteMessage.getNotification().getBody());
            String title = remoteMessage.getNotification().getTitle();
            String notimsg = remoteMessage.getNotification().getBody();
            showNotification(title, notimsg, "type");
        }

    }


    private void showNotification(String title, String message, String type) {

        Log.d(TAG, type);

        Intent intent;
        intent = new Intent(this, NewOrder.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("type", "type");

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, CHANNEL_ONE_ID)
                        .setSmallIcon(R.drawable.dlogo)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setGroup(type)
                        .setSound(defaultSoundUri)
                        .setColor(ContextCompat.getColor(this, R.color.colorPrimary))
                        .setGroupSummary(true)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            notificationBuilder.setPriority(NotificationManager.IMPORTANCE_MAX);
        } else {
            notificationBuilder.setPriority(NotificationCompat.PRIORITY_MAX);
        }


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ONE_ID,
                    CHANNEL_ONE_NAME,
                    NotificationManager.IMPORTANCE_HIGH);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
                notificationManager.notify((int) System.currentTimeMillis() /* ID of notification */, notificationBuilder.build());
            }
        }


    }
}
