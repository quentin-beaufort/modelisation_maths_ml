import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class ViewPanel extends JPanel {
    
    private Color grassColor = new Color(0, 150, 0);
    private Track track;

    ViewPanel(int width, int height) {
        this.setPreferredSize(new Dimension(width, height));
        this.track = new Track();

        

    }

    public void paint(Graphics g) {
        Graphics2D graph = (Graphics2D) g;
        graph.setColor(this.grassColor);
        graph.fillRect(0, 0, 1080, 720);
        track.paint(graph);
    }
}
