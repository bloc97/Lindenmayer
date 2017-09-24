/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lindenmayer.tree.display;

import displayutils.*;
import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

/**
 *
 * @author bowen
 */
public class LineBranch implements Drawable {
    
    private Line2D line;
    private double width;
    
    public LineBranch(Line2D line, double width) {
        this.line = line;
        this.width = width;
    }

    public Line2D getLine() {
        return line;
    }

    public double getWidth() {
        return width;
    }
    
    @Override
    public void draw(Graphics2D g2, Camera camera) {
        int x1 = (int) camera.getScreenX(line.getX1());
        int y1 = (int) camera.getScreenX(line.getY1());
        int x2 = (int) camera.getScreenX(line.getX2());
        int y2 = (int) camera.getScreenX(line.getY2());
        int w  = (int) (int)camera.getScreenR(width);
        g2.setStroke(new BasicStroke(w));
        g2.drawLine(x1, y1, x2, y2);
    }
    
    
}
