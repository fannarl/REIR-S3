
/*************************************************************************
 *************************************************************************/

import java.util.*;
import java.util.Queue;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Out;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.*;

public class KdTree {
    // construct an empty set of points
    private Node root;
    private int size;
    private Point2D nearest;

    private static class Node{
        private Point2D p;
        private RectHV rect;
        private Node left;
        private Node right;

        public Node(Point2D p, RectHV rect){
            this.left = null;
            this.right = null;
            this.p = p;
            this.rect = rect;
        }

        public boolean tree_check(Point2D p, boolean vertical){
            if ((!vertical && p.x() < this.p.x()) || (vertical && p.y() < this.p.y())){
                return true;
            }
            else{
                return false;
            }
        }

        public int compareTo(RectHV rect) {
            if (!tree_check(p, true)) {
                if (rect.ymax() < p.y()) return 1;
                if (rect.ymin() > p.y()) return -1;
            } else {
                if (rect.xmax() < p.x()) return 1;
                if (rect.xmin() > p.x()) return -1;
            }
            return 0;
        }
    }

    public KdTree() {
        size = 0;
        root = null;
    }

    // is the set empty?
    public boolean isEmpty() {
        return root == null;
    }

    // number of points in the set
    public int size() {
        return size;
    }

    // add the point p to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if(isEmpty()){
            size += 1;
            root = new Node(p, new RectHV(0, 0,1,1));
        }
        else{
            insert(root, p, false, 0.0, 0.0, 1.0,1.0);
        }
    };

    private Node insert(Node x, Point2D p, boolean vertical, double xmin, double ymin, double xmax, double ymax){
        // Insert when node is empty
        if (x == null){
            size++;
            RectHV rect = new RectHV(xmin, ymin, xmax, ymax);
            return new Node(p, rect);
        }
        // If the Node is not equal to one and other.
        if(!p.equals(x.p)){
            // if the current node is vertical
            if(x.tree_check(p, vertical)){
                // compare the x-coordinates
                double compare = p.x() - x.p.x();
                if(compare < 0){
                    x.left = insert(x.left, p, !vertical, xmin, ymin, x.p.x(), ymax);
                }
                else{
                    x. right = insert(x.right, p, !vertical, x.p.x(), ymin, xmax, ymax);
                }
            }
            // Current not is horizontal so we compare the y-coordinates
            else{
                double compare = p.y() - x.p.y();
                if(compare < 0){
                    x.left = insert(x.left, p, !vertical, xmin, ymin, xmax, x.p.y());
                }
                else{
                    x.right = insert(x.right, p, !vertical, xmin, x.p.y(), xmax, ymax);
                }
            }
        }
        return x;
    }

    // does the set contain the point p?
    public boolean contains(Point2D p) {
        Node x = root;
        boolean vertical = false;
        while (x != null){
            if(p.equals(x.p)){
                return true;
            }
            else if(x.tree_check(p, vertical)){
                x = x.left;
            }
            else{
                x = x.right;
            }
            vertical = !vertical;
        }
        return false;
    }

    // draw all of the points to standard draw
    public void draw() {
        draw(root, new RectHV(0, 0, 1, 1), false);
    }

    private void draw(Node n, RectHV rect, boolean Vertical) {
        if (n != null) {
            if (Vertical) {
                draw(n.left, new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), n.p.y()), !Vertical);
                draw(n.right, new RectHV(rect.xmin(), n.p.y(), rect.xmax(), rect.ymax()), !Vertical);
                StdDraw.setPenRadius();
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.line(rect.xmin(), n.p.y(), rect.xmax(), n.p.y());
            } else {
                draw(n.left, new RectHV(rect.xmin(), rect.ymin(), n.p.x(), rect.ymax()), !Vertical);
                draw(n.right, new RectHV(n.p.x(), rect.ymin(), rect.xmax(), rect.ymax()), !Vertical);
                StdDraw.setPenRadius();
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(n.p.x(), rect.ymin(), n.p.x(), rect.ymax());
            }
            StdDraw.setPenRadius(0.012);
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.point(n.p.x(), n.p.y());
        }
        return;
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

        if(rect.contains(x.p)){
            rangeList.add(x.p);
        }
        if(rect.intersects(x.rect)){
            range(x.left, rect, rangeList);
            range(x.right, rect, rangeList);
        }
        else{
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
    }

    // a nearest neighbor in the set to p; null if set is empty
    public Point2D nearest(Point2D p) {
        return p;
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
