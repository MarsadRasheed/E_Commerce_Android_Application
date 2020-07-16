package com.hamlet.MrFixer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<CartItem> cartItems;
    public static int oldQuantity;
    public static int newQuantity;

    public CartListAdapter(Context context, ArrayList<CartItem> items) {
        this.mContext = context;
        this.cartItems = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.carlist_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        final CartItem item = cartItems.get(position);
        holder.itemName.setText(item.getName());
        holder.quantutyText.setText(item.getQuantity());

        if (item.getSalePrice().equals("00")) {
            holder.salePrice.setText("");
            holder.regularPrice.setText(item.getRegularPrice());
        } else {
            holder.salePrice.setText("Rs. " + item.getRegularPrice());
            holder.regularPrice.setText("Rs. " + item.getSalePrice());
            holder.salePrice.setPaintFlags(holder.salePrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

        Picasso.get().load(item.getImageUrl()).placeholder(R.drawable.ic_image).fit().centerCrop().into(holder.itemImage);

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Task<Void> task = Utilis.removeCart(FirebaseAuth.getInstance().getCurrentUser().getUid().toString(), item.getId());
                task.addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Snackbar.make(((Activity) mContext).findViewById(android.R.id.content), "Item removed Successfully!", Snackbar.LENGTH_SHORT).setAction("UNDO", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Task<Void> task = FirebaseDatabase.getInstance().getReference("usersCartList")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString())
                                            .child(item.getId()).setValue(item);
                                    task.addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(mContext, "UNDO operation successful !", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(mContext, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            }).show();
                        } else {
                            Toast.makeText(mContext, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        holder.addCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = Integer.parseInt(item.getQuantity());
                oldQuantity = Integer.parseInt(item.getQuantity().toString());
                quantity = quantity + 1;
                Task<Void> task = FirebaseDatabase.getInstance().getReference("usersCartList")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString())
                        .child(item.getId()).child("quantity").setValue(String.valueOf(quantity));
                item.setQuantity(String.valueOf(quantity));
                newQuantity = Integer.parseInt(item.getQuantity().toString());
                holder.quantutyText.setText(item.getQuantity());
            }
        });

        holder.decreaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = Integer.parseInt(item.getQuantity());
                oldQuantity = Integer.parseInt(item.getQuantity().toString());
                if (quantity - 1 >= 1) {
                    quantity = quantity - 1;
                    Task<Void> task = FirebaseDatabase.getInstance().getReference("usersCartList")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString())
                            .child(item.getId()).child("quantity").setValue(String.valueOf(quantity));
                    item.setQuantity(String.valueOf(quantity));
                    newQuantity = Integer.parseInt(item.getQuantity().toString());
                    holder.quantutyText.setText(item.getQuantity());
                } else {
                    Toast.makeText(mContext, "Quantity can't be 0 or delte item from cart list", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView itemImage;
        private TextView itemName;
        private TextView regularPrice;
        private TextView salePrice;
        private TextView quantutyText;
        private ImageButton addCartButton;
        private ImageButton decreaseButton;
        private ImageButton deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.cartListImageView);
            itemName = itemView.findViewById(R.id.cartListNameText);
            addCartButton = itemView.findViewById(R.id.cartlistAddButton);
            decreaseButton = itemView.findViewById(R.id.cartListRemoveButton);
            regularPrice = itemView.findViewById(R.id.cartlistRegularTextView);
            salePrice = itemView.findViewById(R.id.cartlistSaleTextView);
            quantutyText = itemView.findViewById(R.id.cartListQuantityText);
            deleteButton = itemView.findViewById(R.id.cartListDeleteButton);
        }
    }
}
