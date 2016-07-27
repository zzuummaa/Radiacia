package com.example.cntgfy.radiacia.SocketAPI.Radiacia.Server.ServerComands;

/**
 * Created by Cntgfy on 06.07.2016.
 */
public interface GamerCommand extends GameObjectCommand, OnSurfaceOfEarthCommand {
    public static final String IS_ALIVE = "isAlive=";
    public static final String HIT = "hit=";
    public static final String NAME = "name=";
    public static final String IS_SHOOT = "isShoot=";
}
