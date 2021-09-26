package com.turtillion.estoredelivery;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.turtillion.estoredelivery.adapter.NewOrderDetailsAdapter;
import com.turtillion.estoredelivery.base.BaseActivity;
import com.turtillion.estoredelivery.databinding.ActivityNewOrderBinding;
import com.turtillion.estoredelivery.databinding.ActivityNewOrderDetailsBinding;
import com.turtillion.estoredelivery.models.NewOrderDetailsDomain;
import com.turtillion.estoredelivery.models.OrderDomain;
import com.turtillion.estoredelivery.utils.CommonUtils;
import com.turtillion.estoredelivery.utils.Constants;
import com.turtillion.estoredelivery.volleyservice.NotiInterface;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NewOrderDetailsActivity extends BaseActivity implements View.OnClickListener, NotiInterface {
    ActivityNewOrderDetailsBinding binding;
    NewOrderDetailsAdapter adapter;
    ArrayList<NewOrderDetailsDomain> shopList = new ArrayList<>();

    String dBoyId = "";
    public String customer_name,c_phone,c_latitude,c_longitude,total_amount = "0";


    OrderDomain orderDomain;
    String type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_new_order_details);
        dBoyId = sharedPreferences.getString(Constants.DBOY_ID, "");

        Gson gson = new Gson();
        String strObj = getIntent().getStringExtra("orderData");
        orderDomain = gson.fromJson(strObj, OrderDomain.class);

        mActivity = this;

        liseners();
        setInitialValues();
        setupRecyclerView();
        getOrderDetails();
    }

    private void liseners() {
        binding.back.setOnClickListener(this);
        binding.btndeliver.setOnClickListener(this);
        binding.custDirction.setOnClickListener(this);
        binding.custCall.setOnClickListener(this);
    }

    private void setInitialValues() {
        binding.tvOrderId.setText("Customer Order ID : " + orderDomain.getOrder_id());
        binding.tvName.setText("");
        binding.tvTime.setText(orderDomain.getOrder_time());
        binding.tvLocation.setText("Customer Locatoion : " + orderDomain.getTo());
        binding.tvPhone.setText("");
        binding.tvcash.setText("");
    }

    private void setupRecyclerView() {
        binding.recylerViewDeliverShops.setHasFixedSize(true);
        binding.recylerViewDeliverShops.setLayoutManager(new LinearLayoutManager(mActivity));
        adapter = new NewOrderDetailsAdapter(mActivity, shopList);
        binding.recylerViewDeliverShops.setAdapter(adapter);
    }

    private void getOrderDetails() {

        CommonUtils.setProgressBar(mActivity);

        final JSONObject params = new JSONObject();
        try {
            params.put("customer_order_id", orderDomain.getOrder_id());
//            Toast.makeText(mActivity, "id:"+orderDomain.getOrder_id(),  Toast.LENGTH_SHORT).show();
            Log.d("request-->", params.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://day2night.in/delivery/getOrderDetails.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        CommonUtils.cancelProgressBar();
                        try {
                            JSONObject obj = new JSONObject(response);
                            Log.d("response-->1", obj.toString());
                            if (obj.getString("code").equals("1")) {


                                JSONObject basic = obj.getJSONObject("basic");
                                customer_name = basic.getString("customer_name");
                                 c_phone = basic.getString("phone");
                                 c_latitude = basic.getString("latitude");
                                 c_longitude = basic.getString("longitude");
                                String payment_method = basic.getString("payment_method");
                                if (payment_method.equals("cash_on_del")){
                                    String total_amt = basic.getString("total_amt");
                                        total_amount = total_amt;
                                    binding.tvcash.setText("Collect Cash");
                                    binding.tvcash.setTextColor(Color.parseColor("#8C1010"));
                                    binding.tvAmt.setText("₹ "+total_amt);
                                    binding.cashlayout.setVisibility(View.VISIBLE);
                                }else{
                                    binding.cashlayout.setVisibility(View.GONE);
                                    binding.tvcash.setText("Paid");
                                    binding.tvcash.setTextColor(Color.parseColor("#3a873a"));
                                }
                                binding.tvName.setText(customer_name);
                                binding.tvPhone.setText("+91 "+c_phone);





                                JSONArray shops = obj.getJSONArray("shops");
                                for (int i = 0; i < shops.length(); i++) {

                                    JSONObject jsonObject = shops.getJSONObject(i);

                                    String sdBoyId = dBoyId;
                                    String customer_order_id = orderDomain.getOrder_id();

                                    String order_id = jsonObject.getString("order_id");
                                    String shopName = jsonObject.getString("shop_name");
                                    String shopLocation = jsonObject.getString("address");
                                    String shop_id = jsonObject.getString("shop_id");
                                    String phone = jsonObject.getString("phone");
                                    String landmark = jsonObject.getString("landmark");
                                    String latitude = jsonObject.getString("latitude");
                                    String longitude = jsonObject.getString("longitude");
                                    String pickStatus = jsonObject.getString("order_status");


                                    shopList.add(new NewOrderDetailsDomain(sdBoyId, customer_order_id, order_id, shopName, shopLocation, shop_id, phone, landmark, latitude, longitude, pickStatus));
                                }
                                adapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(mActivity, obj.getString("status"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {  e.printStackTrace();  }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CommonUtils.cancelProgressBar();
                Toast.makeText(mActivity, error.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void deliver() {

        CommonUtils.setProgressBar(mActivity);

        final JSONObject params = new JSONObject();
        try {
            params.put("dBoy_id", dBoyId);
            params.put("cust_order_id", orderDomain.getOrder_id());
            // if possible, send shop order ids
            Log.d("request-->2", params.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://day2night.in/delivery/deliverSuccess.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        CommonUtils.cancelProgressBar();
                        try {
                            JSONObject obj = new JSONObject(response);
                            Log.d("response-->2", obj.toString());
                            if (obj.getString("code").equals("1")) {

                               if(obj.getString("deliver").equals("success"))
                               {
                                   JSONArray shops = obj.getJSONArray("shops");
                                   Toast.makeText(mActivity,"Delivery Successful",Toast.LENGTH_SHORT).show();
                                   startActivity(new Intent(NewOrderDetailsActivity.this, MainActivity.class));
                                   finish();
                                   for(int i=0; i<shops.length(); i++){
                                       JSONObject object = shops.getJSONObject(i);
                                       callNotificationAPI(object.getString("shop_id"),object.getString("order_id"));
                                   }


                               }
                               else if(obj.getString("deliver").equals("failed"))
                               {
                                   Toast.makeText(mActivity, "Delivery Failed, Try again", Toast.LENGTH_SHORT).show();
                               }


                            } else {
                                Toast.makeText(mActivity, obj.getString("status"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CommonUtils.cancelProgressBar();
                Toast.makeText(mActivity, error.getMessage(), Toast.LENGTH_SHORT).show();
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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.btndeliver:
                if (!total_amount.equals("0")){
                    AlertDialog.Builder dialog = new AlertDialog.Builder(NewOrderDetailsActivity.this);
                    dialog.setMessage("Did you Collected cash "+total_amount);
                    dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (CommonUtils.checkConnectivity(NewOrderDetailsActivity.this)) {
                                deliver();
                            }
                        }
                    });
                    dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    AlertDialog alert = dialog.create();
                    alert.show();
                }

                break;
            case R.id.custDirction:
                Uri.Builder builder = new Uri.Builder();
                builder.scheme("https")
                        .authority("www.google.com")
                        .appendPath("maps")
                        .appendPath("dir")
                        .appendPath("")
                        .appendQueryParameter("api", "1")
                        .appendQueryParameter("destination", c_latitude + "," + c_longitude);
                String url = builder.build().toString();
                Log.d("Directions", url);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                break;
            case R.id.custCall:
                String number=c_phone;
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:+91 "+number));
                startActivity(callIntent);
                break;
        }
    }


    private void callNotificationAPI(String shopid ,String orderid) {

        String cTopic = "";
        try {
            HashMap<String, Object> params = new HashMap<>();

            JSONObject bodyObject = new JSONObject();

                bodyObject.put("title", "Order Delivered");
                bodyObject.put("body", "Order ID "+orderid+" has been Successfully Deliverd to "+customer_name);

            bodyObject.put("click_action", "OPEN_ACTIVITY");
            bodyObject.put("sound", "default");

            JSONObject dataObject = new JSONObject();
            dataObject.put("type", "common");

            // notification destination id
                cTopic = "s" + shopid;


            params.put("to", "/topics/" + cTopic);
            params.put("priority", "high");
            params.put("type", "common");
            params.put("notification", bodyObject);
            params.put("data", dataObject);

            JSONObject androidObject = new JSONObject();
            JSONObject androidNotObject = new JSONObject();
            androidNotObject.put("sound", "default");
            androidObject.put("notification", androidNotObject);
            params.put("android", androidObject);

            volleyUtils.sendNotification(mActivity, "common", params, this, 1);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void NotificationSuccess(JSONObject response, int requestcode) {

    }

    @Override
    public void NotificationError(String msg, int requestcode) {

    }
}