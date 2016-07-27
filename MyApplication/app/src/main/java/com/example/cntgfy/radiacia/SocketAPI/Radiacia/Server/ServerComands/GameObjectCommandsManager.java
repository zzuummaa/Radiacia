package com.example.cntgfy.radiacia.SocketAPI.Radiacia.Server.ServerComands;

import com.example.cntgfy.radiacia.SocketAPI.Radiacia.Game.GameObject;

/**
 * Created by Cntgfy on 04.07.2016.
 */
public class GameObjectCommandsManager extends CommandsManager<GameObject> implements GameObjectCommand {

    public boolean performCommand(String command, String argument, GameObject gameObject) {
        if (command.equals(DIRECTION)) gameObject.setDirection(Float.valueOf(argument));
        else return false;

        return true;
    }
}
