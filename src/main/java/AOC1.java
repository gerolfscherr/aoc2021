import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class AOC1 {

    static void one(String fn) throws IOException {
        try (Scanner r = new Scanner(new File(fn))) {
             int i = r.nextInt();
             int j, sum =0;
             while (r.hasNext()) {
                 j = r.nextInt();
                if (j > i) sum++;
                i = j;
            }
            System.out.println("1:"+ sum);
        }
    }


    static void two(String fn) throws IOException {
        int sum = 0;
        try (Scanner r = new Scanner(new File(fn))) {
            int i = r.nextInt();
            int j = r.nextInt();
            int k = r.nextInt();
            int l;
            while (r.hasNext()) {
                l = r.nextInt();
                if ((j+k+l)> (i+j+k)) sum++;
                i = j;
                j = k;
                k = l;
            }
        }
        System.out.println("2:" + sum);
    }

    public static void main(String[] args) throws IOException {
        one(args[0]);
        two(args[0]);
    }

}
