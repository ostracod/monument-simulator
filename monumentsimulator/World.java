
package monumentsimulator;

import java.util.Map;
import java.util.Hashtable;

import java.io.File;
import java.nio.file.Paths;

public class World {
    
    private String worldPath = "./monumentWorld";
    private String chunksPath = Paths.get(worldPath, "chunks").toString();
    private File worldDirectory;
    private File chunksDirectory;
    private Map<Pos, Chunk> chunkMap = new Hashtable<Pos, Chunk>(100);
    
    public World() {
        worldDirectory = new File(worldPath);
        chunksDirectory = new File(chunksPath);
        if (!worldDirectory.exists()) {
            worldDirectory.mkdir();
        }
        if (!chunksDirectory.exists()) {
            chunksDirectory.mkdir();
        }
        getChunk(new Pos(0, 0), true);
    }
    
    // pos must conform to chunk spacing.
    public Chunk getChunk(Pos pos, boolean shouldCreateIfMissing) {
        Chunk output = chunkMap.get(pos);
        if (output == null && shouldCreateIfMissing) {
            output = new Chunk(pos);
            chunkMap.put(pos, output);
        }
        return output;
    }
}


