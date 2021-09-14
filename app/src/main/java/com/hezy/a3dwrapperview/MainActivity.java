package com.hezy.a3dwrapperview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.hezy.sensorview.SensorView;

public class MainActivity extends AppCompatActivity {
    private SensorView sensorView;
    private SensorView sensorView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorView = (SensorView) findViewById(R.id.sensor_view);
        sensorView2 = (SensorView) findViewById(R.id.sensor_view2);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sensorView.unregister();
        sensorView2.unregister();
    }
}