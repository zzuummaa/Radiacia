package com.example.cntgfy.radiacia.Determinant;

/**
 * Created by Cntgfy on 11.07.2016.
 */
public class CoordinateConversion2D {
    /*
    * Вычисление длины двухмерного вектора
    * */
    private double abs(double x, double y) {
        return Math.sqrt(x * x + y * y);
    }

    /**
     * Вычисление sin угла между двухмерными векторами а и b
     *
     * @param aX координата х вектора а
     * @param aY координата у вектора а
     * @param bX координата х вектора b
     * @param bY координата у вектора b
     *
     * @return sin угла между векторами а и b
     */
    private double sin(double aX, double aY, double bX, double bY) {
        return vecComposition(aX, aY, bX, bY)/( abs(bX, bY)*abs(aX, aY) );
    }

    /*
    * вычисление векторного произведения двухмерных векторов а и b
    * */
    private double vecComposition(double aX, double aY, double bX, double bY) {
        return aX*bY - bX*aY;
    }


    /**
     * Находит максимальное отклонение угла направления первого объекта
     * от идеального направления на второй объект.
     *
     * @param distance расстояние между объектами.
     * @param size радиус второго объекта.
     *
     * @return значение максимального угла отклонения в градусах [0; 180], при котором первый объект
     * еще попадает во второй.
     */
    public static float deltaAngle(float distance, float size) {
        double deltaAngle = Math.abs(Math.asin(size / distance));

        return (float) Math.toDegrees(deltaAngle);
    }

    /**
     * Находит максимальное отклонение угла направления первого объекта
     * от идеального направления на второй объект.
     *
     * @param distance расстояние между объектами.
     * @param size радиус второго объекта.
     *
     * @return значение максимального угла отклонения в градусах [0; 180], при котором первый объект
     * еще попадает во второй.
     */
    public static float deltaAngle2(float distance, float size) {
        double deltaAngle = Math.abs(Math.asin(size / (distance/2) ));

        return (float) Math.toDegrees(deltaAngle);
    }

    /**
     * Находит угол поворота вектора, проведенного из точки а в точку b
     *
     * @param aX координата х точки а
     * @param aY координата у точки а
     * @param bX координата х точки b
     * @param bY координата y точки b
     *
     * @return угол поворота вектора в градусах [-180; 180]
     */
    public static float angle(double aX, double aY, double bX, double bY) {
        double x = bX - aX;
        double y = bY - aY;

        return (float) Math.toDegrees(Math.atan2(y, x));
    }
}
