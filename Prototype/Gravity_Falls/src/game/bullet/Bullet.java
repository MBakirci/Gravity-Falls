/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.bullet;

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
     private static int MAX_LIFETIME =1000;
     
     public Bullet ( Vector2f pos, Vector2f speed)
     {
         this.pos = pos;
         this.speed = speed;
     }
     
     public Bullet ()
     {
         active = false;
     }
     
     public void update( int t)
     {
         if (active)
         {
             Vector2f realSpeed = speed.copy();
             realSpeed.scale(t/1000.0f);
             pos.add(realSpeed);
             
             lived += t;
             if( lived > MAX_LIFETIME)
             {
                 active = false;
             }
         }
     }
         
   // moet met sprites doe ik wel later
    public void render(GameContainer container, Graphics g) throws SlickException 
    {
        if(active)
        {
          g.fillOval(pos.getX()-20, pos.getY()-20, 5, 5);
        }
    }
    
    public boolean isActive()
    {
        return active;
    }
     
    
}
