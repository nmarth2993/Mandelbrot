import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import javax.swing.event.MouseInputListener;

//TODO: implement previous zoom levels

public class MouseHandler implements MouseInputListener {

    final static int WIDTH = (int) (MandelbrotCore.WIDTH / 5);
    final static int HEIGHT = (int) (MandelbrotCore.HEIGHT / 5);

    MandelbrotCore core;
    MandelbrotPanel panel;
    Rectangle zRect;
    // Stack<Zoom> previousZooms;
    boolean working;

    public MouseHandler(MandelbrotCore core, MandelbrotPanel panel) {
        working = false;
        this.core = core;
        this.panel = panel;
        // previousZooms = new Stack<Zoom>();
        //seed the previous zooms with the original zbox
        // previousZooms.add(new Zoom(core.xyStart(), core.xRange(), core.yRange()));
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
        if (e.getButton() == MouseEvent.BUTTON3) {
            working = true;
            // System.out.println("right click zooms out");
            //zoom out by one factor about the current center point
            // if (previousZooms.size() == 1) {
            //     working = false;
            //     return;
            //     //if already at lowest zoom, do nothing
            // }
            // setZBox(previousZooms.pop());
            resetZRect();
            panel.repaint();
            new Thread(() -> {
                // System.out.println("zoom thread fired");
                synchronized(core) {
                    // new MandelbrotCore(new ComplexCoordinate(-2, -1.5), 3, 3);
                    core.setXYStart(new ComplexCoordinate(-2, -1.5));
                    core.setXRange(3);
                    core.setYRange(3);
                    // System.out.println("re-calculating...");
                    core.calculatePoints(this);
                }
                System.out.println("done.");
                working = false;
            }).start();
        }
        else if (e.getButton() == MouseEvent.BUTTON1) {
            System.out.println("click at: (" + e.getX() + ", " + e.getY() + ")");
            if (getZRect().contains(e.getPoint())) {
                working = true;
                setZBox(new Zoom(zoomXY(e), zoomXRange(e), zoomYRange(e)));
                resetZRect();
                panel.repaint();
                new Thread(() -> {
                    synchronized(core) {
                        System.out.println("re-calculating...");
                        core.calculatePoints(this);
                    }
                    System.out.println("done");
                    working = false;
                }).start();
            }
            else {
                setZRect(e.getPoint());
                panel.repaint();
                // System.out.println("upper left: (" + getZRect().getX() + ", " + getZRect().getY() + ")");
                // System.out.println("lower left: (" + getZRect().getMinX() + ", " + getZRect().getMaxY() + ")");
                // System.out.println("cartesian: (" + core.realIncrement() * getZRect().getMinX() + ", " + core.imaginaryIncrement() * getZRect().getMaxY() + ")");
                // System.out.println("cartesian: (" + xPoint + ", " + yPoint + ")");

            }
        }
    }

    public ComplexCoordinate zoomXY(MouseEvent e) {
        double xPoint = core.xyStart().real() + ((getZRect().getMinX() * core.xRange()) / MandelbrotCore.WIDTH);
        double yPoint = core.xyStart().imaginary() + core.yRange() - ((getZRect().getMaxY() * core.yRange()) / MandelbrotCore.HEIGHT);
        return new ComplexCoordinate(xPoint, yPoint);
    }

    public double zoomXRange(MouseEvent e) {
        return core.realIncrement() * getZRect().getMaxX() - core.realIncrement() * getZRect().getMinX();
    }

    public double zoomYRange(MouseEvent e) {
        return core.imaginaryIncrement() * getZRect().getMaxY() - core.imaginaryIncrement() * getZRect().getMinY();
    }

    public void resetZRect() {
        zRect = new Rectangle(new Point(-WIDTH, -HEIGHT));
        //make rectangle off the screen
    }

    public void setZRect(Point p) {
        zRect = new Rectangle((int) (p.getX() - WIDTH / 2), (int) (p.getY() - HEIGHT / 2), WIDTH, HEIGHT);
    }

    public Rectangle getZRect() {
        return zRect;
    }

    public void setZBox(ComplexCoordinate z, double xR, double yR) {
        core.setXYStart(z);
        core.setXRange(xR);
        core.setYRange(yR);
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