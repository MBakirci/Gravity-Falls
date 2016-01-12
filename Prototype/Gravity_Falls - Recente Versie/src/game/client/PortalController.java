/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.client;

import game.bullet.Bullet;
import game.character.Player;
import game.enums.Facing;
import game.socketmsg.Message;
import game.socketmsg.MsgLocations;
import game.enums.Gravity;
import game.level.LevelObject;
import game.state.LevelState;
import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

/**
 * FXML Controller class
 *
 * @author Asror
 */
public class PortalController {

    public SocketClient client;
    public int port;
    public String serverAddr, username, password;
    public Thread clientThread;
    public DefaultListModel model;
    public File file;
    public String historyFile = "D:/History.xml";
    private LevelState levelstate;

    public boolean isWin32() {
        return System.getProperty("os.name").startsWith("Windows");
    }

    private game.level.Level level;

    public PortalController(game.level.Level level, LevelState levelstate) {
        this.level = level;
        this.levelstate = levelstate;
        serverAddr = "145.93.173.58";
        port = 13000;
        try {
            username = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException ex) {
            Logger.getLogger(PortalController.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            client = new SocketClient(this);
            clientThread = new Thread(client);
            clientThread.start();

        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public void clientsend(Gravity gravity, int playerid, double health, double damage, int kills, double damagedone, float x, float y, Facing facing, boolean crystal) {
        client.send(new MsgLocations(gravity, playerid, health, damage, kills, damagedone, x, y, facing, crystal));
    }

    public void clientsendqueue(String id) {
        client.send(new MsgLocations(id));
    }

    public void clientsendbullet(Bullet bull) {
        client.send(new MsgLocations(bull));
    }
    
    public void clientsendcrystal(float x, float y)
    {
        client.send(new MsgLocations(x, y));
    }
    
    public void addCrystal(MsgLocations mlocc)
    {
        MsgLocations mloc = mlocc;
        if(level != null)
        {
            level.respawnCrystal(mloc.crystalx, mloc.crystaly);
        }
    }

    public void addchars(MsgLocations mlocc) throws SlickException {
        MsgLocations mloc = mlocc;
        if (level != null) {
            level.updatelocation(mloc);
        }
    }

    public void removeObjects(MsgLocations mlocc) {
        MsgLocations mloc = mlocc;
        if (level != null) {
            level.updateobjects(mloc);
        }
    }

    public void addBullet(MsgLocations mlocc) {
        MsgLocations mloc = mlocc;
        if (levelstate != null) {
            levelstate.AddBullets(mloc.bull);
        }
    }

}
