/*
 * This Class is the object inside a picture.
 * The object has name and coordinates (int x,int y).
 */
public class Tag implements ObjectWithCoordinates {

    // property
    private final String name;
    private final int x;
    private final int y;

    // constructor
    public Tag(String name,int x,int y){
        this.name = name;
        this.x = x;
        this.y = y;
    }

    // getter & setter
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public String getData() {
        return name;
    }
}
