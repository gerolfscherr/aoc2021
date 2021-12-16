
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class AOC15 {

    static class Point implements  Comparable<Point> {
        int x, y, cost;
        public Point(int xx, int yy) { x = xx; y = yy;}
        public Point(int xx, int yy, int c) {
            x = xx; y = yy; cost = c;
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) return true;
            if (o instanceof Point p) { return p.x == x && p.y == y; }
            return false;
        }

//equals objects must have same hash code
        @Override
        public int hashCode() {
            return y*311 + x;
        }

        public String toString() {
            return "(" + x + ", " + y + ") -> " + cost;
        }

        // treeset stipulates that if compareto == 0 the objects are equal. so we have to compare all properties here
        // even if we are interested only in the cost.
        // another solution would be to use LinkedList, but it has complexity of N instead of log N
        @Override
        public int compareTo(Point o) {
            int r =   cost- o.cost;
            if (r ==0) r = y-o.y;
            if (r ==0) r = x-o.x;
            return r;
        }
    }

    public static void main(String[] args) throws IOException {
       // for (int i = 0 ; i <  Integer.parseInt(args[1]);i++ )
        new AOC15().start(args[0]);
    }

    private void start(String fn) throws IOException {
        var data = Files.lines(Paths.get(fn)).map( s-> s.chars().map(x->x-'0')  .toArray() ).toList();
        part1(data);
    }

    private void part1(List<int[]> data) {
        solve1(data);
        solve2(data);
    }

    final static int[][] DIR = { {0,1}, {1,0}, {0,-1}, {-1,0} };
    final static int sz1 = 100, sz2=500;

    private void solve2(List<int[]> data) {
        TreeSet<Point>next = new TreeSet<>();
        next.add(new Point(0,0, 0));
        Set<Point> visited = new HashSet<>();
        int i = 0;
        while (!next.isEmpty()) {
            Point cur = next.pollFirst();
            visited.add(cur);
            if (cur.y == sz2-1 && cur.x == sz2-1) {
                System.out.println("part1:" + cur.cost);
                return;
            }
            for (int[] dir : DIR)  {
                int a = cur.x+dir[0], b = cur.y + dir[1];
                if (b < 0 || b >= sz2) continue;
                if (a < 0 || a >= sz2) continue;
                Point p = new Point(a,b,  getCost(data, a,b) + cur.cost);
                if (visited.contains(p) || next.contains(p)) continue;
                next.add(p);
            }
        }
    }


    private final static int getDelta(int i, int j) {
        if (i == 0 & j == 0 ) return 0;
        if (j == 0) return i;
        return 1+getDelta(i, j-1);
    }

    private final static int getCost(List<int[]> data, int x, int y) {
        int a = x%sz1;
        int b = y%sz1;
        int c = data.get(b)[a];

        int i = x / sz1;
        int j = y / sz1;
        int r = (c+getDelta(i,j));
        if (r > 9) r -= 9;
        return r;
    }

    private void solve1(List<int[]> data) {
        System.out.println("solve");
        TreeSet<Point> next = new TreeSet<>();
        next.add(new Point(0,0, 0));
        Set<Point> visited = new HashSet<>();
        int i = 0;
        while (!next.isEmpty()) {
            Point cur = next.pollFirst(); //next.remove();
            visited.add(cur);
            if (cur.y == data.size()-1 && cur.x == data.get(data.size()-1).length-1) {
                System.out.println("part1:" + cur.cost);
                return;
            }
            for (int[] dir : DIR)  {
                int a = cur.x+dir[0], b = cur.y + dir[1];
                if (b < 0 || b >= data.size()) continue;
                if (a < 0 || a >= data.get(b).length) continue;
                int c = data.get(b)[a];
                Point p = new Point(a,b, cur.cost + c);
                if (visited.contains(p) || next.contains(p)) continue;
                next.add(p);
            }
            //next.sort(null);
        }
    }

}
