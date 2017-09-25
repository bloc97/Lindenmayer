/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lindenmayer.tree;

import java.awt.geom.Point2D;
import java.util.List;
import lindenmayer.tree.display.Drawable;
import uvector.Vector2;

/**
 *
 * @author bowen
 */
public interface Node {
    public List<Node> getChildrenNodes();
    public List<Node> getAllChildrenNodes();
    
    public int getDepth();
    public int getReverseDepth();
    
    public double getLength();
    public double getAngle();
    
    public double getMass();
    
    public void setParentNode(Node node);
    public void addChildrenNode(Node node);
    public Vector2 getRelativeChildrenStartPosition();
    public Vector2 getStartPosition();
    
    public List<Drawable> getDrawing();
    
}
