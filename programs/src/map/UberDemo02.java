package map;


import java.util.*;

public class UberDemo02 {

    // --- Simple domain models ---
    static record Driver(String id) {
    }

    static record Cell(int x, int y) {
    }

    // --- Dispatcher holding live availability by grid cell ---
    static class Dispatcher {
        private final Map<Cell, List<Driver>> available = new HashMap<>();

        // A driver appears in a cell (deduped per cell)
        void appear(Cell c, Driver d) {
            List<Driver> list = available.computeIfAbsent(c, k -> new ArrayList<>());
            if (!list.contains(d)) list.add(d);
        }

        // Move driver from one cell to another
        void move(Driver d, Cell from, Cell to) {
            available.computeIfPresent(from, (k, list) -> {
                list.removeIf(x -> x.id().equals(d.id()));
                return list;
            });
            cleanupEmpty();
            appear(to, d);
        }

        // Accept a ride: remove driver wherever they are
        void acceptRide(Driver d) {
            available.values().forEach(list -> list.removeIf(x -> x.id().equals(d.id())));
            cleanupEmpty();
        }

        // Read-only view of drivers in a cell
        List<Driver> driversIn(Cell c) {
            List<Driver> list = available.get(c);
            return list == null ? List.of() : Collections.unmodifiableList(list);
        }

        // Nearby search within Manhattan radius r (includes the center cell)
        List<Driver> nearby(Cell center, int radius) {
            List<Driver> out = new ArrayList<>();
            for (int dx = -radius; dx <= radius; dx++) {
                for (int dy = -radius; dy <= radius; dy++) {
                    if (Math.abs(dx) + Math.abs(dy) <= radius) {
                        Cell c = new Cell(center.x() + dx, center.y() + dy);
                        List<Driver> list = available.get(c);
                        if (list != null) out.addAll(list);
                    }
                }
            }
            return out;
        }

        // Helper to drop empty buckets
        private void cleanupEmpty() {
            available.entrySet().removeIf(e -> e.getValue().isEmpty());
        }

        // Pretty print
        void printState(String label) {
            System.out.println("\n" + label);
            if (available.isEmpty()) {
                System.out.println("  (no drivers)");
                return;
            }
            available.forEach((cell, list) -> System.out.println("  " + cell + " -> " + list));
        }
    }

    // --- Demo ---
    public static void main(String[] args) {
        Dispatcher d = new Dispatcher();

        Cell c47 = new Cell(4, 7);
        Cell c57 = new Cell(5, 7);
        Cell c58 = new Cell(5, 8);

        Driver D1 = new Driver("D1");
        Driver D2 = new Driver("D2");
        Driver D3 = new Driver("D3");

        // Appearances
        d.appear(c47, D1);
        d.appear(c47, D2);
        d.appear(c57, D3);
        d.printState("After appear() calls:");

        // Nearby search
        System.out.println("\nNearby (radius 1) around " + c57 + ": " + d.nearby(c57, 1));

        // Accept a ride for D2
        d.acceptRide(D2);
        d.printState("After acceptRide(D2):");

        // Move D3 from (5,7) to (5,8)
        d.move(D3, c57, c58);
        d.printState("After move(D3, (5,7)->(5,8)):");

        // Read-only view
        System.out.println("\nDrivers in " + c58 + ": " + d.driversIn(c58));
    }
}
