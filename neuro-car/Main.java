import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class Main {
    public static void main(String[] args) {
        
        JFrame window = new JFrame();
        window.setResizable(false);
        window.setTitle("Neuro Car");
        window.setPreferredSize(new Dimension(1080, 720));
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        window.setLocation((int)(screenSize.getWidth() - 1080) / 2, (int)(screenSize.getHeight() - 720) / 2);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        
        ViewPanel vp = new ViewPanel(1080, 720);
        window.add(vp);

        window.pack();
        window.setVisible(true);

    }
}