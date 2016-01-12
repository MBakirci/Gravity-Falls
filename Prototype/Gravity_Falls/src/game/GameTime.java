/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import game.character.Character;
import java.util.ArrayList;
import java.util.TimerTask;
import org.newdawn.slick.GameContainer;

/**
 *
 * @author Asror
 */
public class GameTime extends TimerTask {

    private int count;
    private ArrayList<Character> characters;
    private GameContainer container;

    public GameTime(int count, ArrayList<Character> characters, GameContainer container) {
        this.count = count;
        this.characters = characters;
        this.container = container;
    }

    @Override
    public void run() {
        if (count <= 0) {
            container.setPaused(true);
            endofgame = true;
        } else {
            count--;
            for (Character c : characters) {
                if (c.isCrystal()) {
                    c.AddPoints(5);
                }
            }
        }
    }

    boolean endofgame = false;

    public boolean isEndofgame() {
        return endofgame;
    }

    public int getCount() {
        return count;
    }

}
