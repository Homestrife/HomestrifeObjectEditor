/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homestrifeeditor.windows;

import homestrifeeditor.objects.holds.properties.HSTextureLabel;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Darlos9D
 */
public class TextureAttributesWindow extends JFrame implements ActionListener, ChangeListener {
	private static final long serialVersionUID = 1L;
	
	private static int windowWidth = 150;
    private static int windowHeight = 140;
    private static int windowBorderBuffer = 10;
    
    private static int gridWidth = 650;
    private static int gridRowHeight = 45;
    private static int gridColumns = 2;
    private static int gridHorizontalGap = 10;
    private static int gridVerticalGap = 5;
    
    private HSTextureLabel textureLabel;
    
    private JSpinner offsetXSpinner;
    private JSpinner offsetYSpinner;
    
    public TextureAttributesWindow(HSTextureLabel theTextureLabel)
    {
        textureLabel = theTextureLabel;
        
        setTitle("Hitbox Attributes");
        setSize(windowWidth, windowHeight);
        setLocationRelativeTo(null);
        this.setResizable(false);
        
        createWindowContents();
    }
    
    private void createWindowContents()
    {
        JLabel offsetXLabel = new JLabel("Offset X");
        offsetXSpinner = new JSpinner(new SpinnerNumberModel(0, -99999, 99999, 1));
        offsetXSpinner.setValue((int)textureLabel.texture.offset.x);
        offsetXSpinner.addChangeListener(this);
        
        JLabel offsetYLabel = new JLabel("Offset Y");
        offsetYSpinner = new JSpinner(new SpinnerNumberModel(0, -99999, 99999, 1));
        offsetYSpinner.setValue((int)textureLabel.texture.offset.y);
        offsetYSpinner.addChangeListener(this);
        
        JPanel valueInterface = new JPanel(new GridLayout(2, gridColumns, gridHorizontalGap, gridVerticalGap));
        valueInterface.setSize(gridWidth, gridRowHeight * 2);
        valueInterface.add(offsetXLabel);
        valueInterface.add(offsetXSpinner);
        valueInterface.add(offsetYLabel);
        valueInterface.add(offsetYSpinner);
        
        JButton closeButton = new JButton("Close");
        closeButton.setActionCommand("closeButton");
        closeButton.addActionListener(this);
        
        JPanel buttonPane = new JPanel(new FlowLayout(FlowLayout.TRAILING));
        buttonPane.add(closeButton);
        
        JPanel hitboxAttributesPane = new JPanel();
        hitboxAttributesPane.setLayout(new BoxLayout(hitboxAttributesPane, BoxLayout.Y_AXIS));
        hitboxAttributesPane.setBorder(new EmptyBorder(windowBorderBuffer, windowBorderBuffer, windowBorderBuffer, windowBorderBuffer));
        hitboxAttributesPane.add(valueInterface);
        hitboxAttributesPane.add(buttonPane);
        
        add(hitboxAttributesPane);
    }
    
    private void closeWindow()
    {
        this.dispose();
    }
    
    private void fieldChanged()
    {
        textureLabel.texture.offset.x = (int)offsetXSpinner.getValue();
        textureLabel.texture.offset.y = (int)offsetYSpinner.getValue();
        
        Point pos = textureLabel.parent.parent.getSwingOffset(textureLabel.texture.offset.x, textureLabel.texture.offset.y);
        textureLabel.setBounds(pos.x, pos.y, textureLabel.getWidth(), textureLabel.getHeight());
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        switch(e.getActionCommand())
        {
            case "closeButton": closeWindow(); break;
        }
    }
    
    @Override
    public void stateChanged(ChangeEvent e)
    {
        fieldChanged();
    }
}
