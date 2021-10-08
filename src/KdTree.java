
/*************************************************************************
 *************************************************************************/

import java.awt.*;
import java.util.Arrays;

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

        public Node(Point2D p){
            this.left = null;
            this.right = null;
            this.p = p;
        }

        public boolean tree_check(Point2D p, boolean vertical){
            if ((!vertical && p.x() < this.p.x()) || (vertical && p.y() < this.p.y())){
                return true;
            }
            else{
                return false;
            }
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
            root = new Node(p);
        }
        else{
            insert(root, p, false);
        }
    };

    private void insert(Node x, Point2D p, boolean vertical){
        if(!p.equals(x.p)){
            if(x.tree_check(p, vertical)){
                if(x.left == null){
                    size += 1;
                    x.left = new Node(p);
                }
                else{
                    insert(x.left, p, !vertical);
                }
            }
            else{
                if(x.right == null){
                    size += 1;
                    x.right = new Node(p);
                }
                else{
                    insert(x.right, p, !vertical);
                }
            }
        }
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
        return null;
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
        int nrOfRecangles = in.readInt();
        int nrOfPointsCont = in.readInt();
        int nrOfPointsNear = in.readInt();
        RectHV[] rectangles = new RectHV[nrOfRecangles];
        Point2D[] pointsCont = new Point2D[nrOfPointsCont];
        Point2D[] pointsNear = new Point2D[nrOfPointsNear];
        for (int i = 0; i < nrOfRecangles; i++) {
            rectangles[i] = new RectHV(in.readDouble(), in.readDouble(),
                    in.readDouble(), in.readDouble());
        }
        for (int i = 0; i < nrOfPointsCont; i++) {
            pointsCont[i] = new Point2D(in.readDouble(), in.readDouble());
        }
        for (int i = 0; i < nrOfPointsNear; i++) {
            pointsNear[i] = new Point2D(in.readDouble(), in.readDouble());
        }
        KdTree set = new KdTree();
        for (int i = 0; !in.isEmpty(); i++) {
            double x = in.readDouble(), y = in.readDouble();
            set.insert(new Point2D(x, y));
        }
        for (int i = 0; i < nrOfRecangles; i++) {
            // Query on rectangle i, sort the result, and print
            Iterable<Point2D> ptset = set.range(rectangles[i]);
            int ptcount = 0;
            for (Point2D p : ptset)
                ptcount++;
            Point2D[] ptarr = new Point2D[ptcount];
            int j = 0;
            for (Point2D p : ptset) {
                ptarr[j] = p;
                j++;
            }
            Arrays.sort(ptarr);
            out.println("Inside rectangle " + (i + 1) + ":");
            for (j = 0; j < ptcount; j++)
                out.println(ptarr[j]);
        }
        out.println("Contain test:");
        for (int i = 0; i < nrOfPointsCont; i++) {
            out.println((i + 1) + ": " + set.contains(pointsCont[i]));
        }

        out.println("Nearest test:");
        for (int i = 0; i < nrOfPointsNear; i++) {
            out.println((i + 1) + ": " + set.nearest(pointsNear[i]));
        }

        out.println();
    }
}
