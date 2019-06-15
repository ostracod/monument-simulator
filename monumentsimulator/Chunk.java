
package monumentsimulator;

import java.util.Random;

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
    
    public static void convertPosToChunkPos(Pos pos) {
        pos.setX(Math.floorMod(pos.getX(), size));
        pos.setY(Math.floorMod(pos.getY(), size));
    }
    
}


