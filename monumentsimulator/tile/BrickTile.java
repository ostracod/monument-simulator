
package monumentsimulator.tile;

import java.awt.Color;

import monumentsimulator.Pos;

public class BrickTile extends Tile {
    
    private int color;
    
    public BrickTile() {
        super((byte)3);
        Color tempColor = new Color(144, 144, 144);
        color = tempColor.getRGB();
    }
    
    public int getColor(Pos pos) {
        return color;
    }
}


