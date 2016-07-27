package com.example.cntgfy.radiacia.SocketAPI.Radiacia.Client;

import com.example.cntgfy.radiacia.SocketAPI.Radiacia.Debug.Debug;
import com.example.cntgfy.radiacia.SocketAPI.Radiacia.Debug.Debugable;
import com.example.cntgfy.radiacia.SocketAPI.Radiacia.Server.ServerComands.ClientGameCommandsManager;
import com.example.cntgfy.radiacia.SocketAPI.Radiacia.Game.Gamer;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * Created by Cntgfy on 03.07.2016.
 * Расширение класса Gamer. Реализует сообщение двух объектов класса Gamer с помощью сокет соеденения.
 */
public class ClientGame extends Gamer implements Client<Gamer>, Debugable {
    private ClientGameCommandsManager commands = new ClientGameCommandsManager();
    private BufferedWriter writer;
    private ClientGameThread clientGameThread;

    private Socket socket;

    private boolean isConnected = false;

    /**
     * Настройки объекта debug менять только через методы класса ClientGame
     * методы: debugEnabled(boolean enabled)
     */
    public Debug debug;
    public static final String CLIENT_GAME_DEBUG_NAME = "CLIENT_GAMER";

    public ClientGame(Socket socket, String debugName) throws IOException {
        super(debugName);
        this.debug = new Debug(CLIENT_GAME_DEBUG_NAME + " " + debugName);

        setSocket(socket);

        this.isConnected = true;
    }

    protected ClientGame() {};

    @Override
    public void shoot() {
        super.shoot();
        write();
    }

    /**
     * Устанавливаем значения параметров объекта gamer себе и соедененному объекту
     *
     * @param gamer игрок, параметры которого передаем соеденненому объекту
     */
    public synchronized void write(Gamer gamer) {
        this.setGamer(gamer);
        write();
    }

    public void write() {
        writeCommand(this.toString());
    }

    @Override
    public synchronized void writeCommand(String command) {
        try {
            writer.write(command);
            writer.newLine();
            writer.flush();
            debug.printDebugLog("writeCommand: " + "'" + command + "'");
        } catch (IOException e) {
            catchWriterException();
        }
    }

    /*
    * Посылает запрос сокету, на получение значений полей соедененного объекта
    * Предполагается, что поток чтения прочтет их, как только их пришлют
    * */
    public synchronized void read() {
        try {
            writer.write(commands.GET_GAMER);
            writer.newLine();
            writer.flush();
            debug.printDebugLog("read: send request");
        } catch (IOException e) {
            catchWriterException();
        }
    }

    @Override
    public synchronized void disconnect() {
        try {
            writeCommand(commands.DISCONNECT);
            clientGameThread.interrupt();
            socket.close();
            isConnected = false;
            debug.printLog("disconnected");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void catchWriterException() {
        try {
            writer.close();
            debug.printDebugLog("writer closed");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    * Ждем, пока состояние геймера обновится. Возвращает время ождидания в миллисекундах
    * */
    public long joinUpdate() {
        return clientGameThread.joinUpdate();
    }

    @Override
    public void debugLogEnabled(boolean enabled) {
        debug.debugLogEnabled(enabled);
        clientGameThread.debugLogEnabled(enabled);
    }

    @Override
    public void logEnabled(boolean enabled) {
        debug.logEnabled(enabled);
        clientGameThread.logEnabled(enabled);
    }

    public boolean isConnected() {
        return isConnected;
    }

    public ClientGameCommandsManager getCommands() {
        return commands;
    }

    public Socket getSocket() {
        return socket;
    }

    /**
     * Устанавливает подключение к сокету
     *
     * @param socket
     * @throws IOException
     */
    public void setSocket(Socket socket) throws IOException {
        this.socket = socket;

        if (writer != null) writer.close();
        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        if (clientGameThread != null) clientGameThread.interrupt();
        clientGameThread = new ClientGameThread(socket, this);
        clientGameThread.debugLogEnabled(debug.isDebugLogEnabled());
        clientGameThread.logEnabled(debug.isLogEnabled());

        debug.printLog("connected");
    }
}
