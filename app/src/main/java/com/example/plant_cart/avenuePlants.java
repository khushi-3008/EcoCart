package com.example.plant_cart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.nex3z.notificationbadge.NotificationBadge;
import com.squareup.picasso.Picasso;


public class avenuePlants extends AppCompatActivity {
    private RecyclerView avenuelist;
    private FirebaseFirestore firebaseFirestore;
    private FirestoreRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avenue_plants);

        firebaseFirestore = FirebaseFirestore.getInstance();
        avenuelist = findViewById(R.id.avenuelist);

        //Back button
        getSupportActionBar().setTitle("Avenue Plants");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Query
        Query query = firebaseFirestore.collection("Avenue");

        //Recycler Options
        FirestoreRecyclerOptions<plantsModel> options = new FirestoreRecyclerOptions.Builder<plantsModel>()
                .setQuery(query, plantsModel.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<plantsModel, plantsViewHolder>(options) {
            @NonNull
            @Override
            public plantsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview,parent, false);
                return new plantsViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull plantsViewHolder holder, int position, @NonNull final plantsModel model) {
                holder.list_name.setText(model.getName());
                holder.list_price.setText(model.getPrice() + "");

                holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(view.getContext(),cardDetail.class);
                        intent.putExtra("name", model.getName());
                        intent.putExtra("price", model.getPrice()+"");
                        intent.putExtra("pic", model.getPic());
                        view.getContext().startActivity(intent);

                    }
                });

                //Image view in recycler view from firebase storage
                Picasso.get().load(model.getPic()).into(holder.imageViewf);

            }
        };

        //View Holder
        avenuelist.setHasFixedSize(true);
        avenuelist.setLayoutManager(new LinearLayoutManager(avenuePlants.this));
        avenuelist.setAdapter(adapter);

    }

    class plantsViewHolder extends RecyclerView.ViewHolder {

        TextView list_name;
        TextView list_price;
        ImageView imageViewf;
        ConstraintLayout constraintLayout;

        plantsModel model = new plantsModel();

        public plantsViewHolder(@NonNull View itemView) {
            super(itemView);

            list_name = itemView.findViewById(R.id.listname);
            list_price = itemView.findViewById(R.id.listprice);
            imageViewf = itemView.findViewById(R.id.imageView);
            constraintLayout = itemView.findViewById(R.id.cardview);

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