public class Line {
    
    public Point a, b;

    Line() {}

    Line(Point a, Point b) {
        this.a = a;
        this.b = b;
    }

    Line(double xa, double ya, double xb, double yb) {
        this.a = new Point(xa, ya);
        this.b = new Point(xb, yb);
    }

    public Point intersect(Line other) {
        double x1 = this.a.x;
        double x2 = this.b.x;
        double x3 = other.a.x;
        double x4 = other.b.x;
        double y1 = this.a.y;
        double y2 = this.b.y;
        double y3 = other.a.y;
        double y4 = other.b.y;
        
        double denom = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);
        double t = ((x1 - x3) * (y3 - y4) - (y1 - y3) * (x3 - x4)) / denom;
        double u = ((x1 - x3) * (y1 - y2) - (y1 - y3) * (x1 - x2)) / denom;

        if (0 <= t && t <= 1 && 0 <= u && u <= 1) {
            double intersectX = (x1 + t * (x2 - x1));
            double intersectY = (y1 + t * (y2 - y1));
            return new Point(intersectX, intersectY);
        }

        return null;
    }

}
