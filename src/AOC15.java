import org.jetbrains.annotations.NotNull;

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
            if (o instanceof Point p) { return p.x == x && p.y == y; }
            return false;
        }

        @Override
        public int hashCode() {
            return y*311 + x;
        }

        public String toString() {
            return "(" + x + ", " + y + ") -> " + cost;
            //return "x:" + x + ",y:" + y + ",c:" + cost;
        }

        @Override
        public int compareTo(@NotNull Point o) {
            return   cost- o.cost;
        }
    }

    public static void main(String[] args) throws IOException {
        new AOC15().start(args[0]);
    }

    private void start(String fn) throws IOException {
        var data = Files.lines(Paths.get(fn)).map( s-> s.chars().map(x->x-'0')  .toArray() ).toList();
        part1(data);
    }

    private void part1(List<int[]> data) {
        solve1(data);
        sz1 = data.size();
        sz2 = data.size()*5;
        solve2(data);
    }

    final static int[][] DIR = { {0,1}, {1,0}, {0,-1}, {-1,0} };

    int sz1, sz2;

    private void solve2(List<int[]> data) {
        LinkedList<Point> next = new LinkedList<>();
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
            next.sort((p1, p2) ->p1.cost - p2.cost);
        }
    }

    int getDelta(int i, int j) {
        if (i == 0 & j == 0 ) return 0;
        if (j == 0) return i;
        return 1+getDelta(i, j-1);
    }

    private int getCost(List<int[]> data, int x, int y) {
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
        LinkedList<Point> next = new LinkedList<>();
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
            next.sort(null);
        }
    }

}
