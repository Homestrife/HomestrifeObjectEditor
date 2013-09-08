/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homestrifeeditor.objects.holds.properties;

/**
 *
 * @author Darlos9D
 */
public class HSPalette {
    public String name;
    public String path;
    public int id;
    
    public HSPalette()
    {
        name = "";
        path = "";
        id = 0;
    }
    
    public HSPalette(String n, String p, int i) {
    	name = n;
    	path = p;
    	id = i;
    }
    
    public String toString() {
    	return id + ": " + name;
    }
}
