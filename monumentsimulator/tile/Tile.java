
package monumentsimulator.tile;

import java.util.List;
import java.util.ArrayList;

public abstract class Tile {
    
    public static Tile EMPTY;
    public static Tile DIRT;
    
    private static List<Tile> tileList;
    
    private int number;
    
    static {
        tileList = new ArrayList<Tile>();
        EMPTY = new EmptyTile();
        DIRT = new DirtTile();
    }
    
    public Tile(int inputNumber) {
        number = inputNumber;
        while (tileList.size() <= number) {
            tileList.add(null);
        }
        tileList.set(number, this);
    }
    
}


