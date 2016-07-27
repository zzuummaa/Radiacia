package com.example.cntgfy.radiacia.SocketAPI.Radiacia;

import com.example.cntgfy.radiacia.Determinant.CoordinateConversion2D;
import com.example.cntgfy.radiacia.Determinant.CoordinateConversion3D;
import com.example.cntgfy.radiacia.SocketAPI.Radiacia.Game.GameObject;
import com.example.cntgfy.radiacia.SocketAPI.Radiacia.Game.Shot;

/**
 * Created by Cntgfy on 08.07.2016.
 */
public class Main_TestShots {
    private static Shot shot;
    private static GameObject gameObject;

    public static void main(String[] args) {
        gameObject = new GameObject(44.88263071, 37.32563325, -35.987503f);
        shot = new Shot(44.88331066, 37.32480864, 124.774605f);

        double distanceBetween = CoordinateConversion3D.distanceBetween(shot.getLatitude(), shot.getLongitude(),
                                                                        gameObject.getLatitude(), gameObject.getLongitude());
        System.out.println("distance between objects: " + distanceBetween);

        double[] gameObjectPosition = CoordinateConversion3D.position(gameObject.getLatitude(), gameObject.getLongitude());
        System.out.println("good direction: " + CoordinateConversion3D.angleOnPlane(shot.getPosition(), gameObjectPosition));

        float deltaAngle  = CoordinateConversion2D.deltaAngle((float) distanceBetween, 9f);
        float deltaAngle2 = CoordinateConversion2D.deltaAngle2((float) distanceBetween, 9f);
        System.out.println("deltaAngle:  " + deltaAngle);
        System.out.println("deltaAngle2: " + deltaAngle2);

        System.out.println(gameObject + " was hitting by " + shot);
        long begin = System.currentTimeMillis();
        printInfoAboutShoots(gameObject, shot, deltaAngle2);
        long end = System.currentTimeMillis();

        if (hitsCounts == 0) System.out.println("shoots not hit to " + gameObject);

        System.out.println("shoots makes: " + shootsCount);
        System.out.println("time wasted: " + (end - begin));
    }

    /**
     * Берет позицию выстрела shoot и пытается стрелять во всех направлениях, пытаяся попать в gameObject
     *
     * @param gameObject объект, по которому стреляют
     * @param shoot выстрел, которым пытаются попасть
     * @param step шаг изменения угла
     */
    private static void printInfoAboutShoots(GameObject gameObject, Shot shoot, float step) {
        for (float direction = -180; direction <= 180; direction+=step) {
            shoot.setDirection(direction);
            printIsHit(gameObject, shoot);
        }
    }

    private static int hitsCounts;
    private static int shootsCount;

    private static void printIsHit(GameObject gameObject, Shot shoot) {
        if (shoot.isHit(gameObject)) {
            System.out.println("shot direction=" + shot.getDirection() + " is hit: true");
            hitsCounts++;
        }
        shootsCount++;
    }
}
