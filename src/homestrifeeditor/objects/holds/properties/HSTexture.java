/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homestrifeeditor.objects.holds.properties;

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
    
    public HSTexture(HSTexture t) {
    	//Deep copy
    	offset = new HSVect2D(t.offset);
    	depth = t.depth;
    	filePath = new String(t.filePath);
    }
}
