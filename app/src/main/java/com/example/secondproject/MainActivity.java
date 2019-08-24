package com.example.secondproject;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    Button b1;
    Button b2;
    TextView t1;
    TextView t2;

    Float[] aInfo = new Float[3];
    Float[] lInfo = new Float[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getAccelerometerInfo();
        b1 = (Button) findViewById(R.id.b1);
        b1.setBackgroundColor(Color.YELLOW);
        t1 = (TextView) findViewById(R.id.t1);
        t1.setText("Status: Accelerometer is present" +
                ", Range: " + aInfo[0] + ", Resolution: " + aInfo[1] + ", Delay: " + aInfo[2]);

        getLightInfo();
        b2 = (Button) findViewById(R.id.b2);
        b2.setBackgroundColor(Color.GREEN);
        t2 = (TextView) findViewById(R.id.t2);
        t2.setText("Status: Light Sensor is present" +
                ", Range: " + lInfo[0] + ", Resolution: " + lInfo[1] + ", Delay: " + lInfo[2]);



    }
    public void newActivity1 (View view) {
        Intent intent = new Intent(this, Accelerometer.class);
        startActivity(intent);
    }

    public void newActivity2 (View view) {
        Intent intent = new Intent(this, Light.class);
        startActivity(intent);
    }

    private void getAccelerometerInfo() {
        SensorManager sensorManager = (SensorManager) getSystemService(Service.SENSOR_SERVICE);
        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        aInfo[0] = accelerometer.getMaximumRange();
        aInfo[1] = accelerometer.getResolution();
        aInfo[2] = (float) accelerometer.getMinDelay();
    }

    private void getLightInfo() {
        SensorManager sensorManager = (SensorManager) getSystemService(Service.SENSOR_SERVICE);
        Sensor light = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        lInfo[0] = light.getMaximumRange();
        lInfo[1] = light.getResolution();
        lInfo[2] = (float) light.getMinDelay();
    }
}
