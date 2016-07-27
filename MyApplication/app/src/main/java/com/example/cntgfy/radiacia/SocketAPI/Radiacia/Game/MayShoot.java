package com.example.cntgfy.radiacia.SocketAPI.Radiacia.Game;

/**
 * Created by Cntgfy on 04.07.2016.
 * Может стрелять
 */
public interface MayShoot {
    public boolean isShoot();

    public void setIsShoot(boolean isShoot);

    public Shot getShot();

    public void shoot();

    public void shoot(GameObject gameObject);
}
