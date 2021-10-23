package com.anthony.sena.turisena;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

public class MyApplication extends Application {

    public static JsonObjectRequest jsonObjectRequest;
    public static RequestQueue requestQueue;

    @Override
    public void onCreate() {
        requestQueue = Volley.newRequestQueue(this);
        super.onCreate();

    }
}
