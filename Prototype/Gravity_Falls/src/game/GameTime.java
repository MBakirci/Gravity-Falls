/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import java.sql.Time;
import game.character.Character;
import java.util.ArrayList;
import java.util.TimerTask;

/**
 *
 * @author Asror
 */
public class GameTime extends TimerTask {

    private int count;
    private ArrayList<Character> characters;

    public GameTime(int count, ArrayList<Character> characters) {
        this.count = count;
        this.characters = characters;
    }

    @Override
    public void run() {
        count--;
        if (count == 0) {
            cancel();
        }
        for (Character c : characters) {
            if(c.isCrystal())
            {
             c.AddPoints();
            }
        }
    }

    public int getCount() {
        return count;
    }

}
