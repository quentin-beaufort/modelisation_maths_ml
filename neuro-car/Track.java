import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.util.ArrayList;

public class Track {
 
    private Color trackColor = new Color(50, 50, 50);
    private Color grassColor = new Color(0, 150, 0);
    private ArrayList<Line> innerLines, outerLines;
    private Polygon outerTrack, innerTrack;

    Track() {
        this.innerLines = new ArrayList<Line>(50);
        this.outerLines = new ArrayList<Line>(50);
        
        this.outerLines.add(new Line(540, 100, 560, 100));
        this.addOuterPoint(580, 102);
        this.addOuterPoint(600, 105);
        this.addOuterPoint(630, 110);
        this.addOuterPoint(950, 150);
        this.addOuterPoint(955, 155);
        this.addOuterPoint(958, 160);
        this.addOuterPoint(960, 165);
        
        this.innerTrack = new Polygon();
        for (Line l : this.innerLines) {
            this.innerTrack.addPoint(l.a.x, l.a.y);
            this.innerTrack.addPoint(l.b.x, l.b.y);
        }
        
        this.outerTrack = new Polygon();
        for (Line l : this.outerLines) {
            this.outerTrack.addPoint(l.a.x, l.a.y);
            this.outerTrack.addPoint(l.b.x, l.b.y);
        }
    }

    private void addInnerPoint(int x, int y) {
        Point lasPoint = this.innerLines.get(this.innerLines.size()-1).b;
        this.innerLines.add(new Line(lasPoint.x, lasPoint.y, x, y));
    }
    
    private void addOuterPoint(int x, int y) {
        Point lasPoint = this.outerLines.get(this.outerLines.size()-1).b;
        this.outerLines.add(new Line(lasPoint.x, lasPoint.y, x, y));
    }

    public void paint(Graphics2D graph) {
        graph.setColor(trackColor);
        graph.fill(this.outerTrack);
        graph.setColor(grassColor);
        graph.fill(this.innerTrack);
    }

}
