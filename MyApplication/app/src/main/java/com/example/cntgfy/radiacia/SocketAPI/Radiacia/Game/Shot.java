package com.example.cntgfy.radiacia.SocketAPI.Radiacia.Game;

import com.example.cntgfy.radiacia.Determinant.CoordinateConversion2D;
import com.example.cntgfy.radiacia.Determinant.CoordinateConversion3D;

/**
 * Created by Cntgfy on 02.07.2016.
 *
 * Создается, когда игрок делает выстрел.
 *
 */
public class Shot extends GameObject {
    //Радиус попадания
    private float size = 9f;

    //Хозяин выстрела
    private final GameObject owner;

    CoordinateConversion3D cc3 = new CoordinateConversion3D();
    CoordinateConversion2D cc2 = new CoordinateConversion2D();

    /**
     * Положение данного объекта в с.к., связанной с Землей. Центр с.к. в центре Земли.
     */
    private double[] position;

    /*
    * direction в градусах [-180; 180]
    * */
    public Shot(double latitude, double longitude, float direction) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.direction = direction;
        this.owner = new GameObject();

        this.position = cc3.position(latitude, longitude);
    }

    /*
    * direction в градусах [-180; 180]
    * owner объект, который произвел выстрел
    * */
    public Shot(double latitude, double longitude, float direction, GameObject owner) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.direction = direction;
        this.owner = owner;

        this.position = cc3.position(latitude, longitude);
    }

    /*
    * direction в градусах [-180; 180]
    * size радиус попадания
    * */
    public Shot(double latitude, double longitude, float direction, GameObject owner, float size) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.direction = direction;
        this.size = size;
        this.owner = owner;

        this.position = cc3.position(latitude, longitude);
    }

    //Радиус Земли в метрах
    private static final int EARTH_RADIUS = 6371 * 1000;

    /*
    * Проверяем, попал ли выстрел в игрока gamer
    *
    * Метод еще не работает
    * */
    public boolean isHit(GameObject gameObject) {
        //Если gameObject стреляет сам в себя
        if (gameObject == owner) return false;

        //Угол поворота выстрела
        float direction1 = direction;

        double[] gameObjectPosition = cc3.position(gameObject.getLatitude(), gameObject.getLongitude());

        //Угол поворота вектора между выстрелом и gamerObject
        float direction2 = cc3.angleOnPlane(position, gameObjectPosition);

        //Расстояние между выстрелом и gameObject
        float distance = (float) cc3.abs(cc3.vectorsDifference(gameObjectPosition, position));

        float deltaAngle = cc2.deltaAngle2(distance, size);

        return Math.abs(direction1 - direction2) < deltaAngle;
    }

    public boolean isHit(Gamer gamer) {
        size = gamer.getAccuracy();
        return isHit((GameObject)gamer);
    }

    public double[] getPosition() {
        return position;
    }

    @Override
    public String toString() {
        String string = "";
        if (owner instanceof Gamer) string = "ownerName=" + ((Gamer) owner).getName();
        else                        string = "ownerName=";

        string = string + " latitude=" + latitude
                        + " longitude="+ longitude
                        + " direction="+ direction;

        return string;
    }
}
