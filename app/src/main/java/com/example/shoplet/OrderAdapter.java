package com.example.shoplet;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<Order> orderList;

    public OrderAdapter(List<Order> orderList) {
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false);
        return new OrderViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.orderDate.setText( order.getDate());
        holder.itemPrice.setText("Total: Rs." + order.getTotal());
        holder.paymentMethod.setText("Pay Method: " + order.getPaymentMethod());
        holder.orderStatus.setText("Status: " + order.getStatus());
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        public TextView orderDate, itemPrice, paymentMethod, orderStatus;

        public OrderViewHolder(View view) {
            super(view);
            orderDate = view.findViewById(R.id.orderDate);
            itemPrice = view.findViewById(R.id.tvitemPrice);
            paymentMethod = view.findViewById(R.id.tvMethod);
            orderStatus = view.findViewById(R.id.tvStatus);
        }
    }
}

