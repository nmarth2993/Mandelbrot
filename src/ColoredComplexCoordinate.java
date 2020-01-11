import java.awt.Color;

public class ColoredComplexCoordinate { //could have this as subclass of ComplexCoord
    
    ComplexCoordinate z;
    Color c;

    public ColoredComplexCoordinate(ComplexCoordinate z, int col) {
        setZ(z);
        setC(col);
    }

    public ColoredComplexCoordinate(ComplexCoordinate z, Color col) {
        setZ(z);
        setC(col);
    }

    public ComplexCoordinate getZ() {
        return z;
    }

    public void setZ(ComplexCoordinate z) {
        this.z = z;
    }

    public Color getC() {
        return c;
    }

    public void setC(Color c) {
        this.c = c;
    }

    public void setC(int col) {
        col = 255 - col;
        c = new Color(col, col, col);
    }

    public String toString() {
        if (z.real() == 0 && z.imaginary() == 0) {
            return "" + 0;
        }
        return (z.real() != 0 ? z.real() : "") + (z.imaginary() > 0 && z.real() != 0 ? "+" : "") + (z.imaginary() != 0 ? (z.imaginary() + "i") : "");
    }
}