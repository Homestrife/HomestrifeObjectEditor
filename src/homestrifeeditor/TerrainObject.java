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
public class TerrainObject extends HSObject {
    public ArrayList<HSBox> terrainBoxes;
    public ArrayList<HSAudio> onHitSounds;
    public boolean canBeJumpedThrough;
    public int health;
    public float bounce;
    public float friction;
    public boolean takesTerrainDamage;
    public boolean fragile;
    public TerrainEventHolds terrainEventHolds;
    
    public TerrainObject()
    {
        super();
        
        name = "New Terrain";
        terrainBoxes = new ArrayList<HSBox>();
        onHitSounds = new ArrayList<HSAudio>();
        canBeJumpedThrough = false;
        health = 0;
        bounce = 0;
        friction = 1;
        takesTerrainDamage = false;
        fragile = false;
        terrainEventHolds = new TerrainEventHolds();
    }
    
    @Override
    public boolean IsTerrainObject()
    {
        return true;
    }
}
