
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

import monumentsimulator.tile.Tile;

public class MonumentSimulator {
    
    private static World world;
    private static CustomPanel panel;
    private static boolean shiftKeyIsHeld = false;
    private static Pos[] playerOffsetSet = {
        new Pos(-1, 0),
        new Pos(1, 0),
        new Pos(0, -1),
        new Pos(0, 1)
    };
    
    public static void main(String[] args){
        
        System.out.println("Starting up...");
        world = new World();
        JFrame frame = new JFrame();
        panel = new CustomPanel(world);
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
                timerEvent();
            }
        };
        Timer timer = new Timer(33, actionListener);
        timer.start();
        KeyListener keyListener = new KeyListener() {
            public void keyPressed(KeyEvent actionEvent) {
                int keyCode = actionEvent.getKeyCode();
                keyPressedEvent(keyCode);
            }
            public void keyReleased(KeyEvent actionEvent) {
                int keyCode = actionEvent.getKeyCode();
                keyReleasedEvent(keyCode);
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
    
    public static void performDirectedPlayerAction(int direction) {
        Pos tempOffset = playerOffsetSet[direction];
        Player tempPlayer = panel.getWorld().getPlayer();
        if (shiftKeyIsHeld) {
            tempPlayer.buildOrStartMining(tempOffset);
        } else {
            tempPlayer.startWalking(tempOffset);
        }
    }
    
    public static void stopPlayerWalk(int direction) {
        Pos tempOffset = playerOffsetSet[direction];
        Player tempPlayer = panel.getWorld().getPlayer();
        tempPlayer.stopWalking(tempOffset);
    }
    
    public static void keyPressedEvent(int keyCode) {
        if (keyCode == 16) {
            shiftKeyIsHeld = true;
        }
        if (keyCode == 61 && shiftKeyIsHeld) {
            panel.zoomIn();
        }
        if (keyCode == 45 && !shiftKeyIsHeld) {
            panel.zoomOut();
        }
        // Left / A.
        if (keyCode == 37 || keyCode == 65) {
            performDirectedPlayerAction(0);
        }
        // Right / D.
        if (keyCode == 39 || keyCode == 68) {
            performDirectedPlayerAction(1);
        }
        // Up / W.
        if (keyCode == 38 || keyCode == 87) {
            performDirectedPlayerAction(2);
        }
        // Down / S.
        if (keyCode == 40 || keyCode == 83) {
            performDirectedPlayerAction(3);
        }
        Player tempPlayer = panel.getWorld().getPlayer();
        // 1.
        if (keyCode == 49) {
            tempPlayer.selectInventoryTile(Tile.BRICK);
        }
        // 2.
        if (keyCode == 50) {
            tempPlayer.selectInventoryTile(Tile.DIRT);
        }
    }
    
    public static void keyReleasedEvent(int keyCode) {
        if (keyCode == 16) {
            shiftKeyIsHeld = false;
        }
        // Left / A.
        if (keyCode == 37 || keyCode == 65) {
            stopPlayerWalk(0);
        }
        // Right / D.
        if (keyCode == 39 || keyCode == 68) {
            stopPlayerWalk(1);
        }
        // Up / W.
        if (keyCode == 38 || keyCode == 87) {
            stopPlayerWalk(2);
        }
        // Down / S.
        if (keyCode == 40 || keyCode == 83) {
            stopPlayerWalk(3);
        }
    }
    
    public static void timerEvent() {
        world.timerEvent();
        panel.generateImage();
        panel.repaint();
    }
    
    public static void cleanUp() {
        world.persist();
    }
}


