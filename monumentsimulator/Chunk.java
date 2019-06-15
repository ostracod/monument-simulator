
package monumentsimulator;

import java.util.Random;

import monumentsimulator.tile.Tile;

public class Chunk {
    
    public static int size = 128;
    
    private static Random random = new Random();
    
    private Pos pos;
    private int[] tileNumberList = new int[size * size];
    
    public Chunk(Pos inputPos) {
        pos = inputPos;
        int index = 0;
        while (index < tileNumberList.length) {
            tileNumberList[index] = random.nextInt(2);
            index += 1;
        }
    }
    
    public Tile getTile(Pos inputPos) {
        int offsetX = inputPos.getX() - pos.getX();
        int offsetY = inputPos.getY() - pos.getY();
        int tempTileNumber = tileNumberList[offsetX + offsetY * size];
        return Tile.getTileFromNumber(tempTileNumber);
    }
    
    public static void convertPosToChunkPos(Pos inputPos) {
        inputPos.setX(Math.floorDiv(inputPos.getX(), size) * size);
        inputPos.setY(Math.floorDiv(inputPos.getY(), size) * size);
    }
}


