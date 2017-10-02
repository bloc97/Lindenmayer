/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package displayutils;

/**
 *
 * @author bowen
 */
public class JFrameLinden extends Viewport {

    //Constuct the frame using a certain x and y size
    public JFrameLinden(int xsize, int ysize) {
        super(xsize, ysize);
        this.setTitle("Lindenmayer");
    }

}
