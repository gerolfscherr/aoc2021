import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class AOC9 {

    int bs;
    Set<String> visited;
    List<Integer> basins = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        new AOC9().start(args[0]);
    }

    private void start(String fn) throws IOException {
        var a = Files.lines(Paths.get(fn)).toList();
        process(a);
    }

    private void process(List<String> l) {
        int risk = 0;
        for (int j = 0 ; j < l.size();j++) {
            for (int i = 0 ; i < l.get(j).length(); i++) {
                if (isLowPoint(l, i,j)) {
                    bs = 0;
                    visited = new HashSet<>();
                    countBasinSize(l, i,j);
                    //System.out.println(i + ", " + j + " => " + bs);
                    basins.add(bs);
                }
                risk += getRisk(l, i, j);
            }
        }
        Collections.sort(basins, Collections.reverseOrder());

        System.out.println("part1:" + risk);
        var top = basins.subList(0,3);
        System.out.println("part2: " + top + ":" + top.stream().reduce(1, (a,b) -> a*b));
    }

    String getKey(int i, int j) { return i + ":" + j;};

    private void countBasinSize(List<String> a, int i, int j) {
        String key = getKey(i,j);
        if (visited.contains(key)) return;
        visited.add(key);
        int me = getAt(a, i,j);
        if (me < 9) {
            bs++;
            countBasinSize(a,i,j-1);
            countBasinSize(a, i, j+1);
            countBasinSize(a, i-1, j);
            countBasinSize(a, i+1, j);
        }
    }

    private Integer getAt(List<String> a , int i, int j) {
        if (j < 0 || j >= a.size()) return 9;
        if (i < 0) return 9;
        String s = a.get(j);
        if (i >= s.length()) return 9;
        return s.charAt(i) -'0';
    }

    private int getRisk(List<String> a, int i, int j) {
        int me = getAt(a, i,j),
        n = getAt(a,i,j-1),
        s = getAt(a, i, j+1),
        w = getAt(a, i-1, j),
        e = getAt(a, i+1, j);
        if  (me < n && me < s && me < w && me < e) return me + 1;
        return 0;
    }

    private boolean isLowPoint(List<String> a, int i, int j) {
        int me = getAt(a, i,j),
        n = getAt(a,i,j-1),
        s = getAt(a, i, j+1),
        w = getAt(a, i-1, j),
        e = getAt(a, i+1, j);
        return me < n && me < s && me < w && me < e;
    }
}
