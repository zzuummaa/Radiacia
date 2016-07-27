package com.example.cntgfy.radiacia.SocketAPI.Radiacia.Server.ServerComands;

import com.example.cntgfy.radiacia.SocketAPI.Radiacia.Client.ClientGame;

/**
 * Created by Cntgfy on 06.07.2016.
 */
public class ClientGameCommandsManager extends CommandsManager<ClientGame> implements ClientGameCommand{
    GamerCommandsManager gamerCommands = new GamerCommandsManager();

    public boolean performCommand(String command, String argument, ClientGame clientGame) {
        if (gamerCommands.performCommand(command, argument, clientGame)) return true;
        else if (command.equals(GET_GAMER)) clientGame.write();
        else if (command.equals(DISCONNECT)) clientGame.disconnect();
        else return false;

        return true;
    }
}
