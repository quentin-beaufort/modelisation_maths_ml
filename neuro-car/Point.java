public class Point {
    
    public double x, y;

    Point() {}

    Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public boolean equals(Point p) {
        return p.x == this.x && p.y == this.y;
    }

    public double dist(Point p) {
        return Math.sqrt(Math.pow(p.x - this.x, 2) + Math.pow(p.y - this.y, 2));
    }

    public double dist(Vector v) {
        return Math.sqrt(Math.pow(v.x - this.x, 2) + Math.pow(v.y - this.y, 2));
    }

}
