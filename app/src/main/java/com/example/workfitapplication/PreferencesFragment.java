package com.example.workfitapplication;

import com.example.workfitapplication.R;

import android.os.Bundle;
import android.preference.PreferenceFragment;

public class PreferencesFragment extends android.preference.PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.activity_profile);
    }
}