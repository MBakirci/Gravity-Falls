package game.controller;

import game.character.Player;
import game.level.Level;
import game.character.Character;
import java.util.ArrayList;

import org.newdawn.slick.Input;

public abstract class PlayerController {
    
    protected Player player;
    
    public PlayerController(Player player){
        this.player = player;
    }
    
    public abstract void handleInput(Input i, int delta, ArrayList<Character> characters, int Spectator);

}
