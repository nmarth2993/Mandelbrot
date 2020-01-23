import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class MandelbrotGrapher {

    private MandelbrotCore core;
    private MandelbrotPanel panel;

    private JFrame frame;

    public MandelbrotGrapher() {
        frame = new JFrame("Mandelbrot");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        core = new MandelbrotCore(new ComplexCoordinate(-2, -1.5), 3, 3);

        panel = new MandelbrotPanel(core);

        frame.setContentPane(panel);

        // set size of panel:
        panel.setPreferredSize(new Dimension((int) MandelbrotCore.WIDTH, (int) MandelbrotCore.HEIGHT));
        frame.setVisible(true);
        frame.pack();

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

        //plot the set
        new Thread(() -> {
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