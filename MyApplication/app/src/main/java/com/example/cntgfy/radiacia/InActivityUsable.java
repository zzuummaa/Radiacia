package com.example.cntgfy.radiacia;

import android.app.Activity;

/**
 * Created by Cntgfy on 28.06.2016.
 *
 * Используется в методах класса Activity
 *
 * Желательно Вызвать методы данного интерфейса в аналогичных методах Activity
 * для корректной работы класса, реализующего данный интерфейс
 */
public interface InActivityUsable {
    //public void onCreate(Activity activity);

    public void onResume();

    public void onPause();
}
