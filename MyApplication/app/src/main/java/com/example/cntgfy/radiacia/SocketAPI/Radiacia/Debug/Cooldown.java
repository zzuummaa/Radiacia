package com.example.cntgfy.radiacia.SocketAPI.Radiacia.Debug;

/**
 * Created by Cntgfy on 07.07.2016.
 * Отсчитывает время ожидания в отдельном потоке.
 *
 * Инструкция по использованию:
 * 1. Создаем объект класса
 * 2. Вызываем метод begin()
 * 3. Пока идет ожидание метод isEnd() будет возвращать false. Как только время ожидания
 * истечет, метод начнет возвращать true
 */
public class Cooldown {
    Thread waitingThread;

    private long cooldown;
    private volatile boolean isEnd = true;

    public Cooldown() {
        this.cooldown = 0;
    }

    public Cooldown(long cooldown) {
        this.cooldown = cooldown;
    }

    /**
     * Вызывает отсчет времени с переданным при инициализации значением cooldown
     */
    public void begin() {
        begin(cooldown);
    }

    public void begin(long cooldown) {
        if (cooldown > 0) {
            if (waitingThread.isAlive()) {
                waitingThread.interrupt();
            }
            new Thread(new WaitingThread()).start();
        }
    }

    public boolean isEnd() {
        return isEnd;
    }

    private class WaitingThread implements Runnable {

        @Override
        public void run() {
            try {
                isEnd = false;
                Thread.sleep(cooldown);
            } catch (InterruptedException e) {
            } finally {
                isEnd = true;
            }
        }
    }
}
