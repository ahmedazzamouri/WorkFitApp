package com.example.workfitapplication;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ErrorInternetFragment extends Fragment {

    TextView errorMsg;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,@Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_error_internet, container, false);
        errorMsg = view.findViewById(R.id.errorTextInternet);

        return view;
    }

    @Override
    public void onStart(){
        super.onStart();

        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        Network net = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M){
            net = connectivityManager.getActiveNetwork();
        }

        NetworkCapabilities actNet = connectivityManager.getNetworkCapabilities(net);

        if(net == null)
        if(!actNet.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)){
            //errorMsg.append();
        } else if (!actNet.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)){
            //errorMsg.append();
        }
    }
}