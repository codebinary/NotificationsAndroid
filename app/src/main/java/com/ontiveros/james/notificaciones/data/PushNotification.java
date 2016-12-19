package com.ontiveros.james.notificaciones.data;

import java.util.UUID;

/**
 * Created by james on 14/12/16.
 */

/*
* Representación de una promoción en forma de push notification
* */
public class PushNotification {
    private String id;
    private String mTitle;
    private String mDescription;
    private String mExpiryDate;
    private float mDiscount;

    public PushNotification(){
        id = UUID.randomUUID().toString();
    }

    public String getId(){
        return id;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public float getmDiscount() {
        return mDiscount;
    }

    public void setmDiscount(float mDiscount) {
        this.mDiscount = mDiscount;
    }

    public String getmExpiryDate() {
        return mExpiryDate;
    }

    public void setmExpiryDate(String mExpiryDate) {
        this.mExpiryDate = mExpiryDate;
    }
}
