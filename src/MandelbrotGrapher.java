import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class MandelbrotGrapher {

    MandelbrotCore core;
    MandelbrotPanel panel;

    JFrame frame;

    public MandelbrotGrapher() {
        frame = new JFrame("Mandelbrot");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        core = new MandelbrotCore(new ComplexCoordinate(-2, -1.5), 3, 3); // default

        // core = new MandelbrotCore(new ComplexCoordinate(-1.5, -.5), 1, 1);
        // core = new MandelbrotCore(new ComplexCoordinate(-2.2, -1), 3, 2);
        // core = new MandelbrotCore(new ComplexCoordinate(0, 0), 0.5, 0.5);
        // core = new MandelbrotCore(new ComplexCoordinate(-1.5, -.5), 1, 1);

        // core = new MandelbrotCore(new ComplexCoordinate(0.006666666666666667,
        // 0.8033333333333333), .4, .4);

        panel = new MandelbrotPanel(core);

        frame.setContentPane(panel);

        // set size of panel:
        panel.setPreferredSize(new Dimension((int) MandelbrotCore.WIDTH, (int) MandelbrotCore.HEIGHT));
        frame.setVisible(true);
        frame.pack();

        // TODO, nice to have: make this thread better (not infinite loop)
        new Thread(() -> { // animation thread
            for (;;) {
                panel.repaint();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(() -> { // point calculation needs to be in a thread
            synchronized (core) {
                core.calculatePoints(panel.getMouseHandler());
            }
        }).start();
    }

    public MandelbrotCore getCore() {
        return core;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MandelbrotGrapher();
        });
    }
}