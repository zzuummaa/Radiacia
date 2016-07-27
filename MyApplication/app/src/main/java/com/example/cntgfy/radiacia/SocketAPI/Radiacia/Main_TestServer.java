package com.example.cntgfy.radiacia.SocketAPI.Radiacia;

import com.example.cntgfy.radiacia.SocketAPI.Radiacia.Client.ClientGame;
import com.example.cntgfy.radiacia.SocketAPI.Radiacia.Server.IDManager;
import com.example.cntgfy.radiacia.SocketAPI.Radiacia.Server.ServerGame;
import com.example.cntgfy.radiacia.SocketAPI.Radiacia.Game.Gamer;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by Cntgfy on 02.07.2016.
 * Тестирование работы сервера
 */
public class Main_TestServer {
    private static int port = 9080;

    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("TEST SERVER_GAME");
        System.out.println("-----------------------------------------------------------------------------------------");
        final ServerGame serverGame = new ServerGame(port);
        serverGame.debugLogEnabled(true);

        System.out.println("Connect clients:");
        final ClientGame clientGame0 = new ClientGame(new Socket("localHost", port), "client0");
        final ClientGame clientGame1 = new ClientGame(new Socket("localHost", port), "client1");
        Thread.sleep(100);

        IDManager idManager = new IDManager();
        Gamer gamer1 = new Gamer(String.valueOf(idManager.getID()), 44.8823289, 37.32541064, -31.338673f);
        gamer1.setAccuracy(9);
        Gamer gamer2 = new Gamer(String.valueOf(idManager.getID()), 44.88342901, 37.32472049, -33.281013f);
        gamer2.setAccuracy(9);

        System.out.println("-----------------------");
        System.out.println("Set gamers to clients:");
        clientGame0.setGamerParams(gamer1);
        clientGame0.shoot();
        clientGame1.setGamerParams(gamer2);
        System.out.println(clientGame0);
        System.out.println(clientGame1);

        System.out.println("-----------------------");
        System.out.println("Begin status clients:");
        System.out.println(clientGame0);
        System.out.println(clientGame1);

        System.out.println("-----------------------");
        System.out.println("Begin status server clients:");
        System.out.println(serverGame.getClients().get(0));
        System.out.println(serverGame.getClients().get(1));

        System.out.println("-----------------------");
        System.out.println("Server clients update:");
        serverGame.getClients().get(0).read();
        serverGame.getClients().get(1).read();
        serverGame.getClients().get(0).joinUpdate();
        serverGame.getClients().get(1).joinUpdate();

        System.out.println("-----------------------");
        System.out.println("Server handle updates:");
        Thread.sleep(150);

        System.out.println("------------------------");
        System.out.println("End status clients:");
        System.out.println(clientGame0);
        System.out.println(clientGame1);

        System.out.println("------------------------");
        System.out.println("End status server clients:");
        System.out.println(serverGame.getClients().get(0));
        System.out.println(serverGame.getClients().get(1));

        System.out.println("------------------------");
        System.out.println("client0 disconnect:");
        clientGame0.disconnect();
        Thread.sleep(100);

        System.out.println("------------------------");
        System.out.println("serverClients condition:");
        for (ClientGame clientGame:serverGame.getClients().values()) {
            clientGame.debug.printLog("isConnected=" + clientGame.isConnected());
        }

        System.out.println("------------------------");
        System.out.println("Close all:");
        serverGame.close();
        Thread.sleep(100);

        System.out.println("------------------------");
        System.out.println("connections condition:");
        for (ClientGame clientGame: serverGame.getClients().values()) {
            clientGame.debug.printLog("isConnected=" + clientGame.isConnected());
        }
        clientGame0.debug.printLog("isConnected=" + clientGame0.isConnected());
        clientGame1.debug.printLog("isConnected=" + clientGame1.isConnected());
        System.out.println("-----------------------------------------------------------------------------------------");

        /*System.out.println();
        System.out.println("TEST CLIENT_GAMER_COMMAND");
        GamerCommandsManager gamerCommandsManager = new GamerCommandsManager();

        Gamer gamer4 = new Gamer("4", 10, 15, 45);
        Gamer gamer5 = new Gamer("5");
        System.out.println("before update: " + gamer5);
        gamerCommandsManager.setFields(gamer4.toString(), gamer5);
        System.out.println("after update: " + gamer5);*/
    }
}
