package com.example.cntgfy.radiacia;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.cntgfy.radiacia.SocketAPI.Radiacia.Client.ClientGame;
import com.example.cntgfy.radiacia.SocketAPI.Radiacia.Debug.Debug;

import Radiacia.server.client.GameClient;

public class MainActivity extends Activity {
    TextView clientStatusTV;
    TextView gamerInfoTV;
    TextView attitudeInfoTV;
    Button shootButton;
    Button connectButton;
    EditText ipEditText;

    AttitudeUpdater attitudeUpdater;
    ClientGame clientGame;

    Timer timer = new Timer();

    Debug debug = new Debug("MAIN_ACTIVITY");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        clientStatusTV = (TextView) findViewById(R.id.clientStatusTV);
        gamerInfoTV = (TextView) findViewById(R.id.gamerInfoTV);
        attitudeInfoTV = (TextView) findViewById(R.id.attitudeInfoTV);
        shootButton = (Button) findViewById(R.id.shootButton);
        connectButton = (Button) findViewById(R.id.connectButton);
        ipEditText = (EditText) findViewById(R.id.ipEditText);

        attitudeUpdater = new AttitudeUpdater(this);
    }

    public void onClickConnectButton(View v) {
        ip = String.valueOf(ipEditText.getText());

        new ConnectThread().start();
    }

    public void onClickShootButton(View v) {
        if (clientGame != null) {
            clientGame.shoot();
        }
    }

    public void onClickDisconnectButton(View v) {
        if (clientGame != null) {
            clientGame.disconnect();
            clientGame = null;
        }

        if (connectThread != null) {
            connectThread.interrupt();
            connectThread = null;
        }
        clientConnectStatus = STATUS_DISCONNECTED;
    }

    @Override
    protected void onResume() {
        super.onResume();
        attitudeUpdater.onResume();
        new ConnectThread().start();

        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        clientStatusTV.setText("Client status: " + clientConnectStatus);
                        if (clientGame != null) {
                            gamerInfoTV.setText("Gamer info: " + clientGame);
                        } else {
                            gamerInfoTV.setText("Gamer info: " + attitudeUpdater.getGameObject());
                        }
                        //attitudeUpdater.getDetOfOrient().showInfo(gamerInfoTV);
                        attitudeUpdater.getDetOfPos().showLocation(attitudeInfoTV);

                    }
                });
            }
        };
        timer.schedule(task, 0, 100);
    }

    @Override
    protected void onPause() {
        super.onPause();
        timer.cancel();
        attitudeUpdater.onPause();
        if (clientGame != null) {
            clientGame.disconnect();
        }
    }

    /**
     * Создает нить подключения к серверу.
     */
    private ConnectThread connectThread;
    private String ip = "192.168.1.5";

    private String clientConnectStatus = STATUS_DISCONNECTED;
    private static final String STATUS_DISCONNECTED = "disconnected";
    private static final String STATUS_TRYING_TO_CONNECT = "trying to connect";
    private static final String STATUS_CONNECTED = "connected";
    private static final String STATUS_UNKNOWN_HOST = "unknown host";

    class ConnectThread extends Thread {
        @Override
        public void run() {
            try {
                synchronized (ConnectThread.class) {
                    //Убиваем существующую нить, если она есть
                    if (connectThread != null) {
                        if (connectThread.isAlive()) connectThread.interrupt();
                    }
                    connectThread = this;
                }

                clientConnectStatus = STATUS_TRYING_TO_CONNECT;
                debug.printDebugLog("connectThread start");
                Socket socket = new Socket(ip, 9089);
                if (isInterrupted()) return;

                clientGame = new ClientGame(socket, "AndroidGamer");
                clientConnectStatus = STATUS_CONNECTED;

                attitudeUpdater.setGameObject(clientGame);
            } catch (UnknownHostException e) {
                if (!isInterrupted()) {
                    e.printStackTrace();
                    clientConnectStatus = STATUS_UNKNOWN_HOST;
                }
            } catch (IOException e) {
                if (!isInterrupted()) {
                    e.printStackTrace();
                    clientConnectStatus = STATUS_DISCONNECTED;
                }
            }
        }
    }

}
