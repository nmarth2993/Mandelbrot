import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class MandelbrotPanel extends JPanel {
    MandelbrotCore core;

    public MandelbrotPanel(MandelbrotCore core) {
        this.core = core;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        synchronized (core.getPointList()) {
            for (ColoredComplexCoordinate x : core.getPointList()) {

                int xPixel = (int) ((x.getZ().real() - core.xyStart().real()) * (MandelbrotCore.WIDTH / core.xRange()));
                int yPixel = (int) ((core.xyStart().imaginary() + core.yRange() - x.getZ().imaginary())
                        * (MandelbrotCore.HEIGHT / core.yRange()));

                g2d.setColor(x.getC());
                // g.drawLine(xPixel, yPixel, xPixel, yPixel);

                g2d.fillRect(xPixel, yPixel, 2, 2);
            }
        }
    }
}