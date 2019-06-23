
package monumentsimulator;

import java.util.Comparator;
import java.util.Collections;
import java.util.Queue;
import java.util.PriorityQueue;
import java.util.Map;
import java.util.Hashtable;
import java.util.Random;

import java.io.File;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;

import monumentsimulator.tile.Tile;
import monumentsimulator.tile.EmptyTile;
import monumentsimulator.tile.StoneTile;

public class World {
    
    private String worldPath = "./monumentWorld";
    private String chunksPath = Paths.get(worldPath, "chunks").toString();
    private String statePath = Paths.get(worldPath, "state.dat").toString();
    private File worldDirectory;
    private File chunksDirectory;
    private File stateFile;
    private Map<Pos, Chunk> chunkMap = new Hashtable<Pos, Chunk>(100);
    private Pos scratchPos1 = new Pos(0, 0);
    private Pos scratchPos2 = new Pos(0, 0);
    private int persistDelay = 0;
    private int fallDelay = 0;
    private Player player;
    private Monument monument = null;
    private Queue<Pos> fallingTilePosQueue = new PriorityQueue<Pos>();
    
    private static Random random = new Random();
    private static Pos[] fallUpdateOffsetSet = {
        new Pos(-1, -1),
        new Pos(0, -1),
        new Pos(1, -1),
        new Pos(-1, 0),
        new Pos(1, 0)
    };
    
    public World() {
        worldDirectory = new File(worldPath);
        chunksDirectory = new File(chunksPath);
        stateFile = new File(statePath);
        if (!worldDirectory.exists()) {
            worldDirectory.mkdir();
        }
        if (!chunksDirectory.exists()) {
            chunksDirectory.mkdir();
        }
        if (stateFile.exists()) {
            try {
                DataInputStream tempStream = new DataInputStream(new FileInputStream(statePath));
                Pos tempPlayerPos = Pos.readFromStream(tempStream);
                player = new Player(tempPlayerPos, this);
                int tempBrickCount = tempStream.readInt();
                int tempDirtCount = tempStream.readInt();
                player.setInventoryCount(Tile.BRICK, tempBrickCount);
                player.setInventoryCount(Tile.DIRT, tempDirtCount);
                Pos tempMonumentPos = Pos.readFromStream(tempStream);
                int tempWidth = tempStream.readInt();
                int tempHeight = tempStream.readInt();
                monument = new Monument(tempMonumentPos, tempWidth, tempHeight, this);
                tempStream.close();
            } catch(IOException exception) {
                System.out.println(exception.getMessage());
            }
        } else {
            monument = new Monument(new Pos(0, 0), 0, 0, this);
            player = new Player(new Pos(0, -1), this);
        }
        
    }
    
    public Chunk getChunk(Pos pos, int maturity) {
        // We use scratchPos1 to avoid creating a Pos instance
        // every time we want to find a chunk.
        scratchPos1.set(pos);
        Chunk.convertPosToChunkPos(scratchPos1);
        Chunk output = chunkMap.get(scratchPos1);
        if (output == null) {
            if (maturity < 0) {
                return null;
            }
            Pos tempPos = scratchPos1.copy();
            output = new Chunk(tempPos, this);
            chunkMap.put(tempPos, output);
            output.addFallingTiles();
        }
        output.advanceMaturity(maturity);
        return output;
    }
    
    public Tile getTileWithMaturity(Pos pos, int maturity) {
        Chunk tempChunk = getChunk(pos, maturity);
        if (tempChunk == null) {
            return null;
        }
        return tempChunk.getTile(pos);
    }
    
    public Tile getTile(Pos pos) {
        return getTileWithMaturity(pos, Chunk.maximumMaturity);
    }
    
    public void setTileWithMaturity(Pos pos, Tile tile, int maturity) {
        Chunk tempChunk = getChunk(pos, maturity);
        if (tempChunk == null) {
            return;
        }
        tempChunk.setTile(pos, tile);
        if (monument != null) {
            monument.setTileEvent(pos, tile);
        }
        if (tile.canFall(pos)) {
            addFallingTilePos(pos);
        }
        if (tile instanceof EmptyTile) {
            Pos tempPos = new Pos(0, 0);
            int index = 0;
            while (index < fallUpdateOffsetSet.length) {
                Pos tempOffset = fallUpdateOffsetSet[index];
                tempPos.set(pos);
                tempPos.add(tempOffset);
                Tile tempTile = getTileWithMaturity(tempPos, -1);
                if (tempTile != null && tempTile.canFall(tempPos)) {
                    addFallingTilePos(tempPos);
                }
                index += 1;
            }
        }
    }
    
