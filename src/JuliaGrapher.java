import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

public class JuliaGrapher {

    public static final String ACTION_SCREENSHOT = "screenshot";
    public static final String PATH = "C:/Users/nTandem/Desktop/fractal images/";

    private JuliaCore core;
    private JuliaPanel panel;

    private JFrame frame;

    JMenuBar menuBar;
    JMenu fileMenu;
    JMenuItem screenshot;
    FileMenuListener fileMenuListener;

    public JuliaGrapher() {
        frame = new JFrame("Julia Set");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // create menu
        menuBar = new JMenuBar();

        fileMenu = new JMenu("File");
        fileMenu.setMnemonic('f');

        screenshot = new JMenuItem("Screenshot");
        screenshot.setMnemonic('s');
        screenshot.setAccelerator(KeyStroke.getKeyStroke("ctrl S"));
        screenshot.addActionListener(new FileMenuListener());
        screenshot.setActionCommand(ACTION_SCREENSHOT);

        fileMenu.add(screenshot);

        menuBar.add(fileMenu);

        frame.setJMenuBar(menuBar);

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

    public class FileMenuListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals(ACTION_SCREENSHOT)) {
                // return;
                // System.out.println("screenshot of panel at: " + panel);
                BufferedImage image = new BufferedImage(panel.getWidth() - 2, panel.getHeight() - 2,
                        BufferedImage.TYPE_INT_RGB);
                Graphics2D g2d = (Graphics2D) image.getGraphics();
                panel.print(g2d);
                File outfile = new File(genFilename());
                try {
                    outfile.createNewFile();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                try {
                    ImageIO.write(image, "png", outfile);
                    System.out.println("wrote screenshot to " + outfile.getAbsolutePath());
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
        }

        public String genFilename() {
            return PATH + "julia-" + (System.currentTimeMillis() / 1000) + colModeString(core.colorMode()) + ".png";
        }

        public String colModeString(int colorMode) {
            if (colorMode == JuliaCore.CMODE_BLACK_WHITE) {
                return "_BW";
            } else if (colorMode == JuliaCore.CMODE_INVERT) {
                return "_INV";
            } else if (colorMode == JuliaCore.CMODE_RGB) {
                return "_RGB";
            }
            return "";
        }
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