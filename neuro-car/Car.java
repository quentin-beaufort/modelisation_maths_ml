import java.awt.Color;
import java.awt.Graphics2D;

public class Car {
    
    private final double steeringForce = 0.3;
    private final double accel = 0.5;
    private final double friction = 0.04;
    private final Color carColor = Color.RED;
    private final int spawnX = 650, spawnY = 100;
    private final int size = 24;
    public Vector pos, spd;
    public double angle;

    Car() {
        this.pos = new Vector(this.spawnX, this.spawnY);
        this.spd = new Vector(0, 0);
        this.angle = 0;
    }

    public void rotate(double coef) {
        this.angle += this.steeringForce * coef * (this.spd.mag() > 1 ? 1 : this.spd.mag());
        if (this.angle > Math.PI * 2) this.angle -= Math.PI * 2;
        if (this.angle < 0) this.angle += Math.PI * 2;
    }

    public void accelerate(double coef) {
        double forceX = Math.cos(this.angle) * accel * coef;
        double forceY = Math.sin(this.angle) * accel * coef;
        Vector forceVector = new Vector((float) forceX, (float) forceY);
        this.spd.add(forceVector);
    }

    public void update() {
        this.pos.add(this.spd);
        this.spd.setHeading(this.angle);
        this.spd.mult(1 - friction);
        if (this.spd.mag() < 0.01) this.spd.mult(0);
    }

    public void paint(Graphics2D g) {
        g.translate(this.pos.x, this.pos.y);
        g.rotate(this.angle);
        g.setColor(this.carColor);
        g.fillRoundRect(-this.size/2, -this.size/4, this.size, this.size/2, this.size/5, this.size/5);
        g.rotate(-this.angle);
        g.translate(-this.pos.x, -this.pos.y);
    }

}
