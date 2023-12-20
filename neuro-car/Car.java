public class Car {
    
    public Vector pos, spd;
    public double angle;

    Car(int _x, int _y) {
        this.pos = new Vector(_x, _y);
        this.spd = new Vector(0, 0);
        this.angle = Math.PI/2;
    }

}
