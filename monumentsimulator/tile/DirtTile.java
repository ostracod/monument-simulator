
package monumentsimulator.tile;

import java.awt.Color;

import monumentsimulator.Pos;

public class DirtTile extends Tile {
    
    private int color;
    
    public DirtTile() {
        super((byte)1);
        Color tempColor = new Color(128, 64, 0);
        color = tempColor.getRGB();
    }
    
    public int getColor(Pos pos) {
        return color;
    }
}


