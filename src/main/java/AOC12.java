import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class AOC12 {

    public static void main(String[] args) throws IOException {
        new AOC12().start(args[0]);
    }

    class Cave {
        String name;
        Set<Cave> adj = new HashSet<>();
        public Cave(String n) { name = n;}
        public void add(Cave c1) { adj.add(c1); }
        public String toString() { return name + ":[" + adj.stream().map(s->s.name).collect(Collectors.joining(", ")) + "]"; }

        @Override
        public boolean equals(Object o) { if (o instanceof Cave c) return c.name.equals(name); else return false; }

        @Override
        public int hashCode() { return name != null ? name.hashCode() : 0; }
        public boolean isEnd() { return name.equals("end"); }
        public boolean isStart() { return name.equals("start"); }
        public boolean isSmall() { return Character.isLowerCase(name.charAt(0)) && !isEnd() && !isStart(); }
    }

    Map<String, Cave> caves = new HashMap<>();

    private void start(String fn) throws IOException {
        Files.lines(Paths.get(fn)).forEach(s-> {
            var c = Arrays.stream(s.split("-")).map(t->caves.computeIfAbsent(t, $->new Cave(t))).toList();
            c.get(0).add(c.get(1)); c.get(1).add(c.get(0));
        });
        System.out.println(caves);
        process();
    }

    private void process() {
        count(caves.get("start"));
        System.out.println("count:" + count);
        visited.clear(); count =0;
        count2(caves.get("start"));
        System.out.println("count:" + count);
    }

    int count;
    Set<Cave> visited = new HashSet<>();

    private void count(Cave c) {
        if (c.isEnd()) { count++; return; }

        if (c.isSmall()) {
            if (visited.contains(c)) return;
            visited.add(c);
        }
        for (var a : c.adj) if (!a.isStart()) count(a);
        if (c.isSmall()) visited.remove(c);
    }

    Cave twice;
    private void count2(Cave c) {
        if (c.isEnd()) { count++; return; }

        if (c.isSmall()) {
            if (!visited.contains(c)) visited.add(c);
            else if (twice ==null) twice = c;
            else return;
        }
        for (var a : c.adj) if (!a.isStart()) count2(a);
        if (c.isSmall()) {
            if (c==twice) twice = null; else visited.remove(c);
        }
    }
}
