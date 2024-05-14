package com.example.shoplet;

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

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private Context context;
    private List<CategoryItem> categoryItems;
    private OnCategoryClickListener onCategoryClickListener;

    public CategoryAdapter(Context context, List<CategoryItem> categoryItems, OnCategoryClickListener onCategoryClickListener) {
        this.context = context;
        this.categoryItems = categoryItems;
        this.onCategoryClickListener = onCategoryClickListener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.category_item_row, parent, false);
        return new CategoryViewHolder(view, onCategoryClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        CategoryItem categoryItem = categoryItems.get(position);
        holder.categoryName.setText(categoryItem.getName());
        Glide.with(context).load(categoryItem.getImage()).into(holder.categoryImage);
        holder.itemView.setTag(categoryItem); // Set the categoryItem as a tag to the itemView
    }

    @Override
    public int getItemCount() {
        return categoryItems.size();
    }

    public interface OnCategoryClickListener {
        void onCategoryClick(CategoryItem categoryItem);
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView categoryImage;
        TextView categoryName;
        OnCategoryClickListener onCategoryClickListener;

        public CategoryViewHolder(@NonNull View itemView, OnCategoryClickListener onCategoryClickListener) {
            super(itemView);
            categoryImage = itemView.findViewById(R.id.categoryImage);
            categoryName = itemView.findViewById(R.id.categoryName);
            this.onCategoryClickListener = onCategoryClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            CategoryItem categoryItem = (CategoryItem) v.getTag();
            if (categoryItem != null) {
                onCategoryClickListener.onCategoryClick(categoryItem);
            }
        }
    }
}
