package com.turtillion.estoredelivery.base;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.turtillion.estoredelivery.volleyservice.VolleyUtils;


public class BaseActivity extends AppCompatActivity {
    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    public Activity mActivity;
    public String live_Latitude, live_Longitude;
    public VolleyUtils volleyUtils = new VolleyUtils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("ogl-app", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

    }

}