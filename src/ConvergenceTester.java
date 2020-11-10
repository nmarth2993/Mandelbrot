import java.awt.Color;

public class ConvergenceTester {

    public final static double PHI = 1.6180339887498948482045868343656381177203091798057628621354486227;

    public static int miter(ComplexCoordinate z0, int max) {
        ComplexCoordinate z = z0;
        // z0 = new ComplexCoordinate(-.8, .156);
        // z0 = new ComplexCoordinate(.285, .01);
        // z0 = new ComplexCoordinate(-.7269, .1889);
        // z0 = new ComplexCoordinate(0, -.8);
        // z0 = new ComplexCoordinate(PHI - 2, PHI - 1);
        // z0 = new ComplexCoordinate(.6, .55);
        // z0 = new ComplexCoordinate(.8, .6);
        // z0 = new ComplexCoordinate(.3, .6);
        // z0 = new ComplexCoordinate(-1, 0);
        // z0 = new ComplexCoordinate(-2, 0);
        // z0 = new ComplexCoordinate(.37, .1);
        // z0 = new ComplexCoordinate(.355, .355);

        // z0 = new ComplexCoordinate(.355534, -.337292);
        z0 = new ComplexCoordinate(-0.202420806884766, 0.39527333577474);

        for (int i = 0; i < max; i++) {
            if (z.mod() > 2.0) {
                return i;
            }
            z = z.power(2).plus(z0);
        }
        return max;
    }

    public static Color miterCol(ComplexCoordinate z0, int max) {
        double smoothcolor = 0;
        ComplexCoordinate z = z0;
        for (int i = 0; i < max; i++) {
            if (z.mod() > 2.0) {
                smoothcolor = i + 1 - Math.log(Math.log(z.mod())) / Math.log(2);
                Color c = new Color(Color.HSBtoRGB(0.95f + 10 * (float) smoothcolor, 0.6f, 1.0f));

                // System.out.println("smoothcolor val: " + smoothcolor);
                // System.out.println("RGB bits: " + c.getRGB());
                // System.out.println("as RGB: " + c.getRed() + ", " + c.getGreen() + ", " +
                // c.getBlue());
                return c;
            }
            z = z.square().plus(z0);
            smoothcolor = i + 1 - Math.log(Math.log(z.mod())) / Math.log(2);
        }
        Color c = new Color(Color.HSBtoRGB(0.95f + 10 * (float) smoothcolor, 0.6f, 1.0f));
        return c;
    }
}