package com.example.greenthumbapp;

import android.os.Bundle;
import android.view.View;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.ViewGroup;

public class MyPlants extends Fragment {
    private TabLayout tabs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_plants, container, false);
    }


}
