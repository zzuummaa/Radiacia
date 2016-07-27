package com.example.cntgfy.radiacia.SocketAPI.Radiacia.Server;

/**
 * Created by Cntgfy on 02.07.2016.
 */
public class IDManager {
    private int maxID;

    public IDManager() {
        this.maxID = -1;
    }

    public int getID(){
        maxID++;
        return maxID;
    }
}
