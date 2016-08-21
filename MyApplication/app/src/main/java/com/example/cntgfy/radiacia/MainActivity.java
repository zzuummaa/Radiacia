package com.example.cntgfy.radiacia;

import java.io.IOException;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Exchanger;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import Radiacia.data.ConnectData;
import Radiacia.server.client.Client;
import Radiacia.server.client.ClientGamer;
import Radiacia.server.client.GameClient;
import Radiacia.server.client.SocketClient;

public class MainActivity extends Activity {
    TextView clientStatusTV;
    TextView gamerInfoTV;
    TextView attitudeInfoTV;
    Button shootButton;
    Button connectButton;
    EditText ipEditText;

    AttitudeUpdater attitudeUpdater;

    volatile GameClient gc;
    volatile ConnectData conD;
    volatile ConnectThread cth;

    volatile ClientGamer cg;

    Timer timer = new Timer();

    //Debug debug = new Debug("MAIN_ACTIVITY");

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

        conD = new ConnectData();
        conD.setId(0);
    }

    public void onClickConnectButton(View v) {
        clientConnect();
    }

    public void onClickShootButton(View v) {
        if (cg != null) cg.setIsShoot(true);
    }

    public void onClickDisconnectButton(View v) {
        clientDisconnect();
        clientConnectStatus = STATUS_DISCONNECTED;
    }

    @Override
    protected void onResume() {
        super.onResume();
        attitudeUpdater.onResume();
        clientConnect();

        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        clientStatusTV.setText("Client status: " + clientConnectStatus);
                        if (cg != null) {
                            gamerInfoTV.setText("Gamer info: " + cg);
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

        System.out.println("onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        timer.cancel();
        attitudeUpdater.onPause();
        clientDisconnect();

        System.out.println("onPause");
    }

    private void clientConnect() {
        if (cth == null) cth = new ConnectThread();
    }

    private void clientDisconnect() {
        if (cth != null) {
            cth.interrupt();
        }

        if (cg != null) {
            cg.close();
            cg = null;
        }

        if (gc != null) {
            try {
                conD = gc.getConD();
                gc.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            gc = null;
        }
    }

    private String ip = "192.168.1.5";
    private volatile boolean isBlocked = false;

    private String clientConnectStatus = STATUS_DISCONNECTED;
    private static final String STATUS_DISCONNECTED = "disconnected";
    private static final String STATUS_TRYING_TO_CONNECT = "trying to connect";
    private static final String STATUS_CONNECTED = "connected";
    private static final String STATUS_UNKNOWN_HOST = "unknown host";

    class ConnectThread extends Thread {
        private Socket socket;

        public ConnectThread() {
            super("ConnectThread");
            synchronized (this) {
                if (!isBlocked) {
                    isBlocked = true;
                    start();
                }
            }
        }

        @Override
        public void run() {
            clientConnectStatus = STATUS_TRYING_TO_CONNECT;

            try {
                if (gc == null && cg == null) {
                    ip = ipEditText.getText().toString();

                    socket = new Socket(ip, 9090);
                    Client client = new SocketClient(socket);

                    gc = new GameClient(client, conD.getId());
                    gc.connect();

                    cg = new ClientGamer(gc);
                    attitudeUpdater.setGameObject(cg);
                }
            } catch (IOException e) {
                if (!isInterrupted()) {
                    e.printStackTrace();
                    clientConnectStatus = STATUS_UNKNOWN_HOST;
                    interrupt();
                }
            } catch (Exception e) {
                e.printStackTrace();
                interrupt();
            } finally {
                cth = null;
                isBlocked = false;
                clientConnectStatus = isInterrupted() ? STATUS_DISCONNECTED : STATUS_CONNECTED;
            }
        }

        @Override
        public void interrupt() {
            super.interrupt();
            try {
                if (socket != null) socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*class ConnectThread extends Thread {
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
    }*/

}
