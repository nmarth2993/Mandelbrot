
import java.awt.event.*;

public class KeyListen implements KeyListener {

    MandelbrotCore core;

    public KeyListen(MandelbrotCore core) {
        this.core = core;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_S) {
            core.nextColorMode();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

}
