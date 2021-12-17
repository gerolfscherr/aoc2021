import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

public class AOC17 {
    public static void main(String[] args) throws IOException {
        new AOC17();
    }

    public AOC17() throws IOException {
        part1();
        part2();
    }


    void part2() {
        Set<String> x = new HashSet<>();
        int max = -1;
        for (int j = -200; j < 200; j++) {
            for (int i = 0; i < 200; i++) {
                int r = run(i,j);
                //System.out.println("i:" + i + ", j:" + j + "->" + r);
                if (r == -1) continue;
                x.add(i + ":" + j);
                /*if (r > max) {
                    max = r;
                    System.out.println("new max: " + max + " at " + i + ", " + j);
                }*/
            }
        }
        //System.out.println(x);
        System.out.println(x.size());
    }


    void part1() {
        int max = -1;
        for (int j = -10; j < 1000; j++) {
            for (int i = 1; i < 1000; i++) {
                int r = run(i,j);
                //System.out.println("i:" + i + ", j:" + j + "->" + r);
                if (r == -1) continue;
                if (r > max) {
                    max = r;
        //            System.out.println("new max: " + max + " at " + i + ", " + j);
                }
            }
        }
        System.out.println(max);
    }

    private int run(int xv, int yv) {
     //   int xmin = 20, xmax = 30; int ymin = -10, ymax = -5;
        int xmin = 96, xmax = 125, ymin = -144,ymax = -98;

        boolean good = false;
        int yh = 0;
        int x = 0, y = 0;
        for (; ; ) {
            x += xv;
            y += yv;
            if (y> yh) yh = y;
            if (xv > 0) xv--;
            yv--;

            if (x >= xmin && x <= xmax && y >= ymin && y <= ymax) {
                good = true;
                break;
            }
            if (x > xmax || y < ymin) break;
        }
        if (!good) return -1;
        return yh;
    }

}
