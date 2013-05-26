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
public class HSObjectHold {
    public String name;
    public HSObjectHold nextHold;
    public ArrayList<HSTexture> textures;
    public ArrayList<HSAudio> audioList;
    public int duration;
    public int id;
    public int nextHoldId;
    
    public HSObjectHold()
    {
        name = "New Hold";
        nextHold = null;
        textures = new ArrayList<HSTexture>();
        audioList = new ArrayList<HSAudio>();
        duration = 4;
        id = 0;
        nextHoldId = 0;
    }
    
    public boolean IsHSObjectHold()
    {
        return true;
    }
    
    public boolean IsTerrainObjectHold()
    {
        return false;
    }
    
    public boolean IsPhysicsObjectHold()
    {
        return false;
    }
    
    public boolean IsFighterHold()
    {
        return false;
    }
}
