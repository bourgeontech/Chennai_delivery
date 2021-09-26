package com.turtillion.estoredelivery;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.turtillion.estoredelivery.base.BaseActivity;
import com.turtillion.estoredelivery.databinding.ActivityMainBinding;
import com.turtillion.estoredelivery.interfaces.ClickedItem;
import com.turtillion.estoredelivery.models.OrderDomain;
import com.turtillion.estoredelivery.utils.CommonUtils;
import com.turtillion.estoredelivery.utils.Constants;
import com.turtillion.estoredelivery.volleyservice.VolleyInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends BaseActivity implements LocationListener, View.OnClickListener, ClickedItem, VolleyInterface {
ActivityMainBinding binding;

    public double live_lat1 ;
    public double live_long1 ;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    Boolean isCurrent = false;
    OrderDomain orderDomain;
   // ArrayList<OrderDomain> orderList = new ArrayList<>();

    String dBoyId = "";
int position = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);

        dBoyId = sharedPreferences.getString(Constants.DBOY_ID, "0");
        mActivity = this;

        binding.newOrderLayout.setOnClickListener(this);
        binding.allOrderLayout.setOnClickListener(this);
        binding.currentOrderLayout.setOnClickListener(this);
        binding.btnNotifications.setOnClickListener(this);
        binding.btnPickLocation.setOnClickListener(this);
        binding.btnLogout.setOnClickListener(this);

        check_cur_order();

      //  binding.liveLocation1.setText("Lat: " + live_lat + "Long: " + live_long);
        FirebaseMessaging.getInstance().subscribeToTopic("d" + dBoyId).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("topic-->", "Subscribed by with topic : d"+dBoyId);
            }
        });

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 300000, 500, this);
        // this is tyhe file iam editing
        binding.newOrderLayout.setEnabled(false);
        CommonUtils.setProgressBar(this);
    }

    private void check_cur_order() {
       // orderList.clear();
      //  CommonUtils.setProgressBar(this);
        final JSONObject params = new JSONObject();
        try {
            params.put("dBoy_id",dBoyId);
            Log.d("request-->", params.toString());
        } catch (JSONException e) {   e.printStackTrace();  }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://day2night.in/delivery/checkCurrentOrder.php",
                response -> {
                  //  CommonUtils.cancelProgressBar();
                    try {
                        JSONObject obj = new JSONObject(response);
                        Log.d("response-->", obj.toString());

                        if (obj.getString("code").equals("1")) {
                            if (obj.getString("isCurrent").equals("yes")) {
                                isCurrent = true;
                                binding.currentIndicator.setVisibility(View.VISIBLE);

                                JSONObject c_orders = obj.getJSONObject("cur_order");

                                String sdBoyId = dBoyId;
                                String order_id = c_orders.getString("customer_order_id");
                                String from = "";
                                String to = c_orders.getString("to");
                                String km = "";
                                String order_date = c_orders.getString("order_date_time");
                                String order_time = c_orders.getString("order_date_time");

                            orderDomain = new OrderDomain(sdBoyId,order_id, from, to, km, order_date, order_time);
                              //  orderList.add(new OrderDomain(sdBoyId,order_id, from, to, km, order_date, order_time));
                            }else {
                                isCurrent = false;
                                binding.currentIndicator.setVisibility(View.GONE);
                            }

                        } else {
                            Toast.makeText(mActivity, obj.getString("status"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {  }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }
            @Override
            public byte[] getBody() throws AuthFailureError {
                return params.toString().getBytes();
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    public void onLocationChanged(@NonNull Location location) {
        Log.d("Live Location -->", "Latitude : "+location.getLatitude() +" Longitude : " + location.getLongitude());
        CommonUtils.cancelProgressBar();
        binding.newOrderLayout.setEnabled(true);
        live_lat1 = location.getLatitude();
        live_long1 = location.getLongitude();
      //  binding.liveLocation1.setText("Latitude:" + location.getLatitude() + ", Longitude:" + location.getLongitude());
        updateLiveLocation();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.newOrderLayout:
                if (isCurrent == true){
                    Toast.makeText(mActivity, "You are already in a Delivery", Toast.LENGTH_SHORT).show();
                }else {
                    if(live_lat1 != 0){
                        updateLiveLocation();
                        Intent intent = new Intent(MainActivity.this,NewOrder.class);
                        startActivity(intent);
                      //  finish();
                    }else{  Toast.makeText(mActivity, "try Again !", Toast.LENGTH_SHORT).show();  }
                }
                break;
            case R.id.allOrderLayout:
                startActivity(new Intent(mActivity, AllordersActivity.class));
               // finish();
                break;
           // case R.id.btnNotifications:
             //   startActivity(new Intent(mActivity, NotificationActivity.class));
           //     break;
            case R.id.currentOrderLayout:

                if (isCurrent == false){
                    Toast.makeText(mActivity, "No Current Oredres. Take One order from New Orders", Toast.LENGTH_SHORT).show();
                }else {
                    //Toast.makeText(mActivity, "iscurrent is true", Toast.LENGTH_SHORT).show();
                    Intent intentc = new Intent(MainActivity.this,NewOrderDetailsActivity.class);
                    Gson gson = new Gson();
                    intentc.putExtra("orderData", gson.toJson(orderDomain));
                   // intent.putExtra("orderData", gson.toJson(orderList.get(position)));

                    startActivity(intentc);
                  //  finish();
                }
                break;
            case R.id.btnLogout:
                logoutFunction();
                break;

        }
    }

    private void updateLiveLocation() {
         editor.putString(Constants.LIVE_LATITUDE,Double.toString(live_lat1));
         editor.putString(Constants.LIVE_LONGITUDE,Double.toString(live_long1));
         editor.apply();
        final JSONObject params = new JSONObject();
        try {
            params.put("dBoy_id",dBoyId);
            params.put("live_lat", live_lat1);
            params.put("live_long", live_long1);
            Log.d("request-->", params.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://day2night.in/delivery/mainActivityLiveLocation.php",
                response -> {
                    try {
                        JSONObject response_obj = new JSONObject(response);
                        Log.d("response-->2", response_obj.toString());
                        if (response_obj.getString("code").equals("1")) {
                            Toast.makeText(MainActivity.this, "Successfully Updated Location", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {  }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }
            @Override
            public byte[] getBody() throws AuthFailureError {
                return params.toString().getBytes();
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    @Override
    public void clicked(String type, Object object) {

    }

    @Override
    public void SuccessResponse(JSONObject response, int requestcode) {

    }

    @Override
    public void ErrorResponse(String msg, int requestcode) {

    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude","disable");
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        dialog.setMessage("GPS/Location not Enabled");
        dialog.setPositiveButton("Turn On", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //this will navigate user to the device location settings screen
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });
        dialog.setNegativeButton("Exit App", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishAffinity();
                System.exit(0);
            }
        });
        dialog.setCancelable(false);
        AlertDialog alert = dialog.create();
        alert.show();
    }
    private void logoutFunction() {
        CommonUtils.setProgressBar(mActivity);
        editor.clear();
        editor.apply();
        FirebaseMessaging.getInstance().unsubscribeFromTopic("d" + dBoyId).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                CommonUtils.cancelProgressBar();
                Log.d("topic-->", "UnSubscribed by with topic : d"+dBoyId);
                Intent intent = new Intent(mActivity, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                finish();
                startActivity(intent);
            }
        });

    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude","enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude","status");
    }
}