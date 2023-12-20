import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputHandler implements KeyListener {

    public boolean spaceDown;
    public boolean shiftMod;
    public boolean mDown, lDown, kDown;

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) this.spaceDown = true; 
        if (e.getKeyCode() == KeyEvent.VK_SHIFT) this.shiftMod = true;
        if (e.getKeyCode() == KeyEvent.VK_M) this.mDown = true;
        if (e.getKeyCode() == KeyEvent.VK_L) this.lDown = true;
        if (e.getKeyCode() == KeyEvent.VK_K) this.kDown = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) this.spaceDown = false; 
        if (e.getKeyCode() == KeyEvent.VK_SHIFT) this.shiftMod = false;
        if (e.getKeyCode() == KeyEvent.VK_M) this.mDown = false;
        if (e.getKeyCode() == KeyEvent.VK_L) this.lDown = false;
        if (e.getKeyCode() == KeyEvent.VK_K) this.kDown = false;
    }
    
}
