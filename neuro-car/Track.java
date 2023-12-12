import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

public class Track {
 
    private Color trackColor = new Color(50, 50, 50);
    private Color grassColor = new Color(0, 150, 0);
    private ArrayList<Line> innerLines, outerLines;
    private Polygon outerTrack, innerTrack;
    public boolean closed;

    Track() {
        this.innerLines = new ArrayList<Line>(50);
        this.outerLines = new ArrayList<Line>(50);
    
        this.loadTrack();
    }

    private void loadTrack() {
        try (BufferedReader trackDataReader = new BufferedReader(new FileReader("level.track"));) {

            String outerLineString = trackDataReader.readLine();
            int scIndex = 0;
            int counter = 0;
            int x = 0, y = 0, w = 0, v = 0;
            String pointData = outerLineString.substring(scIndex, outerLineString.indexOf(";", scIndex+1));
            while (!pointData.equals("") && scIndex != -1) {
                if (counter % 2 == 0) {
                    x = Integer.parseInt(pointData.substring(0, pointData.indexOf(',')));
                    y = Integer.parseInt(pointData.substring(pointData.indexOf(',')+1, pointData.length()));
                    scIndex = outerLineString.indexOf(";", scIndex+1);
                } else {
                    w = Integer.parseInt(pointData.substring(0, pointData.indexOf(',')));
                    v = Integer.parseInt(pointData.substring(pointData.indexOf(',')+1, pointData.length()));
                    if (x != w || y != v) this.outerLines.add(new Line(x, y, w, v));
                }
                counter++;
                if (outerLineString.indexOf(";", scIndex+1) != -1) pointData = outerLineString.substring(scIndex+1, outerLineString.indexOf(";", scIndex+1));
            }

            String innerLineString = trackDataReader.readLine();
            scIndex = 0;
            counter = 0;
            pointData = innerLineString.substring(scIndex, innerLineString.indexOf(";", scIndex+1));
            while (!pointData.equals("") && scIndex != -1) {
                if (counter % 2 == 0) {
                    x = Integer.parseInt(pointData.substring(0, pointData.indexOf(',')));
                    y = Integer.parseInt(pointData.substring(pointData.indexOf(',')+1, pointData.length()));
                    scIndex = innerLineString.indexOf(";", scIndex+1);
                } else {
                    w = Integer.parseInt(pointData.substring(0, pointData.indexOf(',')));
                    v = Integer.parseInt(pointData.substring(pointData.indexOf(',')+1, pointData.length()));
                    if (x != w || y != v) this.innerLines.add(new Line(x, y, w, v));  
                }
                counter++;
                if (innerLineString.indexOf(";", scIndex+1) != -1) pointData = innerLineString.substring(scIndex+1, innerLineString.indexOf(";", scIndex+1));
            }
            this.closed = true;
        } catch (FileNotFoundException fnfe) {
            System.out.println("no Track, starting mouse track creation mode");
        } catch (Exception e) {
            e.printStackTrace();
        }

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

    private void saveTrack() {
        try (BufferedWriter trackDataWriter = new BufferedWriter(new FileWriter("level.track"))) {

            String outerLineString = this.outerLines.get(0).a.x + "," + this.outerLines.get(0).a.y + ";";
            for (Line l : this.outerLines) {
                outerLineString += l.b.x + "," + l.b.y + ";";
            }
            outerLineString += "\n";
            trackDataWriter.write(outerLineString);

            String innerLineString = this.innerLines.get(0).a.x + "," + this.innerLines.get(0).a.y + ";";
            for (Line l : this.innerLines) {
                innerLineString += l.b.x + "," + l.b.y + ";";
            }
            trackDataWriter.write(innerLineString);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void createTrack() {
        this.closed = false;
    }

    public void paint(Graphics2D graph) {
        if (this.closed) {
            graph.setColor(trackColor);
            graph.fill(this.outerTrack);
            graph.setColor(grassColor);
            graph.fill(this.innerTrack);
        } else {
            graph.setColor(Color.BLACK);
            for(Line l : this.innerLines) {
                graph.drawLine(l.a.x, l.a.y, l.b.x, l.b.y);
            }
            for(Line l : this.outerLines) {
                graph.drawLine(l.a.x, l.a.y, l.b.x, l.b.y);
            }
        }
    }
}
