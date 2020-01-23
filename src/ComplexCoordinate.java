public class ComplexCoordinate {

    private double a;
    private double b;

    public ComplexCoordinate(double a, double b) {
        this.a = a;
        this.b = b;
    }

    public double real() {
        return a;
    }

    public double imaginary() {
        return b;
    }

    public double mod() { //distance from zero
        return Math.hypot(real(), imaginary());
    }

    public ComplexCoordinate plus(ComplexCoordinate c) {
        return new ComplexCoordinate(real() + c.real(), imaginary() + c.imaginary());
    }

    public ComplexCoordinate multiply(ComplexCoordinate c) {
        //FOIL
        return new ComplexCoordinate(real() * c.real() - (imaginary() * c.imaginary()), real() * c.imaginary() + c.real() * imaginary());
    }

    public ComplexCoordinate square() {
        return this.multiply(this);
    }

    public String toString() {
        if (real() == 0 && imaginary() == 0) {
            return "" + 0;
        }
        return (real() != 0 ? real() : "") + (imaginary() > 0 && real() != 0 ? "+" : "") + (imaginary() != 0 ? (imaginary() + "i") : "");
        //if either term is 0, no need to print
        //if b is negative, don't print the + sign
    }
}