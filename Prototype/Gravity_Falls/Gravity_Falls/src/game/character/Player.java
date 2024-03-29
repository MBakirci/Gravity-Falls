package game.character;

import game.enums.Facing;
import game.enums.Gravity;
import game.physics.AABoundingRect;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

public class Player extends Character {

    private int playerid;

    public Player(float x, float y, int playerid) throws SlickException {
        super(x, y);
        this.playerid = playerid;
        gravity = Gravity.DOWN;

        try {

            SpriteSheet sp = new SpriteSheet("data/img/characters/player/Spacece.png", 36, 36);
            setSprite(sp.getSprite(1, 0));
            setMovingAnimation(new Image[]{sp.getSprite(1, 0), sp.getSprite(0, 0), sp.getSprite(2, 0)}, 400);
        } catch (SlickException e) {
            System.out.println(e.getMessage());
        }

        boundingShape = new AABoundingRect(x + 3, y, 26, 32);

        accelerationSpeed = 0.001f;
        maximumSpeed = 0.15f;
        maximumFallSpeed = 0.3f;
        decelerationSpeed = 0.001f;
    }

    public void updatemodel(String sheet) throws SlickException {
        SpriteSheet sp = new SpriteSheet("data/img/characters/player/" + sheet, 36, 36);
        setSprite(sp.getSprite(1, 0));
        setMovingAnimation(new Image[]{sp.getSprite(1, 0), sp.getSprite(0, 0), sp.getSprite(2, 0)}, 400);
    }

    public void updateBoundingShape() {
        boundingShape.updatePosition(x + 3, y);
    }

    @Override
    public Facing getFacing() {
        return facing;
    }

    @Override
    public int getKills() {
        return kills;
    }

    @Override
    public void setKills(int kills) {
        this.kills = kills;
    }

    @Override
    public void addKills() {
        this.kills++;
    }

    @Override
    public double getDamagedone() {
        return damagedone;
    }

    @Override
    public void addDamagedone(double damage) {
        this.damagedone = this.damagedone + damage;
    }

    @Override
    public double getHealth() {
        return health;
    }

    @Override
    public void setHealth(double health) {
        this.health = health;
    }

    @Override
    public double getDamage() {
        return damage;
    }

    @Override
    public void setDamage(double damage) {
        this.damage = damage;
    }

    @Override
    public void takedamage(double damage) {
        this.health = this.health - damage;
    }

    @Override
    public void setCrystal(boolean crystal) {
        this.crystal = crystal;
    }

    public void updateCrystal(boolean crystal) {
        this.crystal = crystal;
        if (crystal) {
            try {
                updatemodel("SpaceceCryst.png");
            } catch (SlickException ex) {
                Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                updatemodel("Spacece.png");
            } catch (SlickException ex) {
                Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    @Override
    public boolean getCrystal() {
        return crystal;
    }

    @Override
    public int getPlayerId() {
        return playerid;
    }

    @Override
    public Gravity getGravity() {
        return gravity;
    }

    @Override
    public void setGravity(Gravity gravity) {
        this.gravity = gravity;
    }

}
