package kr.o3selab.sunmoonbus.Services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import kr.o3selab.sunmoonbus.Constant.Constants;
import kr.o3selab.sunmoonbus.LoadingActivity;
import kr.o3selab.sunmoonbus.R;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "[MyFirebaseMessagingService]";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Constants.printLog(1, TAG + "From: " + remoteMessage.getFrom(), null);
        if (remoteMessage.getData().size() > 0) {
            Constants.printLog(1, TAG + "Data: " + remoteMessage.getData(), null);
        }
        if (remoteMessage.getNotification() != null) {
            Constants.printLog(1, TAG + "Notification: " + remoteMessage.getNotification().getBody(), null);
            sendNotification(remoteMessage.getNotification().getBody());
        }
    }

    private void sendNotification(String body) {
        Intent intent = new Intent(this, LoadingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0/* request code */, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(body)
                .setAutoCancel(false)
                .setSound(notificationSound)
                .setContentIntent(pendingIntent)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }
}
