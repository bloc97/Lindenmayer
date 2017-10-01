/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package displayutils;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import lindenmayer.LSystem;
import lindenmayer.OpenSimplexNoise;
import lindenmayer.Turtle2D;
import lindenmayer.tree.BranchNode;
import lindenmayer.tree.Node;
import lindenmayer.tree.display.Drawable;

/**
 *
 * @author bowen
 */
public class JPanelLinden extends Scene {
    
    private volatile boolean isReady = false;
    
    private volatile Node rootNode;
            
    private double targetWind = 0;
    private double wind = 0;
    
    private OpenSimplexNoise noise = new OpenSimplexNoise();
    private double noisePos = 0;
    
    private int currentDepth = 0;
    
    public JPanelLinden(int xsize, int ysize) {
        setSize(xsize, ysize);
        this.setLayout(new BorderLayout(0, 0));
        
        this.setBackground(new Color(135, 206, 250));
        
        
        this.addMouseWheelListener(new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                int notches = e.getWheelRotation();
                
                Camera newCamera = camera.copy();

                int xDistanceFromCenter = 0;
                int yDistanceFromCenter = 0;

                xDistanceFromCenter = (e.getX() - getBounds().width/2);
                yDistanceFromCenter = (e.getY() - getBounds().height/2);

                newCamera.addxPos(xDistanceFromCenter);
                newCamera.addyPos(yDistanceFromCenter);
                newCamera.addScale(notches);
                newCamera.addxPos(-xDistanceFromCenter);
                newCamera.addyPos(-yDistanceFromCenter);

                camera.setScale(newCamera.getScale());
                camera.setxPos(newCamera.getxPos());
                camera.setyPos(newCamera.getyPos());
                     
            }
            
        });
        
        this.addKeyListener(new KeyAdapter() {
            
            @Override
            public void keyPressed(KeyEvent e) {
                //System.out.println(e.getKeyChar());
                 switch(e.getKeyCode()) {
                    case KeyEvent.VK_F11:
                        viewport.toggleFullScreen();
                        System.out.println("FullScreen");
                        break;
                    case KeyEvent.VK_SPACE:
                        
                        
                        LSystem gls = new LSystem();
                        Turtle2D turtle = new Turtle2D();

                        
                        try {
                            LSystem.readJSONFile("denseleaf.json", gls, turtle);
                            //LSystem.readJSONFile("tower.json", gls, turtle);
                        } catch (IOException ex) {
                            System.out.println("ERROR");
                        }
                        

                        rootNode = gls.getTree(Turtle2D.PIO180 * 25d, 10, Turtle2D.PIO180 * -90, -0.01, 0.01, gls.getAxiom(), 5);
                        //rootNode = gls.getTree(Turtle2D.PIO180 * 120d, 10, Turtle2D.PIO180 * -90, -0.01, 0.01, gls.getAxiom(), 5);
                        //System.out.println(rootNode);
                        
                        break;
                    case KeyEvent.VK_EQUALS:
                        targetWind += 1;
                        break;
                    case KeyEvent.VK_MINUS:
                        targetWind -= 1;
                        break;
                    case KeyEvent.VK_OPEN_BRACKET:
                        currentDepth--;
                        rootNode.getAllChildrenNodes().forEach(node -> {
                            if (node.getDepth() < currentDepth) {
                                node.setIsGrown(true);
                            } else {
                                node.setIsGrown(false);
                            }
                        });
                        break;
                    case KeyEvent.VK_CLOSE_BRACKET:
                        currentDepth++;
                        rootNode.getAllChildrenNodes().forEach(node -> {
                            if (node.getDepth() < currentDepth) {
                                node.setIsGrown(true);
                            } else {
                                node.setIsGrown(false);
                            }
                        });
                        break;
                    default :
                        break;
                 }
            }
            @Override
            public void keyReleased(KeyEvent e) {
                 switch(e.getKeyCode()) {
                    case KeyEvent.VK_W :
                    default :
                        break;
                 }
            }
        });
        
        this.addMouseMotionListener(new MouseMotionAdapter() {
            
            private int lastX = -1;
            private int lastY = -1;

            @Override
            public void mouseMoved(MouseEvent e) {
                lastX = e.getX();
                lastY = e.getY();
            }
            
            @Override
            public void mouseDragged(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    if (lastX == -1 || lastY == -1) {
                        lastX = e.getX();
                        lastY = e.getY();
                    } else {
                        int changeX = lastX - e.getX();
                        int changeY = lastY - e.getY();
                        camera.addxPos(changeX);
                        camera.addyPos(changeY);
                        lastX = e.getX();
                        lastY = e.getY();
                    }
                }
            }
            
            
        });
        
    }
    
    public void setRootNode(Node node) {
        this.rootNode = node;
    }
    
    public void lock() {
        isReady = false;
    }
    public void unlock() {
        isReady = true;
    }
    public boolean isReady() {
        return isReady;
    }
    
    @Override
    protected void beforePaint() {
        noisePos += 0.01;
        //wind = targetWind * noise.eval(noisePos, 1);
        wind = targetWind * ((noise.eval(noisePos, 1) * 5d/6 + noise.eval(noisePos * 2, 1) * 1d/6) + 0.4);
    }

    @Override
    protected void prePaint() {
        if (rootNode != null) {
            ((BranchNode) rootNode).updateWind(wind);
        }
    }

    @Override
    protected void onPaint(Graphics g) {
        //g.setColor(new Color(0, 75, 0));
        g.setColor(Color.BLACK);
        if (isReady) {
            Graphics2D g2 = (Graphics2D) g;
            
            List<Node> nodes = rootNode.getAllChildrenNodes();
            
            for (Node node : nodes) {
                if (node instanceof BranchNode) {
                    BranchNode shape = (BranchNode) node;
                    shape.drawLeaves(g2, camera);
                }
            }
            for (Node node : nodes) {
                if (node instanceof BranchNode) {
                    Drawable shape = (Drawable) node;
                    shape.draw(g2, camera);
                }
            }
            
        }
    }

    @Override
    protected void postPaint() {
        
    }

    @Override
    protected void afterPaint() {
        
    }
}
