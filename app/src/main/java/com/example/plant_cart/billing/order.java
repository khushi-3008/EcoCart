package com.example.plant_cart.billing;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.example.plant_cart.MainActivity;
import com.example.plant_cart.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.plant_cart.categories.cardDetail;
import com.example.plant_cart.model.historyModel;
import com.example.plant_cart.registration.Profile;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import kotlin.jvm.internal.Ref;

public class order extends AppCompatActivity {

    RecyclerView recycler_order;
    private FirebaseFirestore firebaseFirestore;
    private FirestoreRecyclerAdapter adapter;
    private FirebaseAuth mAuth;
    int count;
    String c;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        recycler_order = findViewById(R.id.order_recycler);

        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.history);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home: {
                        Intent intent = new Intent(order.this, MainActivity.class);
                        finish();
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        return true;
                    }

                    case R.id.drawerprofile: {
                        Intent intent = new Intent(order.this, Profile.class);
                        finish();
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        return true;
                    }

                    case R.id.rate_us: {
                        final Dialog dialog =new Dialog(order.this);
                        dialog.setContentView(R.layout.activity_rate_us);
                        dialog.setCanceledOnTouchOutside(false);

                        RatingBar ratingStars;
                        Button ratebtn;

                        ratebtn = dialog.findViewById(R.id.ratingbtn);
                        ratingStars = dialog.findViewById(R.id.ratingBar);
                        //set dialog width and height
                        dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT);
                        //Set transparent background
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        //set Animation
                        dialog.getWindow().getAttributes().windowAnimations = android.R.style.Animation_Dialog;

                        dialog.show();

                        ratingStars.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                            @Override
                            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {

                                int rating = (int)v;
                                String message = null;

                                switch (rating){
                                    case 1:
                                        message = "Sorry to hear that";
                                        break;
                                    case 2:
                                        message = "We always accept suggestions!!";
                                        break;
                                    case 3:
                                        message = "Good enough";
                                        break;
                                    case 4:
                                        message = "Great!! Thank you";
                                        break;
                                    case 5:
                                        message = "Awesome!! Thank you..";
                                        break;

                                }
                                Toast.makeText(order.this, message, Toast.LENGTH_SHORT).show();
                            }
                        });

                        ratebtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                                Toast.makeText(order.this,"Thanks for rating", Toast.LENGTH_SHORT).show();
                            }
                        });
                        overridePendingTransition(0,0);
                        return true;

                    }

                    case R.id.history: {
                        return true;
                    }

                }
                return false;
            }
        });

        Query query = firebaseFirestore.collection("users").document(mAuth.getUid()).collection("quantity");

        FirestoreRecyclerOptions<historyModel> options = new FirestoreRecyclerOptions.Builder<historyModel>()
                .setQuery(query, historyModel.class)
                .build();

        adapter = new FirestoreRecyclerAdapter< historyModel, orderViewHolder>(options) {
            @NonNull
            @Override
            public order.orderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ordercard, parent, false);
                return new order.orderViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final order.orderViewHolder holder, final int position, @NonNull final historyModel model) {
                holder.billnumer.setText(model.getBillno());

                DocumentReference db = firebaseFirestore.collection("bill").document(mAuth.getUid());
                db.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        String date = documentSnapshot.getString("date"+model.getBillno()+"");
                        holder.date.setText(date);
                    }
                });

                holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(order.this, orderDetail.class);
                        i.putExtra("number",model.getBillno());
                        startActivity(i);
                    }
                });
            }

        };

        //View Holder
        recycler_order.setLayoutManager(new LinearLayoutManager(order.this));
        recycler_order.setAdapter(adapter);
    }


    public class orderViewHolder extends RecyclerView.ViewHolder {
        TextView billnumer;
        TextView date,oqty;
        RelativeLayout relativeLayout;

        public orderViewHolder(@NonNull final View itemView) {
            super(itemView);
            billnumer = itemView.findViewById(R.id.billnumber);
            date = itemView.findViewById(R.id.date);
            relativeLayout = itemView.findViewById(R.id.order_card);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

}