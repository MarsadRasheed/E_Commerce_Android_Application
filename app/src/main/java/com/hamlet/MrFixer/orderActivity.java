package com.hamlet.MrFixer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class orderActivity extends AppCompatActivity {

    private EditText nameEdit, contactEdit, cnicEdit, deviceNameEdit, deviceModelEdit, deviceProblemEdit, addressEdit;
    private Spinner deviceTypeEdit;
    private Button sendButton;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private RepairerCustomer repairerCustomer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        getSupportActionBar().setTitle("Order Repair");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initViews();
        loadDeviceType();

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
                Toast.makeText(getApplicationContext(), "Data Sent!", Toast.LENGTH_SHORT).show();
                clearEditFields();
            }
        });
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

    public void clearEditFields(){
        nameEdit.setText("");
        contactEdit.setText("");
        cnicEdit.setText("");
        deviceNameEdit.setText("");
        deviceModelEdit.setText("");
        deviceProblemEdit.setText("");
        addressEdit.setText("");
    }

    public void initViews(){
        nameEdit = findViewById(R.id.orderEditNameText);
        contactEdit = findViewById(R.id.orderEditContactText);
        cnicEdit= findViewById(R.id.orderEditCnicText);
        deviceTypeEdit= findViewById(R.id.orderEditDTText);
        deviceNameEdit= findViewById(R.id.orderEditDNText);
        deviceModelEdit= findViewById(R.id.orderEditDMText);
        deviceProblemEdit= findViewById(R.id.orderEditDPText);
        addressEdit= findViewById(R.id.orderEditAddressText);
        sendButton = findViewById(R.id.orderSendButton);
    }

    public void loadDeviceType(){
        String[] types = {"Mobile","Laptop","Tablet","iPad"};
        ArrayAdapter<String> typesAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,types);
        typesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        deviceTypeEdit.setAdapter(typesAdapter);
    }
}


