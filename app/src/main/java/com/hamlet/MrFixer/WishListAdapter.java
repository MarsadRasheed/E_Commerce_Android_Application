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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class WishListAdapter extends RecyclerView.Adapter<WishListAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<ExampleItem> exampleItems;

    public WishListAdapter(Context context, ArrayList<ExampleItem> items) {
        this.mContext = context;
        this.exampleItems = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.wishlist_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("usersCartList");
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        assert firebaseUser != null;
        final String userID = firebaseUser.getUid();

        final ExampleItem item = exampleItems.get(position);

        String imageUrl = item.getImageUrl();
        String name = item.getName();
        final String regularPrice = item.getRegularPrice();
        String salePrice = item.getSalePrice();

        holder.itemName.setText(name);
        holder.regularPrice.setText("Rs ." + regularPrice);
        if (salePrice.equals("00")) {
            holder.salePrice.setText("");
        } else {
            holder.salePrice.setText("Rs. " + salePrice);
            holder.salePrice.setPaintFlags(holder.salePrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

        Picasso.get().load(imageUrl).placeholder(R.drawable.ic_image).fit().centerCrop().into(holder.itemImage);

        holder.addCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartItem cartItem = new CartItem(item, "1");
                Task<Void> task = databaseReference.child(userID).child(cartItem.getId()).setValue(cartItem);
                task.addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(mContext, "Item added to cart list", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mContext, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        holder.wishListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ExampleItem removed = exampleItems.get(position);
                Task<Void> task = Utilis.removeValue(userID, removed.getId());
                task.addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Snackbar.make(((Activity)mContext).findViewById(android.R.id.content),"Item removed from wishlist",Snackbar.LENGTH_SHORT).setAction("UNDO", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Task<Void> task = FirebaseDatabase.getInstance().getReference("usersWishList").child(userID).child(item.getId()).setValue(item);
                                    task.addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
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

    }

    @Override
    public int getItemCount() {
        return exampleItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView itemImage;
        private TextView itemName;
        private ImageButton addCartButton;
        private ImageButton wishListButton;
        private TextView regularPrice;
        private TextView salePrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.wishListImageView);
            itemName = itemView.findViewById(R.id.wishListNameText);
            addCartButton = itemView.findViewById(R.id.wishListAddToCartButton);
            wishListButton = itemView.findViewById(R.id.wishlistDelete);
            regularPrice = itemView.findViewById(R.id.wishlistRegularTextView);
            salePrice = itemView.findViewById(R.id.wishlistSaleTextView);
        }
    }
}
