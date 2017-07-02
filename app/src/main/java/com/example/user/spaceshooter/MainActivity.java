package com.example.user.spaceshooter;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class MainActivity extends Activity {

    //Gyroscope Sensors
    private SensorManager sm;
    private Sensor gs;
    private SensorEventListener eventListener;

    //Global variables
    public static int px = 150, py = 150;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(new GamePanel(this));

        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        gs = sm.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        if(gs == null) {
            Toast.makeText(this, "Device Has no Gyroscope", Toast.LENGTH_SHORT).show();
            finish();
        }
        eventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent e) {
                if(e.values[0] < -0.5f){//up
                    py -= 1;
                }
                if(e.values[1] > 0.5f){//right
                    px += 2;
                }
                if(e.values[1] < -0.5f){//left
                    px -= 2;
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
    }

    @Override
    protected void onResume(){
        super.onResume();
        sm.registerListener(eventListener, gs, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    protected  void onPause(){
        super.onPause();
        sm.unregisterListener(eventListener);
    }
}
