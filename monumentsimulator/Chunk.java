
package monumentsimulator;

import java.util.Random;

import java.io.File;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;

import monumentsimulator.tile.Tile;

public class Chunk {
    
    public static int size = 256;
    
    private static Random random = new Random();
    
    private World world;
    private Pos pos;
    private byte[] tileNumberList = new byte[size * size];
    private boolean isMature;
    private String path;
    private File file;
    private boolean isDirty;
    
    public Chunk(Pos inputPos, World inputWorld) {
        pos = inputPos;
        world = inputWorld;
        path = Paths.get(
            world.getChunksPath(),
            "chunk_" + pos.getX() + "_" + pos.getY() + ".dat"
        ).toString();
        file = new File(path);
        if (file.exists()) {
            try {
                DataInputStream tempStream = new DataInputStream(new FileInputStream(path));
                byte tempValue = tempStream.readByte();
                isMature = (tempValue != 0);
                tempStream.read(tileNumberList);
                tempStream.close();
            } catch(IOException exception) {
                System.out.println(exception.getMessage());
            }
            isDirty = false;
        } else {
            int index = 0;
            while (index < tileNumberList.length) {
                byte tempTileNumber;
                if (random.nextInt(5) == 0) {
                    tempTileNumber = 1;
                } else {
                    tempTileNumber = 0;
                }
                tileNumberList[index] = tempTileNumber;
                index += 1;
            }
            isMature = false;
            isDirty = true;
        }
    }
    
    public Tile getTile(Pos inputPos) {
        int offsetX = inputPos.getX() - pos.getX();
        int offsetY = inputPos.getY() - pos.getY();
        byte tempTileNumber = tileNumberList[offsetX + offsetY * size];
        return Tile.getTileFromNumber(tempTileNumber);
    }
    
    public void persist() {
        if (!isDirty) {
            return;
        }
        try {
            DataOutputStream tempStream = new DataOutputStream(new FileOutputStream(path));
            if (isMature) {
                tempStream.writeByte(1);
            } else {
                tempStream.writeByte(0);
            }
            tempStream.write(tileNumberList);
            tempStream.close();
        } catch(IOException exception) {
            System.out.println(exception.getMessage());
        }
        isDirty = false;
    }
    
    public static void convertPosToChunkPos(Pos inputPos) {
        inputPos.setX(Math.floorDiv(inputPos.getX(), size) * size);
        inputPos.setY(Math.floorDiv(inputPos.getY(), size) * size);
    }
}


