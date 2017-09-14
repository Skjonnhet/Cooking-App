package com.example.ninah.cooking_app;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

/**
 * Created by Jonas on 11.09.2017.
 */

public class MovingSensor {

    //constants
    //motion_detection values show how big the movements of the devices has to be until motion is detected by the sensor
    private final static int MOTION_DETECTION_X = 8;
    private final static int MOTION_DETECTION_Y = 10;
    private final static int MOTION_DETECTION_Z = 15;

    //axis are the values of the x,y,z -axis of the device
    private final static int X_AXIS = 0;
    private final static int Y_AXIS = 1;
    private final static int Z_AXIS = 2;


   private final Context context;
    private final SensorEventListener listener;
    private SensorManager manager;
    private Sensor sensor;


    //constructor of the MovingSensor
    //needs a context and a listener
    public MovingSensor(Context context, SensorEventListener listener) {
        this.context = context;
        this.listener = listener;
        init();
    }

    //inits the SensorManager and the Sensor through the context
    //context was given in the constructor
    private void init() {
        manager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensor = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    //registers the listener and senor via the SensorManager
    //listener was given in the constructor
    public void start() {
        manager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_UI);
        Log.d("MovingSensor", "started");
    }

    //unregisters the listener via the SensorManager
    public void stop() {

        manager.unregisterListener(listener);
        Log.d("MovingSensor", "stopped");
    }

    //controls if the device has been moved in x,y or z direction
    //if so, returns true, else false
    public boolean hasBeenMoved(final float[] values) {

        if (values[X_AXIS]>MOTION_DETECTION_X ||values[Y_AXIS] > MOTION_DETECTION_Y||values[Z_AXIS] > MOTION_DETECTION_Z) {
            return true;
        }
        return false;
    }
}
