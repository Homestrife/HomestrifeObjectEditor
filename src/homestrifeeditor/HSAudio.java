/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homestrifeeditor;

/**
 *
 * @author Darlos9D
 */
public class HSAudio {
    public int delay;
    public String filePath;
    public boolean exclusive;
    public int percentage;
    public boolean usePercentage;
    
    public HSAudio (String theFilePath)
    {
        delay = 0;
        filePath = theFilePath;
        exclusive = false;
        percentage = 100;
        usePercentage = false;
    }
}
