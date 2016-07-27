package com.example.cntgfy.radiacia.SocketAPI.Radiacia.Client.io;

import java.io.IOException;

/**
 * Created by Cntgfy on 21.07.2016.
 *
 * Позволяет читать состояние игры
 *
 * Соглашение о командах игры при реализации чтения с помощью строк:
 * - комманда обозначающая, объект заключается между двумя символами "@"
 *  Пример: "@gamer@"
 * - затем следует пробел и после него идут параметры для конструирования объекта
 *  Пример строки: "@gamer@ name latitude longitude"
 */
public interface GameReader {
    /**
     * Читает строку
     *
     * @return считанную строку
     * @throws IOException когда объект не получилось считать
     *                 или когда объект не String
     */
    String readString() throws IOException;
}
