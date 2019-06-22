
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

public class Monument extends Rectangle {
    
    private World world;
    
    public Monument(
        Pos pos,
        int width,
        int height,
        World inputWorld
    ) {
        super(pos, width, height);
        world = inputWorld;
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
    
    private Rectangle findLargestRectangle(Pos pos) {
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
        // Find best combination of top boundary and bottom boundary.
        int bestArea = 0;
        Rectangle output = new Rectangle(new Pos(0, 0), 0, 0);
        int topIndex = 0;
        while (topIndex < topBoundaryList.size()) {
            RectangleBoundary topBoundary = topBoundaryList.get(topIndex);
            int bottomIndex = 0;
            while (bottomIndex < bottomBoundaryList.size()) {
                RectangleBoundary bottomBoundary = bottomBoundaryList.get(bottomIndex);
                int tempPosX1 = Math.max(
                    topBoundary.getLeftPosX(),
                    bottomBoundary.getLeftPosX()
                );
                int tempPosY1 = topBoundary.getPosY();
                int tempPosX2 = Math.min(
                    topBoundary.getRightPosX(),
                    bottomBoundary.getRightPosX()
                );
                int tempPosY2 = bottomBoundary.getPosY();
                int tempWidth = tempPosX2 - tempPosX1 + 1;
                int tempHeight = tempPosY2 - tempPosY1 + 1;
                int tempArea = tempWidth * tempHeight;
                if (tempArea > bestArea) {
                    bestArea = tempArea;
                    Pos tempRectanglePos = output.getPos();
                    tempRectanglePos.setX(tempPosX1);
                    tempRectanglePos.setY(tempPosY1);
                    output.setWidth(tempWidth);
                    output.setHeight(tempHeight);
                }
                bottomIndex += 1;
            }
            topIndex += 1;
        }
        return output;
    }
    
    public Rectangle findLargestSubdivision(Pos inputPos) {
        // TODO: Implement.
        
        return new Rectangle(new Pos(0, 0), 0, 0);
    }
    
    public void setTileEvent(Pos inputPos, Tile tile) {
        if (tile instanceof BrickTile) {
            Rectangle tempRectangle = findLargestRectangle(inputPos);
            if (tempRectangle.getArea() > getArea()) {
                set(tempRectangle);
            }
        } else if (containsPos(inputPos)) {
            Rectangle tempRectangle = findLargestSubdivision(inputPos);
            set(tempRectangle);
        }
    }
}


