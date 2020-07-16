package com.hamlet.MrFixer.ui.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.daasuu.cat.CountAnimationTextView;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hamlet.MrFixer.ExampleAdapter;
import com.hamlet.MrFixer.ExampleItem;
import com.hamlet.MrFixer.R;
import com.hamlet.MrFixer.orderActivity;
import java.util.ArrayList;
import java.util.Map;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private TextView linkText;
    private Button orderButton;
    private ProgressBar progressBar;
    private ImageSlider imageSlider;

    private CountAnimationTextView productsText;
    private CountAnimationTextView facebookText;
    private CountAnimationTextView instagramText;
    private CountAnimationTextView customerText;

    private CardView fbCard;
    private CardView instaCard;

    private RecyclerView recyclerView;
    private ExampleAdapter adapter;
    private ArrayList<ExampleItem> items;
    private ArrayList<SlideModel> models;

    private RecyclerView saleRecyclerView;
    private ExampleAdapter saleAdapter;
    private ArrayList<ExampleItem> saleItems;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Main");
        initViews(root);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        items = new ArrayList<>();
        adapter = new ExampleAdapter(getContext(), items);
        recyclerView.setAdapter(adapter);

        saleRecyclerView.setHasFixedSize(true);
        saleRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        saleItems = new ArrayList<>();
        saleAdapter = new ExampleAdapter(getContext(), saleItems);
        saleRecyclerView.setAdapter(saleAdapter);

        Toast.makeText(getContext(), "Loading...", Toast.LENGTH_SHORT).show();
        models = new ArrayList<SlideModel>();
        models.add(new SlideModel(R.drawable.slider_1, "Repair your device at your Doorstep"));
        models.add(new SlideModel(R.drawable.slider_2,"24 Hours Repairing guarantee"));
        models.add(new SlideModel(R.drawable.slider_3, "Bring your gadget back to life"));
        imageSlider.setImageList(models,true);


        databaseReference.child("RegularProducts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ExampleItem item = snapshot.getValue(ExampleItem.class);
                    if (item.getSalePrice().toString().equals("00")) {
                        items.add(item);
                        adapter.notifyDataSetChanged();
                    } else {
                        saleItems.add(item);
                        saleAdapter.notifyDataSetChanged();
                    }
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseReference.child("Fans").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, Integer> data = (Map<String, Integer>) dataSnapshot.getValue();
                int products = Integer.parseInt(String.valueOf(data.get("Products")));
                int fbFans = Integer.parseInt(String.valueOf(data.get("Facebook")));
                int instaFans = Integer.parseInt(String.valueOf(data.get("Instagram")));
                int customers = Integer.parseInt(String.valueOf(data.get("Customer")));
                setAnimationsText(products, fbFans, instaFans, customers);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent orderIntent = new Intent(getActivity(), orderActivity.class);
                startActivity(orderIntent);
            }
        });

        fbCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/mrfixerfix/?_rdc=1&_rdr")));
            }
        });

        instaCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/mrfixerfix/")));
            }
        });
        return root;
    }

    public void initViews(View root) {
        linkText = root.findViewById(R.id.linkText);
        linkText.setMovementMethod(LinkMovementMethod.getInstance());
        orderButton = root.findViewById(R.id.orderButton);
        recyclerView = root.findViewById(R.id.homeRecyclerView);
        saleRecyclerView = root.findViewById(R.id.saleRecyclerView);
        productsText = root.findViewById(R.id.productsNumber);
        facebookText = root.findViewById(R.id.facebookNumber);
        instagramText = root.findViewById(R.id.InstagramNumber);
        customerText = root.findViewById(R.id.customerNumber);
        progressBar = root.findViewById(R.id.homeProgressBar);
        progressBar.setVisibility(View.VISIBLE);
        fbCard = root.findViewById(R.id.facebookCard);
        instaCard = root.findViewById(R.id.instagramCard);
        imageSlider =root.findViewById(R.id.imageSlider);
    }

    public void setAnimationsText(int products, int fbFans, int instaFans, int customers) {
        productsText.setAnimationDuration(9000).countAnimation(0, products);
        facebookText.setAnimationDuration(7000).countAnimation(0, fbFans);
        instagramText.setAnimationDuration(8000).countAnimation(0, instaFans);
        customerText.setAnimationDuration(8400).countAnimation(0, customers);
    }
}
