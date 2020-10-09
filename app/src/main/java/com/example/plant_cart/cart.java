package com.example.plant_cart;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

public class cart extends AppCompatActivity  {

    RecyclerView recycler_cart;
    Button btn_proceed;
    private FirebaseFirestore firebaseFirestore;
    private FirestoreRecyclerAdapter adapter;
    private FirebaseAuth mAuth;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recycler_cart = findViewById(R.id.recycler_cart);
        btn_proceed = findViewById(R.id.proceedtobill_btn);

        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        btn_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(cart.this , bill.class);
                finish();
                startActivity(intent);
            }
        });


        Query query = firebaseFirestore.collection("users").document(mAuth.getUid()).collection(cardDetail.cnt+"");

        FirestoreRecyclerOptions<cartModel> options = new FirestoreRecyclerOptions.Builder<cartModel>()
                .setQuery(query, cartModel.class)
                .build();

        adapter = new FirestoreRecyclerAdapter< cartModel, cartViewHolder>(options) {
            @NonNull
            @Override
            public cart.cartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cartcard, parent, false);
                return new cartViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final cart.cartViewHolder holder, final int position, @NonNull final cartModel model) {


                holder.cart_name.setText(model.getName());
                holder.cart_price.setText(model.getPrice());

                //Image view in recycler view from firebase storage
                Picasso.get().load(model.getImage()).into(holder.cart_image);

                holder.cart_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CharSequence[] options = new CharSequence[]
                                {
                                        "Cancel",
                                        "Delete"
                                };
                        final AlertDialog.Builder builder = new AlertDialog.Builder(cart.this);
                        builder.setTitle("Delete item from cart");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (i==1){
                                    //adapter.notifyDataSetChanged();
                                    firebaseFirestore.collection("users").document(mAuth.getUid()).collection(cardDetail.cnt+"").document(model.getName().toLowerCase()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(cart.this, "Item removed successfully", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        });
                        builder.show();
                    }
                });

                holder.numberButton.setOnClickListener(new ElegantNumberButton.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                         String num = holder.numberButton.getNumber();
                         holder.card_quantity.setText(num);
                         holder.n=num;

                    }
                });

                DocumentReference documentReference = firebaseFirestore.collection("users").document(mAuth.getUid()).collection(cardDetail.cnt+"").document(model.getName().toLowerCase());
                HashMap<String, Object> updatequantity = new HashMap<>();
                updatequantity.put("quantity" , holder.n);
                documentReference.update(updatequantity);
            }

        };

        //View Holder
        recycler_cart.setLayoutManager(new LinearLayoutManager(cart.this));
        recycler_cart.setAdapter(adapter);
    }

    public class cartViewHolder extends RecyclerView.ViewHolder {

        TextView cart_name;
        TextView cart_price;
        ImageView cart_image;
        ImageView cart_delete;
        TextView card_quantity;
        ElegantNumberButton numberButton;
        String n;


        public cartViewHolder(@NonNull final View itemView) {
            super(itemView);

            cart_name = itemView.findViewById(R.id.cart_name);
            cart_price = itemView.findViewById(R.id.cart_price);
            cart_image = itemView.findViewById(R.id.cart_image);
            cart_delete = itemView.findViewById(R.id.delete);
            card_quantity = itemView.findViewById(R.id.cart_quantity);
            numberButton = itemView.findViewById(R.id.number_button);
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