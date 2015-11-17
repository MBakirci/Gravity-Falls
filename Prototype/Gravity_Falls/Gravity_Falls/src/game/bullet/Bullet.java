/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.bullet;

import game.enums.Gravity;
import game.level.LevelObject;
import game.physics.AABoundingRect;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

/**
 *
 * @author Richard
 */
public class Bullet {

    private Vector2f pos;
    private Vector2f speed;
    private int lived = 0;
    private boolean active = true;
    private static int MAX_LIFETIME = 1000;
    private Vector2f mousel;

    public Bullet(Vector2f pos, Vector2f speed, Vector2f mousel) {
        this.pos = pos;
        this.speed = speed;
        this.mousel = mousel;
        
    }

    public Bullet() {
        active = false;
    }

    public void update(int t) {
        if (active) {
            Vector2f realSpeed = speed.copy();
            realSpeed.scale(t / 1000.0f);

            
            // if a bullet is fired it is initialized like this
            float bulletX = pos.x;
            float bulletY = pos.y;

            // and you need to calculate the direction vector for it's movement
            float dirX = mousel.x - pos.x;
            float dirY = mousel.y - pos.y;
            // you need to "normalize" the direction vector to be able to use the speed variable
            float dirLength = (float) Math.sqrt(dirX * dirX + dirY * dirY);
            dirX = dirX / dirLength;
            dirY = dirY / dirLength;

            // now on every update, you can add up the direction * speed * timestep to the bullet
            bulletX = bulletX + (dirX * realSpeed.x);
            bulletY = bulletY + (dirY * realSpeed.x);

            pos.x = bulletX;
            pos.y = bulletY;
            
            //if(pos.)
            
            lived += t;
            if (lived > MAX_LIFETIME) {
                active = false;
            }
        }
    }

    
    public void render(GameContainer container, Graphics g) throws SlickException {
        if (active) {
            g.fillOval(pos.getX() - 20, pos.getY() - 20, 5, 5);
        }
    }

    public float getPosx() {
        return pos.x;
    }

    public float getPosy() {
        return pos.y;
    }
    
    

    public boolean isActive() {
        return active;
    }



}
