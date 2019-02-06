package com.Danthop.bionet.Class;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;
import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived( remoteMessage );


        if (remoteMessage.getData().isEmpty()) {
            ShowNotification( remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody() );
        }
        else{
            ShowNotification( remoteMessage.getData());
        }
    }

    private void ShowNotification(Map<String,String> data) {

        String title=data.get("title").toString();
        String body=data.get("body").toString();

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE );
        String NOTIFICATION_CHANNEL_ID = "com.Danthop.bionet";

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            NotificationChannel notificationChannel = new NotificationChannel( NOTIFICATION_CHANNEL_ID,"Notificacion",notificationManager.IMPORTANCE_DEFAULT );

            notificationChannel.setDescription( "" );
            notificationChannel.enableLights( true );
            notificationChannel.setLightColor( Color.BLUE );
            notificationChannel.setVibrationPattern( new long[]{0,1000,500,1000});
            notificationChannel.enableLights( true );
            notificationManager.createNotificationChannel( notificationChannel );
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder( this,NOTIFICATION_CHANNEL_ID );

        notificationBuilder.setAutoCancel( true )
                .setDefaults(Notification.DEFAULT_ALL )
                .setWhen( System.currentTimeMillis() )
                .setSmallIcon( android.R.drawable.alert_light_frame)
                .setContentTitle( title )
                .setContentText( body )
                .setContentInfo( "info" );

        notificationManager.notify( new Random().nextInt(),notificationBuilder.build());
    }

    private void ShowNotification(String title, String body) {
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE );
        String NOTIFICATION_CHANNEL_ID = "com.Danthop.bionet";

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            NotificationChannel notificationChannel = new NotificationChannel( NOTIFICATION_CHANNEL_ID,"Notificacion",notificationManager.IMPORTANCE_DEFAULT );

            notificationChannel.setDescription( "" );
            notificationChannel.enableLights( true );
            notificationChannel.setLightColor( Color.BLUE );
            notificationChannel.setVibrationPattern( new long[]{0,1000,500,1000});
            notificationChannel.enableLights( true );
            notificationManager.createNotificationChannel( notificationChannel );
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder( this,NOTIFICATION_CHANNEL_ID );

        notificationBuilder.setAutoCancel( true )
                .setDefaults(Notification.DEFAULT_ALL )
                .setWhen(System.currentTimeMillis())
                .setSmallIcon( android.R.drawable.alert_light_frame)
                .setContentTitle( title )
                .setContentText( body )
                .setContentInfo( "info" );

        notificationManager.notify( new Random().nextInt(),notificationBuilder.build());
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken( s );
        Log.d("TokenID",s);
    }

}
