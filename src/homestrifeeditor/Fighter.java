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
public class Fighter extends PhysicsObject {
    public ArrayList<HSBox> uprightTerrainBoxes;
    public ArrayList<HSBox> crouchingTerrainBoxes;
    public ArrayList<HSBox> proneTerrainBoxes;
    public ArrayList<HSBox> compactTerrainBoxes;
    public FighterEventHolds fighterEventHolds;
    public float walkSpeed;
    public float runSpeed;
    public float jumpSpeed;
    public float forwardAirDashSpeed;
    public float backwardAirDashSpeed;
    public int forwardAirDashDuration;
    public int backwardAirDashDuration;
    public float stepHeight;
    public int airActions;
    public float airControlAccel;
    public float maxAirControlSpeed;
    
    public Fighter()
    {
        super();
        
        name = "New Fighter";
        uprightTerrainBoxes = new ArrayList<HSBox>();
        crouchingTerrainBoxes = new ArrayList<HSBox>();
        proneTerrainBoxes = new ArrayList<HSBox>();
        compactTerrainBoxes = new ArrayList<HSBox>();
        fighterEventHolds = new FighterEventHolds();
        walkSpeed = 0;
        runSpeed = 0;
        jumpSpeed = 0;
        forwardAirDashSpeed = 0;
        backwardAirDashSpeed = 0;
        forwardAirDashDuration = 0;
        backwardAirDashDuration = 0;
        stepHeight = 0;
        airActions = 0;
        airControlAccel = 0;
        maxAirControlSpeed = 0;
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
    
    @Override
    public boolean IsFighter()
    {
        return true;
    }
}
