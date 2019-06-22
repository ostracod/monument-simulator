
package monumentsimulator;

class Rectangle {
    
    // pos is in the top left corner of the rectangle.
    private Pos pos;
    private int width;
    private int height;
    
    public Rectangle(Pos inputPos, int inputWidth, int inputHeight) {
        pos = inputPos;
        width = inputWidth;
        height = inputHeight;
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
    
    public void setPos(Pos inputPos) {
        pos = inputPos;
    }
    
    public void setWidth(int inputWidth) {
        width = inputWidth;
    }
    
    public void setHeight(int inputHeight) {
        height = inputHeight;
    }
    
    public int getArea() {
        return width * height;
    }
    
    public boolean containsPos(Pos inputPos) {
        int inputPosX = inputPos.getX();
        int inputPosY = inputPos.getY();
        int posX = pos.getX();
        int posY = pos.getY();
        return (inputPosX >= posX && inputPosX < posX + width
            && inputPosY >= posY && inputPosY < posY + height);
    }
    
    public void set(Rectangle rectangle) {
        pos = rectangle.getPos().copy();
        width = rectangle.getWidth();
        height = rectangle.getHeight();
    }
}


