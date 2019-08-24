package com.example.secondproject;

import android.app.Service;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;

public class Light extends AppCompatActivity implements SensorEventListener {

    SensorManager sensorManager;
    Sensor light;

    CustomView2 cv;

    ArrayList<Float> values = new ArrayList<Float>(7);
    ArrayList<Float> means = new ArrayList<Float>(7);
    ArrayList<Float> stds = new ArrayList<Float>(7);

    ImageView animationLight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light);

        animationLight = (ImageView) findViewById(R.id.animationLight);

        for(int i=0;i<7;i++) {
            values.add(0.0f);
            means.add(0.0f);
            stds.add(0.0f);
        }

        sensorManager = (SensorManager) getSystemService(Service.SENSOR_SERVICE);
        light = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        cv = (CustomView2) findViewById(R.id.customview2);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, light, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent event) { //change logic to just be the x value, or index 0
        float one = event.values[0];

        CustomView2 cv = (CustomView2) findViewById(R.id.customview2);
        this.addPoint(one);
        cv.sendAndReceiveData(values, means, stds);
        changeLightAnimation();
        cv.plotData();
        cv.invalidate();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void goBack(View v) {
        onBackPressed();
    }

    public void addPoint(Float one) {
        float value = one;
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

    private void changeLightAnimation() {
        if (means.get(6) > 400) {
            animationLight.setBackgroundResource(R.drawable.batteryhigh);
        } else if (means.get(6) > 200) {
            animationLight.setBackgroundResource(R.drawable.batterymedium);
        } else if (means.get(6) > 0){
            animationLight.setBackgroundResource(R.drawable.batterylow);
        }
    }
}
