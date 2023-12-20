import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputHandler implements KeyListener {

    public boolean spaceDown;
    public boolean shiftMod;
    public boolean mDown, lDown, kDown;
    public boolean z, q, s, d;

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

        int keyCode = e.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_Z:
                this.z = true;
                break;
            case KeyEvent.VK_Q:
                this.q = true;
                break;
            case KeyEvent.VK_S:
                this.s = true;
                break;
            case KeyEvent.VK_D:
                this.d = true;
                break;
            default:
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) this.spaceDown = false; 
        if (e.getKeyCode() == KeyEvent.VK_SHIFT) this.shiftMod = false;
        if (e.getKeyCode() == KeyEvent.VK_M) this.mDown = false;
        if (e.getKeyCode() == KeyEvent.VK_L) this.lDown = false;
        if (e.getKeyCode() == KeyEvent.VK_K) this.kDown = false;

        int keyCode = e.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_Z:
                this.z = false;
                break;
            case KeyEvent.VK_Q:
                this.q = false;
                break;
            case KeyEvent.VK_S:
                this.s = false;
                break;
            case KeyEvent.VK_D:
                this.d = false;
                break;
            default:
                break;
        }
    }
    
}
