import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class AOC8 {
    Map<Integer, String> map;
    int part1, part2;

    public static void main(String[] args) throws IOException {
        new AOC8().start(args[0]);
    }

    private void start(String fn) throws IOException {
        Files.lines(Paths.get(fn)).map( s-> s.split(" \\| ")).forEach(
                kv -> {
                    var a = Arrays.stream(kv).map( s-> {
                                return Arrays.stream(s.split(" ")).map(t-> {
                                    char[] x = t.toCharArray();
                                    Arrays.sort(x);
                                    return new String(x);
                                }).toList();
                    }).toList();
                    process(a.get(0), a.get(1));
                });
        System.out.println("part1:" + part1);
        System.out.println("part2:" + part2);
    }

    private void process(List<String> input, List<String> output) {
        String s1 = null, s3=null, s7 = null,  s8=null, s6 = null;
        String s73 = null, sleft = null, stopright=null;
        //System.out.println("in/out:" + input + "\t" +output);
        map = new HashMap<>();
        while (map.size() < 10) {
            for (String s : input) {
                switch (s.length()) {
                    // the easy ones
                    case 2: s1 = s; map.put(1, s); break;
                    case 3: s7 = s; map.put(7,s);  break;
                    case 4:         map.put(4,s);  break;
                    case 7: s8 = s; map.put(8,s);  break;
                    // the not so easy ones
                    case 5:
                        if (s1 != null && containsAll(s, s1)) { s3= s; map.put(3,s); break; }
                        if (stopright != null) {
                            if (containsAll(s, stopright)) {  map.put(2,s); break; }
                            else {  map.put(5,s); break; }
                        }
                    break;
                    case 6:
                        if (s73 != null) {
                            if (!containsAll(s, s73)) {
                                 map.put(0, s); break;
                            }
                            if (sleft != null) {
                                if (containsAll(s, sleft)) {
                                    s6 = s; map.put(6,s); break;
                                } else {
                                    map.put(9,s); break;
                                }
                            }
                        }
                    break;
                }
                if (s7 != null && s3 != null) { s73 = minus(s3, s7); }
                if (s8 != null && s3 != null) { sleft = minus(s8, s3); } // eigentlich würde nur s3 ausreichen, weil s8 trivial ist
                if (s6 != null && s8 != null) { stopright = minus(s8, s6); } // -"-
            }
        }
      //  System.out.println(map);
        decode(output);
    }

    private void decode(List<String> output) {
        List l = Arrays.asList(map.get(1), map.get(4), map.get(7), map.get(8));
        int nr = 0;
        var inv = map.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
        for (String s : output) {
            if (l.contains(s)) part1++;
            nr += inv.get(s);
            nr *= 10;
        }
        nr /= 10;
        System.out.println("nr:" + nr);
        part2 += nr;
    }

    private String minus(String first, String second) {
       StringBuilder b = new StringBuilder();
       for (char c: first.toCharArray()) {
           if (second.indexOf(c) >=0) continue;
           b.append(c);
       }
       return b.toString();
    }

    // return s true if s contails all chars of s1
    private boolean containsAll(String s, String s1) {
        if (s1 == null) return false;
        for (char c : s1.toCharArray()) { if (s.indexOf(c) <0) return false; }
        return true;
    }
}
