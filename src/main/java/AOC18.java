import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

class Node {
    int left =-1;
    int right =-1;
    private Node nleft, nright, parent;

    public Node() { }
    Node getLeft() { return nleft; }
    Node getRight() { return nright; }

    void addLeft(Node n) {
        nleft = n; nleft.parent = this;
    }
    void addRight(Node n) {
        nright = n; nright.parent = this;
    }

    public Node(Node l, Node r) {
        addLeft(l); addRight(r);
    }

    public String toString() {
        StringBuilder b = new StringBuilder("[");
        if (nleft != null) b.append(nleft); else b.append(left);
        b.append(",");
        if (nright != null) b.append(nright); else b.append(right);
        b.append(']');
        return b.toString();
    }

    static int nr = -1;
    static Node nrnode;

    public Node getNodeBefore(Node n) {
        nr = -1; nrnode = null;
        findNumberLeftOf(n);
        return nrnode;
    }

    public Node getNodeAfter(Node n) {
        nr = -1; nrnode = null;
        findNumberRightOf(n);
        return nrnode;
    }

    public boolean findNumberLeftOf(Node n) {
        if (this == n) return false;
        if (nleft != null) { if (!nleft.findNumberLeftOf(n)) return false; }
        else { nr = left; nrnode = this; }
        if (nright != null)  {if (!nright.findNumberLeftOf(n)) return false; }
        else { nr = right; nrnode = this;}
        return true;
    }

    public boolean findNumberRightOf(Node n) {
        if (this == n) return false;
        if (nright != null)  {if (!nright.findNumberRightOf(n)) return false; }
        else { nr = right; nrnode = this; }
        if (nleft != null) { if (!nleft.findNumberRightOf(n)) return false; }
        else { nr = left; nrnode = this; }
        return true;
    }

    public void explode(Node root) {
        Node prev = root.getNodeBefore(this);
        if (prev != null) {
            if (prev.right != -1) prev.right += left;
            else if (prev.left != -1) prev.left += left; else throw new IllegalArgumentException("ups1");
        }
        Node next = root.getNodeAfter(this);
        if (next != null) {
            if (next.left != -1)  next.left += right;
            else if (next.right != -1) next.right += right;
            else throw new IllegalArgumentException("ups2");
        }

        if (parent.nleft == this) { parent.left = 0; parent.nleft = null;
        } else if (parent.nright == this) { parent.right = 0; parent.nright = null;
        } else throw new IllegalArgumentException("orphan");
    }

    public int getMagnitude() {
        int m = 0;
        if (left > -1) { m += left * 3;  if (nleft != null) throw new IllegalStateException(); }
        else m += nleft.getMagnitude() * 3;
        if (right > -1) {
            m += right * 2; if (nright != null) throw new IllegalStateException(); }
        else m += nright.getMagnitude() * 2;
        return m;
    }

    public Node getExplodeNode() { return getExplodeNode(this, 0); }

    boolean doExplode() {
        Node n = getExplodeNode();
        if (n == null) return false;
        n.explode(this);
        return true;
    }

    boolean doSplit() {
        Node n = getSplitNode();
        if (n == null) return false;
        n.split();
        return true;
    }

    private Node getExplodeNode(Node n, int i) {
        if (i == 4) return n;
        Node r = null;
        if (n.getLeft() != null) r = getExplodeNode(n.getLeft(), i+1);
        if (r != null) return r;
        if (n.getRight() != null) return getExplodeNode(n.getRight(), i+1);
        return null;
    }

    public Node getSplitNode() {
        if (nleft != null) {
            Node n = nleft.getSplitNode();
            if (n != null) return n;
        }
        if (left > 9) return this;
        if (nright != null) {
            Node n = nright.getSplitNode();
            if (n != null) return n;
        }
        if (right > 9) return this;
        return null;
    }

    static Node createSplitNode(int nr, Node parent) {
        if (nr < 10) throw new IllegalArgumentException("ups");
        Node n = new Node();
        n.left = nr / 2;
        n.right = (int)Math.ceil(nr / 2.0);
        n.parent = parent;
        return n;
    }

    public void split() {
        if (left > 9) {
            nleft = createSplitNode(left, this);
            left = -1;
        } else if (right > 9) {
            nright = createSplitNode(right, this);
            right = -1;
        } else throw new IllegalArgumentException("ups:" + this);
    }
}

class NodeBuilder {
    String s;
    int i;
    char c;

    void read() {
        c = s.charAt(i++);
    }

    int getnum() {
        int r = c - '0';
        read();
        if (Character.isDigit(c)) {
            r = 10 * r + c-'0';
            read();
        }
        return r;
    }

    void match(char x) {
        if (c == x) read(); else throw new IllegalArgumentException("" + x + " vs " + c);
    }

    Node parse() {
        Node me = new Node();
        match('[');
        if (Character.isDigit(c)) me.left = getnum(); else me.addLeft(parse());
        match(',');
        if (Character.isDigit(c)) me.right = getnum(); else me.addRight(parse());
        match(']');
        return me;
    }

     Node parse(String t) {
        s = t + " ";
        i = 0;
        read();
        return parse();
    }
}

public class AOC18 {
    public static void main(String[] args) throws IOException {
        new AOC18(args[0]);
    }

    NodeBuilder nb = new NodeBuilder();

    Node add(Node l, Node r) {
        if (l == null) return r;
        Node n = new Node(l, r);
        while (n.doExplode() || n.doSplit());
        return n;
    }

