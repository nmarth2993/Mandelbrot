import java.awt.Color;

public class ConvergenceTester {

    public static int miter(ComplexCoordinate z0, int max) {
        ComplexCoordinate z = z0;

        for (int i = 0; i < max; i++) {
            if (z.mod() > 2.0) {
                return i;
            }
            z = z.square().plus(z0);

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