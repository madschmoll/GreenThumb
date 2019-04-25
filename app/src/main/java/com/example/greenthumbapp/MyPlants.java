package com.example.greenthumbapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MyPlants extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_plants);
    }

    public void goToHistory(View v){
        if(v.getId() == R.id.history_tab){
            //handle the click here and make whatever you want
            startActivity(new Intent(MyPlants.this, DataGraphs.class));
        }
    }

    public void goToLearnMore(View v){
        if(v.getId() == R.id.learn_more_tab){
            //handle the click here and make whatever you want
            startActivity(new Intent(MyPlants.this, LearnMore.class));
        }
    }
}
