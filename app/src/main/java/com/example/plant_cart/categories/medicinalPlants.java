package com.example.plant_cart.categories;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
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

import com.example.plant_cart.R;
import com.example.plant_cart.model.plantsModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

public class medicinalPlants extends AppCompatActivity {
    private RecyclerView medicinallist;
    private FirebaseFirestore firebaseFirestore;
    SearchView searchView;
    private FirestoreRecyclerAdapter adapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicinal_plants);
        firebaseFirestore = FirebaseFirestore.getInstance();
        medicinallist = findViewById(R.id.medicinallist);
        searchView = findViewById(R.id.search_badgeme);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                plantSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                plantSearch(newText);
                return false;
            }
        });
        
        //Back button
        getSupportActionBar().setTitle("Medicinal Plants");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Query
        Query query = firebaseFirestore.collection("Medicinal Plants");

        //Recycler Options
        FirestoreRecyclerOptions<plantsModel> options = new FirestoreRecyclerOptions.Builder<plantsModel>()
                .setQuery(query, plantsModel.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<plantsModel, medicinalPlants.plantsViewHolder>(options) {
            @NonNull
            @Override
            public medicinalPlants.plantsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview,parent, false);
                return new medicinalPlants.plantsViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull medicinalPlants.plantsViewHolder holder, int position, @NonNull final plantsModel model) {
                holder.list_name.setText(model.getName());
                holder.list_price.setText("Rs. "+model.getPrice() + "/-");

                holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(view.getContext(), cardDetail.class);
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
        //medicinallist.setHasFixedSize(true);
        adapter.startListening();
        medicinallist.setLayoutManager(new LinearLayoutManager(medicinalPlants.this));
        medicinallist.setAdapter(adapter);

    }

    private void plantSearch(String s) {

        Query query = firebaseFirestore.collection("Medicinal Plants");

        FirestoreRecyclerOptions<plantsModel> options = new FirestoreRecyclerOptions.Builder<plantsModel>().setQuery(query.orderBy("name").startAt(s.toUpperCase()).endAt(s.toUpperCase()+"\uf8ff"),plantsModel.class).build();

        adapter = new FirestoreRecyclerAdapter<plantsModel, medicinalPlants.plantsViewHolder>(options) {
            @NonNull
            @Override
            public medicinalPlants.plantsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview,parent, false);
                return new medicinalPlants.plantsViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull medicinalPlants.plantsViewHolder holder, int position, @NonNull final plantsModel model) {
                holder.list_name.setText(model.getName());
                holder.list_price.setText("Rs. "+model.getPrice() + "/-");

                holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(view.getContext(), cardDetail.class);
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
        //medicinallist.setHasFixedSize(true);
        adapter.startListening();
        medicinallist.setLayoutManager(new LinearLayoutManager(medicinalPlants.this));
        medicinallist.setAdapter(adapter);


    }

    private class plantsViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout constraintLayout;
        TextView list_name;
        TextView list_price;
        ImageView imageViewf;

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