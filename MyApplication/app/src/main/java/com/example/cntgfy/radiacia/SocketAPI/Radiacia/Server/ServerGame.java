package com.example.cntgfy.radiacia.SocketAPI.Radiacia.Server;

import com.example.cntgfy.radiacia.SocketAPI.Radiacia.Client.ClientGame;
import com.example.cntgfy.radiacia.SocketAPI.Radiacia.Debug.Debug;
import com.example.cntgfy.radiacia.SocketAPI.Radiacia.Debug.Debugable;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Cntgfy on 03.07.2016.
 */
public class ServerGame extends Thread implements Debugable {
    private HashMap<Integer, ClientGame> clients = new HashMap<Integer, ClientGame>();
    private ServerSocket serverSocket;
    private int port;
    private Debug debug = new Debug("SERVER_GAME");

    private IDManager idManager = new IDManager();
    private ShotManager shotManager = new ShotManager(clients.values());

    public ServerGame() {
        port = 9090;
        start();
    }

    public ServerGame(int port) {
        this.port = port;
        start();
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (!isStop) {
                        try {
                            connectSocket();
                        } catch (IOException e) {
                            if (!serverSocket.isClosed()) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }).start();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    //Частота обновления/сек
                    int frequency = 10;

                    frequency = 1000/frequency;
                    while (!isStop) {

                        shotManager.checkHits();
                        try {
                            Thread.sleep(frequency);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();

            debug.printLog("start");

            while (!isStop);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /*
    * Проверяет, кто из clients отключился и удаляет отключившихся
    * */
    public void checkDisconnects() {
        Set<Integer> keys = new HashSet<>(clients.keySet());
        for (Integer key:keys) {
            if (clients.get(key).isConnected() == false) clients.remove(key);
        }
    }

    /**
     * Обрабратывает новые socket подключения
     *
     * @throws IOException
     */
    private void connectSocket() throws IOException {
        Socket newConnect = serverSocket.accept();

        InetAddress newConnectAddress = newConnect.getInetAddress();
        int newConnectPort = newConnect.getPort();

        for (ClientGame client:clients.values()) {
            InetAddress clientAddress = client.getSocket().getInetAddress();
            int clientPort = client.getSocket().getPort();

            if (newConnectAddress.equals(clientAddress)) {
                client.debugLogEnabled(debug.isDebugLogEnabled());
                client.logEnabled(debug.isLogEnabled());
                client.setSocket(newConnect);
                return;
            }
        }

        int id = idManager.getID();
        clients.put(id, new ClientGame(newConnect, "serverClient" + id));
        clients.get(id).logEnabled(debug.isLogEnabled());
        clients.get(id).debugLogEnabled(debug.isDebugLogEnabled());
    }

    public synchronized HashMap<Integer, ClientGame> getClients() {
        return new HashMap<>(clients);
    }

    public synchronized void disconnectAllClients() {
        for (ClientGame clientGame :clients.values()) {
            clientGame.disconnect();
        }
    }

    public void close() {
        try {
            disconnectAllClients();
            interrupt();
            serverSocket.close();
            debug.printDebugLog("closed");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
        * Реализация механизма остановки сервера
        * */
    private volatile boolean isStop = false;

    public void interrupt() {
        super.interrupt();
        isStop = true;
        debug.printDebugLog("interrupt");
    }

    @Override
    public synchronized void debugLogEnabled(boolean enabled) {
        debug.debugLogEnabled(enabled);
        for (ClientGame clientGame: clients.values()) {
            clientGame.debugLogEnabled(enabled);
        }
        shotManager.debugLogEnabled(enabled);
    }

    @Override
    public void logEnabled(boolean enabled) {
        debug.logEnabled(enabled);
        for (ClientGame clientGame: clients.values()) {
            clientGame.logEnabled(enabled);
        }
        shotManager.logEnabled(enabled);
    }
}
