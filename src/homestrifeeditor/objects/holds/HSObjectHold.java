/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homestrifeeditor.objects.holds;

import homestrifeeditor.objects.holds.properties.HSAudio;
import homestrifeeditor.objects.holds.properties.HSTexture;
import homestrifeeditor.objects.holds.properties.HSVect2D;
import homestrifeeditor.objects.holds.properties.HSSpawnObject;

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
    public ArrayList<HSSpawnObject> spawnObjects;
    public int duration;
    public int id;
    public int nextHoldId;
    public HSVect2D reposition;
    public HSVect2D velocity;
    public boolean overwriteVelocity;
    
    public HSObjectHold()
    {
        name = "New Hold";
        nextHold = null;
        textures = new ArrayList<HSTexture>();
        audioList = new ArrayList<HSAudio>();
        spawnObjects = new ArrayList<HSSpawnObject>();
        duration = 4;
        id = 0;
        nextHoldId = 0;
        reposition = new HSVect2D();
        velocity = new HSVect2D();
        overwriteVelocity = false;
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
    
    public String getNameOnly() {
    	String numberRemoved = new String(name);
    	
    	numberRemoved = numberRemoved.substring(0, numberRemoved.length() - 4);
    	if(numberRemoved.endsWith("_")) numberRemoved = numberRemoved.substring(0, numberRemoved.length() - 1);
        
    	return numberRemoved;
    }
    
    public int getNumFromName() {
    	int num = -1;
		num = Integer.parseInt(name.substring(name.length() - 4));
		return num;
    }
    
    public String toString() {
    	//String[] split = name.split("_");
    	//return split[split.length - 1];
    	String suffix = "";
    	if(audioList.size() > 0 || spawnObjects.size() > 0) {
    		suffix = " (";
    		if(spawnObjects.size() > 0) {
    			suffix += "S";
    		}
    		if(audioList.size() > 0) {
    			suffix += spawnObjects.size() > 0 ? "|A" : "A";
    		}
    		suffix += ")";
    	}
    	return name + suffix;
    }
}
