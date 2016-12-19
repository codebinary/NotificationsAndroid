package com.ontiveros.james.notificaciones.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;
import com.ontiveros.james.notificaciones.R;
import com.ontiveros.james.notificaciones.login.LoginActivity;

/**
 * Created by james on 11/12/16.
 */

public class PushNotificationsActivity extends AppCompatActivity {

    private static final String TAG = PushNotificationsActivity.class.getSimpleName();

    private PushNotificationsFragment mNotificationsFragment;
    private PushNotificationsPresenter mNotificationsPresenter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.title_activity_notifications));

        // ¿Existe un usuario loguead ?
        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        mNotificationsFragment = (PushNotificationsFragment) getSupportFragmentManager()
                .findFragmentById(R.id.notifications_container);

        if(mNotificationsFragment == null){
            mNotificationsFragment = PushNotificationsFragment.newInstance();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.notifications_container, mNotificationsFragment)
                    .commit();
        }

        mNotificationsPresenter = new PushNotificationsPresenter(
                mNotificationsFragment, FirebaseMessaging.getInstance()
        );


    }

}

