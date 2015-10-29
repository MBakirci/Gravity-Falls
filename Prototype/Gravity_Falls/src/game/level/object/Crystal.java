/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.level.object;

import game.enums.Gravity;
import game.enums.Guns;
import game.level.LevelObject;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

/**
 *
 * @author Asror
 */
public class Crystal extends LevelObject {

    protected Animation animation;

    @SuppressWarnings("fallthrough")
    public Crystal(float x, float y) {
        super(x, y);
        try {
            SpriteSheet sp = new SpriteSheet("data/img/crystal/jewelry.png", 129, 65);

            animation = new Animation(new Image[]{sp.getSprite(0, 0), sp.getSprite(0, 1), sp.getSprite(0, 2)}, new int[]{125,125,125});
            animation.setAutoUpdate(true);

        } catch (SlickException se) {
            System.out.println(se.getMessage());
        }

        //we will just keep the default boundingrect of 32x32 for the objective
    }

    @Override
    public void render(float offset_x, float offset_y, Gravity gravity) {
        animation.draw(x - 2 - offset_x, y - 2 - offset_y);
    }

}
