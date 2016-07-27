package com.example.cntgfy.radiacia.Determinant.Orientation;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.TextView;

import com.example.cntgfy.radiacia.Determinant.CoordinateConversion3D;

/**
 * Created by Cntgfy on 26.06.2016.
 * Определение ориентации устройства в пространстве в 3-х углах
 * Ссыль на исходный код: http://startandroid.ru/ru/uroki/vse-uroki-spiskom/287-urok-137-sensory-uskorenie-orientatsija.html
 */
public class DeterminantOfOrientationByStartAndroid implements DeterminantOfOrientation {
    SensorManager sensorManager;
    Sensor sensorAccel;
    Sensor sensorMagnet;



    StringBuilder sb = new StringBuilder();

    public DeterminantOfOrientationByStartAndroid(Activity activity) {
        this.sensorManager = (SensorManager) activity.getSystemService(activity.SENSOR_SERVICE);
        sensorAccel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorMagnet = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    @Override
    public void onResume() {
        sensorManager.registerListener(listener, sensorAccel, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(listener, sensorMagnet, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        sensorManager.unregisterListener(listener);
    }

    String format(float values[]) {
        return String.format("%1$.1f\t\t%2$.1f\t\t%3$.1f", values[0], values[1], values[2]);
    }

    @Override
    public void showInfo(TextView textView) {
        getDeviceOrientation();
        sb.setLength(0);
        sb.append("Orientation : " + format(valuesResult))
        ;

        textView.setText(sb);
    }

    float[] r = new float[9];
    float[] valuesResult = new float[3];

    @Override
    public float[] getDeviceOrientation() {
        SensorManager.getRotationMatrix(r, null, valuesAccel, valuesMagnet);
        SensorManager.getOrientation(r, valuesResult);

        valuesResult[0] = (float) Math.toDegrees(valuesResult[0]);
        valuesResult[1] = (float) Math.toDegrees(valuesResult[1]);
        valuesResult[2] = (float) Math.toDegrees(valuesResult[2]);
        return valuesResult;
    }

    float[] valuesAccel = new float[3];
    float[] valuesMagnet = new float[3];

    SensorEventListener listener = new SensorEventListener() {

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            switch (event.sensor.getType()) {
                case Sensor.TYPE_ACCELEROMETER:
                    for (int i=0; i < 3; i++){
                        valuesAccel[i] = event.values[i];
                    }
                    break;
                case Sensor.TYPE_MAGNETIC_FIELD:
                    for (int i=0; i < 3; i++){
                        valuesMagnet[i] = event.values[i];
                    }
                    break;
            }
        }
    };
}
