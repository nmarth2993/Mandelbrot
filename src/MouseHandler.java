import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import javax.swing.event.MouseInputListener;

public class MouseHandler implements MouseInputListener {

    final static int WIDTH = 75;
    final static int HEIGHT = 75;

    Rectangle zRect;

    public MouseHandler() {
        reset();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) {
            System.out.println("right click zooms out");
            //zoom out by one factor about the current center point
            reset();
        }
        else if (zRect == null || zRect.contains(e.getPoint())) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                System.out.println("left click zooms in");
                //zoom in by one factor about the center of the rectangle (the point clicked)
                reset();
            }
        }
        else {
            setZRect(e.getPoint());
        }
    }

    public void reset() {
        zRect = new Rectangle();
    }

    public void setZRect(Point p) {
        zRect = new Rectangle(p.x - WIDTH / 2, p.y - HEIGHT / 2, WIDTH, HEIGHT);
    }

    public Rectangle getZRect() {
        return zRect;
    }

    @Override
    public void mousePressed(MouseEvent e) {

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

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}