
package monumentsimulator.tile;

import java.awt.Color;

import monumentsimulator.Pos;

public class StoneTile extends Tile {
    
    private int color;
    
    public StoneTile() {
        super((byte)2);
        Color tempColor = new Color(112, 112, 112);
        color = tempColor.getRGB();
    }
    
    public int getColor(Pos pos) {
        return color;
    }
    
    public Tile getMiningDrop() {
        return Tile.BRICK;
    }
}


