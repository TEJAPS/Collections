import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MapDemo01 {

    // --- Small helpers to print section titles ---
    static void title(String t) { System.out.println("\n=== " + t + " ==="); }

    // --- Demo record types (need Java 16+ or change to simple classes) ---
    static record Driver(String id) {}
    static record Cell(int x, int y) {}

    // Used for equals/hashCode pitfall demo
    static final class MutableKey {
        String a; // changing this after put() breaks hashing contract
        MutableKey(String a) { this.a = a; }
        @Override public boolean equals(Object o) {
            return (o instanceof MutableKey mk) && Objects.equals(a, mk.a);
        }
        @Override public int hashCode() { return Objects.hash(a); }
        @Override public String toString() { return "Key(" + a + ")"; }
    }

    public static void main(String[] args) {
        // 1) Basics -----------------------------------------------------------
        title("Basics: put, get, containsKey, remove, size, isEmpty");
        Map<String, String> phone = new HashMap<>();
        phone.put("Alice", "999-111");
        phone.put("Bob",   "999-222");
        System.out.println("phone: " + phone);
        System.out.println("get(Alice) -> " + phone.get("Alice"));
        System.out.println("containsKey(Eve) -> " + phone.containsKey("Eve"));
        phone.remove("Bob");
        System.out.println("after remove(Bob): " + phone + "  size=" + phone.size() + " empty? " + phone.isEmpty());

        // 2) Views: keySet, values, entrySet ---------------------------------
        title("Views: keySet(), values(), entrySet()");
        phone.put("Carol", "999-333");
        System.out.println("keys:   " + phone.keySet());
        System.out.println("values: " + phone.values());
        System.out.println("entries:");
        for (Map.Entry<String,String> e : phone.entrySet()) {
            System.out.println("  " + e.getKey() + " -> " + e.getValue());
        }

        // 3) Convenience: putIfAbsent, getOrDefault, replace -----------------
        title("Convenience: putIfAbsent, getOrDefault, replace");
        Map<Integer,String> m = new HashMap<>();
        m.put(101, "Alice");
        m.putIfAbsent(101, "New");        // won't overwrite
        m.putIfAbsent(102, "Bob");        // adds
        System.out.println("map: " + m);
        String name = m.getOrDefault(999, "Unknown");
        System.out.println("getOrDefault(999) -> " + name);
        m.replace(102, "Bobby");          // replace if present
        System.out.println("after replace(102): " + m);

        // 4) computeIfAbsent: Uber/Ola-style grid of drivers -----------------
        title("computeIfAbsent: Map<Cell, List<Driver>> (grid buckets)");
        Map<Cell, List<Driver>> available = new HashMap<>();
        appear(available, new Cell(4,7), new Driver("D1"));
        appear(available, new Cell(4,7), new Driver("D2"));
        appear(available, new Cell(5,7), new Driver("D3"));
        System.out.println("available: " + available);
        // accept a ride -> remove driver id wherever present
        acceptRide(available, new Driver("D2"));
        System.out.println("after acceptRide(D2): " + available);

        // 5) merge: word frequency counter ----------------------------------
        title("merge(): word frequency");
        String[] words = "to be or not to be".split("\\s+");
        Map<String,Integer> freq = new HashMap<>();
        for (String w : words) freq.merge(w, 1, Integer::sum);
        System.out.println(freq);

        // 6) Iteration patterns ----------------------------------------------
        title("Iteration patterns: entrySet + forEach");
        for (Map.Entry<String,Integer> e : freq.entrySet()) {
            System.out.println("  " + e.getKey() + " -> " + e.getValue());
        }
        freq.forEach((k,v) -> System.out.println("  " + k + ":" + v));

        // 7) Ordering tricks: LinkedHashMap as LRU in ~6 lines ---------------
        title("LinkedHashMap: LRU (access-order)");
        LruCache<Integer, String> cache = new LruCache<>(3);
        cache.put(1,"A"); cache.put(2,"B"); cache.put(3,"C");
        cache.get(1);                // access 1 to make it most-recent
        cache.put(4,"D");            // evicts least-recent (key 2)
        System.out.println("LRU cache contents: " + cache);

        // 8) TreeMap: sorted + range queries --------------------------------
        title("TreeMap: sorted keys and range queries");
        TreeMap<Integer,String> tm = new TreeMap<>();
        tm.put(10,"A"); tm.put(20,"B"); tm.put(30,"C");
        System.out.println("tm: " + tm);
        System.out.println("floorKey(21) -> " + tm.floorKey(21));                  // 20
        System.out.println("ceilingEntry(19).getValue() -> " + tm.ceilingEntry(19).getValue()); // "B"
        System.out.println("subMap(10,true, 20,false) -> " + tm.subMap(10,true,20,false));      // [10..20)

        // 9) Equality & hashing pitfall --------------------------------------
        title("Equality & hashing pitfall: don't mutate keys after put()");
        Map<MutableKey, String> bad = new HashMap<>();
        MutableKey k = new MutableKey("x");
        bad.put(k, "VALUE");
        System.out.println("get before mutate -> " + bad.get(new MutableKey("x"))); // "VALUE"
        k.a = "y"; // BAD: mutating a field used in equals/hashCode
        System.out.println("get after mutate  -> " + bad.get(new MutableKey("x"))); // null (lost!)
        System.out.println("map still contains entry but in 'wrong bucket': " + bad);

        // 10) Null rules quick demo ------------------------------------------
        title("Null rules (HashMap allows one null key; TreeMap with natural order doesn't)");
        Map<String,String> hm = new HashMap<>();
        hm.put(null, "NULL-KEY-OK");
        hm.put("k", null); // null value OK
        System.out.println("HashMap nulls: " + hm);
        try {
            TreeMap<String,String> tm2 = new TreeMap<>(); // natural order
            tm2.put(null, "boom"); // NPE
        } catch (NullPointerException npe) {
            System.out.println("TreeMap(null key) -> " + npe);
        }

        // 11) Safe removal while iterating -----------------------------------
        title("Safe removal while iterating");
        Map<Integer,String> people = new HashMap<>();
        people.put(1,"Ann"); people.put(2,""); people.put(3,"Bob");
        // Safest: operate on a view collection
        people.entrySet().removeIf(e -> e.getValue().isBlank());
        System.out.println("after entrySet().removeIf(value blank): " + people);

        // 12) Concurrency quick taste ----------------------------------------
        title("ConcurrentHashMap basics");
        ConcurrentHashMap<String,Integer> chm = new ConcurrentHashMap<>();
        chm.put("x", 1);
        chm.merge("x", 1, Integer::sum);  // atomic per-key
        System.out.println("CHM: " + chm);
        // chm does not allow null keys/values

        // 13) Immutability & factories ---------------------------------------
        title("Immutability & factory methods");
        Map<String,Integer> m1 = Map.of("a",1, "b",2);               // unmodifiable
        Map<String,Integer> m2 = Map.copyOf(m1);                     // defensive unmodifiable copy
        Map<String,Integer> ro = Collections.unmodifiableMap(new HashMap<>(m1)); // view of a copy
        System.out.println("Map.of -> " + m1);
        System.out.println("Map.copyOf -> " + m2);
        System.out.println("unmodifiable(view of copy) -> " + ro);
        try { m1.put("c",3); } catch (UnsupportedOperationException e) { System.out.println("m1.put -> " + e); }
    }

    // --- Helpers for section (4): computeIfAbsent Uber-style buckets --------
    static void appear(Map<Cell, List<Driver>> available, Cell c, Driver d) {
        available.computeIfAbsent(c, k -> new ArrayList<>()).add(d);
    }
    static void acceptRide(Map<Cell, List<Driver>> available, Driver d) {
        available.values().forEach(list -> list.removeIf(x -> x.id().equals(d.id())));
    }

    // --- Section (7): LRU cache using LinkedHashMap -------------------------
    static class LruCache<K,V> extends LinkedHashMap<K,V> {
        private final int cap;
        LruCache(int cap) { super(16, 0.75f, true); this.cap = cap; } // access-order
        @Override protected boolean removeEldestEntry(Map.Entry<K,V> e) { return size() > cap; }
    }
}
