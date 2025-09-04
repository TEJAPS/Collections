package generic;


// Generic Method Example
class GenericMethod04 {

    // A generic method with type parameter <T>
    public static <T> void printElement(T element) {
        System.out.println("Element: " + element + " (Type: " + element.getClass().getSimpleName() + ")");
    }

    public static void main(String[] args) {
        // Call generic method with different types
        printElement(100);             // Integer
        printElement("Hello Generics"); // String
        printElement(45.67);            // Double
        printElement(true);             // Boolean
    }
}
