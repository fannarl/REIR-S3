
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

import java.awt.*;
import java.util.Arrays;

import edu.princeton.cs.algs4.*;

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
    }

}
