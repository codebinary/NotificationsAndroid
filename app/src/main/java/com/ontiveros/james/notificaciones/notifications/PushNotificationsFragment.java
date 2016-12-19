package com.ontiveros.james.notificaciones.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ontiveros.james.notificaciones.R;
import com.ontiveros.james.notificaciones.data.PushNotification;
import com.ontiveros.james.notificaciones.login.LoginContract;

import java.util.ArrayList;
/*
* Muestra la lista de notificaciones
* */
public class PushNotificationsFragment extends Fragment implements PushNotificationsContract.View {

    public static final String ACTION_NOTIFY_NEW_PROMO = "NOTIFY_NEW_PROMO";
    private BroadcastReceiver mNotificationReceiver;

    private RecyclerView mRecyclerView;
    private LinearLayout mNoMessageView;
    private PushNotificationsAdapter mNotificationsAdapter;

    private PushNotificationsPresenter mPresenter;

    public PushNotificationsFragment(){

    }

    public static PushNotificationsFragment newInstance(){
        PushNotificationsFragment fragment = new PushNotificationsFragment();
        //Setup de Argumentos
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            //Gets arguments
        }

        //Registramos un BroadcastReceiver para recibir los intents
        //Su comportamiento nos dice registrarlo en onResume y eliminaar en onPause
        mNotificationReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String title = intent.getStringExtra("title");
                String description = intent.getStringExtra("description");
                String expiryDate = intent.getStringExtra("expiry_date");
                String discount = intent.getStringExtra("discount");

                mPresenter.savePushMessage(title, description, expiryDate, discount);

            }
        };
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);

        mNotificationsAdapter = new PushNotificationsAdapter();
        mRecyclerView = (RecyclerView) root.findViewById(R.id.rv_notifications_list);
        mNoMessageView = (LinearLayout) root.findViewById(R.id.noMessage);

        mRecyclerView.setAdapter(mNotificationsAdapter);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity())
                .unregisterReceiver(mNotificationReceiver);
    }

    @Override
    public void showNotifications(ArrayList<PushNotification> notifications) {
        mNotificationsAdapter.replaceData(notifications);
    }

    @Override
    public void showEmptyState(boolean empty) {
        mRecyclerView.setVisibility(empty ? View.GONE : View.VISIBLE);
        mNoMessageView.setVisibility(empty ? View.VISIBLE : View.GONE);
    }

    @Override
    public void popPushNotifications(PushNotification pushMessage) {
        mNotificationsAdapter.addItem(pushMessage);
    }


    @Override
    public void setPresenter(PushNotificationsContract.Presenter presenter) {
        if(presenter != null){
            mPresenter = (PushNotificationsPresenter) presenter;
        }else{
            throw new RuntimeException("El presenter de notificaciones no puede ser null");
        }
    }
}
