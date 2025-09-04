package generic;

import java.util.*;

class ProducerExtends06 {
    // Producer: we only read from the list
    static double sum(List<? extends Number> nums) {
        double total = 0.0;
        for (Number n : nums) {
            total += n.doubleValue();  // safe to read as Number
        }
        return total;
    }

    public static void main(String[] args) {
        List<Integer> intList = Arrays.asList(1, 2, 3, 4);
        List<Double> doubleList = Arrays.asList(1.5, 2.5, 3.5);

        System.out.println("Sum of Integers: " + sum(intList));
        System.out.println("Sum of Doubles: " + sum(doubleList));
    }
}

//It will work for any subtype of Number, but you can’t add new elements.
