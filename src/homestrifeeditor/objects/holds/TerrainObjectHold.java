/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homestrifeeditor.objects.holds;

import homestrifeeditor.objects.holds.properties.Blockability;
import homestrifeeditor.objects.holds.properties.HSAudio;
import homestrifeeditor.objects.holds.properties.HSBox;
import homestrifeeditor.objects.holds.properties.HSVect2D;

import java.util.ArrayList;

/**
 *
 * @author Darlos9D
 */
public class TerrainObjectHold extends HSObjectHold {
    public ArrayList<HSBox> attackBoxes;
    public ArrayList<HSBox> hurtBoxes;
    public ArrayList<HSAudio> hitAudioList;
    public ArrayList<HSAudio> blockedAudioList;
    public boolean changeAttackBoxAttributes;
    public Blockability blockability;
    public boolean horizontalDirectionBasedBlock;
    public boolean reversedHorizontalBlock;
    public int damage;
    public int ownHitstop;
    public int victimHitstop;
    public int hitstun;
    public int blockstun;
    public HSVect2D force;
    public boolean trips;
    public boolean resetHits;
    
    public TerrainObjectHold()
    {
        super();
        
        attackBoxes = new ArrayList<HSBox>();
        hurtBoxes = new ArrayList<HSBox>();
        hitAudioList = new ArrayList<HSAudio>();
        blockedAudioList = new ArrayList<HSAudio>();
        changeAttackBoxAttributes = false;
        blockability = Blockability.MID;
        horizontalDirectionBasedBlock = false;
        reversedHorizontalBlock = false;
        damage = 0;
        ownHitstop = 0;
        victimHitstop = 0;
        hitstun = 0;
        blockstun = 0;
        force = new HSVect2D();
        trips = false;
        resetHits = false;
    }
    
    @Override
    public boolean IsTerrainObjectHold()
    {
        return true;
    }
}
