import java.util.ArrayList;

public class MandelbrotCore {

    final static double WIDTH = 600;
    final static double HEIGHT = 600;

    final static double DENSITY = .9; // density (decimal <= 1) where 1 == 100% density

    final int max = 255;

    ComplexCoordinate xyStart;
    ArrayList<ColoredComplexCoordinate> pointList;

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
        pointList = new ArrayList<ColoredComplexCoordinate>();
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

    public ArrayList<ColoredComplexCoordinate> getPointList() {
        return pointList;
    }

    public void calculatePoints() {

        // System.out.println("calc points");

        //TODO: disallow user input when first plotting the set

        synchronized(pointList) {
            // System.out.println("has lock");
            pointList = new ArrayList<ColoredComplexCoordinate>();
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