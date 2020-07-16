package com.hamlet.MrFixer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class Checkout extends AppCompatActivity {

    private EditText nameText;
    private EditText emailText;
    private EditText phoneText;
    private EditText addressText;

    private TextView productsText;
    private TextView shippingText;
    private TextView totalText;

    private Button proceedButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Shipping Address");
        initViews();

        int amount = getIntent().getIntExtra("AMOUNT",0);
        int no = getIntent().getIntExtra("NUMBER",0);
        int shippingPrice = no * 100;
        final String products = getIntent().getStringExtra("PRODUCTS");

        productsText.setText(String.valueOf(amount));
        shippingText.setText(String.valueOf(no) + "(no. of products) * 100 = " + String.valueOf(shippingPrice));
        totalText.setText(String.valueOf( shippingPrice + amount ));
        proceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(nameText.getText().toString().isEmpty()){
                    nameText.setError("Name required");
                    nameText.requestFocus();
                    return;
                }
                if(emailText.getText().toString().isEmpty()){
                    emailText.setError("Email required");
                    emailText.requestFocus();
                    return;
                }
                if(phoneText.getText().toString().isEmpty()){
                    phoneText.setError("Phone required");
                    phoneText.requestFocus();
                    return;
                }
                if(addressText.getText().toString().isEmpty()){
                    addressText.setError("Address required");
                    addressText.requestFocus();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(emailText.getText().toString()).matches()){
                    emailText.setError("Please enter valid email");
                    emailText.requestFocus();
                    return;
                }
                if(!Patterns.PHONE.matcher(emailText.getText().toString()).matches()){
                    phoneText.setError("Please enter valid phone number");
                    phoneText.requestFocus();
                    return;
                }

                Task<Void> task = FirebaseDatabase.getInstance().getReference("productCustomer").child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).setValue(getCustomer(products));
                task.addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Checkout.this, "Your Order has been placed successfully.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    public void initViews(){
        nameText = findViewById(R.id.cNameEditText);
        emailText = findViewById(R.id.cEmailEditText);
        phoneText = findViewById(R.id.cPhoneEditText);
        addressText = findViewById(R.id.cAddressEditText);

        productsText = findViewById(R.id.cProductsPrice);
        shippingText = findViewById(R.id.cShippingPrice);
        totalText = findViewById(R.id.cTotalPrice);

        proceedButton = findViewById(R.id.cProceedButton);
    }

    public Customer getCustomer(String products){
        Customer customer = new Customer(nameText.getText().toString(),emailText.getText().toString(),phoneText.getText().toString(),addressText.getText().toString(),products);
        return customer;
    }
}
