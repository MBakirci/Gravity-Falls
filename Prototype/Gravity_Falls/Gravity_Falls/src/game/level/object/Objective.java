package game.level.object;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import game.enums.Gravity;
import game.enums.Guns;
import game.level.LevelObject;
import org.newdawn.slick.SpriteSheet;

public class Objective extends LevelObject {

    protected Animation animation;
    private Guns gun;

    @SuppressWarnings("fallthrough")
    public Objective(float x, float y, Guns gun) throws SlickException {
        super(x, y);
        try {
            SpriteSheet sp = new SpriteSheet("data/img/objects/Arms.png", 134, 32);

            //add the right animation for this objective
            switch (gun) {
                case Handgun:
                    animation = new Animation(new Image[]{sp.getSprite(0, 0), sp.getSprite(0, 0)}, new int[]{1000, 1000});
                    animation.setAutoUpdate(false);
                case P90:
                    animation = new Animation(new Image[]{sp.getSprite(0, 2), sp.getSprite(0, 2)}, new int[]{1000, 1000});
                    animation.setAutoUpdate(false);
                    
            }
            

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
