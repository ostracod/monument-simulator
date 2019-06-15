
package monumentsimulator;

import java.util.Map;
import java.util.Hashtable;

import java.io.File;
import java.nio.file.Paths;

import monumentsimulator.tile.Tile;

public class World {
    
    private String worldPath = "./monumentWorld";
    private String chunksPath = Paths.get(worldPath, "chunks").toString();
    private File worldDirectory;
    private File chunksDirectory;
    private Map<Pos, Chunk> chunkMap = new Hashtable<Pos, Chunk>(100);
    private Pos lookUpPos = new Pos(0, 0);
    private static int persistDelay = 0;
    
    public World() {
        worldDirectory = new File(worldPath);
        chunksDirectory = new File(chunksPath);
        if (!worldDirectory.exists()) {
            worldDirectory.mkdir();
        }
        if (!chunksDirectory.exists()) {
            chunksDirectory.mkdir();
        }
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
    
    public Tile getTile(Pos pos) {
        Chunk tempChunk = getChunk(pos);
        return tempChunk.getTile(pos);
    }
    
    public String getChunksPath() {
        return chunksPath;
    }
    
    public File getChunksDirectory() {
        return chunksDirectory;
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
}


