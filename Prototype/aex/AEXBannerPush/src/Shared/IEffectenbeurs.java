/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Shared;

import fontys.observer.RemotePublisher;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 *
 * @author Dennis
 */
public interface IEffectenbeurs extends Remote, RemotePublisher{
    public ArrayList<Fonds> getKoersen() throws RemoteException;
}
