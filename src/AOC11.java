 import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AOC11 {

    public static void main(String[] args) throws IOException {
        new AOC11().start(args[0]);
    }

    int flashes;

    private void start(String fn) throws IOException {
        var data = Files.lines(Paths.get(fn)).map( s->  s.chars().map(c->c-'0').toArray()).toList();
        var d2 = data.stream().map( s-> s.clone()).toList();

        for (int i  = 0 ; i < 100; i++)  step(data);
        System.out.println("step1:" + flashes);

        int step2 = 0;
        do {
            step(d2);
            step2++;
        } while (d2.stream().filter(s -> IntStream.of(s).filter(t-> t !=    0).findFirst().isPresent() ).findFirst().isPresent() );
        //while (d2.stream().map( d -> IntStream.of(d).sum()).collect(Collectors.summingInt(Integer::intValue)) > 0);
        System.out.println("step2:" + step2);


    }

    private void step(List<int[]> data) {
        data.forEach(s -> { for (int i = 0; i < s.length; i++) s[i]++; });
        //data.forEach(s -> System.out.println(Arrays.toString(s)));
        visited = new HashSet<>();
        for (int j = 0 ; j < data.size(); j++) {
            int[] a = data.get(j);
            for (int i = 0; i < a.length; i++) {
                if (a[i] > 9) flash(data, i,j);
            }
        }
        visited.forEach(v -> data.get(v.get(1))[v.get(0)] = 0 );
        flashes += visited.size();
//        data.forEach(s -> { Arrays.stream(s).forEach(t ->System.out.print(t)); System.out.println();   });
    }

    Set<List<Integer>> visited;
    List<Integer> getKey(int i, int j) { return Arrays.asList(i,j); };

    private void flash(List<int[]> data, int i, int j) {
        var k = getKey(i,j);
        if (visited.contains(k)) return;
        visited.add(k);
        data.get(j)[i] = 0;

        for (int y = -1; y < 2; y++) {
            for (int x = -1; x < 2; x++) {
                if (x == 0 && y == 0) continue;
                int xx = x+i, yy = y+j;
                if (yy < 0 || yy >= data.size()) continue;
                int[]a = data.get(yy);
                if (xx < 0 || xx >= a.length) continue;

                a[xx]++;
                if (a[xx] > 9) flash(data, xx, yy);
            }
        }
    }
}
