
package monumentsimulator.tile;

import java.awt.Color;

import monumentsimulator.Pos;

public class PlayerTile extends Tile {
    
    private int color;
    
    public PlayerTile() {
        super((byte)4);
        Color tempColor = new Color(32, 192, 32);
        color = tempColor.getRGB();
    }
    
    public int getColor(Pos pos) {
        return color;
    }
}


