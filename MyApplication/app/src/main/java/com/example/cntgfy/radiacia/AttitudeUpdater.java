package com.example.cntgfy.radiacia;

import android.app.Activity;

import com.example.cntgfy.radiacia.Determinant.CoordinateConversion3D;
import com.example.cntgfy.radiacia.Determinant.Orientation.DeterminantOfOrientation;
import com.example.cntgfy.radiacia.Determinant.Orientation.DeterminantOfOrientationByStartAndroid;
import com.example.cntgfy.radiacia.Determinant.Position.DeterminantOfPosition;
import com.example.cntgfy.radiacia.Determinant.Position.DeterminantOfPositionByStartAndroid;
import com.example.cntgfy.radiacia.SocketAPI.Radiacia.Debug.Debug;
import com.example.cntgfy.radiacia.SocketAPI.Radiacia.Game.GameObject;

/**
 * Created by Cntgfy on 05.07.2016.
 * реализует обновление информации о позиции и угле поворота объекта класса GameObject
 * использует объект класса Activity для получения информации о положении в пространстве
 */
public class AttitudeUpdater implements InActivityUsable {
    private DeterminantOfPosition detOfPos;
    private DeterminantOfOrientation detOfOrient;

    private UpdateThread updateThread;

    private GameObject gameObject;

    private Debug debug = new Debug("ATTITUDE_UPDATER", 0);
    private float goodAccuracy = 20;

    public AttitudeUpdater(Activity activity) {
        gameObject = new GameObject();
        detOfPos = new DeterminantOfPositionByStartAndroid(activity);
        detOfOrient = new DeterminantOfOrientationByStartAndroid(activity);
    }

    public AttitudeUpdater(GameObject gameObject, Activity activity) {
        this.gameObject = gameObject;
        detOfPos = new DeterminantOfPositionByStartAndroid(activity);
        detOfOrient = new DeterminantOfOrientationByStartAndroid(activity);
    }

    private class UpdateThread extends Thread {
        private boolean isCancelled = false;

        @Override
        public void run() {
            debug.printDebugLog("UpdateThread started");

            while (!isCancelled()) {
                if (gameObject == null) continue;

                gameObject.setDirection(direction(detOfOrient.getDeviceOrientation()));

                if (detOfPos.getAccuracy() < goodAccuracy) {
                    gameObject.setLatitude(detOfPos.getLatitude());
                    gameObject.setLongitude(detOfPos.getLongitude());
                }
                //debug.printDebugLog("UpdateAsyncTask set attitude");
                try {
                    Thread.sleep(150);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            debug.printDebugLog("UpdateAsyncTask is cancelled");
        }

        public boolean isCancelled() {
            return isCancelled;
        }

        public void cancel() {
            isCancelled = true;
        }
    }

    private static CoordinateConversion3D c3D = new CoordinateConversion3D();
    private static final double[]  normalOfPlane = new double[]{0, 0, 1d};
    private static final double[] zeroAngleOnPlane = new double[]{0, 1d, 0};
    /**
     * Определяем направление в плоскости, параллельной полу
     *
     * @param orientation углы поворота в трех плоскостях в градусах [-180; 180]
     *                    orientation[0] - z
     *                    orientation[1] - x
     *                    orientation[2] - y
     * @return угол поворота в плоскост, параллельной полу в градусах [-180; 180]
     */
    public static float direction(float[] orientation) {
        return (orientation[1] > -90 & orientation[1] < 90) ? orientation[0] : -orientation[0];
    }

    /**
     * Переводит углы ориентации в вектор направления
     */
    /*public static double[] rotationVector(float[] orientation) {
        double[] rotationVector = new double[3];
        rotationVector[0] = Math.cos(orientation[0]);
        rotationVector[1] = Math.sin(orientation[0]);
        rotationVector[2] = Math.sin(orientation[2]) > 0 ? Math.cos(orientation[2])
                : -Math.cos(orientation[2]);

        return rotationVector;
    }*/

    public void setGameObject(GameObject gameObject) {
        this.gameObject = gameObject;
    }

    public GameObject getGameObject() {
        return gameObject;
    }

    @Override
    public void onResume() {
        detOfPos.onResume();
        detOfOrient.onResume();

        updateThread = new UpdateThread();
        updateThread.start();

        debug.printDebugLog("onResume");
    }

    @Override
    public void onPause() {
        updateThread.cancel();

        detOfPos.onPause();
        detOfOrient.onPause();

        debug.printDebugLog("onPause");
    }

    public DeterminantOfPosition getDetOfPos() {
        return detOfPos;
    }

    public DeterminantOfOrientation getDetOfOrient() {
        return detOfOrient;
    }

}
