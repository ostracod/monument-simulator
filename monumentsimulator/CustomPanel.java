
package monumentsimulator;

import java.util.Random;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.awt.image.DataBufferInt;

import javax.swing.JPanel;

public class CustomPanel extends JPanel {
    
    World world;
    private int size = 512;
    private Random random = new Random();
    private int bufferedImageSize;
    private BufferedImage bufferedImage;
    private int[] pixelArray;
    private static Color textColor = new Color(255, 255, 255);
    private static Font textFont = new Font("Verdana", Font.PLAIN, 16);
    
    public CustomPanel(World inputWorld) {
        super();
        world = inputWorld;
        setUpBufferedImage(64);
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
        int index = 0;
        while (index < pixelArray.length) {
            pixelArray[index] = random.nextInt(256) << 16;
            index += 1;
        }
    }
    
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        graphics.drawImage(bufferedImage, 0, 0, size, size, this);
        graphics.setFont(textFont);
        graphics.setColor(textColor);
        graphics.drawString("WOW HELLO!!!", 10, 50);
    }
    
    public int getWidth() {
        return size;
    }
    
    public int getHeight() {
        return size;
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


