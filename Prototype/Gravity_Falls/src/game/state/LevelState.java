package game.state;

import game.Game;
import game.GameTime;
import game.bullet.Bullet;
import game.character.Player;
import game.controller.MouseAndKeyBoardPlayerController;
import game.controller.PlayerController;
import game.level.Level;
import game.physics.Physics;
import java.awt.Rectangle;
import java.awt.SystemColor;
import java.util.Iterator;
import java.util.LinkedList;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;
import org.newdawn.slick.AppGameContainer;

import java.util.Timer;

import org.newdawn.slick.Color;

import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.KeyListener;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class LevelState extends BasicGameState {

    private Level level;
    private String startinglevel;
    private Player player;
    private Player p;
    private PlayerController playerController;
    private Physics physics;
    TextField chatlog;
    TextField textmessage;
    TrueTypeFont font;
    Input i;
    boolean typing = false;

    private Random r = new Random();
    private Timer t = new Timer();
    

    //BULLET: stuk moet apart voor character, doe ik wel later 
    private LinkedList<Bullet> bullets;
    protected int DELAY = 800;
    protected int deelta = 0;

    public LevelState(String startingLevel) {
        this.startinglevel = startingLevel;
    }

    public void init(GameContainer container, StateBasedGame sbg) throws SlickException {

        //at the start of the game we don't have a player yet
        player = new Player(128, 415, 1);
        p = new Player((r.nextInt(200)+ 200), 800, 2);
        
        
        //once we initialize our level, we want to load the right level
        level = new Level(startinglevel, player);
        
        level.addCharacter(p);
        //and we create a controller, for now we use the MouseAndKeyBoardPlayerController
        playerController = new MouseAndKeyBoardPlayerController(player);

        physics = new Physics(player);

        font = new TrueTypeFont(new java.awt.Font(java.awt.Font.SERIF, java.awt.Font.BOLD, 13), false);
        chatlog = new TextField(container, font, 910, container.getHeight() - 721, 273, container.getHeight() - 250);
        chatlog.setText("AWSOME");


        


        //BULLET: stuk moet apart voor character, doe ik wel later 

        bullets = new LinkedList<Bullet>();

    }
   
	
    public void update(GameContainer container, StateBasedGame sbg, int delta) throws SlickException {

        //every update we have to handle the input from the player
        playerController.handleInput(container.getInput(), delta, level.getCharacters());
        
        //we want to pass on the gravity here, and only here in case some levels decide to do things differently (like disabling the gravity device for example)
        physics.handlePhysics(level, delta, level.getCurrentGravity());

        
           //BULLET: stuk moet apart voor character, doe ik wel later  vast maken aan character


        //BULLET: stuk moet apart voor character, doe ik wel later 

        deelta += delta;
        
        Iterator<Bullet> i = bullets.iterator();
        while (i.hasNext()) {
            Bullet b = i.next();

            if (b.isActive()) {
                b.update(delta);
            } else {
                i.remove();
            }
        }

       
       
        
     
        
        // bullet: gaat naar muis toe moet offset nog goed zetten
        if( container.getInput().isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) && deelta > DELAY )
		{
                        
			bullets.add( new Bullet( new Vector2f(player.getX() -2 - level.getXOffset() + 50, player.getY() - 2 - level.getYOffset() + 35), new Vector2f(350,350), new Vector2f(container.getInput().getMouseX(), container.getInput().getMouseY())));
                        deelta = 0;
                        checkBulletCollision();
                        System.out.println(container.getInput().getMouseX() + "  " + container.getInput().getMouseY());
                        System.out.println((player.getX() -2 - level.getXOffset() + 56) + "    " + (player.getY() - 2 - level.getYOffset() + 98));
		}
      

        // bullet moet nog op characters pos worden gezet en moet naar de muis toe worde geschoten , heb nu schietn op punt van muis 
        if (container.getInput().isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) && deelta > DELAY) {

            bullets.add(new Bullet(new Vector2f(player.getX() - 2 - level.getXOffset() + 50, player.getY() - 2 - level.getYOffset() + 35), new Vector2f(250, 10), new Vector2f(container.getInput().getMouseX(), container.getInput().getMouseY())));
            deelta = 0;
            checkBulletCollision ();
            System.out.println(container.getInput().getMouseX() + "  " + container.getInput().getMouseY());
            System.out.println((player.getX() - 2 - level.getXOffset() + 56) + "    " + (player.getY() - 2 - level.getYOffset() + 98));
        }

    }

 
 
    public void render(GameContainer container, StateBasedGame sbg, Graphics g) throws SlickException {
        g.scale(Game.SCALE, Game.SCALE);
        //render the level
        level.render();
        
        g.drawString("Score: " + player.getPoints(), 20, 20);
        g.drawString("Current gravity: " + player.getGravity(), 20, 40);
        //g.drawString("Health Enemie: " + p.getHealth(), 20, 60);
        g.fillRect(container.getWidth() / 2 - 200, -1, 200, 60, new Image("data/img/ui/timerui.png"), 0, 0);
        g.drawString(level.getGameTimeCount(), container.getWidth() / 2 - 125, 20);

    
       
         //BULLET: stuk moet apart voor character, doe ik wel later 
        for( Bullet b : bullets)
        {
            b.render(container, g);
        }

     

    }

    String message = "";

    //this method is overriden from basicgamestate and will trigger once you press any key on your keyboard
    public void keyPressed(int key, char code) {
        //if the key is escape, close our application

        if (key == Input.KEY_ESCAPE) {
            System.exit(0);
        }
        message = message + Character.toString(code);

        if (key == Input.KEY_DELETE) {
            message = "";
        }
        if (key == Input.KEY_BACK) {
            message = message.substring(0, message.length() - 2);
        }
        

    }

    public int getID() {
        //this is the id for changing states
        return 0;
    }
    
    // timer class voor resawn
      class RespawnTimer extends TimerTask {

        @Override
        public void run() {
              p.resetHealth();
            try {
                level.addCharacter(p = new Player((r.nextInt(200)+ 200), 800, 2));
            } catch (SlickException ex) {
                Logger.getLogger(LevelState.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
             
        }        
     }
      
      // dmg on click op dit moment, doodgaan remove char set timer
      // collision fixen voor bullet
      public void checkBulletCollision ()
	{
		// dynamisch doen 
                               p.dmgCalc();
				if(p.getHealth() <= 0)
                                { 
                                    level.removeCharacter(p);
                                    t.schedule(new RespawnTimer(),2000);
                                }
		
	}
   
}


