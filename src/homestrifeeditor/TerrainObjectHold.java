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
public class TerrainObjectHold extends HSObjectHold {
    public ArrayList<HSBox> attackBoxes;
    public ArrayList<HSBox> hurtBoxes;
    public ArrayList<HSAudio> hitAudioList;
    public boolean changeAttackBoxAttributes;
    public Blockability blockability;
    public boolean horizontalDirectionBasedBlock;
    public boolean reversedHorizontalBlock;
    public int damage;
    public int hitstun;
    public int blockstun;
    public HSVect2D force;
    public boolean trips;
    
    public TerrainObjectHold()
    {
        super();
        
        attackBoxes = new ArrayList<HSBox>();
        hurtBoxes = new ArrayList<HSBox>();
        hitAudioList = new ArrayList<HSAudio>();
        changeAttackBoxAttributes = false;
        blockability = Blockability.MID;
        horizontalDirectionBasedBlock = false;
        reversedHorizontalBlock = false;
        damage = 0;
        hitstun = 0;
        blockstun = 0;
        force = new HSVect2D();
        trips = false;
    }
    
    @Override
    public boolean IsTerrainObjectHold()
    {
        return true;
    }
}
