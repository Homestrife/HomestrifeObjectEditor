/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homestrifeeditor;

/**
 *
 * @author Darlos9D
 */
public class HSPalette {
    public String name;
    public String path;
    
    public HSPalette()
    {
        name = "";
        path = "";
    }
    
    public HSPalette(String n, String p) {
    	name = n;
    	path = p;
    }
    
    public String toString() {
    	return name;
    }
}
