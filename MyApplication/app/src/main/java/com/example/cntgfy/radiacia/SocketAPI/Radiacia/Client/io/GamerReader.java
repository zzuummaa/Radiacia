package com.example.cntgfy.radiacia.SocketAPI.Radiacia.Client.io;

import com.example.cntgfy.radiacia.SocketAPI.Radiacia.Game.Gamer;

import java.io.IOException;

/**
 * Created by Cntgfy on 21.07.2016.
 *  Класс, позволяющий читать состояние игрока
 */
public interface GamerReader extends GameReader{

    /**
     * Читает игрока
     *
     * @return если удалось считать, то возвращает игрока
     * @throws IOException когда объект невозможно считать
     *                 или когда объект не Gamer
     */
    Gamer readGamer() throws IOException;

}
