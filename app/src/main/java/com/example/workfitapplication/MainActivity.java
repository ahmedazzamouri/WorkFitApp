package com.example.workfitapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;
    NavigationView nv;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nv = findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.drawer_layout);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.Open, R.string.Close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        nv.setNavigationItemSelectedListener(this);
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_timer){
            Intent intent1 = new Intent(MainActivity.this, Timer.class);
            startActivity(intent1);
        }
        else if (id == R.id.nav_profile){
            Intent intent2 = new Intent(MainActivity.this, UserProfile.class);
            startActivity(intent2);
        }
        else if (id == R.id.nav_splits){
            Intent intent3 = new Intent(MainActivity.this, DaySplits.class);
            startActivity(intent3);
        } else if (id == R.id.nav_calc_calorie){
            Intent intent4 = new Intent(MainActivity.this, CalorieRecommendation.class);
            startActivity(intent4);
        } else if (id == R.id.nav_steps){
            Intent intent5 = new Intent(MainActivity.this,StepCountActivity.class);
            startActivity(intent5);
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
