import java.awt.Color;
import java.awt.Graphics2D;

public class Car {
    
    private final double steeringForce = 0.1;
    private final double friction = 0.05;
    private final Color carColor = Color.RED;
    public Vector pos, spd;
    public double angle;

    Car(int _x, int _y) {
        this.pos = new Vector(_x, _y);
        this.spd = new Vector(0, 0);
        this.angle = 0;
    }

    public void rotate(double coef) {
        this.angle += this.steeringForce * coef;
    }

    public void accelerate(double force) {
        double forceX = Math.cos(this.angle) * force;
        double forceY = Math.sin(this.angle) * force;
        Vector forceVector = new Vector((float) forceX, (float) forceY);
        this.spd.add(forceVector);
    }

    public void update() {
        this.pos.add(this.spd);
        this.spd.mult(1 - friction);
    }

    public void paint(Graphics2D g) {
        g.translate(this.pos.x, this.pos.y);
        g.rotate(this.angle);
        g.setColor(this.carColor);
        g.fillRoundRect(-16, -8, 32, 16, 3, 3);
        g.rotate(-this.angle);
        g.translate(-this.pos.x, -this.pos.y);
    }

}
