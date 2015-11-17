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
import javafx.application.Application;
import javafx.stage.Stage;
import org.newdawn.slick.GameContainer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

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
             c.AddPoints(5);
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
