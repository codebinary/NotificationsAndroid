package com.ontiveros.james.notificaciones.login;

/**
 * Created by james on 11/12/16.
 */

public class LoginPresenter implements LoginContract.Presenter, LoginInteractor.Callback {

    private final LoginContract.View mLoginView;
    private LoginInteractor mLoginInteractor;

    public LoginPresenter(LoginContract.View mLoginView,
                          LoginInteractor mLoginInteractor) {
        this.mLoginView = mLoginView;
        mLoginView.setPresenter(this);
        this.mLoginInteractor = mLoginInteractor;
    }


    @Override
    public void attempLogin(String email, String password) {
        mLoginView.showProgress(true); //Mostramos el progress cuando sucede la autenticación
        mLoginInteractor.login(email, password, this);
    }

    @Override
    public void start() {
        //Comprobar si el usuario está logueado
    }

    @Override
    public void onEmailError(String msg) {
        mLoginView.showProgress(false);
        mLoginView.setEmailError(msg);
    }

    @Override
    public void onPasswordError(String msg) {
        mLoginView.showProgress(false);
        mLoginView.setPasswordError(msg);
    }

    @Override
    public void onNetworkConnectFailed() {
        mLoginView.showProgress(false);
        mLoginView.showNetworkError();
    }

    @Override
    public void onBeUserResolvableError(int errorCode) {
        mLoginView.showProgress(false);
        mLoginView.showGooglePlayServicesDialog(errorCode);
    }

    @Override
    public void onGooglePlayServicesFailed() {
        mLoginView.showGooglePlayServiceError();
    }

    @Override
    public void onAuthFailed(String msg) {
        mLoginView.showProgress(false);
        mLoginView.showLoginError(msg);
    }

    @Override
    public void onAuthSuccess() {
        mLoginView.showPushNotifications();
    }
}
