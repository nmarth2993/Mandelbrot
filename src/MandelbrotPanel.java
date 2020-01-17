import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class MandelbrotPanel extends JPanel {
    MandelbrotCore core;
    MouseHandler handler;

    public MandelbrotPanel(MandelbrotCore core) {
        this.core = core;
        handler = new MouseHandler(core);
        addMouseListener(handler);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        // ArrayList<ColoredComplexCoordinate> drawList = new ArrayList<ColoredComplexCoordinate>(core.getPointList());
        synchronized (core.getPointList()) {
            if (core.getPointList().size() != 0) {
                for (ColoredComplexCoordinate x : core.getPointList()) {
                    // System.out.println("accessing x: \n" + x);
                    if (x == null) {
                        System.out.println("what the fuck");
                        System.exit(1);
                    }
                    int xPixel = (int) ((x.getZ().real() - core.xyStart().real())
                            * (MandelbrotCore.WIDTH / core.xRange()));
                    int yPixel = (int) ((core.xyStart().imaginary() + core.yRange() - x.getZ().imaginary())
                            * (MandelbrotCore.HEIGHT / core.yRange()));

                    g2d.setColor(x.getC());
                    // g.drawLine(xPixel, yPixel, xPixel, yPixel);

                    g2d.fillRect(xPixel, yPixel, 2, 2);
                }
            }
        }
        g2d.setColor(Color.YELLOW);
        g2d.setStroke(new BasicStroke(3f));
        g2d.draw(handler.getZRect());
        // System.out.println("x: " + handler.getZRect().x + " y: " +
        // handler.getZRect().y);
    }
}