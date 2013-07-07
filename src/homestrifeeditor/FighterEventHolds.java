/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homestrifeeditor;

/**
 *
 * @author Darlos9D
 */
public class FighterEventHolds {
    public FighterHold standing;
    public FighterHold turn;
    public FighterHold walk;
    public FighterHold walking;
    public FighterHold walkingTurn;
    public FighterHold run;
    public FighterHold running;
    public FighterHold runningTurn;
    public FighterHold runningStop;
    public FighterHold crouch;
    public FighterHold crouching;
    public FighterHold crouchingTurn;
    public FighterHold stand;

    public FighterHold jumpNeutralStart;
    public FighterHold jumpNeutralStartAir;
    public FighterHold jumpNeutralRising;
    public FighterHold jumpNeutralFall;
    public FighterHold jumpNeutralFalling;
    public FighterHold jumpNeutralLand;
    //public FighterHold jumpNeutralLandHard;
    //public FighterHold jumpForwardStart;
    //public FighterHold jumpForwardRising;
    //public FighterHold jumpForwardFall;
    //public FighterHold jumpForwardFalling;
    //public FighterHold jumpForwardLand;
    //public FighterHold jumpForwardLandHard;
    //public FighterHold jumpBackwardStart;
    public FighterHold jumpBackwardRising;
    public FighterHold jumpBackwardFall;
    //public FighterHold jumpBackwardFalling;
    //public FighterHold jumpBackwardLand;
    //public FighterHold jumpBackwardLandHard;

    public FighterHold airDashForward;
    public FighterHold airDashBackward;

    //public FighterHold ledgeGrab;
    //public FighterHold ledgeClimb;
    
    public FighterHold blockHigh;
    public FighterHold blockLow;
    public FighterHold blockAir;

    //public FighterHold hitstunHighStandingStart;
    public FighterHold hitstunLightHighStanding;
    //public FighterHold hitstunHighStandingEnd;
    //public FighterHold hitstunMidStandingStart;
    public FighterHold hitstunLightMidStanding;
    //public FighterHold hitstunMidStandingEnd;
    //public FighterHold hitstunLowStandingStart;
    public FighterHold hitstunLightLowStanding;
    //public FighterHold hitstunLowStandingEnd;
    //public FighterHold hitstunMidCrouchingStart;
    public FighterHold hitstunLightMidCrouching;
    //public FighterHold hitstunMidCrouchingEnd;
    //public FighterHold hitstunLowCrouchingStart;
    public FighterHold hitstunLightLowCrouching;
    //public FighterHold hitstunLowCrouchingEnd;
    public FighterHold hitstunLightAir;
//    public FighterHold hitstunHeavyHighStanding;
//    public FighterHold hitstunHeavyMidStanding;
//    public FighterHold hitstunHeavyLowStanding;
//    public FighterHold hitstunHeavyMidCrouching;
//    public FighterHold hitstunHeavyLowCrouching;
//    public FighterHold hitstunHeavyAir;
    //public FighterHold tripForward;
    //public FighterHold tripBackward;
    //public FighterHold proneFaceUp;
    //public FighterHold proneFaceUpStand;
    //public FighterHold proneFaceDown;
    //public FighterHold proneFaceDownStand;
    //public FighterHold crumpleStart;
    //public FighterHold crumple;

//    public FighterHold airbornFaceUpRising;
//    public FighterHold airbornFaceUpFall;
//    public FighterHold airbornFaceUpFalling;
//    public FighterHold airbornFaceUpRise;
//    public FighterHold airbornFaceDownRising;
//    public FighterHold airbornFaceDownFall;
//    public FighterHold airbornFaceDownFalling;
//    public FighterHold airbornFaceDownRise;
//    public FighterHold airbornFaceSideMovingForward;
//    public FighterHold airbornFaceSideMoveBackward;
//    public FighterHold airbornFaceSideMovingBackward;
//    public FighterHold airbornFaceSideMoveForward;
    
