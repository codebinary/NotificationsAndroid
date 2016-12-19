package com.ontiveros.james.notificaciones.login;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ontiveros.james.notificaciones.R;
import com.ontiveros.james.notificaciones.notifications.PushNotificationsActivity;


public class LoginFragment extends Fragment implements LoginContract.View{

    private LoginContract.Presenter mPresenter;
    private TextInputEditText mEmail;
    private TextInputEditText mPassword;
    private Button mSignInButton;
    private View mLoginForm;
    private View mLoginProgress;
    private TextInputLayout mEmailError;
    private TextInputLayout mPasswordError;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private Callback mCallback;

    public static LoginFragment newInstance(){
        LoginFragment fragment = new LoginFragment();
        //Setup de argumentos en caso de que los tenga
        return fragment;
    }

    public LoginFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            //Extracción de argumnetos en caso los haya
        }

        //Obtenemos la instancia de FirebaseAuth
        mFirebaseAuth = FirebaseAuth.getInstance();
        //Creamos un AuthStateListener que responda a los cambios en el
        // estado de inicio de sesión del usuario:
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    //User is signed in
                    Log.d("ID", "onAuthStateChanged:signed_in: " + user.getUid());
                    showPushNotifications();
                }else{
                    //El usuario no esta logueado
                    Log.d("ID", "onAuthStateChanged:signed_out");
                }
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_login, container, false);
        mLoginForm = root.findViewById(R.id.login_form);
        mLoginProgress = root.findViewById(R.id.login_progress);

        mEmail = (TextInputEditText) root.findViewById(R.id.tv_email);
        mPassword = (TextInputEditText) root.findViewById(R.id.tv_password);
        mEmailError = (TextInputLayout) root.findViewById(R.id.til_email_error);
        mPasswordError = (TextInputLayout) root.findViewById(R.id.til_password_error);

        mSignInButton = (Button) root.findViewById(R.id.b_sign_in);

        //Eventos
        mEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mEmailError.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mPasswordError.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        return root;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof Callback){
            mCallback = (Callback) context;
        }else{
            throw new RuntimeException(context.toString() + " debe implementar Callback");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        //Registra el Framework con FirebaseAuth.addAuthStateListener() en onStart();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        //Aqui eliminamos el registro
        if(mAuthStateListener != null){
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    @Override
    public void onResume() {
        super.onResume();

        //Iniciamos el presentador
        mPresenter.start();
    }

    private void attemptLogin(){
        //Obtenemos las credenciales y se lo pasamos al presentador
        mPresenter.attempLogin(
                mEmail.getText().toString(),
                mPassword.getText().toString());
    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        if(presenter != null){
            mPresenter = presenter;
        }else{
            throw new RuntimeException("El presenter no puede ser nulo");
        }
    }

    @Override
    public void showProgress(boolean show) {
        mLoginForm.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoginProgress.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setEmailError(String error) {
        mEmailError.setError(error);
    }

    @Override
    public void setPasswordError(String error) {
        mPasswordError.setError(error);
    }

    @Override
    public void showLoginError(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showPushNotifications() {
        startActivity(new Intent(getActivity(), PushNotificationsActivity.class));
        getActivity().finish();
    }

    @Override
    public void showGooglePlayServicesDialog(int errorCode) {
        mCallback.onInvokeGooglePlayServices(errorCode);
    }

    @Override
    public void showGooglePlayServiceError() {
        Toast.makeText(getActivity(), "Se requiere Google Play Services para usar la app", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showNetworkError() {
        Toast.makeText(getActivity(), "La red no esta disponible. Conéctece y vuelva a intentarlo", Toast.LENGTH_SHORT).show();
    }

    interface Callback{
        void onInvokeGooglePlayServices(int errorCode);
    }

}
