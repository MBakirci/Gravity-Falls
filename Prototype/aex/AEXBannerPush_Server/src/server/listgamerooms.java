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
    private Timer fondstimer;
    private BasicPublisher publisher;
    private int gameroomssize;

    public listgamerooms() throws RemoteException {
        publisher = new BasicPublisher(new String[]{"rooms"});
        gamerooms = new ArrayList<>();
        gamerooms.add(new GameRoom("ey",4,"127.0.0.1"));
        gameroomssize = 0;
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new GenerateKoersen(), 0, 5000);

    }

    @Override
    public void addListener(RemotePropertyListener rl, String string) throws RemoteException {
        publisher.addListener(rl, string);
    }

    @Override
    public void removeListener(RemotePropertyListener rl, String string) throws RemoteException {
        publisher.addListener(rl, string);
    }

    @Override
    public List<IGameRoom> getGameRooms() throws RemoteException {
        return gamerooms;
    }

    @Override
    public void addGameRoom(String name, int players, String host) throws RemoteException {
        System.out.println(name);
        System.out.println(players);
        System.out.println(host);
        gamerooms.add(new GameRoom(name, players, host));
    }

    class GenerateKoersen extends TimerTask {

        @Override
        public void run() {
            if (gameroomssize != gamerooms.size()) {
                gameroomssize = gamerooms.size();
                publisher.inform(this, "rooms", null, gamerooms);
            }
        }
    }
}
