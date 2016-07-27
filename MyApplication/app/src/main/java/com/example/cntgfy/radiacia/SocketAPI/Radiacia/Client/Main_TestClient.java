package com.example.cntgfy.radiacia.SocketAPI.Radiacia.Client;

import com.example.cntgfy.radiacia.SocketAPI.Radiacia.Client.io.GamerBufferedReader;
import com.example.cntgfy.radiacia.SocketAPI.Radiacia.Client.io.GamerBufferedWriter;
import com.example.cntgfy.radiacia.SocketAPI.Radiacia.Client.io.GamerObjectReader;
import com.example.cntgfy.radiacia.SocketAPI.Radiacia.Client.io.GamerObjectWriter;
import com.example.cntgfy.radiacia.SocketAPI.Radiacia.Game.Gamer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * Created by Cntgfy on 21.07.2016.
 */
public class Main_TestClient {
    public static void main(String[] args) throws IOException {

        Gamer writeGamer = new Gamer("Sange", 9d, 10d, 11f);
        writeGamer.setAccuracy(8f);
        System.out.println("------------------------------------------------------------------------------------------");
        testGamerBufferedWriterAndReader(writeGamer);
        System.out.println("------------------------------------------------------------------------------------------");
        testGamerInputAndOutputStreams(writeGamer);
        System.out.println("------------------------------------------------------------------------------------------");

    }

    private static void printInfo(Gamer writeGamer, Gamer readGamer) {
        System.out.println("write: " + writeGamer);
        System.out.println("read:  " + readGamer);
    }


    private static void testGamerBufferedWriterAndReader(Gamer writeGamer) throws IOException {
        System.out.println("Test GamerBufferedWriter and GamerBufferedReader:");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        GamerBufferedWriter writer = new GamerBufferedWriter(new PrintWriter(byteArrayOutputStream));
        writer.write(writeGamer);

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        GamerBufferedReader reader = new GamerBufferedReader(new InputStreamReader(byteArrayInputStream));
        Gamer readGamer = reader.readGamer();

        printInfo(writeGamer, readGamer);
    }

    private static void testGamerInputAndOutputStreams(Gamer writeGamer) throws IOException {
        System.out.println("Test GamerOutputStream and GamerInputStream");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        GamerObjectWriter gamerWriter = new GamerObjectWriter(byteArrayOutputStream);

        gamerWriter.write(writeGamer);
        String writeString = "fuck eah!";
        gamerWriter.write(writeString);

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        GamerObjectReader gamerReader = new GamerObjectReader(byteArrayInputStream);

        Gamer readGamer = gamerReader.readGamer();
        String readString = gamerReader.readString();

        System.out.println("write: " + writeString);
        System.out.println("read:  " + readString);
        printInfo(writeGamer, readGamer);
    }
}
