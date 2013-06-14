/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homestrifeeditor;

import java.awt.Color;
import java.awt.Component;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.ListCellRenderer;
import javax.swing.JList;

/**
 *
 * @author Darlos9D
 */
public class SpawnListCellRenderer extends JLabel implements ListCellRenderer<Object> {
    @Override
    public Component getListCellRendererComponent(
        JList<?> list,           // the list
        Object value,            // value to display
        int index,               // cell index
        boolean isSelected,      // is the cell selected
        boolean cellHasFocus)    // does the cell have focus
    {
        File file = new File(((SpawnObject)value).defFilePath);
        
        String s = file.getName();
        setText(s);
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        
        if(cellHasFocus)
        {
            setBorder(BorderFactory.createDashedBorder(Color.BLACK));
        }
        else
        {
            setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        }
        
        setEnabled(list.isEnabled());
        setFont(list.getFont());
        setOpaque(true);
        return this;
    }
}
