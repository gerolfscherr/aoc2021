import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class AOC4 {

    public final static int SZ = 5;

    static class Board implements Cloneable {

        Integer[][] myData = new Integer[SZ][SZ];

        static <T> T[][] deepCopy(T[][] matrix) {
            return Arrays.stream(matrix)
                    .map(arr -> arr.clone())
                    .toArray($ -> matrix.clone());
        }

        public Board(Board b) { myData = deepCopy(b.myData); }
        public Board() { }
        public void setRow(int row, Integer[]d) {
            myData[row] = d;
        }

        public String toString() {
            StringBuilder b = new StringBuilder();
            for (var j : myData) {
                Arrays.stream(j).forEach(x -> {
                    b.append("\t" + (x == null ? "*" : x));
                });
                b.append("\n");
            }
            return b.toString();
        }

        public boolean draw(Integer nr) {
            for (Integer[] r : myData) {
                for (int i = 0; i < r.length; i++) {
                    if (nr == r[i]) {
                        r[i] = null;
                        return true;
                    }
                }
            }
            return false;
        }

        int getScore() {
            int sum = 0;
            for (Integer[] r : myData)
                for (Integer c : r) { if (c != null) sum += c; }
            return sum;
        }

        public int getScore2() {
            return Arrays.stream(myData).flatMap(Arrays::stream).filter(x -> x != null).mapToInt(Integer::intValue).sum();
        }

        public boolean isBingo() {
            boolean bingo = false;
            for (int j = 0 ; j < myData.length; j++) {
                bingo = true;
                for (int i = 0 ; i < myData[j].length; i++) {
                    if (myData[j][i] != null) { bingo = false; break; }
                }
                if (bingo) return bingo;
            }
            // this is copied from above and will break if not square
            for (int j = 0 ; j < myData.length; j++) {
                bingo = true;
                for (int i = 0 ; i < myData[j].length; i++) {
                    if (myData[i][j] != null) { bingo = false; break; }
                }
                if (bingo) return bingo;
            }
            return bingo;
        }

    }

    public static void main(String[] args) throws IOException {
        new AOC4().start(args[0]);
    }

    List<Integer> myDraw;
    List<Board> myBoards = new ArrayList<>(), myBoards2;

    private void start(String arg) throws IOException {

        try (BufferedReader r = new BufferedReader((new FileReader(arg)))) {
            myDraw = Arrays.stream(r.readLine().split(",")).map(Integer::valueOf).collect(Collectors.toList());
            String s = null;
            while ((s = r.readLine()) != null) { // this reads the empty delimiter line
                Board b = new Board();
                myBoards.add(b);
                for (int i = 0 ; i < 5; i++) {
                    s = r.readLine().trim(); // trim wg. starting blank which confuses the RE
                    b.setRow(i, Arrays.stream(s.split(" +")).map(Integer::valueOf).toArray(Integer[]::new));
                }
            }
        }
        myBoards2 = myBoards.stream().map(Board::new).collect(Collectors.toList());
        run();
        run2();
    }

    private void run() {
        for (int nr : myDraw) {
            for (Board b : myBoards) {
                b.draw(nr);
                if (b.isBingo()) {
                    System.out.println("bingo, score:" + b.getScore() * (nr));
                    System.out.println(b);
                    return;
                }
            }
        }
    }

    private void run2() {
        Board last = null;
        int lastnr = -1;
        for (int nr : myDraw) {
            for (Iterator<Board> it = myBoards2.iterator(); it.hasNext();) {
                Board b = it.next();
                b.draw(nr);
                if (b.isBingo()) {
                    it.remove();
                    last = b;
                    lastnr = nr;
                }
            }
        }
        System.out.println("run2:\n" + last + ":\n" + last.getScore() * lastnr);
    }
}
