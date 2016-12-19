package com.ontiveros.james.notificaciones.notifications;

import com.ontiveros.james.notificaciones.BasePresenter;
import com.ontiveros.james.notificaciones.BaseView;
import com.ontiveros.james.notificaciones.data.PushNotification;
import com.ontiveros.james.notificaciones.login.LoginContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by james on 14/12/16.
 */

/**
 * Interacción MVP en Notificaciones
 */

public interface PushNotificationsContract {

    interface View extends BaseView<Presenter>{
        void showNotifications(ArrayList<PushNotification> notifications);

        void showEmptyState(boolean empty);

        void popPushNotifications(PushNotification pushMessage);
    }

    interface Presenter extends BasePresenter{
        void registerAppClient();

        void loadNotifications();

        void savePushMessage(String title, String description, String expiryDate, String discount);
    }
}
