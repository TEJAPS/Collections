package generic;

import java.util.ArrayList;
import java.util.List;

public class BeforeAfterGeneric01 {
    public static void main(String[] args) throws Exception{
        // Without Generics
//        List list = new ArrayList(); // raw type
//        list.add("42");
//        Integer n = (Integer) list.get(0); // Runtime error: ClassCastException

        // With Generics
        List<Integer> nums = new ArrayList<>();
        nums.add(42);
        Integer n = nums.get(0); // Safe, no cast needed


    }
}