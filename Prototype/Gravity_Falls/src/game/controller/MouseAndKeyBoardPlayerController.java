package game.controller;

import game.character.Player;
import game.enums.Gravity;
import game.level.Level;
import java.util.Timer;
import java.util.logging.Logger;

import org.newdawn.slick.Input;

public class MouseAndKeyBoardPlayerController extends PlayerController {

    boolean cooldown = true;
    
    public MouseAndKeyBoardPlayerController(Player player) {
        super(player);
    }

    public void handleInput(Input i, int delta, Level level) {
        //handle any input from the keyboard
        handleKeyboardInput(i, delta, level);
    }

    private void handleKeyboardInput(Input i, int delta, Level level) {
        //we can both use the arrow keys to move around, obviously we can't move both left and right simultaneously
        switch (level.getCurrentGravity()) {
            case UP:
            case DOWN:
                if (i.isKeyDown(Input.KEY_A)) {
                    player.moveLeft(delta, level.getCurrentGravity());
                } else if (i.isKeyDown(Input.KEY_D)) {
                    player.moveRight(delta, level.getCurrentGravity());
                } else {
                    //we dont move if we don't press left or right, this will have the effect that our player decelerates
                    player.setMoving(false);
                }
                break;
        }

//        if (i.isKeyPressed(Input.KEY_SPACE)) {
//            player.jump(level.getCurrentGravity());
//        }
        //switching the gravity device clockwise
        if (i.isKeyPressed(Input.KEY_SPACE)) {
            //down becomes left, left becomes up, up becomes right and right becomes down
            switch (level.getCurrentGravity()) {
                case DOWN:
                    level.setGravity(Gravity.UP);
                    break;
                case UP:
                    level.setGravity(Gravity.DOWN);
                    break;
            }
        }
    }
    
    public long getTime(){
        return 0;
    }

}
