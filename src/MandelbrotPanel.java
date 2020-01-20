import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class MandelbrotPanel extends JPanel {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    MandelbrotCore core;
    MouseHandler handler;

    public MandelbrotPanel(MandelbrotCore core) {
        this.core = core;
        handler = new MouseHandler(core, this);
        addMouseListener(handler);
    }

    public MouseHandler getMouseHandler() {
        return handler;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        synchronized (core.getPointList()) { //must use synchronized list in a synch block
            if (!core.getPointList().isEmpty()) {
                for (ColoredComplexCoordinate x : core.getPointList()) {
                    int xPixel = (int) ((x.getZ().real() - core.xyStart().real())
                            * (MandelbrotCore.WIDTH / core.xRange()));
                    int yPixel = (int) ((core.xyStart().imaginary() + core.yRange() - x.getZ().imaginary())
                            * (MandelbrotCore.HEIGHT / core.yRange()));

                    g2d.setColor(x.getC());
                    g.drawLine(xPixel, yPixel, xPixel, yPixel);
                }
            }

            //drawing axes:
            if (core.xyStart().real() < 0 && core.xyStart().real() + core.xRange() > 0) {
                int yAxis = (int) (-core.xyStart().real() * (MandelbrotCore.WIDTH / core.xRange()));
					g2d.setColor(Color.GREEN);
                g.drawLine(yAxis, 0, yAxis, getHeight());
            }
            if (core.xyStart().imaginary() < 0 && core.xyStart().imaginary() + core.yRange() > 0) {
                int xAxis = (int) ((core.xyStart().imaginary() + core.yRange()) * (MandelbrotCore.HEIGHT / core.yRange()));
					g2d.setColor(Color.GREEN);
                g.drawLine(0, xAxis, getWidth(), xAxis);
            }

            //printing start and end points
            if (!core.getPointList().isEmpty()) {
                ColoredComplexCoordinate start = core.getPointList().get(0);
                ComplexCoordinate end = new ComplexCoordinate(core.xyStart().real() + core.xRange(), core.xyStart().imaginary() + core.yRange());
                g2d.setColor(Color.RED);
                g2d.drawString("start: " + start.getZ() + "  end: " + end.toString(), 0, 10);
            }
            

        }
        //drawing user zoom box
        g2d.setColor(Color.YELLOW);
        g2d.setStroke(new BasicStroke(3f));
        g2d.draw(handler.getZRect());
    }
}