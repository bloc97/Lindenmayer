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
public class BranchNode implements Node, Drawable {
    private Node parentNode;
    private Plant plant;
    private final List<Node> nodes = new LinkedList<>();
    
    
    private final double initialAngle, length;
    private double angle;
    
    private int savedDepth = -1;
    private int savedReverseDepth = -1;
    
    private double totalSavedMass = -1;
    
    private Vector2 savedStartPosition;
    
    double x = Math.random() * 80;
    
    private boolean isGrown = false;
    
    public BranchNode() {
        this(0, 0);
    }
    
    public BranchNode(double length, double angle) {
        this.length = length;
        this.angle = angle;
        this.initialAngle = angle;
    }

    @Override
    public void setIsGrown(boolean isGrown) {
        this.isGrown = isGrown;
    }

    @Override
    public int getDepth() {
        if (savedDepth == -1) {
            if (parentNode == null) {
                savedDepth = 0;
            } else {
                savedDepth = parentNode.getDepth() + 1;
            }
        }
        return savedDepth;
    }
    
    @Override
    public int getReverseDepth() {
        if (savedReverseDepth == -1) {
            if (nodes.isEmpty()) {
                savedReverseDepth = 0;
            } else {
                int maxReverseDepth = 0;
                
                for (Node node : nodes) {
                    int thisReverseDepth = node.getReverseDepth();
                    if (thisReverseDepth > maxReverseDepth) {
                        maxReverseDepth = thisReverseDepth;
                    }
                }
                
                savedReverseDepth = maxReverseDepth + 1;
            }
        }
        return savedReverseDepth;
    }
    
    
    @Override
    public void setParentNode(Node node) {
        this.parentNode = node;
    }

    @Override
    public void setPlant(Plant plant) {
        this.plant = plant;
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
        if (savedStartPosition == null) {
            if (parentNode != null) {
                savedStartPosition = parentNode.getStartPosition().add(parentNode.getRelativeChildrenStartPosition());
            } else {
                savedStartPosition = new Vector2();
            }
        }
        return savedStartPosition.copy();
    }
    
    public void updateStartPosition() {
        for (Node node : getAllChildrenNodes()) {
            if (node instanceof BranchNode) {
                ((BranchNode) node).savedStartPosition = null;
            }
        }
    }
    
    public void updateWind(double windforce) {
        double totalMass = getMass();
        for (Node node : getAllChildrenNodes()) {
            if (node instanceof BranchNode) {
                BranchNode branchNode = (BranchNode) node;
                
                Vector2 targetVector = new Vector2(1, 0).rotate(branchNode.initialAngle).add(windforce * (1 - (Math.sqrt(branchNode.getMass() / totalMass))), 0);
                
                Vector2 newVector = targetVector.copy().div(100 * branchNode.getMass() / totalMass).add(new Vector2(1, 0).rotate(branchNode.angle));
                
                
                branchNode.angle = newVector.getRot();
                branchNode.updateStartPosition();
                
            }
        }
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
    public double getTotalMass() {
        if (parentNode == null) {
            return getMass();
        } else {
            return parentNode.getTotalMass();
        }
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
    public List<Node> getAllChildrenNodes() {
        List<Node> allNodes = new LinkedList<>();
        addAllNodes(this, allNodes);
        return allNodes;
    }
    
    private static void addAllNodes(Node node, List<Node> allNodes) {
        if (node != null) {
            allNodes.add(node);
            List<Node> children = node.getChildrenNodes();
            if (!children.isEmpty()) {
                for (Node child: children) {
                    addAllNodes(child, allNodes);
                }
            }
        }
    }
    
    @Override
    public double getAngle() {
        return angle;
    }
    
    @Override
    public String toString() {
        if (nodes.isEmpty()) {
            return "" + getMass();
        }
        return getMass() + " " + nodes.toString();
    }

    @Override
    public List<Drawable> getDrawing() {
        LinkedList<Drawable> drawableList = new LinkedList<>();
        
        List<Node> allNodes = getAllChildrenNodes();
        
        for (Node node : allNodes) {
        
            if (node.getLength() > 0) {

                Vector2 startPosition = node.getStartPosition();
                Vector2 newRelPos = node.getRelativeChildrenStartPosition();

                double width = Math.sqrt(node.getMass()) / node.getLength();
                if (width < node.getLength()/10) {
                    width = node.getLength()/10;
                }

                drawableList.add(new LineBranch(new Line2D.Double(startPosition.getX(), startPosition.getY(), startPosition.getX() + newRelPos.getX(), startPosition.getY() + newRelPos.getY()), width));

            }
        }
        return drawableList;
    }
    
    @Override
    public void draw(Graphics2D g2, Camera camera) {
        
        if (getLength() <= 0 || !isGrown) {
            return;
        }
        
        Vector2 startPosition = getStartPosition();
        Vector2 newRelPos = getRelativeChildrenStartPosition();
        Vector2 endPosition = getStartPosition().add(newRelPos);
        
        /*
        double width = Math.sqrt(getMass()) / getLength();
        if (width < getLength()/10) {
            width = getLength()/10;
        }
        */
        
        double width = Math.sqrt(getMass() / getTotalMass()) * 20;
        
        int x1 = (int) camera.getScreenX(startPosition.getX());
        int y1 = (int) camera.getScreenY(startPosition.getY());
        int x2 = (int) camera.getScreenX(endPosition.getX());
        int y2 = (int) camera.getScreenY(endPosition.getY());
        int w  = (int) (int)camera.getScreenR(width);
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(w, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_BEVEL));

        g2.drawLine(x1, y1, x2, y2);
    }
    
    public void drawLeaves(Graphics2D g2, Camera camera) {
        
        if (getLength() <= 0 || getReverseDepth() > 0 || !isGrown) {
            return;
        }
        
        Vector2 startPosition = getStartPosition();
        Vector2 newRelPos = getRelativeChildrenStartPosition();//.rotate(0.60);
        //Vector2 newRelPos2 = getRelativeChildrenStartPosition().rotate(0.60);
        Vector2 endPosition = getStartPosition().add(newRelPos);
        //Vector2 endPosition2 = getStartPosition().add(newRelPos2);
        
        
        double width = getLength();
        
        int x1 = (int) camera.getScreenX(startPosition.getX());
        int y1 = (int) camera.getScreenY(startPosition.getY());
        int x2 = (int) camera.getScreenX(endPosition.getX());
        int y2 = (int) camera.getScreenY(endPosition.getY());
        //int x3 = (int) camera.getScreenX(endPosition2.getX());
        //int y3 = (int) camera.getScreenY(endPosition2.getY());
        int w  = (int) (int)camera.getScreenR(width);
        g2.setStroke(new BasicStroke(w, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_BEVEL));
        g2.setColor(new Color(0, 75, 0));
        g2.drawLine(x1, y1, x2, y2);
        //g2.drawLine(x1, y1, x3, y3);
        g2.setColor(Color.BLACK);
        
    }
    
    public void drawDepth(Graphics2D g2, Camera camera) {
        Vector2 startPosition = getStartPosition();
        int x1 = (int) camera.getScreenX(startPosition.getX());
        int y1 = (int) camera.getScreenY(startPosition.getY());
        
        g2.setColor(Color.YELLOW);
        g2.drawString("" + getDepth(), x1, y1);
    }
    
}
