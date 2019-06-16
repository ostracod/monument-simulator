
package monumentsimulator;

import monumentsimulator.tile.Tile;
import monumentsimulator.tile.EmptyTile;

public class Player {
    
    private Pos pos;
    private World world;
    private Pos fallOffset = new Pos(0, 1);
    
    private static Pos[] shouldFallOffsetSet = {
        new Pos(-1, 0),
        new Pos(1, 0),
        new Pos(-1, 1),
        new Pos(0, 1),
        new Pos(1, 1),
    };
    
    public Player(Pos inputPos, World inputWorld) {
        pos = inputPos;
        world = inputWorld;
        world.setTile(pos, Tile.PLAYER, true);
    }
    
    public Pos getPos() {
        return pos;
    }
    
    public void walk(Pos offset) {
        if (shouldFall()) {
            return;
        }
        move(offset);
    }
    
    public void move(Pos offset) {
        Pos tempNextPos = pos.copy();
        tempNextPos.add(offset);
        Tile tempTile = world.getTile(tempNextPos, true);
        if (!(tempTile instanceof EmptyTile)) {
            return;
        }
        world.setTile(pos, Tile.EMPTY, true);
        pos.set(tempNextPos);
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
    
    public boolean shouldFall() {
        Pos tempPos = new Pos(0, 0);
        int index = 0;
        while (index < shouldFallOffsetSet.length) {
            Pos tempOffset = shouldFallOffsetSet[index];
            tempPos.set(pos);
            tempPos.add(tempOffset);
            Tile tempTile = world.getTile(tempPos, true);
            if (!(tempTile instanceof EmptyTile)) {
                return false;
            }
            index += 1;
        }
        return true;
    }
    
    public void timerEvent() {
        if (shouldFall()) {
            move(fallOffset);
        }
    }
}


