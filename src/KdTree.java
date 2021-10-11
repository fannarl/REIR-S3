
/*************************************************************************
 *************************************************************************/

import java.util.*;
import java.util.Queue;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Out;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.*;

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
            this.point  = point;
            this.color  = color;
            this.size   = size;
            this.left   = null;
            this.right  = null;
        }

        public int compareTo(Point2D point) {
            if (color) return Point2D.Y_ORDER.compare(this.point, point);
            return Point2D.X_ORDER.compare(this.point, point);
        }

        public int compareTo(RectHV rect) {
            if (color) {
                if (rect.ymax() < point.y()) return 1;
                if (rect.ymin() > point.y()) return -1;
            } else {
                if (rect.xmax() < point.x()) return 1;
                if (rect.xmin() > point.x()) return -1;
            }
            return 0;
        }

        public double distanceTo(Point2D point) {
            if (color) return Math.abs(this.point.y() - point.y());
            return Math.abs(this.point.x() - point.x());
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
        this.nearestDist = Double.MAX_VALUE;
        this.nearest = null;
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
        List<Point2D> rangeList = new ArrayList<>();
        range(root, rect, rangeList);
        return rangeList;
    }

    // Find the points the lie within the rectangle
    private void range(Node x, RectHV rect, List<Point2D> rangeList){
        if (x == null){ return; }

        if(rect.contains(x.point)){
            rangeList.add(x.point);
        }
        int parentRect = x.compareTo(rect);
        if (parentRect == 1){
            range(x.left, rect, rangeList);
        }
        else if (parentRect == -1){
            range(x.right, rect, rangeList);
        }
        else{
            range(x.left, rect, rangeList);
            range(x.right, rect, rangeList);
        }
    }

    private Node nearest;
    private double nearestDist;

    // a nearest neighbor in the set to point; null if set is empty
    public Point2D nearest(Point2D point) {
        if (root == null) return null;

        this.nearest     = null;
        this.nearestDist = Double.MAX_VALUE;

        nearestSearch(point, root);
        return nearest.point;
    }

    private void nearestSearch(Point2D point, Node node) {
        if (node == null) return;

        int cmp = 0;
        double nodePointDist = point.distanceSquaredTo(node.point);

        if (nodePointDist < nearestDist) {
            nearestDist = nodePointDist;
            nearest = node;
        }

        if (node.left == null && node.right == null)
            return;

        cmp = node.compareTo(point);

        if (cmp <= 0)
            nearestSearch(point, node.right);
        if (cmp >= 0)
            nearestSearch(point, node.left);

        double nodeDist = node.distanceTo(point);

        if (nearestDist > (nodeDist * nodeDist)) {
            if (cmp > 0)
                nearestSearch(point, node.right);
            else
                nearestSearch(point, node.left);
        }

        return;
    }

    /*******************************************************************************
     * Test client
     ******************************************************************************/
    public static void main(String[] args) {
        In in = new In();
        Out out = new Out();
        int N = in.readInt(), R = in.readInt(), T = 800;
        RectHV[] rectangles = new RectHV[R];
        KdTree tree = new KdTree();
        out.printf("Inserting %d points into tree\n", N);
        for (int i = 0; i < N; i++) {
            tree.insert(new Point2D(in.readDouble(), in.readDouble()));
        }
        out.printf("tree.size(): %d\n", tree.size());
        out.printf("Testing `range` method, querying %d rectangles\n", R);
        ArrayList<Point2D> range = new ArrayList<Point2D>();
        for (int i = 0; i < R; i++) {
            rectangles[i] = new RectHV(in.readDouble(), in.readDouble(),
                    in.readDouble(), in.readDouble());
            out.printf("Points inside rectangle %s\n", rectangles[i]);
            for (Point2D point : tree.range(rectangles[i])) {
                range.add(point);
            }
            Collections.sort(range);
            for (Point2D point : range) {
                out.printf("%s\n", point);
            }
            range.clear();
        }
        for (int i = 0; i < T; i++) {
            for (int j = 0; j < rectangles.length; j++) {
                tree.range(rectangles[j]);
            }
        }
    }
}
