package com.hamlet.MrFixer.ui.about;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hamlet.MrFixer.R;

import java.util.Map;

public class AboutFragment extends Fragment implements OnMapReadyCallback {

    private TextView detailText;
    private TextView mobileText;
    private TextView laptopText;
    private TextView tabletText;
    private TextView otherText;
    private TextView locationtext;
    private TextView phoneText;
    private TextView emailText;
    private TextView timeText;

    private TextView mobilePerc;
    private TextView laptopPerc;
    private TextView tabletPerc;
    private TextView otherPerc;

    private ProgressBar mobileProgressBar;
    private ProgressBar laptopProgressBar;
    private ProgressBar tabletProgressBar;
    private ProgressBar otherProgressBar;

    private AboutViewModel aboutViewModel;
    private GoogleMap Map;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    public View onCreateView(@NonNull final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("About");

        aboutViewModel =
                ViewModelProviders.of(this).get(AboutViewModel.class);
        View root = inflater.inflate(R.layout.fragment_about, container, false);

        initViews(root);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.aboutMapView);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        setDetailText();
        setProgressBars();

        phoneText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:+923216734937"));
                startActivity(intent);
            }
        });

        emailText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_SENDTO,Uri.parse("mailto:repair@mrfixer.pk")));
            }
        });
        return root;
    }

    public void initViews(View root){
        detailText = root.findViewById(R.id.abTextDetail);
        mobileText = root.findViewById(R.id.abMobileText);
        mobileProgressBar = root.findViewById(R.id.abMobilePB);
        laptopText = root.findViewById(R.id.abLaptopText);
        laptopProgressBar = root.findViewById(R.id.abLaptopPB);
        tabletText = root.findViewById(R.id.abTabletText);
        tabletProgressBar = root.findViewById(R.id.abTabletPB);
        otherText = root.findViewById(R.id.abOtherText);
        otherProgressBar = root.findViewById(R.id.abOtherPB);

        locationtext = root.findViewById(R.id.abLocationText);
        phoneText = root.findViewById(R.id.abPhoneText);
        emailText = root.findViewById(R.id.abEmailText);
        timeText = root.findViewById(R.id.abTimeText);

        mobilePerc = root.findViewById(R.id.aboutMobilePercentage);
        laptopPerc = root.findViewById(R.id.aboutLaptopPercentage);
        otherPerc = root.findViewById(R.id.aboutOtherPercentage);
        tabletPerc = root.findViewById(R.id.aboutTabletPercentage);
    }

    public void setDetailText(){

        detailText.setText("Mr Fixer is a service that aims to make your life easy by providing laptop and mobile repairing at your doorstep in just 24 hours*..\n" +
                "\n" +
                "If you use any electronic, you’ll know that after some time it needs maintenance work; and if you’re unlucky enough, it might need some repairs too. But you don’t have to worry. Mr fixer got you covered.\n" +
                "\n" +
                "Our delivery person picks your device from your doorstep to take it to our expert repairers. We then repair your device and deliver it back to you within 24 hours.\n" +
                "\n" +
                "We’re committed to boosting the income of our technicians, to provide our customers with the best service, and to make sure that our employees are well-cared for.");

        locationtext.setText("   First Floor (Imperial Dental Care), 872-D,\n\t Faisal Town, Lahore, 54000, Pakistan.");
        phoneText.setText("   Phones: +92-3216734937");
        emailText.setText("   repair@mrfixer.pk");
        timeText.setText("   Mon to Sat: 1 PM to 8 PM \n\t Fri: 3 PM to 8 PM");

    }

    public void setProgressBars(){

        DatabaseReference reference = databaseReference.child("Skills");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, Integer> data = (Map<String, Integer>) dataSnapshot.getValue();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                    ProgressBarAnimation mobileAnimation = new ProgressBarAnimation(mobileProgressBar, 0,  Integer.parseInt(String.valueOf(data.get("Mobile"))));
                    mobileAnimation.setDuration(2000);
                    mobileProgressBar.startAnimation(mobileAnimation);
                    mobilePerc.setText(String.valueOf(data.get("Mobile")) + " %");

                    ProgressBarAnimation laptopAnimation = new ProgressBarAnimation(laptopProgressBar, 0, Integer.parseInt(String.valueOf(data.get("Laptop"))));
                    laptopAnimation.setDuration(2500);
                    laptopProgressBar.startAnimation(laptopAnimation);
                    laptopPerc.setText(String.valueOf(data.get("Laptop")) + " %");

                    ProgressBarAnimation tabletAnimation = new ProgressBarAnimation(tabletProgressBar, 0, Integer.parseInt(String.valueOf(data.get("Tablet"))));
                    tabletAnimation.setDuration(3000);
                    tabletProgressBar.startAnimation(tabletAnimation);
                    tabletPerc.setText(String.valueOf(data.get("Tablet")) + " %");

                    ProgressBarAnimation otherAnimation = new ProgressBarAnimation(otherProgressBar, 0, Integer.parseInt(String.valueOf(data.get("Other"))));
                    otherAnimation.setDuration(3500);
                    otherProgressBar.startAnimation(otherAnimation);
                    otherPerc.setText(String.valueOf(data.get("Other")) + " %");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Map = googleMap;

        LatLng mr = new LatLng( 31.467255,74.308594 );
        Map.addMarker(new MarkerOptions().position(mr).title("Mr Fixer"));
        Map.moveCamera(CameraUpdateFactory.newLatLngZoom(mr,13F));
    }

    public class ProgressBarAnimation extends Animation {
        private ProgressBar progressBar;
        private float from;
        private float  to;

        public ProgressBarAnimation(ProgressBar progressBar, float from, float to) {
            super();
            this.progressBar = progressBar;
            this.from = from;
            this.to = to;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            float value = from + (to - from) * interpolatedTime;
            progressBar.setProgress((int) value);
        }
    }

}

