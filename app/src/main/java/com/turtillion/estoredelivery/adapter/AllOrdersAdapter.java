package com.turtillion.estoredelivery.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.turtillion.estoredelivery.R;
import com.turtillion.estoredelivery.databinding.AllOrdersBinding;
import com.turtillion.estoredelivery.models.OrderDomain;
import java.util.ArrayList;

public class AllOrdersAdapter extends RecyclerView.Adapter<AllOrdersAdapter.ViewHolder> {
    Context context;
    ArrayList<OrderDomain> orderList;

    public AllOrdersAdapter(Context context, ArrayList<OrderDomain> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AllOrdersBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.all_orders, parent, false);
        return new AllOrdersAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.binding.tvOrderiD.setText(orderList.get(position).getOrder_id());
        holder.binding.tvDate.setText(orderList.get(position).getOrder_date());
        holder.binding.tvTime.setText(orderList.get(position).getOrder_time());
        holder.binding.tvFrom.setText( orderList.get(position).getFrom());
        holder.binding.tvTo.setText( orderList.get(position).getTo());
        holder.binding.tvKm.setText( orderList.get(position).getKm() + " KM");

    }
    @Override
    public int getItemCount() {
        if (orderList != null)
            return orderList.size();
        else
            return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        AllOrdersBinding binding;

        public ViewHolder(@NonNull AllOrdersBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
