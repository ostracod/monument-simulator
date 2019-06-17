
package monumentsimulator;

import java.util.Map;
import java.util.Hashtable;

import monumentsimulator.tile.Tile;
import monumentsimulator.tile.EmptyTile;

public class Player {
    
    private Pos pos;
    private World world;
    private Pos fallOffset = new Pos(0, 1);
    private Pos climbOffset = new Pos(0, -1);
    private Map<Tile, Integer> inventory = new Hashtable<Tile, Integer>(10);
    private Tile selectedInventoryTile = Tile.BRICK;
    private boolean isMining = false;
    private Tile miningTile;
    private Pos miningTilePos;
    private Pos miningPlayerPos;
    private int miningDelay;
    private int maximumMiningDelay;
    
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
    
    // Assumes that offset is exclusively horizontal,
    // and there is a solid tile in the way.
    public void climbStep(Pos offset) {
        Pos tempPos = pos.copy();
        tempPos.add(climbOffset);
        tempPos.add(offset);
        Tile tempTile = world.getTile(tempPos, true);
        if (!(tempTile instanceof EmptyTile)) {
            return;
        }
        boolean tempResult = move(climbOffset);
        if (!tempResult) {
            return;
        }
        move(offset);
    }
    
    public void walk(Pos offset) {
        if (shouldFall()) {
            return;
        }
        boolean tempResult = move(offset);
        if (tempResult) {
            return;
        }
        if (offset.getY() == 0 && offset.getX() != 0) {
            climbStep(offset);
        }
    }
    
    public boolean move(Pos offset) {
        Pos tempNextPos = pos.copy();
        tempNextPos.add(offset);
        Tile tempTile = world.getTile(tempNextPos, true);
        if (!(tempTile instanceof EmptyTile)) {
            return false;
        }
        world.setTile(pos, Tile.EMPTY, true);
        pos.set(tempNextPos);
        world.setTile(pos, Tile.PLAYER, false);
        return true;
    }
    
    public void build(Pos inputPos) {
        int tempCount = getInventoryCount(selectedInventoryTile);
        if (tempCount <= 0) {
            return;
        }
        setInventoryCount(selectedInventoryTile, tempCount - 1);
        world.setTile(inputPos, selectedInventoryTile, true);
    }
    
    public void startMining(Pos inputPos, Tile tile) {
        int tempDelay = tile.getMiningDelay();
        if (tempDelay < 0) {
            return;
        }
        isMining = true;
        miningTile = tile;
        miningTilePos = inputPos;
        miningPlayerPos = pos.copy();
        miningDelay = 0;
        maximumMiningDelay = tempDelay;
    }
    
    public void finishMining() {
        world.setTile(miningTilePos, Tile.EMPTY, true);
        Tile tempDropTile = miningTile.getMiningDrop();
        if (tempDropTile != null) {
            int tempCount = getInventoryCount(tempDropTile);
            setInventoryCount(tempDropTile, tempCount + 1);
        }
        isMining = false;
    }
    
    public void buildOrStartMining(Pos offset) {
        Pos tempPos = pos.copy();
        tempPos.add(offset);
        Tile tempTile = world.getTile(tempPos, true);
        if (tempTile instanceof EmptyTile) {
            build(tempPos);
        } else {
            startMining(tempPos, tempTile);
        }
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
        if (isMining) {
            Tile tempTile = world.getTile(miningTilePos, true);
            if (miningTile.getNumber() != tempTile.getNumber()) {
                isMining = false;
            } else if (!miningPlayerPos.equals(pos)) {
                isMining = false;
            } else {
                miningDelay += 1;
                if (miningDelay >= maximumMiningDelay) {
                    finishMining();
                }
            }
        }
    }
    
    public int getInventoryCount(Tile tile) {
        Integer tempCount = inventory.get(tile);
        if (tempCount == null) {
            return 0;
        }
        return tempCount;
    }
    
    public void setInventoryCount(Tile tile, int count) {
        inventory.put(tile, count);
    }
    
    public void selectInventoryTile(Tile tile) {
        selectedInventoryTile = tile;
    }
    
    public Tile getSelectedInventoryTile() {
        return selectedInventoryTile;
    }
    
    public boolean getIsMining() {
        return isMining;
    }
    
    public double getMiningProgress() {
        return miningDelay * 1.0 / maximumMiningDelay;
    }
}


