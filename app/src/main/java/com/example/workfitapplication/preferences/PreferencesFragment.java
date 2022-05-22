package com.example.workfitapplication.preferences;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.example.workfitapplication.R;

public class PreferencesFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.editprofile);
    }
}
