package com.example.workfitapplication;

import android.os.Bundle;


public class PreferencesActivity extends android.preference.PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new PreferencesFragment()).commit();
    }
}