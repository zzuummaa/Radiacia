package com.example.cntgfy.radiacia.Determinant.Orientation;

import android.widget.TextView;

import com.example.cntgfy.radiacia.InActivityUsable;

/**
 * Created by Cntgfy on 26.06.2016.
 * Определяет поворот устройства в пространстве
 */
public interface DeterminantOfOrientation extends InActivityUsable {
    public void showInfo(TextView textView);

    public float[] getDeviceOrientation();
}
