import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AOC3 {


    public static void main(String[] args) throws IOException {
        new AOC3().start1(args[0]);
        new AOC3().start2(args[0]);
    }

    private void calc1(int[]zeroes, int[]ones) {
        long gamma =0, epsilon = 0;
        for (int i = 0 ; i < zeroes.length; i++) {
            if (ones[i] > zeroes[i]) gamma |=1;
            else epsilon |=1;
            if (i < zeroes.length-1) { // shift left to make space for next bit except the last time
                gamma <<= 1;
                epsilon <<= 1;
            }
        }
        System.out.println("gamma:" + gamma + ", epsilon:" + epsilon + ":" + gamma * epsilon);
    }



    private void start1(String fn) throws IOException {
        int[] zeroes = null, ones=null;
        try(BufferedReader r = new BufferedReader(new FileReader(fn))) {
            String s = null;
            while ((s = r.readLine()) != null) {
                int l = s.length();
                if (zeroes == null) { zeroes = new int[l]; ones = new int[l]; }
                for (int i = 0; i < l; i++) {
                    char c = s.charAt(i);
                    if (c =='0') zeroes[i]++; else ones[i]++;
                }
            }
        }
        calc1(zeroes, ones);
    }


    int[] zeroes, ones;

    void countBits(List<String> data) {
        int l = data.get(0).length();
        zeroes = new int[l]; ones = new int[l];
        for (String s : data) {
            for (int i = 0 ; i < s.length(); i++) {
                if (s.charAt(i) == '0') zeroes[i] ++; else ones[i] ++;
            }
        }
    }


    private int calc2(List<String>data, boolean oxy) {
        List<String> x = new ArrayList<>(data);
        int col = 0;
        char search;
        while (x.size() > 1) {
            countBits(x);
            if (oxy)
                if (ones[col] >= zeroes[col]) search = '1'; else search ='0';
                else if (zeroes[col] <= ones[col]) search = '0'; else search = '1';

            for (Iterator<String> it = x.iterator(); it.hasNext();) {
                if (it.next().charAt(col) != search) it.remove();
            }
            col++;
        }
        return new BigInteger(x.get(0),2).intValue();
    }


    private void calc2(List<String>data) {
        int oxy = calc2(data,true);
        int co2 = calc2(data, false);
        System.out.println("oxy:" + oxy+ ", co2:" + co2 + ", " + (oxy*co2));
    }

    private void start2(String fn) throws IOException {
        List<String> data = new ArrayList<>();
        try (BufferedReader r = new BufferedReader(new FileReader(fn))) {
            String s = null;
            while ((s = r.readLine()) != null) {
                data.add(s);
            }
        }
        calc2(data);
    }
}
