import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class AOC14 {



    public static void main(String[] args) throws IOException {
        new AOC14().start(args[0]);
    }



    private void start(String fn) throws IOException {
        Path f = Paths.get(fn);
        String in = Files.lines(f).findFirst().get();
        var map =  Files.lines(f).skip(2).map(s -> s.split(" -> ")).collect(Collectors.toMap(s->s[0], s->s[1]));
        process1(in, map);
        process2(in, map);
    }



    void process2(String in, Map<String, String>dict) {
        var pairs = new HashMap<String, Long>();
        for (int i = 0 ; i < in.length()-1; i++) pairs.merge(in.substring(i, i+2), 1l, Long::sum);
        var letters = in.chars().boxed().map(s->Character.toString(s)).collect(Collectors.toMap(x->x, x->1l, Long::sum));

        for (int i = 0 ; i < 40; i++) {
            var np = new HashMap<String, Long>();
            for (var kv : pairs.entrySet()) {
                String pair = kv.getKey();
                Long v = kv.getValue();
                String m = dict.get(pair);
        //        if (m == null) throw new IllegalArgumentException(pair);
                String l = pair.substring(0, 1),  r = pair.substring(1, 2);

                np.merge(l+m, v, Long::sum);
                np.merge(m+r, v, Long::sum);
                letters.merge(m, v, Long::sum);
            }
            pairs = np;
        }
        var freq= letters.values().stream().sorted().toList();
        System.out.println("diff:" + (freq.get(freq.size()-1) - freq.get(0)));
    }



    private void process1(String in, Map<String, String> map) {
        for (int j = 0; j < 10; j++) {
            var b = new StringBuilder();
            b.append(in.charAt(0));
            for (int i = 0; i < in.length() - 1; i++) {
                String k = in.substring(i, i + 2);
                b.append(map.getOrDefault(k, ""));
                b.append(k.charAt(1));
            }
            in = b.toString();
        }

        var letters = in.chars().boxed().collect(Collectors.groupingBy(s-> Character.toString(s), Collectors.counting()));
        var c = letters.values().stream().sorted().toList();
        System.out.println("z:" + (c.get(c.size()-1) -  c.get(0)));
    }


}
