/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homestrifeeditor;

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
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Darlos9D
 */
public class HitboxAttributesWindow extends JFrame implements ActionListener, ChangeListener {
    private static int windowWidth = 300;
    private static int windowHeight = 140;
    private static int windowBorderBuffer = 10;
    
    private static int gridWidth = 650;
    private static int gridRowHeight = 45;
    private static int gridColumns = 4;
    private static int gridHorizontalGap = 10;
    private static int gridVerticalGap = 5;
    
    private HSBoxLabel hitboxLabel;
    
    private JSpinner offsetXSpinner;
    private JSpinner offsetYSpinner;
    private JSpinner widthSpinner;
    private JSpinner heightSpinner;
    
    public HitboxAttributesWindow(HSBoxLabel theHitboxLabel)
    {
        hitboxLabel = theHitboxLabel;
        
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
        offsetXSpinner.setValue((int)hitboxLabel.box.offset.x);
        offsetXSpinner.addChangeListener(this);
        
        JLabel offsetYLabel = new JLabel("Offset Y");
        offsetYSpinner = new JSpinner(new SpinnerNumberModel(0, -99999, 99999, 1));
        offsetYSpinner.setValue((int)hitboxLabel.box.offset.y);
        offsetYSpinner.addChangeListener(this);
        
        JLabel widthLabel = new JLabel("Width");
        widthSpinner = new JSpinner(new SpinnerNumberModel(0, -99999, 99999, 1));
        widthSpinner.setValue((int)hitboxLabel.box.width);
        widthSpinner.addChangeListener(this);
        
        JLabel heightLabel = new JLabel("Height");
        heightSpinner = new JSpinner(new SpinnerNumberModel(0, -99999, 99999, 1));
        heightSpinner.setValue((int)hitboxLabel.box.height);
        heightSpinner.addChangeListener(this);
        
        JPanel valueInterface = new JPanel(new GridLayout(2, gridColumns, gridHorizontalGap, gridVerticalGap));
        valueInterface.setSize(gridWidth, gridRowHeight * 2);
        valueInterface.add(offsetXLabel);
        valueInterface.add(offsetXSpinner);
        valueInterface.add(widthLabel);
        valueInterface.add(widthSpinner);
        valueInterface.add(offsetYLabel);
        valueInterface.add(offsetYSpinner);
        valueInterface.add(heightLabel);
        valueInterface.add(heightSpinner);
        
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
        hitboxLabel.box.offset.x = (int)offsetXSpinner.getValue();
        hitboxLabel.box.offset.y = (int)offsetYSpinner.getValue();
        hitboxLabel.box.width = (int)widthSpinner.getValue();
        hitboxLabel.box.height = (int)heightSpinner.getValue();
        
        Point pos = hitboxLabel.parent.parent.getSwingOffset(hitboxLabel.box.offset.x, hitboxLabel.box.offset.y);
        hitboxLabel.setBounds(pos.x, pos.y, (int)hitboxLabel.box.width, (int)hitboxLabel.box.height);
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
