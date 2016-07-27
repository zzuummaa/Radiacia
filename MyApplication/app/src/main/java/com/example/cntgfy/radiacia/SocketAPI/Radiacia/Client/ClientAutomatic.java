package com.example.cntgfy.radiacia.SocketAPI.Radiacia.Client;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by Cntgfy on 21.07.2016.
 */
public class ClientAutomatic {
    private volatile boolean isConnected = false;

    private Socket socket;
    private String address = "localHost";
    private int port = 9090;

    public ClientAutomatic() throws IOException {
        connect();
    }

    public ClientAutomatic(Socket socket) {
        this.socket = socket;
    }

    public void connect(String address, int port) throws IOException {
        this.address = address;
        this.port = port;

        connect();
    }

    public void connect() throws IOException {
        if (isConnected == false) {
            disconnect();
            socket = new Socket(address, port);
        }
    }

    public void disconnect() throws IOException {
        if (socket != null & socket.isConnected()) {
            socket.close();
        }
    }
}
