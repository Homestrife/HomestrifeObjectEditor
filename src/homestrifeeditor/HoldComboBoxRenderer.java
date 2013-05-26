/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homestrifeeditor;

import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 *
 * @author Darlos9D
 */
public class HoldComboBoxRenderer extends JLabel implements ListCellRenderer {
    @Override
    public Component getListCellRendererComponent(
                                       JList list,
                                       Object value,
                                       int index,
                                       boolean isSelected,
                                       boolean cellHasFocus) {
        if (isSelected || cellHasFocus) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        
        
        String s;
        if(index < 0)
        {
            s = ((HSObjectHold)value).name;
        }
        else if(index == 0)
        {
            s = "NONE";
        }
        else
        {
            s = (index - 1) + " - " + ((HSObjectHold)value).name;
        }
        
        setText(s);

        return this;
    }
}
