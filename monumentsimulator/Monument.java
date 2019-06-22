
package monumentsimulator;

import java.util.List;
import java.util.ArrayList;

import monumentsimulator.tile.Tile;
import monumentsimulator.tile.BrickTile;

class RectangleBoundary {
    
    private int posY;
    private int leftPosX;
    private int rightPosX;
    
    public RectangleBoundary(
        int inputPosY,
        int inputLeftPosX,
        int inputRightPosX
    ) {
        posY = inputPosY;
        leftPosX = inputLeftPosX;
        rightPosX = inputRightPosX;
    }
    
    public int getPosY() {
        return posY;
    }
    
    public int getLeftPosX() {
        return leftPosX;
    }
    
    public int getRightPosX() {
        return rightPosX;
    }
    
    public String toString() {
        return "(" + posY + ": " + leftPosX + ", " + rightPosX + ")";
    }
}

public class Monument {
    
    private Pos pos;
    private int width;
    private int height;
    private World world;
    
    public Monument(
        Pos inputPos,
        int inputWidth,
        int inputHeight,
        World inputWorld
    ) {
        pos = inputPos;
        width = inputWidth;
        height = inputHeight;
        world = inputWorld;
    }
    
    public Pos getPos() {
        return pos;
    }
    
    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }
    
    private int seekBoundaryTile(int posX, int startPosY, int endPosY, int offsetY) {
        Pos tempPos = new Pos(posX, 0);
        int tempPosY = startPosY;
        while (true) {
            int nextPosY = tempPosY + offsetY;
            if (offsetY < 0) {
                if (nextPosY < endPosY) {
                    break;
                }
            } else {
                if (nextPosY > endPosY) {
                    break;
                }
            }
            tempPos.setY(nextPosY);
            Tile tempTile = world.getTile(tempPos, true);
            if (!(tempTile instanceof BrickTile)) {
                break;
            }
            tempPosY = nextPosY;
        }
        return tempPosY;
    }
    
    // Returns an array of two positions: one at the
    // top left corner, the other at the bottom right
    // corner.
    private Pos[] findLargestRectangle(Pos pos) {
        int centerPosX = pos.getX();
        int centerPosY = pos.getY();
        Pos tempPos = new Pos(centerPosX, 0);
        int topPosY = centerPosY;
        int bottomPosY = centerPosY;
        // Move topPosY to the top brick.
        while (true) {
            int nextPosY = topPosY - 1;
            tempPos.setY(nextPosY);
            Tile tempTile = world.getTile(tempPos, true);
            if (!(tempTile instanceof BrickTile)) {
                break;
            }
            topPosY = nextPosY;
        }
        // Move bottomPosY to the bottom brick.
        while (true) {
            int nextPosY = bottomPosY + 1;
            tempPos.setY(nextPosY);
            Tile tempTile = world.getTile(tempPos, true);
            if (!(tempTile instanceof BrickTile)) {
                break;
            }
            bottomPosY = nextPosY;
        }
        List<RectangleBoundary> topBoundaryList = new ArrayList<RectangleBoundary>();
        List<RectangleBoundary> bottomBoundaryList = new ArrayList<RectangleBoundary>();
        int leftPosX;
        int leftPosY;
        int rightPosX;
        int rightPosY;
        int tempPosY;
        // Create the list of top boundaries.
        leftPosX = centerPosX;
        leftPosY = topPosY;
        rightPosX = centerPosX;
        rightPosY = topPosY;
        tempPosY = topPosY;
        while (tempPosY <= centerPosY) {
            while (leftPosY <= tempPosY) {
                leftPosX -= 1;
                leftPosY = seekBoundaryTile(leftPosX, centerPosY + 1, leftPosY, -1);
            }
            while (rightPosY <= tempPosY) {
                rightPosX += 1;
                rightPosY = seekBoundaryTile(rightPosX, centerPosY + 1, rightPosY, -1);
            }
            RectangleBoundary tempBoundary = new RectangleBoundary(
                tempPosY,
                leftPosX + 1,
                rightPosX - 1
            );
            topBoundaryList.add(tempBoundary);
            tempPosY += 1;
        }
        // Create the list of bottom boundaries.
        leftPosX = centerPosX;
        leftPosY = bottomPosY;
        rightPosX = centerPosX;
        rightPosY = bottomPosY;
        tempPosY = bottomPosY;
        while (tempPosY >= centerPosY) {
            while (leftPosY >= tempPosY) {
                leftPosX -= 1;
                leftPosY = seekBoundaryTile(leftPosX, centerPosY - 1, leftPosY, 1);
            }
            while (rightPosY >= tempPosY) {
                rightPosX += 1;
                rightPosY = seekBoundaryTile(rightPosX, centerPosY - 1, rightPosY, 1);
            }
            RectangleBoundary tempBoundary = new RectangleBoundary(
                tempPosY,
                leftPosX + 1,
                rightPosX - 1
            );
            bottomBoundaryList.add(tempBoundary);
            tempPosY -= 1;
        }
        
        // TEST CODE.
        int index;
        index = 0;
        while (index < topBoundaryList.size()) {
            RectangleBoundary tempBoundary = topBoundaryList.get(index);
            System.out.println(tempBoundary);
            index += 1;
        }
        index = 0;
        while (index < bottomBoundaryList.size()) {
            RectangleBoundary tempBoundary = bottomBoundaryList.get(index);
            System.out.println(tempBoundary);
            index += 1;
        }
        
        Pos[] output = {null, null};
        return output;
    }
    
    public void setTileEvent(Pos pos, Tile tile) {
        if (tile instanceof BrickTile) {
            Pos[] posPair = findLargestRectangle(pos);
            // TODO: Update the monument.
            
        }
    }
}


