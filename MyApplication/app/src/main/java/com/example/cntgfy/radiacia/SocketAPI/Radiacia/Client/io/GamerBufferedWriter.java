package com.example.cntgfy.radiacia.SocketAPI.Radiacia.Client.io;

import com.example.cntgfy.radiacia.SocketAPI.Radiacia.Game.Gamer;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * Created by Cntgfy on 21.07.2016.
 */
public class GamerBufferedWriter extends BufferedWriter implements GamerWriter {
    public GamerBufferedWriter(Writer out) {
        super(out);
    }

    public GamerBufferedWriter(Writer out, int size) {
        super(out, size);
    }

    public static final String GAMER_POINTER = "@gamer@";
    StringBuilder stringBuilder = new StringBuilder();

    @Override
    public void write(Gamer gamer) throws IOException {
        stringBuilder.delete(0, stringBuilder.length());
        stringBuilder.append(GAMER_POINTER).append(" ")
                     .append(gamer.getName()).append(" ")
                     .append(String.valueOf(gamer.getLatitude())).append(" ")
                     .append(String.valueOf(gamer.getLongitude())).append(" ")
                     .append(String.valueOf(gamer.getDirection())).append(" ")
                     .append(String.valueOf(gamer.isALive())).append(" ")
                     .append(String.valueOf(gamer.isShoot()));

        write(stringBuilder.toString());
        newLine();
        flush();
    }
}
