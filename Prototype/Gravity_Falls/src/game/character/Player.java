package game.character;

import game.physics.AABoundingRect;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

public class Player extends Character {

    public Player(float x, float y) throws SlickException {
        super(x, y);
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

    public void updateBoundingShape() {
        boundingShape.updatePosition(x + 3, y);
    }

}
