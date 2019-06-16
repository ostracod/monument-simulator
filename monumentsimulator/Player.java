
package monumentsimulator;

import monumentsimulator.tile.Tile;
import monumentsimulator.tile.EmptyTile;

public class Player {
    
    private Pos pos;
    private World world;
    
    public Player(Pos inputPos, World inputWorld) {
        pos = inputPos;
        world = inputWorld;
        world.setTile(pos, Tile.PLAYER, true);
    }
    
    public Pos getPos() {
        return pos;
    }
    
    public void move(Pos offset) {
        world.setTile(pos, Tile.EMPTY, true);
        pos.add(offset);
        world.setTile(pos, Tile.PLAYER, false);
    }
    
    public void buildOrMine(Pos offset) {
        Pos tempPos = pos.copy();
        tempPos.add(offset);
        Tile tempPreviousTile = world.getTile(tempPos, true);
        Tile tempNextTile;
        if (tempPreviousTile instanceof EmptyTile) {
            tempNextTile = Tile.BRICK;
        } else {
            tempNextTile = Tile.EMPTY;
        }
        world.setTile(tempPos, tempNextTile, true);
    }
}


