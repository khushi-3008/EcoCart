package com.example.plant_cart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toolbar;

import com.google.android.material.internal.NavigationMenuItemView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
        DrawerLayout mDrawerlayout;
        ActionBarDrawerToggle mtoggle;
        Toolbar toolbar;
        NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationView = findViewById(R.id.navigationdrawer);
        navigationView.bringToFront();

        mDrawerlayout = (DrawerLayout) findViewById(R.id.drawer);
        mtoggle = new ActionBarDrawerToggle(this,mDrawerlayout,R.string.draweropen,R.string.drawerclose);

        mtoggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home: {
                        mDrawerlayout.closeDrawers();
                        return true;
                    }

                    case R.id.drawerprofile:{
                        mDrawerlayout.closeDrawers();
                        Intent intent = new Intent(MainActivity.this, Profile.class);
                        startActivity(intent);
                        return true;
                    }

                    case R.id.product: {

                    }

                    case R.id.placeorder: {


                    }

                    case R.id.history: {


                    }

                    case R.id.logout: {

                        mDrawerlayout.closeDrawers();
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getApplicationContext(),Login.class));
                        finish();
                    }
                }
                return true;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (mtoggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }
}