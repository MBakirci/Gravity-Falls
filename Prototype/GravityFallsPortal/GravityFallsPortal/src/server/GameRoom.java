/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import shared.IGameRoom;

/**
 *
 * @author Asror
 */
public class GameRoom implements IGameRoom {

    private int id = 1;
    private int currentID;
    private transient SimpleStringProperty name;
    private transient SimpleIntegerProperty maxPlayers;
    private String roomname;
    private int roommaxplayer;
    private int availablePlayers = 0;
    private String host;

    public int getId() {
        return currentID;
    }

    public String getName() {
        return new SimpleStringProperty(roomname).get();
    }

    public String getRoomname() {
        return roomname;
    }
    

    public int getMaxPlayers() {
        return new SimpleIntegerProperty(roommaxplayer).get();
    }
    
    public int getRoomMaxPlayers()
    {
        return this.roommaxplayer;
    }

    public int getAvailablePlayers() {
        return availablePlayers;
    }

    public void setAvailablePlayers() {
        this.availablePlayers++;
    }

    public String getHost() {
        return host;
    }
    
    

    public GameRoom(String name, int maxPlayer, String host) {
        this.currentID = id++;
        this.name = new SimpleStringProperty(name);
        this.roomname = name;
        this.roommaxplayer = maxPlayer;
        this.maxPlayers = new SimpleIntegerProperty(maxPlayer);
        this.host = host;
    }

}
