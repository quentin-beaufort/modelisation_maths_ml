import java.awt.Color;
import java.awt.Graphics2D;

public class Car {
    
    private final double steeringForce = 0.4;
    private final double accel = 0.5;
    private final double friction = 0.04;
    private final Color carColor = Color.RED;
    private final int spawnX = 650, spawnY = 100;
    private final int size = 16;
    private final double reach = 100.0;
    public ViewPanel owner;
    public Vector pos, spd;
    public double angle;
    public Line[] hitbox = new Line[6];
    public Line[] raycasts = new Line[7];
    public Point[] intersects = new Point[7];
    public Brain brain;
    public Brain bestBrain;
    public long birth;
    public long bestLifeSpan = 0;

    Car(ViewPanel owner) {
        this.respawn();
        this.owner = owner;
        this.brain = new Brain(raycasts.length+1, 3, 3, 1);
        this.brain.randomize();
        this.bestBrain = new Brain(this.brain);
        this.birth = System.nanoTime();

        this.updateHitbox();
        this.updateRaycasts();
    }

    public void steer(double coef) {
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

    public void checkCollision() {
        for (Line l : this.owner.track.innerLines) {
            for (Line h : this.hitbox) {
                if (l.intersect(h) != null) {
                    this.updateAI();
                }
            }
        }
        for (Line l : this.owner.track.outerLines) {
            for (Line h : this.hitbox) {
                if (l.intersect(h) != null) {
                    this.updateAI();
                }
            }
        }
    }

    public void castRays() {
        for (int i = 0; i < this.raycasts.length; i++) {
            boolean changed = false;
            for (Line l : this.owner.track.innerLines) {
                Point inter = l.intersect(this.raycasts[i]);
                if (inter != null) {
                    if (this.intersects[i] != null) {
                        if (inter.dist(this.pos) < this.intersects[i].dist(this.pos)) {
                            this.intersects[i] = inter;
                            changed = true;
                        }
                    } else {
                        this.intersects[i] = inter;
                        changed = true;
                    }
                }
            }
            for (Line l : this.owner.track.outerLines) {
                Point inter = l.intersect(this.raycasts[i]);
                if (inter != null) {
                    if (this.intersects[i] != null) {
                        if (inter.dist(this.pos) < this.intersects[i].dist(this.pos)) {
                            this.intersects[i] = inter;
                            changed = true;
                        }
                    } else {
                        this.intersects[i] = inter;
                        changed = true;
                    }
                }
            }
            if (!changed) this.intersects[i] = null;
        }
    }

    public void update() {
        if (Double.isNaN(this.pos.x) || Double.isNaN(this.pos.y)) this.respawn();
        this.accelerate(1);
        this.act();
        this.pos.add(this.spd);
        this.spd.setHeading(this.angle);
        this.spd.mult(1 - friction);
        if (this.spd.mag() < 0.01) this.spd.mult(0);
        this.updateHitbox();
        this.updateRaycasts();
        this.castRays();
        this.checkCollision();
    }

    public void updateAI() {
        long death = System.nanoTime();
        long lifeSpan = death - this.birth;
        if (lifeSpan > this.bestLifeSpan) {
            this.bestLifeSpan = lifeSpan;
            this.bestBrain = new Brain(this.brain);
            this.mutate();
        } else {
            this.brain = new Brain(this.bestBrain);
            this.mutate();
        }
    }

    public void mutate() {
        this.bestBrain = new Brain(brain);
        this.brain.mutate();
        this.respawn();
    }

    public void respawn() {
        this.pos = new Vector(this.spawnX, this.spawnY);
        this.spd = new Vector(0, 0);
        this.angle = 0;
        this.birth = System.nanoTime();
    }

    public void act() {
        double[] inputs = new double[raycasts.length + 1];
        for (int i = 0; i < raycasts.length; i++) {
            if (this.intersects[i] != null) {
                inputs[i] = intersects[i].dist(pos);
            } else {
                inputs[i] = 1;
            }
        }
        inputs[raycasts.length] = this.spd.mag();

        double[] actions = this.brain.think(inputs);
        this.steer(actions[0]);
    }

    public void updateRaycasts() {
        Vector ray1 = new Vector(this.reach, 0);
        ray1.rotate(Math.PI * 7.0/8.0);
        ray1.rotate(this.angle - Math.PI/2);
        Vector ray2 = new Vector(this.reach, 0);
        ray2.rotate(Math.PI * 3.0/4.0);
        ray2.rotate(this.angle - Math.PI/2);
        Vector ray3 = new Vector(this.reach, 0);
        ray3.rotate(Math.PI * 5.0/8.0);
        ray3.rotate(this.angle - Math.PI/2);
        Vector ray4 = new Vector(this.reach, 0);
        ray4.rotate(this.angle);
        Vector ray5 = new Vector(this.reach, 0);
        ray5.rotate(Math.PI * 3.0/8.0);
        ray5.rotate(this.angle - Math.PI/2);
        Vector ray6 = new Vector(this.reach, 0);
        ray6.rotate(Math.PI * 1.0/4.0);
        ray6.rotate(this.angle - Math.PI/2);
        Vector ray7 = new Vector(this.reach, 0);
        ray7.rotate(Math.PI * 1.0/8.0);
        ray7.rotate(this.angle - Math.PI/2);

        this.raycasts[0] = new Line(this.pos.x, this.pos.y, this.pos.x + ray1.x, this.pos.y + ray1.y);
        this.raycasts[1] = new Line(this.pos.x, this.pos.y, this.pos.x + ray2.x, this.pos.y + ray2.y);
        this.raycasts[2] = new Line(this.pos.x, this.pos.y, this.pos.x + ray3.x, this.pos.y + ray3.y);
        this.raycasts[3] = new Line(this.pos.x, this.pos.y, this.pos.x + ray4.x, this.pos.y + ray4.y);
        this.raycasts[4] = new Line(this.pos.x, this.pos.y, this.pos.x + ray5.x, this.pos.y + ray5.y);
        this.raycasts[5] = new Line(this.pos.x, this.pos.y, this.pos.x + ray6.x, this.pos.y + ray6.y);
        this.raycasts[6] = new Line(this.pos.x, this.pos.y, this.pos.x + ray7.x, this.pos.y + ray7.y);
    }

    public void updateHitbox() {
        Vector diag1 = new Vector(this.size * 0.9, 0);
        diag1.rotate(Math.PI * (5.0/6.0));
        diag1.rotate(this.angle);
        Vector diag2 = new Vector(this.size * 0.9, 0);
        diag2.rotate(Math.PI * (1.0/6.0));
        diag2.rotate(this.angle);
        Vector diag3 = new Vector(this.size * 0.9, 0);
        diag3.rotate(Math.PI * (11.0/6.0));
        diag3.rotate(this.angle);
        Vector diag4 = new Vector(this.size * 0.9, 0);
        diag4.rotate(Math.PI * (7.0/6.0));
        diag4.rotate(this.angle);

        this.hitbox[0] = new Line(this.pos.x + diag1.x, this.pos.y + diag1.y, this.pos.x + diag2.x, this.pos.y + diag2.y);
        this.hitbox[1] = new Line(this.pos.x + diag2.x, this.pos.y + diag2.y, this.pos.x + diag3.x, this.pos.y + diag3.y);
        this.hitbox[2] = new Line(this.pos.x + diag3.x, this.pos.y + diag3.y, this.pos.x + diag4.x, this.pos.y + diag4.y);
        this.hitbox[3] = new Line(this.pos.x + diag4.x, this.pos.y + diag4.y, this.pos.x + diag1.x, this.pos.y + diag1.y);
        this.hitbox[4] = new Line(this.pos.x + diag1.x, this.pos.y + diag1.y, this.pos.x + diag3.x, this.pos.y + diag3.y);
        this.hitbox[5] = new Line(this.pos.x + diag4.x, this.pos.y + diag4.y, this.pos.x + diag2.x, this.pos.y + diag2.y);
    }

    public void paint(Graphics2D g) {
        if (this.owner.DEBUG) {
            g.setColor(Color.yellow);
            for (Point p : this.intersects) {
                if (p != null) {
                    g.fillRect((int) p.x - 2, (int) p.y - 2, 4, 4);
                    // g.drawLine((int) pos.x, (int) pos.y, (int) p.x, (int) p.y);
                }
            }
        }

        double x = Math.cos(Math.PI/6) * this.size;
        double y = Math.sin(Math.PI/6) * this.size;

        g.translate(this.pos.x, this.pos.y);
        g.rotate(this.angle);
        g.setColor(this.carColor);
        g.fillRoundRect((int) -x, (int) -y, (int) x*2, (int) y*2, this.size/5, this.size/5);
        g.rotate(-this.angle);
        g.translate(-this.pos.x, -this.pos.y);

        if (this.owner.DEBUG) this.brain.paint(g);
        if (this.owner.DEBUG) {
            long bestLS = this.bestLifeSpan / 1000000;
            g.setColor(Color.black);
            g.drawString("Best life span : " + bestLS + "ms", 15, 15);
        }
    }
}
