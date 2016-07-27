package com.example.cntgfy.radiacia.SocketAPI.Radiacia.Client.io;

import java.io.IOException;

/**
 * Created by Cntgfy on 21.07.2016.
 *
 * Позволяет записывать состояние игры
 *
 * см. соглашение о реализации команд игры в классе GameReader
 */
public interface GameWriter {
    void write(String string) throws IOException;
}
