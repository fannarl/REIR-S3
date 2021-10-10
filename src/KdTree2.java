

import java.util.Arrays;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.RedBlackBST;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Out;

class KdTree {

    private static final boolean RED   = true;
    private static final boolean BLACK = false;

    private Node root;     // root of the BST
    private int size;

    // BST helper node data type
    private class Node {

        private Point2D point;         // associated data
        private Node left, right;      // links to left and right subtrees
        private boolean color;         // color of parent link
        private int size;              // subtree count

        public Node(Point2D point, boolean color, int size) {
            this.point = point;
            this.color = color;
            this.size = size;
        }

        public int compareTo(Point2D point) {
            if (color) return Point2D.Y_ORDER.compare(this.point, point);
                       return Point2D.X_ORDER.compare(this.point, point);
        }

        public int compareTo(RectHV rect) {
            if (color) {
                if (rect.ymax() < point.y()) return -1;
                if (rect.ymin() > point.y()) return 1;
            } else {
                if (rect.xmax() < point.x()) return -1;
                if (rect.xmin() > point.x()) return 1;
            }
            return 0;
        }
    }

    /***************************************************************************
    *  Node helper methods.
    ***************************************************************************/

    private boolean isRed(Node x) {
        if (x == null) return false;
        return x.color == RED;
    }

    private int size(Node x) {
        if (x == null) return 0;
        return x.size;
    }

    /***************************************************************************
    *  Red-black tree helper functions.
    ***************************************************************************/

    private Node rotateRight(Node h) {
        Node x = h.left;
        h.left = x.right;
        x.right = h;
        x.color = x.right.color;
        x.right.color = RED;
        x.size = h.size;
        h.size = size(h.left) + size(h.right) + 1;
        return x;
    }

    private Node rotateLeft(Node h) {
        Node x = h.right;
        h.right = x.left;
        x.left = h;
        x.color = x.left.color;
        x.left.color = RED;
        x.size = h.size;
        h.size = size(h.left) + size(h.right) + 1;
        return x;
    }

    private void flipColors(Node h) {
        h.color = !h.color;
        h.left.color = !h.left.color;
        h.right.color = !h.right.color;
    }

    // construct an empty set of points
    public KdTree() {
    }

    public int size() {
        return this.size;
        // return size(root);
    }

    public boolean isEmpty() {
        return root == null;
    }

    // add the point point to the set (if it is not already in the set)
    public void insert(Point2D point) {
        root = put(root, point, BLACK);
    };

    private Node put(Node h, Point2D point, Boolean color) {
        if (h == null) {
            size++;
            return new Node(point, !color, 0);
        }
        if (h.point.equals(point)) return h;

        int cmp = h.compareTo(point);

        if (cmp > 0) h.left  = put(h.left, point, h.color);
        else         h.right = put(h.right, point, h.color);

        // fix-up any right-leaning links
        // if (isRed(h.right) && !isRed(h.left))      h = rotateLeft(h);
        // if (isRed(h.left)  &&  isRed(h.left.left)) h = rotateRight(h);
        // if (isRed(h.left)  &&  isRed(h.right))     flipColors(h);
        // h.size = size(h.left) + size(h.right) + 1;

        return h;
    }

    private Node get(Node h, Point2D point) {
        while (h != null) {
            if (h.point.equals(point)) return h;
            int cmp = h.compareTo(point);

            if (cmp > 0) h = h.left;
            else         h = h.right;
        }
        return null;
    }

    // does the set contain the point point?
    public boolean contains(Point2D point) {
        return get(root, point) != null;
    }

    // draw all of the points to standard draw
    public void draw() {
        draw(root);
    }

    private void draw(Node node) {
        if (node == null) return;

        StdDraw.point(node.point.x(), node.point.y());

        draw(node.left);
        draw(node.right);
    }

    // all points in the set that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        return range(root, rect);
    }

    public SET<Point2D> range(Node node, RectHV rect) {
        if (node == null) return new SET<Point2D>();

        SET<Point2D> pointsInRect = new SET<Point2D>();

        if (rect.contains(node.point)) pointsInRect.add(node.point);

        int cmp = node.compareTo(rect);

        if      (cmp > 0) pointsInRect.union(range(node.right, rect));
        else if (cmp < 0) pointsInRect.union(range(node.left, rect));
        else {
            pointsInRect.union(range(node.right, rect));
            pointsInRect.union(range(node.left, rect));
        }

        return pointsInRect;
    }

    // a nearest neighbor in the set to point; null if set is empty
    public Point2D nearest(Point2D point) {
        double dist = point.distanceSquaredTo(root.point);
        return nearest(point, root, dist, root.point, dist);
    }

    private Point2D nearest(Point2D point, Node node, double nodeDist, Point2D nearest, double nearestDist) {
        // StdOut.println(1);
        if (node == null) return nearest;
        // if (node.point.equals(point)) return node.point;
        // StdOut.println(2);
        if (nodeDist < nearestDist) {
            nearestDist = nodeDist;
            nearest = node.point;
        }

        int cmp = node.compareTo(point);

        if (cmp < 0) {
            // StdOut.println(3);
            nearest = nearest(point, node.right, point.distanceSquaredTo(node.right.point), nearest, nearestDist);
        } else {
            // StdOut.println(4);
            nearest = nearest(point, node.left, point.distanceSquaredTo(node.left.point), nearest, nearestDist);
        }



        // StdOut.println(5);

        return nearest;
    }


    /*******************************************************************************
     * Test client
     ******************************************************************************/
    public static void main(String[] args) {
        In in = new In();
        Out out = new Out();
        int N = in.readInt(), C = in.readInt(), T = 50;
        Point2D[] queries = new Point2D[C];
        KdTree tree = new KdTree();
        out.printf("Inserting %d points into tree\n", N);
        for (int i = 0; i < N; i++) {
            tree.insert(new Point2D(in.readDouble(), in.readDouble()));
        }
        out.printf("tree.size(): %d\n", tree.size());
        out.printf("Testing `nearest` method, querying %d points\n", C);

        for (int i = 0; i < C; i++) {
            queries[i] = new Point2D(in.readDouble(), in.readDouble());
            out.printf("%s: %s\n", queries[i], tree.nearest(queries[i]));
        }
        for (int i = 0; i < T; i++) {
            for (int j = 0; j < C; j++) {
                tree.nearest(queries[j]);
            }
        }
    }
}
