
package monumentsimulator.tile;

import java.awt.Color;

import monumentsimulator.Pos;

public class BrickTile extends Tile {
    
    private int color;
    
    public BrickTile() {
        super((byte)3);
        Color tempColor = new Color(160, 160, 160);
        color = tempColor.getRGB();
    }
    
    public int getColor(Pos pos) {
        return color;
    }
    
    public int getMiningDelay() {
        return 30;
    }
    
    public Tile getMiningDrop() {
        return this;
    }
}


