package com.theprotectors.theprotectors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class ShakeEventManager implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;

    private static final int MOVE_COUNTS = 2;
    private static final int MOVE_THRESHOLD = 4;
    private static final float ALPHA = 0.8F;
    private static final int SHAKE_WINDOW_TIME_INTERVAL = 500; // milliseconds

    // Gravity force on x,y,z axis
    private float gravity[] = new float[3];

    private int counter;
    private long firstMoveTime;
    private ShakeListener listener;

    public ShakeEventManager() {

    }

    public void setListener(ShakeListener listener) {
        this.listener = listener;
    }

    public void init(Context ctx) {
        mSensorManager = (SensorManager) ctx.getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        register();
    }

    public void register() {
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void deregister()  {
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float maxAcc = calcMaxAcceleration(sensorEvent);
        if (maxAcc >= MOVE_THRESHOLD) {
            if (counter == 0) {
                counter++;
                firstMoveTime = System.currentTimeMillis();
            } else {
                long now = System.currentTimeMillis();
                if ((now - firstMoveTime) < SHAKE_WINDOW_TIME_INTERVAL)
                counter++;
                else {
                    resetAllData();
                    counter++;
                    return;
                }

                if (counter >= MOVE_COUNTS)
                if (listener != null)
                    listener.onShake();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {}

    private float calcMaxAcceleration(SensorEvent event) {
        gravity[0] = calcGravityForce(event.values[0], 0);
        gravity[1] = calcGravityForce(event.values[1], 1);
        gravity[2] = calcGravityForce(event.values[2], 2);

        float accX = event.values[0] - gravity[0];
        float accY = event.values[1] - gravity[1];
        float accZ = event.values[2] - gravity[2];

        float max1 = Math.max(accX, accY);
        return Math.max(max1, accZ);
    }

    // Low pass filter
    private float calcGravityForce(float currentVal, int index) {
        return ALPHA * gravity[index] + (1 - ALPHA) * currentVal;
    }

    private void resetAllData() {
        counter = 0;
        firstMoveTime = System.currentTimeMillis();
    }

    public static interface ShakeListener {
        public void onShake();
    }
}
