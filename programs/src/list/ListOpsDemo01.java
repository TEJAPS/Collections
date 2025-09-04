package list;


import java.util.*;

public class ListOpsDemo01 {

    static void print(String label, List<String> l) {
        System.out.println(label + " " + l + "  (size=" + l.size() + ")");
    }

    public static void main(String[] args) {
        // Start with an empty, resizable list
        List<String> l = new ArrayList<>();

        // --- add / add(index, value) ---
        l.add("red");                         // append
        l.add("green");
        l.add(0, "blue");                     // insert at index
        print("After adds:", l);              // [blue, red, green]

        // --- set(index, value) / replace ---
        l.set(1, "yellow");                   // replace element at index 1
        print("After set(1,'yellow'):", l);   // [blue, yellow, green]

        // --- remove(value) (first match) ---
        l.remove("blue");                     // remove by value
        print("After remove(\"blue\"):", l);  // [yellow, green]

        // --- remove(index) ---
        l.remove(0);                          // remove by index
        print("After remove(0):", l);         // [green]

        // --- contains / indexOf / lastIndexOf ---
        l.addAll(List.of("red", "cyan", "red")); // duplicates allowed
        System.out.println("contains(\"red\")? " + l.contains("red"));         // true
        System.out.println("indexOf(\"red\") = " + l.indexOf("red"));          // first index
        System.out.println("lastIndexOf(\"red\") = " + l.lastIndexOf("red"));  // last index
        print("After duplicates:", l);        // [green, red, cyan, red]

        // --- subList(from, to) is a VIEW (not a copy) ---
        List<String> view = l.subList(1, 3);  // elements at indexes 1..2
        System.out.println("subList(1,3) view: " + view); // [red, cyan]
        // Mutate parent -> view sees it
        l.set(2, "magenta");
        System.out.println("view after parent change: " + view); // [red, magenta]
        // If you need independence, make a copy:
        List<String> copy = new ArrayList<>(view);
        System.out.println("independent copy of view: " + copy);

        // --- sort (natural order) ---
        l.sort(Comparator.naturalOrder());    // or l.sort(null)
        print("After sort(natural):", l);

        // --- replaceAll (transform elements) ---
        l.replaceAll(String::toUpperCase);
        print("After replaceAll( toUpperCase ):", l);

        // --- removeIf (bulk conditional removal) ---
        l.removeIf(s -> s.length() < 4);
        print("After removeIf(len < 4):", l);

        // --- toArray ---
        String[] arr = l.toArray(new String[0]);            // classic
        String[] arr2 = l.toArray(String[]::new);           // Java 11+
        System.out.println("toArray(new String[0]) -> " + Arrays.toString(arr));
        System.out.println("toArray(String[]::new) -> " + Arrays.toString(arr2));

        // --- Fixed-size & Unmodifiable traps ---
        System.out.println("\nFixed-size and unmodifiable examples:");
        List<String> fixed = Arrays.asList("x", "y");       // fixed-size view
        System.out.println("Arrays.asList -> " + fixed + " (can set, cannot add/remove)");
        try {
            fixed.add("z");                                  // throws UnsupportedOperationException
        } catch (UnsupportedOperationException e) {
            System.out.println("fixed.add -> " + e);
        }
        List<String> unmod = List.of("p", "q");             // truly unmodifiable
        System.out.println("List.of -> " + unmod + " (no set/add/remove)");
        try {
            unmod.set(0, "P");
        } catch (UnsupportedOperationException e) {
            System.out.println("unmod.set -> " + e);
        }

        // --- Fail-fast removal vs iterator.remove ---
        System.out.println("\nFail-fast vs safe removal:");
        List<String> colors = new ArrayList<>(List.of("AA", "XB", "XC", "AD"));
        try {
            for (String s : colors) {                       // enhanced for uses an iterator underneath
                if (s.startsWith("X")) colors.remove(s);    // will throw CME
            }
        } catch (ConcurrentModificationException ex) {
            System.out.println("Caught: " + ex.getClass().getSimpleName());
        }
        // Correct: use explicit iterator.remove()
        for (Iterator<String> it = colors.iterator(); it.hasNext(); ) {
            if (it.next().startsWith("X")) it.remove();     // safe
        }
        System.out.println("After safe removal: " + colors);
    }
}

