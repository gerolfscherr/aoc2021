import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class AOC2 {


    public static void main(String[] args) throws FileNotFoundException {
        new AOC2().start1(args[0]);
        new AOC2().start2(args[0]);
    }

    private void start1(String fn) throws FileNotFoundException {
        int x = 0, y =0;
        try(Scanner sc = new Scanner(new File(fn))) {
            while (sc.hasNext()) {
                char dir = sc.next().charAt(0);
                int nr = sc.nextInt();
                switch(dir) {
                    case 'd': y+=nr; break;
                    case 'u': y-=nr; break;
                    case 'f': x+=nr; break;
                    default: throw new IllegalArgumentException("" + dir);
                }
             //   System.out.print("dir:" + dir + ":" + nr);
             //   System.out.println("  " +i + ":" +dir +  ", x" + x + ", y:" + y + "," + x*y);
            }
            System.out.println("x" + x + ", y:" + y + "," + x*y);
        }
    }


    private void start2(String fn) throws FileNotFoundException {
        int x = 0, y =0, aim=0;
        try(Scanner sc = new Scanner(new File(fn))) {
            while (sc.hasNext()) {
                char dir = sc.next().charAt(0);
                int nr = sc.nextInt();
                switch(dir) {
                    case 'd': aim+=nr; break;
                    case 'u': aim-=nr; break;
                    case 'f': x+=nr; y+=aim*nr; break;
                    default: throw new IllegalArgumentException("" + dir);
                }
                //   System.out.print("dir:" + dir + ":" + nr);
                //   System.out.println("  " +i + ":" +dir +  ", x" + x + ", y:" + y + "," + x*y);
            }
            System.out.println("x" + x + ", y:" + y + "," + x*y);
        }
    }
}
