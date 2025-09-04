package generic;

// A generic class with one type parameter
class Box<T> {
    private T value;

    // Constructor
    public Box(T value) {
        this.value = value;
    }

    // Getter
    public T getValue() {
        return value;
    }

    // Setter
    public void setValue(T value) {
        this.value = value;
    }
}

    class GenericClass02 {
    public static void main(String[] args) {
        // Create a Box of Integer
        Box<Integer> intBox = new Box<>(123);
        System.out.println("Integer Value: " + intBox.getValue());

        // Create a Box of String
        Box<String> strBox = new Box<>("Hello Generics");
        System.out.println("String Value: " + strBox.getValue());
    }
}

