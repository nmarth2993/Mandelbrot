import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class JuliaGrapher {

    private JuliaCore core;
    private JuliaPanel panel;

    private JFrame frame;

    public JuliaGrapher() {
        frame = new JFrame("Julia Set");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        core = new JuliaCore(new ComplexCoordinate(-2, -1.5), 3, 3);

        frame.addKeyListener(new KeyListen(core));

        panel = new JuliaPanel(core);

        frame.setContentPane(panel);

        // set size of panel:
        panel.setPreferredSize(new Dimension((int) JuliaCore.WIDTH, (int) JuliaCore.HEIGHT));
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

        // plot the set
        new Thread(() -> {
            synchronized (core) {
                core.calculatePoints(panel.getMouseHandler());
            }
        }).start();
    }

    public JuliaCore getCore() {
        return core;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new JuliaGrapher();
        });
    }
}