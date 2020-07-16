package com.hamlet.MrFixer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class HomeDetailActivity extends AppCompatActivity {

    public static final int REQUEST_CALL = 1;

    private ImageView imageView;
    private TextView nameText;
    private TextView regularPriceText;
    private TextView salePriceText;
    private Spinner quantitySpinner;
    private TextView daysText;
    private TextView availabilityText;
    private Button cartButton;
    private TextView callToOrderText;
    private ImageButton likeButton;

    private Boolean likeButtonClicked = false;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference refOfWishList;
    private DatabaseReference refOfCartList;
    private FirebaseAuth firebaseAuth;

    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Detail");

        firebaseDatabase = FirebaseDatabase.getInstance();
        refOfWishList = firebaseDatabase.getReference("usersWishList");
        refOfCartList = firebaseDatabase.getReference("usersCartList");
        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        userId = firebaseUser != null ? firebaseUser.getUid() : null;

        initViews();
        loadQuantity();
        final ExampleItem exampleItem = (ExampleItem) getIntent().getSerializableExtra("EXTRA_ITEM");

        String productName = exampleItem.getName();
        String regularPrice = exampleItem.getRegularPrice();
        final String salePrice = exampleItem.getSalePrice();
        String link = exampleItem.getImageUrl();

        nameText.setText(productName);

        if (!salePrice.equals("00")) {
            salePriceText.setText("Rs. " + regularPrice);
            regularPriceText.setText("Rs. " + salePrice);
            regularPriceText.setTextColor(Color.parseColor("#008000"));
            salePriceText.setTextColor(Color.RED);
            salePriceText.setPaintFlags(salePriceText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            regularPriceText.setText("Rs. " + regularPrice);
            regularPriceText.setTextColor(Color.parseColor("#008000"));
            salePriceText.setText("");
        }

        Picasso.get().load(link).placeholder(R.drawable.ic_image).fit().centerCrop().into(imageView);

        Integer quantity = (Integer) quantitySpinner.getSelectedItem();

        refOfWishList.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    ExampleItem item = snapshot.getValue(ExampleItem.class);
                    if(exampleItem.getId().equals(item.getId())){
                        int icon = R.drawable.ic_favorite_red;
                        likeButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), icon));
                        likeButtonClicked = true;
                    } else {
                        int icon = R.drawable.ic_favorite_black;
                        likeButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), icon));
                        likeButtonClicked = false;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (likeButtonClicked) {
                    if (userId != null) {
                        Task<Void> task = Utilis.removeValue(userId, exampleItem.getId());
                        int icon = R.drawable.ic_favorite_black;
                        likeButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), icon));
                        Toast.makeText(HomeDetailActivity.this, "removed from wishlist", Toast.LENGTH_SHORT).show();
                        likeButtonClicked = false;

                    } else {
                        Toast.makeText(HomeDetailActivity.this, "You need to log in first!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (userId != null) {
                        Task<Void> task = refOfWishList.child(userId).child(exampleItem.getId()).setValue(exampleItem);
                        task.addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    int icon = R.drawable.ic_favorite_red;
                                    likeButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), icon));
                                    Snackbar.make(findViewById(android.R.id.content), "Item added to wish list", Snackbar.LENGTH_SHORT).setAction("Go to Wishlist", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(HomeDetailActivity.this, WishListActivity.class);
                                            startActivity(intent);
                                        }
                                    }).show();
                                    likeButtonClicked = true;
                                } else {
                                    Toast.makeText(HomeDetailActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(HomeDetailActivity.this, "You need to log in first!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userId != null) {
                    final String quantity = quantitySpinner.getSelectedItem().toString();
                    CartItem cartItem = new CartItem(exampleItem, quantity);
                    Task<Void> task = refOfCartList.child(userId).child(cartItem.getId()).setValue(cartItem);
                    task.addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Snackbar.make(findViewById(android.R.id.content), "Item added to Cart List", Snackbar.LENGTH_SHORT).setAction("Go to Cart", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(HomeDetailActivity.this, CartListActivity.class);
                                        startActivity(intent);
                                    }
                                }).show();
                            } else {
                                Toast.makeText(HomeDetailActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(HomeDetailActivity.this, "You need to log in first!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        callToOrderText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoneCall();
            }
        });
    }

    private void makePhoneCall() {
        if (ContextCompat.checkSelfPermission(HomeDetailActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(HomeDetailActivity.this,new String[] {Manifest.permission.CALL_PHONE},REQUEST_CALL);
        } else {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:+923216734937"));
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL ){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                makePhoneCall();
            } else {
                Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void initViews() {
        imageView = findViewById(R.id.hdImageView);
        nameText = findViewById(R.id.hdNameText);
        regularPriceText = findViewById(R.id.hdRegularPriceText);
        salePriceText = findViewById(R.id.hdSalePriceText);
        quantitySpinner = findViewById(R.id.hdQuantitySpinner);
        daysText = findViewById(R.id.hdDaysText);
        daysText.setTextColor(Color.BLUE);
        availabilityText = findViewById(R.id.hdStockText);
        availabilityText.setTextColor(Color.parseColor("#3CB371"));
        likeButton = findViewById(R.id.hdWishLstButton);
        cartButton = findViewById(R.id.hdCartButton);
        callToOrderText = findViewById(R.id.hdCallText);
    }

    public void loadQuantity() {
        Integer[] quantity = {1, 2, 3, 4, 5};
        ArrayAdapter<Integer> quantityAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, quantity);
        quantityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        quantitySpinner.setAdapter(quantityAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.cartDetailButton) {
            if (userId != null) {
                Intent intent = new Intent(HomeDetailActivity.this, CartListActivity.class);
                startActivity(intent);
            } else {
                Snackbar.make(findViewById(android.R.id.content), "You need to log in first", Snackbar.LENGTH_LONG).setAction("LOG IN", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(HomeDetailActivity.this,LogInActivity.class);
                        startActivity(intent);
                    }
                }).show();
            }
        } else if (item.getItemId() == R.id.wishListDetailButton) {
            if (userId != null) {
                Intent intent = new Intent(HomeDetailActivity.this, WishListActivity.class);
                startActivity(intent);
            } else {
                Snackbar.make(findViewById(android.R.id.content), "You need to log in first", Snackbar.LENGTH_LONG).setAction("LOG IN", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(HomeDetailActivity.this, LogInActivity.class);
                        startActivity(intent);
                    }
                }).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

}
