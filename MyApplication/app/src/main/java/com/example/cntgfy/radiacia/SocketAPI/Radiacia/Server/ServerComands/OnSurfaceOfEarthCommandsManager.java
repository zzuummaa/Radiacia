package com.example.cntgfy.radiacia.SocketAPI.Radiacia.Server.ServerComands;

import com.example.cntgfy.radiacia.SocketAPI.Radiacia.Game.OnSurfaceOfEarth;

/**
 * Created by Cntgfy on 12.07.2016.
 */
public class OnSurfaceOfEarthCommandsManager extends CommandsManager<OnSurfaceOfEarth> implements OnSurfaceOfEarthCommand {
    @Override
    public boolean performCommand(String command, String argument, OnSurfaceOfEarth onSurfaceOfEarth) {
        if (command.equals(LATITUDE)) onSurfaceOfEarth.setLatitude(Double.valueOf(argument));
        else if (command.equals(LONGITUDE)) onSurfaceOfEarth.setLongitude(Double.valueOf(argument));
        else if (command.equals(ACCURACY)) onSurfaceOfEarth.setAccuracy(Float.valueOf(argument));
        else return false;

        return true;
    }
}
