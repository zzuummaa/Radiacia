package com.example.cntgfy.radiacia.SocketAPI.Radiacia.Client;

import com.example.cntgfy.radiacia.SocketAPI.Radiacia.Debug.Debug;
import com.example.cntgfy.radiacia.SocketAPI.Radiacia.Debug.Debugable;
import com.example.cntgfy.radiacia.SocketAPI.Radiacia.Server.ServerComands.ClientGameCommandsManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Date;

/**
 * Created by Cntgfy on 05.07.2016.
 * Реализует поток чтения данных
 */
public class ClientGameThread extends Thread implements Debugable {
    private Socket clientSocket;
    private BufferedReader reader;

    private ClientGame clientGame;
    private ClientGameCommandsManager commandsManager = new ClientGameCommandsManager();

    public Debug debug;

    private boolean isJoinUpdate = true;

    public ClientGameThread(Socket clientSocket, ClientGame clientGame) {
        this.clientSocket = clientSocket;
        this.clientGame = clientGame;
        debug = new Debug("CLIENT_GAME_THREAD");
        start();
    }

    public ClientGameThread(Socket clientSocket, ClientGame clientGame, String debugName) {
        this.clientSocket = clientSocket;
        this.clientGame = clientGame;
        this.debug = new Debug("CLIENT_GAME_THREAD " + debugName);
        start();
    }

    @Override
    public void run() {
        try {
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
            interrupt();
        }

        //Поток чтения
        while (!isInterrupted()) {
            try {
                String commandWithArgument = reader.readLine();

                //Разобраться, в каких случаях reader возвращает null
                if (commandWithArgument == null) {
                    //debug.printLog("read null reference");
                    return;
                }
                debug.printDebugLog("read: command is received " + "'" + commandWithArgument + "'");

                synchronized (clientGame) {
                    commandsManager.performCommands(commandWithArgument, clientGame);
                    isJoinUpdate = false;
                }
            } catch (IOException e) {
                if (clientSocket.isClosed()) {
                    break;
                }
                e.printStackTrace();
            }
        }
        debug.printDebugLog("interrupted");
    }

    /*
    * Ждем, пока состояние геймера обновится. Возвращает время ождидания в миллисекундах
    * */
    public long joinUpdate() {
        Date timeBegin = new Date(System.currentTimeMillis());

        while (isJoinUpdate);

        isJoinUpdate = true;

        Date timeEnd = new Date(System.currentTimeMillis());

        return timeEnd.getTime() - timeBegin.getTime();
    }

    @Override
    public void debugLogEnabled(boolean enabled) {
        debug.debugLogEnabled(enabled);
    }

    @Override
    public void logEnabled(boolean enabled) {
        debug.logEnabled(enabled);
    }

    /*
    * interrupt класса Thread не работает!!! Да что ж такое!
    *
    * Переписана логика interrupt
    *
    * P.S. Не за что.
    * */
    private volatile boolean isInterrupted = false;

    @Override
    public boolean isInterrupted() {
        return isInterrupted;
    }

    @Override
    public synchronized void interrupt() {
        isInterrupted = true;
    }
}
