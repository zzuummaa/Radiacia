package com.example.cntgfy.radiacia.Determinant.Position;

import android.location.Location;
import android.widget.TextView;
import com.example.cntgfy.radiacia.InActivityUsable;

/**
 * Created by Cntgfy on 28.06.2016.
 * Определяет координаты устройства
 */
public interface DeterminantOfPosition extends InActivityUsable {
    public void showLocation(TextView textView);

    //Широта
    public double getLatitude();

    //Долгота
    public double getLongitude();

    //Точность
    public float getAccuracy();

    //public Location getLocation();

    public boolean isProviderEnabled();
}
