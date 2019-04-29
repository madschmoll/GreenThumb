package com.example.greenthumbapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

public class MyPlants extends Fragment {
    private TabLayout tabs;
    private BroadcastReceiver mReceiver;
    TextView bluetoothVal;
    View rootView;

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("message");
            Log.d("receiver", "Got message: " + message);
            bluetoothVal.setText(message);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_my_plants, container, false);
        bluetoothVal = rootView.findViewById(R.id.textView);

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-event-name"));

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_plants, container, false);

    }

    public void updateSMVal(String t){
        bluetoothVal.setText(t);
    }

    @Override
    public void onResume() {
        super.onResume();

        IntentFilter intentFilter = new IntentFilter(
                "android.intent.action.MyPlants");

        mReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                //extract your message from intent
                String msg_for_me = intent.getStringExtra("msgKey");
                //log your message value
                Log.i("SMV from Plants", msg_for_me);
                bluetoothVal.setText(msg_for_me);
            }
        };

        //registering your receiver
        getActivity().registerReceiver(mReceiver, intentFilter);
    }


//    @Override
//    public void onResume() {
//        super.onResume();
//
//        IntentFilter intentFilter = new IntentFilter(
//                "android.intent.action.MyPlants");
//
//        mReceiver = new BroadcastReceiver() {
//
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                //extract your message from intent
//                bluetoothVal = getActivity().getBluetoothVal();
//                String msg_for_me = intent.getStringExtra("msgKey");
//                //log your message value
//                Log.i("SMV from MyPlants", msg_for_me);
//
//                bluetoothVal.setText(msg_for_me);
//            }
//        };
//
//        //registering your receiver
//
//        getActivity().registerReceiver(mReceiver, intentFilter);
//    }
//
//
}
