package com.ontiveros.james.notificaciones.login;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Patterns;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by james on 10/12/16.
 */

/*
* Interactor del login
* */
public class LoginInteractor {

    private final Context mContext;
    private FirebaseAuth mFirebaseAuth;

    public LoginInteractor(Context context, FirebaseAuth firebaseAuth){
        mContext = context;
        if (firebaseAuth != null){
            mFirebaseAuth = firebaseAuth;
        }else{
            throw new RuntimeException("La instancia de FirebaseAuth no puede ser null");
        }
    }

    public void login(String email, String password, final Callback callback){
        //Check lógica
        boolean c1 = isValidEmail(email, callback);
        boolean c2 = isValidPassword(password, callback);

        if(!(c1 && c2)){
            return;
        }

        //Check red
        if(!isNetworkAvailable()){
            callback.onNetworkConnectFailed();
            return;
        }

        //Check Google Play Service
        if(!isGooglePlayServicesAvailable(callback)){
            return;
        }

        //Consultar Firebase Authentication
        signInUser(email, password, callback);

    }

    private boolean isValidPassword(String password, Callback callback){
        boolean isValid = true;
        if(TextUtils.isEmpty(password)){
            callback.onPasswordError("Escribe tu contraseña");
            isValid = false;
        }
        return isValid;
    }

    private boolean isValidEmail(String email, Callback callback){
        boolean isValid = true;
        if(TextUtils.isEmpty(email)){
            callback.onEmailError("Escribe tu correo");
            isValid = false;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            callback.onEmailError("Correo no válido");
            isValid = false;
        }
        return isValid;
    }

    private boolean isNetworkAvailable(){
        //Verificamos la conexión de red en android
        ConnectivityManager connMgr = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    //Verificamos si Google Play Services está disponible
    private boolean isGooglePlayServicesAvailable(Callback callback){
        int statusCode = GoogleApiAvailability.getInstance()
                .isGooglePlayServicesAvailable(mContext);

        if(GoogleApiAvailability.getInstance().isUserResolvableError(statusCode)){
            callback.onBeUserResolvableError(statusCode);
            return false;
        }else if(statusCode != ConnectionResult.SUCCESS){
            callback.onGooglePlayServicesFailed();
            return false;
        }
        return true;
    }

    //Solo se llama al método si las tres restricciones han sido cumplidas
    private void signInUser(String email, String password, final Callback callback){
        mFirebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            //Si deseamos obtener los datos del usuarios
                            //lo obtenermos con AuthResutl.getUser()

                            //Si en algúm momento deseamos cerrar cesión
                            //Lo hacemos con el método FirebaseAuth.getInstance().signOut();
                            callback.onAuthFailed(task.getException().getMessage());
                        }else{
                            callback.onAuthSuccess();
                        }
                    }
                });
    }

    //Interface que comunica los resultados al presenter
    interface Callback{

        //Reporta al presenter que hubo un error en el email. El parámetro es el mensaje que
        //se mostrará al usuario
        void onEmailError(String msg);

        //Similar a onEmailError() pero para el campo de la contraseña
        void onPasswordError(String msg);

        //Reporta al presenter la no disponibilidad de la red
        void onNetworkConnectFailed();

        //Reporta al presenter que hay un error de Play Services, pero es
        //posible que el usuario pueda arreglarlo con un asistente de Android
        void onBeUserResolvableError(int errorCode);

        //Reporta al presenter un error de Play Service que no puede resolver el usuario
        void onGooglePlayServicesFailed();

        //Reporta al presenter un error en la autenticación en Firebase
        void onAuthFailed(String msg);

        //Reporta al presenter que la autenticación fue exitosa
        void onAuthSuccess();
    }
}
