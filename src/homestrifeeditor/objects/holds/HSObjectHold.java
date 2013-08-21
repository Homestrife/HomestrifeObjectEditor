/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homestrifeeditor.objects.holds;

import homestrifeeditor.objects.holds.properties.HSAudio;
import homestrifeeditor.objects.holds.properties.HSTexture;
import homestrifeeditor.objects.holds.properties.SpawnObject;

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
    public ArrayList<SpawnObject> spawnObjects;
    public int duration;
    public int id;
    public int nextHoldId;
    
    public HSObjectHold()
    {
        name = "New Hold";
        nextHold = null;
        textures = new ArrayList<HSTexture>();
        audioList = new ArrayList<HSAudio>();
        spawnObjects = new ArrayList<SpawnObject>();
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
    
    public String toString() {
    	//String[] split = name.split("_");
    	//return split[split.length - 1];
    	return name;
    }
}