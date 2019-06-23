
package monumentsimulator.tile;

import java.awt.Color;

import monumentsimulator.Pos;

public class DirtTile extends Tile {
    
    private int color;
    
    public DirtTile() {
        super((byte)1);
        Color tempColor = new Color(128, 64, 32);
        color = tempColor.getRGB();
    }
    
    public int getColor(Pos pos) {
        return color;
    }
    
    public int getMiningDelay() {
        return 60;
    }
    
    public Tile getMiningDrop() {
        return this;
    }
    
    public boolean canFall(Pos pos) {
        return (pos.getY() < 0);
    }
}


