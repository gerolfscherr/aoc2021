import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class AOC10 {

    public static void main(String[] args) throws IOException {
        new AOC10().start(args[0]);
    }

    private void start(String fn) throws IOException {
        Files.lines(Paths.get(fn)).forEach(s -> process(s));
        System.out.println("part1:" + score1);
        Collections.sort(scores2);
        System.out.println("part2:" + scores2.get(scores2.size()/2));
    }

    int score1 = 0;

    List<Long> scores2 = new ArrayList<>();

    private void process(String s) {
        Stack<Character> t = new Stack<>();
        for (char c : s.toCharArray()) {
            if ("([{<".indexOf(c) >=0) {
                t.push(c);
            } else {
                char o = t.pop();
                if (!matches(o,c)) {
                    score1 += getScore(c); return;
                }
            }
        }

        long sc2 = 0;
        while (!t.empty()) {
            sc2 *= 5;
            char c = t.pop();
            sc2 += getScore2(c);
        }
        scores2.add(sc2);
    }

    private int getScore2(char c) {
        switch (c) {
            case '(': return 1;
            case '[': return 2;
            case '{' : return 3;
            case  '<': return 4;
            default: throw new IllegalArgumentException("" + c);
        }
    }

    private int getScore(char c) {
        switch (c) {
            case ')': return 3;
            case ']': return 57;
            case '}' : return 1197;
            case  '>': return 25137;
            default: throw new IllegalArgumentException("" + c);
        }
    }

    private boolean matches(char c, char o) {
        return c == '(' && o == ')' || c == '<' && o == '>' ||c == '{' && o == '}' || c == '[' && o == ']';
    }
}
