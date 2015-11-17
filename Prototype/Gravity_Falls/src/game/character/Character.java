package game.character;

import game.bullet.Bullet;
import java.util.HashMap;

import game.enums.Facing;
import game.enums.Gravity;
import game.level.LevelObject;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

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
    protected int points = 0;
    private int health;

    public Character(float x, float y) throws SlickException {
        super(x, y);
        //in case we forget to set the image, we don't want the game to crash, but it still has to be obvious that something was forgotten
        setSprite(new Image("data/img/placeholder_sprite.png"));
        health = 100;
        //default direction will be right
        facing = Facing.RIGHT;
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
        System.out.println(animation);
        movingAnimations.get(Facing.RIGHT).put(Gravity.UP, animation);

        //and for the left facing ones...
        animation = new Animation();
        for (Image i : images) {
            animation.addFrame(i.getFlippedCopy(true, false), frameDuration);
        }
        System.out.println(animation);
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

    public int getHealth()
    {
        return health;
    }
    
    public int dmgCalc()
    {
        Bullet b = new Bullet();
        return   health-=b.getDamage();
    }
    
    public int resetHealth()
    {
        return health = 100;
    }
    
    
    public boolean isMoving() {
        return moving;
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

    public void setCrystal(boolean crystal) {
        this.crystal = crystal;
    }

    public boolean isCrystal() {
        return crystal;
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

        //draw a moving animation if we have one and we moved within the last 150 miliseconds
        if (movingAnimations != null && moving) {
            movingAnimations.get(facing).get(gravity).draw(x - 2 - offset_x, y - 2 - offset_y);
        } else {
            sprites.get(facing).get(gravity).draw(x - 2 - offset_x, y - 2 - offset_y);
        }
    }

    public void AddPoints() {
        points = points + 5;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

}
