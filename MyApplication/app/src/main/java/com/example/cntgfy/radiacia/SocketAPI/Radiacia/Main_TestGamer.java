package com.example.cntgfy.radiacia.SocketAPI.Radiacia;

import com.example.cntgfy.radiacia.SocketAPI.Radiacia.Server.IDManager;
import com.example.cntgfy.radiacia.SocketAPI.Radiacia.Game.Gamer;

/**
 * Created by Cntgfy on 08.07.2016.
 */
public class Main_TestGamer {
    public static void main(String[] args) {
        System.out.println("TEST GAMER");
        System.out.println("-----------------------------------------------------------------------------------------");
        IDManager idManager = new IDManager();
        Gamer gamer1 = new Gamer(String.valueOf(idManager.getID()), 10, 10, 45);
        Gamer gamer2 = new Gamer(String.valueOf(idManager.getID()), 50, 50, 45);
        Gamer gamer3 = new Gamer(String.valueOf(idManager.getID()), 50, 65, 45);

        gamer1.shoot(gamer2);
        System.out.println("gamer2 mast die!!!: " + gamer2);

        gamer1.shoot(gamer3);
        System.out.println("gamer3 is Alive!!!: " + gamer3);
        System.out.println("-----------------------------------------------------------------------------------------");
    }
}
