/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lindenmayer;

import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.List;
import uvector.Vector2;

/**
 *
 * @author bowen
 */
public class Turtle2D implements Turtle {
    
    private final LinkedList<State2D> stateStack;
    private State2D currentState;
    
    private double moveLength = 1;
    private double turnAngle = 90;
    
    public Turtle2D() {
        stateStack = new LinkedList<>();
        currentState = new State2D();
    }
    
    @Override
    public void draw() {
        Vector2 diff = getMovementDifference();
        
        //Draw here
        
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
        currentState = new State2D(position, angle_deg);
    }

    @Override
    public Point2D getPosition() {
        return currentState.getPosition();
    }

    @Override
    public double getAngle() {
        return currentState.getAngle();
    }

    @Override
    public void setUnits(double step, double delta) {
        moveLength = step;
        turnAngle = delta;
    }
    
}
