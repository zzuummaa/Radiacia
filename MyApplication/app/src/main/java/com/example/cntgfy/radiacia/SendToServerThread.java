package com.example.cntgfy.radiacia;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Cntgfy on 22.08.2016.
 *
 * Посылает данные в отедльных потоках
 */
public class SendToServerThread extends Thread {
    private LinkedBlockingQueue<Thread> threadQueue;

    public SendToServerThread() {
        super("PoolSendToServerThread");
        threadQueue = new LinkedBlockingQueue<>();
        start();
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            try {
                if (threadQueue.isEmpty()) {
                    yield();
                } else {
                    Thread thread = threadQueue.remove();
                    thread.start();
                    thread.join();
                }
            } catch (InterruptedException e) {

            }

        }
    }

    public void add(Runnable runnable) {
        threadQueue.add(new Thread(runnable, "SendToServerThread"));
    }
}
