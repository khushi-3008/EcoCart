package com.example.plant_cart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.plant_cart.billing.address;
import com.example.plant_cart.billing.cart;
import com.example.plant_cart.billing.order;
import com.example.plant_cart.categories.avenuePlants;
import com.example.plant_cart.categories.floralPlants;
import com.example.plant_cart.categories.fruitPlants;
import com.example.plant_cart.categories.indoorPlants;
import com.example.plant_cart.categories.medicinalPlants;
import com.example.plant_cart.categories.others;
import com.example.plant_cart.categories.outdoorPlants;
import com.example.plant_cart.categories.palmPlants;
import com.example.plant_cart.registration.Login;
import com.example.plant_cart.registration.Profile;
import com.google.android.material.bottomnavigation.BottomNavigationView;
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

        //Initialize ConnectivityManager
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        //Get active network info
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        //Check network status
        if(networkInfo == null || !networkInfo.isConnected() || !networkInfo.isAvailable()){
            //When network is inactive

            //Initialize dialog
            Dialog dialog = new Dialog(this);
            //Set content view
            dialog.setContentView(R.layout.alert_dialog);
            //Set outside touch
            dialog.setCanceledOnTouchOutside(false);
            //Set dialog width and height
            dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT);
            //Set transparent background
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            //Set animation
            dialog.getWindow().getAttributes().windowAnimations =
                    android.R.style.Animation_Dialog;

            //Initialize dialog variable
            Button btnTryAgain = dialog.findViewById(R.id.btn_try_again);
            //perform onClick Listener
            btnTryAgain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Call recreate methods
                    recreate();
                }
            });

            //Show dialog
            dialog.show();
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.home);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home: {
                        return true;
                    }

                    case R.id.drawerprofile: {
                        Intent intent = new Intent(MainActivity.this, Profile.class);
                        finish();
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        return true;
                    }

                    case R.id.rate_us: {
                        final Dialog dialog =new Dialog(MainActivity.this);
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
                                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        });

                        ratebtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                                Toast.makeText(MainActivity.this,"Thanks for rating", Toast.LENGTH_SHORT).show();
                            }
                        });
                        overridePendingTransition(0,0);
                        return true;

                    }


                    case R.id.history: {
                        Intent intent = new Intent(MainActivity.this, order.class);
                        finish();
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        return true;
                    }

                }
                return false;
            }
        });


        //Navigation View
//        navigationView = findViewById(R.id.navigationdrawer);
//        navigationView.bringToFront();
//
//        mDrawerlayout = (DrawerLayout) findViewById(R.id.drawer);
//        mtoggle = new ActionBarDrawerToggle(this, mDrawerlayout, R.string.draweropen, R.string.drawerclose);
//
//        mtoggle.syncState();
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.home: {
//                        mDrawerlayout.closeDrawers();
//                        return true;
//                    }
//
//                    case R.id.drawerprofile: {
//                        mDrawerlayout.closeDrawers();
//                        Intent intent = new Intent(MainActivity.this, Profile.class);
//                        startActivity(intent);
//                        return true;
//                    }
//
//                    case R.id.rate_us: {
//                        mDrawerlayout.closeDrawers();
//                        final Dialog dialog =new Dialog(MainActivity.this);
//                        dialog.setContentView(R.layout.activity_rate_us);
//                        dialog.setCanceledOnTouchOutside(false);
//
//                        RatingBar ratingStars;
//                        Button ratebtn;
//
//                        ratebtn = dialog.findViewById(R.id.ratingbtn);
//                        ratingStars = dialog.findViewById(R.id.ratingBar);
//                        //set dialog width and height
//                        dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT);
//                        //Set transparent background
//                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                        //set Animation
//                        dialog.getWindow().getAttributes().windowAnimations = android.R.style.Animation_Dialog;
//
//                        dialog.show();
//
//                        ratingStars.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
//                            @Override
//                            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
//
//                                int rating = (int)v;
//                                String message = null;
//
//                                switch (rating){
//                                    case 1:
//                                        message = "Sorry to hear that";
//                                        break;
//                                    case 2:
//                                        message = "We always accept suggestions!!";
//                                        break;
//                                    case 3:
//                                        message = "Good enough";
//                                        break;
//                                    case 4:
//                                        message = "Great!! Thank you";
//                                        break;
//                                    case 5:
//                                        message = "Awesome!! Thank you..";
//                                        break;
//
//                                }
//                                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
//                            }
//                        });
//
//                        ratebtn.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                dialog.dismiss();
//                                Toast.makeText(MainActivity.this,"Thanks for rating", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                        return true;
//
//                    }
//
//                    case R.id.placeorder: {
//                        mDrawerlayout.closeDrawers();
//                        Intent intent = new Intent(MainActivity.this, cart.class);
//                        startActivity(intent);
//                        return true;
//
//                    }
//
//                    case R.id.history: {
//                        mDrawerlayout.closeDrawers();
//                        Intent intent = new Intent(MainActivity.this, order.class);
//                        startActivity(intent);
//                        return true;
//                    }
//
//                    case R.id.logout: {
//
//                        mDrawerlayout.closeDrawers();
//                        FirebaseAuth.getInstance().signOut();
//                        startActivity(new Intent(getApplicationContext(), Login.class));
//                        finish();
//                    }
//                }
//                return true;
//            }
//        });

        //Recycler View Opening

        CardView avenue = findViewById(R.id.avenuec);
        CardView floral = findViewById(R.id.floralc);
        CardView indoor =  findViewById(R.id.indoorc);
        CardView fruit = findViewById(R.id.fruitc);
        CardView medicinal =  findViewById(R.id.medicinalc);
        CardView outdoor =  findViewById(R.id.outdoorc);
        CardView palm =  findViewById(R.id.palmc);
        CardView pot = findViewById(R.id.potc);
        CardView other =  findViewById(R.id.otherc);

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
                Intent intent = new Intent(MainActivity.this, com.example.plant_cart.categories.pot.class);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.placeorder: {
                Intent intent = new Intent(MainActivity.this, cart.class);
                startActivity(intent);
                return true;
            }

            case R.id.logout: {
                AlertDialog.Builder confirmorderDialog = new AlertDialog.Builder(MainActivity.this);
                confirmorderDialog.setTitle("Are you sure you want to Logout?");
                confirmorderDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getApplicationContext(), Login.class));
                        finish();
                    }
                });

                confirmorderDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                confirmorderDialog.create().show();

            }
        }
        return true;
    }
}