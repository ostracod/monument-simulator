
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
    
    @Override
    public int hashCode() {
        return getNumber();
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (!(object instanceof Tile)) {
            return false;
        }
        Tile tempTile = (Tile)object;
        return (number == tempTile.getNumber());
    }
    
    public abstract int getColor(Pos pos);
    
    public Tile getMiningDrop() {
        return null;
    }
    
    public static Tile getTileFromNumber(int inputNumber) {
        return tileList.get(inputNumber);
    }
}


