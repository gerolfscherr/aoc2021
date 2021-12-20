import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

record Coord(int x, int y)  {}

public class AOC20 {
    public static void main(String[] args) throws IOException {
        new AOC20(args[0]);
    }

    void printMap(Map<Coord, Integer> map) {
        var xx = map.keySet().stream().mapToInt($ -> $.x()).summaryStatistics();
        var yy = map.keySet().stream().mapToInt($ -> $.y()).summaryStatistics();

        int xmin = xx.getMin() - 2;
        int xmax = xx.getMax() + 2;
        for (int y = yy.getMin()-2; y <= yy.getMax()+2; y++) {
            for (int x = xmin; x <= xmax; x++) {
                int r = map.getOrDefault(new Coord(x, y), bg);
                if (r == 1) System.out.print('#');
                else System.out.print('.');
            }
            System.out.println();
        }
        System.out.println();
    }



    private Map<Coord, Integer> iterate(Map<Coord, Integer> map, String dec, int bg) {
        Map<Coord, Integer> dst = new HashMap<Coord, Integer>();
        var xx = map.keySet().stream().mapToInt($ -> $.x()).summaryStatistics();
        var yy = map.keySet().stream().mapToInt($ -> $.y()).summaryStatistics();

        int ymin = yy.getMin();
        int ymax = yy.getMax();
        for (int y = ymin; y <= ymax; y++) {
            for (int x = xx.getMin() - 2; x <= xx.getMax() + 2; x++) {
                if (dec.charAt(getSum(map, x, y)) == '#') dst.put(new Coord(x, y), 1);
                else dst.put(new Coord(x, y), 0);
            }
        }
     return dst;
    }

    private int getSum(Map<Coord, Integer> map, int x, int y) {
        int sum = 0;
        int n = 8;
        for (int j = y - 1; j <= y + 1; j++) {
            for (int i = x - 1; i <= x + 1; i++) {
                int r = map.getOrDefault(new Coord(i, j), bg);
     //           if (r < 0 || r > 1) throw new IllegalStateException();
                sum |= r << n;
                n--;
            }
        }
        return sum;
    }

    int bg = 0;

    public AOC20(String fn) throws IOException {
        long t= System.currentTimeMillis();
        var a = Files.readAllLines(Paths.get(fn));
        String dec = a.get(0);
        Map<Coord, Integer> map = new HashMap<>(); // treemap is much slower

        var data = a.subList(2, a.size());
        for (int y = 0; y < data.size(); y++) {
            String s = data.get(y);
            for (int x = 0; x < s.length(); x++) {
                map.put(new Coord(x, y), s.charAt(x) == '#' ? 1 : 0);
            }
        }

        for (int i = 0; i < 50; i++) {
            map = iterate(map, dec, bg);
            bg = 1-bg;
        }
        System.out.println(map.values().stream().filter($ -> $ == 1).count());
        System.out.println((System.currentTimeMillis() - t));
    }
}