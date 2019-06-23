
package monumentsimulator;

import java.util.Comparator;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
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
import monumentsimulator.tile.StoneTile;

public class World {
    
    private String worldPath = "./monumentWorld";
    private String chunksPath = Paths.get(worldPath, "chunks").toString();
    private String statePath = Paths.get(worldPath, "state.dat").toString();
    private File worldDirectory;
    private File chunksDirectory;
    private File stateFile;
    private Map<Pos, Chunk> chunkMap = new Hashtable<Pos, Chunk>(100);
    private Pos lookUpPos = new Pos(0, 0);
    private int persistDelay = 0;
    private Player player;
    private Monument monument = null;
    private List<Pos> fallingTilePosList = new ArrayList<Pos>();
    
    private static Random random = new Random();
    
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
        // We use lookUpPos to avoid creating a Pos instance
        // every time we want to find a chunk.
        lookUpPos.set(pos);
        Chunk.convertPosToChunkPos(lookUpPos);
        Chunk output = chunkMap.get(lookUpPos);
        if (output == null) {
            if (maturity < 0) {
                return null;
            }
            output = new Chunk(lookUpPos.copy(), this);
            chunkMap.put(lookUpPos, output);
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
            fallingTilePosList.add(pos.copy());
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
    
    public void processFallingTilePos(Pos pos) {
        // TODO: Implement.
        
        System.out.println(pos);
    }
    
    public void timerEvent() {
        player.timerEvent();
        persistDelay += 1;
        if (persistDelay > 1800) {
            persist();
            persistDelay = 0;
        }
        if (fallingTilePosList.size() > 0) {
            List<Pos> tempPosList = fallingTilePosList;
            fallingTilePosList = new ArrayList<Pos>();
            Collections.sort(tempPosList, new Comparator<Pos>() {
                public int compare(Pos pos1, Pos pos2) {
                    int posY1 = pos1.getY();
                    int posY2 = pos2.getY();
                    if (posY1 != posY2) {
                        return posY2 - posY1;
                    }
                    return pos2.getX() - pos1.getX();
                }
            });
            int index = 0;
            while (index < tempPosList.size()) {
                Pos tempPos = tempPosList.get(index);
                processFallingTilePos(tempPos);
                index += 1;
            }
        }
    }
    
    public void addStoneCluster(Pos inputPos) {
        int tempSize = 6 + random.nextInt(15);
        // Make sure that the cluster does not collide with other clusters.
        Pos tempOffset = new Pos(0, 0);
        Pos tempPos = new Pos(0, 0);
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


