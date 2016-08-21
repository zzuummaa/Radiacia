package com.example.cntgfy.radiacia;

import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.net.Socket;

import Radiacia.server.client.Client;
import Radiacia.server.client.GameClient;

/**
 * Created by Cntgfy on 21.07.2016.
 *
 * Позволяет записывать данные из ClientGame в текстовое поле
 */
public class ClientTextViewAdapter extends GameClient {
    private static final String STATUS_DISCONNECTED = "Client status: disconnected";
    private static final String STATUS_TRYING_TO_CONNECT = "Client status: trying to connect";
    private static final String STATUS_CONNECTED = "Client status: connected";
    private static final String STATUS_UNKNOWN_HOST = "Client status: unknown host";

    private TextView textView;

    public ClientTextViewAdapter(Client client) {
        super(client);
    }

    /*public ClientTextViewAdapter(Socket socket, String debugName) throws IOException {
        super(socket, debugName);
    }

    @Override
    public void setSocket(Socket socket) throws IOException {
        super.setSocket(socket);

        if (textView != null) {
            textView.setText(STATUS_CONNECTED);
        }
    }

    @Override
    public synchronized void disconnect() {
        super.disconnect();
        if (textView != null) {
            textView.setText(STATUS_DISCONNECTED);
        }
    }*/


}
