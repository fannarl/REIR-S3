
/******************************************************************************
 *  Compilation:  javac NearestNeighborVisualizer.java
 *  Execution:    java NearestNeighborVisualizer input.txt
 *  Dependencies: PointST.java KdTreeST.java
 *
 *  Read points from a file (specified as a command-line argument) and
 *  draw to standard draw. Highlight the closest point to the mouse.
 *
 *  The nearest neighbor according to the brute-force algorithm is drawn
 *  in red; the nearest neighbor using the kd-tree algorithm is drawn in blue.
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.StdDraw;

public class NearestNeighborVisualizer {

    public static void main(String[] args) {
        String filename = args[0];
        In in = new In(filename);

        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(-0.25,1.25);
        StdDraw.setYscale(-0.25,1.25);
        StdDraw.show();

        // initialize the two data structures with point from standard input
        PointSET brute = new PointSET();
        KdTree kdtree = new KdTree();
        for (int i = 0; !in.isEmpty(); i++) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
            brute.insert(p);
        }

        while (true) {

            // the location (x, y) of the mouse
            double x = StdDraw.mouseX();
            double y = StdDraw.mouseY();
            Point2D query = new Point2D(x, y);

            // draw all of the points
            StdDraw.clear();
            StdDraw.setPenColor(StdDraw.LIGHT_GRAY);
            StdDraw.setPenRadius();
            StdDraw.square(0.5,0.5,0.5);
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(.01);
            brute.draw();
            // for (Point2D p : brute.points())  p.draw();

            // draw in red the nearest neighbor according to the brute-force algorithm
            StdDraw.setPenRadius(.03);
            StdDraw.setPenColor(StdDraw.RED);
            Point2D bruteAnswer = brute.nearest(query);
            if (bruteAnswer != null)
                bruteAnswer.draw();
            StdDraw.setPenRadius(.02);

            // draw in blue the nearest neighbor according to the kd-tree algorithm
            StdDraw.setPenColor(StdDraw.BLUE);
            Point2D kdtreeAnswer = kdtree.nearest(query);
            if (kdtreeAnswer != null)
                kdtreeAnswer.draw();

            StdDraw.show();
            StdDraw.pause(100);
        }
    }
}
