import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

public class ViewPanel extends JPanel implements Runnable {
    
    private Color grassColor = new Color(0, 150, 0);
    private Track track;
    private InputHandler inputs;
    private Thread gameThread;
    private boolean gameRunning = true;
    private Point currentPoint = null;
    private float currentAngle = 0;
    private float lastAngle = 0;
    private int currentThickness = 10;
    private int lastThickness = 0;
    private int trackPieceId = 0;

    ViewPanel(int width, int height) {
        this.setPreferredSize(new Dimension(width, height));
        this.track = new Track();

        this.inputs = new InputHandler();
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

        this.addMouseMotionListener(new MouseMotionListener() {

            @Override
            public void mouseDragged(MouseEvent e) {
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                if (currentPoint == null) return;
                int lastInnerX = currentPoint.x + Math.cos(lastAngle)
                track.innerLines.set(trackPieceId, new Line(currentPoint.x, currentPoint.y, e.getX(), e.getY()));
            }
            
        });

        this.gameThread = new Thread(this);
        this.gameThread.start();
    }

    private void addTrackPiece(int x, int y, float a) {
        if (this.currentPoint == null) return;
        System.out.println("track");
        this.trackPieceId++;
    }

    @Override
    public void run() {
        while (this.gameRunning) {

            if (this.inputs.spaceDown) this.track.close();
            this.repaint();

        }
    }

    public void paint(Graphics g) {
        Graphics2D graph = (Graphics2D) g;
        graph.setColor(this.grassColor);
        graph.fillRect(0, 0, 1080, 720);
        track.paint(graph);

        if (!this.track.closed) {
            graph.setColor(Color.RED);
            graph.fillRect(10, 10, 10, 10);
        }
    }
}
