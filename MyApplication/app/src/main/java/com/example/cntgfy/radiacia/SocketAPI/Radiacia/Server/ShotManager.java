package com.example.cntgfy.radiacia.SocketAPI.Radiacia.Server;

import com.example.cntgfy.radiacia.SocketAPI.Radiacia.Client.ClientGame;
import com.example.cntgfy.radiacia.SocketAPI.Radiacia.Debug.Debug;
import com.example.cntgfy.radiacia.SocketAPI.Radiacia.Debug.Debugable;
import com.example.cntgfy.radiacia.SocketAPI.Radiacia.Game.Shot;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Cntgfy on 04.07.2016.
 * Отвечает за логику выполнения выстрелов игроков
 */
public class ShotManager implements Debugable {
    //частота проверки наличия выстрелов
    private Collection<ClientGame> gamers;
    private ArrayList<Shot> shots = new ArrayList<>();

    private Debug debug = new Debug("SHOT_MANAGER");

    public ShotManager(Collection<ClientGame> gamers) {
        this.gamers = gamers;
    }

    /*
    * Собирает выстрелы у игроков
    * */
    private void updateShots() {
        shots.clear();
        for (ClientGame gamer : gamers) {
            if (gamer.isShoot()) {
                Shot shoot = gamer.getShot();

                shots.add(shoot);
                debug.printLog("new shot: " + shoot.toString());

                gamer.setIsShoot(false);
                gamer.writeCommand(gamer.getCommands().IS_SHOOT + false);
            }
        }
    }

    public void checkHits() {
        updateShots();

        for (ClientGame gamer: gamers) {
            if (!gamer.isALive()) continue;

            for (int i = 0; i < shots.size(); i++) {
                if (shots.get(i).isHit(gamer)) {
                    gamer.hit();
                    debug.printLog(gamer.getName() + " is hit");
                    gamer.writeCommand(gamer.getCommands().HIT);
                }
            }
        }
    }

    @Override
    public void debugLogEnabled(boolean enabled) {
        debug.debugLogEnabled(enabled);
    }

    @Override
    public void logEnabled(boolean enabled) {
        debug.logEnabled(enabled);
    }
}
