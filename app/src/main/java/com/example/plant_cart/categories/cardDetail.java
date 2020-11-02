package com.example.plant_cart.categories;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.plant_cart.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class cardDetail extends AppCompatActivity {

    TextView name_detail;
    TextView price_detail,detail;
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
        detail = findViewById(R.id.detail);
        imageView = findViewById(R.id.carddetail_image);
        add_to_cart = (Button)findViewById(R.id.add_to_cart);

        final Intent intent = getIntent();
        final String name = intent.getStringExtra("name");
        final String price = intent.getStringExtra("price");
        final String pic = intent.getStringExtra("pic");

        name_detail.setText(name);
        price_detail.setText("Rs. "+price+"/-");

        Picasso.get().load(pic).into(imageView);

        String d = "Name – Albizia julibrissin\n" +
                "Family – legume family (Fabaceae)\n" +
                "Type – tree\n" +
                "Height – 20 to 40 feet (6 to 12m)\n" +
                "Exposure – Full sun Soil – ordinary\n" +
                "Foliage – deciduous\n" +
                "Flowering – July-August\n" +
                "Invasive in – USA, Canada, Australia";

        detail.setText(d);

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
            @Override
            public final void onClick(View view) {
                cnt++;
                DocumentReference documentReference1 = firebaseFirestore.collection("users").document(mAuth.getUid()).collection("quantity").document(cardDetail.cnt+"");
                HashMap<String,Object> quantity = new HashMap<>();
                quantity.put(name.toLowerCase(),"1");
                quantity.put("billno",cnt+"");
                documentReference1.set(quantity, SetOptions.merge());

                DocumentReference documentReference = firebaseFirestore.collection("users").document(mAuth.getUid()).collection(cnt+"").document(name.toLowerCase());
                HashMap<String, Object> add_to_cart = new HashMap<>();
                add_to_cart.put("name", name);
                add_to_cart.put("price", price);
                add_to_cart.put("image", pic);
                add_to_cart.put("quantity","1");

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