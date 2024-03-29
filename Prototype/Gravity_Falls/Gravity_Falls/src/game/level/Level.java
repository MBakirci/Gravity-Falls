package game.level;

import java.util.ArrayList;

import game.*;
import game.character.Character;
import game.character.Player;
import game.client.PortalController;
import game.enums.Gravity;
import game.enums.Guns;
import game.level.object.Crystal;
import game.level.object.Objective;
import game.level.tile.AirTile;
import game.level.tile.SolidTile;
import game.level.tile.Tile;
import game.socketmsg.Message;
import game.socketmsg.MsgLocations;
import java.util.List;
import java.util.ListIterator;
import java.util.Timer;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

public class Level {

    private TiledMap map;

    //a list of all characters present somewhere on this map
    private ArrayList<Character> characters;
    private Player player;

    //a list of the objects in this map (excluding characters)
    private ArrayList<LevelObject> levelObjects;

    private Tile[][] tiles;

    private Image background;

    private Gravity gravity;
    private PortalController control;

    public Level(String level, Player player) throws SlickException {
        map = new TiledMap("data/levels/" + level + ".tmx", "data/img");
        characters = new ArrayList<Character>();

        levelObjects = new ArrayList<LevelObject>();
        gravity = Gravity.DOWN;

        this.player = player;
        addCharacter(player);

        //Player p = new Player(250, 800, 2);
        Player p = new Player(128, 415, 1);
        addCharacter(p);

        loadTileMap();
        loadObjects();
        loadCrystal();

        background = new Image("data/img/backgrounds/" + map.getMapProperty("background", "background.png"));

    }

    public void setControl(PortalController control) {
        this.control = control;
    }

    public void updatelocation(MsgLocations mloc) {
        if (mloc != null) {
            for (Character c : getCharacters()) {

                if (mloc.playerid == c.getPlayerId()) {
                    c.setX(mloc.x);
                    c.setY(mloc.y);
                    c.setGravity(mloc.gravity);
                    c.setHealth(mloc.health);
                    c.setFacing(mloc.facing);
                    c.setCrystal(mloc.crystal);
                }
            }
        } else {
            System.out.println("shits zero");
        }
    }
    ListIterator<LevelObject> it;

    public void updateobjects(MsgLocations mloc) {
        it = this.getLevelObjects().listIterator();
        while (it.hasNext()) {
            if (it.next().getUniqid().equals(mloc.objectid)) {
                it.remove();
            }

        }
    }

