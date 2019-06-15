
package monumentsimulator;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class MonumentSimulator {
    
    public static void main(String[] args){
        
        System.out.println("Starting up!");
        JFrame frame = new JFrame();
        CustomPanel panel = new CustomPanel();
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.pack();
        frame.setSize(512, 512);
        Insets insets = frame.getInsets();
        frame.setSize(
            insets.left + insets.right + panel.getWidth(),
            insets.top + insets.bottom + panel.getHeight()
        );
        frame.setContentPane(panel);
        frame.setTitle("Monument Simulator");
        frame.setVisible(true);
        ActionListener actionListener = new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                panel.generateImage();
                panel.repaint();
            }
        };
        Timer timer = new Timer(33, actionListener);
        timer.start();
        KeyListener keyListener = new KeyListener() {
            public void keyPressed(KeyEvent actionEvent) {
                int keyCode = actionEvent.getKeyCode();
                System.out.println("Key pressed! " + keyCode);
            }
            public void keyReleased(KeyEvent actionEvent) {
                int keyCode = actionEvent.getKeyCode();
                System.out.println("Key released! " + keyCode);
            }
            public void keyTyped(KeyEvent actionEvent) {
                // Do nothing.
            }
        };
        frame.addKeyListener(keyListener);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                cleanUp();
            }
        });
    }
    
    public static void cleanUp() {
        System.out.println("Goodbye!");
    }
}


