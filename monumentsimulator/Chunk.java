
package monumentsimulator;

import java.util.Random;
import java.util.Arrays;

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
    public static int maximumMaturity = 2;
    
    private static Random random = new Random();
    
    private World world;
    private Pos pos;
    private byte[] tileNumberList = new byte[size * size];
    // Maturity values:
    // -1 = Missing chunk.
    // 0 = Just solid dirt.
    // 1 = Stone clusters populated in current chunk.
    // 2 = Stone clusters populated in adjacent chunks.
    private byte maturity;
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
                maturity = tempStream.readByte();
                tempStream.read(tileNumberList);
                tempStream.close();
            } catch(IOException exception) {
                System.out.println(exception.getMessage());
            }
            isDirty = false;
        } else {
            Tile tempTile;
            if (pos.getY() < 0) {
                tempTile = Tile.EMPTY;
            } else {
                tempTile = Tile.DIRT;
            }
            Arrays.fill(tileNumberList, tempTile.getNumber());
            maturity = 0;
            isDirty = true;
        }
    }
    
    public void addStoneClusters() {
        Pos tempOffset = new Pos(0, 0);
        Pos tempPos = new Pos(0, 0);
        while (tempOffset.getY() < size) {
            tempPos.set(pos);
            tempPos.add(tempOffset);
            if (tempPos.getY() > 0) {
                if (random.nextInt(500) == 0) {
                    world.addStoneCluster(tempPos);
                }
            }
            tempOffset.advance(1, 0, size);
        }
    }
    
    public void advanceMaturity(int inputMaturity) {
        if (maturity == 0 && inputMaturity > 0) {
            addStoneClusters();
            maturity = 1;
        }
        if (maturity == 1 && inputMaturity > 1) {
            Pos tempOffset = new Pos(-size, -size);
            Pos tempPos = new Pos(0, 0);
            while (tempOffset.getY() <= size) {
                tempPos.set(pos);
                tempPos.add(tempOffset);
                Chunk tempChunk = world.getChunk(tempPos, 1);
                tempOffset.advance(size, -size, size * 2);
            }
            maturity = 2;
        }
    }
    
    private int getPosTileIndex(Pos inputPos) {
        int offsetX = inputPos.getX() - pos.getX();
        int offsetY = inputPos.getY() - pos.getY();
        return offsetX + offsetY * size;
    }
    
    public Tile getTile(Pos inputPos) {
        int index = getPosTileIndex(inputPos);
        byte tempTileNumber = tileNumberList[index];
        return Tile.getTileFromNumber(tempTileNumber);
    }
    
    public void setTile(Pos inputPos, Tile tile) {
        byte tempNextTileNumber = tile.getNumber();
        int index = getPosTileIndex(inputPos);
        byte tempPreviousTileNumber = tileNumberList[index];
        if (tempNextTileNumber == tempPreviousTileNumber) {
            return;
        }
        tileNumberList[index] = tempNextTileNumber;
        isDirty = true;
    }
    
    public void persist() {
        if (!isDirty) {
            return;
        }
        try {
            DataOutputStream tempStream = new DataOutputStream(new FileOutputStream(path));
            tempStream.writeByte(maturity);
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


