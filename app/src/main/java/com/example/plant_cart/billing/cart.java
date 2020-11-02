package com.example.plant_cart.billing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.plant_cart.R;
import com.example.plant_cart.categories.cardDetail;
import com.example.plant_cart.model.cartModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

import it.sephiroth.android.library.numberpicker.NumberPicker;

public class cart extends AppCompatActivity {

    RecyclerView recycler_cart;
    Button btn_placeorder;
    TextView cart_price;
    private FirebaseFirestore firebaseFirestore;
    private FirestoreRecyclerAdapter adapter;
    private FirebaseAuth mAuth;
    int samount;
    static int totalamount = 0;
    int amount;
    static int c;
    //int stotalamount = 0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recycler_cart = findViewById(R.id.recycler_cart);
        btn_placeorder = findViewById(R.id.placeorder_btn);
        cart_price = findViewById(R.id.total_cartPrice);

        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        btn_placeorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder confirmorderDialog = new AlertDialog.Builder(view.getContext());
                confirmorderDialog.setTitle("Are you sure you want to continue?");
                confirmorderDialog.setMessage("Total amount to be paid: Rs. "+totalamount+"/-");
                confirmorderDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(cart.this, address.class);
                        intent.putExtra("total",totalamount+"");
                        finish();
                        startActivity(intent);
                    }
                });

                confirmorderDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        totalamount=0;
                        dialog.dismiss();
                    }
                });

                confirmorderDialog.create().show();


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

                amount = Integer.parseInt(model.getPrice());
                totalamount+=amount;
                cart_price.setText(("Rs. "+totalamount+"/-"));

                //Image view in recycler view from firebase storage
                Picasso.get().load(model.getImage()).into(holder.cart_image);

                holder.cart_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder deleteDialog = new AlertDialog.Builder(view.getContext());
                        deleteDialog.setTitle("Delete item fro cart?");

                        deleteDialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                showSnackBar();
                                amount = Integer.parseInt(model.getPrice())*Integer.parseInt(holder.quantity);
                                totalamount= totalamount-amount;
                                cart_price.setText("Rs. "+totalamount+"/-");

                                //adapter.notifyDataSetChanged();
                                firebaseFirestore.collection("users").document(mAuth.getUid()).collection(cardDetail.cnt+"").document(model.getName().toLowerCase()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(cart.this, "Item removed successfully", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            public void showSnackBar(){
                                Snackbar snackbar = Snackbar.make(recycler_cart,"Item has been deleted", BaseTransientBottomBar.LENGTH_LONG)
                                        .setAction("UNDO", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                firebaseFirestore.collection("users").document(mAuth.getUid()).collection(cardDetail.cnt+"").document(model.getName().toLowerCase()).set(model);                                            }
                                        });
                                snackbar.show();
                            }
                        });

                        deleteDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });

                        deleteDialog.create().show();
                    }
                });

                holder.numberPicker.setMinValue(1);
                holder.numberPicker.setMaxValue(1000);

                holder.numberPicker.setNumberPickerChangeListener(new  NumberPicker.OnNumberPickerChangeListener() {
                    @Override
                    public void onProgressChanged(@NotNull NumberPicker numberPicker, int i, boolean b) {

                            String qty = String.valueOf(i) ;
                            DocumentReference documentReference = firebaseFirestore.collection("users").document(mAuth.getUid()).collection("quantity").document(cardDetail.cnt+"");
                            HashMap<String,Object> quantity = new HashMap<>();
                            quantity.put(model.getName().toLowerCase(),qty);
                            documentReference.update(quantity);

                            DocumentReference documentReference1 = firebaseFirestore.collection("users").document(mAuth.getUid()).collection("quantity").document(cardDetail.cnt+"");
                        documentReference1.get()
                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()){
                                            DocumentSnapshot snapshot = task.getResult();
                                            totalamount = totalamount - (Integer.parseInt(holder.quantity)*Integer.parseInt(model.getPrice()));
                                            holder.quantity = snapshot.getString(model.getName().toLowerCase());
                                            amount = Integer.parseInt(model.getPrice()) * Integer.parseInt(holder.quantity);
                                            totalamount += amount;
                                            cart_price.setText("Rs. "+totalamount+"/-");
                                        }
                                        else {
                                            Toast.makeText(cart.this, "Error!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                    }

                    @Override
                    public void onStartTrackingTouch(@NotNull NumberPicker numberPicker) {

                    }

                    @Override
                    public void onStopTrackingTouch(@NotNull NumberPicker numberPicker) {

                    }

                });




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
        NumberPicker numberPicker;
        String quantity="1";

        public cartViewHolder(@NonNull final View itemView) {
            super(itemView);

            cart_name = itemView.findViewById(R.id.cart_name);
            cart_price = itemView.findViewById(R.id.cart_price);
            cart_image = itemView.findViewById(R.id.cart_image);
            cart_delete = itemView.findViewById(R.id.delete);
            numberPicker = itemView.findViewById(R.id.numberPicker);
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