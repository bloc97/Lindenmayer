/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lindenmayer.tree;

import displayutils.Camera;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import uvector.Vector2;

/**
 *
 * @author bowen
 */
public class LeafNode extends BranchNode {
    
    public LeafNode(double length, double angle) {
        super(length, angle);
    }
    
    public void drawLeaves(Graphics2D g2, Camera camera) {
        
        if (getLength() <= 0) {
            return;
        }
        
        Vector2 startPosition = getStartPosition();
        Vector2 newRelPos = getRelativeChildrenStartPosition().rotate(-0.35);
        Vector2 newRelPos2 = getRelativeChildrenStartPosition().rotate(0.35);
        Vector2 endPosition = getStartPosition().add(newRelPos);
        Vector2 endPosition2 = getStartPosition().add(newRelPos2);
        
        
        double width = getLength();
        
        int x1 = (int) camera.getScreenX(startPosition.getX());
        int y1 = (int) camera.getScreenY(startPosition.getY());
        int x2 = (int) camera.getScreenX(endPosition.getX());
        int y2 = (int) camera.getScreenY(endPosition.getY());
        int x3 = (int) camera.getScreenX(endPosition2.getX());
        int y3 = (int) camera.getScreenY(endPosition2.getY());
        int w  = (int) (int)camera.getScreenR(width);
        g2.setStroke(new BasicStroke(w, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_BEVEL));
        g2.setColor(new Color(0, 75, 0));
        g2.drawLine(x1, y1, x2, y2);
        g2.drawLine(x1, y1, x3, y3);
        g2.setColor(Color.BLACK);
        
    }
    
    
}
