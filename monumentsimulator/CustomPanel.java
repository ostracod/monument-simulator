
package monumentsimulator;

import java.util.Random;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.awt.image.DataBufferInt;

import javax.swing.JPanel;

import monumentsimulator.tile.Tile;

public class CustomPanel extends JPanel {
    
    private World world;
    private int size = 512;
    private Random random = new Random();
    private int bufferedImageSize;
    private BufferedImage bufferedImage;
    private int[] pixelArray;
    private Pos cameraPos = new Pos(0, -20);
    
    private static Color textColor = new Color(255, 255, 255);
    private static Font textFont = new Font("Verdana", Font.PLAIN, 14);
    
    public CustomPanel(World inputWorld) {
        super();
        world = inputWorld;
        setUpBufferedImage(32);
    }
    
    public void setUpBufferedImage(int imageSize) {
        bufferedImageSize = imageSize;
        bufferedImage = new BufferedImage(
            bufferedImageSize,
            bufferedImageSize,
            BufferedImage.TYPE_INT_RGB
        );
        WritableRaster tempRaster = bufferedImage.getRaster();
        DataBufferInt tempBuffer = (DataBufferInt)(tempRaster.getDataBuffer());
        pixelArray = tempBuffer.getData();
    }
    
    public void generateImage() {
        Pos tempPlayerPos = world.getPlayer().getPos();
        cameraPos.setX(tempPlayerPos.getX() - bufferedImageSize / 2);
        cameraPos.setY(tempPlayerPos.getY() - bufferedImageSize / 2);
        int index = 0;
        Pos tempOffset = new Pos(0, 0);
        Pos tempPos = new Pos(0, 0);
        while (tempOffset.getY() < bufferedImageSize) {
            tempPos.set(cameraPos);
            tempPos.add(tempOffset);
            Tile tempTile = world.getTile(tempPos, true);
            pixelArray[index] = tempTile.getColor(tempPos);
            index += 1;
            tempOffset.advance(1, 0, bufferedImageSize);
        }
    }
    
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Player tempPlayer = world.getPlayer();
        int tempBrickCount = tempPlayer.getInventoryCount(Tile.BRICK);
        int tempDirtCount = tempPlayer.getInventoryCount(Tile.DIRT);
        graphics.drawImage(bufferedImage, 0, 0, size, size, this);
        graphics.setFont(textFont);
        graphics.setColor(textColor);
        graphics.drawString("Brick: " + tempBrickCount, 8, 20);
        graphics.drawString("Dirt: " + tempDirtCount, 8, 40);
    }
    
    public int getWidth() {
        return size;
    }
    
    public int getHeight() {
        return size;
    }
    
    public World getWorld() {
        return world;
    }
    
    public void zoomIn() {
        int tempNextSize = bufferedImageSize / 2;
        if (tempNextSize < 16) {
            return;
        }
        setUpBufferedImage(tempNextSize);
    }
    
    public void zoomOut() {
        int tempNextSize = bufferedImageSize * 2;
        if (tempNextSize > size) {
            return;
        }
        setUpBufferedImage(tempNextSize);
    }
}


