package com.beachpartnerllc.beachpartner;



import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import com.beachpartnerllc.beachpartner.activity.LoginActivity;
import com.beachpartnerllc.beachpartner.connections.MyJobService;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


import java.util.Map;
import java.util.Random;

/**
 * Created by Owner on 4/15/2018.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    NotificationManager notificationManager;
    String ADMIN_CHANNEL_ID="BeachPartner";
    private static final String TAG = "MyFirebaseMsgService";

    @Override
//    public void onMessageReceived(RemoteMessage remoteMessage) {

//        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        Notification notification = new Notification.Builder(getApplicationContext())
//                .setContentTitle(remoteMessage.getNotification().getTitle())
//                .setContentText(remoteMessage.getNotification().getBody())
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setSound(defaultSoundUri)
//                .build();
//        NotificationManagerCompat manager = NotificationManagerCompat.from(getApplicationContext());
//        manager.notify(1, notification);


    public void onMessageReceived(RemoteMessage remoteMessage) {
       super.onMessageReceived(remoteMessage);
        Log.d(TAG, "From: " + remoteMessage.getData().size()); // for the data size
        final Map<String, String> data = remoteMessage.getData();
        String title = data.get("title");
        String body = data.get("body");
        if(!title.equals("") && !body.equals("")){
            sendNotificationData(title, body); //send notification to user
        }
//        Toast.makeText(this, "You just got a hi Five", Toast.LENGTH_SHORT).show();
    }

//    @RequiresApi(api = Build.VERSION_CODES.O) private void setupChannels()
//    { CharSequence adminChannelName = "BeachPartner";
//    NotificationChannel adminChannel;
//    adminChannel = new NotificationChannel(ADMIN_CHANNEL_ID, adminChannelName, NotificationManager.IMPORTANCE_HIGH);
//    adminChannel.enableLights(true);
//    adminChannel.setLightColor(Color.RED);
//    adminChannel.enableVibration(true);
//    if (notificationManager != null)
//        { notificationManager.createNotificationChannel(adminChannel);
//        }
//    }




    private void scheduleJob() {
        // [START dispatch_job]
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
        Job myJob = dispatcher.newJobBuilder()
                .setService(MyJobService.class)
                .setTag("my-job-tag")
                .build();
        dispatcher.schedule(myJob);
        // [END dispatch_job]
    }

    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private void handleNow() {
        Log.d(TAG, "Short lived task is done.");
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotificationData(String messageTitle,String messageBody) {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0 /* request code */, intent,PendingIntent.FLAG_UPDATE_CURRENT);

        long[] pattern = {500,500,500,500,500};

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(messageTitle)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setVibrate(pattern)
                .setLights(Color.BLUE,1,1)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }


}
