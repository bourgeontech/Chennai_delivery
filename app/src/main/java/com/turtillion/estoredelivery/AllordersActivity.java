package com.turtillion.estoredelivery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.turtillion.estoredelivery.adapter.AllOrdersAdapter;
import com.turtillion.estoredelivery.adapter.NewOrdersAdapter;
import com.turtillion.estoredelivery.base.BaseActivity;
import com.turtillion.estoredelivery.databinding.ActivityAllOrdersBinding;
import com.turtillion.estoredelivery.interfaces.ClickedItem;
import com.turtillion.estoredelivery.models.OrderDomain;
import com.turtillion.estoredelivery.utils.CommonUtils;
import com.turtillion.estoredelivery.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;

public class AllordersActivity extends BaseActivity {
ActivityAllOrdersBinding binding;
    AllOrdersAdapter adapter;
    ArrayList<OrderDomain> orderList = new ArrayList<>();

    String dBoyId = "";
    final Calendar myCalendar = Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_all_orders);
        dBoyId = sharedPreferences.getString(Constants.DBOY_ID, "");
        mActivity = this;

        setupRecyclerview();

        binding.tvDate.setText(CommonUtils.getTodayDate2());

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {  finish(); }
        });

        binding.btnChangeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(mActivity, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (CommonUtils.checkConnectivity(this)) {
                 getAllOrders("");
        } else {
            Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            SimpleDateFormat df = new SimpleDateFormat("dd EEEE, MMMM yyyy", Locale.US);
            binding.tvDate.setText(df.format(myCalendar.getTime()));

            String myFormat = "yyyy-MM-dd";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            if (CommonUtils.checkConnectivity(AllordersActivity.this)) {
                getAllOrders(sdf.format(myCalendar.getTime()));
            } else {
                Toast.makeText(AllordersActivity.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
            }

        }

    };

    private void getAllOrders(final String t) {
        orderList.clear();
        CommonUtils.setProgressBar(mActivity);
        final JSONObject params = new JSONObject();
        try {
            params.put("dBoy_id", dBoyId);
           // params.put("date", "total");

            if (t.equals(""))
                params.put("order_date", CommonUtils.getTodayDate3());
            else
                params.put("order_date", t);

            Log.d("request-->", params.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://day2night.in/delivery/allOrderList.php",
                response -> {

                    CommonUtils.cancelProgressBar();
                    try {
                        JSONObject obj = new JSONObject(response);
                        Log.d("response-->", obj.toString());

                        if (obj.getString("code").equals("1")) {
                            if (obj.getString("isOrder").equals("yes")) {
                                JSONArray orders = obj.getJSONArray("cur_order");

                                for (int i = 0; i < orders.length(); i++) {
                                    JSONObject object = orders.getJSONObject(i);
                                    String sdBoyId = dBoyId;
                                    String order_id = object.getString("customer_order_id");
//                                    String from = object.getString("from");
//                                    String to = object.getString("to");
//                                    String km = object.getString("km");
                                            String from = "";
                                            String to = "";
                                            String km = "";
                                    String order_date = object.getString("delivered_date");
                                    String order_time = object.getString("delivered_time");

                                    orderList.add(new OrderDomain(sdBoyId,order_id, from, to, km, order_date, order_time));
                                }

                                    binding.emptyLayout.setVisibility(View.GONE);
                                    adapter.notifyDataSetChanged();
                                    binding.recylerViewAllOrders.scheduleLayoutAnimation();
                            }else{
                                binding.emptyLayout.setVisibility(View.VISIBLE);
                                binding.tvEmpty.setText("No orders");
                            }
                        } else {
                            Toast.makeText(mActivity, obj.getString("status"), Toast.LENGTH_SHORT).show();
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

    private void setupRecyclerview() {
        binding.recylerViewAllOrders.setHasFixedSize(true);
        binding.recylerViewAllOrders.setLayoutManager(new LinearLayoutManager(mActivity));
        adapter = new AllOrdersAdapter(mActivity, orderList);
        binding.recylerViewAllOrders.setAdapter(adapter);
    }

}