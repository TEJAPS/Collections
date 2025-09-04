package generic;

import java.util.*;

class ConsumerSuper07 {
    // Consumer: we only write into the list
    static void addNumbers(List<? super Integer> list) {
        list.add(10);
        list.add(20);
    }

    public static void main(String[] args) {
        List<Integer> intList = new ArrayList<>();
        List<Number> numList = new ArrayList<>();

        addNumbers(intList);  // Integer list is fine
        addNumbers(numList);  // Number list also fine

        System.out.println("Integer List: " + intList);
        System.out.println("Number List: " + numList);
    }
}
// ✅ We can safely add Integers to any list of Integer or its supertype.
