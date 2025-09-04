import java.util.*;

// Track data model (record introduced in Java 14+)
record Track(String id, String title, boolean explicit, int rating /*1..5*/) {
    @Override
    public String toString() {
        return title + " (rating=" + rating + (explicit ? ", explicit" : "") + ")";
    }
}

// Playlist using List APIs
class Playlist {
    private final List<Track> tracks = new ArrayList<>();

    // Add a track (duplicates allowed)
    void add(Track t) { tracks.add(t); }

    // Reorder two tracks by index
    void swap(int i, int j) { Collections.swap(tracks, i, j); }

    // Remove all explicit tracks
    void removeExplicit() { tracks.removeIf(Track::explicit); }

    // Sort tracks by rating (high → low), then title
    void sortByRating() {
        tracks.sort(Comparator.comparingInt(Track::rating)
                .reversed()
                .thenComparing(Track::title));
    }

    // "Up next" (first N tracks) – returns a *view* of the list
    List<Track> upNext(int n) {
        return tracks.subList(0, Math.min(n, tracks.size()));
//         return new ArrayList<>(tracks.subList(0, Math.min(n, tracks.size())));

    }

    // Print current playlist
    void printAll() { System.out.println(tracks); }
}

// Main demo class
class Main {
    public static void main(String[] args) {
        Playlist playlist = new Playlist();

        // Add some tracks
        playlist.add(new Track("t1", "Song A", false, 5));
        playlist.add(new Track("t2", "Song B", true, 3));
        playlist.add(new Track("t3", "Song C", false, 4));
        playlist.add(new Track("t4", "Song D", false, 5));

        System.out.println("🎵 Initial Playlist:");
        playlist.printAll();

        // Swap two tracks
        playlist.swap(0, 2);
        System.out.println("\nAfter swapping index 0 and 2:");
        playlist.printAll();

        // Remove explicit songs
        playlist.removeExplicit();
        System.out.println("\nAfter removing explicit tracks:");
        playlist.printAll();

        // Sort by rating
        playlist.sortByRating();
        System.out.println("\nAfter sorting by rating:");
        playlist.printAll();

        // Show "Up Next"
        List<Track> upNext = playlist.upNext(2);
        System.out.println("\n▶ Up Next (first 2 tracks): " + upNext);

        // Demonstrate subList view
        playlist.add(new Track("t5", "Song E", false, 2));
        System.out.println("\nAfter adding a new song to playlist:");
        playlist.printAll();
        System.out.println("Up Next view reflects change: " + upNext);
    }
}
