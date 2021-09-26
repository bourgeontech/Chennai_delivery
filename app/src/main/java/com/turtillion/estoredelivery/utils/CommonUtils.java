package com.turtillion.estoredelivery.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.Window;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.turtillion.estoredelivery.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class CommonUtils {

    private static Dialog progressdialog;

    public static void setProgressBar(Context context) {
        progressdialog = new Dialog(context, R.style.AlertDialogCustom);
        progressdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressdialog.setContentView(R.layout.progressdialog);
        progressdialog.setCancelable(false);

        Objects.requireNonNull(progressdialog.getWindow()).setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
        progressdialog.getWindow().getAttributes().windowAnimations = R.style.AlertDialogCustom;
        if (!((Activity) context).isFinishing()) {
            progressdialog.show();
        }

    }


    public static void cancelProgressBar() {

        if (progressdialog != null && progressdialog.isShowing()) {
            progressdialog.cancel();
        }

    }

    public static String getTodayDate() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        return df.format(c);
    }

    public static String getTodayDate2() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd EEEE, MMMM yyyy", Locale.US);
        return df.format(c);
    }
    public static String getTodayDate3() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        return df.format(c);
    }
    public static boolean checkConnectivity(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            assert cm != null;
            NetworkInfo info = cm.getActiveNetworkInfo();
            if (info == null) {
                return false;
            } else if (info.isConnectedOrConnecting()) {
                return true;
            }
        } catch (Exception e) {
            Log.e("Error", e.toString());
        }
        return false;
    }

    public static void setAlerDialog(Context context, String title, String msg, boolean needNegativeBtn, String posBtn, final DialogInterface.OnClickListener listener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton(posBtn, listener);
        if (needNegativeBtn) {
            builder.setNegativeButton(android.R.string.no, null);
        }

        builder.show();

    }
}
