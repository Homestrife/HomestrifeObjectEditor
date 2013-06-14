/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homestrifeeditor;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

/**
 * This window is for assigning holds to various object events, such as a fighter attacking
 * @author Darlos9D
 */
public class EventHoldsWindow extends JFrame implements ActionListener, ItemListener {
    private static int windowWidth = 920;
    private static int windowHeightGeneral = 160;
    private static int windowHeightTerrain = 210;
    private static int windowHeightPhysics = 210;
    private static int windowHeightFighter = 500;
    private static int windowBorderBuffer = 10;
    
    private static int gridWidth = 700;
    private static int gridRowHeight = 45;
    private static int gridColumns = 4;
    private static int gridHorizontalGap = 10;
    private static int gridVerticalGap = 5;
    
    private HoldListWindow parent;
    private HSObject hsObject;
    
    private JComboBox lifetimeDeath;
    
    private JComboBox healthDeath;
    
    private JComboBox standing;
    private JComboBox turn;
    private JComboBox walk;
    private JComboBox walking;
    private JComboBox walkingTurn;
    private JComboBox run;
    private JComboBox running;
    private JComboBox runningTurn;
    private JComboBox runningStop;
    private JComboBox crouch;
    private JComboBox crouching;
    private JComboBox crouchingTurn;
    private JComboBox stand;

    private JComboBox jumpNeutralStart;
    private JComboBox jumpNeutralStartAir;
    private JComboBox jumpNeutralRising;
    private JComboBox jumpNeutralFall;
    private JComboBox jumpNeutralFalling;
    private JComboBox jumpNeutralLand;
    private JComboBox jumpNeutralLandHard;
    private JComboBox jumpForwardStart;
    private JComboBox jumpForwardRising;
    private JComboBox jumpForwardFall;
    private JComboBox jumpForwardFalling;
    private JComboBox jumpForwardLand;
    private JComboBox jumpForwardLandHard;
    private JComboBox jumpBackwardStart;
    private JComboBox jumpBackwardRising;
    private JComboBox jumpBackwardFall;
    private JComboBox jumpBackwardFalling;
    private JComboBox jumpBackwardLand;
    private JComboBox jumpBackwardLandHard;

    private JComboBox airDashForward;
    private JComboBox airDashBackward;
    
    private JComboBox blockHigh;
    private JComboBox blockLow;
    private JComboBox blockAir;

    private JComboBox ledgeGrab;
    private JComboBox ledgeClimb;

    private JComboBox hitstunHighStandingStart;
    private JComboBox hitstunLightHighStanding;
    private JComboBox hitstunHighStandingEnd;
    private JComboBox hitstunLightMidStanding;
    private JComboBox hitstunLowStandingStart;
    private JComboBox hitstunLightLowStanding;
    private JComboBox hitstunLowStandingEnd;
    private JComboBox hitstunCrouchingStart;
    private JComboBox hitstunLightMidCrouching;
    private JComboBox hitstunCrouchingEnd;
    private JComboBox hitstunLightLowCrouching;
    private JComboBox hitstunLightAir;
    private JComboBox tripForward;
    private JComboBox tripBackward;
    private JComboBox proneFaceUp;
    private JComboBox proneFaceUpStand;
    private JComboBox proneFaceDown;
    private JComboBox proneFaceDownStand;
    private JComboBox crumpleStart;
    private JComboBox crumple;

    private JComboBox airbornFaceUpRising;
    private JComboBox airbornFaceUpFall;
    private JComboBox airbornFaceUpFalling;
    private JComboBox airbornFaceUpRise;
    private JComboBox airbornFaceDownRising;
    private JComboBox airbornFaceDownFall;
    private JComboBox airbornFaceDownFalling;
    private JComboBox airbornFaceDownRise;
    private JComboBox airbornFaceSideMovingForward;
    private JComboBox airbornFaceSideMoveBackward;
    private JComboBox airbornFaceSideMovingBackward;
    private JComboBox airbornFaceSideMoveForward;
    
    private JComboBox attackLightNeutralGround;
    private JComboBox attackLightDownGround;
    private JComboBox attackLightUpGround;
    private JComboBox attackLightForwardGround;
    private JComboBox attackLightQCFGround;
    private JComboBox attackLightNeutralAir;
    private JComboBox attackLightDownAir;
    private JComboBox attackLightUpAir;
    private JComboBox attackLightForwardAir;
    private JComboBox attackLightBackwardAir;
    private JComboBox attackLightQCFAir;
    
    private JComboBox attackHeavyNeutralGround;
    private JComboBox attackHeavyDownGround;
    private JComboBox attackHeavyUpGround;
    private JComboBox attackHeavyForwardGround;
    private JComboBox attackHeavyQCFGround;
    private JComboBox attackHeavyNeutralAir;
    private JComboBox attackHeavyDownAir;
    private JComboBox attackHeavyUpAir;
    private JComboBox attackHeavyForwardAir;
    private JComboBox attackHeavyBackwardAir;
    private JComboBox attackHeavyQCFAir;
    
    private JComboBox knockout;
    
    private JButton applyButton;
    
    public EventHoldsWindow(HoldListWindow theParent, HSObject theHSObject)
    {
        parent = theParent;
        hsObject = theHSObject;
        
        setTitle("Event Holds - " + hsObject.name);
        setSize(windowWidth, windowHeightGeneral);
        setLocationRelativeTo(null);
        this.setResizable(false);
        
        createWindowContents();
    }
    
    private JComboBox createHoldComboBox(HoldComboBoxRenderer renderer, HSObjectHold[] allHoldsPlusNull, HSObjectHold hold)
    {
        JComboBox holdCombo = new JComboBox(allHoldsPlusNull);
        holdCombo.setRenderer(renderer);
        if(hold == null)
        {
            holdCombo.setSelectedIndex(0);
        }
        else
        {
            holdCombo.setSelectedItem(hold);
        }
        holdCombo.setActionCommand("fieldChanged");
        holdCombo.addItemListener(this);
        
        return holdCombo;
    }
    
