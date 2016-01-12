/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.socketmsg;

import game.bullet.Bullet;
import game.enums.Facing;
import game.enums.Gravity;
import game.level.LevelObject;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Asror
 */
public class MsgLocations implements Serializable {

    private static final long serialVersionUID = 1L;
    public Gravity gravity;
    public int playerid;
    public double health;
    public double damage;
    public int kills;
    public double damagedone;
    public float x;
    public float y;
    public Facing facing;
    public boolean crystal;

    public MsgLocations(Gravity gravity, int playerid, double health, double damage, int kills, double damagedone, float x, float y, Facing facing, boolean crystal) {
        this.gravity = gravity;
        this.playerid = playerid;
        this.health = health;
        this.damage = damage;
        this.kills = kills;
        this.damagedone = damagedone;
        this.x = x;
        this.y = y;
        this.facing = facing;
        this.crystal = crystal;
    }
    
    public String objectid;
    
    public MsgLocations(String objectid) {
        this.objectid = objectid;
    }
    
    public float crystalx;
    public float crystaly;
    
    public MsgLocations(float x, float y)
    {
        this.crystalx = x;
        this.crystaly = y;
    }
    
    
    public Bullet bull;
    
    public MsgLocations(Bullet bull)
    {
     this.bull = bull;   
     this.playerid = bull.getPlayerid();
    }

    @Override
    public String toString() {
        return "gravity=" + gravity.toString() + "p.id =" + playerid + "p.health = " + health;
    }

}
