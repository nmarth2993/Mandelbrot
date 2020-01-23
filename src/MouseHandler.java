import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.Stack;

import javax.swing.event.MouseInputListener;

public class MouseHandler implements MouseInputListener {

    //width and height of rectangle for zooming
    //a higher divisor will cause a more agressive zoom
    final static int WIDTH = (int) (MandelbrotCore.WIDTH / 10);
    final static int HEIGHT = (int) (MandelbrotCore.HEIGHT / 10);

    private MandelbrotCore core;
    private MandelbrotPanel panel;
    private Rectangle zRect;
    private Stack<Zoom> previousZooms;
    private boolean working; //used to disable user input when plotting

    public MouseHandler(MandelbrotCore core, MandelbrotPanel panel) {
        working = false;
        this.core = core;
        this.panel = panel;
        previousZooms = new Stack<Zoom>();
        previousZooms.push(new Zoom(core.xyStart(), core.xRange(), core.yRange()));
        resetZRect();
    }

    public void setWorking(boolean working) {
        this.working = working;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (working) {
            return;
        }
        if (e.getButton() == MouseEvent.BUTTON3) { //right click zooms out
            working = true; //ignore user input until plotted

            new Thread(() -> {
                synchronized (core) {
                    if (previousZooms.size() > 1) {
                        previousZooms.pop();
                        //removes current zoom level to make previous zoom top of stack
                    }
                    setZBox(previousZooms.peek());

                    resetZRect();
                    panel.repaint();
                    core.calculatePoints(this);
                }
                working = false; //allow user input again
            }).start();
        } else if (e.getButton() == MouseEvent.BUTTON1) {
            if (getZRect().contains(e.getPoint())) {
                working = true; //ignore user input until plotted
                
                new Thread(() -> {
                    synchronized (core) {
                        Zoom z = new Zoom(zoomXY(e), zoomXRange(e), zoomYRange(e));
                        previousZooms.push(z);

                        setZBox(z);
                        resetZRect();
                        panel.repaint();
                        core.calculatePoints(this);
                    }
                    working = false; //allow user input again
                }).start();
            } else {
                setZRect(e.getPoint());
                panel.repaint();
            }
        }
    }

    //zoomXY: finds the cartesian xyStart given an x/y pixel
    public ComplexCoordinate zoomXY(MouseEvent e) {
        double xPoint = core.xyStart().real() + ((getZRect().getMinX() * core.xRange()) / MandelbrotCore.WIDTH);
        double yPoint = core.xyStart().imaginary() + core.yRange()
                - ((getZRect().getMaxY() * core.yRange()) / MandelbrotCore.HEIGHT);
        return new ComplexCoordinate(xPoint, yPoint);
    }

    public double zoomXRange(MouseEvent e) {
        return core.realIncrement() * getZRect().getMaxX() - core.realIncrement() * getZRect().getMinX();
    }

    public double zoomYRange(MouseEvent e) {
        return core.imaginaryIncrement() * getZRect().getMaxY() - core.imaginaryIncrement() * getZRect().getMinY();
    }

    public void resetZRect() {
        zRect = new Rectangle();
    }

    public void setZRect(Point p) {
        //sets the rectangle centered around the click
        zRect = new Rectangle((int) (p.getX() - WIDTH / 2), (int) (p.getY() - HEIGHT / 2), WIDTH, HEIGHT);
    }

    public Rectangle getZRect() {
        return zRect;
    }

    public void setZBox(Zoom z) {
        core.setXYStart(z.getXYStart());
        core.setXRange(z.getXRange());
        core.setYRange(z.getYRange());
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

    //Zoom is just a wrapper class to package the three attributes into
    //one object for ease of use in a stack
    class Zoom {
        ComplexCoordinate xyStart;
        double xRange;
        double yRange;

        public Zoom(ComplexCoordinate z, double xR, double yR) {
            xyStart = z;
            xRange = xR;
            yRange = yR;
        }

        public ComplexCoordinate getXYStart() {
            return xyStart;
        }

        public double getXRange() {
            return xRange;
        }

        public double getYRange() {
            return yRange;
        }
    }
}