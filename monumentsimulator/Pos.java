
package monumentsimulator;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Pos implements Comparable<Pos> {
    
    private int x;
    private int y;
    
    public Pos(int inputX, int inputY) {
        x = inputX;
        y = inputY;
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public void setX(int inputX) {
        x = inputX;
    }
    
    public void setY(int inputY) {
        y = inputY;
    }
    
    public Pos copy() {
        return new Pos(x, y);
    }
    
    public void set(Pos pos) {
        x = pos.getX();
        y = pos.getY();
    }
    
    public void add(Pos pos) {
        x += pos.getX();
        y += pos.getY();
    }
    
    public void subtract(Pos pos) {
        x -= pos.getX();
        y -= pos.getY();
    }
    
    public void scale(double number) {
        x *= number;
        y *= number;
    }
    
    public void advance(int offset, int startX, int endX) {
        x += offset;
        if (x >= endX) {
            x = startX;
            y += offset;
        }
    }
    
    @Override
    public int hashCode() {
        return (x + 30000) + ((y + 30000) << 16);
    }
    
    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (!(object instanceof Pos)) {
            return false;
        }
        Pos tempPos = (Pos)object;
        return (x == tempPos.getX() && y == tempPos.getY());
    }
    
    @Override
    public int compareTo(Pos pos) {
        int tempPosY = pos.getY();
        if (y != tempPosY) {
            return tempPosY - y;
        }
        return pos.getX() - x;
    }
    
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
    
    public void writeToStream(DataOutputStream stream) throws IOException {
        stream.writeInt(x);
        stream.writeInt(y);
    }
    
    public static Pos readFromStream(DataInputStream stream) throws IOException {
        return new Pos(stream.readInt(), stream.readInt());
    }
}
