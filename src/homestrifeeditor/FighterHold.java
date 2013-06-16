/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homestrifeeditor;

/**
 *
 * @author Darlos9D
 */
public class FighterHold extends PhysicsObjectHold {
    Cancels cancels;
    boolean changeCancels;
    
    public FighterHold()
    {
        super();
        
        cancels = new Cancels();
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
    
    @Override
    public boolean IsFighterHold()
    {
        return true;
    }
}
