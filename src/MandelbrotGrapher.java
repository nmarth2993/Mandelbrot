import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class MandelbrotGrapher {

    MandelbrotCore core;
    MandelbrotPanel panel;

    JFrame frame;

    public MandelbrotGrapher() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        core = new MandelbrotCore(new ComplexCoordinate(-2.2, -1), 2, 2);
        // core = new MandelbrotCore(new ComplexCoordinate(-1.5, -.5), 1, 1);
        panel = new MandelbrotPanel(core);

        frame.setContentPane(panel);

        panel.setPreferredSize(new Dimension((int) MandelbrotCore.WIDTH, (int) MandelbrotCore.HEIGHT));
        frame.setVisible(true);
        frame.pack();

        //TODO: make this thread better (not infinite loop)
        new Thread(() -> { //animation thread
            for(;;) {
                panel.repaint();
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        //TODO: fix this thread so that it plays nice with the zooming function
        //as of now, this thread will have the lock...
        new Thread(() -> { //point calculation needs to be in a thread
            synchronized(core) {
                core.calculatePoints();
            }
        }).start();

        //adding the points to an arrayList must be in a thread as well possibly
        //because the list is synchronized elsewhere, I think the main thread will hold it
        //until done if it's not in a thread
    }

    public MandelbrotCore getCore() {
        return core;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // JOptionPane.showMessageDialog(null, "Note that the set is being calculated at lower density for performace");
            new MandelbrotGrapher();
            System.out.println("done.");
        });
    }
}