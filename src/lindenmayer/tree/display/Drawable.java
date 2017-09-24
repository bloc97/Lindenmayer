/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lindenmayer.tree.display;

import displayutils.Camera;
import java.awt.Graphics2D;

/**
 *
 * @author bowen
 */
public interface Drawable {
    public void draw(Graphics2D g2, Camera camera);
}
