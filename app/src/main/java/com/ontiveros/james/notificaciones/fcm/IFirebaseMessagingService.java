package com.ontiveros.james.notificaciones.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.ontiveros.james.notificaciones.R;
import com.ontiveros.james.notificaciones.notifications.PushNotificationsActivity;
import com.ontiveros.james.notificaciones.notifications.PushNotificationsFragment;

import java.util.Map;

public class IFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = IFirebaseMessagingService.class.getSimpleName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "Mensaje recibido!!");
        displayNotification(remoteMessage.getNotification(), remoteMessage.getData());
        sendNewPromoBroadcast(remoteMessage);
    }

    private void displayNotification(RemoteMessage.Notification notification, Map<String, String> data) {
        Intent intent = new Intent(this, PushNotificationsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_car)
                .setContentTitle(notification.getTitle())
                .setContentText(notification.getBody())
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());

    }

    private void sendNewPromoBroadcast(RemoteMessage remoteMessage) {
        Intent intent = new Intent(PushNotificationsFragment.ACTION_NOTIFY_NEW_PROMO);
        intent.putExtra("title", remoteMessage.getNotification().getTitle());
        intent.putExtra("description", remoteMessage.getNotification().getBody());
        intent.putExtra("expiry_date", remoteMessage.getData().get("expiry_date"));
        intent.putExtra("discount", remoteMessage.getData().get("discount"));
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }
    
    


    public IFirebaseMessagingService() {
    }

}
