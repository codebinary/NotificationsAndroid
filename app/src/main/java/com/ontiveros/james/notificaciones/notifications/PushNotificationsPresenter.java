package com.ontiveros.james.notificaciones.notifications;

import android.text.TextUtils;

import com.google.firebase.messaging.FirebaseMessaging;
import com.ontiveros.james.notificaciones.data.PushNotification;
import com.ontiveros.james.notificaciones.data.PushNotificationsRepository;
import com.ontiveros.james.notificaciones.login.LoginContract;

import java.util.ArrayList;

/**
 * Created by james on 14/12/16.
 */

/*
* Presentador de las notificaciones
* */

public class PushNotificationsPresenter implements PushNotificationsContract.Presenter {

    //Añadimos dos campos para la vista y otro para el componente de Firebase
    private final PushNotificationsContract.View mNotificationView;
    private final FirebaseMessaging mFCMInteractor;

    public PushNotificationsPresenter(PushNotificationsContract.View notificationView,
                                      FirebaseMessaging FCMInteractor){
        this.mNotificationView = notificationView;
        this.mFCMInteractor = FCMInteractor;

        notificationView.setPresenter(this);
    }

    @Override
    public void registerAppClient() {
        mFCMInteractor.subscribeToTopic("promos");
    }

    @Override
    public void loadNotifications() {
        PushNotificationsRepository.getInstance().getPushNotifications(
                new PushNotificationsRepository.LoadCallback(){
                    @Override
                    public void onLoaded(ArrayList<PushNotification> notifications) {
                        if(notifications.size() > 0){
                            mNotificationView.showEmptyState(false);
                            mNotificationView.showNotifications(notifications);
                        }else{
                            mNotificationView.showEmptyState(true);
                        }
                    }
                }
        );
    }

    @Override
    public void savePushMessage(String title, String description, String expiryDate, String discount) {
        PushNotification pushMessage = new PushNotification();
        pushMessage.setmTitle(title);
        pushMessage.setmDescription(description);
        pushMessage.setmExpiryDate(expiryDate);
        pushMessage.setmDiscount(TextUtils.isEmpty(discount) ? 0 : Float.parseFloat(discount));

        PushNotificationsRepository.getInstance().savePushNotification(pushMessage);

        mNotificationView.showEmptyState(false);
        mNotificationView.popPushNotifications(pushMessage);
    }

    @Override
    public void start() {
        registerAppClient();
        loadNotifications();
    }
}