    public AOC18(String fn) throws IOException {
        testcases();
        var a = Files.lines(Paths.get(fn)).toList();
        Node root = null;
        for (String s : a) root = add(root, nb.parse(s));
        System.out.println("part1:" + root.getMagnitude());
        int max = 0;
        var p = a;
        for (int i = 0 ; i < a.size()-1 ; i++) {
            for (int j = 0 ; j < a.size(); j++) {
                Node l = nb.parse(p.get(i)), r = nb.parse(p.get(j));
                int m = add(l,r).getMagnitude();
                if (m > max) {
                    //  System.out.println("new max lr: " + m);
                    max = m;
                }
                l = nb.parse(p.get(i));
                r = nb.parse(p.get(j));
                m = add(r, l).getMagnitude();
                if (m > max) {
                    // System.out.println("new max rl: " + m);
                    max = m;
                }
            }
        }
        System.out.println("part2:" + max);
        if (max != 4650) throw new IllegalStateException();
    }

    void testExplode(String root, String exp) {
        Node r = nb.parse(root);
        Node e = r.getExplodeNode();
        e.explode(r);
        expect(r, exp);
    }

    void testcases() {
        Node nx = nb.parse("[[[[[9,8],1],2],3],4]");
        Node e = nx.getExplodeNode();
    //    System.out.println("explode: " + e);
        e.explode(nx);
        expect(nx, "[[[[0,9],2],3],4]");
      //  System.out.println(nx);

        nx = nb.parse("[7,[6,[5,[4,[3,2]]]]]");
        e = nx.getExplodeNode();
        e.explode(nx);
        expect(nx, "[7,[6,[5,[7,0]]]]");

        testExplode("[[6,[5,[4,[3,2]]]],1]", "[[6,[5,[7,0]]],3]");
        testExplode("[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]", "[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]");
        testExplode("[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]", "[[3,[2,[8,0]]],[9,[5,[7,0]]]]");
        testExplode("[[[[0,7],4],[7,[[8,4],9]]],[1,1]]", "[[[[0,7],4],[15,[0,13]]],[1,1]]");

        nx = nb.parse("[[[[0,7],4],[15,[0,13]]],[1,1]]");
        Node sp = nx.getSplitNode();
        //System.out.println("split: " + sp);
        sp.split();
        expect(nx, "[[[[0,7],4],[[7,8],[0,13]]],[1,1]]");
        nx.getSplitNode().split();
        expect(nx, "[[[[0,7],4],[[7,8],[0,[6,7]]]],[1,1]]");

        nx.doExplode();
        expect(nx, "[[[[0,7],4],[[7,8],[6,0]]],[8,1]]");
       // System.out.println("split: " + nx);

        Node left = nb.parse("[[[[4,3],4],4],[7,[[8,4],9]]]");
        Node right = nb.parse("[1,1]");
        nx = add(left, right);
        expect(nx, "[[[[0,7],4],[[7,8],[6,0]]],[8,1]]");

        Node root = null;
        var l = Arrays.asList("[1,1]", "[2,2]", "[3,3]", "[4,4]", "[5,5]", "[6,6]");
        for (var s : l) {
            root = add(root, nb.parse(s));
        }
        expect(root, "[[[[5,0],[7,4]],[5,5]],[6,6]]");
        //System.out.println(root);

        String big ="""
                [[[0,[4,5]],[0,0]],[[[4,5],[2,6]],[9,5]]]
                [7,[[[3,7],[4,3]],[[6,3],[8,8]]]]
                [[2,[[0,8],[3,4]]],[[[6,7],1],[7,[1,6]]]]
                [[[[2,4],7],[6,[0,5]]],[[[6,8],[2,8]],[[2,1],[4,5]]]]
                [7,[5,[[3,8],[1,4]]]]
                [[2,[2,2]],[8,[8,1]]]
                [2,9]
                [1,[[[9,3],9],[[9,0],[0,7]]]]
                [[[5,[7,4]],7],1]
                [[[[4,2],2],6],[8,7]]
                """;
        root = null;
        for (String s : big.split("\n")) {
            root = add(root, nb.parse(s));
        }
        expect(root, "[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]");
//        System.out.println(root);
        //System.out.println(big.split("\n").length);

        root = nb.parse("[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]");
        int m = root.getMagnitude();
        if (m != 3488) throw new IllegalStateException("" + m);
  //      System.out.println(m);

        String[] homework = """
                [[[0,[5,8]],[[1,7],[9,6]]],[[4,[1,2]],[[1,4],2]]]
                [[[5,[2,8]],4],[5,[[9,9],0]]]
                [6,[[[6,2],[5,6]],[[7,6],[4,7]]]]
                [[[6,[0,7]],[0,9]],[4,[9,[9,0]]]]
                [[[7,[6,4]],[3,[1,3]]],[[[5,5],1],9]]
                [[6,[[7,3],[3,2]]],[[[3,8],[5,7]],4]]
                [[[[5,4],[7,7]],8],[[8,3],8]]
                [[9,3],[[9,9],[6,[4,9]]]]
                [[2,[[7,7],7]],[[5,8],[[9,3],[0,2]]]]
                [[[[5,2],5],[8,[3,7]]],[[5,[7,5]],[4,4]]]""".split("\n");
        root = null;
        for (String s : homework) root = add(root, nb.parse(s));
    //    System.out.println(root.getMagnitude());

    }

    private void expect(Node nx, String s) {
        String x = nx.toString();
        //System.out.println(x);
        if (!x.equals(s)) throw new IllegalArgumentException("expected: " +s + ", got: " + x);
    }
}
