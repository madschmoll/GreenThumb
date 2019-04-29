package com.example.greenthumbapp;

import android.content.BroadcastReceiver;
import android.support.v4.content.LocalBroadcastManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Main2Activity extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener {

    private BluetoothConnectionService device;
    private String bluetoothVal;

    public BroadcastReceiver mReceiver;

    MyPlants plantsFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.FragmentContainer, fragment)
                    .commit();

            return true;
        }

        return false;
    }


    @Override
    public void onResume() {
        super.onResume();

        IntentFilter intentFilter = new IntentFilter(
                "android.intent.action.Main2Activity");

        mReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                //extract your message from intent
                String msg_for_me = intent.getStringExtra("msgKey");
                //log your message value
                Log.i("SMV from Main2Activity", msg_for_me);
                MyPlants fragment = (MyPlants) getSupportFragmentManager().
                        findFragmentById(R.id.nav_my_plants);

                sendMessage(msg_for_me);
                
            }
        };

        //registering your receiver
        registerReceiver(mReceiver, intentFilter);
    }

    private void sendMessage(String t) {
        Log.d("sender", "Broadcasting message");
        Intent intent = new Intent("custom-event-name");
        // You can also include some extra data.
        intent.putExtra("message", t);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    public String getBluetoothVal(){
        return bluetoothVal;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        Fragment fragment = null;

        switch (menuItem.getItemId()) {
            case R.id.nav_my_plants:
                fragment = new MyPlants();
                break;
            case R.id.nav_history:
                fragment = new DataGraphs();
                break;
            case R.id.nav_learn_more:
                fragment = new LearnMore();
                break;
        }

        return loadFragment(fragment);
    }
}