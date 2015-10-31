package game.controller;

import game.character.Player;
import game.character.Character;
import game.enums.Gravity;
import game.level.Level;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import org.newdawn.slick.Input;

public class MouseAndKeyBoardPlayerController extends PlayerController {

    boolean cooldown = false;
    String lastPressedButton = "none";

    public MouseAndKeyBoardPlayerController(Player player) {
        super(player);
    }

    public void handleInput(Input i, int delta, ArrayList<Character> characters) {
        //handle any input from the keyboard
        handleKeyboardInput(i, delta, characters);
    }

    private void handleKeyboardInput(Input i, int delta, ArrayList<Character> characters) {
        //we can both use the arrow keys to move around, obviously we can't move both left and right simultaneously
        switch (player.getGravity()) {
            case UP:
            case DOWN:
                if (i.isKeyDown(Input.KEY_A)) {
                    player.moveLeft(delta, player.getGravity());
                } else if (i.isKeyDown(Input.KEY_D)) {
                    player.moveRight(delta, player.getGravity());
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
            switch (player.getGravity()) {
                case DOWN:
                    if (lastPressedButton.equals("none")) {
                        player.setGravity(Gravity.UP);
                        lastPressedButton = "pressed";
                        resetLastPressedButton();
                    }

                    break;
                case UP:
                    if (lastPressedButton.equals("none")) {
                        player.setGravity(Gravity.DOWN);
                        lastPressedButton = "pressed";
                        resetLastPressedButton();
                    }
                    break;
            }

        }

    }

    public void resetLastPressedButton() {
        if (lastPressedButton != "none") {
            Timer timer = new Timer();
            timer.schedule(new ResetLastButtonTimer(), 500);
        }
    }

    class ResetLastButtonTimer extends TimerTask {

        @Override
        public void run() {
            lastPressedButton = "none";
        }
    }

    public long getTime() {
        return 0;
    }

}
