import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AOC6 {

    public static void main(String[] args) throws IOException {
        new AOC6().start(args[0]);
        new AOC6().start2(args[0]);
    }

    private void start(String fn) throws IOException {
        var in =Arrays.stream(Files.lines(Paths.get(fn)).toList().get(0).split(",")).mapToInt(Integer::parseInt).boxed().toList();
        List<Integer> out = null;
        for (int j = 1 ; j <= 80; j++) {
            out = new ArrayList<>();
            for (int i : in) {
                if (i == 0) {
                    out.add(6);
                    out.add(8);
                } else out.add(i - 1);
            }
            in = out;
            System.out.println("day:" + j + "\t" + out.size());
        }
    }

    private void start2(String fn) throws IOException {
        var in =Arrays.stream(Files.lines(Paths.get(fn)).toList().get(0).split(",")).mapToInt(Integer::parseInt).boxed().toList();
        long[] state = new long[9];
        for (int i : in) state[i]++;
        for (int i = 0 ; i < 256; i++) {
            long z = state[0];
            for (int j = 0 ; j < state.length-1; j++) {
                state[j] = state[j+1];
            }
            state[6] += z;
            state[8] = z;
        }
        System.out.println(Arrays.stream(state).sum());
    }

}
