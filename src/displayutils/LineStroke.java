/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package displayutils;

import java.awt.geom.Line2D;

/**
 *
 * @author bowen
 */
public class LineStroke {
    
    private Line2D line;
    private double width;
    
    public LineStroke(Line2D line, double width) {
        this.line = line;
        this.width = width;
    }

    public Line2D getLine() {
        return line;
    }

    public double getWidth() {
        return width;
    }
    
    
}
