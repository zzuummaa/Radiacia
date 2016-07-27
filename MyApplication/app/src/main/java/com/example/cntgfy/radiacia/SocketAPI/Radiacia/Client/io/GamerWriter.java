package com.example.cntgfy.radiacia.SocketAPI.Radiacia.Client.io;

import com.example.cntgfy.radiacia.SocketAPI.Radiacia.Game.Gamer;

import java.io.IOException;

/**
 * Created by Cntgfy on 21.07.2016.
 */
public interface GamerWriter extends GameWriter {
    void write(Gamer gamer) throws IOException;
}
