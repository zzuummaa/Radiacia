package com.example.cntgfy.radiacia.SocketAPI.Radiacia.Client.io;

import com.example.cntgfy.radiacia.SocketAPI.Radiacia.Game.Gamer;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

/**
 * Created by Cntgfy on 21.07.2016.
 */
public class GamerObjectWriter extends ObjectOutputStream implements GamerWriter {
    private ObjectOutputStream out;

    public GamerObjectWriter(OutputStream output) throws IOException {
        super(output);
    }

    @Override
    public void write(Gamer gamer) throws IOException {
        out.writeObject(gamer);
    }

    @Override
    public void write(String string) throws IOException {
        out.writeObject(string);
    }


}
