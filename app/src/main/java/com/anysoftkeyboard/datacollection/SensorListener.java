package com.anysoftkeyboard.datacollection;


import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.anysoftkeyboard.base.utils.Logger;

import java.util.ArrayList;

public class SensorListener implements SensorEventListener {
    private static final String TAG = "SensorListener";
    private SensorManager sensorManager;
    private Sensor rotationSensor;
    private Sensor accSensor;
    private ArrayList<RateOfRotation> rateOfRotation;
    private ArrayList<Acceleration> acceleration;
    private int rate;


    public SensorListener(SensorManager sensorManager, int rate) {
        Logger.v(TAG, "dc-- SensorListener constructor..");
        this.sensorManager = sensorManager;
        rotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        accSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        rateOfRotation = new ArrayList<>();
        acceleration = new ArrayList<>();
        this.rate = rate;
    }

    public void start() {
        Logger.v(TAG, "dc-- SensorListener start..");
//        enable = true;
        sensorManager.registerListener(this, rotationSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, accSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void stop() {
        Logger.v(TAG, "dc-- SensorListener stop..");
//        enable = false;
        if (sensorManager != null) {
            sensorManager.unregisterListener(this, rotationSensor);
            sensorManager.unregisterListener(this, accSensor);
            sensorManager = null;
        }
    }

    public ArrayList<RateOfRotation> getRateOfRotation() {
        return rateOfRotation;
    }

    public void setRateOfRotation(ArrayList<RateOfRotation> rateOfRotation) {
        this.rateOfRotation = rateOfRotation;
    }

    public ArrayList<Acceleration> getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(ArrayList<Acceleration> acceleration) {
        this.acceleration = acceleration;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        float x = sensorEvent.values[0];
        float y = sensorEvent.values[1];
        float z = sensorEvent.values[2];
        Sensor sensor = sensorEvent.sensor;

        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            Acceleration acc = new Acceleration(x, y, z);
            acceleration.add(acc);
            Logger.v(TAG, "Acceleration=%s", acc);
        }

        if (sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            RateOfRotation rotation = new RateOfRotation(x, y, z);
            rateOfRotation.add(rotation);
            Logger.v(TAG, "rotation=%s", rotation);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
