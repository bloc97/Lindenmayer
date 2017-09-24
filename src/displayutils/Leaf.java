/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package displayutils;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

/**
 *
 * @author bowen
 */
public class Leaf {
    
    private Point2D point;
    private double radius;
    
    public Leaf(Point2D point, double radius) {
        this.point = point;
        this.radius = radius;
    }

    public Point2D getPoint() {
        return point;
    }

    public double getRadius() {
        return radius;
    }
    
}
