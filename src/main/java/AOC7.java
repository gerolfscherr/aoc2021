
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class AOC7 {

    public static void main(String[] args) throws IOException {
        List<Integer> l = Arrays.stream(Files.lines(Paths.get(args[0])).findFirst().get().split(",")).mapToInt(Integer::parseInt).boxed().toList();
        start1(l);
        start2(l);
    }

    private static void start2( List<Integer> l) {
        int min = Integer.MAX_VALUE;
        int minindex = 0;
        for (int j = 0 ; j < l.size(); j++) {
            int sum = 0;
            for (int i : l) {
                int diff = Math.abs(i - j);
                sum += diff * (diff + 1) / 2; // gauss formula
            }
     //       System.out.println(j + ": " + "sum:" + sum);
            if (sum < min) {
                min = sum;
                minindex = j;
            }
        }
        System.out.println("min:" + min + " at index: " + minindex);
    }

    private static void start1(List<Integer> l) {
        int min = Integer.MAX_VALUE;
        int minindex = 0;
        for (int j = 0 ; j < l.size(); j++) {
            int sum = 0;
            for (int i : l) {
                sum += Math.abs(i - j);
            }
            if (sum < min) {
                min = sum;
                minindex = j;
            }
        }
        System.out.println("min:" + min + " at index: " + minindex);
    }
}
