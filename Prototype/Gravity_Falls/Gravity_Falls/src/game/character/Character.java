package game.character;

import java.util.HashMap;

import game.enums.Facing;
import game.enums.Gravity;
import game.level.LevelObject;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

public abstract class Character extends LevelObject {

    protected HashMap<Facing, HashMap<Gravity, Image>> sprites;

    protected HashMap<Facing, HashMap<Gravity, Animation>> movingAnimations;
    protected Facing facing;
    protected boolean moving = false;
    protected float accelerationSpeed = 1;
    protected float maximumSpeed = 1;
    protected boolean isStartingJump = false;
    protected boolean crystal;
    protected int PlayerId;
    protected Gravity gravity = Gravity.DOWN;
    protected int points;
    protected double health;
    protected int kills;
    protected double damagedone;
    protected double damage;

    public Character(float x, float y) throws SlickException {
        super(x, y);
        //in case we forget to set the image, we don't want the game to crash, but it still has to be obvious that something was forgotten
        setSprite(new Image("data/img/placeholder_sprite.png"));

        //default direction will be right
        facing = Facing.RIGHT;
        health = 100;
        points = 0;
        damage = 10;
        damagedone = 0;
    }

    //this method has become a bit of monster :(
    protected void setMovingAnimation(Image[] images, int frameDuration) throws SlickException {
               movingAnimations = new HashMap<Facing, HashMap<Gravity, Animation>>();
        movingAnimations.put(Facing.LEFT, new HashMap<Gravity, Animation>());
        movingAnimations.put(Facing.RIGHT, new HashMap<Gravity, Animation>());

        //we can just put the right facing in with the default images
        movingAnimations.get(Facing.RIGHT).put(Gravity.DOWN, new Animation(images, frameDuration));

        Animation animation;

        animation = new Animation();
        for (Image i : images) {
            animation.addFrame(i.getFlippedCopy(false, true), frameDuration);
        }
        movingAnimations.get(Facing.RIGHT).put(Gravity.UP, animation);

        //and for the left facing ones...
        animation = new Animation();
        for (Image i : images) {
            animation.addFrame(i.getFlippedCopy(true, false), frameDuration);
        }
        movingAnimations.get(Facing.LEFT).put(Gravity.DOWN, animation);

        animation = new Animation();
        for (Image i : images) {
            animation.addFrame(i.getFlippedCopy(true, true), frameDuration);
        }
        movingAnimations.get(Facing.LEFT).put(Gravity.UP, animation);
    }

    protected void setSprite(Image i) throws SlickException {
        sprites = new HashMap<Facing, HashMap<Gravity, Image>>();
        sprites.put(Facing.LEFT, new HashMap<Gravity, Image>());
        sprites.put(Facing.RIGHT, new HashMap<Gravity, Image>());

        Image i2;

        sprites.get(Facing.RIGHT).put(Gravity.UP, i.getFlippedCopy(false, true));
        sprites.get(Facing.RIGHT).put(Gravity.DOWN, i);

        sprites.get(Facing.LEFT).put(Gravity.UP, i.getFlippedCopy(true, true));
        sprites.get(Facing.LEFT).put(Gravity.DOWN, i.getFlippedCopy(true, false));
    }

    public boolean isMoving() {
        return moving;
    }

    public int getKills() {
        return kills;
    }

    public void addKills() {
        this.kills++;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public double getDamagedone() {
        return damagedone;
    }

    public void addDamagedone(double damage) {
        this.damagedone = this.damagedone + damage;
    }

    public Facing getFacing() {
        return facing;
    }

    public void setFacing(Facing facing) {
        this.facing = facing;
    }

    public int getPlayerId() {
        return PlayerId;
    }

    public Gravity getGravity() {
        return gravity;
    }

    public void setGravity(Gravity gravity) {
        this.gravity = gravity;

    }

    public void setMoving(boolean b) {
        moving = b;
    }

    public void jump(Gravity gravity) {
        if (onGround) {
            switch (gravity) {
                case DOWN:
                    y_velocity = -0.4f;
                    break;
                case UP:
                    y_velocity = 0.4f;
                    break;
            }
        }
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public void takedamage(double damage) {
        this.health = this.health - damage;
    }

    public void setCrystal(boolean crystal) {
        this.crystal = crystal;
    }

    public boolean getCrystal() {
        return crystal;
    }

    public boolean isCrystal() {
        return crystal;
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

    public void updatemodel(String sheet) throws SlickException {
        SpriteSheet sp = new SpriteSheet("data/img/characters/player/" + sheet, 36, 36);
        setSprite(sp.getSprite(1, 0));
        setMovingAnimation(new Image[]{sp.getSprite(1, 0), sp.getSprite(0, 0), sp.getSprite(2, 0)}, 400);
    }

    public void moveLeft(int delta, Gravity gravity) {
        //if we aren't already moving at maximum speed
        switch (gravity) {
            case UP:
            case DOWN:
                if (x_velocity > -maximumSpeed) {
                    //accelerate
                    x_velocity -= accelerationSpeed * delta;
                    if (x_velocity < -maximumSpeed) {
                        //and if we exceed maximum speed, set it to maximum speed
                        x_velocity = -maximumSpeed;
                    }
                }
                break;
        }
        moving = true;
        facing = Facing.LEFT;
    }

    public void moveRight(int delta, Gravity gravity) {
        switch (gravity) {
            case UP:
            case DOWN:
                if (x_velocity < maximumSpeed) {
                    x_velocity += accelerationSpeed * delta;
                    if (x_velocity > maximumSpeed) {
                        x_velocity = maximumSpeed;
                    }
                }
                break;

        }

        moving = true;
        facing = Facing.RIGHT;
    }

    public void render(float offset_x, float offset_y, Gravity gravity) {
        
        this.updateCrystal(this.getCrystal());
        
        //draw a moving animation if we have one and we moved within the last 150 miliseconds
        if (movingAnimations != null && moving) {
            movingAnimations.get(facing).get(gravity).draw(x - 2 - offset_x, y - 2 - offset_y);
        } else {
            sprites.get(facing).get(gravity).draw(x - 2 - offset_x, y - 2 - offset_y);
        }
    }

    public void AddPoints(int point) {
        points = points + point;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public double getDamage() {
        return damage;
    }

    public void setDamage(double damage) {
        this.damage = damage;
    }

}
