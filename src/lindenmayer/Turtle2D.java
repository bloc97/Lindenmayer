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
 * IFT2015 Devoir 1, Bowen Peng et Lifeng Wan
 * @author bowen, lifeng
 */
public class Turtle2D implements Turtle {
    
    public static double PIO180 = Math.PI / 180d;
    public static double PIU180 = 180d / Math.PI;
    
    private final LinkedList<State2D> stateStack;
    private State2D currentState;
    
    private double moveLength = 1;
    private double turnAngle = Math.PI / 2;
    
    private JPanelLinden component;
    
    public Turtle2D() {
        stateStack = new LinkedList<>();
        currentState = new State2D();
    }
    
    /**
     * Sets a JPanel to draw with
     * @param component Component to draw with
     */
    public void setComponent(JPanelLinden component) {
        this.component = component;
    }
    
    @Override
    public void draw() {
        Vector2 diff = getMovementDifference();
        
        //Draw if there is a component
        if (component != null) {
            
            double x0 = currentState.getPosition().getX();
            double y0 = currentState.getPosition().getY();
            
            double x1 = x0 + diff.getX();
            double y1 = y0 + diff.getY();
            
            component.addLine(x0, y0, x1, y1);
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
    
    /**
     * Moves turtle by the amount of the 2-vector
     * @param vector Relative position as a 2-vector
     */
    public void move(Vector2 vector) {
        currentState = State2D.add(currentState, new State2D(vector));
    }
    
    @Override
    public void turnL() {
        currentState = State2D.add(currentState, new State2D(turnAngle));
    }
    
    @Override
    public void turnR() {
        currentState = State2D.sub(currentState, new State2D(turnAngle));
    }

    @Override
    public void push() {
        stateStack.push(currentState);
    }

    @Override
    public void pop() {
        if (!stateStack.isEmpty()) {
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
    
    /**
     * @return Angle in radians
     */
    public double getAngleRad() {
        return currentState.getAngle();
    }

    @Override
    public void setUnits(double step, double delta) {
        moveLength = step;
        turnAngle = delta * PIO180;
    }

    @Override
    public Turtle getEmptyClone() {
        Turtle turtle = new Turtle2D();
        turtle.init(getPosition(), getAngle());
        turtle.setUnits(moveLength, turnAngle / PIO180);
        return turtle;
    }
    
}
