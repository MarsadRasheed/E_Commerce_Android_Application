package com.hamlet.MrFixer.ui.contact;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hamlet.MrFixer.Contact;
import com.hamlet.MrFixer.R;

public class ContactFragment extends Fragment implements OnMapReadyCallback{

    private EditText firstNameEdit;
    private EditText lastNameEdit;
    private EditText emailText;
    private EditText messageText;

    private TextView locationTextView;
    private TextView phoneTextView;
    private TextView timeTextView;
    private TextView emailTextView;

    private ImageView fbImage;
    private ImageView instaImage;

    private Button sendButton;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private ContactViewModel contactViewModel;

    private SupportMapFragment mapFragment;
    private GoogleMap map;


    public View onCreateView(@NonNull final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        contactViewModel =
                ViewModelProviders.of(this).get(ContactViewModel.class);
        View root = inflater.inflate(R.layout.fragment_contact, container, false);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Contact");

        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.contactMapView);
        mapFragment.getMapAsync(this);

        initViews(root);
        setViews();

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(firstNameEdit.getText().toString().isEmpty()){
                    firstNameEdit.setError("First Name required");
                    firstNameEdit.requestFocus();
                    return;
                }
                if(lastNameEdit.getText().toString().isEmpty()){
                    lastNameEdit.setError("Last Name required");
                    lastNameEdit.requestFocus();
                    return;
                }
                if(emailText.getText().toString().isEmpty()){
                    emailText.setError("Email required");
                    emailText.requestFocus();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(emailText.getText().toString()).matches()){
                    emailText.setError("Please enter valid email address");
                    emailText.requestFocus();
                    return;
                }
                if(messageText.getText().toString().isEmpty()){
                    messageText.setError("Message required");
                    messageText.requestFocus();
                    return;
                }


                Task<Void> task = onSendButtonPressed();
                if (task != null) {
                    task.addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getActivity(), "Message Sent", Toast.LENGTH_SHORT).show();
                            clearEditFields();
                        }
                    });
                } else {
                    Toast.makeText(getActivity(), "Can't send Message ", Toast.LENGTH_SHORT).show();
                }
            }
        });

        fbImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/mrfixerfix/?_rdc=1&_rdr")));
            }
        });

        instaImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/mrfixerfix/")));
            }
        });

        phoneTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:+923216734937"));
                startActivity(intent);
            }
        });

        emailTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_SENDTO,Uri.parse("mailto:repair@mrfixer.pk")));
            }
        });

        return root;
    }

    private void clearEditFields() {
        firstNameEdit.setText("");
        lastNameEdit.setText("");
        emailText.setText("");
        messageText.setText("");
    }

    public Task<Void> onSendButtonPressed() {
        Contact contact = getContact();
        Task<Void> task = databaseReference.child(contact.getEmail()).setValue(contact);
        return task;
    }

    public Contact getContact() {
        String email = emailText.getText().toString();
        String firstName = firstNameEdit.getText().toString();
        String lastName = lastNameEdit.getText().toString();
        String message = messageText.getText().toString();

        return new Contact(email, firstName, lastName, message);
    }

    public void initViews(View root) {
        firstNameEdit = root.findViewById(R.id.conFNameEdit);
        lastNameEdit = root.findViewById(R.id.conLNameEdit);
        emailText = root.findViewById(R.id.conEmailEdit);
        messageText = root.findViewById(R.id.conMessageEdit);
        sendButton = root.findViewById(R.id.contactSendButton);

        locationTextView = root.findViewById(R.id.conLocationText);
        phoneTextView = root.findViewById(R.id.conPhoneText);
        emailTextView = root.findViewById(R.id.conEmailText);
        timeTextView = root.findViewById(R.id.conTimeText);

        fbImage = root.findViewById(R.id.conFbImage);
        instaImage = root.findViewById(R.id.conInstaImage);
    }

    public void setViews(){

        locationTextView.setText("   First Floor (Imperial Dental Care), 872-D,\n\t Faisal Town, Lahore, 54000, Pakistan.");
        phoneTextView.setText("   Phones: +92-3216734937");
        emailTextView.setText("   repair@mrfixer.pk");
        timeTextView.setText("   Mon to Sat: 1 PM to 8 PM \n\t Fri: 3 PM to 8 PM");

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        LatLng mrfixer = new LatLng(31.467255,74.308594 );
        map.addMarker(new MarkerOptions().position(mrfixer).title("Mr Fixer"));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(mrfixer,13F));
    }

}
