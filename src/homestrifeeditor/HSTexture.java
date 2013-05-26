/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homestrifeeditor;

import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;

/**
 *
 * @author Darlos9D
 */
public class HSTexture {
    public HSVect2D offset;
    public int depth;
    public String filePath;
    
    public HSTexture (String theFilePath)
    {
        offset = new HSVect2D();
        depth = 0;
        filePath = theFilePath;
    }
}
