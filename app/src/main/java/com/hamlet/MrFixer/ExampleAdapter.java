package com.hamlet.MrFixer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.viewHolder> {

    private Context mContext;
    private ArrayList<ExampleItem> exampleItems;

    public ExampleAdapter(Context context,ArrayList<ExampleItem> items){
        this.mContext = context;
        this.exampleItems = items;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.example_item,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        final ExampleItem exampleItem = exampleItems.get(position);

        final String regularPrice = exampleItem.getRegularPrice();
        final String name = exampleItem.getName();
        final String salePrice = exampleItem.getSalePrice();
        final String link = exampleItem.getImageUrl();

        holder.nameText.setText(name);
        Picasso.get().load(link).placeholder(R.drawable.ic_image).fit().centerCrop().into(holder.imageView);

        if ( Integer.parseInt(salePrice) != 00) {
            holder.regularPrice.setText("Rs. " + salePrice);
            holder.regularPrice.setTextColor(Color.RED);
            holder.salePrice.setText("Rs. " + regularPrice);
            holder.salePrice.setPaintFlags(holder.salePrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.regularPrice.setText("Rs. " + regularPrice);
            holder.salePrice.setText("");
        }

        holder.itemCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,HomeDetailActivity.class);
                intent.putExtra("EXTRA_ITEM",exampleItem);
                intent.putExtra("EXTRA_NAME",name);
                intent.putExtra("EXTRA_REGULAR_PRICE",regularPrice);
                intent.putExtra("EXTRA_SALE_PRICE",salePrice);
                intent.putExtra("EXTRA_URL",link);

                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return exampleItems.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        private ImageView imageView;
        private TextView nameText;
        private TextView regularPrice;
        private TextView salePrice;
        private CardView itemCard;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.exampleImageView);
            nameText = itemView.findViewById(R.id.exampleName);
            regularPrice = itemView.findViewById(R.id.exampleRegularPrice);
            salePrice = itemView.findViewById(R.id.exampleSalePrice);
            itemCard = itemView.findViewById(R.id.itemCardView);
        }
    }


}
