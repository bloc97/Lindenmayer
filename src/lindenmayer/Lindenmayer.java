/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lindenmayer;

import displayutils.JFrameLinden;
import displayutils.JPanelLinden;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import javax.swing.JFrame;
import static lindenmayer.Turtle2D.PIO180;
import static lindenmayer.Utils.readStringFile;
import lindenmayer.tree.Node;

/**
 *
 * @author bowen
 */
public class Lindenmayer {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice(); //Trouver la taille de l'ecran
        int width = gd.getDisplayMode().getWidth();
        int height = gd.getDisplayMode().getHeight();
        int defaultW = width*9/10; //Afficher un window un peu plus petit
        int defaultH = height*9/10;
        
        //Creer le Frame, Panel et start le thread leger
        JFrameLinden frame = new JFrameLinden(defaultW, defaultH);
        JPanelLinden panel = new JPanelLinden(defaultW, defaultH);
        frame.add(panel);
        
        panel.setFocusable(true);
        
        Turtle2D turtle = new Turtle2D();
        turtle.setComponent(panel);
        
        LSystem gls = new LSystem();
        
        LSystem.readJSONFile("testTree.json", gls, turtle);
        
        Rectangle2D bound = gls.getBoundingBox(turtle, gls.getAxiom(), 8);
        Node rootNode = gls.getTree(turtle.PIO180 * 25d, 10, turtle.PIO180 * -90, 0, 0, gls.getAxiom(), 7);
        //System.out.println(rootNode);
        panel.setRootNode(rootNode);
        //gls.tell(turtle, 2);
        
        panel.unlock();
        panel.start();
        
        if (bound.getHeight() < bound.getWidth()) {
            panel.getCamera().setScale(defaultH / (bound.getHeight() * 2.2));
        } else {
            panel.getCamera().setScale(defaultW / (bound.getWidth() * 2.2));
        }
        
        
        panel.getCamera().setxPos(bound.getCenterX());
        panel.getCamera().setyPos(bound.getCenterY());
        
        
        
    }
    
    
}
