package com.turtillion.estoredelivery.adapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.turtillion.estoredelivery.MainActivity;
import com.turtillion.estoredelivery.NewOrder;
import com.turtillion.estoredelivery.databinding.NewOrdersBinding;
import com.turtillion.estoredelivery.utils.CommonUtils;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.turtillion.estoredelivery.R;
import com.turtillion.estoredelivery.NewOrderDetailsActivity;
import com.turtillion.estoredelivery.models.OrderDomain;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class NewOrdersAdapter extends RecyclerView.Adapter<NewOrdersAdapter.ViewHolder> {
    Context context;
    ArrayList<OrderDomain> orderList;



    public NewOrdersAdapter(Context context, ArrayList<OrderDomain> orderList) {
        this.context = context;
        this.orderList = orderList;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        NewOrdersBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.new_orders, parent, false);
        return new NewOrdersAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.binding.tvOrderiD.setText(orderList.get(position).getOrder_id());
        holder.binding.tvDate.setText(orderList.get(position).getOrder_date());
        holder.binding.tvTime.setText(orderList.get(position).getOrder_time());
        holder.binding.tvFrom.setText( orderList.get(position).getFrom());
        holder.binding.tvTo.setText( orderList.get(position).getTo());
        holder.binding.tvKm.setText( orderList.get(position).getKm() + " KM");

        holder.binding.btnAcceptOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    final JSONObject params = new JSONObject();
                    try {
                        params.put("dBoy_id", orderList.get(position).getdBoyId());
                        params.put("customer_order_id", orderList.get(position).getOrder_id());
                        Log.d("request-->", params.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://day2night.in/delivery/orderAccept.php",
                            response -> {

                                try {
                                    JSONObject response_obj = new JSONObject(response);
                                    Log.d("response-->", response_obj.toString());
                                    if (response_obj.getString("code").equals("1")) {
                                        if (response_obj.getString("order_accept").equals("success")) {
                                            Toast.makeText(context, "Order Accepted , ID : " + orderList.get(position).getOrder_id(), Toast.LENGTH_SHORT).show();

                                            Intent intent = new Intent(context, MainActivity.class);
                                            Gson gson = new Gson();
                                            intent.putExtra("orderData", gson.toJson(orderList.get(position)));
                                            context.startActivity(intent);
                                            ((Activity)context).finish();
                                        }
                                        if (response_obj.getString("order_accept").equals("failed")) {
                                            Toast.makeText(context, "Try Again !", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            CommonUtils.cancelProgressBar();

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
                    RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());
                    requestQueue.add(stringRequest);

            }
        });

    }

    @Override
    public int getItemCount() {
        if (orderList != null)
            return orderList.size();
        else
            return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        NewOrdersBinding binding;

        public ViewHolder(@NonNull NewOrdersBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
