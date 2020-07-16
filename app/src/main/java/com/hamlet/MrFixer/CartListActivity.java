package com.hamlet.MrFixer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import static com.hamlet.MrFixer.CartListAdapter.newQuantity;
import static com.hamlet.MrFixer.CartListAdapter.oldQuantity;

public class CartListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private TextView amountText;
    private Button checkoutButton;

    private CartListAdapter adapter;
    private ArrayList<CartItem> items;

    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;

    private String userId;
    private int amount = 0;
    private ArrayList<String> customerProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_list);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Cart List");

        recyclerView = findViewById(R.id.cartListRecyclerView);
        amountText = findViewById(R.id.cartListTotalAmountText);
        checkoutButton = findViewById(R.id.cartListCheckoutButton);

        databaseReference = FirebaseDatabase.getInstance().getReference("usersCartList");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        assert firebaseUser != null;
        userId = firebaseUser.getUid();

        customerProducts = new ArrayList<>();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        items = new ArrayList<>();
        adapter = new CartListAdapter(this, items);
        recyclerView.setAdapter(adapter);

        databaseReference.child(userId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                CartItem item = dataSnapshot.getValue(CartItem.class);
                items.add(item);
                customerProducts.add(item.getId() + " -> " + item.getName());
                if (item.getSalePrice().equals("00")) {
                    amount = amount + Integer.parseInt(item.getQuantity().toString()) * Integer.parseInt(item.getRegularPrice().toString());
                } else {
                    amount = amount + Integer.parseInt(item.getQuantity().toString()) * Integer.parseInt(item.getSalePrice().toString());
                }
                amountText.setText("Rs. " + String.valueOf(amount));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                CartItem item = dataSnapshot.getValue(CartItem.class);
                if (newQuantity > oldQuantity) {
                    int difference = newQuantity - oldQuantity;
                    if (item.getSalePrice().equals("00")) {
                        amount = amount + difference * Integer.parseInt(item.getRegularPrice().toString());
                    } else {
                        amount = amount + difference * Integer.parseInt(item.getSalePrice().toString());
                    }
                } else {
                    int difference = oldQuantity - newQuantity;
                    if (item.getSalePrice().equals("00")) {
                        amount = amount - difference * Integer.parseInt(item.getRegularPrice().toString());
                    } else {
                        amount = amount - difference * Integer.parseInt(item.getSalePrice().toString());
                    }
                }
                amountText.setText("Rs. " + String.valueOf(amount));
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                CartItem item = dataSnapshot.getValue(CartItem.class);
                for (int i = 0; i < items.size(); i++) {
                    if (items.get(i).getId().equals(item.getId())) {
                        if (item.getSalePrice().equals("00")) {
                            amount -= Integer.parseInt(item.getRegularPrice());
                        } else {
                            amount -= Integer.parseInt(item.getSalePrice());
                        }
                        amountText.setText("Rs. " + String.valueOf(amount));
                        items.remove(i);
                        adapter.notifyItemRemoved(i);
                        break;
                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartListActivity.this, Checkout.class);
                intent.putExtra("AMOUNT", amount);
                intent.putExtra("NUMBER", items.size());
                intent.putExtra("PRODUCTS", customerProducts.toString());
                startActivity(intent);
            }
        });
    }
}
