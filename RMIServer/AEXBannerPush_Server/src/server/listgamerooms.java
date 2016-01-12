/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import shared.IGameRoom;
import shared.ILobby;
import fontys.observer.BasicPublisher;
import fontys.observer.RemotePropertyListener;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author asror
 */
public class listgamerooms extends UnicastRemoteObject implements ILobby {

    public ArrayList<IGameRoom> gamerooms;
    private BasicPublisher publisher;

    public listgamerooms() throws RemoteException {
        publisher = new BasicPublisher(new String[]{});
        gamerooms = new ArrayList<>();

    }

    @Override
    public void addListener(RemotePropertyListener rl, String string) throws RemoteException {
        publisher.addProperty(string);
        publisher.addListener(rl, string);
        System.out.println("added listener --" + string + "eee" + rl.toString());
    }

    @Override
    public void removeListener(RemotePropertyListener rl, String string) throws RemoteException {
        publisher.removeProperty(string);
        publisher.removeListener(rl, string);
    }

    @Override
    public List<IGameRoom> getGameRooms() throws RemoteException {
        return gamerooms;
    }

    @Override
    public synchronized void addGameRoom(String name, int players, String host) throws RemoteException {
        gamerooms.add(new GameRoom(name, players, host));

        Iterator it = publisher.getProperties();

        while (it.hasNext()) {

            if (it != null || it.next() != null) {
                try {
                    publisher.inform(this, (String) it.next(), null, gamerooms);
                } catch (Exception e) {

                }
            }
        }

    }

}
