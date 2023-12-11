public class Line {
    
    public Point a, b;

    Line() {}

    Line(Point a, Point b) {
        this.a = a;
        this.b = b;
    }

    Line(int xa, int ya, int xb, int yb) {
        this.a = new Point(xa, ya);
        this.b = new Point(xb, yb);
    }

}
