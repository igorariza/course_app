package com.fesi.funda.utilidades;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class setPersistenceEnabled extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
