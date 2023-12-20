public class Point {
    
    public int x, y;

    Point() {}

    Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean equals(Point p) {
        return p.x == this.x && p.y == this.y;
    }

}
