/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homestrifeeditor;

import java.util.ArrayList;

/**
 *
 * @author Darlos9D
 */
public class HSObject {
    public String name;
    public ArrayList<HSObjectHold> holds;
    public int lifetime;
    public HSObjectEventHolds hsObjectEventHolds;
    public HSPalette[] palettes;
    public int curPalette;
    
    public HSObject()
    {
        name = "New Graphic";
        holds = new ArrayList<HSObjectHold>();
        lifetime = 0;
        hsObjectEventHolds = new HSObjectEventHolds();
        palettes = new HSPalette[10];
        for(int i = 0; i < palettes.length; i++)
        {
            palettes[i] = new HSPalette();
            palettes[i].name = "Palette " + (i + 1);
        }
        curPalette = 0;
    }
    
    public boolean IsTerrainObject()
    {
        return false;
    }
    
    public boolean IsPhysicsObject()
    {
        return false;
    }
    
    public boolean IsFighter()
    {
        return false;
    }
}
