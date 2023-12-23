public class Vector {
   
    public double x, y;

    Vector(double _x, double _y) {
        this.x = _x;
        this.y = _y;
    }

    public void add(Vector v) {
        this.x += v.x;
        this.y += v.y;
    }

    public void mult(double n) {
        this.x *= n;
        this.y *= n;
    }

    public double heading() {
        double heading = (-1 * Math.atan2(this.x, this.y)) + Math.PI/2;
        if (heading < 0) heading += Math.PI * 2;
        if (heading > Math.PI * 2) heading -= Math.PI * 2;
        return heading;
    }

    public double mag() {
        return Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y,  2));
    }

    public void setHeading(double newHeading) {
        double newX = Math.cos(newHeading) * this.mag();
        double newY = Math.sin(newHeading) * this.mag();
        this.x = newX;
        this.y = newY;
    }

    public void rotate(double rot) {
        this.setHeading(this.heading() + rot);
    }

}
