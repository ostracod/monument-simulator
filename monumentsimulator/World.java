
package monumentsimulator;

import java.util.Map;
import java.util.Hashtable;
import java.util.Random;

import java.io.File;
import java.nio.file.Paths;

import monumentsimulator.tile.Tile;
import monumentsimulator.tile.StoneTile;

public class World {
    
    private String worldPath = "./monumentWorld";
    private String chunksPath = Paths.get(worldPath, "chunks").toString();
    private File worldDirectory;
    private File chunksDirectory;
    private Map<Pos, Chunk> chunkMap = new Hashtable<Pos, Chunk>(100);
    private Pos lookUpPos = new Pos(0, 0);
    private int persistDelay = 0;
    private Player player;
    
    private static Random random = new Random();
    
    public World() {
        worldDirectory = new File(worldPath);
        chunksDirectory = new File(chunksPath);
        if (!worldDirectory.exists()) {
            worldDirectory.mkdir();
        }
        if (!chunksDirectory.exists()) {
            chunksDirectory.mkdir();
        }
        player = new Player(new Pos(0, -1), this);
    }
    
    public Chunk getChunk(Pos pos) {
        // We use lookUpPos to avoid creating a Pos instance
        // every time we want to find a chunk.
        lookUpPos.set(pos);
        Chunk.convertPosToChunkPos(lookUpPos);
        Chunk output = chunkMap.get(lookUpPos);
        if (output == null) {
            output = new Chunk(lookUpPos.copy(), this);
            chunkMap.put(lookUpPos, output);
        }
        return output;
    }
    
    public Tile getTile(Pos pos, boolean shouldBeMature) {
        Chunk tempChunk = getChunk(pos);
        return tempChunk.getTile(pos, shouldBeMature);
    }
    
    public void setTile(Pos pos, Tile tile, boolean shouldBeMature) {
        Chunk tempChunk = getChunk(pos);
        tempChunk.setTile(pos, tile, shouldBeMature);
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
    
    public void persist() {
        System.out.println("Persisting world...");
        for (Chunk chunk : chunkMap.values()) {
            chunk.persist();
        }
        System.out.println("Finished persisting world.");
    }
    
    public void timerEvent() {
        persistDelay += 1;
        if (persistDelay > 1800) {
            persist();
            persistDelay = 0;
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
            Tile tempTile = getTile(tempPos, false);
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
            setTile(tempPos, Tile.STONE, false);
            tempOffset.advance(1, 0, tempSize);
        }
    }
}


