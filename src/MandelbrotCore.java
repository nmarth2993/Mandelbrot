import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MandelbrotCore {

    //TODO: implement BigDecimal class to fix double precision errors:

    public final static double WIDTH = 600; //resolution of the image
    public final static double HEIGHT = 600;

    public final static double DENSITY = 1; // density (decimal <= 1) where 1 == 100% density

    public final int max = 255; //max iterations to test if a point is in the set

    private ComplexCoordinate xyStart; //bottom leftmost point in the graph
    private List<ColoredComplex> pointList; //list of points to be plotted, includes all points in the field

    private double xRange;
    private double yRange;

    public MandelbrotCore(ComplexCoordinate xyStart, double xRange, double yRange) {
        this.xyStart = xyStart;
        this.xRange = xRange;
        this.yRange = yRange;
        pointList = Collections.synchronizedList(new ArrayList<ColoredComplex>());
        //using a synchronized list to allow adding points from multiple threads
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

    public List<ColoredComplex> getPointList() {
        return pointList;
    }

    public void calculatePoints(MouseHandler h) {
        long startTime = System.currentTimeMillis();
        h.setWorking(true); //disable user input while calculating
        synchronized (pointList) {
            pointList = Collections.synchronizedList(new ArrayList<ColoredComplex>());
            //clear any previous points from the list
            //note: this is faster than Collections.removeAll() [O(1) vs O(n)]
        }
        Thread t1; //scope thread outside of synchronized block
        Thread t2; //scope thread outside of synchronized block
        synchronized (pointList) { //must use synch block for synchronizedList
            t1 = new Thread(() -> {
                //iterate over half of the field
                for (ComplexCoordinate x = new ComplexCoordinate(xyStart().real(), xyStart().imaginary()); nextPoint(x)
                        .real() < (xyStart().real() + xRange() / 2); x = nextPoint(x)) {
                    int iter = ConvergenceTester.miter(x, max);
                    ColoredComplex c = new ColoredComplex(x, iter);
                    // ColoredComplex c = new ColoredComplex(x, ConvergenceTester.miterCol(x, max));
                    pointList.add(c);
                    // System.out.println("next value of real: " + nextPoint(x).real());
                    // System.out.println("stop val: " + xyStart().real() + xRange() / 2);
                }
                System.out.println("thread 1 done.");
            });
        }

        synchronized (pointList) {
            t2 = new Thread(() -> {
                //iterate over the other half of the field
                for (ComplexCoordinate x = new ComplexCoordinate(xyStart().real() + xRange() / 2,
                        xyStart().imaginary()); nextPoint(x) != null; x = nextPoint(x)) {
                    int iter = ConvergenceTester.miter(x, max);
                    ColoredComplex c = new ColoredComplex(x, iter);
                    // ColoredComplex c = new ColoredComplex(x, ConvergenceTester.miterCol(x, max));
                    pointList.add(c);
                }
                System.out.println("thread 2 done.");
            });
        }
        t1.start();
        t2.start();
        while (t1.isAlive() || t2.isAlive()) {
            // wait until both threads have completed
        }
        h.setWorking(false); //allow user input again
        System.out.println("elapsed time: " + (System.currentTimeMillis() - startTime) + "ms");
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
        // because half density only plots every 2 points
    }

    public double imaginaryIncrement() {
        return (yRange() / HEIGHT) * (1 / DENSITY);
        // reciprocal of density constant
        // because half density only plots every 2 points
    }

}