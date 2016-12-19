package com.ontiveros.james.notificaciones.login;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.auth.FirebaseAuth;
import com.ontiveros.james.notificaciones.R;
/*
* Screen de login basada en el método email/password de firebase
* */
public class LoginActivity extends AppCompatActivity implements LoginFragment.Callback {

    public static final int REQUEST_GOOGLE_PLAY_SERVICES = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LoginFragment loginFragment = (LoginFragment) getSupportFragmentManager()
                .findFragmentById(R.id.login_container);
        if (loginFragment == null){
            loginFragment = LoginFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.login_container, loginFragment)
                    .commit();
        }

        //Creamos el presentador
        LoginInteractor loginInteractor = new LoginInteractor(
                getApplicationContext(), FirebaseAuth.getInstance());
        new LoginPresenter(loginFragment, loginInteractor);



    }
    //Implementamos el controlador para el Dialog de Google Play Services
    @Override
    public void onInvokeGooglePlayServices(int errorCode) {
        showPlayServicesErrorDialog(errorCode);
    }

    void showPlayServicesErrorDialog(final int errorCode){
        Dialog dialog = GoogleApiAvailability.getInstance()
                .getErrorDialog(
                        LoginActivity.this,
                        errorCode,
                        REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }
}
