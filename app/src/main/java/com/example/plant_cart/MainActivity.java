package com.example.plant_cart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toolbar;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    DrawerLayout mDrawerlayout;
    ActionBarDrawerToggle mtoggle;
    NavigationView navigationView;
    Menu menu;
    TextView textCartItemCount;
    int mCartItemCount = 10;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Navigation View
        navigationView = findViewById(R.id.navigationdrawer);
        navigationView.bringToFront();

        mDrawerlayout = (DrawerLayout) findViewById(R.id.drawer);
        mtoggle = new ActionBarDrawerToggle(this, mDrawerlayout, R.string.draweropen, R.string.drawerclose);

        mtoggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home: {
                        mDrawerlayout.closeDrawers();
                        return true;
                    }

                    case R.id.drawerprofile: {
                        mDrawerlayout.closeDrawers();
                        Intent intent = new Intent(MainActivity.this, Profile.class);
                        startActivity(intent);
                        return true;
                    }

                    case R.id.product: {

                    }

                    case R.id.placeorder: {
                        mDrawerlayout.closeDrawers();
                        Intent intent = new Intent(MainActivity.this, cart.class);
                        startActivity(intent);
                        return true;

                    }

                    case R.id.history: {


                    }

                    case R.id.logout: {

                        mDrawerlayout.closeDrawers();
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getApplicationContext(), Login.class));
                        finish();
                    }
                }
                return true;
            }
        });

        //Recycler View Opening

        final Button avenue = (Button) findViewById(R.id.avenuebtn);
        Button floral = (Button) findViewById(R.id.floralbtn);
        Button indoor = (Button) findViewById(R.id.indoorbtn);
        Button fruit = (Button) findViewById(R.id.fruitbtn);
        Button medicinal = (Button) findViewById(R.id.medicinalbtn);
        Button outdoor = (Button) findViewById(R.id.outdoorbtn);
        Button palm = (Button) findViewById(R.id.palmbtn);
        Button pot = (Button) findViewById(R.id.potbtn);
        Button other = (Button) findViewById(R.id.otherbtn);

        avenue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, avenuePlants.class);
                startActivity(intent);
            }
        });

        floral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, floralPlants.class);
                startActivity(intent);
            }
        });

        indoor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, indoorPlants.class);
                startActivity(intent);
            }
        });

        fruit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, fruitPlants.class);
                startActivity(intent);
            }
        });

        medicinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, medicinalPlants.class);
                startActivity(intent);
            }
        });

        outdoor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, outdoorPlants.class);
                startActivity(intent);
            }
        });

        palm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, palmPlants.class);
                startActivity(intent);
            }
        });

        pot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, pot.class);
                startActivity(intent);
            }
        });

        other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, others.class);
                startActivity(intent);
            }
        });

        //Image Slide
        ImageSlider imageSlider = findViewById(R.id.slider);
        List<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel("https://timesofindia.indiatimes.com/thumb/msid-76223288,imgsize-281003,width-400,resizemode-4/76223288.jpg", "Our Indoor Plantation"));
        slideModels.add(new SlideModel("https://image.shutterstock.com/image-photo/selective-closeup-green-seedlinggreen-salad-260nw-557142946.jpg", "Seeds and Saplings"));
        slideModels.add(new SlideModel("https://www.nchasia.com/images/Plants.jpg"));
        imageSlider.setImageList(slideModels, false);

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (mtoggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }
}