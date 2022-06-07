package com.example.mi_menu.data.model.user;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;

/**
 * Created by Sánchez Deivis on 10,febrero,2022
 */
public class Usuario implements Parcelable {

    //private File bussinesUrl;
    private String bussinesName;
    private String email;

    public Usuario() {
    }

    public Usuario(/*File bussinesUrl, */String bussinesName, String email) {
        /*this.setBussinesUrl(bussinesUrl);*/
        this.setBussinesName(bussinesName);
        this.setEmail(email);
    }

   /* public File getBussinesUrl() {
        return bussinesUrl;
    }

    public void setBussinesUrl(File bussinesUrl) {
        this.bussinesUrl = bussinesUrl;
    }*/

    protected Usuario(Parcel in) {
        bussinesName = in.readString();
        email = in.readString();
    }

    public static final Creator<Usuario> CREATOR = new Creator<Usuario>() {
        @Override
        public Usuario createFromParcel(Parcel in) {
            return new Usuario(in);
        }

        @Override
        public Usuario[] newArray(int size) {
            return new Usuario[size];
        }
    };

    public String getBussinesName() {
        return bussinesName;
    }

    public void setBussinesName(String bussinesName) {
        this.bussinesName = bussinesName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(bussinesName);
        parcel.writeString(email);
    }
}
