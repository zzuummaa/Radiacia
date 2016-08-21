package com.example.cntgfy.radiacia;

import android.app.Activity;

import com.example.cntgfy.radiacia.Determinant.CoordinateConversion3D;
import com.example.cntgfy.radiacia.Determinant.Orientation.DeterminantOfOrientation;
import com.example.cntgfy.radiacia.Determinant.Orientation.DeterminantOfOrientationByStartAndroid;
import com.example.cntgfy.radiacia.Determinant.Position.DeterminantOfPosition;
import com.example.cntgfy.radiacia.Determinant.Position.DeterminantOfPositionByStartAndroid;

import Radiacia.server.client.ClientGamer;

/**
 * Created by Cntgfy on 05.07.2016.
 * реализует обновление информации о позиции и угле поворота объекта класса GameObject
 * использует объект класса Activity для получения информации о положении в пространстве
 */
public class AttitudeUpdater implements InActivityUsable {

    private DeterminantOfPosition detOfPos;
    private DeterminantOfOrientation detOfOrient;

    private UpdateThread updateThread;
    private SendToServerThread sendToServerThread;

    private volatile ClientGamer cg;

    private float goodAccuracy = 20;

    private int fps;

    public AttitudeUpdater(MainActivity activity, SendToServerThread sendToServerThread) {
        this(activity, (ClientGamer)null);

        this.sendToServerThread = sendToServerThread;
    }

    public AttitudeUpdater(MainActivity activity, ClientGamer cg) {
        this(activity, cg, 1);
    }

    public AttitudeUpdater(MainActivity activity, int fps) {
        this(activity, null, fps);
    }

    public AttitudeUpdater(MainActivity activity, ClientGamer cg, int fps) {
        this.cg = cg;

        this.fps = fps;
        detOfPos = new DeterminantOfPositionByStartAndroid(activity);
        detOfOrient = new DeterminantOfOrientationByStartAndroid(activity);
    }

    private class UpdateThread extends Thread {
        private boolean isCancelled = false;

        @Override
        public void run() {

            while (!isCancelled()) {
                synchronized (this) {
                    if (cg == null) continue;

                    sendToServerThread.add(new Runnable() {
                        @Override
                        public void run() {
                            cg.setDirection(direction(detOfOrient.getDeviceOrientation()));

                            if (detOfPos.getAccuracy() < goodAccuracy) {
                                cg.setLatitude(detOfPos.getLatitude());
                                cg.setLongitude(detOfPos.getLongitude());
                            }

                            cg.writeSelf();
                            //System.out.println("write self: " + cg);
                        }
                    });


                    //debug.printDebugLog("UpdateAsyncTask set attitude");
                }
                try {
                    Thread.sleep(1000 / fps);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
            }
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

    public void setClientGamer(ClientGamer cg) {
        this.cg = cg;
    }

    public ClientGamer getClientGamer() {
        return cg;
    }

    @Override
    public void onResume() {
        detOfPos.onResume();
        detOfOrient.onResume();

        updateThread = new UpdateThread();
        updateThread.start();

    }

    @Override
    public void onPause() {
        updateThread.cancel();

        detOfPos.onPause();
        detOfOrient.onPause();

    }

    public DeterminantOfPosition getDetOfPos() {
        return detOfPos;
    }

    public DeterminantOfOrientation getDetOfOrient() {
        return detOfOrient;
    }

}
