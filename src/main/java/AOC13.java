import java.awt.Point;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class AOC13 {

    public static void main(String[] args) throws IOException {
        for (int i = 0; i < Integer.parseInt(args[1]); i++)
            new AOC13().start(args[0]);
    }

    private static final class Split { final String coord; final int val; public Split(String c, int v) { coord = c; val = v; }
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

//        Point pmax = o.stream().reduce(new Point(0,0), (p,c) -> {   p.x = Math.max(p.x, c.x); p.y = Math.max(p.y, c.y); return p;  });
       // System.out.println("set:" + o);
        outer: for (int y = 0; y <= 10; y++) {
            inner: for (int x =0; x <= 50; x++) {
                if (o.isEmpty()) break outer;
                for (Point p : o) {
                    if (p.x== x && p.y == y) { System.out.print("##"); o.remove(p); continue inner;}
                }
                System.out.print("  ");
            }
            System.out.println();
        }
    }
}
