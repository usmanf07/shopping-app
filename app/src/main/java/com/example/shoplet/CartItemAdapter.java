package com.example.shoplet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

public class CartItemAdapter extends ArrayAdapter<CartItem> {

    private ArrayList<CartItem> cartItemList;

    public CartItemAdapter(Context context, ArrayList<CartItem> itemList) {
        super(context, 0, itemList);
        this.cartItemList = itemList;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final CartItem cartItem = getItem(position);


        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.cart_item, parent, false);
        }


        ImageView itemImageView = convertView.findViewById(R.id.itemImageView);
        TextView itemNameTextView = convertView.findViewById(R.id.itemNameTextView);
        final TextView itemPriceTextView = convertView.findViewById(R.id.itemPriceTextView);
        final TextView itemQuantityTextView = convertView.findViewById(R.id.itemQuantityTextView);
        Button plusButton = convertView.findViewById(R.id.button1);
        Button minusButton = convertView.findViewById(R.id.button2);


        itemImageView.setImageResource(cartItem.getImageResource());
        itemNameTextView.setText(cartItem.getName());
        itemPriceTextView.setText("Price: $" + cartItem.getPrice());
        itemQuantityTextView.setText("Quantity: " + cartItem.getQuantity());


        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = cartItem.getQuantity();
                cartItem.setQuantity(quantity + 1);
                itemQuantityTextView.setText("Quantity: " + cartItem.getQuantity());
                notifyDataSetChanged();
            }
        });

        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = cartItem.getQuantity();
                if (quantity > 0) {
                    cartItem.setQuantity(quantity - 1);
                    itemQuantityTextView.setText("Quantity: " + cartItem.getQuantity());
                    notifyDataSetChanged();
                }
                if (cartItem.getQuantity() == 0) {
                    cartItemList.remove(position);
                    notifyDataSetChanged();
                }
            }
        });


        return convertView;
    }
}
