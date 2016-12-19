package com.ontiveros.james.notificaciones.login;

import com.ontiveros.james.notificaciones.BasePresenter;
import com.ontiveros.james.notificaciones.BaseView;

/**
 * Created by james on 10/12/16.
 */

public interface LoginContract {

    interface View extends BaseView<Presenter>{
        void showProgress(boolean show);

        void setEmailError(String error);

        void setPasswordError(String error);

        void showLoginError(String msg);

        void showPushNotifications();

        void showGooglePlayServicesDialog(int errorCode);

        void showGooglePlayServiceError();

        void showNetworkError();
    }

    interface Presenter extends BasePresenter {
        void attempLogin(String email, String password);
    }
}
