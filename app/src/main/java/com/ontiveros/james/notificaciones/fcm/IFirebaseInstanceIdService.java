package com.ontiveros.james.notificaciones.fcm;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class IFirebaseInstanceIdService extends FirebaseInstanceIdService {

    private static final String TAG = IFirebaseInstanceIdService.class.getSimpleName();

    public IFirebaseInstanceIdService() {

    }

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String fcmToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "FCM Token: " + fcmToken);

        sendTokenToServer(fcmToken);
    }

    private void sendTokenToServer(String fcmToken){
        //Acciones para enviar token a tu app server
    }
}