    private void createWindowContents()
    {
        HoldComboBoxRenderer holdComboRenderer = new HoldComboBoxRenderer();
        HSObjectHold[] allHolds = parent.getAllHolds();
        HSObjectHold[] allHoldsPlusNull = new HSObjectHold[allHolds.length + 1];
        HSObjectHold nullHold = new HSObjectHold();
        nullHold.name = "NONE";
        allHoldsPlusNull[0] = nullHold;
        for (int i = 0; i < allHolds.length; i++)
        {
            allHoldsPlusNull[i + 1] = allHolds[i];
        }
        
        JLabel lifetimeDeathLabel = new JLabel("Lifetime Death");
        lifetimeDeath = createHoldComboBox(holdComboRenderer, allHoldsPlusNull, hsObject.hsObjectEventHolds.lifetimeDeath);
        
        JPanel graphicInterface = new JPanel(new GridLayout(1, gridColumns, gridHorizontalGap, gridVerticalGap));
        graphicInterface.setSize(gridWidth, gridRowHeight);
        graphicInterface.setBorder(new TitledBorder("General Events"));
        graphicInterface.add(lifetimeDeathLabel);
        graphicInterface.add(lifetimeDeath);
        graphicInterface.add(new JLabel(""));
        graphicInterface.add(new JLabel(""));
        
        JPanel eventHoldPane = new JPanel();
        eventHoldPane.setLayout(new BoxLayout(eventHoldPane, BoxLayout.Y_AXIS));
        eventHoldPane.setBorder(new EmptyBorder(windowBorderBuffer, windowBorderBuffer, windowBorderBuffer, windowBorderBuffer));
        eventHoldPane.add(graphicInterface);
        
        if(hsObject.IsTerrainObject())
        {
            setSize(windowWidth, windowHeightTerrain);
            
            TerrainObject tObject = (TerrainObject)hsObject;
            
            JLabel healthDeathLabel = new JLabel("Health Death");
            healthDeath = createHoldComboBox(holdComboRenderer, allHoldsPlusNull, tObject.terrainEventHolds.healthDeath);
            
            JPanel terrainInterface = new JPanel(new GridLayout(1, gridColumns, gridHorizontalGap, gridVerticalGap));
            terrainInterface.setSize(gridWidth, gridRowHeight);
            terrainInterface.setBorder(new TitledBorder("Destruction Events"));
            terrainInterface.add(healthDeathLabel);
            terrainInterface.add(healthDeath);
            terrainInterface.add(new JLabel(""));
            terrainInterface.add(new JLabel(""));
            
            eventHoldPane.add(terrainInterface);
        }
        
        if(hsObject.IsPhysicsObject())
        {
            setSize(windowWidth, windowHeightPhysics);
            
            PhysicsObject pObject = (PhysicsObject)hsObject;
        }
        
        if(hsObject.IsFighter())
        {
            setSize(windowWidth, windowHeightFighter);
            
            Fighter fighter = (Fighter)hsObject;
            
            JLabel standingLabel = new JLabel("Standing");
            standing = createHoldComboBox(holdComboRenderer, allHoldsPlusNull, fighter.fighterEventHolds.standing);
            JLabel turnLabel = new JLabel("Turn");
            turn = createHoldComboBox(holdComboRenderer, allHoldsPlusNull, fighter.fighterEventHolds.turn);
            JLabel walkLabel = new JLabel("Walk");
            walk = createHoldComboBox(holdComboRenderer, allHoldsPlusNull, fighter.fighterEventHolds.walk);
            JLabel walkingLabel = new JLabel("Walking");
            walking = createHoldComboBox(holdComboRenderer, allHoldsPlusNull, fighter.fighterEventHolds.walking);
            JLabel walkingTurnLabel = new JLabel("Walking Turn");
            walkingTurn = createHoldComboBox(holdComboRenderer, allHoldsPlusNull, fighter.fighterEventHolds.walkingTurn);
            JLabel runLabel = new JLabel("Run");
            run = createHoldComboBox(holdComboRenderer, allHoldsPlusNull, fighter.fighterEventHolds.run);
            JLabel runningLabel = new JLabel("Running");
            running = createHoldComboBox(holdComboRenderer, allHoldsPlusNull, fighter.fighterEventHolds.running);
            JLabel runningTurnLabel = new JLabel("Running Turn");
            runningTurn = createHoldComboBox(holdComboRenderer, allHoldsPlusNull, fighter.fighterEventHolds.runningTurn);
            JLabel runningStopLabel = new JLabel("Running Stop");
            runningStop = createHoldComboBox(holdComboRenderer, allHoldsPlusNull, fighter.fighterEventHolds.runningStop);
            JLabel crouchLabel = new JLabel("Crouch");
            crouch = createHoldComboBox(holdComboRenderer, allHoldsPlusNull, fighter.fighterEventHolds.crouch);
            JLabel crouchingLabel = new JLabel("Crouching");
            crouching = createHoldComboBox(holdComboRenderer, allHoldsPlusNull, fighter.fighterEventHolds.crouching);
            JLabel crouchingTurnLabel = new JLabel("Crouching Turn");
            crouchingTurn = createHoldComboBox(holdComboRenderer, allHoldsPlusNull, fighter.fighterEventHolds.crouchingTurn);
            JLabel standLabel = new JLabel("Stand");
            stand = createHoldComboBox(holdComboRenderer, allHoldsPlusNull, fighter.fighterEventHolds.stand);
            
            JLabel jumpNeutralStartLabel = new JLabel("Neutral Start");
            jumpNeutralStart = createHoldComboBox(holdComboRenderer, allHoldsPlusNull, fighter.fighterEventHolds.jumpNeutralStart);
            JLabel jumpNeutralStartAirLabel = new JLabel("Neutral Start Air");
            jumpNeutralStartAir = createHoldComboBox(holdComboRenderer, allHoldsPlusNull, fighter.fighterEventHolds.jumpNeutralStartAir);
            JLabel jumpNeutralRisingLabel = new JLabel("Neutral Rising");
            jumpNeutralRising = createHoldComboBox(holdComboRenderer, allHoldsPlusNull, fighter.fighterEventHolds.jumpNeutralRising);
            JLabel jumpNeutralFallLabel = new JLabel("Neutral Fall");
            jumpNeutralFall = createHoldComboBox(holdComboRenderer, allHoldsPlusNull, fighter.fighterEventHolds.jumpNeutralFall);
            JLabel jumpNeutralFallingLabel = new JLabel("Neutral Falling");
            jumpNeutralFalling = createHoldComboBox(holdComboRenderer, allHoldsPlusNull, fighter.fighterEventHolds.jumpNeutralFalling);
            JLabel jumpNeutralLandLabel = new JLabel("Neutral Land");
            jumpNeutralLand = createHoldComboBox(holdComboRenderer, allHoldsPlusNull, fighter.fighterEventHolds.jumpNeutralLand);
//            JLabel jumpNeutralLandHardLabel = new JLabel("jump Neutral Land Hard");
//            jumpNeutralLandHard = createHoldComboBox(holdComboRenderer, allHoldsPlusNull, fighter.fighterEventHolds.jumpNeutralLandHard);
//            JLabel jumpForwardStartLabel = new JLabel("jump Forward Start");
//            jumpForwardStart = createHoldComboBox(holdComboRenderer, allHoldsPlusNull, fighter.fighterEventHolds.jumpForwardStart);
//            JLabel jumpForwardRisingLabel = new JLabel("jump Forward Rising");
//            jumpForwardRising = createHoldComboBox(holdComboRenderer, allHoldsPlusNull, fighter.fighterEventHolds.jumpForwardRising);
//            JLabel jumpForwardFallLabel = new JLabel("jump Forward Fall");
//            jumpForwardFall = createHoldComboBox(holdComboRenderer, allHoldsPlusNull, fighter.fighterEventHolds.jumpForwardFall);
//            JLabel jumpForwardFallingLabel = new JLabel("jump Forward Falling");
//            jumpForwardFalling = createHoldComboBox(holdComboRenderer, allHoldsPlusNull, fighter.fighterEventHolds.jumpForwardFalling);
//            JLabel jumpForwardLandLabel = new JLabel("jump Forward Land");
//            jumpForwardLand = createHoldComboBox(holdComboRenderer, allHoldsPlusNull, fighter.fighterEventHolds.jumpForwardLand);
//            JLabel jumpForwardLandHardLabel = new JLabel("jump Forward Land Hard");
//            jumpForwardLandHard = createHoldComboBox(holdComboRenderer, allHoldsPlusNull, fighter.fighterEventHolds.jumpForwardLandHard);
//            JLabel jumpBackwardStartLabel = new JLabel("jump Backward Start");
//            jumpBackwardStart = createHoldComboBox(holdComboRenderer, allHoldsPlusNull, fighter.fighterEventHolds.jumpBackwardStart);
            JLabel jumpBackwardRisingLabel = new JLabel("Backward Rising");
            jumpBackwardRising = createHoldComboBox(holdComboRenderer, allHoldsPlusNull, fighter.fighterEventHolds.jumpBackwardRising);
            JLabel jumpBackwardFallLabel = new JLabel("Backward Fall");
            jumpBackwardFall = createHoldComboBox(holdComboRenderer, allHoldsPlusNull, fighter.fighterEventHolds.jumpBackwardFall);
//            JLabel jumpBackwardFallingLabel = new JLabel("jump Backward Falling");
//            jumpBackwardFalling = createHoldComboBox(holdComboRenderer, allHoldsPlusNull, fighter.fighterEventHolds.jumpBackwardFalling);
//            JLabel jumpBackwardLandLabel = new JLabel("jump Backward Land");
//            jumpBackwardLand = createHoldComboBox(holdComboRenderer, allHoldsPlusNull, fighter.fighterEventHolds.jumpBackwardLand);
//            JLabel jumpBackwardLandHardLabel = new JLabel("jump Backward Land Hard");
//            jumpBackwardLandHard = createHoldComboBox(holdComboRenderer, allHoldsPlusNull, fighter.fighterEventHolds.jumpBackwardLandHard);
            
            JLabel airDashForwardLabel = new JLabel("Forward");
            airDashForward = createHoldComboBox(holdComboRenderer, allHoldsPlusNull, fighter.fighterEventHolds.airDashForward);
            JLabel airDashBackwardLabel = new JLabel("Backward");
            airDashBackward = createHoldComboBox(holdComboRenderer, allHoldsPlusNull, fighter.fighterEventHolds.airDashBackward);
            
            JLabel blockHighLabel = new JLabel("High");
            blockHigh = createHoldComboBox(holdComboRenderer, allHoldsPlusNull, fighter.fighterEventHolds.blockHigh);
            JLabel blockLowLabel = new JLabel("Low");
            blockLow = createHoldComboBox(holdComboRenderer, allHoldsPlusNull, fighter.fighterEventHolds.blockLow);
            JLabel blockAirLabel = new JLabel("Air");
            blockAir = createHoldComboBox(holdComboRenderer, allHoldsPlusNull, fighter.fighterEventHolds.blockAir);
            
//            JLabel ledgeGrabLabel = new JLabel("ledge Grab");
//            ledgeGrab = createHoldComboBox(holdComboRenderer, allHoldsPlusNull, fighter.fighterEventHolds.ledgeGrab);
//            JLabel ledgeClimbLabel = new JLabel("ledge Climb");
//            ledgeClimb = createHoldComboBox(holdComboRenderer, allHoldsPlusNull, fighter.fighterEventHolds.ledgeClimb);
            
//            JLabel hitstunHighStandingStartLabel = new JLabel("hitstun High Standing Start");
//            hitstunHighStandingStart = createHoldComboBox(holdComboRenderer, allHoldsPlusNull, fighter.fighterEventHolds.hitstunHighStandingStart);
            JLabel hitstunLightHighStandingLabel = new JLabel("Light High Standing");
            hitstunLightHighStanding = createHoldComboBox(holdComboRenderer, allHoldsPlusNull, fighter.fighterEventHolds.hitstunLightHighStanding);
//            JLabel hitstunHighStandingEndLabel = new JLabel("hitstun High Standing End");
//            hitstunHighStandingEnd = createHoldComboBox(holdComboRenderer, allHoldsPlusNull, fighter.fighterEventHolds.hitstunHighStandingEnd);
            JLabel hitstunLightMidStandingLabel = new JLabel("Light Mid Standing");
            hitstunLightMidStanding = createHoldComboBox(holdComboRenderer, allHoldsPlusNull, fighter.fighterEventHolds.hitstunLightMidStanding);
//            JLabel hitstunLowStandingStartLabel = new JLabel("hitstun Low Standing Start");
//            hitstunLowStandingStart = createHoldComboBox(holdComboRenderer, allHoldsPlusNull, fighter.fighterEventHolds.hitstunLowStandingStart);
            JLabel hitstunLightLowStandingLabel = new JLabel("Light Low Standing");
            hitstunLightLowStanding = createHoldComboBox(holdComboRenderer, allHoldsPlusNull, fighter.fighterEventHolds.hitstunLightLowStanding);
//            JLabel hitstunLowStandingEndLabel = new JLabel("hitstun Low Standing End");
//            hitstunLowStandingEnd = createHoldComboBox(holdComboRenderer, allHoldsPlusNull, fighter.fighterEventHolds.hitstunLowStandingEnd);
//            JLabel hitstunCrouchingStartLabel = new JLabel("hitstun Crouching Start");
//            hitstunCrouchingStart = createHoldComboBox(holdComboRenderer, allHoldsPlusNull, fighter.fighterEventHolds.hitstunCrouchingStart);
            JLabel hitstunLightMidCrouchingLabel = new JLabel("Light Mid Crouching");
            hitstunLightMidCrouching = createHoldComboBox(holdComboRenderer, allHoldsPlusNull, fighter.fighterEventHolds.hitstunLightMidCrouching);
//            JLabel hitstunCrouchingEndLabel = new JLabel("hitstun Crouching End");
//            hitstunCrouchingEnd = createHoldComboBox(holdComboRenderer, allHoldsPlusNull, fighter.fighterEventHolds.hitstunCrouchingEnd);
            JLabel hitstunLightLowCrouchingLabel = new JLabel("Light Low Crouching");
            hitstunLightLowCrouching = createHoldComboBox(holdComboRenderer, allHoldsPlusNull, fighter.fighterEventHolds.hitstunLightLowCrouching);
            JLabel hitstunLightAirLabel = new JLabel("Light Air");
            hitstunLightAir = createHoldComboBox(holdComboRenderer, allHoldsPlusNull, fighter.fighterEventHolds.hitstunLightAir);
//            JLabel tripForwardLabel = new JLabel("trip Forward");
//            tripForward = createHoldComboBox(holdComboRenderer, allHoldsPlusNull, fighter.fighterEventHolds.tripForward);
//            JLabel tripBackwardLabel = new JLabel("trip Backward");
//            tripBackward = createHoldComboBox(holdComboRenderer, allHoldsPlusNull, fighter.fighterEventHolds.tripBackward);
//            JLabel proneFaceUpLabel = new JLabel("prone Face Up");
//            proneFaceUp = createHoldComboBox(holdComboRenderer, allHoldsPlusNull, fighter.fighterEventHolds.proneFaceUp);
//            JLabel proneFaceUpStandLabel = new JLabel("prone Face Up Stand");
//            proneFaceUpStand = createHoldComboBox(holdComboRenderer, allHoldsPlusNull, fighter.fighterEventHolds.proneFaceUpStand);
//            JLabel proneFaceDownLabel = new JLabel("prone Face Down");
//            proneFaceDown = createHoldComboBox(holdComboRenderer, allHoldsPlusNull, fighter.fighterEventHolds.proneFaceDown);
//            JLabel proneFaceDownStandLabel = new JLabel("prone Face Down Stand");
//            proneFaceDownStand = createHoldComboBox(holdComboRenderer, allHoldsPlusNull, fighter.fighterEventHolds.proneFaceDownStand);
//            JLabel crumpleStartLabel = new JLabel("crumple Start");
//            crumpleStart = createHoldComboBox(holdComboRenderer, allHoldsPlusNull, fighter.fighterEventHolds.crumpleStart);
//            JLabel crumpleLabel = new JLabel("crumple");
//            crumple = createHoldComboBox(holdComboRenderer, allHoldsPlusNull, fighter.fighterEventHolds.crumple);
            
//            JLabel airbornFaceUpRisingLabel = new JLabel("airborn Face Up Rising");
//            airbornFaceUpRising = createHoldComboBox(holdComboRenderer, allHoldsPlusNull, fighter.fighterEventHolds.airbornFaceUpRising);
//            JLabel airbornFaceUpFallLabel = new JLabel("airborn Face Up Fall");
//            airbornFaceUpFall = createHoldComboBox(holdComboRenderer, allHoldsPlusNull, fighter.fighterEventHolds.airbornFaceUpFall);
//            JLabel airbornFaceUpFallingLabel = new JLabel("airborn Face Up Falling");
//            airbornFaceUpFalling = createHoldComboBox(holdComboRenderer, allHoldsPlusNull, fighter.fighterEventHolds.airbornFaceUpFalling);
//            JLabel airbornFaceUpRiseLabel = new JLabel("airborn Face Up Rise");
//            airbornFaceUpRise = createHoldComboBox(holdComboRenderer, allHoldsPlusNull, fighter.fighterEventHolds.airbornFaceUpRise);
//            JLabel airbornFaceDownRisingLabel = new JLabel("airborn Face Down Rising");
//            airbornFaceDownRising = createHoldComboBox(holdComboRenderer, allHoldsPlusNull, fighter.fighterEventHolds.airbornFaceDownRising);
//            JLabel airbornFaceDownFallLabel = new JLabel("airborn Face Down Fall");
//            airbornFaceDownFall = createHoldComboBox(holdComboRenderer, allHoldsPlusNull, fighter.fighterEventHolds.airbornFaceDownFall);
//            JLabel airbornFaceDownFallingLabel = new JLabel("airborn Face Down Falling");
//            airbornFaceDownFalling = createHoldComboBox(holdComboRenderer, allHoldsPlusNull, fighter.fighterEventHolds.airbornFaceDownFalling);
//            JLabel airbornFaceDownRiseLabel = new JLabel("airborn Face Down Rise");
//            airbornFaceDownRise = createHoldComboBox(holdComboRenderer, allHoldsPlusNull, fighter.fighterEventHolds.airbornFaceDownRise);
//            JLabel airbornFaceSideMovingForwardLabel = new JLabel("airborn Face Side Moving Forward");
//            airbornFaceSideMovingForward = createHoldComboBox(holdComboRenderer, allHoldsPlusNull, fighter.fighterEventHolds.airbornFaceSideMovingForward);
//            JLabel airbornFaceSideMoveBackwardLabel = new JLabel("airborn Face Side Move Backward");
//            airbornFaceSideMoveBackward = createHoldComboBox(holdComboRenderer, allHoldsPlusNull, fighter.fighterEventHolds.airbornFaceSideMoveBackward);
//            JLabel airbornFaceSideMovingBackwardLabel = new JLabel("airborn Face Side Moving Backward");
//            airbornFaceSideMovingBackward = createHoldComboBox(holdComboRenderer, allHoldsPlusNull, fighter.fighterEventHolds.airbornFaceSideMovingBackward);
//            JLabel airbornFaceSideMoveForwardLabel = new JLabel("airborn Face Side Move Forward");
//            airbornFaceSideMoveForward = createHoldComboBox(holdComboRenderer, allHoldsPlusNull, fighter.fighterEventHolds.airbornFaceSideMoveForward);
            
            JLabel attackLightNeutralGroundLabel = new JLabel("Light Neutral");
            attackLightNeutralGround = createHoldComboBox(holdComboRenderer, allHoldsPlusNull, fighter.fighterEventHolds.attackLightNeutralGround);
            JLabel attackLightDownGroundLabel = new JLabel("Light Down");
            attackLightDownGround = createHoldComboBox(holdComboRenderer, allHoldsPlusNull, fighter.fighterEventHolds.attackLightDownGround);
            JLabel attackLightUpGroundLabel = new JLabel("Light Up");
            attackLightUpGround = createHoldComboBox(holdComboRenderer, allHoldsPlusNull, fighter.fighterEventHolds.attackLightUpGround);
            JLabel attackLightForwardGroundLabel = new JLabel("Light Forward");
            attackLightForwardGround = createHoldComboBox(holdComboRenderer, allHoldsPlusNull, fighter.fighterEventHolds.attackLightForwardGround);
            JLabel attackLightQCFGroundLabel = new JLabel("Light QCF");
            attackLightQCFGround = createHoldComboBox(holdComboRenderer, allHoldsPlusNull, fighter.fighterEventHolds.attackLightQCFGround);
            JLabel attackLightNeutralAirLabel = new JLabel("Light Neutral");
            attackLightNeutralAir = createHoldComboBox(holdComboRenderer, allHoldsPlusNull, fighter.fighterEventHolds.attackLightNeutralAir);
            JLabel attackLightDownAirLabel = new JLabel("Light Down");
            attackLightDownAir = createHoldComboBox(holdComboRenderer, allHoldsPlusNull, fighter.fighterEventHolds.attackLightDownAir);
            JLabel attackLightUpAirLabel = new JLabel("Light Up");
            attackLightUpAir = createHoldComboBox(holdComboRenderer, allHoldsPlusNull, fighter.fighterEventHolds.attackLightUpAir);
            JLabel attackLightForwardAirLabel = new JLabel("Light Forward");
            attackLightForwardAir = createHoldComboBox(holdComboRenderer, allHoldsPlusNull, fighter.fighterEventHolds.attackLightForwardAir);
            JLabel attackLightBackwardAirLabel = new JLabel("Light Backward");
            attackLightBackwardAir = createHoldComboBox(holdComboRenderer, allHoldsPlusNull, fighter.fighterEventHolds.attackLightBackwardAir);
            JLabel attackLightQCFAirLabel = new JLabel("Light QCF");
            attackLightQCFAir = createHoldComboBox(holdComboRenderer, allHoldsPlusNull, fighter.fighterEventHolds.attackLightQCFAir);
            
            JLabel attackHeavyNeutralGroundLabel = new JLabel("Heavy Neutral");
            attackHeavyNeutralGround = createHoldComboBox(holdComboRenderer, allHoldsPlusNull, fighter.fighterEventHolds.attackHeavyNeutralGround);
            JLabel attackHeavyDownGroundLabel = new JLabel("Heavy Down");
            attackHeavyDownGround = createHoldComboBox(holdComboRenderer, allHoldsPlusNull, fighter.fighterEventHolds.attackHeavyDownGround);
            JLabel attackHeavyUpGroundLabel = new JLabel("Heavy Up");
            attackHeavyUpGround = createHoldComboBox(holdComboRenderer, allHoldsPlusNull, fighter.fighterEventHolds.attackHeavyUpGround);
            JLabel attackHeavyForwardGroundLabel = new JLabel("Heavy Forward");
            attackHeavyForwardGround = createHoldComboBox(holdComboRenderer, allHoldsPlusNull, fighter.fighterEventHolds.attackHeavyForwardGround);
            JLabel attackHeavyQCFGroundLabel = new JLabel("Heavy QCF");
            attackHeavyQCFGround = createHoldComboBox(holdComboRenderer, allHoldsPlusNull, fighter.fighterEventHolds.attackHeavyQCFGround);
            JLabel attackHeavyNeutralAirLabel = new JLabel("Heavy Neutral");
            attackHeavyNeutralAir = createHoldComboBox(holdComboRenderer, allHoldsPlusNull, fighter.fighterEventHolds.attackHeavyNeutralAir);
            JLabel attackHeavyDownAirLabel = new JLabel("Heavy Down");
            attackHeavyDownAir = createHoldComboBox(holdComboRenderer, allHoldsPlusNull, fighter.fighterEventHolds.attackHeavyDownAir);
            JLabel attackHeavyUpAirLabel = new JLabel("Heavy Up");
            attackHeavyUpAir = createHoldComboBox(holdComboRenderer, allHoldsPlusNull, fighter.fighterEventHolds.attackHeavyUpAir);
            JLabel attackHeavyForwardAirLabel = new JLabel("Heavy Forward");
            attackHeavyForwardAir = createHoldComboBox(holdComboRenderer, allHoldsPlusNull, fighter.fighterEventHolds.attackHeavyForwardAir);
            JLabel attackHeavyBackwardAirLabel = new JLabel("Heavy Backward");
            attackHeavyBackwardAir = createHoldComboBox(holdComboRenderer, allHoldsPlusNull, fighter.fighterEventHolds.attackHeavyBackwardAir);
            JLabel attackHeavyQCFAirLabel = new JLabel("Heavy QCF");
            attackHeavyQCFAir = createHoldComboBox(holdComboRenderer, allHoldsPlusNull, fighter.fighterEventHolds.attackHeavyQCFAir);
            
            JLabel knockoutLabel = new JLabel("Knockout");
            knockout = createHoldComboBox(holdComboRenderer, allHoldsPlusNull, fighter.fighterEventHolds.knockout);
            
            JPanel groundMoveInterface = new JPanel(new GridLayout(7, gridColumns, gridHorizontalGap, gridVerticalGap));
            groundMoveInterface.setSize(gridWidth, gridRowHeight * 7);
            groundMoveInterface.setBorder(new TitledBorder("Ground Movement Events"));
            groundMoveInterface.add(standingLabel);
            groundMoveInterface.add(standing);
            groundMoveInterface.add(turnLabel);
            groundMoveInterface.add(turn);
            groundMoveInterface.add(walkLabel);
            groundMoveInterface.add(walk);
            groundMoveInterface.add(walkingLabel);
            groundMoveInterface.add(walking);
            groundMoveInterface.add(walkingTurnLabel);
            groundMoveInterface.add(walkingTurn);
            groundMoveInterface.add(runLabel);
            groundMoveInterface.add(run);
            groundMoveInterface.add(runningLabel);
            groundMoveInterface.add(running);
            groundMoveInterface.add(runningTurnLabel);
            groundMoveInterface.add(runningTurn);
            groundMoveInterface.add(runningStopLabel);
            groundMoveInterface.add(runningStop);
            groundMoveInterface.add(crouchLabel);
            groundMoveInterface.add(crouch);
            groundMoveInterface.add(crouchingLabel);
            groundMoveInterface.add(crouching);
            groundMoveInterface.add(crouchingTurnLabel);
            groundMoveInterface.add(crouchingTurn);
            groundMoveInterface.add(standLabel);
            groundMoveInterface.add(stand);
//            groundMoveInterface.add(ledgeGrabLabel);
//            groundMoveInterface.add(ledgeGrab);
//            groundMoveInterface.add(ledgeClimbLabel);
//            groundMoveInterface.add(ledgeClimb);
            groundMoveInterface.add(new JLabel(""));
            groundMoveInterface.add(new JLabel(""));
            
            eventHoldPane.add(groundMoveInterface);
            
            JPanel jumpInterface = new JPanel(new GridLayout(4, gridColumns, gridHorizontalGap, gridVerticalGap));
            jumpInterface.setSize(gridWidth, gridRowHeight * 4);
            jumpInterface.setBorder(new TitledBorder("Jump Events"));
            jumpInterface.add(jumpNeutralStartLabel);
            jumpInterface.add(jumpNeutralStart);
            jumpInterface.add(jumpNeutralStartAirLabel);
            jumpInterface.add(jumpNeutralStartAir);
            jumpInterface.add(jumpNeutralRisingLabel);
            jumpInterface.add(jumpNeutralRising);
            jumpInterface.add(jumpNeutralFallLabel);
            jumpInterface.add(jumpNeutralFall);
            jumpInterface.add(jumpNeutralFallingLabel);
            jumpInterface.add(jumpNeutralFalling);
            jumpInterface.add(jumpNeutralLandLabel);
            jumpInterface.add(jumpNeutralLand);
//            airMoveInterface.add(jumpNeutralLandHardLabel);
//            airMoveInterface.add(jumpNeutralLandHard);
//            airMoveInterface.add(jumpForwardStartLabel);
//            airMoveInterface.add(jumpForwardStart);
//            airMoveInterface.add(jumpForwardRisingLabel);
//            airMoveInterface.add(jumpForwardRising);
//            airMoveInterface.add(jumpForwardFallLabel);
//            airMoveInterface.add(jumpForwardFall);
//            airMoveInterface.add(jumpForwardFallingLabel);
//            airMoveInterface.add(jumpForwardFalling);
//            airMoveInterface.add(jumpForwardLandLabel);
//            airMoveInterface.add(jumpForwardLand);
//            airMoveInterface.add(jumpForwardLandHardLabel);
//            airMoveInterface.add(jumpForwardLandHard);
//            airMoveInterface.add(jumpBackwardStartLabel);
//            airMoveInterface.add(jumpBackwardStart);
            jumpInterface.add(jumpBackwardRisingLabel);
            jumpInterface.add(jumpBackwardRising);
            jumpInterface.add(jumpBackwardFallLabel);
            jumpInterface.add(jumpBackwardFall);
//            airMoveInterface.add(jumpBackwardFallingLabel);
//            airMoveInterface.add(jumpBackwardFalling);
//            airMoveInterface.add(jumpBackwardLandLabel);
//            airMoveInterface.add(jumpBackwardLand);
//            airMoveInterface.add(jumpBackwardLandHardLabel);
//            airMoveInterface.add(jumpBackwardLandHard);
            
            eventHoldPane.add(jumpInterface);
            
            JPanel airDashInterface = new JPanel(new GridLayout(1, gridColumns, gridHorizontalGap, gridVerticalGap));
            airDashInterface.setSize(gridWidth, gridRowHeight * 1);
            airDashInterface.setBorder(new TitledBorder("Air Dash Events"));
            airDashInterface.add(airDashForwardLabel);
            airDashInterface.add(airDashForward);
            airDashInterface.add(airDashBackwardLabel);
            airDashInterface.add(airDashBackward);
            
            eventHoldPane.add(airDashInterface);
            
            JPanel blockInterface = new JPanel(new GridLayout(2, gridColumns, gridHorizontalGap, gridVerticalGap));
            blockInterface.setSize(gridWidth, gridRowHeight * 2);
            blockInterface.setBorder(new TitledBorder("Block Events"));
            blockInterface.add(blockHighLabel);
            blockInterface.add(blockHigh);
            blockInterface.add(blockLowLabel);
            blockInterface.add(blockLow);
            blockInterface.add(blockAirLabel);
            blockInterface.add(blockAir);
            blockInterface.add(new JLabel(""));
            blockInterface.add(new JLabel(""));
            
            eventHoldPane.add(blockInterface);
            
            JPanel hitstunInterface = new JPanel(new GridLayout(3, gridColumns, gridHorizontalGap, gridVerticalGap));
            hitstunInterface.setSize(gridWidth, gridRowHeight * 3);
            hitstunInterface.setBorder(new TitledBorder("Hitstun Events"));
//            groundStunInterface.add(hitstunHighStandingStartLabel);
//            groundStunInterface.add(hitstunHighStandingStart);
            hitstunInterface.add(hitstunLightHighStandingLabel);
            hitstunInterface.add(hitstunLightHighStanding);
//            groundStunInterface.add(hitstunHighStandingEndLabel);
            hitstunInterface.add(hitstunLightMidStandingLabel);
            hitstunInterface.add(hitstunLightMidStanding);
//            groundStunInterface.add(hitstunHighStandingEnd);
//            groundStunInterface.add(hitstunLowStandingStartLabel);
//            groundStunInterface.add(hitstunLowStandingStart);
            hitstunInterface.add(hitstunLightLowStandingLabel);
            hitstunInterface.add(hitstunLightLowStanding);
//            groundStunInterface.add(hitstunLowStandingEndLabel);
//            groundStunInterface.add(hitstunLowStandingEnd);
//            groundStunInterface.add(hitstunCrouchingStartLabel);
//            groundStunInterface.add(hitstunCrouchingStart);
            hitstunInterface.add(hitstunLightMidCrouchingLabel);
            hitstunInterface.add(hitstunLightMidCrouching);
//            groundStunInterface.add(hitstunCrouchingEndLabel);
//            groundStunInterface.add(hitstunCrouchingEnd);
            hitstunInterface.add(hitstunLightLowCrouchingLabel);
            hitstunInterface.add(hitstunLightLowCrouching);
            hitstunInterface.add(hitstunLightAirLabel);
            hitstunInterface.add(hitstunLightAir);
//            groundStunInterface.add(tripForwardLabel);
//            groundStunInterface.add(tripForward);
//            groundStunInterface.add(tripBackwardLabel);
//            groundStunInterface.add(tripBackward);
//            groundStunInterface.add(proneFaceUpLabel);
//            groundStunInterface.add(proneFaceUp);
//            groundStunInterface.add(proneFaceUpStandLabel);
//            groundStunInterface.add(proneFaceUpStand);
//            groundStunInterface.add(proneFaceDownLabel);
//            groundStunInterface.add(proneFaceDown);
//            groundStunInterface.add(proneFaceDownStandLabel);
//            groundStunInterface.add(proneFaceDownStand);
//            groundStunInterface.add(crumpleStartLabel);
//            groundStunInterface.add(crumpleStart);
//            groundStunInterface.add(crumpleLabel);
//            groundStunInterface.add(crumple);
            
            eventHoldPane.add(hitstunInterface);
            
//            JPanel airStunInterface = new JPanel(new GridLayout(6, gridColumns, gridHorizontalGap, gridVerticalGap));
//            airStunInterface.setSize(gridWidth, gridRowHeight * 6);
//            airStunInterface.setBorder(new TitledBorder("Air Stun Events"));
//            airStunInterface.add(airbornFaceUpRisingLabel);
//            airStunInterface.add(airbornFaceUpRising);
//            airStunInterface.add(airbornFaceUpFallLabel);
//            airStunInterface.add(airbornFaceUpFall);
//            airStunInterface.add(airbornFaceUpFallingLabel);
//            airStunInterface.add(airbornFaceUpFalling);
//            airStunInterface.add(airbornFaceUpRiseLabel);
//            airStunInterface.add(airbornFaceUpRise);
//            airStunInterface.add(airbornFaceDownRisingLabel);
//            airStunInterface.add(airbornFaceDownRising);
//            airStunInterface.add(airbornFaceDownFallLabel);
//            airStunInterface.add(airbornFaceDownFall);
//            airStunInterface.add(airbornFaceDownFallingLabel);
//            airStunInterface.add(airbornFaceDownFalling);
//            airStunInterface.add(airbornFaceDownRiseLabel);
//            airStunInterface.add(airbornFaceDownRise);
//            airStunInterface.add(airbornFaceSideMovingForwardLabel);
//            airStunInterface.add(airbornFaceSideMovingForward);
//            airStunInterface.add(airbornFaceSideMoveBackwardLabel);
//            airStunInterface.add(airbornFaceSideMoveBackward);
//            airStunInterface.add(airbornFaceSideMovingBackwardLabel);
//            airStunInterface.add(airbornFaceSideMovingBackward);
//            airStunInterface.add(airbornFaceSideMoveForwardLabel);
//            airStunInterface.add(airbornFaceSideMoveForward);
//            
//            eventHoldPane.add(airStunInterface);
            
            JPanel groundNormalInterface = new JPanel(new GridLayout(5, gridColumns, gridHorizontalGap, gridVerticalGap));
            groundNormalInterface.setSize(gridWidth, gridRowHeight * 5);
            groundNormalInterface.setBorder(new TitledBorder("Ground Normal Attack Events"));
            
            groundNormalInterface.add(attackLightNeutralGroundLabel);
            groundNormalInterface.add(attackLightNeutralGround);
            
            groundNormalInterface.add(attackLightDownGroundLabel);
            groundNormalInterface.add(attackLightDownGround);
            
            groundNormalInterface.add(attackLightUpGroundLabel);
            groundNormalInterface.add(attackLightUpGround);
            
            groundNormalInterface.add(attackLightForwardGroundLabel);
            groundNormalInterface.add(attackLightForwardGround);
            
            groundNormalInterface.add(attackLightQCFGroundLabel);
            groundNormalInterface.add(attackLightQCFGround);
            
            groundNormalInterface.add(attackHeavyNeutralGroundLabel);
            groundNormalInterface.add(attackHeavyNeutralGround);
            
            groundNormalInterface.add(attackHeavyDownGroundLabel);
            groundNormalInterface.add(attackHeavyDownGround);
            
            groundNormalInterface.add(attackHeavyUpGroundLabel);
            groundNormalInterface.add(attackHeavyUpGround);
            
            groundNormalInterface.add(attackHeavyForwardGroundLabel);
            groundNormalInterface.add(attackHeavyForwardGround);
            
            groundNormalInterface.add(attackHeavyQCFGroundLabel);
            groundNormalInterface.add(attackHeavyQCFGround);
            
            eventHoldPane.add(groundNormalInterface);
            
            JPanel airNormalInterface = new JPanel(new GridLayout(6, gridColumns, gridHorizontalGap, gridVerticalGap));
            airNormalInterface.setSize(gridWidth, gridRowHeight * 6);
            airNormalInterface.setBorder(new TitledBorder("Air Normal Attack Events"));
            airNormalInterface.add(attackLightNeutralAirLabel);
            airNormalInterface.add(attackLightNeutralAir);
            airNormalInterface.add(attackLightDownAirLabel);
            airNormalInterface.add(attackLightDownAir);
            airNormalInterface.add(attackLightUpAirLabel);
            airNormalInterface.add(attackLightUpAir);
            airNormalInterface.add(attackLightForwardAirLabel);
            airNormalInterface.add(attackLightForwardAir);
            airNormalInterface.add(attackLightBackwardAirLabel);
            airNormalInterface.add(attackLightBackwardAir);
            airNormalInterface.add(attackLightQCFAirLabel);
            airNormalInterface.add(attackLightQCFAir);
            airNormalInterface.add(attackHeavyNeutralAirLabel);
            airNormalInterface.add(attackHeavyNeutralAir);
            airNormalInterface.add(attackHeavyDownAirLabel);
            airNormalInterface.add(attackHeavyDownAir);
            airNormalInterface.add(attackHeavyUpAirLabel);
            airNormalInterface.add(attackHeavyUpAir);
            airNormalInterface.add(attackHeavyForwardAirLabel);
            airNormalInterface.add(attackHeavyForwardAir);
            airNormalInterface.add(attackHeavyBackwardAirLabel);
            airNormalInterface.add(attackHeavyBackwardAir);
            airNormalInterface.add(attackHeavyQCFAirLabel);
            airNormalInterface.add(attackHeavyQCFAir);
            
            eventHoldPane.add(airNormalInterface);
            
            JPanel otherInterface = new JPanel(new GridLayout(1, gridColumns, gridHorizontalGap, gridVerticalGap));
            otherInterface.setSize(gridWidth, gridRowHeight * 1);
            otherInterface.setBorder(new TitledBorder("Other Events"));
            otherInterface.add(knockoutLabel);
            otherInterface.add(knockout);
            otherInterface.add(new JLabel(""));
            otherInterface.add(new JLabel(""));
            
            eventHoldPane.add(otherInterface);
        }
        
        JScrollPane eventHoldScrollPane = new JScrollPane(eventHoldPane);
        
        JButton okButton = new JButton("OK");
        okButton.setActionCommand("okButton");
        okButton.addActionListener(this);
        JButton closeButton = new JButton("Close");
        closeButton.setActionCommand("closeButton");
        closeButton.addActionListener(this);
        applyButton = new JButton("Apply");
        applyButton.setActionCommand("applyButton");
        applyButton.addActionListener(this);
        applyButton.setEnabled(false);
        
        JPanel buttonPane = new JPanel(new FlowLayout(FlowLayout.TRAILING));
        buttonPane.add(okButton);
        buttonPane.add(closeButton);
        buttonPane.add(applyButton);
        
        JPanel interfacePane = new JPanel();
        interfacePane.setLayout(new BoxLayout(interfacePane, BoxLayout.Y_AXIS));
        interfacePane.setBorder(new EmptyBorder(windowBorderBuffer, windowBorderBuffer, windowBorderBuffer, windowBorderBuffer));
        interfacePane.add(eventHoldScrollPane);
        interfacePane.add(buttonPane);
        
        add(interfacePane);
    }
    
