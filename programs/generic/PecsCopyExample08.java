package generic;

import java.util.*;

class PecsCopyExample08 {

    // Copy elements from source (Producer) to destination (Consumer)
    public static <T> void copy(List<? super T> dest, List<? extends T> src) {
        for (T item : src) {
            dest.add(item); // safe: writing into supertype list
        }
    }

    public static void main(String[] args) {
        List<Integer> src = Arrays.asList(1, 2, 3, 4); // Producer (extends)
        List<Number> dest = new ArrayList<>();         // Consumer (super)

        copy(dest, src);

        System.out.println("Source List: " + src);
        System.out.println("Destination List: " + dest);
    }
}

// when reading be generic i.e the list produces for us - src is a Producer (? extends T) → we only read from it.
// when writing be specific i.e. we produce the list - dest is a Consumer (? super T) → we only write into it.