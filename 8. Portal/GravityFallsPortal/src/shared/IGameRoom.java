/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shared;

import java.io.Serializable;
import java.rmi.RemoteException;

/**
 *
 * @author Asror
 */
public interface IGameRoom extends Serializable {

    public int getId();

    public String getName();

    public int getMaxPlayers();

    public int getAvailablePlayers();

    public void setAvailablePlayers();

    public String getRoomname();

    public int getRoomMaxPlayers();
    
    public String getHost();

}
