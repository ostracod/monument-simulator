
package monumentsimulator.tile;

import java.awt.Color;

import monumentsimulator.Pos;

public class EmptyTile extends Tile {
    
    private int aboveGroundColor;
    private int belowGroundColor;
    
    public EmptyTile() {
        super((byte)0);
        Color tempAboveGroundColor = new Color(32, 64, 96);
        Color tempBelowGroundColor = new Color(64, 32, 16);
        aboveGroundColor = tempAboveGroundColor.getRGB();
        belowGroundColor = tempBelowGroundColor.getRGB();
    }
    
    public int getColor(Pos pos) {
        if (pos.getY() < 0) {
            return aboveGroundColor;
        } else {
            return belowGroundColor;
        }
    }
}


