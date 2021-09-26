package com.turtillion.estoredelivery.volleyservice;

import android.annotation.SuppressLint;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MySingleTon {
    @SuppressLint("StaticFieldLeak")
    private static MySingleTon mySingleTon;
    private static RequestQueue requestQueue;
    @SuppressLint("StaticFieldLeak")
    private static Context mctx;

    private MySingleTon(Context context) {
        mctx = context;
        requestQueue = getRequestQueue();

    }

    public static synchronized MySingleTon getInstance(Context context) {
        if (mySingleTon == null) {
            mySingleTon = new MySingleTon(context);
        }
        return mySingleTon;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(mctx.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQue(Request<T> request) {
        requestQueue.add(request);

    }

}
