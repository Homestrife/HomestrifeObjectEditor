/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homestrifeeditor.objects;

/**
 *
 * @author Darlos9D
 */
public class PhysicsObject extends TerrainObject {
    public float mass;
    public boolean falls;
    public float maxFallSpeed;
    
    public PhysicsObject()
    {
        super();
        
        name = "New Physics Object";
        mass = 0;
        falls = false;
        maxFallSpeed = 0;
    }
    
    @Override
    public boolean IsTerrainObject()
    {
        return true;
    }
    
    @Override
    public boolean IsPhysicsObject()
    {
        return true;
    }
}
