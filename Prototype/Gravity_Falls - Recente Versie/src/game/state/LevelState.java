package game.state;

import game.Game;
import game.GameTime;
import game.UpdateGameLocations;
import game.bullet.Bullet;
import game.character.Player;
import game.client.PortalController;
import game.controller.MouseAndKeyBoardPlayerController;
import game.controller.PlayerController;
import game.enums.Facing;
import game.level.Level;
import game.physics.AABoundingRect;
import game.physics.Physics;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import java.util.Timer;
import java.util.logging.Logger;
import org.newdawn.slick.Color;

import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class LevelState extends BasicGameState {

    private Level level;
    private String startinglevel;
    private Player player;
    private PlayerController playerController;
    private Physics physics;
    TextField chatlog;
    TextField textmessage;
    TrueTypeFont font;
    Input i;
    boolean typing = false;
    private PortalController control;
    private Timer gameTimer;
    private GameTime gametimecount;
    private Timer updateTimer;
    private UpdateGameLocations updategameloc;
    private int EndGame = 0;

    //Database
    Connection conn = null;
    PreparedStatement stmt;
    Statement stmtMatch;
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";

    static final String DB_URL = "jdbc:mysql://84.246.4.143:9131/waligravityfalls";

    //  Database credentials
    static final String USER = "WaliGravityFalls";
    static final String PASS = "gravityfalls1";
    private int Matches;
    //BULLET: stuk moet apart voor character, doe ik wel later 
    public LinkedList<Bullet> bullets;
    protected int DELAY = 100;
    protected int deelta = 0;
    private int spectator = 0;

    public LevelState(String startingLevel) {
        this.startinglevel = startingLevel;
    }

    public void init(GameContainer container, StateBasedGame sbg) throws SlickException {

        //at the start of the game we don't have a player yet
        player = new Player(128, 415, 1, "Wouttotti");
        //player = new Player(250, 800, 2,"Walicorp");

        //once we initialize our level, we want to load the right level
        level = new Level(startinglevel, player);
        control = new PortalController(level, this);
        level.setControl(control);
        //and we create a controller, for now we use the MouseAndKeyBoardPlayerController
        playerController = new MouseAndKeyBoardPlayerController(player, level);

        physics = new Physics(player);

        font = new TrueTypeFont(new java.awt.Font(java.awt.Font.SERIF, java.awt.Font.BOLD, 13), false);
        chatlog = new TextField(container, font, 0, container.getHeight() - 300, 300, 128);
        chatlog.setText("AWSOME");

        gametimecount = new GameTime(1000, level.getCharacters(), container);
        updategameloc = new UpdateGameLocations(level.getCharacters(), player, level, control);

        gameTimer = new Timer();
        gameTimer.scheduleAtFixedRate(gametimecount, 0, 1000);
        updateTimer = new Timer();
        updateTimer.scheduleAtFixedRate(updategameloc, 0, 100);

        textmessage = new TextField(container, font, 0, container.getHeight() - 170, 300, 25);

        //BULLET: stuk moet apart voor character, doe ik wel later 
        bullets = new LinkedList<Bullet>();

    }

    public void update(GameContainer container, StateBasedGame sbg, int delta) throws SlickException {

        if (!gametimecount.isEndofgame()) {

            //int playerid, double health, double damage, int kills, double damagedone, Gravity gravity, float x, float y
            //every update we have to handle the input from the player
            playerController.handleInput(container.getInput(), delta, level.getCharacters(), spectator);

            //we want to pass on the gravity here, and only here in case some levels decide to do things differently (like disabling the gravity device for example)
            physics.handlePhysics(level, delta, level.getCurrentGravity());

            //BULLET: stuk moet apart voor character, doe ik wel later 
            deelta += delta;
            Iterator<Bullet> i = bullets.iterator();
            while (i.hasNext()) {
                Bullet b = i.next();

                if (b.isActive()) {
                    b.update(delta);
                    for (game.character.Character c : level.getCharacters()) {
                        if (b.getPlayerid() != c.getPlayerId()) {
                            AABoundingRect boundingShape = new AABoundingRect(b.getPosx(), b.getPosy(), 5, 5);
                            AABoundingRect boundingShape2 = new AABoundingRect(c.getX() - 2 - level.getXOffset() + 20, c.getY() - 2 - level.getYOffset() + 20, 26, 32);

                            if (boundingShape.checkCollision(boundingShape2)) {
                                if (player.getPlayerId() != c.getPlayerId()) {
                                    c.takedamage(player.getDamage());

                                    System.out.println("damage");
                                    if (player.getPlayerId() == b.getPlayerid()) {
                                        player.addDamagedone(player.getDamage());
                                        if (c.getHealth() <= 0) {
                                            player.AddPoints(200);
                                            player.addKills();
                                            int[] xspawns = new int[]{200, 1400, 200, 1800};
                                            int[] yspawns = new int[]{250, 250, 1250, 1250};
                                            Random rand = new Random();
                                            int n = rand.nextInt(3);
                                            if (c.getCrystal()) {
                                                c.setCrystal(false);
                                                control.clientsendcrystal(c.getX(), c.getY());
                                            }
                                            c.setX(xspawns[n]);
                                            c.setY(yspawns[n]);
                                            System.out.println(n);

                                            c.setHealth(100);
                                        }

                                    }
                                    control.clientsend(player.getGravity(), player.getPlayerId(), player.getHealth(), player.getDamage(), player.getKills(), player.getDamagedone(), player.getX(), player.getY(), player.getFacing(), player.getCrystal());
                                    control.clientsend(c.getGravity(), c.getPlayerId(), c.getHealth(), c.getDamage(), c.getKills(), c.getDamagedone(), c.getX(), c.getY(), c.getFacing(), c.getCrystal());
                                    i.remove();
                                }
                            }
                        }

                    }

                } else {
                    i.remove();
                }
            }

            // bullet moet nog op characters pos worden gezet en moet naar de muis toe worde geschoten , heb nu schietn op punt van muis 
            if (container.getInput()
                    .isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) && deelta > DELAY && spectator != 1) {

                Bullet bull = new Bullet(new Vector2f(player.getX() - 2 - level.getXOffset() + 50, player.getY() - 2 - level.getYOffset() + 35), new Vector2f(250, 10), new Vector2f(container.getInput().getMouseX(), container.getInput().getMouseY()), this.player.getPlayerId());
                bullets.add(bull);
                control.clientsendbullet(bull);

                deelta = 0;
            }
        }

    }

    public String getGameTimeCount() {
        return secondsToString(gametimecount.getCount());
    }

    private String secondsToString(int pTime) {
        return String.format("%02d:%02d", pTime / 60, pTime % 60);
    }

    public void render(GameContainer container, StateBasedGame sbg, Graphics g) throws SlickException {
        g.scale(Game.SCALE, Game.SCALE);
        //render the level
        level.render();

        g.drawString("Score: " + player.getPoints(), 20, 20);
        g.drawString("Current gravity: " + player.getGravity(), 20, 30);
        g.drawString("Damage: " + player.getDamage(), 20, 45);
        g.fillRect(container.getWidth() / 2 - 200, -1, 200, 60, new Image("data/img/ui/timerui.png"), 0, 0);
        g.drawString(getGameTimeCount(), container.getWidth() / 2 - 125, 20);

        for (game.character.Character c : level.getCharacters()) {
            g.drawString("hp: " + c.getHealth(), c.getX() - 2 - level.getXOffset(), c.getY() - 2 - level.getYOffset() - 20);
        }

        //BULLET: stuk moet apart voor character, doe ik wel later 
        Iterator<Bullet> i = bullets.iterator();
        while (i.hasNext()) {
            System.out.println(" ee");
            i.next().render(container, g);

        }
        if (gametimecount.isEndofgame()) {
            Color myColour = new Color(0, 0, 0, 180);
            EndGame++;
            g.setColor(myColour);
            g.fillRect(0, 0, container.getScreenWidth(), container.getScreenHeight());
            Color scorebcolor = new Color(255, 255, 255, 255);
            g.setColor(scorebcolor);
            g.fillRect(200, 100, 700, 400);
            Color scorecolor = new Color(0, 0, 0, 255);
            g.setColor(scorecolor);
            g.drawString("Player", 240, 120);
            g.drawString("Points", 410, 120);
            g.drawString("Damage Done", 550, 120);
            g.drawString("Kills", 750, 120);
            g.drawLine(200, 150, 900, 150);
            int locationy = 160;
            for (game.character.Character c : level.getCharacters()) {
                g.drawString(c.getUsername(), 230, locationy);
                g.drawString(Integer.toString(c.getPoints()), 420, locationy);
                g.drawString(Double.toString(c.getDamagedone()), 560, locationy);
                g.drawString(Integer.toString(c.getKills()), 760, locationy);

                locationy += 30;
            }
            if (EndGame == 1) {
                try {
                    //STEP 2: Register JDBC driver
                    Class.forName("com.mysql.jdbc.Driver");

                    //STEP 3: Open a connection
                    System.out.println("Connecting to database...");
                    conn = (Connection) DriverManager.getConnection(DB_URL, USER, PASS);

                    //STEP 4: Execute a query
                    System.out.println("Creating statement...");
                    String sqlMatch;
                    sqlMatch = "INSERT INTO `matches`(`Winner`, `Duration`) VALUES ('Wouttotti', 1000)";
                    stmtMatch = (Statement) conn.createStatement();
                    String sql;
                    String sqlUser;
                    String sqlMatches;
                    sqlMatches = "SELECT COUNT(*) AS id FROM playermatch";
                    ResultSet rs = stmtMatch.executeQuery(sqlMatches);
                    while (rs.next()) {
                        Matches = rs.getInt("id");
                    }
                    stmt = conn.prepareStatement(sqlMatch);
                    stmt.executeUpdate();
                    for (game.character.Character c : level.getCharacters()) {
                        String sqlPlayerMatch;

                        sqlPlayerMatch = "INSERT INTO `playermatch`(`MatchID`, `Score`, `Deaths`, `Kills`, `Username`) VALUES (" + Matches + ",10, 10, 10,'" + c.getUsername() + "')";
                        stmt = conn.prepareStatement(sqlPlayerMatch);
                        stmt.executeUpdate();
                    }
                    //STEP 6: Clean-up environment
                    conn.close();
                } catch (SQLException se) {
                    //Handle errors for JDBC
                    se.printStackTrace();
                } catch (Exception e) {
                    //Handle errors for Class.forName
                    e.printStackTrace();
                } finally {
            //finally block used to close resources

                    // nothing we can do
                    try {
                        if (conn != null) {
                            conn.close();
                        }
                    } catch (SQLException se) {
                        se.printStackTrace();
                    }//end finally try
                }//end try

            }
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
        textmessage.setText(message);

    }

    public String backSpace(String str) {
        if (str.length() > 0 && str.charAt(str.length() - 1) == 'x') {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    public int getID() {
        //this is the id for changing states
        return 0;
    }

    public void AddBullets(Bullet bull) {

        for (game.character.Character e : level.getCharacters()) {
            if (spectator == 0) {

                if (this.player.getPlayerId() != bull.getPlayerid()) {
                    if (e.getPlayerId() == bull.getPlayerid()) {
                        Bullet bullet = new Bullet(new Vector2f(e.getX() - 2 - level.getXOffset() + 50, e.getY() - 2 - level.getYOffset() + 35), bull.getSpeed(), bull.getMousel(), bull.getPlayerid());
                        this.bullets.add(bullet);
                        System.out.println("  " + bullets.size());
                    }
                }
            } else {
                if (e.getPlayerId() == bull.getPlayerid()) {
                    Bullet bullet = new Bullet(new Vector2f(e.getX() - 2 - level.getXOffset() + 50, e.getY() - 2 - level.getYOffset() + 35), bull.getSpeed(), bull.getMousel(), bull.getPlayerid());
                    this.bullets.add(bullet);
                    System.out.println("  " + bullets.size());
                }
            }

            if (this.player.getPlayerId() != bull.getPlayerid()) {
                if (e.getPlayerId() == bull.getPlayerid()) {
                    Bullet bullet = new Bullet(new Vector2f(e.getX() - 2 - level.getXOffset() + 50, e.getY() - 2 - level.getYOffset() + 35), bull.getSpeed(), bull.getMousel(), bull.getPlayerid());
                    this.bullets.add(bullet);
                }
            }
        }
    }

}
