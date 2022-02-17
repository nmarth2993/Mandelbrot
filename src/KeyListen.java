
import java.awt.event.*;

public class KeyListen implements KeyListener {

    MandelbrotCore core;
    MouseHandler handler;

    public KeyListen(MandelbrotCore core, MouseHandler handler) {
        this.core = core;
        this.handler = handler;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        // ignore keypress if thread already working
        if (handler.isWorking()) {
            return;
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_S) {
            core.nextColorMode();
        } else if (e.getKeyCode() == KeyEvent.VK_O) {
            // toggle overlay
            core.setOverlay(!core.isOverlay());
        }
        // else if (e.getKeyCode() == KeyEvent.VK_R) {
        // synchronized (core) {
        // core.resetZoom();
        // }
        // }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

}
