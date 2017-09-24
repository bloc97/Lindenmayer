/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lindenmayer.tree;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.List;
import lindenmayer.tree.display.Drawable;
import lindenmayer.tree.display.LineBranch;
import uvector.Vector2;

/**
 *
 * @author bowen
 */
public class BranchNode implements Node {
    private Node parentNode;
    private final List<Node> nodes = new LinkedList<>();
    
    private final double length, angle;
    private double totalSavedMass = -1;
    
    public BranchNode(double length, double angle) {
        this.length = length;
        this.angle = angle;
    }
    
    @Override
    public void setParentNode(Node node) {
        this.parentNode = node;
    }
    
    @Override
    public void addChildrenNode(Node node) {
        nodes.add(node);
        node.setParentNode(this);
    }
    
    @Override
    public Vector2 getRelativeChildrenStartPosition() {
        return new Vector2(length, 0).rotate(angle);
    }
    
    @Override
    public Vector2 getStartPosition() {
        if (parentNode != null) {
            return parentNode.getStartPosition().add(parentNode.getRelativeChildrenStartPosition());
        }
        return new Vector2();
    }
    
    @Override
    public double getMass() {
        if (totalSavedMass == -1) { //If mass wasn't set before
            totalSavedMass = getLength(); //This branch's mass is its length
            
            if (!nodes.isEmpty()) { //If has child branches
                for (Node node : nodes) {
                    totalSavedMass += node.getMass(); //Sum their masses
                }
            }
        }
        return totalSavedMass;
    
    }
    
    @Override
    public double getLength() {
        return length;
    }

    @Override
    public List<Node> getChildrenNodes() {
        return nodes;
    }

    @Override
    public double getAngle() {
        return angle;
    }
    
    @Override
    public String toString() {
        if (nodes.isEmpty()) {
            return "" + angle;
        }
        return nodes.toString();
    }

    @Override
    public List<Drawable> getDrawing() {
        LinkedList<Drawable> drawableList = new LinkedList<>();
        
        Vector2 startPosition = getStartPosition();
        Vector2 newRelPos = getRelativeChildrenStartPosition();
        
        drawableList.add(new LineBranch(new Line2D.Double(startPosition.getX(), startPosition.getY(), startPosition.getX() + newRelPos.getX(), startPosition.getY() + newRelPos.getY()), getMass()));
        
        for (Node node : nodes) {
            for (Drawable drawable : node.getDrawing()) {
                drawableList.add(drawable);
            }
        }
        return drawableList;
    }
    
}
