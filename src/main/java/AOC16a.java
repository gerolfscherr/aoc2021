import java.io.*;
import java.nio.ByteOrder;
import java.util.*;
import java.util.stream.Stream;
import org.apache.commons.compress.utils.BitInputStream;

public class AOC16a {

    public static void main(String[] args) throws IOException {
        for (int i = 0 ; i < 10000; i++)
        new AOC16a().start(args[0]);
    }

    int rb(BitInputStream bi, int c ) throws IOException { return (int) bi.readBits(c); }
    int rb(BitInputStream bi) throws IOException { return (int)bi.readBits(1); }

    static record Result (long val, int bits) {}
    enum TYPE { sum, mul, min, max, lit, gt, lt, eq;
        private static TYPE[] values = values();
        static TYPE value(int ordinal) { return values[ordinal];}
        public static boolean isAggregate(TYPE t) { return t.ordinal() < 4; }
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
        version += rb(bi, 3); bits += 3;
        TYPE t = TYPE.value(rb(bi, 3)); bits += 3;
        //System.out.println("type:" + t + TYPE.values[tt]);
        if (t == TYPE.lit) {
            Result r = readValue(bi); bits += r.bits;
            //System.out.println("val:" + r.val);
            return new Result(r.val, bits);
        }
        int lt = rb(bi); bits++;
        List<Result> results = new ArrayList<>();
        if (lt == 0) { // count bits
            int l = rb(bi, 15); bits += 15;
            int c = 0;
            do {
                Result u = readPacket(bi); results.add(u); c += u.bits; bits += u.bits;
            } while (c < l);
        } else { // count packages
            int n = rb(bi, 11); bits += 11;
            for (int i = 0 ; i < n; i++) {
                Result u = readPacket(bi); results.add(u); bits += u.bits;
            }
        }
        //System.out.println("res:" + results);
        if (TYPE.isAggregate(t)) res = aggregate(t, results.stream().map(s -> s.val));
              else res = binary(t, results.get(0).val,results.get(1).val);
        //System.out.println("res:" + res);
        return new Result(res, bits);
    }

    private long binary(TYPE type, long l, long r) {
        return switch(type) {
            case gt -> l > r ? 1 : 0;
            case lt -> l < r ? 1 : 0;
            case eq -> l == r ? 1 : 0;
            default -> throw new IllegalArgumentException("unknown type:" + type);
        };
    }

    private long aggregate(TYPE type, Stream<Long> x) {
        return switch (type) {
            case sum -> x.reduce(0l, Long::sum).longValue();
            case mul -> x.reduce(1l, (a, b) -> a * b).longValue();
            case min -> x.min(Long::compare).get().longValue();
            case max -> x.max(Long::compare).get().longValue();
            default -> throw new IllegalArgumentException("" + type);
        };
    }

    private Result readValue(BitInputStream bi) throws IOException {
        long data = 0; int bits = 0;
        for (;;) {
            long h = rb(bi); bits++;
            data += rb(bi, 4); bits += 4;
            if (h == 0) break;
            data <<=4;
        }
        return new Result(data, bits);
    }
}