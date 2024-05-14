package com.example.shoplet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import android.graphics.Paint;

import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class FlashSaleAdapter extends RecyclerView.Adapter<FlashSaleAdapter.FlashSaleViewHolder> {
    private Context context;
    private List<FlashSaleItem> flashSaleItems;

    public FlashSaleAdapter(Context context, List<FlashSaleItem> flashSaleItems) {
        this.context = context;
        this.flashSaleItems = flashSaleItems;
    }

    @NonNull
    @Override
    public FlashSaleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_flash_sale, parent, false);
        return new FlashSaleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FlashSaleViewHolder holder, int position) {
        FlashSaleItem flashSaleItem = flashSaleItems.get(position);
        holder.bind(flashSaleItem);
    }

    @Override
    public int getItemCount() {
        return flashSaleItems.size();
    }

    public class FlashSaleViewHolder extends RecyclerView.ViewHolder {
        private ImageView productImage;
        private TextView productName;
        private TextView productRating;
        private TextView productPrice;
        private TextView productPriceOld;

        public FlashSaleViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_image);
            productName = itemView.findViewById(R.id.product_name);
            productRating = itemView.findViewById(R.id.product_rating);
            productPrice = itemView.findViewById(R.id.product_price);
            productPriceOld = itemView.findViewById(R.id.product_price_old);
            productPriceOld.setPaintFlags(productPriceOld.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

        public void bind(FlashSaleItem flashSaleItem) {
            productImage.setImageResource(flashSaleItem.getImageResource());
            productName.setText(flashSaleItem.getName());
            productRating.setText(flashSaleItem.getRating());
            productPrice.setText(flashSaleItem.getPrice());
        }
    }
}
