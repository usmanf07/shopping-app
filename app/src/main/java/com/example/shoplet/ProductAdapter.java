package com.example.shoplet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productList;
    private Context context;
    private OnProductClickListener onProductClickListener;

    public ProductAdapter(List<Product> productList, Context context, OnProductClickListener onProductClickListener) {
        this.productList = productList;
        this.context = context;
        this.onProductClickListener = onProductClickListener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view, onProductClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.productName.setText(product.getName());
        holder.productRating.setText(String.valueOf(product.getTotalrating()));

        if (product.isOnsale()) {
            int discountedPrice = (int) (product.getPrice() - (product.getPrice() * product.getDiscount() / 100.0));

            holder.productPrice.setText("Rs " + product.getPrice());
            holder.productPrice.setTextColor(context.getResources().getColor(android.R.color.darker_gray));
            holder.productPrice.setPaintFlags(holder.productPrice.getPaintFlags() | android.graphics.Paint.STRIKE_THRU_TEXT_FLAG);

            holder.productDiscountedPrice.setText("Rs " + discountedPrice);
            holder.productDiscountedPrice.setVisibility(View.VISIBLE);
        } else {
            holder.productPrice.setText("Rs " + product.getPrice());
            holder.productPrice.setTextColor(context.getResources().getColor(android.R.color.black));
            holder.productPrice.setPaintFlags(holder.productPrice.getPaintFlags() & (~android.graphics.Paint.STRIKE_THRU_TEXT_FLAG));

            holder.productDiscountedPrice.setVisibility(View.GONE);
        }

        Glide.with(context)
                .load(product.getImage())
                .into(holder.productImage);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public interface OnProductClickListener {
        void onProductClick(int position);
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView productImage;
        TextView productName;
        TextView productRating;
        TextView productPrice;
        TextView productDiscountedPrice;
        OnProductClickListener onProductClickListener;

        public ProductViewHolder(@NonNull View itemView, OnProductClickListener onProductClickListener) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_image);
            productName = itemView.findViewById(R.id.product_name);
            productRating = itemView.findViewById(R.id.product_rating);
            productPrice = itemView.findViewById(R.id.product_price);
            productDiscountedPrice = itemView.findViewById(R.id.product_discounted_price);
            this.onProductClickListener = onProductClickListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onProductClickListener.onProductClick(getAdapterPosition());
        }
    }
}
