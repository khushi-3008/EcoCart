package com.example.plant_cart.billing;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.plant_cart.R;
import com.example.plant_cart.categories.cardDetail;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class address extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth mAuth;
    EditText fname,sphone,building,area,state,city,pincode;
    String name,sPhone,Building,Area,State,City,Pincode;
    Button proceedbtn;
//    String fullname;
//    String email;
//    String phone;
    String total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        total = intent.getStringExtra("total");

//        final DocumentReference dr = firebaseFirestore.collection("users").document(mAuth.getUid());
//        dr.addSnapshotListener(this,new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
//                fullname = value.getString("fName");
//                email = value.getString("email");
//                phone = value.getString("phone");
//
//                HashMap<String,Object> bill = new HashMap<>();
//                bill.put("fName",fullname);
//                bill.put("email",email);
//                bill.put("phone",phone);
//                bill.put("total_bills",cardDetail.cnt+"");
//                dr.set(bill);

//            }
//        });

        fname = findViewById(R.id.sfullname);
        sphone = findViewById(R.id.sPhone);
        building = findViewById(R.id.shouseno);
        area = findViewById(R.id.sRoad);
        city = findViewById(R.id.city);
        state = findViewById(R.id.state);
        pincode = findViewById(R.id.pincode);
        proceedbtn = findViewById(R.id.proceedbtn);

        proceedbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = fname.getText().toString();
                sPhone = sphone.getText().toString();
                Building = building.getText().toString();
                Area = area.getText().toString();
                City = city.getText().toString();
                State = state.getText().toString();
                Pincode = pincode.getText().toString();

                if (TextUtils.isEmpty(name))
                {
                    fname.setError("Name is required");
                    return;
                }

                if (TextUtils.isEmpty(Building))
                {
                    building.setError("Building name is required");
                    return;
                }

                if (TextUtils.isEmpty(Area))
                {
                    area.setError("Area is required");
                    return;
                }

                if (TextUtils.isEmpty(City))
                {
                    city.setError("City is required");
                    return;
                }

                if (TextUtils.isEmpty(State))
                {
                    state.setError("State is required");
                    return;
                }

                if (TextUtils.isEmpty(sPhone))
                {
                    sphone.setError("Enter phone number");
                    return;
                }

                if (TextUtils.isEmpty(Pincode))
                {
                    pincode.setError("Enter Pincode");
                    return;
                }

                if (sPhone.length() != 10)
                {
                    sphone.setError("Enter a valid phone number");
                    return;
                }

                if(Pincode.length() != 6)
                {
                    pincode.setError("Enter a valid pincode");
                    return;
                }

                String address=Building+"\n"+Area+"\n"+City+"\n"+State+"\n"+Pincode;

                DocumentReference documentReference1 = firebaseFirestore.collection("bill").document(mAuth.getUid());
                HashMap<String,Object> total = new HashMap<>();
                total.put("address"+ cardDetail.cnt+"",address);
                total.put("total"+ cardDetail.cnt+"", cart.totalamount+"");
                total.put("name"+ cardDetail.cnt+"", name+"");
                total.put("phone"+ cardDetail.cnt+"", sPhone+"");
                documentReference1.set(total, SetOptions.merge());

                cart.totalamount=0;

                Intent intent = new Intent(address.this, bill.class);
                intent.putExtra("name",name);
                intent.putExtra("phone",sPhone);
                intent.putExtra("address",address);
                intent.putExtra("total",total);
                finish();
                startActivity(intent);

            }
        });


    }
}