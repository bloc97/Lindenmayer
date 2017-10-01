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
import static lindenmayer.Utils.readStringFile;

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
        
        LSystem.readJSONFile("test_json/sierpinski.json", gls, turtle);
        
        Rectangle2D bound = gls.getBoundingBox(turtle, gls.getAxiom(), 6);
        gls.tell(turtle, 6);
        
        panel.unlock();
        panel.start();
        
        if (bound.getHeight() < bound.getWidth()) {
            panel.getCamera().setScale(defaultW / (bound.getWidth()));
        } else {
            panel.getCamera().setScale(defaultH / (bound.getHeight()));
        }
        
        panel.getCamera().setxPos(bound.getCenterX());
        panel.getCamera().setyPos(bound.getCenterY());
        
        
        
    }
    
    
}