    public void applyChanges()
    {
        hsObject.hsObjectEventHolds.lifetimeDeath = lifetimeDeath.getSelectedIndex() == 0 ? null : (HSObjectHold)lifetimeDeath.getSelectedItem();
        
        if(hsObject.IsTerrainObject())
        {
            ((TerrainObject)hsObject).terrainEventHolds.healthDeath = healthDeath.getSelectedIndex() == 0 ? null : (TerrainObjectHold)healthDeath.getSelectedItem();
        }
        
        if(hsObject.IsPhysicsObject())
        {
            
        }
        
        if(hsObject.IsFighter())
        {
            ((Fighter)hsObject).fighterEventHolds.standing = standing.getSelectedIndex() == 0 ? null : (FighterHold)standing.getSelectedItem();
            ((Fighter)hsObject).fighterEventHolds.turn = turn.getSelectedIndex() == 0 ? null : (FighterHold)turn.getSelectedItem();
            ((Fighter)hsObject).fighterEventHolds.walk = walk.getSelectedIndex() == 0 ? null : (FighterHold)walk.getSelectedItem();
            ((Fighter)hsObject).fighterEventHolds.walking = walking.getSelectedIndex() == 0 ? null : (FighterHold)walking.getSelectedItem();
            ((Fighter)hsObject).fighterEventHolds.walkingTurn = walkingTurn.getSelectedIndex() == 0 ? null : (FighterHold)walkingTurn.getSelectedItem();
            ((Fighter)hsObject).fighterEventHolds.run = run.getSelectedIndex() == 0 ? null : (FighterHold)run.getSelectedItem();
            ((Fighter)hsObject).fighterEventHolds.running = running.getSelectedIndex() == 0 ? null : (FighterHold)running.getSelectedItem();
            ((Fighter)hsObject).fighterEventHolds.runningTurn = runningTurn.getSelectedIndex() == 0 ? null : (FighterHold)runningTurn.getSelectedItem();
            ((Fighter)hsObject).fighterEventHolds.runningStop = runningStop.getSelectedIndex() == 0 ? null : (FighterHold)runningStop.getSelectedItem();
            ((Fighter)hsObject).fighterEventHolds.crouch = crouch.getSelectedIndex() == 0 ? null : (FighterHold)crouch.getSelectedItem();
            ((Fighter)hsObject).fighterEventHolds.crouching = crouching.getSelectedIndex() == 0 ? null : (FighterHold)crouching.getSelectedItem();
            ((Fighter)hsObject).fighterEventHolds.crouchingTurn = crouchingTurn.getSelectedIndex() == 0 ? null : (FighterHold)crouchingTurn.getSelectedItem();
            ((Fighter)hsObject).fighterEventHolds.stand = stand.getSelectedIndex() == 0 ? null : (FighterHold)stand.getSelectedItem();
            ((Fighter)hsObject).fighterEventHolds.jumpNeutralStart = jumpNeutralStart.getSelectedIndex() == 0 ? null : (FighterHold)jumpNeutralStart.getSelectedItem();
            ((Fighter)hsObject).fighterEventHolds.jumpNeutralStartAir = jumpNeutralStartAir.getSelectedIndex() == 0 ? null : (FighterHold)jumpNeutralStartAir.getSelectedItem();
            ((Fighter)hsObject).fighterEventHolds.jumpNeutralRising = jumpNeutralRising.getSelectedIndex() == 0 ? null : (FighterHold)jumpNeutralRising.getSelectedItem();
            ((Fighter)hsObject).fighterEventHolds.jumpNeutralFall = jumpNeutralFall.getSelectedIndex() == 0 ? null : (FighterHold)jumpNeutralFall.getSelectedItem();
            ((Fighter)hsObject).fighterEventHolds.jumpNeutralFalling = jumpNeutralFalling.getSelectedIndex() == 0 ? null : (FighterHold)jumpNeutralFalling.getSelectedItem();
            ((Fighter)hsObject).fighterEventHolds.jumpNeutralLand = jumpNeutralLand.getSelectedIndex() == 0 ? null : (FighterHold)jumpNeutralLand.getSelectedItem();
//            ((Fighter)hsObject).fighterEventHolds.jumpNeutralLandHard = jumpNeutralLandHard.getSelectedIndex() == 0 ? null : (FighterHold)jumpNeutralLandHard.getSelectedItem();
//            ((Fighter)hsObject).fighterEventHolds.jumpForwardStart = jumpForwardStart.getSelectedIndex() == 0 ? null : (FighterHold)jumpForwardStart.getSelectedItem();
//            ((Fighter)hsObject).fighterEventHolds.jumpForwardRising = jumpForwardRising.getSelectedIndex() == 0 ? null : (FighterHold)jumpForwardRising.getSelectedItem();
//            ((Fighter)hsObject).fighterEventHolds.jumpForwardFall = jumpForwardFall.getSelectedIndex() == 0 ? null : (FighterHold)jumpForwardFall.getSelectedItem();
//            ((Fighter)hsObject).fighterEventHolds.jumpForwardFalling = jumpForwardFalling.getSelectedIndex() == 0 ? null : (FighterHold)jumpForwardFalling.getSelectedItem();
//            ((Fighter)hsObject).fighterEventHolds.jumpForwardLand = jumpForwardLand.getSelectedIndex() == 0 ? null : (FighterHold)jumpForwardLand.getSelectedItem();
//            ((Fighter)hsObject).fighterEventHolds.jumpForwardLandHard = jumpForwardLandHard.getSelectedIndex() == 0 ? null : (FighterHold)jumpForwardLandHard.getSelectedItem();
//            ((Fighter)hsObject).fighterEventHolds.jumpBackwardStart = jumpBackwardStart.getSelectedIndex() == 0 ? null : (FighterHold)jumpBackwardStart.getSelectedItem();
            ((Fighter)hsObject).fighterEventHolds.jumpBackwardRising = jumpBackwardRising.getSelectedIndex() == 0 ? null : (FighterHold)jumpBackwardRising.getSelectedItem();
            ((Fighter)hsObject).fighterEventHolds.jumpBackwardFall = jumpBackwardFall.getSelectedIndex() == 0 ? null : (FighterHold)jumpBackwardFall.getSelectedItem();
//            ((Fighter)hsObject).fighterEventHolds.jumpBackwardFalling = jumpBackwardFalling.getSelectedIndex() == 0 ? null : (FighterHold)jumpBackwardFalling.getSelectedItem();
//            ((Fighter)hsObject).fighterEventHolds.jumpBackwardLand = jumpBackwardLand.getSelectedIndex() == 0 ? null : (FighterHold)jumpBackwardLand.getSelectedItem();
//            ((Fighter)hsObject).fighterEventHolds.jumpBackwardLandHard = jumpBackwardLandHard.getSelectedIndex() == 0 ? null : (FighterHold)jumpBackwardLandHard.getSelectedItem();
            ((Fighter)hsObject).fighterEventHolds.airDashForward = airDashForward.getSelectedIndex() == 0 ? null : (FighterHold)airDashForward.getSelectedItem();
            ((Fighter)hsObject).fighterEventHolds.airDashBackward = airDashBackward.getSelectedIndex() == 0 ? null : (FighterHold)airDashBackward.getSelectedItem();
            ((Fighter)hsObject).fighterEventHolds.blockHigh = blockHigh.getSelectedIndex() == 0 ? null : (FighterHold)blockHigh.getSelectedItem();
            ((Fighter)hsObject).fighterEventHolds.blockLow = blockLow.getSelectedIndex() == 0 ? null : (FighterHold)blockLow.getSelectedItem();
            ((Fighter)hsObject).fighterEventHolds.blockAir = blockAir.getSelectedIndex() == 0 ? null : (FighterHold)blockAir.getSelectedItem();
//            ((Fighter)hsObject).fighterEventHolds.ledgeGrab = ledgeGrab.getSelectedIndex() == 0 ? null : (FighterHold)ledgeGrab.getSelectedItem();
//            ((Fighter)hsObject).fighterEventHolds.ledgeClimb = ledgeClimb.getSelectedIndex() == 0 ? null : (FighterHold)ledgeClimb.getSelectedItem();
//            ((Fighter)hsObject).fighterEventHolds.hitstunHighStandingStart = hitstunHighStandingStart.getSelectedIndex() == 0 ? null : (FighterHold)hitstunHighStandingStart.getSelectedItem();
            ((Fighter)hsObject).fighterEventHolds.hitstunLightHighStanding = hitstunLightHighStanding.getSelectedIndex() == 0 ? null : (FighterHold)hitstunLightHighStanding.getSelectedItem();
//            ((Fighter)hsObject).fighterEventHolds.hitstunHighStandingEnd = hitstunHighStandingEnd.getSelectedIndex() == 0 ? null : (FighterHold)hitstunHighStandingEnd.getSelectedItem();
            ((Fighter)hsObject).fighterEventHolds.hitstunLightMidStanding = hitstunLightMidStanding.getSelectedIndex() == 0 ? null : (FighterHold)hitstunLightMidStanding.getSelectedItem();
//            ((Fighter)hsObject).fighterEventHolds.hitstunLowStandingStart = hitstunLowStandingStart.getSelectedIndex() == 0 ? null : (FighterHold)hitstunLowStandingStart.getSelectedItem();
            ((Fighter)hsObject).fighterEventHolds.hitstunLightLowStanding = hitstunLightLowStanding.getSelectedIndex() == 0 ? null : (FighterHold)hitstunLightLowStanding.getSelectedItem();
//            ((Fighter)hsObject).fighterEventHolds.hitstunLowStandingEnd = hitstunLowStandingEnd.getSelectedIndex() == 0 ? null : (FighterHold)hitstunLowStandingEnd.getSelectedItem();
//            ((Fighter)hsObject).fighterEventHolds.hitstunCrouchingStart = hitstunCrouchingStart.getSelectedIndex() == 0 ? null : (FighterHold)hitstunCrouchingStart.getSelectedItem();
            ((Fighter)hsObject).fighterEventHolds.hitstunLightMidCrouching = hitstunLightMidCrouching.getSelectedIndex() == 0 ? null : (FighterHold)hitstunLightMidCrouching.getSelectedItem();
//            ((Fighter)hsObject).fighterEventHolds.hitstunCrouchingEnd = hitstunCrouchingEnd.getSelectedIndex() == 0 ? null : (FighterHold)hitstunCrouchingEnd.getSelectedItem();
            ((Fighter)hsObject).fighterEventHolds.hitstunLightLowCrouching = hitstunLightLowCrouching.getSelectedIndex() == 0 ? null : (FighterHold)hitstunLightLowCrouching.getSelectedItem();
            ((Fighter)hsObject).fighterEventHolds.hitstunLightAir = hitstunLightAir.getSelectedIndex() == 0 ? null : (FighterHold)hitstunLightAir.getSelectedItem();
//            ((Fighter)hsObject).fighterEventHolds.tripForward = tripForward.getSelectedIndex() == 0 ? null : (FighterHold)tripForward.getSelectedItem();
//            ((Fighter)hsObject).fighterEventHolds.tripBackward = tripBackward.getSelectedIndex() == 0 ? null : (FighterHold)tripBackward.getSelectedItem();
//            ((Fighter)hsObject).fighterEventHolds.proneFaceUp = proneFaceUp.getSelectedIndex() == 0 ? null : (FighterHold)proneFaceUp.getSelectedItem();
//            ((Fighter)hsObject).fighterEventHolds.proneFaceUpStand = proneFaceUpStand.getSelectedIndex() == 0 ? null : (FighterHold)proneFaceUpStand.getSelectedItem();
//            ((Fighter)hsObject).fighterEventHolds.proneFaceDown = proneFaceDown.getSelectedIndex() == 0 ? null : (FighterHold)proneFaceDown.getSelectedItem();
//            ((Fighter)hsObject).fighterEventHolds.proneFaceDownStand = proneFaceDownStand.getSelectedIndex() == 0 ? null : (FighterHold)proneFaceDownStand.getSelectedItem();
//            ((Fighter)hsObject).fighterEventHolds.crumpleStart = crumpleStart.getSelectedIndex() == 0 ? null : (FighterHold)crumpleStart.getSelectedItem();
//            ((Fighter)hsObject).fighterEventHolds.crumple = crumple.getSelectedIndex() == 0 ? null : (FighterHold)crumple.getSelectedItem();
//            ((Fighter)hsObject).fighterEventHolds.airbornFaceUpRising = airbornFaceUpRising.getSelectedIndex() == 0 ? null : (FighterHold)airbornFaceUpRising.getSelectedItem();
//            ((Fighter)hsObject).fighterEventHolds.airbornFaceUpFall = airbornFaceUpFall.getSelectedIndex() == 0 ? null : (FighterHold)airbornFaceUpFall.getSelectedItem();
//            ((Fighter)hsObject).fighterEventHolds.airbornFaceUpFalling = airbornFaceUpFalling.getSelectedIndex() == 0 ? null : (FighterHold)airbornFaceUpFalling.getSelectedItem();
//            ((Fighter)hsObject).fighterEventHolds.airbornFaceUpRise = airbornFaceUpRise.getSelectedIndex() == 0 ? null : (FighterHold)airbornFaceUpRise.getSelectedItem();
//            ((Fighter)hsObject).fighterEventHolds.airbornFaceDownRising = airbornFaceDownRising.getSelectedIndex() == 0 ? null : (FighterHold)airbornFaceDownRising.getSelectedItem();
//            ((Fighter)hsObject).fighterEventHolds.airbornFaceDownFall = airbornFaceDownFall.getSelectedIndex() == 0 ? null : (FighterHold)airbornFaceDownFall.getSelectedItem();
//            ((Fighter)hsObject).fighterEventHolds.airbornFaceDownFalling = airbornFaceDownFalling.getSelectedIndex() == 0 ? null : (FighterHold)airbornFaceDownFalling.getSelectedItem();
//            ((Fighter)hsObject).fighterEventHolds.airbornFaceDownRise = airbornFaceDownRise.getSelectedIndex() == 0 ? null : (FighterHold)airbornFaceDownRise.getSelectedItem();
//            ((Fighter)hsObject).fighterEventHolds.airbornFaceSideMovingForward = airbornFaceSideMovingForward.getSelectedIndex() == 0 ? null : (FighterHold)airbornFaceSideMovingForward.getSelectedItem();
//            ((Fighter)hsObject).fighterEventHolds.airbornFaceSideMoveBackward = airbornFaceSideMoveBackward.getSelectedIndex() == 0 ? null : (FighterHold)airbornFaceSideMoveBackward.getSelectedItem();
//            ((Fighter)hsObject).fighterEventHolds.airbornFaceSideMovingBackward = airbornFaceSideMovingBackward.getSelectedIndex() == 0 ? null : (FighterHold)airbornFaceSideMovingBackward.getSelectedItem();
//            ((Fighter)hsObject).fighterEventHolds.airbornFaceSideMoveForward = airbornFaceSideMoveForward.getSelectedIndex() == 0 ? null : (FighterHold)airbornFaceSideMoveForward.getSelectedItem();
            ((Fighter)hsObject).fighterEventHolds.attackLightNeutralGround = attackLightNeutralGround.getSelectedIndex() == 0 ? null : (FighterHold)attackLightNeutralGround.getSelectedItem();
            ((Fighter)hsObject).fighterEventHolds.attackLightDownGround = attackLightDownGround.getSelectedIndex() == 0 ? null : (FighterHold)attackLightDownGround.getSelectedItem();
            ((Fighter)hsObject).fighterEventHolds.attackLightUpGround = attackLightUpGround.getSelectedIndex() == 0 ? null : (FighterHold)attackLightUpGround.getSelectedItem();
            ((Fighter)hsObject).fighterEventHolds.attackLightForwardGround = attackLightForwardGround.getSelectedIndex() == 0 ? null : (FighterHold)attackLightForwardGround.getSelectedItem();
            ((Fighter)hsObject).fighterEventHolds.attackLightQCFGround = attackLightQCFGround.getSelectedIndex() == 0 ? null : (FighterHold)attackLightQCFGround.getSelectedItem();
            ((Fighter)hsObject).fighterEventHolds.attackLightNeutralAir = attackLightNeutralAir.getSelectedIndex() == 0 ? null : (FighterHold)attackLightNeutralAir.getSelectedItem();
            ((Fighter)hsObject).fighterEventHolds.attackLightDownAir = attackLightDownAir.getSelectedIndex() == 0 ? null : (FighterHold)attackLightDownAir.getSelectedItem();
            ((Fighter)hsObject).fighterEventHolds.attackLightUpAir = attackLightUpAir.getSelectedIndex() == 0 ? null : (FighterHold)attackLightUpAir.getSelectedItem();
            ((Fighter)hsObject).fighterEventHolds.attackLightForwardAir = attackLightForwardAir.getSelectedIndex() == 0 ? null : (FighterHold)attackLightForwardAir.getSelectedItem();
            ((Fighter)hsObject).fighterEventHolds.attackLightBackwardAir = attackLightBackwardAir.getSelectedIndex() == 0 ? null : (FighterHold)attackLightBackwardAir.getSelectedItem();
            ((Fighter)hsObject).fighterEventHolds.attackLightQCFAir = attackLightQCFAir.getSelectedIndex() == 0 ? null : (FighterHold)attackLightQCFAir.getSelectedItem();
            
            ((Fighter)hsObject).fighterEventHolds.attackHeavyNeutralGround = attackHeavyNeutralGround.getSelectedIndex() == 0 ? null : (FighterHold)attackHeavyNeutralGround.getSelectedItem();
            ((Fighter)hsObject).fighterEventHolds.attackHeavyDownGround = attackHeavyDownGround.getSelectedIndex() == 0 ? null : (FighterHold)attackHeavyDownGround.getSelectedItem();
            ((Fighter)hsObject).fighterEventHolds.attackHeavyUpGround = attackHeavyUpGround.getSelectedIndex() == 0 ? null : (FighterHold)attackHeavyUpGround.getSelectedItem();
            ((Fighter)hsObject).fighterEventHolds.attackHeavyForwardGround = attackHeavyForwardGround.getSelectedIndex() == 0 ? null : (FighterHold)attackHeavyForwardGround.getSelectedItem();
            ((Fighter)hsObject).fighterEventHolds.attackHeavyQCFGround = attackHeavyQCFGround.getSelectedIndex() == 0 ? null : (FighterHold)attackHeavyQCFGround.getSelectedItem();
            ((Fighter)hsObject).fighterEventHolds.attackHeavyNeutralAir = attackHeavyNeutralAir.getSelectedIndex() == 0 ? null : (FighterHold)attackHeavyNeutralAir.getSelectedItem();
            ((Fighter)hsObject).fighterEventHolds.attackHeavyDownAir = attackHeavyDownAir.getSelectedIndex() == 0 ? null : (FighterHold)attackHeavyDownAir.getSelectedItem();
            ((Fighter)hsObject).fighterEventHolds.attackHeavyUpAir = attackHeavyUpAir.getSelectedIndex() == 0 ? null : (FighterHold)attackHeavyUpAir.getSelectedItem();
            ((Fighter)hsObject).fighterEventHolds.attackHeavyForwardAir = attackHeavyForwardAir.getSelectedIndex() == 0 ? null : (FighterHold)attackHeavyForwardAir.getSelectedItem();
            ((Fighter)hsObject).fighterEventHolds.attackHeavyBackwardAir = attackHeavyBackwardAir.getSelectedIndex() == 0 ? null : (FighterHold)attackHeavyBackwardAir.getSelectedItem();
            ((Fighter)hsObject).fighterEventHolds.attackHeavyQCFAir = attackHeavyQCFAir.getSelectedIndex() == 0 ? null : (FighterHold)attackHeavyQCFAir.getSelectedItem();
            
            ((Fighter)hsObject).fighterEventHolds.knockout = knockout.getSelectedIndex() == 0 ? null : (FighterHold)knockout.getSelectedItem();
        }
        
        parent.repaint();
        
        applyButton.setEnabled(false);
    }
    
    private void closeWindow()
    {
        this.dispose();
    }
    
    private void okButtonPressed()
    {
        applyChanges();
        closeWindow();
    }
    
    private void closeButtonPressed()
    {
        closeWindow();
    }
    
    private void applyButtonPressed()
    {
        applyChanges();
    }
    
    public void fieldChanged()
    {
        applyButton.setEnabled(true);
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        switch(e.getActionCommand())
        {
            case "okButton": okButtonPressed(); break;
            case "closeButton": closeButtonPressed(); break;
            case "applyButton": applyButtonPressed(); break;
            case "fieldChanged": fieldChanged();
        }
    }
    
    @Override
    public void itemStateChanged(ItemEvent e)
    {
        fieldChanged();
    }
}
