import org.apache.commons.compress.utils.BitInputStream;
import java.io.*;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

public class AOC16 {

    public static void main(String[] args) throws IOException {
        new AOC16().start(args[0]);
    }

    int rb(BitInputStream bi, int c ) throws IOException { return (int) bi.readBits(c); }
    int rb(BitInputStream bi) throws IOException { return (int)bi.readBits(1); }

    class Result {
        long val; int bits;
        public Result(long v, int b) {
            val = v; bits = b;
        }
        public String toString() { return "v:" + val; }
    }

    private void start(String fn) throws IOException {
        InputStream fi = new FileInputStream(fn);
        BitInputStream bi = new BitInputStream(new FilterInputStream(fi) {
            public int read() throws IOException {
                return Integer.parseInt("" +(char)in.read() + (char)in.read(), 16);
            }
        }, ByteOrder.BIG_ENDIAN);

        Result r = readPacket(bi);
        System.out.println("part1:" + version);
        System.out.println("part2:" + r.val);
    }

    int version = 0;

    private Result readPacket(BitInputStream bi) throws IOException {
        int bits = 0; long res = -1;
        int v = rb(bi, 3); bits += 3;
        System.out.println("version:" + v);
        version += v;
        int t = rb(bi, 3); bits += 3;
        System.out.println("type:" + t);
        if (t == 4) { // literal
            System.out.println("literal");
            Result r = readValue(bi); bits += r.bits;
            System.out.println("val:" + r.val);
            return new Result(r.val, bits);
        }
        // else packet list
        int lt = rb(bi); bits++;
        List<Result> results = new ArrayList<>();
        if (lt == 0) { // count bits
            int l = rb(bi, 15); bits += 15;
            System.out.println("***len:" + l);
            int c = 0;
            do {
                Result u = readPacket(bi); results.add(u); c += u.bits; bits += u.bits;
            } while (c < l);
        } else { // count packages
            int n = rb(bi, 11); bits += 11;
            System.out.println("***cnt:" + n);
            for (int i = 0 ; i < n; i++) {
                Result u = readPacket(bi); results.add(u); bits += u.bits;
            }
        }
        System.out.println("res:" + results);
        if (t < 4) { // aggregate operators
            var x = results.stream().map(s -> s.val);
            switch (t) {
                case 0:
                    System.out.println("sum");
                    res = x.reduce(0l, Long::sum).longValue();
                    break;
                case 1:
                    System.out.println("mul");
                    res = x.reduce(1l, (a, b) -> a * b).longValue();
                    break;
                case 2:
                    System.out.println("min");
                    res = x.min(Long::compare).get().longValue();
                    break;
                case 3:
                    System.out.println("max");
                    res = x.max(Long::compare).get().longValue();
                    break;
            }
        } else { // binary operators
            long l = results.get(0).val, r = results.get(1).val;
            switch(t) {
                case 5:
                    System.out.println("gt");
                    res = l > r ? 1 : 0;
                    break;
                case 6:
                    System.out.println("lt");
                    res = l < r ? 1 : 0;
                    break;
                case 7:
                    System.out.println("eq");
                    res = l == r ? 1 : 0;
                    break;
                default: throw new IllegalArgumentException("unknown type:" + t);
            }
        }
        System.out.println("res:" + res);
        return new Result(res, bits);
    }

    private Result readValue(BitInputStream bi) throws IOException {
        long data = 0; int bits = 0;
        for (;;) {
            long h = rb(bi); bits++;
            data += rb(bi, 4); bits += 4;
            if (h == 0) break;
            data <<=4;
        }
        return new Result(data ,bits);
    }
}