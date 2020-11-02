package com.example.plant_cart.billing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.plant_cart.R;
import com.example.plant_cart.categories.cardDetail;
import com.example.plant_cart.model.orderModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class orderDetail extends AppCompatActivity {
    int total = 0;
    private RecyclerView billrecyclerlist;
    private FirebaseFirestore firebaseFirestore;
    private FirestoreRecyclerAdapter adapter;
    private FirebaseAuth mAuth;
    Calendar calendar;
    int i =0;
    TextView billname,billaddress,billno,billdate,billphone,tamount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        Intent intent = getIntent();
        final String billnum = intent.getStringExtra("number");

        billrecyclerlist = (RecyclerView) findViewById(R.id.orderbill);
        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        billname = findViewById(R.id.obillname);
        billaddress = findViewById(R.id.obilladdress);
        billno = findViewById(R.id.obillno);
        billdate = findViewById(R.id.obilldate);
        billphone = findViewById(R.id.obillphone);
        tamount = findViewById(R.id.otamount);

        calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String date = simpleDateFormat.format(calendar.getTime());

        DocumentReference db = firebaseFirestore.collection("bill").document(mAuth.getUid());
        db.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documentSnapshot = task.getResult();
                String address = documentSnapshot.getString("address"+billnum+"");
                String name = documentSnapshot.getString("name"+billnum+"");
                String phone = documentSnapshot.getString("phone"+billnum+"");
                String date = documentSnapshot.getString("date"+billnum+"");
                billaddress.setText(address);
                billphone.setText(phone);
                billname.setText(name);
                billdate.setText(date);
            }
        });

        billno.setText(billnum+"");


        //Query
        Query query = firebaseFirestore.collection("users").document(mAuth.getUid()).collection(billnum+"");

        //Recycler Options
        FirestoreRecyclerOptions<orderModel> options = new FirestoreRecyclerOptions.Builder<orderModel>()
                .setQuery(query, orderModel.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<orderModel, orderDetail.orderDetailViewHolder>(options) {
            @NonNull
            @Override
            public orderDetail.orderDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.billcard, parent , false);
                return new orderDetailViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final orderDetail.orderDetailViewHolder holder, int position, @NonNull final orderModel model) {

                holder.product.setText(model.getName());
                firebaseFirestore.collection("users").document(mAuth.getUid()).collection("quantity").document(billnum+"")
                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        String qty = documentSnapshot.getString(model.getName().toLowerCase());
                        holder.quantity.setText(qty);
                        holder.amount.setText(String.valueOf(Integer.parseInt(model.getPrice())*Integer.parseInt(qty)));
                        total = total + (Integer.parseInt(model.getPrice())*Integer.parseInt(qty));
                        tamount.setText("Rs. "+total+"/-");
                    }
                });
                holder.srno.setText(String.valueOf(i));
                holder.rate.setText(model.getPrice());

            }
        };

        //View Holder
        billrecyclerlist.setLayoutManager(new LinearLayoutManager(orderDetail.this));
        billrecyclerlist.setAdapter(adapter);

    }

    class orderDetailViewHolder extends RecyclerView.ViewHolder {
        TextView srno;
        TextView product;
        TextView quantity;
        TextView amount;
        TextView rate;

        public orderDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            i++;
            srno = itemView.findViewById(R.id.number);
            product=itemView.findViewById(R.id.product);
            quantity = itemView.findViewById(R.id.qty);
            rate = itemView.findViewById(R.id.rate);
            amount = itemView.findViewById(R.id.amount);

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