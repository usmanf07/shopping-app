package com.example.shoplet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CartItemAdapter extends ArrayAdapter<CartItem> {
    private ArrayList<CartItem> cartItemList;
    private CartInteractionListener listener;

    public CartItemAdapter(Context context, ArrayList<CartItem> itemList, CartInteractionListener listener) {
        super(context, 0, itemList);
        this.cartItemList = itemList;
        this.listener = listener;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        CartItem cartItem = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.cart_item, parent, false);
        }

        ImageView itemImageView = convertView.findViewById(R.id.itemImageView);
        TextView itemNameTextView = convertView.findViewById(R.id.itemNameTextView);
        TextView itemPriceTextView = convertView.findViewById(R.id.itemPriceTextView);
        TextView itemQuantityTextView = convertView.findViewById(R.id.itemQuantityTextView);
        Button plusButton = convertView.findViewById(R.id.button1);
        Button minusButton = convertView.findViewById(R.id.button2);

        if (cartItem.getProduct().getImage() != null) {
            Picasso.get()
                    .load(cartItem.getProduct().getImage())
                    .into(itemImageView);
        }
        itemNameTextView.setText(cartItem.getProduct().getName());
        itemPriceTextView.setText("Price: Rs. " + cartItem.getProduct().getPrice());
        itemQuantityTextView.setText("Quantity: " + cartItem.getQuantity());

        plusButton.setOnClickListener(v -> {
            cartItem.setQuantity(cartItem.getQuantity() + 1);
            itemQuantityTextView.setText("Quantity: " + cartItem.getQuantity());
            notifyDataSetChanged();
            listener.onCartUpdated();
        });

        minusButton.setOnClickListener(v -> {
            int quantity = cartItem.getQuantity();
            if (quantity > 1) {
                cartItem.setQuantity(quantity - 1);
                itemQuantityTextView.setText("Quantity: " + cartItem.getQuantity());
                notifyDataSetChanged();
                listener.onCartUpdated();
            } else {
                cartItemList.remove(position);
                notifyDataSetChanged();
                listener.onCartUpdated();
            }
        });

        return convertView;
    }
}
