/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lindenmayer;

import displayutils.JPanelLinden;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JComponent;
import uvector.Vector2;

/**
 *
 * @author bowen
 */
public class Turtle2D implements Turtle {
    
    public static double PIO180 = Math.PI / 180d;
    public static double PIU180 = 180d / Math.PI;
    
    private final LinkedList<State2D> stateStack;
    private State2D currentState;
    
    private double moveLength = 1;
    private double turnAngle = Math.PI / 2;
    
    private JPanelLinden component;
    
    private double startWidth = 1;
    private double width = 1;
    
    public Turtle2D() {
        stateStack = new LinkedList<>();
        currentState = new State2D();
    }
    
    public void setComponent(JPanelLinden component) {
        this.component = component;
    }

    @Override
    public void setWidth(int width) {
        this.startWidth = width;
        this.width = width;
        System.out.println(width);
    }
    
    @Override
    public void draw() {
        Vector2 diff = getMovementDifference();
        
        //Draw here
        if (component != null) {
            
            double x0 = currentState.getPosition().getX();
            double y0 = currentState.getPosition().getY();
            
            double x1 = x0 + diff.getX();
            double y1 = y0 + diff.getY();
            
            component.addLine(x0, y0, x1, y1, width);
            if (width < startWidth / 2) {
                component.addLeaf(x1, y1, diff.norm() * Math.sqrt(2d * (double)width / (double)startWidth));
            }
        }
        
        move(diff);
    }
    
    public Vector2 getMovementDifference() {
        return new Vector2(moveLength, 0).rotate(currentState.getAngle());
    }
    
    @Override
    public void move() {
        move(getMovementDifference());
    }
    public void move(Vector2 vector) {
        currentState = State2D.add(currentState, new State2D(vector));
    }

    @Override
    public void turnR() {
        currentState = State2D.sub(currentState, new State2D(turnAngle));
    }

    @Override
    public void turnL() {
        currentState = State2D.add(currentState, new State2D(turnAngle));
    }

    @Override
    public void push() {
        width/=2;
        stateStack.push(currentState);
    }

    @Override
    public void pop() {
        if (!stateStack.isEmpty()) {
            width*=2;
            currentState = stateStack.pop();
        }
    }

    @Override
    public void stay() {
    }

    @Override
    final public void init(Point2D position, double angle_deg) {
        currentState = new State2D(position, angle_deg * PIO180);
    }

    @Override
    public Point2D getPosition() {
        return currentState.getPosition();
    }

    @Override
    public double getAngle() {
        return getAngleRad() * PIU180;
    }
    
    public double getAngleRad() {
        return currentState.getAngle();
    }

    @Override
    public void setUnits(double step, double delta) {
        moveLength = step;
        turnAngle = delta * PIO180;
    }
    
}
