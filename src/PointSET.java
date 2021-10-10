
/****************************************************************************
 *  Compilation:  javac PointSET.java
 *  Execution:    
 *  Dependencies:
 *  Author:
 *  Date:
 *
 *  Data structure for maintaining a set of 2-D points, 
 *    including rectangle and nearest-neighbor queries
 *
 *************************************************************************/

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;

public class PointSET {
    // construct an empty set of points
    private SET<Point2D> the_set;
    public PointSET() {
        the_set = new SET<Point2D>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return the_set.isEmpty();
    }

    // number of points in the set
    public int size() {
        return the_set.size();
    }

    // add the point p to the set (if it is not already in the set)
    public void insert(Point2D p) {
        the_set.add(p);
    }

    // does the set contain the point p?
    public boolean contains(Point2D p) {
        return the_set.contains(p);
    }

    // draw all of the points to standard draw
    public void draw() {
        for(Point2D pos : the_set){
            pos.draw();
        }
    }

    // all points in the set that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        SET<Point2D> inRange;
        inRange = new SET<Point2D>();
        for (Point2D pos : the_set){
            if(rect.contains(pos)){
                inRange.add(pos);
            }
        }
        return inRange;
    }

    // a nearest neighbor in the set to p; null if set is empty
    public Point2D nearest(Point2D p) {
        if(!isEmpty()){
            Point2D nearest = the_set.min();

            for(Point2D pos : the_set){
                if(pos.distanceSquaredTo(p) < nearest.distanceSquaredTo(p)){
                    nearest = pos;
                }
            }
            return nearest;
        }
        return null;
    }

    public static void main(String[] args) {
        PointSET brute = new PointSET();
        Stopwatch w = new Stopwatch();

        int n = 1000; //StdIn.readInt();

        for (int i = 0; i < n; i++){
            brute.insert(new Point2D(StdIn.readDouble(), StdIn.readDouble()));
        }

        StdOut.println(w.elapsedTime() + "s");

        /*
        StdOut.println(brute.nearest(new Point2D(0.04,0.02)));
        StdOut.println(brute.isEmpty());
        brute.insert(new Point2D(0.53,0.48));
        brute.insert(new Point2D(0.88,0.99));
        brute.insert(new Point2D(0.1,0.72));
        brute.insert(new Point2D(0.15,0.13));
        brute.insert(new Point2D(0.02,0.65));
        brute.insert(new Point2D(0.70,0.94));
        brute.insert(new Point2D(0.41,0.89));
        brute.insert(new Point2D(0.83,0.19));
        brute.range(new RectHV(0.5, 0.4, 0.9,0.5));
        StdOut.println(brute.nearest(new Point2D(0.5,0.5)));
        StdOut.println(brute.contains(new Point2D(0.88,0.99)));
        StdOut.println(brute.contains(new Point2D(0.07,0.02)));
        StdOut.println(brute.isEmpty());
        brute.draw();
        */
        
    }
}
