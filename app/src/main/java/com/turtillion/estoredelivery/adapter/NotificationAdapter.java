//package com.turtillion.estoredelivery.adapter;
//
//import android.content.Context;
//import android.content.Intent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.annotation.NonNull;
//import androidx.databinding.DataBindingUtil;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.google.gson.Gson;
//import com.online.estoreshop.R;
//import com.online.estoreshop.activity.OrderDetailsActivity;
//import com.online.estoreshop.databinding.ItemNotificationsBinding;
//import com.online.estoreshop.models.OrderDomain;
//
//import java.util.ArrayList;
//
//public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
//
//    Context context;
//    ArrayList<OrderDomain> notificationList;
//
//    public NotificationAdapter(Context context, ArrayList<OrderDomain> notificationList) {
//        this.context = context;
//        this.notificationList = notificationList;
//    }
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        ItemNotificationsBinding binding = DataBindingUtil.inflate(
//                LayoutInflater.from(parent.getContext()),
//                R.layout.item_notifications, parent, false);
//
//        return new com.online.estoreshop.adapter.NotificationAdapter.ViewHolder(binding);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
//        holder.binding.tvContent.setText(notificationList.get(position).getCustomer_name() + ", " +
//                notificationList.get(position).getAddress() + " ordered some items.");
//        holder.binding.tvDate.setText(notificationList.get(position).getOrder_date() + ", " + notificationList.get(position).getOrder_time());
//
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, OrderDetailsActivity.class);
//                Gson gson = new Gson();
//                intent.putExtra("orderData", gson.toJson(notificationList.get(position)));
//                intent.putExtra("type", "Undelivered");
//                context.startActivity(intent);
//            }
//        });
//
//    }
//
//    @Override
//    public int getItemCount() {
//        if (notificationList != null)
//            return notificationList.size();
//        else
//            return 0;
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//        ;
//
//        ItemNotificationsBinding binding;
//
//        public ViewHolder(@NonNull ItemNotificationsBinding itemView) {
//            super(itemView.getRoot());
//            binding = itemView;
//        }
//    }
//}
