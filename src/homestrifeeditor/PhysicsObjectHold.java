/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homestrifeeditor;

/**
 *
 * @author Darlos9D
 */
public class PhysicsObjectHold extends TerrainObjectHold {
    public PhysicsObjectHold()
    {
        super();
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
