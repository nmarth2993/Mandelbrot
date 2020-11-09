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

    final static boolean drawAxes = false;
    final static boolean diag = false;

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

        int colorMode = core.colorMode();

        synchronized (core.getPointList()) {
            if (!core.getPointList().isEmpty()) {
                Color drawColor = null;
                int maxColorValue = 0;
                // scope colorMode outside of for loop so that we don't
                // have an O(n^2) loop pointlessly and then spend 30 minutes
                // trying to figure out how there's deadlock when there isn't
                if (colorMode == MandelbrotCore.CMODE_INVERT) {
                    maxColorValue = core.maxColorValue();
                }
                for (ColoredComplex x : core.getPointList()) {
                    int xPixel = (int) ((x.getZ().real() - core.xyStart().real())
                            * (MandelbrotCore.WIDTH / core.xRange()));
                    int yPixel = (int) ((core.xyStart().imaginary() + core.yRange() - x.getZ().imaginary())
                            * (MandelbrotCore.HEIGHT / core.yRange()));

                    if (colorMode == MandelbrotCore.CMODE_BLACK_WHITE) {
                        drawColor = x.getColor();
                    } else if (colorMode == MandelbrotCore.CMODE_INVERT) {
                        drawColor = new Color(maxColorValue - x.getColor().getRed(),
                                maxColorValue - x.getColor().getRed(), maxColorValue - x.getColor().getRed());
                    } else if (colorMode == MandelbrotCore.CMODE_RGB) {
                        if (x.getColor().getRed() < 85) {
                            drawColor = new Color(255 - x.getColor().getRed(), 0, 0);
                        } else if (x.getColor().getRed() < 170) {
                            drawColor = new Color(0, 255 - x.getColor().getRed(), 0);
                        } else {
                            drawColor = new Color(0, 0, 255 - x.getColor().getRed());
                        }
                    }

                    g2d.setColor(drawColor);
                    g2d.drawLine(xPixel, yPixel, xPixel, yPixel);
                }
            }

            // drawing axes:
            if (drawAxes) {
                if (core.xyStart().real() < 0 && core.xyStart().real() + core.xRange() > 0) {
                    int yAxis = (int) (-core.xyStart().real() * (MandelbrotCore.WIDTH / core.xRange()));
                    g2d.setColor(Color.GREEN);
                    g.drawLine(yAxis, 0, yAxis, getHeight());
                }
                if (core.xyStart().imaginary() < 0 && core.xyStart().imaginary() + core.yRange() > 0) {
                    int xAxis = (int) ((core.xyStart().imaginary() + core.yRange())
                            * (MandelbrotCore.HEIGHT / core.yRange()));
                    g2d.setColor(Color.GREEN);
                    g.drawLine(0, xAxis, getWidth(), xAxis);
                }
            }

            // printing start and end points
            if (diag) {
                if (!core.getPointList().isEmpty()) {
                    ColoredComplex start = core.getPointList().get(0);
                    ComplexCoordinate end = new ComplexCoordinate(core.xyStart().real() + core.xRange(),
                            core.xyStart().imaginary() + core.yRange());
                    g2d.setColor(Color.RED);
                    g2d.drawString("start: " + start.getZ() + " end: " + end.toString(), 0, 10);
                }
            }
        }

        // the code below is previous code:
        // synchronized (core.getPointList()) {
        // if (!core.getPointList().isEmpty()) {
        // for (ColoredComplex x : core.getPointList()) {
        // int xPixel = (int) ((x.getZ().real() - core.xyStart().real())
        // * (MandelbrotCore.WIDTH / core.xRange()));
        // int yPixel = (int) ((core.xyStart().imaginary() + core.yRange() -
        // x.getZ().imaginary())
        // * (MandelbrotCore.HEIGHT / core.yRange()));

        // Color rgbcol;
        // if (x.getColor().getRed() < 85) {
        // // draw red
        // // rgbcol = Color.RED;
        // rgbcol = new Color(255 - x.getColor().getRed(), 0, 0);
        // } else if (x.getColor().getRed() < 170) {
        // // draw green
        // // rgbcol = Color.GREEN;
        // rgbcol = new Color(0, 255 - x.getColor().getRed(), 0);
        // } else {
        // // draw blue
        // // rgbcol = Color.BLUE;
        // rgbcol = new Color(0, 0, 255 - x.getColor().getRed());
        // }

        // g2d.setColor(rgbcol);
        // g.drawLine(xPixel, yPixel, xPixel, yPixel);
        // }
        // }
        // }

        // drawing user zoom box
        g2d.setColor(Color.YELLOW);
        g2d.setStroke(new BasicStroke(3f));
        g2d.draw(handler.getZRect());
    }
}