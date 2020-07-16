package com.hamlet.MrFixer.ui.gallery;

import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hamlet.MrFixer.R;
import com.hamlet.MrFixer.RepairerCustomer;

public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;

    private EditText nameEdit, contactEdit, cnicEdit, deviceNameEdit, deviceModelEdit, deviceProblemEdit, addressEdit;
    private Spinner deviceTypeEdit;
    private Button sendButton;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private RepairerCustomer repairerCustomer;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        initializeViews(root);
        loadDeviceTypes();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("repairOrders");

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nameEdit.getText().toString().isEmpty()){
                    nameEdit.setError("Name required");
                    nameEdit.requestFocus();
                    return;
                }
                if(contactEdit.getText().toString().isEmpty()){
                    contactEdit.setError("Contact required");
                    contactEdit.requestFocus();
                    return;
                }
                if(!Patterns.PHONE.matcher(contactEdit.getText().toString()).matches()){
                    contactEdit.setError("Please enter valid contact number");
                    contactEdit.requestFocus();
                    return;
                }
                if(cnicEdit.getText().toString().isEmpty()){
                    cnicEdit.setError("CNIC required");
                    cnicEdit.requestFocus();
                    return;
                }
                if(deviceNameEdit.getText().toString().isEmpty()){
                    deviceNameEdit.setError("Device Name required");
                    deviceNameEdit.requestFocus();
                    return;
                }
                if(deviceModelEdit.getText().toString().isEmpty()){
                    deviceModelEdit.setError("Device Model required");
                    deviceModelEdit.requestFocus();
                    return;
                }
                if(deviceProblemEdit.getText().toString().isEmpty()){
                    deviceProblemEdit.setError("Device Problem required");
                    deviceProblemEdit.requestFocus();
                    return;
                }
                if(addressEdit.getText().toString().isEmpty()){
                    addressEdit.setError("Address required");
                    addressEdit.requestFocus();
                    return;
                }
                repairerCustomer = getData();
                databaseReference.child(repairerCustomer.getName().toString()).setValue(repairerCustomer);
                Toast.makeText(getContext(), "Data Sent!", Toast.LENGTH_SHORT).show();
                clearEditFields();
            }
        });
        return root;
    }

    public void loadDeviceTypes(){
        String[] types = {"Mobile","Laptop","Tablet","iPad"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,types);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        deviceTypeEdit.setAdapter(adapter);
    }

    public RepairerCustomer getData(){

        String name = nameEdit.getText().toString();
        String contact = contactEdit.getText().toString();
        String cnic = cnicEdit.getText().toString();
        String deviceType = deviceTypeEdit.getSelectedItem().toString();
        String deviceName = deviceNameEdit.getText().toString();
        String deviceModel = deviceModelEdit.getText().toString();
        String deviceProblem = deviceProblemEdit.getText().toString();
        String address = addressEdit.getText().toString();

        RepairerCustomer repairerCustomer = new RepairerCustomer(name,contact,cnic,deviceType,deviceName,deviceModel,deviceProblem,address);
        return repairerCustomer;
    }

    public void initializeViews(View root){

        nameEdit = root.findViewById(R.id.repairerEditNameText);
        contactEdit = root.findViewById(R.id.repairerEditContactText);
        cnicEdit= root.findViewById(R.id.repairerEditCnicText);
        deviceTypeEdit= root.findViewById(R.id.repairerEditDTText);
        deviceNameEdit= root.findViewById(R.id.repairerEditDNText);
        deviceModelEdit= root.findViewById(R.id.repairerEditDMText);
        deviceProblemEdit= root.findViewById(R.id.repairerEditDPText);
        addressEdit= root.findViewById(R.id.repairerEditAddressText);

        sendButton = root.findViewById(R.id.repairerSendButton);
    }

    public void clearEditFields(){
        nameEdit.setText("");
        contactEdit.setText("");
        cnicEdit.setText("");
        deviceNameEdit.setText("");
        deviceModelEdit.setText("");
        deviceProblemEdit.setText("");
        addressEdit.setText("");
    }
}
