
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
    
    private int width = 512;
    private int height = 512;
    private Random random;
    private BufferedImage bufferedImage;
    private int[] pixelArray;
    private static Color textColor = new Color(255, 255, 255);
    private static Font textFont = new Font("Verdana", Font.PLAIN, 16);;
    
    public CustomPanel() {
        super();
        random = new Random();
        bufferedImage = new BufferedImage(64, 64, BufferedImage.TYPE_INT_RGB);
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
        graphics.drawImage(bufferedImage, 0, 0, width, height, this);
        graphics.setFont(textFont);
        graphics.setColor(textColor);
        graphics.drawString("WOW HELLO!!!", 10, 50);
    }
    
    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }
    
}


