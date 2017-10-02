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
 * IFT2015 Devoir 1, Bowen Peng et Lifeng Wan
 * @author bowen
 */
public class Lindenmayer {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        
        if (args.length == 0) {
            System.out.println("You must specify a L-System JSON file in the argument");
            return;
        }
        
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice(); //Find the screen size
        int width = gd.getDisplayMode().getWidth();
        int height = gd.getDisplayMode().getHeight();
        int defaultW = width;
        int defaultH = height*19/20; //Show a window that is a bit shorter than full-screen
        
        //Build the Frame, Panel and light thread
        JFrameLinden frame = new JFrameLinden(defaultW, defaultH);
        JPanelLinden panel = new JPanelLinden(defaultW, defaultH);
        frame.add(panel);
        
        panel.setFocusable(true);
        
        Turtle2D turtle = new Turtle2D();
        turtle.setComponent(panel);
        
        LSystem gls = new LSystem();
        
        LSystem.readJSONFile(args[0], gls, turtle);
        
        Rectangle2D bound = gls.getBoundingBox(turtle, gls.getAxiom(), 6);
        gls.tell(turtle, 6);
        
        panel.unlock();
        panel.start();
        
        //Set the camera to the correct scale
        if (defaultH < defaultW) {
            panel.getCamera().setScale(defaultH / (bound.getHeight() * 1.2));
        } else {
            panel.getCamera().setScale(defaultW / (bound.getWidth() * 1.2));
        }
        
        panel.getCamera().setxPos(bound.getCenterX());
        panel.getCamera().setyPos(bound.getCenterY());
        
        
        
    }
    
    
}
