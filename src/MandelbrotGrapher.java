import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class MandelbrotGrapher {

    MandelbrotCore core;
    MandelbrotPanel panel;

    JFrame frame;

    public MandelbrotGrapher() {
        frame = new JFrame();
        core = new MandelbrotCore(new ComplexCoordinate(-.5, -.5), 1.5, 1.5);
        panel = new MandelbrotPanel(core);

        frame.setContentPane(panel);

        frame.setPreferredSize(new Dimension((int) MandelbrotCore.WIDTH, (int) MandelbrotCore.HEIGHT));
        frame.setVisible(true);
        frame.pack();

        new Thread(() -> { //animation thread
            for(;;) {
                panel.repaint();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        core.calculatePoints();
    }

    public MandelbrotCore getCore() {
        return core;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MandelbrotGrapher();
            System.out.println("done.");
        });
    }
}