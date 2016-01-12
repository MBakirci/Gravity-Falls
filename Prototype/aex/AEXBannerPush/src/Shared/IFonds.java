/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Shared;
import java.rmi.Remote;
/**
 *
 * @author Dennis
 */
public interface IFonds extends Remote {
    public String getNaam();
    public double getKoers();
}
