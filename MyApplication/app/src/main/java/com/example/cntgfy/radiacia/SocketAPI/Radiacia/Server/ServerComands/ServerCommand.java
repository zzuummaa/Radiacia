package com.example.cntgfy.radiacia.SocketAPI.Radiacia.Server.ServerComands;

/**
 * Created by Cntgfy on 06.07.2016.
 */
public interface ServerCommand extends Command {
    public static final String EXIT = "exit";
    public static final String SERVER_STOP = "serverEnabled=false";
    public static final String SERVER_START = "serverEnabled=true";
}
