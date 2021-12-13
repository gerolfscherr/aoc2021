import java.awt.Point;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class AOC13 {

    public static void main(String[] args) throws IOException {
        new AOC13().start(args[0]);
    }

    class Split {String coord; int val; public Split(String c, int v) { coord = c; val = v; }
        public @Override String  toString() { return "Split(" + coord + ", " + val + ")"; }
    }

    private void start(String fn) throws IOException {
    Set<Point> points = new HashSet<>();
    List<Split> splits = new ArrayList<>();
        Files.lines(Paths.get(fn)).forEach(s-> {
            if (s.contains(",")) {
                int[] a = Arrays.stream(s.split(",")).mapToInt(Integer::parseInt).toArray();
                points.add(new Point(a[0], a[1]));
            } else if (s.contains("=")) {
                String[] x = s.split("=");
                splits.add(new Split(x[0].substring(x[0].length()-1), Integer.parseInt(x[1])));
            }
        });
     //   System.out.println(points);
     //   System.out.println(splits);
        boolean first = true;
        var o = points;
        do {
            o = o.stream().map(p -> {
                Split split = splits.get(0);
                int x = p.x, y = p.y, v = split.val;
                if ("x".equals(split.coord)) {
                    if (x > v)  x = 2 * v - x;
                } else if (y > v) y = v - (y-v);
                return new Point(x, y);
            }).collect(Collectors.toSet());
            splits.remove(0);
            if (first) { first = false; System.out.println("first:" + o.size()); }
        } while(splits.size() > 0);


        //print it. obv ths has really bad runtime, but who cares
        System.out.println("set:" + o);
        for (int y = 0; y < 10; y++) {
            outer: for (int x =0; x < 50; x++) {
                for (Point p : o) {
                    if (p.x== x && p.y == y) { System.out.print("##"); continue outer;}
                }
                System.out.print("  ");
            }
            System.out.println();
        }
    }
}
