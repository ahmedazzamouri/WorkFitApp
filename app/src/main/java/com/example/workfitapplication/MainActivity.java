package com.example.workfitapplication;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;
    private Toolbar toolbar;
    NavigationView nv;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    public static final String WIFI = "Wi-Fi";
    public static final String ANY = "Any";
    private static SharedPreferences sharedPrefs;
    private static boolean wifiConnected = false;
    private static boolean mobileConnected = false;
    public static String sPref = null;
    private MainActivity.NetworkReceiver receiver = new NetworkReceiver();
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build()
        );

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nv = findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.drawer_layout);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.Open, R.string.Close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        nv.setNavigationItemSelectedListener(this);

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkReceiver();
        this.registerReceiver(receiver, filter);
        if (Build.VERSION.SDK_INT >= 23)
            if (! checkPermissions())
                requestPermissions();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 5){
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK){
                FirebaseUser user = mAuth.getCurrentUser();
            }
        }
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
        } else if (id == R.id.nav_steps) {
            Intent intent5 = new Intent(MainActivity.this, StepCountActivity.class);
            startActivity(intent5);
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public Fragment activeInternet() {

        Fragment selectedFragment = null;

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        sPref = sharedPrefs.getString("listPref", "Wi-Fi");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            updateConnectedFlags();
        }

        if (((sPref.equals(ANY)) && (wifiConnected || mobileConnected)) || ((sPref.equals(WIFI)) && (wifiConnected))) {
            selectedFragment = new HomeFragment();
        } else {
            selectedFragment = new ErrorInternetFragment();
        }

        return selectedFragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onStart() {
        super.onStart();
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        sPref = sharedPrefs.getString("listPref", "Wi-Fi");
        updateConnectedFlags();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            this.unregisterReceiver(receiver);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void updateConnectedFlags() {
        ConnectivityManager cMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        Network nw = cMgr.getActiveNetwork();
        if (nw == null) {
            wifiConnected = false;
            mobileConnected = false;
        } else {
            NetworkCapabilities actNw = cMgr.getNetworkCapabilities(nw);
            if (actNw == null) {
                wifiConnected = false;
                mobileConnected = false;
            } else if (actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET))
                wifiConnected = true;
            else if (actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
                mobileConnected = true;
        }
    }


    public static class NetworkReceiver extends BroadcastReceiver {

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onReceive(Context context, Intent intent) {

            ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            sPref = sharedPrefs.getString("listPref", "Wi-Fi");
            Network nw = connMgr.getActiveNetwork();
            if (nw == null) {
                Toast.makeText(context, R.string.lost_connection, Toast.LENGTH_SHORT).show();
            } else {
                NetworkCapabilities actNw = connMgr.getNetworkCapabilities(nw);
                if (actNw == null) {
                    Toast.makeText(context, R.string.lost_connection, Toast.LENGTH_SHORT).show();
                } else if (WIFI.equals(sPref) && (actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET))) {
                    Toast.makeText(context, R.string.wifi_connected, Toast.LENGTH_SHORT).show();
                } else if (ANY.equals(sPref))
                    Toast.makeText(context, R.string.connection_enabled, Toast.LENGTH_SHORT).show();
            }
        }
    }
    private boolean checkPermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.INTERNET) ==
                    PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.ACCESS_NETWORK_STATE) ==
                    PackageManager.PERMISSION_GRANTED)
                return true;
            else
                return false;
        } else
            return true;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE,},
                0);
    }
}
