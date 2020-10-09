package com.example.plant_cart;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.style.IconMarginSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.example.plant_cart.cart.cartViewHolder.*;

import java.util.Collection;
import java.util.HashMap;

public class cardDetail extends AppCompatActivity {

    TextView name_detail;
    TextView price_detail;
    ImageView imageView;
    Button add_to_cart;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth mAuth;
    public static int cnt;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_detail);

        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        name_detail = findViewById(R.id.namedetail);
        price_detail = findViewById(R.id.pricedetail);
        imageView = findViewById(R.id.carddetail_image);
        add_to_cart = (Button)findViewById(R.id.add_to_cart);

        final Intent intent = getIntent();
        final String name = intent.getStringExtra("name");
        final String price = intent.getStringExtra("price");
        final String pic = intent.getStringExtra("pic");

        name_detail.setText(name);
        price_detail.setText(price);

        Picasso.get().load(pic).into(imageView);

        DocumentReference document = firebaseFirestore.collection("users").document(mAuth.getUid());
        document.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documentSnapshot = task.getResult();
                String c = documentSnapshot.getString("total_bills");
                cnt = Integer.parseInt(c);
            }
        });

        add_to_cart.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public final void onClick(View view) {
                cnt++;
                DocumentReference documentReference = firebaseFirestore.collection("users").document(mAuth.getUid()).collection(cnt+"").document(name.toLowerCase());
                HashMap<String, Object> add_to_cart = new HashMap<>();
                add_to_cart.put("name", name);
                add_to_cart.put("price", price);
                add_to_cart.put("image", pic);
                add_to_cart.put("quantity", "1"+"");

                documentReference.set(add_to_cart).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(cardDetail.this, "Item added to cart successfully", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(cardDetail.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}