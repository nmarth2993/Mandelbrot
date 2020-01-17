import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MandelbrotCore {

    final static double WIDTH = 600;
    final static double HEIGHT = 600;

    final static double DENSITY = 1; // density (decimal <= 1) where 1 == 100% density

    final int max = 255;

    ComplexCoordinate xyStart;
    List<ColoredComplexCoordinate> pointList;

    double xRange;
    double yRange;
    double realIncrement;
    double imaginaryIncrement;

    public MandelbrotCore(ComplexCoordinate xyStart, double xRange, double yRange) {
        this.xyStart = xyStart;
        this.xRange = xRange;
        this.yRange = yRange;
        // this.realIncrement = realIncrement;
        // this.imaginaryIncrement = imaginaryIncrement;
        pointList = Collections.synchronizedList(new ArrayList<ColoredComplexCoordinate>());
    }

    public void setXYStart(ComplexCoordinate xyS) {
        this.xyStart = xyS;
    }

    public void setXRange(double r) {
        this.xRange = r;
    }

    public void setYRange(double r) {
        this.yRange = r;
    }

    public ComplexCoordinate xyStart() {
        return xyStart;
    }

    public List<ColoredComplexCoordinate> getPointList() {
        return pointList;
    }

    /*
    public void calculatePoints() {

        // System.out.println("calc points");

        // TODO: disallow user input when first plotting the set

        synchronized (pointList) {
            // System.out.println("has lock");
            pointList = new ArrayList<ColoredComplexCoordinate>();
            //
            // System.out.println("points removed");
        }

        for (ComplexCoordinate x = new ComplexCoordinate(xyStart().real(), xyStart().imaginary()); nextPoint(
                x) != null; x = nextPoint(x)) {
            int iter = ConvergenceTester.miter(x, max);
            ColoredComplexCoordinate c = new ColoredComplexCoordinate(x, iter);
            synchronized (pointList) {
                if (c != null) {
                    pointList.add(c);
                }
            }
        }
        // TODO: thread this part so that the calculation is very fast :)
    }
    */

    public void calculatePointsThreaded() {
        synchronized (pointList) {
            pointList = Collections.synchronizedList(new ArrayList<ColoredComplexCoordinate>());
        }
        synchronized (pointList) {
            new Thread(() -> {
                for (ComplexCoordinate x = new ComplexCoordinate(xyStart().real(), xyStart().imaginary()); nextPoint(x)
                        .real() < (xyStart().real() + xRange()) / 2; x = nextPoint(x)) {
                    int iter = ConvergenceTester.miter(x, max);
                    if (x == null) {
                        System.out.println("x is null at point " + x);
                        System.out.println("on thread 1");
                        System.exit(1);
                    }
                    ColoredComplexCoordinate c = new ColoredComplexCoordinate(x, iter);
                    if (c != null && c.getZ() != null) {
                        pointList.add(c);
                    }
                }
                System.out.println("thread 1 done.");
            }).start();
        }

        synchronized (pointList) {
            new Thread(() -> {
                for (ComplexCoordinate x = new ComplexCoordinate(xyStart().real() + xRange() / 2,
                        xyStart().imaginary()); nextPoint(x) != null; x = nextPoint(x)) {
                    int iter = ConvergenceTester.miter(x, max);
                    if (x == null) {
                        System.out.println("x is null at point " + x);
                        System.out.println("on thread 2");
                        System.exit(1);
                    }
                    ColoredComplexCoordinate c = new ColoredComplexCoordinate(x, iter);
                    if (c != null && c.getZ() != null) {
                        pointList.add(c);
                    }
                }
                System.out.println("thread 2 done.");
            }).start();
        }
    }

    public ComplexCoordinate nextPoint(ComplexCoordinate z) {

        if (z.real() + realIncrement() > xyStart().real() + xRange()) {
            return null;
            // the next point is not in the scope
        } else if (z.imaginary() + imaginaryIncrement() > xyStart().imaginary() + yRange()) {
            return new ComplexCoordinate(z.real() + realIncrement(), xyStart().imaginary());
            // the next point is on the next line, so shift accordingly
        } else {
            return new ComplexCoordinate(z.real(), z.imaginary() + imaginaryIncrement());
            // the next point is on the same line, no need to shift
        }
    }

    public double xRange() {
        return xRange;
    }

    public double yRange() {
        return yRange;
    }

    public double realIncrement() {
        return (xRange() / WIDTH) * (1 / DENSITY);
        // reciprocal of density constant
        // because half density only does every 2 points
    }

    public double imaginaryIncrement() {
        return (yRange() / HEIGHT) * (1 / DENSITY);
        // reciprocal of density constant
        // because half density only does every 2 points
    }

    public void setRealIncrement(double inc) {
        realIncrement = inc;
    }

    public void setImaginaryIncrement(double inc) {
        imaginaryIncrement = inc;
    }

}