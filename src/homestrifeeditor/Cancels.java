/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homestrifeeditor;

/**
 *
 * @author Darlos9D
 */
public class Cancels {
    Cancel dash;
    Cancel jump;

    Cancel lightNeutral;
    Cancel lightForward;
    Cancel lightUp;
    Cancel lightDown;
    Cancel lightBackward;
    Cancel lightQCF;

    Cancel heavyNeutral;
    Cancel heavyForward;
    Cancel heavyUp;
    Cancel heavyDown;
    Cancel heavyBackward;
    Cancel heavyQCF;
    
    public Cancels()
    {
        dash = Cancel.NEVER;
        jump = Cancel.NEVER;
        
        lightNeutral = Cancel.NEVER;
        lightForward = Cancel.NEVER;
        lightUp = Cancel.NEVER;
        lightDown = Cancel.NEVER;
        lightBackward = Cancel.NEVER;
        lightQCF = Cancel.NEVER;
        
        heavyNeutral = Cancel.NEVER;
        heavyForward = Cancel.NEVER;
        heavyUp = Cancel.NEVER;
        heavyDown = Cancel.NEVER;
        heavyBackward = Cancel.NEVER;
        heavyQCF = Cancel.NEVER;
    }
}
