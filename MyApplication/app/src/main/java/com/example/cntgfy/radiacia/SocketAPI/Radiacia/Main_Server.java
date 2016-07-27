package com.example.cntgfy.radiacia.SocketAPI.Radiacia;

import com.example.cntgfy.radiacia.SocketAPI.Radiacia.Client.ClientGame;
import com.example.cntgfy.radiacia.SocketAPI.Radiacia.Debug.Debug;
import com.example.cntgfy.radiacia.SocketAPI.Radiacia.Server.ServerComands.ServerCommand;
import com.example.cntgfy.radiacia.SocketAPI.Radiacia.Server.ServerGame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Cntgfy on 05.07.2016.
 */
public class Main_Server implements ServerCommand {
    //Включение и отключение лога дебага
    private static boolean debugEnabled = false;

    private static ServerGame serverGame;
    private static Debug debug = new Debug("SERVER_MAIN");

    public static void main(String[] args) throws InterruptedException {
        debug.debugLogEnabled(debugEnabled);
        startServer();

        new Thread(new Runnable() {
            @Override
            public void run() {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

                while (true) {
                    try {
                        //System.out.println("write number of client or command: ");
                        String command = reader.readLine();

                        if (command.equals(SERVER_START)) {
                            startServer();
                        }

                        if (command.equals(SERVER_STOP)) {
                            stopServer();
                        }

                        if (command.equals(EXIT)) {
                            stopServer();
                            return;
                        }

                        int number = Integer.parseInt(command);
                        if (serverGame.getClients().containsKey(number)) {
                            ClientGame client = serverGame.getClients().get(number);
                            client.read();
                            client.joinUpdate();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (NumberFormatException e) {

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        serverGame.join();
    }

    public static void startServer() {
        if (serverGame != null) {
            System.out.println("Server already started. Use 'stop', for stopping server.");
        } else {
            serverGame = new ServerGame(9089);
            serverGame.debugLogEnabled(debugEnabled);
        }
    }

    public static void stopServer() throws InterruptedException {
        debug.printLog("start stopping the server");
        serverGame.close();
        serverGame.join();
        serverGame = null;
        debug.printLog("server closed");
    }

}
