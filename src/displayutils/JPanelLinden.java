/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package displayutils;

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
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author bowen
 */
public class JPanelLinden extends Scene {
    
    private final List<Line2D> lineList = new LinkedList<>();
    
    private volatile boolean isReady = false;
    
    public JPanelLinden(int xsize, int ysize) {
        setSize(xsize, ysize);
        this.setLayout(new BorderLayout(0, 0));
        
        
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
    
    public void addLine(double x0, double y0, double x1, double y1) {
        lock();
        lineList.add(new Line2D.Double(x0, y0, x1, y1));
    }
    
    public void clearLines() {
        lineList.clear();
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
        
    }

    @Override
    protected void prePaint() {
        
    }

    @Override
    protected void onPaint(Graphics g) {
        //g.setColor(new Color(0, 75, 0));
        if (isReady) {
            Graphics2D g2 = (Graphics2D) g;
            for (Line2D line : lineList) {
                if (!isReady) {
                    return;
                }
                
                double screenX1 = camera.getScreenX(line.getX1());
                double screenX2 = camera.getScreenX(line.getX2());
                double screenY1 = camera.getScreenY(line.getY1());
                double screenY2 = camera.getScreenY(line.getY2());
                g2.draw(new Line2D.Double(screenX1, screenY1, screenX2, screenY2));
                
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
