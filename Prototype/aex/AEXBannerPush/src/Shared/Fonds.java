/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Shared;
import java.io.Serializable;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
/**
 *
 * @author Dennis
 */
public class Fonds implements IFonds, Serializable{
    private String naam;
    private double koers;
    
    public Fonds(String naam, double koers)
    {
        this.naam = naam;
        this.koers = koers;
    }

    @Override
    public String getNaam() {
        return this.naam;
    }

    @Override
    public double getKoers() {
        return this.koers;
    }
    
    public void setKoers(double koers)
    {
        this.koers = koers;
    }
}
