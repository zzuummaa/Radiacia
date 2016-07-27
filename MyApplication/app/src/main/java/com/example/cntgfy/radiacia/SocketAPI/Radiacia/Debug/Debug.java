package com.example.cntgfy.radiacia.SocketAPI.Radiacia.Debug;

/**
 * Created by Cntgfy on 04.07.2016.
 * Позволяет отслеживать ход исполнения приложения
 *
 * Для работы необходимо в месте, которое необходимо залогировать, вызвать один из двух методов:
 * printLog(String info)
 * printDebugLog(String info) - выводит строку (debugName + " " + info)
 */
public class Debug implements Debugable {
    private String debugName;

    private volatile boolean isDebugLogEnabled = true;
    private volatile boolean isLogEnabled = true;

    private Cooldown cooldown;

    public Debug(String debugName) {
        this.debugName = debugName;
        this.cooldown = new Cooldown(0);
    }

    public Debug(String debugName, long cooldown) {
        this.debugName = debugName;
        this.cooldown = new Cooldown(cooldown);
    }

    /**
     * используется для вывода постоянного лога
     * лог отключается методом logEnabled(false)
     */
    public void printLog(String info) {
        if (isLogEnabled == true & cooldown.isEnd()) {
            System.out.println(debugName + " " + info);
            cooldown.begin();
        }
    }

    /**
     * используется для вывода лога для отладки
     * debugLog отключается методом debugLogEnabled(false)
     * так же будет отключен если вызвать logEnabled(false)
     */
    public void printDebugLog(String info) {
        if (isDebugLogEnabled == true & isLogEnabled == true & cooldown.isEnd()) {
            System.out.println(debugName + " " + info);
            cooldown.begin();
        }
    }

    public boolean isDebugLogEnabled() {
        return isDebugLogEnabled;
    }

    public void debugLogEnabled(boolean enabled) {
        isDebugLogEnabled = enabled;
    }

    public boolean isLogEnabled() {
        return isLogEnabled;
    }

    public void logEnabled(boolean enabled) {
        isLogEnabled = enabled;
    }
}
