/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.socket;

/**
 *
 * @author Wout
 */
public class Player {
    private Boolean adminBoolean;
    private String NaamString;

    public Player(Boolean adminBoolean, String NaamString) {
        this.adminBoolean = adminBoolean;
        this.NaamString = NaamString;
    }
    
    public boolean GetAdmin()
    {
        return this.adminBoolean;
    }
    
    public String GetNaam()
    {
        return this.NaamString;
    }
}
