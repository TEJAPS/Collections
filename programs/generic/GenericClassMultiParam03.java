package generic;

// A generic class with two type parameters
class Pair<T, U> {
    private T first;
    private U second;

    // Constructor
    public Pair(T first, U second) {
        this.first = first;
        this.second = second;
    }

    public void printPair() {
        System.out.println("First: " + first + " (" + first.getClass().getSimpleName() + ")");
        System.out.println("Second: " + second + " (" + second.getClass().getSimpleName() + ")");
    }
}

public class GenericClassMultiParam03 {
    public static void main(String[] args) {
        // Pair of String and Integer
        Pair<String, Integer> student = new Pair<>("Alice", 21);
        student.printPair();

        // Pair of Integer and Double
        Pair<Integer, Double> marks = new Pair<>(101, 89.5);
        marks.printPair();
    }
}
