/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lindenmayer;

import java.awt.geom.Point2D;
import uvector.Vector2;
import uvector.Vectors;

/**
 *
 * @author bowen
 */
public class State2D {
    private final Vector2 position;
    private final double angle;
    
    public State2D() {
        this(0);
    }
    public State2D(Vector2 position) {
        this(position, 0);
    }
    public State2D(Point2D position) {
        this(position, 0);
    }
    public State2D(double angle) {
        this(new Vector2(), angle);
    }
    public State2D(Vector2 position, double angle) {
        this.position = position.copy();
        this.angle = angle;
    }
    public State2D(Point2D position, double angle) {
        this.position = new Vector2(position.getX(), position.getY());
        this.angle = angle;
    }
    
    public Point2D getPosition() {
        return new Point2D.Double(position.getX(), position.getY());
    }
    public double getAngle() {
        return angle;
    }
    
    public static State2D add(State2D state1, State2D state2) {
        return new State2D(Vectors.add(state1.position, state2.position), state1.getAngle() + state2.getAngle());
    }
    
    public static State2D sub(State2D state1, State2D state2) {
        return new State2D(Vectors.sub(state1.position, state2.position), state1.getAngle() - state2.getAngle());
    }
    
}
