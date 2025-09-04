package generic;


import java.util.List;
import java.util.stream.Collectors;

public class NetflixGenerics09 {

    // ---------- WITHOUT GENERICS ----------
    static class PageRaw {
        List<Object> items;
        int page;
        int totalPages;

        PageRaw(List<Object> items, int page, int totalPages) {
            this.items = items;
            this.page = page;
            this.totalPages = totalPages;
        }

        List<Object> items() {
            return items;
        }
    }

    static class FeedServiceRaw {
        PageRaw loadRow(String rowId) {
            // Mixed bag: caller must "guess" the type
            List<Object> mixed = List.of(
                    new Movie("Inception", 148),
                    new Series("Breaking Bad", 5),
                    new Movie("Interstellar", 169)
            );
            return new PageRaw(mixed, 1, 1);
        }
    }

    // ---------- WITH GENERICS ----------
    interface FeedItem {
        String title();
    }

    static class Movie implements FeedItem {
        final String title;
        final int minutes;

        Movie(String title, int minutes) {
            this.title = title;
            this.minutes = minutes;
        }

        public String title() {
            return title;
        }

        public String toString() {
            return "Movie(" + title + ", " + minutes + "m)";
        }
    }

    static class Series implements FeedItem {
        final String title;
        final int seasons;

        Series(String title, int seasons) {
            this.title = title;
            this.seasons = seasons;
        }

        public String title() {
            return title;
        }

        public String toString() {
            return "Series(" + title + ", " + seasons + " seasons)";
        }
    }

    static class Page<T> {
        final List<T> items;
        final int page;
        final int totalPages;

        Page(List<T> items, int page, int totalPages) {
            this.items = items;
            this.page = page;
            this.totalPages = totalPages;
        }

        List<T> items() {
            return items;
        }
    }

    static class FeedService {
        // The feed is mixed internally
        private final List<FeedItem> mixed = List.of(
                new Movie("Inception", 148),
                new Series("Breaking Bad", 5),
                new Movie("Interstellar", 169),
                new Series("Chernobyl", 1)
        );

        // Ask for the specific subtype you want
        public <T extends FeedItem> Page<T> loadRow(String rowId, Class<T> kind) {
            List<T> filtered = mixed.stream()
                    .filter(kind::isInstance)
                    .map(kind::cast)
                    .collect(Collectors.toList());
            return new Page<>(filtered, 1, 1);
        }
    }

    // ---------- DEMO ----------
    public static void main(String[] args) {
        // Without generics: risky casting
        FeedServiceRaw raw = new FeedServiceRaw();
        PageRaw rawPage = raw.loadRow("continue");
        Object first = rawPage.items().get(0);
        // Need a cast — might explode at runtime if wrong:
        Movie m1 = (Movie) first; // ok here, but could throw ClassCastException
        System.out.println("[Raw] First item (after cast): " + m1);

        // With generics: type-safe, no casts
        FeedService svc = new FeedService();
        Page<Movie> movies = svc.loadRow("continue", Movie.class);
        Page<Series> shows = svc.loadRow("trending", Series.class);

        Movie m2 = movies.items().get(0);    // already Movie
        Series s1 = shows.items().get(0);    // already Series

        System.out.println("[Generics] First movie: " + m2);
        System.out.println("[Generics] First series: " + s1);
    }
}
