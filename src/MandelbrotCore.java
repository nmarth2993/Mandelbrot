import java.util.ArrayList;
import java.awt.Color;

public class MandelbrotCore {

    final static double WIDTH = 500;
    final static double HEIGHT = 500;

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

    public ComplexCoordinate xyStart() {
        return xyStart;
    }

    public ArrayList<ColoredComplexCoordinate> getPointList() {
        return pointList;
    }

    public void calculatePoints() {
        for (ComplexCoordinate x = new ComplexCoordinate(xyStart().real(), xyStart().imaginary()); nextPoint(x) != null; x = nextPoint(x)) {
            int iter = ConvergenceTester.miter(x, max);
            ColoredComplexCoordinate c = new ColoredComplexCoordinate(x, iter);
            pointList.add(c);
        }
    }

    public ComplexCoordinate nextPoint(ComplexCoordinate z) {

        if (z.real() + realIncrement() > xyStart().real() + xRange()) {
            return null;
            // the next point is not in the scope
        }
        else if (z.imaginary() + imaginaryIncrement() > xyStart().imaginary() + yRange()) {
            return new ComplexCoordinate(z.real() + realIncrement(), xyStart().imaginary());
            // the next point is on the next line, so shift accordingly
        }
        else {
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
        return xRange() / WIDTH;
    }

    public double imaginaryIncrement() {
        return yRange() / HEIGHT;
    }

    public void setRealIncrement(double inc) {
        realIncrement = inc;
    }

    public void setImaginaryIncrement(double inc) {
        imaginaryIncrement = inc;
    }

}