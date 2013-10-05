/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homestrifeeditor.objects.holds;

/**
 *
 * @author Darlos9D
 */
public class PhysicsObjectHold extends TerrainObjectHold {
	public boolean changePhysics;
	public boolean ignoreGravity;
	
    public PhysicsObjectHold()
    {
        super();
        changePhysics = false;
        ignoreGravity = false;
    }
    
    @Override
    public boolean IsTerrainObjectHold()
    {
        return true;
    }
    
    @Override
    public boolean IsPhysicsObjectHold()
    {
        return true;
    }
}
