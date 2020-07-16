package com.hamlet.MrFixer;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;

import java.util.ArrayList;
import java.util.List;

public class Notifications extends Application {

    public static final String CHANNEL_ONE_ID = "CHANNEL_ONE_ID";
    public static final String CHANNEL_TWO_ID = "CHANNEL_TWO_ID";

    @Override
    public void onCreate() {
        super.onCreate();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            NotificationChannel channelLogIn = new NotificationChannel(CHANNEL_ONE_ID,"LOG IN", NotificationManager.IMPORTANCE_HIGH);
            channelLogIn.setDescription("This channel is for LOG IN notification");

            NotificationChannel channelSignUp = new NotificationChannel(CHANNEL_TWO_ID,"LOG IN", NotificationManager.IMPORTANCE_DEFAULT);
            channelSignUp.setDescription("This channel is for LOG IN notification");

            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            List<NotificationChannel> channelList = new ArrayList<>();
            channelList.add(channelLogIn);
            channelList.add(channelSignUp);

            manager.createNotificationChannels(channelList);
        }
    }
}
