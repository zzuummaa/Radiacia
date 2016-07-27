package com.example.cntgfy.radiacia.SocketAPI.Radiacia.Game;

/**
 * Created by Cntgfy on 12.07.2016.
 * Объект связан с показаниями навигатора
 */
public interface OnSurfaceOfEarth {
    public void setAccuracy(float accuracy);

    public float getAccuracy();

    public double getLatitude();

    public void setLatitude(double latitude);

    public double getLongitude();

    public void setLongitude(double longitude);

}
