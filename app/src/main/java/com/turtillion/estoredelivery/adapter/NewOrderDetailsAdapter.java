package com.turtillion.estoredelivery.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.turtillion.estoredelivery.NewOrderDetailsActivity;
import com.turtillion.estoredelivery.R;
import com.turtillion.estoredelivery.databinding.NewOrderDetailsShopsBinding;
import com.turtillion.estoredelivery.databinding.NewOrdersBinding;
import com.turtillion.estoredelivery.models.NewOrderDetailsDomain;
import com.turtillion.estoredelivery.utils.CommonUtils;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class NewOrderDetailsAdapter extends RecyclerView.Adapter<NewOrderDetailsAdapter.ViewHolder> {
    Context context;
    ArrayList<NewOrderDetailsDomain> shopList;

    public NewOrderDetailsAdapter(Context context, ArrayList<NewOrderDetailsDomain> shopList) {
        this.context = context;
        this.shopList = shopList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        NewOrderDetailsShopsBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.new_order_details_shops, parent, false);
        return new NewOrderDetailsAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        int shopno = position + 1;
        holder.binding.tvShopNo.setText("Shop " + (shopno));
        holder.binding.tvOrderiD.setText(shopList.get(position).getOrder_id());
        holder.binding.tvName.setText(shopList.get(position).getShopName());
        holder.binding.tvLocation.setText(shopList.get(position).getShopLocation());
        holder.binding.tvphonenum.setText("+91 "+shopList.get(position).getPhone());
        if (shopList.get(position).getPickStatus().equals("Picked-Up")){
            holder.binding.btnPickup.setVisibility(View.GONE);
            holder.binding.tvPicked.setVisibility(View.VISIBLE);
        }
        holder.binding.shopCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+shopList.get(position).getPhone()));
                context.startActivity(callIntent);
            }
        });
        holder.binding.shopDirction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri.Builder builder = new Uri.Builder();
                builder.scheme("https")
                        .authority("www.google.com")
                        .appendPath("maps")
                        .appendPath("dir")
                        .appendPath("")
                        .appendQueryParameter("api", "1")
                        .appendQueryParameter("destination", shopList.get(position).getLandmark() + "," + shopList.get(position).getLongitude());
                String url = builder.build().toString();
                Log.d("Directions", url);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                context.startActivity(i);
            }
        });


        holder.binding.btnPickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final JSONObject params = new JSONObject();
                try {
                    params.put("dBoy_id", shopList.get(position).getdBoyId());
                    params.put("order_id", shopList.get(position).getOrder_id());
                    params.put("customer_order_id", shopList.get(position).getCustomer_Order_Id());
                    Log.d("request-->", params.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://day2night.in/delivery/pickup.php",
                        response -> {
                            try {
                                JSONObject response_obj = new JSONObject(response);
                                Log.d("response-->", response_obj.toString());
                                if (response_obj.getString("code").equals("1")) {
                                    if (response_obj.getString("shop_pickup").equals("success"))
                                    {
                                        holder.binding.btnPickup.setVisibility(View.GONE);
                                        holder.binding.tvPicked.setVisibility(View.VISIBLE);
                                        Toast.makeText(context.getApplicationContext(),"Pick up Succesfull",Toast.LENGTH_SHORT).show();
                                    }
                                    if (response_obj.getString("shop_pickup").equals("failed"))
                                    {

                                        holder.binding.btnPickup.setVisibility(View.VISIBLE);
                                        Toast.makeText(context.getApplicationContext(),"Try Again",Toast.LENGTH_SHORT).show();
                                    }
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
                RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());
                requestQueue.add(stringRequest);
            }
        });
    }
    @Override
    public int getItemCount() {
        if (shopList != null)
            return shopList.size();
        else
            return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        NewOrderDetailsShopsBinding binding;

        public ViewHolder(@NonNull NewOrderDetailsShopsBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}