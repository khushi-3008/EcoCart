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
import com.example.plant_cart.model.billModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.SetOptions;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;


public class bill extends AppCompatActivity {
    int total = 0;
    private RecyclerView billrecyclerlist;
    private FirebaseFirestore firebaseFirestore;
    private FirestoreRecyclerAdapter adapter;
    private FirebaseAuth mAuth;
    Calendar calendar;

    int i =0;
    TextView billname,billaddress,billno,billdate,billphone,tamount;
    Button makepayment,savebtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);

        billrecyclerlist = findViewById(R.id.billrecycler);
        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        final Intent intent = getIntent();
        final String name = intent.getStringExtra("name");
        final String phone = intent.getStringExtra("phone");
        final String address = intent.getStringExtra("address");
//        final String total = intent.getStringExtra("total");

        billname = findViewById(R.id.billname);
        billaddress = findViewById(R.id.billaddress);
        billno = findViewById(R.id.billno);
        billdate = findViewById(R.id.billdate);
        billphone = findViewById(R.id.billphone);
        makepayment = findViewById(R.id.paybtn);
        tamount = findViewById(R.id.tamount);

        calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String date = simpleDateFormat.format(calendar.getTime());

        DocumentReference documentReference1 = firebaseFirestore.collection("bill").document(mAuth.getUid());
        HashMap<String,Object> totalo = new HashMap<>();
        totalo.put("date"+cardDetail.cnt+"",date);
        documentReference1.update(totalo);

        RelativeLayout rl = findViewById(R.id.bill);
        billname.setText(name);
        billaddress.setText(address);
        billno.setText(String.valueOf(cardDetail.cnt));
        billphone.setText(phone);
//        tamount.setText(total);
        billdate.setText(date);
        
        makepayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(bill.this, payment.class);
                intent.putExtra("total",String.valueOf(total));
                finish();
                startActivity(intent1);
            }
        });


        //Query
        Query query = firebaseFirestore.collection("users").document(mAuth.getUid()).collection(String.valueOf(cardDetail.cnt));

        //Recycler Options
        FirestoreRecyclerOptions<billModel> options = new FirestoreRecyclerOptions.Builder<billModel>()
                .setQuery(query, billModel.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<billModel, bill.billViewHolder>(options) {
            @NonNull
            @Override
            public bill.billViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.billcard, parent , false);
                return new billViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final bill.billViewHolder holder, int position, @NonNull final billModel model) {

                holder.product.setText(model.getName());
                firebaseFirestore.collection("users").document(mAuth.getUid()).collection("quantity").document(cardDetail.cnt+"")
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
        billrecyclerlist.setLayoutManager(new LinearLayoutManager(bill.this));
        billrecyclerlist.setAdapter(adapter);

    }

    class billViewHolder extends RecyclerView.ViewHolder {
        TextView srno;
        TextView product;
        TextView quantity;
        TextView amount;
        TextView rate;

        public billViewHolder(@NonNull View itemView) {
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