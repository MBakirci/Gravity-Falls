/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import game.character.Player;
import game.client.PortalController;
import game.level.Level;
import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.TimerTask;
import java.util.logging.Logger;
import org.newdawn.slick.GameContainer;

/**
 *
 * @author Asror
 */
public class UpdateGameLocations extends TimerTask {

    private int count;
    private ArrayList<game.character.Character> characters;
    private GameContainer container;
    private Player player;
    private PortalController control;

    public UpdateGameLocations(ArrayList<game.character.Character> characters, Player player, Level level, PortalController control) {
        this.player = player;
        this.control = control;

    }

    @Override
    public void run() {
       

        control.clientsend(player.getGravity(), player.getPlayerId(), player.getHealth(), player.getDamage(), player.getKills(), player.getDamagedone(), player.getX(), player.getY(), player.getFacing(), player.getCrystal());

    }

}
