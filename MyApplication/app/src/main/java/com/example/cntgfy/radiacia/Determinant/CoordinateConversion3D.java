package com.example.cntgfy.radiacia.Determinant;

/**
 * Created by Cntgfy on 10.07.2016.
 */
public class CoordinateConversion3D {

    /**
     * Находит угол между 3-х мерными векторами
     *
     * @return угол в градусах [-180; 180]
     */
    public static float angle(double[] vec1, double[] vec2) {
        float aCos = (float) Math.toDegrees(Math.acos(cos(vec1, vec2)));

        return Math.asin(sin(vec1, vec2)) > 0 ? aCos : -aCos;
    }

    //Радиус Земли в метрах
    public static final int EARTH_RADIUS = 6371 * 1000;

    /**
     * Вычисляет расстояние между двумя точками, переводя геоцентрические координаты
     * 2-х точен в 3-х мерные декартовы и находя модуль вектора из ондой точки во вторую.
     *
     * @return расстояние между точками в метрах
     */
    public static double distanceBetween(double latitude1, double longitude1, double latitude2, double longitude2) {
        return abs(vectorsDifference( position(latitude1, longitude1), position(latitude2, longitude2) ));
    }

    /**
     * Вычисление длины трехмерного вектора
     */
    public static double abs(double x, double y, double z) {
        return Math.sqrt(x * x + y * y + z * z);
    }

    /**
     * Вычисление длины трехмерного вектора
     *
     * @param vec vec[0] - x
     *            vec[1] - y
     *            vec[2] - z
     */
    public static double abs(double[] vec) {
        return Math.sqrt(vec[0]*vec[0] + vec[1]*vec[1] + vec[2]*vec[2]);
    }

    /**
     * Преобразует значения широты и долготы в позицию в к.с., связанной с Землей.
     *
     * @return 3-х мерный вектор в к.с., связанной с Землей. Центр к.с. находится в центре Земли
     *         position[0] - x
     *         position[1] - y
     *         position[2] - z
     */
    public static double[] position(double latitude, double longitude) {
        double[] pos = new double[3];

        pos[0] = EARTH_RADIUS * Math.cos(Math.toRadians(longitude));
        pos[1] = EARTH_RADIUS * Math.sin(Math.toRadians(longitude));
        pos[2] = EARTH_RADIUS * Math.sin(Math.toRadians(latitude));

        return pos;
    }

    /**
     * Вектрное произведене двух векторов
     *
     * Считает определитель матрицы:
     * |i  j  k |
     * |x1 y1 z1|
     * |x2 y2 z2|
     *
     * @param vec1 3-х мерный вектор
     * @param vec2 3-х мерный вектор
     *
     * @return 3-х мерный вектор, результат векторного произведения
     *         composition[0] - x
     *         composition[0] - y
     *         composition[0] - z
     */
    public static double[] vecComposition(double[] vec1, double[] vec2) {
        double[] composition = new double[3];

        composition[0] = vec1[1]*vec2[2] - vec1[2]*vec2[1];
        composition[1] = vec1[2]*vec2[0] - vec1[0]*vec2[2];
        composition[2] = vec1[0]*vec2[1] - vec1[1]*vec2[0];

        return composition;
    }

    /**
     * Находит sin угла между трехмерными векторами
     *
     * @return синус угла в радианах [-pi/2; pi/2]
     */
    public static double sin(double[] vec1, double[] vec2) {
        return abs(vecComposition(vec1, vec2) )/( abs(vec1) * abs(vec2) );
    }

    /**
     * Скалярное произведение двух 3-х мерных векторов
     *
     * @return число, резултат скалярного произведения
     */
    public static double scalarComposition(double[] vec1, double[] vec2) {
        return vec1[0]*vec2[0] + vec1[1]*vec2[1] + vec1[2]*vec2[2];
    }

    /**
     * Находит проекцию 3-х мерного вектора на плоскость.
     *
     * Вместо нормали можно передать направляющий вектор не единичной длины.
     * Тогда направление проекции вектора будет правильное, но длина будет не верной.
     *
     * @param normalOfPlane нормаль к плоскости, не nul
     * @param vector вектор, который надо спроецировать, не null
     * @return координаты проекции вектора
     *         projectionOnPlane[0] - x
     *         projectionOnPlane[1] - y
     *         projectionOnPlane[2] - z
     */
    public static double[] projectionOnPlane(double[] normalOfPlane, double[] vector) {
        //Вектор проецируется повернутым на 90 градусов в плоскости проекции
        double[] vec2 = vecComposition(vector, normalOfPlane);
        //Поворачиваем вектор на 90 градусов
        return vecComposition(normalOfPlane, vec2);
    };

    /**
     * Преобразует массивы типа float в массивы double и вычиляет проекцию на плоскость
     * @see #projectionOnPlane(double[], double[]) - справка по входным и выходному параметрам
     */
    public static double[] projectionOnPlane(float[] normalOfPlane, float[] vector) {
        double[] doubleNormalOfPlane = new double[3];
        double[] doubleVector = new double[3];

        for (int i = 0; i < 3; i++) {
            doubleNormalOfPlane[i] = normalOfPlane[i];
            doubleVector[i] = vector[i];
        }

        return projectionOnPlane(doubleNormalOfPlane, doubleVector);
    }

    /**
     * Возвращает угол поворота проекции вектора в касательной плоскости в точке.
     * Угол считается от направления магнитного поля
     *
     * @param point точка на сфере, проведенной из (0;0;0) с радиусом Земли
     * @param vector вектор
     *
     * @return угол поворота вектора в касательной к сфере плоскости в градусах
     */
    public static float angleOnPlane(double[] point, double[] vector) {
        //Это просто повернутый на 180 градусов point
        double[] normal = normalize(vectorsDifference(new double[]{0, 0, 0}, point));

        double[] projectionVector = projectionOnPlane(normal, vector);

        //Начало отсчета угла на плоскости
        double[] magnetVector = {0, 0, EARTH_RADIUS};
        magnetVector = projectionOnPlane(normal, magnetVector);

        return angle(magnetVector, projectionVector);
    }

    /**
     * Находит cos угла между векторами
     *
     * @return косинус угла в радианах [0; pi]
     */
    public static double cos(double[] vec1, double[] vec2) {
        return scalarComposition(vec1, vec2)/( abs(vec1) * abs(vec2) );
    }

    /**
     * Вычитает один вектор из другого
     *
     * @return vec1 - vec2
     */
    public static double[] vectorsDifference(double[] vec1, double[] vec2) {
        double[] vec = new double[3];

        vec[0] = vec1[0] - vec2[0];
        vec[1] = vec1[1] - vec2[1];
        vec[2] = vec1[2] - vec2[2];

        return vec;
    }

    /**
     * Нормализует вектор, делая его длину = 1
     */
    public static double[] normalize(double[] vector) {
        double length = abs(vector);

        vector[0] = vector[0]/length;
        vector[1] = vector[1]/length;
        vector[2] = vector[2]/length;

        return vector;
    }

    /**
     * Переводит систему координат класса DeterminantOfOrientation
     * в систему координат класса DeterminantOfPosition
     *
     * @param vec вектор в с.к DeterminantOfOrientation
     * @param latitude
     * @param longitude
     * @return вектор в с.к DeterminantOfPosition
     */
    public static double[] orientVecToPosVec(float[] vec, double latitude, double longitude) {
        position(latitude, longitude);

        return null;
    }
}
