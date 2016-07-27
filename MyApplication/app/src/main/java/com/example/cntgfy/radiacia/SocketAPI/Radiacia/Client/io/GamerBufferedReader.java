package com.example.cntgfy.radiacia.SocketAPI.Radiacia.Client.io;

import com.example.cntgfy.radiacia.SocketAPI.Radiacia.Game.Gamer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

/**
 * Created by Cntgfy on 21.07.2016.
 */
public class GamerBufferedReader extends BufferedReader implements GamerReader {
    public GamerBufferedReader(Reader in) {
        super(in);
    }

    public GamerBufferedReader(Reader in, int size) {
        super(in, size);
    }

    public static final String GAMER_POINTER = "@gamer@";

    /**
     * Считывает объект класса Gamer
     *
     * @return объект класса Gamer
     * @throws IOException
     * @throws NumberFormatException полученная строка не может быть преобразована в объект Gamer
     */
    @Override
    public Gamer readGamer() throws IOException, NumberFormatException {
        String data = readLine();
        if (data.length() < 9) throw new NumberFormatException("can't convert String '" + data + "' to Gamer");

        Gamer gamer;

        if (data.substring(0, GAMER_POINTER.length()).equals(GAMER_POINTER)) {
            gamer = toGamer(data.substring(GAMER_POINTER.length()+1));
        } else {
            gamer = null;
        }

        if (gamer != null) {
            return gamer;
        } else {
            throw new NumberFormatException("can't convert String '" + data + "' to Gamer");
        }
    }

    /**
     * Преобразует строку в объект типа Gamer
     *
     * @param params строка вида "name latitude longitude direction isAlive isShot"
     * @return объект класса Gamer
     *         либо null, если строка не правильного вида
     */
    private Gamer toGamer(String params) {
        String[] splitParams = params.split(" ");

        if (splitParams.length < 6) return null;
        else                        return toGamer(splitParams);
    }

    /**
     * Преобразует массив строковых параметров в объект типа Gamer
     *
     * @param splitParams строковые параметры, преобразуемые к нужным типам
     * @return объект Gamer
     */
    private Gamer toGamer(String[] splitParams) {
        String name = splitParams[0];
        double latitude = Double.parseDouble(splitParams[1]);
        double longitude = Double.parseDouble(splitParams[2]);
        float direction = Float.parseFloat(splitParams[3]);
        boolean isAlive = Boolean.parseBoolean(splitParams[4]);
        boolean isShoot = Boolean.parseBoolean(splitParams[5]);

        Gamer gamer = new Gamer(name, latitude, longitude, direction);
        gamer.setIsShoot(isShoot);
        gamer.setALive(isAlive);
        return gamer;
    }

    @Override
    public String readString() throws IOException {
        return null;
    }
}