    private void loadTileMap() {
        //create an array to hold all the tiles in the map
        tiles = new Tile[map.getWidth()][map.getHeight()];

        int layerIndex = map.getLayerIndex("CollisionLayer");

        if (layerIndex == -1) {
            //TODO we can clean this up later with an exception if we want, but because we make the maps ourselfs this will suffice for now
            System.err.println("Map does not have the layer \"CollisionLayer\"");
            System.exit(0);
        }

        //loop through the whole map
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {

                //get the tile
                int tileID = map.getTileId(x, y, layerIndex);

                Tile tile = null;

                //and check what kind of tile it is (
                switch (map.getTileProperty(tileID, "tileType", "solid")) {
                    case "air":
                        tile = new AirTile(x, y);
                        break;
                    default:
                        tile = new SolidTile(x, y);
                        break;
                }
                tiles[x][y] = tile;
            }
        }
    }

    private void loadObjects() throws SlickException {

        //we only have one layer defined, slick2d does not support getting an object layer by name but that does not matter to much for our game
        int objectAmount = map.getObjectCount(0);

        //the objects are loaded into an array, again retrieval by name is not supported
        for (int i = 0; i < objectAmount; i++) {

            switch (map.getObjectType(0, i)) {
                case "Objective":
                    if (i == 1) {
                        addLevelObject(new Objective(map.getObjectX(0, i), map.getObjectY(0, i), Guns.P90));
                    } else {
                        addLevelObject(new Objective(map.getObjectX(0, i), map.getObjectY(0, i), Guns.Handgun));
                    }
                    break;
            }
        }

    }

    private void loadCrystal() throws SlickException {

        //we only have one layer defined, slick2d does not support getting an object layer by name but that does not matter to much for our game
        int objectAmount = map.getObjectCount(1);

        //the objects are loaded into an array, again retrieval by name is not supported
        for (int i = 0; i < objectAmount; i++) {
            switch (map.getObjectType(1, i)) {
                case "Crystal":
                    if (i == 1) {
                        addLevelCrystal(new Crystal(map.getObjectX(1, i), map.getObjectY(1, i)));
                    } else {
                        addLevelCrystal(new Crystal(map.getObjectX(1, i), map.getObjectY(1, i)));
                    }
                    break;
            }
        }
    }

    public void respawnCrystal(float x, float y) {
        //we only have one layer defined, slick2d does not support getting an object layer by name but that does not matter to much for our game

        addLevelCrystal(new Crystal(x, y));

    }

    public void addCharacter(Character c) {
        characters.add(c);
    }

    public ArrayList<Character> getCharacters() {
        return characters;
    }

    public void addLevelObject(LevelObject obj) {
        levelObjects.add(obj);
    }

    public void addLevelCrystal(LevelObject obj) {
        levelObjects.add(obj);
    }

    public ArrayList<LevelObject> getLevelObjects() {
        return levelObjects;
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    public void removeObject(LevelObject obj) {
        levelObjects.remove(obj);
    }

    public void removeObjects(ArrayList<LevelObject> objects) {
        if (objects.size() > 0) {
            for (LevelObject e : objects) {
                System.out.println("ee");
                control.clientsendqueue(e.getUniqid());
            }
        }
    }

    public int getXOffset() {
        int offset_x = 0;

        //the first thing we are going to need is the half-width of the screen, to calculate if the player is in the middle of our screen
        int half_width = (int) (Game.WINDOW_WIDTH / Game.SCALE / 2);

        //next up is the maximum offset, this is the most right side of the map, minus half of the screen offcourse
        int maxX = (int) (map.getWidth() * 32) - half_width;

        //now we have 3 cases here
        if (player.getX() < half_width) {
            //the player is between the most left side of the map, which is zero and half a screen size which is 0+half_screen
            offset_x = 0;
        } else if (player.getX() > maxX) {
            //the player is between the maximum point of scrolling and the maximum width of the map
            //the reason why we substract half the screen again is because we need to set our offset to the topleft position of our screen
            offset_x = maxX - half_width;
        } else {
            //the player is in between the 2 spots, so we set the offset to the player, minus the half-width of the screen
            offset_x = (int) (player.getX() - half_width);
        }

        return offset_x;
    }

    //similar to the getXOffset() method
    public int getYOffset() {
        int offset_y = 0;

        int half_heigth = (int) (Game.WINDOW_HEIGTH / Game.SCALE / 2);

        int maxY = (int) (map.getHeight() * 32) - half_heigth;

        if (player.getY() < half_heigth) {
            offset_y = 0;
        } else if (player.getY() > maxY) {
            offset_y = maxY - half_heigth;
        } else {
            offset_y = (int) (player.getY() - half_heigth);
        }

        return offset_y;
    }

    public void render() {

        int offset_x = getXOffset();
        int offset_y = getYOffset();

        //render the background
        renderBackground();

        //then the map on top of that
        map.render(-(offset_x % 32), -(offset_y % 32), offset_x / 32, offset_y / 32, 33, 19);

        for (LevelObject obj : levelObjects) {
            obj.render(offset_x, offset_y, getCurrentGravity());
        }

        //and then render the characters on top of the map
        for (Character c : characters) {
            c.render(offset_x, offset_y, c.getGravity());

        }

    }

    public Gravity getCurrentGravity() {
        return gravity;
    }

    public void setGravity(Gravity g) {
        gravity = g;
    }

    private void renderBackground() {

        //first calculate the maximum amount we can "scroll" the background image before we have the rightmore or bottom most pixel on the screen
        float backgroundXScrollValue = (background.getWidth() - Game.WINDOW_WIDTH / Game.SCALE);
        float backgroundYScrollValue = (background.getHeight() - Game.WINDOW_HEIGTH / Game.SCALE);

        //we do the same for the map
        float mapXScrollValue = ((float) map.getWidth() * 32 - Game.WINDOW_WIDTH / Game.SCALE);
        float mapYScrollValue = ((float) map.getHeight() * 32 - Game.WINDOW_HEIGTH / Game.SCALE);

        //and now calculate the factor we have to multiply the offset with, making sure we multiply the offset by -1 to get it to negative
        float scrollXFactor = backgroundXScrollValue / mapXScrollValue * -1;
        float scrollYFactor = backgroundYScrollValue / mapYScrollValue * -1;

        //and now draw it using the factor and the offset to see where we start drawing
        background.draw(this.getXOffset() * scrollXFactor, this.getYOffset() * scrollYFactor);
    }

}
