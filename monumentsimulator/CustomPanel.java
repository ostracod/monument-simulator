
package monumentsimulator;

import java.util.Random;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.awt.image.DataBufferInt;

import javax.swing.JPanel;

import monumentsimulator.tile.Tile;
import monumentsimulator.tile.BrickTile;

public class CustomPanel extends JPanel {
    
    private World world;
    private int size = 512;
    private Random random = new Random();
    private int bufferedImageSize;
    private BufferedImage bufferedImage;
    private int[] pixelArray;
    private Pos cameraPos = new Pos(0, -20);
    private FontMetrics fontMetrics;
    
    private static Color textColor = new Color(255, 255, 255);
    private static Color barForegroundColor = new Color(255, 255, 255);
    private static Color barBackgroundColor = new Color(0, 0, 0);
    private static Font textFont = new Font("Verdana", Font.PLAIN, 15);
    private static int barWidth = 70;
    private static int barHeight = 5;
    
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
        Pos tempPos = new Pos(0, 0);
        int index = 0;
        Pos tempOffset = new Pos(0, 0);
        while (tempOffset.getY() < bufferedImageSize) {
            tempPos.set(cameraPos);
            tempPos.add(tempOffset);
            Tile tempTile = world.getTile(tempPos);
            pixelArray[index] = tempTile.getColor(tempPos);
            index += 1;
            tempOffset.advance(1, 0, bufferedImageSize);
        }
    }
    
    public void drawTextRight(Graphics graphics, String text, int posY) {
        int tempWidth = fontMetrics.stringWidth(text);
        graphics.drawString(text, size - 8 - tempWidth, posY);
    }
    
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Player tempPlayer = world.getPlayer();
        int tempBrickCount = tempPlayer.getInventoryCount(Tile.BRICK);
        int tempDirtCount = tempPlayer.getInventoryCount(Tile.DIRT);
        graphics.drawImage(bufferedImage, 0, 0, size, size, this);
        graphics.setFont(textFont);
        graphics.setColor(textColor);
        fontMetrics = graphics.getFontMetrics();
        graphics.drawString("Brick: " + tempBrickCount, 19, 20);
        graphics.drawString("Dirt: " + tempDirtCount, 19, 40);
        int tempOffsetY;
        if (tempPlayer.getSelectedInventoryTile() instanceof BrickTile) {
            tempOffsetY = 8;
        } else {
            tempOffsetY = 28;
        }
        int[] posXList = {8, 14, 8};
        int[] posYList = {0 + tempOffsetY, 6 + tempOffsetY, 12 + tempOffsetY};
        Polygon tempPolygon = new Polygon(posXList, posYList, posXList.length);
        graphics.fillPolygon(tempPolygon);
        if (tempPlayer.getIsMining()) {
            double tempProgress = tempPlayer.getMiningProgress();
            graphics.setColor(barBackgroundColor);
            graphics.fillRect(8, 50, barWidth, barHeight);
            graphics.setColor(barForegroundColor);
            graphics.fillRect(8, 50, (int)(barWidth * tempProgress), barHeight);
            graphics.setColor(textColor);
        }
        String tempMessage = world.getMessageToDisplay();
        if (tempMessage != null) {
            graphics.drawString(tempMessage, 8, 60);
        }
        drawTextRight(graphics, "Pos: " + tempPlayer.getPos().toString(), 20);
        Monument tempMonument = world.getMonument();
        int tempWidth = tempMonument.getWidth();
        int tempHeight = tempMonument.getHeight();
        drawTextRight(graphics, "Score: " + tempWidth + " x " + tempHeight, 40);
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


