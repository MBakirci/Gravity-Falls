package game.physics;

import game.enums.Gravity;
import game.level.tile.Tile;
import java.io.Serializable;

import java.util.ArrayList;

public class AABoundingRect extends BoundingShape implements Serializable {

    public float x;
    public float y;
    public float width;
    public float height;

    public AABoundingRect(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void updatePosition(float newX, float newY) {
        this.x = newX;
        this.y = newY;
    }

    public void movePosition(float x, float y) {
        this.x += x;
        this.y += y;
    }

    public boolean checkCollision(AABoundingRect rect) {
        return !(rect.x > this.x + width || rect.x + rect.width < this.x || rect.y > this.y + height || rect.y + rect.height < this.y);
    }

    public ArrayList<Tile> getTilesOccupying(Tile[][] tiles) {
        ArrayList<Tile> occupiedTiles = new ArrayList<Tile>();

        //we go from the left of the rect towards to right of the rect, making sure we round upwards to a multiple of 32 or we might miss a few tiles
        for (int i = (int) x; i <= x + width + (32 - width % 32); i += 32) {
            for (int j = (int) y; j <= y + height + (32 - height % 32); j += 32) {
                occupiedTiles.add(tiles[i / 32][j / 32]);
            }
        }
        return occupiedTiles;
    }

    public ArrayList<Tile> getGroundTiles(Tile[][] tiles, Gravity gravity) {

        ArrayList<Tile> tilesUnderneath = new ArrayList<Tile>();

        int i = 0;
        int j = 0;

        //because of different gravity cases, we can have different "ground" tiles
        switch (gravity) {
            case DOWN:
                j = (int) (y + height + 1);
                for (i = (int) x; i <= x + width + (32 - width % 32); i += 32) {
                    tilesUnderneath.add(tiles[i / 32][j / 32]);
                }
                break;
            case UP:
                j = (int) (y - 1);
                for (i = (int) x; i <= x + width + (32 - width % 32); i += 32) {
                    tilesUnderneath.add(tiles[i / 32][j / 32]);
                }
                break;
        }

        return tilesUnderneath;
    }

}
