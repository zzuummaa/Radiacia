package com.example.cntgfy.radiacia.SocketAPI.Radiacia.Client.io;

import com.example.cntgfy.radiacia.SocketAPI.Radiacia.Game.Gamer;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.Reader;
import java.io.StreamCorruptedException;

/**
 * Created by Cntgfy on 21.07.2016.
 *
 */
public class GamerObjectReader extends ObjectInputStream implements GamerReader {
    private ObjectInputStream in;

    public GamerObjectReader(InputStream input) throws StreamCorruptedException, IOException {
        super(input);
    }

    /**
     * Читает игрока
     *
     * @return возвращает игрока, not null
     * @throws IOException если считанный объект не Gamer
     */
    @Override
    public Gamer readGamer() throws IOException {
        Object object = readObj();

        if (object instanceof Gamer) {
            return new Gamer((Gamer) object);
        } else {
            throw new IOException("read not Gamer class object");
        }
    }

    /**
     * Считывает строку
     *
     * @return считанную строку, not null
     * @throws IOException когда считанный объект не является строкой
     */
    @Override
    public String readString() throws IOException {
        Object object = readObj();

        if (object instanceof String) {
            return (String) object;
        } else {
            throw new IOException("read not String class object");
        }
    }

    /**
     * Читает объект
     *
     * @return считанный объект, not null
     * @throws IOException когда не удается распознать класс объекта
     */
    private Object readObj() throws IOException {
        Object object;
        try {
            object = readObject();
        } catch (ClassNotFoundException e) {
            throw new IOException("invalid object");
        }

        return object;
    }

    private int readCondition() throws IOException {

        return read();
    }
}
