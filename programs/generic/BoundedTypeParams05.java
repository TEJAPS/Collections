package generic;

// Example of bounded type parameter
class BoundedTypeParams05 {

    // Generic method restricted to subclasses of Number
    public static <T extends Number> double add(T a, T b) {
        return a.doubleValue() + b.doubleValue();
    }

    // Generic method with multiple bounds (Number + Comparable)
    public static <T extends Number & Comparable<T>> T max(T a, T b) {
        return (a.compareTo(b) >= 0) ? a : b;
    }

    public static void main(String[] args) {
        // Using add() method
        System.out.println("Sum: " + add(10, 20));         // Integer
        System.out.println("Sum: " + add(5.5, 6.5));       // Double

        // Using max() method
        System.out.println("Max: " + max(3, 7));           // Integer
        System.out.println("Max: " + max(4.2, 2.8));       // Double
    }
}

//✅ Integer and Double (they are numbers AND comparable).
//❌ BigDecimal if it wasn’t comparable → not allowed.
//❌ String → not a number.
