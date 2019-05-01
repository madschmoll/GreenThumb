package com.example.greenthumbapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.view.View;
import android.widget.EditText;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import android.util.Log;

import java.util.TimerTask;
import java.util.Timer;
import java.util.ArrayList;

public class DataGraphs extends Fragment {
    private static final String TAG = "DataGraphs";
    private LineGraphSeries<DataPoint> series1;
    private Button addPoint;
    private EditText mX;
    private ArrayList<XYvalues> pointsArray;
    GraphView plot;
    private String SMV;

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("message");
            Log.d("receiver", "Got message: " + message);
            SMV = message;
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_data_graphs, container, false);
        addPoint = (Button) rootView.findViewById(R.id.btnAddPt);

        plot = (GraphView) rootView.findViewById(R.id.graph);
        pointsArray = new ArrayList<>();

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-event-name"));

        init(1);

        return rootView;
    }

    private void init(final double xVal){
        series1 = new LineGraphSeries<>();

        addPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Timer timer = new Timer();
                timer.schedule(new AddPoint(1), 0, 100);
            }
        });

    }

    private double getY(){
        if(SMV!=null){
            return Double.parseDouble(SMV);
        }
        else {
            Log.d(TAG, "getY: NOT WORKING");
            return -1;
        }
    }

    class AddPoint extends TimerTask {
        private double currX;
        private double prevX;

        AddPoint(double x){
            this.currX = x;
            this.prevX = 0;
        }

        public void run() {
            double y = getY();
            double xOld = this.prevX;
            double x = this.currX;
            Log.d(TAG, "onClick: Adding a new point. (x,y): (" + x + "," + y + ")" );
            if(xOld < x) {
                pointsArray.add(new XYvalues(x, y));
                Log.d(TAG, "SIZE OF ARRAY :: " + (pointsArray.size()));
            }
            prevX = currX;
            currX += 2;


            if(pointsArray.size() != 0){
                createGraph();
            }else{
                Log.d(TAG, "onCreate: No data to plot.");
            }
        }
    }

    protected void createGraph(){

        Log.d(TAG, "createGraph: Creating plot.");
        pointsArray = sortArray(pointsArray);
        int maxPoints = Integer.MAX_VALUE;
        //add the data to the series

        for(int i = 0;i <pointsArray.size(); i++) {
            try {
                double x = pointsArray.get(i).getX();
                double y = pointsArray.get(i).getY();
                series1.appendData(new DataPoint(x, y), true, maxPoints);
            } catch (IllegalArgumentException e) {
                Log.e(TAG, "createScatterPlot: IllegalArgumentException: " + e.getMessage());
            }
        }

        //set some properties
        series1.setColor(Color.rgb(237,65,106));

        //set Scrollable and Scaleable
        plot.getViewport().setScalable(false);
        plot.getViewport().setScalableY(false);
        plot.getViewport().setScrollable(false);
        plot.getViewport().setScrollableY(true);

        //set manual x bounds
        plot.getViewport().setYAxisBoundsManual(true);
        plot.getViewport().setMaxY(200);
        plot.getViewport().setMinY(0);

        //set manual y bounds
        plot.getViewport().setXAxisBoundsManual(true);
        plot.getViewport().setMaxX(150);
        plot.getViewport().setMinX(0);

        plot.addSeries(series1);

    }



    /**
     * Sorts an ArrayList<XYValue> with respect to the x values.
     * @param array
     * @return
     */
    private ArrayList<XYvalues> sortArray(ArrayList<XYvalues> array){
        /*
        //Sorts the xyValues in Ascending order to prepare them for the PointsGraphSeries<DataSet>
         */
        int factor = Integer.parseInt(String.valueOf(Math.round(Math.pow(array.size(),2))));
        int m = array.size() - 1;
        int count = 0;
        Log.d(TAG, "sortArray: Sorting the XYArray.");


        while (true) {
            m--;
            if (m <= 0) {
                m = array.size() - 1;
            }
            Log.d(TAG, "sortArray: m = " + m);
            try {
                //print out the y entrys so we know what the order looks like
                //Log.d(TAG, "sortArray: Order:");
                //for(int n = 0;n < array.size();n++){
                //Log.d(TAG, "sortArray: " + array.get(n).getY());
                //}
                double tempY = array.get(m - 1).getY();
                double tempX = array.get(m - 1).getX();
                if (tempX > array.get(m).getX()) {
                    array.get(m - 1).setY(array.get(m).getY());
                    array.get(m).setY(tempY);
                    array.get(m - 1).setX(array.get(m).getX());
                    array.get(m).setX(tempX);
                } else if (tempX == array.get(m).getX()) {
                    count++;
                    Log.d(TAG, "sortArray: count = " + count);
                } else if (array.get(m).getX() > array.get(m - 1).getX()) {
                    count++;
                    Log.d(TAG, "sortArray: count = " + count);
                }
                //break when factorial is done
                if (count == factor) {
                    break;
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                Log.e(TAG, "sortArray: ArrayIndexOutOfBoundsException. Need more than 1 data point to create Plot." +
                        e.getMessage());
                break;
            }
        }
        return array;
    }


}