package com.pramodrepaka.notifyme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button btNotify, btUpdate, btCancel;
    private static final String PRIMARY_CHANNEL_ID = "primary_notification_channel"; //Channel ID
    private static final int NOTIFICATION_ID = 0;
    private NotificationManager mNotificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btNotify = (Button) findViewById(R.id.btNotify);
        btUpdate = (Button) findViewById(R.id.btUpdate);
        btCancel = (Button) findViewById(R.id.btCancel);

        btNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNotification();
            }
        });

        btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateNotification();
                setNotificationButtonState(true,false,false);
            }
        });

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelNotification();
            }
        });

        createNotificationChannel();
    }

    //method stub for sending notifications
    public void sendNotification(){
        NotificationCompat.Builder notifyBuilder = getNotificationBuilder();
        mNotificationManager.notify(NOTIFICATION_ID,notifyBuilder.build());

        setNotificationButtonState(false,true,true);
    }

    public void updateNotification(){
        // This will update notification to big picture style
        Bitmap  androidImage = BitmapFactory.decodeResource(getResources(),R.drawable.starter_img);
        NotificationCompat.Builder notifyBuilder = getNotificationBuilder();
        notifyBuilder.setStyle(new NotificationCompat.BigPictureStyle()
                .bigPicture(androidImage)
                .setBigContentTitle("Notification Updated!!!")
        );

        mNotificationManager.notify(NOTIFICATION_ID,notifyBuilder.build());

        setNotificationButtonState(false,false,true);
    }

    public void cancelNotification(){
        // This method will clear the notification from the status bar
        mNotificationManager .cancel(NOTIFICATION_ID);
        setNotificationButtonState(true,false,false);
    }

    public void createNotificationChannel(){
        mNotificationManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
            //Create a notificationChannel
            NotificationChannel notificationChannel = new NotificationChannel(
                    PRIMARY_CHANNEL_ID, //Channel ID
                    "Test Notification Channel", //name for the channel
                    NotificationManager.IMPORTANCE_HIGH); //Importance Level

            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("Notification from test Notification Channel");

            mNotificationManager.createNotificationChannel(notificationChannel);
        }
    }

    //helper method to create our notification builder object
    public NotificationCompat.Builder getNotificationBuilder(){

        //Create an intent that will launch when a user taps on the notification
        Intent notificationIntent = new Intent(this,MainActivity.class);

        PendingIntent notificationPendingIntent = PendingIntent.getActivity(
                this,
                NOTIFICATION_ID,
                notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        //create and instantiate our notification builder
        NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(
                this, //application context
                PRIMARY_CHANNEL_ID) //notification channel ID
                    .setContentTitle("You've been notified")
                    .setContentText("This is your detail text for your notification")
                    .setSmallIcon(R.drawable.ic_stat_name)
                    .setContentIntent(notificationPendingIntent)
                    .setAutoCancel(true);

        return notifyBuilder;

    }

    public void  setNotificationButtonState(Boolean isNotifyEnabled,
                                            Boolean isUpdateEnabled,
                                            Boolean isCancelEnabled){
        btNotify.setEnabled(isNotifyEnabled);
        btUpdate.setEnabled(isUpdateEnabled);
        btCancel.setEnabled(isCancelEnabled);
    }

}
