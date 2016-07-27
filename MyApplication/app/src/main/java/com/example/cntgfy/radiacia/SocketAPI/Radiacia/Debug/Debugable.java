package com.example.cntgfy.radiacia.SocketAPI.Radiacia.Debug;

/**
 * Created by Cntgfy on 08.07.2016.
 * У класса можно отключать и включать логирование
 */
public interface Debugable {
    public void debugLogEnabled(boolean enabled);

    public void logEnabled(boolean enabled);
}
