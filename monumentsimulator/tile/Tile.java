
package monumentsimulator.tile;

import java.util.List;
import java.util.ArrayList;

import java.awt.Color;

import monumentsimulator.Pos;

public abstract class Tile {
    
    public static Tile EMPTY;
    public static Tile DIRT;
    public static Tile STONE;
    public static Tile BRICK;
    public static Tile PLAYER;
    
    private static List<Tile> tileList;
    
    private byte number;
    
    static {
        tileList = new ArrayList<Tile>();
        EMPTY = new EmptyTile();
        DIRT = new DirtTile();
        STONE = new StoneTile();
        BRICK = new BrickTile();
        PLAYER = new PlayerTile();
    }
    
    public Tile(byte inputNumber) {
        number = inputNumber;
        while (tileList.size() <= number) {
            tileList.add(null);
        }
        tileList.set(number, this);
    }
    
    public byte getNumber() {
        return number;
    }
    
    public abstract int getColor(Pos pos);
    
    public static Tile getTileFromNumber(int inputNumber) {
        return tileList.get(inputNumber);
    }
}


