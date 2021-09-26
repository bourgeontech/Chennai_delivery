package com.turtillion.estoredelivery;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.turtillion.estoredelivery.adapter.NewOrdersAdapter;
import com.turtillion.estoredelivery.base.BaseActivity;
import com.turtillion.estoredelivery.databinding.ActivityNewOrderBinding;
import com.turtillion.estoredelivery.models.OrderDomain;
import com.turtillion.estoredelivery.utils.CommonUtils;
import com.turtillion.estoredelivery.utils.Constants;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.PermissionRequest;

import static com.turtillion.estoredelivery.utils.Constants.PERMISSIONS;

public class NewOrder extends BaseActivity {
    NewOrdersAdapter adapter;
    ActivityNewOrderBinding binding;
    ArrayList<OrderDomain> orderList = new ArrayList<>();

    private double currentLatitude;
    private double currentLongitude;

    LocationManager locationManager;
    String postalCode = "0";

    private String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    String dBoyId = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_new_order);

        dBoyId = sharedPreferences.getString(Constants.DBOY_ID, "");
        live_Latitude = sharedPreferences.getString(Constants.LIVE_LATITUDE, "");
        live_Longitude = sharedPreferences.getString(Constants.LIVE_LONGITUDE, "");
        mActivity = this;

        setupLiveLocation();

        setupRecyclerview();
    }



    @Override
    protected void onResume() {
        super.onResume();
        if (CommonUtils.checkConnectivity(this)) {
       //     getAllOrders();
        } else {
            Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupLiveLocation() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(NewOrder.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS);
        } else {
            final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                buildAlertMessageNoGps();
            } else {
                getCurrentLocation();
            }
        }
    }
    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("You need to turn on Location first")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
    private void requestPerms() {

        EasyPermissions.requestPermissions(
                new PermissionRequest.Builder(this, PERMISSIONS, perms)
                        .setRationale("Need Location Permissions")
                        .build());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void getAllOrders() {
        orderList.clear();
        CommonUtils.setProgressBar(mActivity);
        final JSONObject params = new JSONObject();
        try {
            params.put("dBoy_id", dBoyId);
            params.put("live_lat", live_Latitude);
            params.put("live_long", live_Longitude);
            Log.d("request-->", params.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://day2night.in/delivery/newOrderList.php",
                response -> {

                    CommonUtils.cancelProgressBar();
                    try {
                        JSONObject obj = new JSONObject(response);
                        Log.d("response-->", obj.toString());

                        if (obj.getString("code").equals("1")) {

                            JSONArray orders = obj.getJSONArray("orders");

                            for (int i = 0; i < orders.length(); i++) {
                                JSONObject object = orders.getJSONObject(i);
                                String sdBoyId = dBoyId;
                                String order_id = object.getString("customer_order_id");
                                String from = object.getString("from");
                                String to = object.getString("to");
                                String km = object.getString("km");
                                String order_date = object.getString("order_date_time");
                                String order_time = object.getString("order_date_time");

                                orderList.add(new OrderDomain(sdBoyId,order_id, from, to, km, order_date, order_time));
                            }

                            if (orderList.size() == 0) {
                                binding.emptyLayout.setVisibility(View.VISIBLE);
                                binding.tvEmpty.setText("No orders");
                            } else {
                                binding.emptyLayout.setVisibility(View.GONE);
                                adapter.notifyDataSetChanged();
                                binding.recylerViewNewOrders.scheduleLayoutAnimation();
                            }
                        } else {
                            Toast.makeText(mActivity, "connected", Toast.LENGTH_SHORT).show();
                            binding.emptyLayout.setVisibility(View.VISIBLE);
                            binding.tvEmpty.setText(obj.getString("status"));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CommonUtils.cancelProgressBar();
                binding.emptyLayout.setVisibility(View.VISIBLE);
                binding.tvEmpty.setText("No New Orders");
            }
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
    private void getCurrentLocation() {

       // CommonUtils.setProgressBar(mActivity);
        final LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.getFusedLocationProviderClient(mActivity).
                requestLocationUpdates(locationRequest, new LocationCallback() {

                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(mActivity).
                                removeLocationUpdates(this);

                        if (locationResult != null && locationResult.getLocations().size() > 0) {
                            int lastLocationIndex = locationResult.getLocations().size() - 1;
                            currentLatitude = locationResult.getLocations().get(lastLocationIndex).getLatitude();
                            currentLongitude = locationResult.getLocations().get(lastLocationIndex).getLongitude();

                            Geocoder geocoder;
                            List<Address> addresses;
                            geocoder = new Geocoder(mActivity, Locale.getDefault());

                            try {
                                addresses = geocoder.getFromLocation(currentLatitude, currentLongitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

                                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                                String city = addresses.get(0).getLocality();
                                String state = addresses.get(0).getAdminArea();
                                String country = addresses.get(0).getCountryName();
                                postalCode = addresses.get(0).getPostalCode();
//                                String knownName = addresses.get(0).getFeatureName();

                                Log.d("location-->", address + " " + city + " " + state + " " + country + " " + postalCode);



                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }

                       // CommonUtils.cancelProgressBar();
                    }
                }, Looper.getMainLooper());
    }

    private void setupRecyclerview() {
        if (CommonUtils.checkConnectivity(mActivity)) {
            getAllOrders();
        } else {
            Toast.makeText(mActivity, "Please check your internet connection", Toast.LENGTH_SHORT).show();
        }

        binding.recylerViewNewOrders.setHasFixedSize(true);
       binding.recylerViewNewOrders.setLayoutManager(new LinearLayoutManager(mActivity));
        adapter = new NewOrdersAdapter(mActivity, orderList);
        binding.recylerViewNewOrders.setAdapter(adapter);
    }
}