import java.awt.Color;
public class ColoredComplex extends ComplexCoordinate {

    private Color c;

    public ColoredComplex(double a, double b) {
        super(a, b);
    }
    
    public ColoredComplex(ComplexCoordinate z, int col) {
        super(z.real(), z.imaginary());
        setCol(col);
    }

    public ColoredComplex(ComplexCoordinate z, Color col) {
        super(z.real(), z.imaginary());
        c = col;
    }

    public void setCol(int col) {
        col = 255 - col;
        c = new Color(col, col, col);
    }

    public Color getColor() {
        return c;
    }

    public ComplexCoordinate getZ() {
        return new ComplexCoordinate(super.real(), super.imaginary());
    }

}