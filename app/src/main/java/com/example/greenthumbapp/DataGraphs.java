package com.example.greenthumbapp;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.widget.EditText;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class DataGraphs extends AppCompatActivity {
    private static final String TAG = "DataGraphs";
    private LineGraphSeries<DataPoint> series1;
    private Button addPoint;
    private EditText mX,mY;
    private ArrayList<XYvalues> pointsArray;
    GraphView plot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_graphs);

        addPoint = (Button) findViewById(R.id.btnAddPt);
        mX = (EditText) findViewById(R.id.numX);
        mY = (EditText) findViewById(R.id.numY);
        plot = (GraphView) findViewById(R.id.graph);
        pointsArray = new ArrayList<>();

        init();
    }

    private void init(){
        series1 = new LineGraphSeries<>();

        addPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!mX.getText().toString().equals("") && !mY.getText().toString().equals("") ){
                    double x = Double.parseDouble(mX.getText().toString());
                    double y = Double.parseDouble(mY.getText().toString());
                    Log.d(TAG, "onClick: Adding a new point. (x,y): (" + x + "," + y + ")" );
                    pointsArray.add(new XYvalues(x,y));
                    init();
                }else {
                    toastMessage("You must fill out both fields!");
                }
            }
        });

        if(pointsArray.size() != 0){
            createGraph();
        }else{
            Log.d(TAG, "onCreate: No data to plot.");
        }

    }

    protected void createGraph(){

        Log.d(TAG, "createGraph: Creating plot.");
        pointsArray = sortArray(pointsArray);

        //add the data to the series
        for(int i = 0;i <pointsArray.size(); i++) {
            try {
                double x = pointsArray.get(i).getX();
                double y = pointsArray.get(i).getY();
                series1.appendData(new DataPoint(x, y), true, 1000);
            } catch (IllegalArgumentException e) {
                Log.e(TAG, "createScatterPlot: IllegalArgumentException: " + e.getMessage());
            }
        }

        //set some properties
        series1.setColor(Color.BLUE);

        //set Scrollable and Scaleable
        plot.getViewport().setScalable(false);
        plot.getViewport().setScalableY(false);
        plot.getViewport().setScrollable(false);
        plot.getViewport().setScrollableY(false);

        //set manual x bounds
        plot.getViewport().setYAxisBoundsManual(true);
        plot.getViewport().setMaxY(150);
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

    /**
     * customizable toast
     * @param message
     */
    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }

    public void goToMyPlants(View v){
        if(v.getId() == R.id.my_plants){
            //handle the click here and make whatever you want
            startActivity(new Intent(DataGraphs.this, MyPlants.class));
        }
    }

    /*public void goToLearnMore(View v){
        if(v.getId() == R.id.learn_more){
            //handle the click here and make whatever you want
            startActivity(new Intent(DataGraphs.this, LearnMore.class));
        }
    }*/
}