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

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<Product> productList;
    private Context context;
    private OnProductClickListener onProductClickListener;

    public interface OnProductClickListener {
        void onProductClick(int position);
    }

    public ProductAdapter(List<Product> productList, Context context, OnProductClickListener onProductClickListener) {
        this.productList = productList;
        this.context = context;
        this.onProductClickListener = onProductClickListener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView productName;
        private TextView productPrice;
        private TextView productRating;
        private ImageView productImage;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.product_name);
            productPrice = itemView.findViewById(R.id.product_price);
            productImage = itemView.findViewById(R.id.product_image);
            productRating = itemView.findViewById(R.id.product_rating);
            itemView.setOnClickListener(this);
        }

        public void bind(Product product) {
            productName.setText(product.getName());
            productPrice.setText(String.valueOf(product.getPrice()));
            productRating.setText(String.valueOf(product.getTotalrating()) + "/5.0");

            // Set image if you have image URL in your Product model
            // You can use libraries like Picasso or Glide for loading images
            if (product.getImage() != null) {
                Picasso.get()
                        .load(product.getImage())
                        .into(productImage);
            }
        }

        @Override
        public void onClick(View v) {
            if (onProductClickListener != null) {
                onProductClickListener.onProductClick(getAdapterPosition());
            }
        }
    }
}
