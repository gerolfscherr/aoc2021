import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class AOC5 {

    public final static boolean phase2 = false;

    public static void main(String[] args) throws IOException {
        new AOC5().start(args[0]);
    }

    static class Point {
        int x, y;
        public Point(int xx, int yy) { x = xx; y = yy; }
        @Override
        public boolean equals(Object o) {
            if (o instanceof Point peer) return peer.x == x && peer.y == y;
            return false;
        }

        @Override
        public int hashCode() { return Objects.hash(x, y); } // needed for function.identity

        public String toString() { return "(" + x + "," + y + ")";}
    }

    static class Line {
        Point start, end;
        public Line(List<Integer> x) {
            start = new Point(x.get(0), x.get(1));
            end = new Point(x.get(2), x.get(3));
        }

        public String toString() { return start + "->" + end;  }

        public static Stream<Integer> seq(int a, int b) {
            if (a < b) return IntStream.rangeClosed(a, b).boxed();
            return IntStream.rangeClosed(b,a).boxed();
        }

        Stream<Point> getPoints() {
            if (start.x == end.x) {
                return seq(start.y, end.y).map(y-> { return new Point(start.x, y); } );
            } else if (start.y == end.y) {
                return seq(start.x, end.x).map(x-> { return new Point(x, start.y); } );
            }
            if (phase2) {
                List<Point> l = new ArrayList<>();
                int dx = 1, dy = 1;
                if (end.x < start.x) dx = -1;
                if (end.y < start.y) dy = -1;
                int a = start.x, b = start.y;
                while (a != end.x && b != end.y) {
                    l.add(new Point(a, b));
                    a += dx;
                    b += dy;
                }
                l.add(new Point(end.x, end.y));
                return l.stream();
            }
            return Stream.empty();
        }
    }

    private void start(String fn) throws IOException {
        var a =Files.lines(Paths.get(fn)).map(s -> {
            return new Line(Pattern.compile("\\D+").splitAsStream(s).mapToInt(Integer::parseInt).boxed().toList());
        }) .toList();
        var b = a.stream().flatMap(s->s.getPoints());
        var c = b.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        System.out.println(c.entrySet().stream().filter(x -> x.getValue() > 1).count());
    }
}
