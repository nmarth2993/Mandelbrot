public class ConvergenceTester {

    public static int miter(ComplexCoordinate z0, int max) {
        ComplexCoordinate z = z0;

        for (int i = 0; i < max; i++) {
            if (z.mod() > 2.0) {
                // System.out.println("mod > 2");
                return i;
            }
            // System.out.println(z0 + " " + z.mod());
            // System.out.println("z: " + z);
            // System.out.println("z.square(): " + z.square());
            // System.out.println("z0: " + z0);
            // System.out.println("z.n+1: " + z.square().plus(z0));
            z = z.square().plus(z0);

            // System.out.println(z);

            // return i + 1 - Math.log(Math.log(z.mod())) / Math.log(2);
        }
        return max;
    }
}