package game;

import game.server.gameserver;
import game.state.LevelState;
import java.awt.Font;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.state.StateBasedGame;

public class Game extends StateBasedGame {

    //set the window width and then the height according to a aspect ratio
    public static final int WINDOW_WIDTH = 1280;
    public static final int WINDOW_HEIGTH = WINDOW_WIDTH / 16 * 9;
    public static final boolean FULLSCREEN = false;

    //1280x720 is our base, we use 32x32 tiles but we want it to be 40x40 at 1280x720
    //so our base scale is not 1 but 1.25 actually
    public static final float SCALE = (float) (1.25 * ((double) WINDOW_WIDTH / 1280));
    public static final String GAME_NAME = "Gravity Falls";

    public static int SCRAPS_COLLECTED = 0;

    public Game() {
        super(GAME_NAME);
    }

    TextField text;
    UnicodeFont font;

    public void initStatesList(GameContainer gc) throws SlickException {

        //create a level state, this state will do the whole logic and rendering for individual levels
        addState(new LevelState("level_0"));
        font = getNewFont("Arial", 16);
        text = new TextField(gc, font, 150, 270, 200, 35);
        text = new TextField(gc, font, 150, 270, 200, 35);
        this.enterState(0);

    }

    public UnicodeFont getNewFont(String fontName, int fontSize) {
        font = new UnicodeFont(new Font(fontName, Font.PLAIN, fontSize));
        font.addGlyphs("@");
        font.getEffects().add(new ColorEffect(java.awt.Color.white));
        return (font);
    }

    static public gameserver server;

    public static void main(String[] args) throws SlickException {
        if (available(13000)) {
            //server = new gameserver();
        }
        AppGameContainer app = new AppGameContainer(new Game());

        //set the size of the display to the width and height and fullscreen or not
        app.setDisplayMode(WINDOW_WIDTH, WINDOW_HEIGTH, FULLSCREEN);

        //this will attempt to create a framerate of approximately 60 frames per second
        app.setTargetFrameRate(60);

        app.start();
    }

    public static boolean available(int port) {
        if (port < 12999 || port > 14000) {
            throw new IllegalArgumentException("Invalid start port: " + port);
        }

        ServerSocket ss = null;
        DatagramSocket ds = null;
        try {
            ss = new ServerSocket(port);
            ss.setReuseAddress(true);
            ds = new DatagramSocket(port);
            ds.setReuseAddress(true);
            return true;
        } catch (IOException e) {
        } finally {
            if (ds != null) {
                ds.close();
            }

            if (ss != null) {
                try {
                    ss.close();
                } catch (IOException e) {
                    /* should not be thrown */
                }
            }
        }

        return false;
    }

}
