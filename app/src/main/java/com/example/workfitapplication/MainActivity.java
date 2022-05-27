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
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
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

import com.example.workfitapplication.preferences.PreferencesActivity;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener{

    public static Uri photo;
    private FirebaseAuth mAuth;
    private Toolbar toolbar;
    BottomNavigationView bottomNav;
    public static final String WIFI = "Wi-Fi";
    public static final String ANY = "Any";
    private static SharedPreferences sharedPrefs;
    private static boolean wifiConnected = false;
    private static boolean mobileConnected = false;
    public static String sPref = null;
    private MainActivity.NetworkReceiver receiver = new NetworkReceiver();



    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                Fragment selectedFragment = null;

                if (id == R.id.homeButton) {
                    selectedFragment = activeInternet();
                } else if (id == R.id.searchButton) {
                    selectedFragment = new HomeFragment();
                } else if (id == R.id.addWorkoutButton) {
                    selectedFragment = new WorkoutFragment();

                } else if (id == R.id.stepCounter) {
                    selectedFragment = new HomeFragment();
                } else if (id == R.id.profileButton) {
                    selectedFragment = new UserProfileFragment();
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                return true;
            }
        });

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
                setSharedPrefs();
                Fragment fragment = activeInternet();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                System.out.println("Success");
            } else {
                Toast.makeText(this,R.string.login_email, Toast.LENGTH_LONG).show();
                System.out.println("Fail");
                finish();
            }
        }
    }

    private void setSharedPrefs() {
        FirebaseUser user = mAuth.getCurrentUser();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Name", user.getDisplayName());
        editor.putString("E-mail", user.getEmail());
        photo = user.getPhotoUrl();
        editor.apply();
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Fragment selectedFragment = null;

        if (id == R.id.homeButton) {
            selectedFragment = activeInternet();
        } else if (id == R.id.searchButton) {
            selectedFragment = new HomeFragment();
        } else if (id == R.id.addWorkoutButton) {
            selectedFragment = new WorkoutFragment();

        } else if (id == R.id.stepCounter) {
            selectedFragment = new HomeFragment();
        } else if (id == R.id.profileButton) {
            selectedFragment = new UserProfileFragment();
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.config:
                startActivity(new Intent(this, PreferencesActivity.class));
                return true;

            case R.id.logout:
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.signOut();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            default:
                return super.onOptionsItemSelected(item);
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