    public FighterHold attackLightNeutralGround;
    public FighterHold attackLightDownGround;
    public FighterHold attackLightUpGround;
    public FighterHold attackLightForwardGround;
    public FighterHold attackLightQCFGround;
    public FighterHold attackLightNeutralAir;
    public FighterHold attackLightDownAir;
    public FighterHold attackLightUpAir;
    public FighterHold attackLightForwardAir;
    public FighterHold attackLightBackwardAir;
    public FighterHold attackLightQCFAir;
    
    public FighterHold attackHeavyNeutralGround;
    public FighterHold attackHeavyDownGround;
    public FighterHold attackHeavyUpGround;
    public FighterHold attackHeavyForwardGround;
    public FighterHold attackHeavyQCFGround;
    public FighterHold attackHeavyNeutralAir;
    public FighterHold attackHeavyDownAir;
    public FighterHold attackHeavyUpAir;
    public FighterHold attackHeavyForwardAir;
    public FighterHold attackHeavyBackwardAir;
    public FighterHold attackHeavyQCFAir;
    
    public FighterHold knockout;
    
    public FighterEventHolds()
    {
        standing = null;
        turn = null;
        walk = null;
        walking = null;
        walkingTurn = null;
        run = null;
        running = null;
        runningTurn = null;
        runningStop = null;
        crouch = null;
        crouching = null;
        crouchingTurn = null;
        stand = null;

        jumpNeutralStart = null;
        jumpNeutralStartAir = null;
        jumpNeutralRising = null;
        jumpNeutralFall = null;
        jumpNeutralFalling = null;
        jumpNeutralLand = null;
//        jumpNeutralLandHard = null;
//        jumpForwardStart = null;
//        jumpForwardRising = null;
//        jumpForwardFall = null;
//        jumpForwardFalling = null;
//        jumpForwardLand = null;
//        jumpForwardLandHard = null;
//        jumpBackwardStart = null;
        jumpBackwardRising = null;
        jumpBackwardFall = null;
//        jumpBackwardFalling = null;
//        jumpBackwardLand = null;
//        jumpBackwardLandHard = null;

        airDashForward = null;
        airDashBackward = null;
        
        blockHigh = null;
        blockLow = null;
        blockAir = null;

//        ledgeGrab = null;
//        ledgeClimb = null;

//        hitstunHighStandingStart = null;
        hitstunLightHighStanding = null;
//        hitstunHighStandingEnd = null;
        hitstunLightMidStanding = null;
//        hitstunLowStandingStart = null;
        hitstunLightLowStanding = null;
//        hitstunLowStandingEnd = null;
//        hitstunCrouchingStart = null;
          hitstunLightMidCrouching = null;
//        hitstunCrouchingEnd = null;
          hitstunLightAir = null;
//        tripForward = null;
//        tripBackward = null;
//        proneFaceUp = null;
//        proneFaceUpStand = null;
//        proneFaceDown = null;
//        proneFaceDownStand = null;
//        crumpleStart = null;
//        crumple = null;

//        airbornFaceUpRising = null;
//        airbornFaceUpFall = null;
//        airbornFaceUpFalling = null;
//        airbornFaceUpRise = null;
//        airbornFaceDownRising = null;
//        airbornFaceDownFall = null;
//        airbornFaceDownFalling = null;
//        airbornFaceDownRise = null;
//        airbornFaceSideMovingForward = null;
//        airbornFaceSideMoveBackward = null;
//        airbornFaceSideMovingBackward = null;
//        airbornFaceSideMoveForward = null;
    
        attackLightNeutralGround = null;
        attackLightDownGround = null;
        attackLightUpGround = null;
        attackLightForwardGround = null;
        attackLightQCFGround = null;
        attackLightNeutralAir = null;
        attackLightDownAir = null;
        attackLightUpAir = null;
        attackLightForwardAir = null;
        attackLightBackwardAir = null;
        attackLightQCFAir = null;
    
        attackHeavyNeutralGround = null;
        attackHeavyDownGround = null;
        attackHeavyUpGround = null;
        attackHeavyForwardGround = null;
        attackHeavyQCFGround = null;
        attackHeavyNeutralAir = null;
        attackHeavyDownAir = null;
        attackHeavyUpAir = null;
        attackHeavyForwardAir = null;
        attackHeavyBackwardAir = null;
        attackHeavyQCFAir = null;
        
        knockout = null;
    }
}
