package com.example.cntgfy.radiacia.SocketAPI.Radiacia.Server.ServerComands;

import com.example.cntgfy.radiacia.SocketAPI.Radiacia.Game.Gamer;

/**
 * Created by Cntgfy on 04.07.2016.
 * Позволяет преобразовывать класс GameObject в строку и обратно.
 */
public class GamerCommandsManager extends CommandsManager<Gamer> implements GamerCommand{
    GameObjectCommandsManager gameObjectCommands = new GameObjectCommandsManager();
    OnSurfaceOfEarthCommandsManager onSurfaceOfEarthCommands = new OnSurfaceOfEarthCommandsManager();

    public boolean performCommand(String command, String argument, Gamer gamer) {
        if (gameObjectCommands.performCommand(command, argument, gamer));
        else if (onSurfaceOfEarthCommands.performCommand(command, argument, gamer));
        else if (command.equals(IS_ALIVE)) gamer.setALive(Boolean.valueOf(argument));
        else if (command.equals(NAME)) gamer.setName(argument);
        else if (command.equals(IS_SHOOT)) gamer.setIsShoot(Boolean.valueOf(argument));
        else if (command.equals(HIT)) gamer.hit();
        else return false;

        return true;
    }
}
