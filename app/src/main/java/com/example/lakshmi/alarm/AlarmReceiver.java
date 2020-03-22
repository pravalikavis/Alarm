package com.example.lakshmi.alarm;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.widget.EditText;
import android.widget.Toast;
import android.os.Bundle;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static android.content.Intent.getIntent;


public class AlarmReceiver extends BroadcastReceiver {


    private static final int MY_NOTIFICATION_ID=1;
    NotificationManager notificationManager;
    Notification myNotification;
    String s="hi";


    @Override
    public void onReceive(Context context, Intent intent ) {
        Toast.makeText(context, "Alarm received!", Toast.LENGTH_LONG).show();

        Intent myIntent = new Intent(context, SetAlarm.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                myIntent,
               0);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);

s=intent.getExtras().getString("st");
        Uri myUri = intent.getParcelableExtra("ring");

        myNotification = new NotificationCompat.Builder(context)
                .setContentTitle("Alarm:")
                .setContentText(s)
                .setTicker("Notification!")
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pendingIntent)

                .setSound(myUri)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.clk)
                .build();

        notificationManager =
                (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(MY_NOTIFICATION_ID, myNotification);
    }

}