/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homestrifeeditor.objects.holds.properties;

/**
 *
 * @author Darlos9D
 */
public class Cancels {
	public Cancel dash;
    public Cancel jump;

    public Cancel lightNeutral;
    public Cancel lightForward;
    public Cancel lightUp;
    public Cancel lightDown;
    public Cancel lightBackward;
    public Cancel lightQCF;

    public Cancel heavyNeutral;
    public Cancel heavyForward;
    public Cancel heavyUp;
    public Cancel heavyDown;
    public Cancel heavyBackward;
    public Cancel heavyQCF;
    
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
