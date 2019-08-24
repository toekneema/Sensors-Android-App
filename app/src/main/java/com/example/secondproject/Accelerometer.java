package com.example.secondproject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.util.ArrayList;
import java.util.Random;

public class Accelerometer extends AppCompatActivity implements SensorEventListener {

    private static final String TAG = "MainActivity";
    SensorManager sensorManager;
    Sensor accelerometer;

    CustomView cv;

    ArrayList<Float> values = new ArrayList<Float>(7);
    ArrayList<Float> means = new ArrayList<Float>(7);
    ArrayList<Float> stds = new ArrayList<Float>(7);

    ImageView animationAccelerometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometer);

        animationAccelerometer = (ImageView) findViewById(R.id.animationAccelerometer);

        for(int i=0;i<7;i++) {
            values.add(0.0f);
            means.add(0.0f);
            stds.add(0.0f);
        }

        Log.d(TAG, "onCreate: Initializing Sensor Services");
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        Log.d(TAG, "onCreate: Registered accelerometer listener");

        cv = (CustomView) findViewById(R.id.customview);
    }

    public void goBack(View v) {
        onBackPressed();
    }

    @Override
    public void onSensorChanged(SensorEvent event) { //similar to Foo function from classwork 3
        float one = event.values[0];
        float two = event.values[1];
        float three = event.values[2];

        CustomView cv = (CustomView) findViewById(R.id.customview);
        //cv.addPoint(one, two, three);
        this.addPoint(one,two,three);
        cv.sendAndReceiveData(values, means, stds);
        changeAccelerometerAnimation();
        cv.plotData();
        cv.invalidate();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void addPoint(Float one, Float two, Float three) {
        float value = (float) Math.sqrt(one*one + two*two + three*three);
        values.add(value);
        if (values.size() > 7) {
            values.remove(0);
        }

        float mean = (values.get(2) + values.get(3) + values.get(4) + values.get(5) + values.get(6))/ 5.0f;
        means.add(mean);
        if (means.size() > 7) {
            means.remove(0);
        }

        float temp = 0;
        for(float a : values)
            temp += (a-mean)*(a-mean);
        float std = (float)Math.sqrt(temp/6.0f);
        stds.add(std);
        if (stds.size() > 7) {
            stds.remove(0);
        }
    }

    private void changeAccelerometerAnimation() {
        if (means.get(6) > 20) {
            animationAccelerometer.setBackgroundResource(R.drawable.bulbon);
        } else {
            animationAccelerometer.setBackgroundResource(R.drawable.bulboff);
        }
    }
}
