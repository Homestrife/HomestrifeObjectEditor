/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homestrifeeditor.objects.holds.properties;


/**
 *
 * @author Darlos9D
 */
public class SpawnObject {
    public boolean followParent; //whether or not it should follow the parent's movements
    public HSVect2D parentOffset; //initial location in reference to the parent
    public HSVect2D vel; //initial velocity of the object
    public String defFilePath; //path to the file that defines this
    public int delay; //frames to wait before actually spawning the object
    public boolean collideParent; //whether or not it should collide with its parent
    public int number; //how many to spawn
    public boolean useParentPalette;
    
    public SpawnObject (String theFilePath)
    {
        followParent = false;
        parentOffset = new HSVect2D();
        vel = new HSVect2D();
        parentOffset.x = 0;
        parentOffset.y = 0;
        vel.x = 0;
        vel.y = 0;
        defFilePath = theFilePath;
        delay = 0;
        collideParent = false;
        number = 1;
        useParentPalette = false;
    }
}
