/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homestrifeeditor.objects.holds;

import homestrifeeditor.objects.holds.properties.Cancels;

/**
 *
 * @author Darlos9D
 */
public class FighterHold extends PhysicsObjectHold {
    public Cancels cancels;
    public boolean changeCancels;

    public boolean changeFighterAttributes;
    public boolean disableAirControl;
    public boolean endAirDash;
    
    public FighterHold()
    {
        super();
        
        cancels = new Cancels();
        endAirDash = false;
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
