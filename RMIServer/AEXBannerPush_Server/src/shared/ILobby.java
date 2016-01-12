/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shared;

import fontys.observer.RemotePublisher;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Asror
 */
public interface ILobby extends RemotePublisher {

    public List<IGameRoom> getGameRooms() throws RemoteException;

    public void addGameRoom(String name, int players, String host) throws RemoteException;
}
