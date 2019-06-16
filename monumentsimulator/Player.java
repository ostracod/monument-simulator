
package monumentsimulator;

import monumentsimulator.tile.Tile;

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
}


