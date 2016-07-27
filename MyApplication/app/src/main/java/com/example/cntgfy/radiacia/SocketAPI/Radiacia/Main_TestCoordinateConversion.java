package com.example.cntgfy.radiacia.SocketAPI.Radiacia;

import com.example.cntgfy.radiacia.Determinant.CoordinateConversion3D;

/**
 * Created by Cntgfy on 11.07.2016.
 */
public class Main_TestCoordinateConversion {
    public static void main(String[] args) {
        System.out.println("TEST COORDINATE_CONVERSION");
        System.out.println("-----------------------------------------------------------------------------------------");

        System.out.println("Composition:");
        double[] a = {3, 5, 4};
        double[] b = {2, 1, 3};

        double[] c1 = {11, -1, 7};
        double[] c2 = CoordinateConversion3D.vecComposition(a, b);

        System.out.println("expected values: " + format(c1));
        System.out.println("counted values:  " + format(c1));
        System.out.println("-----------------------------------------------------------------------------------------");

        System.out.println("Projection on plane:");
        double[] a1 = {2, -4, 1};
        double[] n  = {0, 1, 1};

        n = CoordinateConversion3D.normalize(n);

        double[] b1 = CoordinateConversion3D.projectionOnPlane(n, a1);
        System.out.println("counted values:  " + format(b1));
        float angle1 = CoordinateConversion3D.angle(b1, a1);
        System.out.println("angle between b1 and a1: " + angle1);
        float angle2 = CoordinateConversion3D.angle(a1, n);
        System.out.println("angle between a1 and n: " + angle2);
        System.out.println("sum angles: " + (angle2+angle1));
        System.out.println("-----------------------------------------------------------------------------------------");

        System.out.println("Angle on plain");
    }

    private static String format(double[] vec) {
        return "x=" + vec[0] + " y=" + vec[1] + " z=" + vec[2];
    }
}