    public void setTile(Pos pos, Tile tile) {
        setTileWithMaturity(pos, tile, Chunk.maximumMaturity);
    }
    
    public String getChunksPath() {
        return chunksPath;
    }
    
    public File getChunksDirectory() {
        return chunksDirectory;
    }
    
    public Player getPlayer() {
        return player;
    }
    
    public Monument getMonument() {
        return monument;
    }
    
    public void persist() {
        System.out.println("Persisting world...");
        try {
            DataOutputStream tempStream = new DataOutputStream(new FileOutputStream(statePath));
            player.getPos().writeToStream(tempStream);
            int tempBrickCount = player.getInventoryCount(Tile.BRICK);
            int tempDirtCount = player.getInventoryCount(Tile.DIRT);
            tempStream.writeInt(tempBrickCount);
            tempStream.writeInt(tempDirtCount);
            monument.getPos().writeToStream(tempStream);
            int tempWidth = monument.getWidth();
            int tempHeight = monument.getHeight();
            tempStream.writeInt(tempWidth);
            tempStream.writeInt(tempHeight);
            tempStream.close();
        } catch(IOException exception) {
            System.out.println(exception.getMessage());
        }
        for (Chunk chunk : chunkMap.values()) {
            chunk.persist();
        }
        System.out.println("Finished persisting world.");
    }
    
    public void addFallingTilePos(Pos pos) {
        if (fallingTilePosQueue.contains(pos)) {
            return;
        }
        fallingTilePosQueue.add(pos.copy());
    }
    
    private boolean tryDiagonalFall(Pos pos, int offsetX) {
        pos.setX(pos.getX() + offsetX);
        Tile tempTile = getTileWithMaturity(pos, -1);
        if (!(tempTile instanceof EmptyTile)) {
            return false;
        }
        pos.setY(pos.getY() + 1);
        tempTile = getTileWithMaturity(pos, -1);
        return (tempTile instanceof EmptyTile);
    }
    
    public boolean getFallPos(Pos destination, Pos pos) {
        destination.setX(pos.getX());
        destination.setY(pos.getY() + 1);
        Tile tempTile = getTileWithMaturity(destination, -1);
        if (tempTile instanceof EmptyTile) {
            return true;
        }
        int tempOffsetX;
        if (random.nextInt(2) == 0) {
            tempOffsetX = 1;
        } else {
            tempOffsetX = -1;
        }
        boolean tempResult;
        destination.set(pos);
        tempResult = tryDiagonalFall(destination, tempOffsetX);
        if (tempResult) {
            return true;
        }
        destination.set(pos);
        tempResult = tryDiagonalFall(destination, -tempOffsetX);
        return tempResult;
    }
    
    public void processFallingTilePos(Pos pos) {
        Tile tempTile = getTile(pos);
        if (!tempTile.canFall(pos)) {
            return;
        }
        boolean tempResult = getFallPos(scratchPos2, pos);
        if (!tempResult) {
            return;
        }
        setTile(pos, Tile.EMPTY);
        setTile(scratchPos2, tempTile);
    }
    
    public void processFallingTiles() {
        Queue<Pos> tempPosQueue = fallingTilePosQueue;
        fallingTilePosQueue = new PriorityQueue<Pos>();
        for (Pos pos : tempPosQueue) {
            processFallingTilePos(pos);
        }
    }
    
    public void timerEvent() {
        player.timerEvent();
        persistDelay += 1;
        if (persistDelay > 1800) {
            persist();
            persistDelay = 0;
        }
        fallDelay += 1;
        if (fallDelay > 1) {
            if (fallingTilePosQueue.size() > 0) {
                processFallingTiles();
            }
            fallDelay = 0;
        }
    }
    
    public void addStoneCluster(Pos inputPos) {
        int tempSize = 6 + random.nextInt(15);
        Pos tempPos = new Pos(0, 0);
        // Make sure that the cluster does not collide with other clusters.
        Pos tempOffset = new Pos(0, 0);
        while (tempOffset.getY() < tempSize) {
            tempPos.set(inputPos);
            tempPos.add(tempOffset);
            Tile tempTile = getTileWithMaturity(tempPos, 0);
            if (tempTile instanceof StoneTile) {
                return;
            }
            tempOffset.advance(1, 0, tempSize);
        }
        // If there were no collisions, place the cluster.
        tempOffset = new Pos(0, 0);
        while (tempOffset.getY() < tempSize) {
            tempPos.set(inputPos);
            tempPos.add(tempOffset);
            setTileWithMaturity(tempPos, Tile.STONE, 0);
            tempOffset.advance(1, 0, tempSize);
        }
    }
}


