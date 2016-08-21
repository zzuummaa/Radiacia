package com.example.cntgfy.radiacia;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

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

    SendToServerThread sendToServerThread;

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

        sendToServerThread = new SendToServerThread();

        attitudeUpdater = new AttitudeUpdater(this, sendToServerThread);

        conD = new ConnectData();
        conD.setId(0);
    }

    public void onClickConnectButton(View v) {
        clientConnect();
    }

    public void onClickShootButton(View v) {
        synchronized (cg) {
            sendToServerThread.add(new Runnable() {
                @Override
                public void run() {
                    cg.setIsShoot(true);
                }
            });
        }
    }

    public void onClickDisconnectButton(View v) {
        clientDisconnect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        attitudeUpdater.onResume();
        //clientConnect();

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
                            gamerInfoTV.setText("Gamer info: " + attitudeUpdater.getClientGamer());
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
            cth = null;
        }

        if (attitudeUpdater.getClientGamer() != null) {
            attitudeUpdater.setClientGamer(null);
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

        clientConnectStatus = STATUS_DISCONNECTED;
    }

    private String ip = "192.168.1.5";

    private String clientConnectStatus = STATUS_DISCONNECTED;
    private static final String STATUS_DISCONNECTED = "disconnected";
    private static final String STATUS_TRYING_TO_CONNECT = "trying to connect";
    private static final String STATUS_CONNECTED = "connected";
    private static final String STATUS_UNKNOWN_HOST = "unknown host";

    class ConnectThread extends Thread {
        private volatile Socket socket;

        public ConnectThread() {
            super("ConnectThread");
            start();
        }

        @Override
        public void run() {
            try {
                clientConnectStatus = STATUS_TRYING_TO_CONNECT;

                connect();

                clientConnectStatus = STATUS_CONNECTED;
            } catch (IOException e) {
                if (!isInterrupted()) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cth == this && clientConnectStatus == STATUS_TRYING_TO_CONNECT) clientConnectStatus = STATUS_DISCONNECTED;
                System.out.println("End connect thread with status: " + clientConnectStatus);
            }
        }

        private void connect() throws Exception {
            if (gc == null && cg == null) {
                ip = ipEditText.getText().toString();

                socket = new Socket();
                socket.connect(new InetSocketAddress(ip, 9090));

                if (isInterrupted()) return;

                Client client = new SocketClient(socket);
                socket = null;

                gc = new GameClient(client, conD.getId());
                gc.connect();

                cg = new ClientGamer(gc);
                cg.setName("Test");
                attitudeUpdater.setClientGamer(cg);
            }
        }

        @Override
        public void interrupt() {
            super.interrupt();
            if (socket != null) try {
                socket.close();
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
