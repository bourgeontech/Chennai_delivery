package com.turtillion.estoredelivery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.turtillion.estoredelivery.base.BaseActivity;
import com.turtillion.estoredelivery.utils.Constants;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.PermissionRequest;

import static com.turtillion.estoredelivery.utils.Constants.PERMISSIONS;

public class SplashActivity  extends BaseActivity implements EasyPermissions.PermissionCallbacks {
    private Runnable mRunnable;
    private static final long SPLASH_DISPLAY_LENGTH = 2000;
    private Handler mHandler;
    private String[] permsLocation = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CALL_PHONE};

    String sdboy_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sdboy_id = sharedPreferences.getString(Constants.DBOY_ID, "");


        mRunnable = new Runnable() {
            @Override
            public void run() {
                requestPerms();
            }
        };
        mHandler = new Handler();
        mHandler.postDelayed(mRunnable, SPLASH_DISPLAY_LENGTH);
    }

    private void requestPerms() {

        if (EasyPermissions.hasPermissions(SplashActivity.this, permsLocation)) {
            if (sdboy_id.equals("")) {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            } else {
                Intent in = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(in);
                finish();
            }

        }else {
            EasyPermissions.requestPermissions(
                    new PermissionRequest.Builder(this, PERMISSIONS, permsLocation)
                            .setRationale("Need permissions to Allow")
                            .build());
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        if (sdboy_id.equals("")) {
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            Intent in = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(in);
            finish();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Toast.makeText(SplashActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
    }
}