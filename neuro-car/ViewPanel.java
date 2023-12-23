import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JPanel;

public class ViewPanel extends JPanel implements Runnable {
    
    private final int FPS = 60;
    private final double thicknessSensitivity = 3.0;
    private final double angleSensitivity = 0.2;
    private final boolean loadByDefault = true;
    public boolean DEBUG = true;
    private Color grassColor = new Color(0, 150, 0);
    public Track track;
    private InputHandler inputs;
    private Thread gameThread;
    private boolean gameRunning = true;
    private Point currentPoint = null;
    private double currentAngle = Math.PI/2;
    private double lastAngle = 0;
    private int currentThickness = 30;
    private int lastThickness = 0;
    private int trackPieceId = 0;
    private int mouseX, mouseY;
    private Car car;

    ViewPanel(int width, int height) {
        this.setPreferredSize(new Dimension(width, height));
        this.track = new Track();

        this.inputs = new InputHandler(this);
        this.addKeyListener(inputs); 
        this.setFocusable(true);

        this.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                addTrackPiece(e.getX(), e.getY(), currentAngle);
                currentPoint = new Point(e.getX(), e.getY());
                lastAngle = currentAngle;
                lastThickness = currentThickness;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
            
        });

        this.addMouseWheelListener(new MouseWheelListener() {

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                int rotation = e.getWheelRotation();
                if (inputs.shiftMod) {
                    currentThickness += rotation * thicknessSensitivity;
                } else {
                    currentAngle += rotation * angleSensitivity;
                }
            }
            
        });

        this.addMouseMotionListener(new MouseMotionListener() {

            @Override
            public void mouseDragged(MouseEvent e) {
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
            }
            
        });

        if (this.loadByDefault) this.track.loadTrack();

        this.gameThread = new Thread(this);
        this.gameThread.start();
    }

    public void respawnCar() {
        this.car = new Car(this);
    }

    public void toggleDebug() {
        this.DEBUG = !this.DEBUG;
    }

    private void addTrackPiece(int x, int y, double a) {
        if (this.currentPoint == null) return;
        this.trackPieceId++;
    }

    private void newTrack() {
        this.track = new Track();
        this.currentAngle = Math.PI/2;
        this.lastAngle = Math.PI/2;
        this.currentThickness = 30;
        this.lastThickness = 30;
        this.currentPoint = null;
    }

    private void closeTrack() {
        double lastInnerX = (currentPoint.x + Math.cos(lastAngle) * lastThickness);
        double lastInnerY = (currentPoint.y + Math.sin(lastAngle) * lastThickness);
        double lastOuterX = (currentPoint.x - Math.cos(lastAngle) * lastThickness);
        double lastOuterY = (currentPoint.y - Math.sin(lastAngle) * lastThickness);
        double newInnerX = this.track.innerLines.get(0).a.x;
        double newInnerY = this.track.innerLines.get(0).a.y;
        double newOuterX = this.track.outerLines.get(0).b.x;
        double newOuterY = this.track.outerLines.get(0).b.y;

        this.track.innerLines.set(this.trackPieceId, new Line(lastInnerX, lastInnerY, newInnerX, newInnerY));
        this.track.outerLines.set(this.trackPieceId, new Line(lastOuterX, lastOuterY, newOuterX, newOuterY));

        this.track.close();
    }

    @Override
    public void run() {
        while (this.gameRunning) {

            long start = System.nanoTime();

            this.repaint();
            if (!this.track.closed && currentPoint != null) {
                if (this.inputs.spaceDown) this.closeTrack();
                while (track.innerLines.size() < trackPieceId+1) track.innerLines.add(new Line());
                while (track.outerLines.size() < trackPieceId+1) track.outerLines.add(new Line());
                double lastInnerX = (currentPoint.x + Math.cos(lastAngle) * lastThickness);
                double lastInnerY = (currentPoint.y + Math.sin(lastAngle) * lastThickness);
                double lastOuterX = (currentPoint.x - Math.cos(lastAngle) * lastThickness);
                double lastOuterY = (currentPoint.y - Math.sin(lastAngle) * lastThickness);
                double newInnerX = (mouseX + Math.cos(currentAngle) * currentThickness);
                double newInnerY = (mouseY + Math.sin(currentAngle) * currentThickness);
                double newOuterX = (mouseX - Math.cos(currentAngle) * currentThickness);
                double newOuterY = (mouseY - Math.sin(currentAngle) * currentThickness);
                try {
                    track.innerLines.set(trackPieceId, new Line(lastInnerX, lastInnerY, newInnerX, newInnerY));
                    track.outerLines.set(trackPieceId, new Line(lastOuterX, lastOuterY, newOuterX, newOuterY));
                } catch (Exception e) {}
            }
            if (this.inputs.lDown) this.track.loadTrack();
            if (this.inputs.mDown) this.track.saveTrack();
            if (this.inputs.kDown) {
                this.newTrack();
            }
            if (this.track.closed && this.car == null) {
                this.car = new Car(this);
            }

            if (this.inputs.z) this.car.accelerate(1);
            if (this.inputs.s) this.car.accelerate(-1);
            if (this.inputs.q) this.car.steer(-0.2);
            if (this.inputs.d) this.car.steer(0.2);
            if (this.car != null && this.inputs.spaceDown) this.respawnCar();
            if (this.car != null) this.car.update();

            long end = System.nanoTime();
            long delay = (1000000000 / this.FPS) - (end - start);
            // System.out.println("DeltaTime : " + ((end - start)/1000) + "mms, for " + this.FPS + " fps");
            try {
                Thread.sleep(delay/1000000);
            } catch (Exception e) {}
        }
    }

    public void paint(Graphics g) {
        Graphics2D graph = (Graphics2D) g;
        graph.setColor(this.grassColor);
        graph.fillRect(0, 0, 1080, 720);
        track.paint(graph);
        if (this.car != null) this.car.paint(graph);
    }
}
