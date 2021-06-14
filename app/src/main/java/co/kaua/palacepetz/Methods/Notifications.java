package co.kaua.palacepetz.Methods;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;

import androidx.annotation.RequiresApi;

import co.kaua.palacepetz.R;

/**
 *  Copyright (c) 2021 Kauã Vitório
 *  Official repository https://github.com/Kauavitorio/PalacePetz
 *  Responsible developer: https://github.com/Kauavitorio
 *  @author Kaua Vitorio
 **/

@SuppressWarnings("deprecation")
public class Notifications {

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void show(Context context){
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_APP_EMAIL);
        String CHANNEL_ID="NOTIFICATION_MY_CHANNEL";
        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,"successfully_register_account", NotificationManager.IMPORTANCE_LOW);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,1,intent,0);
        Notification notification = new Notification.Builder(context,CHANNEL_ID)
                .setContentText(context.getString(R.string.please_confirmEmail))
                .setContentTitle(context.getString(R.string.your_account_was_created))
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .addAction(R.mipmap.ic_launcher, context.getString(R.string.open_email),pendingIntent)
                .setChannelId(CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setOnlyAlertOnce(true)
                .setVibrate(new long[]{1000, 1000, 1000,
                        1000, 1000})
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(notificationChannel);
        notificationManager.notify(1, notification);
    }
}
