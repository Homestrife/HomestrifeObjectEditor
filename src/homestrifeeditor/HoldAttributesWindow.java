/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homestrifeeditor;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * The attributes of the currently selected hold
 * @author Darlos9D
 */
public class HoldAttributesWindow extends JFrame implements ActionListener, ChangeListener, DocumentListener, ItemListener {
    private static int windowWidth = 800;
    private static int windowHeightGeneral = 600;
    private static int windowHeightTerrain = 360;
    private static int windowBorderBuffer = 10;
    
    private static int gridWidth = 650;
    private static int gridRowHeight = 45;
    private static int gridColumns = 4;
    private static int gridHorizontalGap = 10;
    private static int gridVerticalGap = 5;
    
    private HoldListPane parent;
    private HSObjectHold hold;
    
    private JTextField nameField;           private static String nameTooltip = "<html>The name to be displayed in the hold list.</html>";
    private JSpinner durationSpinner;       private static String durationTooltip = "<html>How many frames this hold lasts. The game runs at 60 frames per second.</html>";
    private JComboBox nextHoldCombo;        private static String nextHoldTooltip = "<html>Which hold this object should switch to once this hold's duration expires.<br>If this is set to NONE, the engine will determine the next hold some other way.</html>";
    private JButton holdSoundsButton;       private static String holdSoundsTooltip = "<html>Add/Edit sounds that occur in this hold</html>";
    
    private JCheckBox changeAttackBoxAttributesCheck; private static String changeAttackBoxAttributesTooltip = "<html>If this is unchecked, this hold will inherit the attack<br/>attributes of any hold that has this hold as its<br/>next hold.</html>";
    
    private JSpinner damageSpinner;         private static String damageTooltip = "<html>How much damage is done to another object with hurt boxes,<br>should one of this objects attack boxes collide with one of them.</html>";
    private JSpinner hitstunSpinner;        private static String hitstunTooltip = "<html>How many frames another fighter is rendered inactive<br>when struck by one of this object's attack boxes.</html>";
    private JSpinner blockstunSpinner;      private static String blockstunTooltip = "<html>How many frames another fighter is rendered inactive<br>when struck by one of this object's attack boxes while blocking.</html>";
    
    private JSpinner forceXSpinner;         private static String forceXTooltip = "<html>When another physics object is struck by one of this object's<br>attack boxes, its X velocity is set to this value.</html>";
    private JSpinner forceYSpinner;         private static String forceYTooltip = "<html>When another physics object is struck by one of this object's<br>attack boxes, its Y velocity is set to this value.</html>";
    private JCheckBox tripsCheck;           private static String tripsTooltip = "<html>If checked, another fighter will fall prone if struck by one of this object's attack boxes.</html>";
    
    private JComboBox blockabilityCombo;    private static String blockabilityTooltip = "<html>Unblockable: This object's attack boxes cannot be blocked.<br>High: This object's attack boxes must be blocked while standing or jumping.<br>Low: This object's attack boxes must be blocked while crouching or jumping.<br>Mid: This object's attack boxes can be blocked while crouching, standing, or jumping.</html>";
    private JCheckBox directionBlockCheck;  private static String directionBlockTooltip = "<html>If checked, another figher must be facing in the opposite<br>direction that this object is facing in order to block this<br>object's attack boxes.<br>Normally, whether this object's attack boxes must be blocked while<br>facing left or right is determined by which side of the defending<br>fighter this object is on, regardless of which way this object is facing.</html>";
    private JCheckBox reverseBlockCheck;    private static String reverseBlockTooltip = "<html>If checked, another fighter must face the direction opposite<br>of what they would normally need to in order to block this object's attack boxes.</html>";
    private JButton hitSoundsButton;        private static String hitSoundsTooltip = "<html>Add/Edit sounds that occur upon an attack box collision</html>";
    
    private JPanel terrainInterface;
    
    private JButton applyButton;
    
    public HoldAttributesWindow(HoldListPane theParent, HSObjectHold theHold)
    {
        parent = theParent;
        hold = theHold;
        
        setTitle("Hold Attributes - " + hold.name);
        setSize(windowWidth, windowHeightGeneral);
        setLocationRelativeTo(null);
        this.setResizable(false);
       
        createWindowContents();
    }
    
    private void createWindowContents()
    {
        JLabel nameLabel = new JLabel("Name");
        nameLabel.setToolTipText(nameTooltip);
        nameField = new JTextField(hold.name);
        nameField.setToolTipText(nameTooltip);
        nameField.getDocument().addDocumentListener(this);
        
        JLabel nextHoldLabel = new JLabel("Next Hold");
        nextHoldLabel.setToolTipText(nextHoldTooltip);
        HoldComboBoxRenderer holdComboRenderer = new HoldComboBoxRenderer();
        HSObjectHold[] allHolds = parent.getAllHolds();
        HSObjectHold[] allHoldsPlusNull = new HSObjectHold[allHolds.length + 1];
        HSObjectHold nullHold = new HSObjectHold();
        nullHold.name = "NONE";
        allHoldsPlusNull[0] = nullHold;
        for (int i = 0; i < allHolds.length; i++)
        {
            allHoldsPlusNull[i + 1] = allHolds[i];
        }
        nextHoldCombo = new JComboBox(allHoldsPlusNull);
        nextHoldCombo.setRenderer(holdComboRenderer);
        if(hold.nextHold == null)
        {
            nextHoldCombo.setSelectedIndex(0);
        }
        else
        {
            nextHoldCombo.setSelectedItem(hold.nextHold);
        }
        nextHoldCombo.setToolTipText(nextHoldTooltip);
        nextHoldCombo.setActionCommand("fieldChanged");
        nextHoldCombo.addItemListener(this);
        
        JLabel durationLabel = new JLabel("Duration");
        durationLabel.setToolTipText(durationTooltip);
        durationSpinner = new JSpinner(new SpinnerNumberModel(4, 0, 99999, 1));
        durationSpinner.setToolTipText(durationTooltip);
        durationSpinner.setValue(hold.duration);
        durationSpinner.addChangeListener(this);
        
        holdSoundsButton = new JButton("Hold Sounds...");
        holdSoundsButton.setToolTipText(holdSoundsTooltip);
        holdSoundsButton.setActionCommand("holdSoundsButton");
        holdSoundsButton.addActionListener(this);
        
        JPanel graphicInterface = new JPanel(new GridLayout(2, gridColumns, gridHorizontalGap, gridVerticalGap));
        graphicInterface.setSize(gridWidth, gridRowHeight * 2);
        graphicInterface.setBorder(new TitledBorder("General Attributes"));
        graphicInterface.add(nameLabel);
        graphicInterface.add(nameField);
        graphicInterface.add(durationLabel);
        graphicInterface.add(durationSpinner);
        graphicInterface.add(nextHoldLabel);
        graphicInterface.add(nextHoldCombo);
        graphicInterface.add(new JLabel(""));
        graphicInterface.add(holdSoundsButton);
        
        JPanel holdAttributesPane = new JPanel();
        holdAttributesPane.setLayout(new BoxLayout(holdAttributesPane, BoxLayout.Y_AXIS));
        holdAttributesPane.setBorder(new EmptyBorder(windowBorderBuffer, windowBorderBuffer, windowBorderBuffer, windowBorderBuffer));
        holdAttributesPane.add(graphicInterface);
        
        if(hold.IsTerrainObjectHold())
        {
            setSize(windowWidth, windowHeightTerrain);
            
            TerrainObjectHold toHold = (TerrainObjectHold)hold;
            
            changeAttackBoxAttributesCheck = new JCheckBox("Change attack box attributes this hold", toHold.changeAttackBoxAttributes);
            changeAttackBoxAttributesCheck.setToolTipText(changeAttackBoxAttributesTooltip);
            changeAttackBoxAttributesCheck.setActionCommand("changeAttackBoxAttributesChanged");
            changeAttackBoxAttributesCheck.addActionListener(this);
            
            JPanel changeAttackBoxAttributesInterface = new JPanel(new GridLayout(1, gridColumns, gridHorizontalGap, gridVerticalGap));
            changeAttackBoxAttributesInterface.setSize(gridWidth, gridRowHeight);
            changeAttackBoxAttributesInterface.add(changeAttackBoxAttributesCheck);

            holdAttributesPane.add(changeAttackBoxAttributesInterface);
            
            JLabel blockabilityLabel = new JLabel("Blockability");
            blockabilityLabel.setToolTipText(blockabilityTooltip);
            blockabilityCombo = new JComboBox(Blockability.values());
            blockabilityCombo.setToolTipText(blockabilityTooltip);
            blockabilityCombo.setSelectedItem(toHold.blockability);
            blockabilityCombo.setActionCommand("fieldChanged");
            blockabilityCombo.addItemListener(this);
            JLabel directionBlockLabel = new JLabel("Direction Block");
            directionBlockLabel.setToolTipText(directionBlockTooltip);
            directionBlockCheck = new JCheckBox("", toHold.horizontalDirectionBasedBlock);
            directionBlockCheck.setToolTipText(directionBlockTooltip);
            directionBlockCheck.setActionCommand("fieldChanged");
            directionBlockCheck.addActionListener(this);
            JLabel reverseBlockLabel = new JLabel("Reverse Block");
            reverseBlockLabel.setToolTipText(reverseBlockTooltip);
            reverseBlockCheck = new JCheckBox("", toHold.reversedHorizontalBlock);
            reverseBlockCheck.setToolTipText(reverseBlockTooltip);
            reverseBlockCheck.setActionCommand("fieldChanged");
            reverseBlockCheck.addActionListener(this);
            JLabel damageLabel = new JLabel("Damage");
            damageLabel.setToolTipText(damageTooltip);
            damageSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 99999, 1));
            damageSpinner.setToolTipText(damageTooltip);
            damageSpinner.setValue(toHold.damage);
            damageSpinner.addChangeListener(this);
            JLabel hitstunLabel = new JLabel("Hit Stun");
            hitstunLabel.setToolTipText(hitstunTooltip);
            hitstunSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 99999, 1));
            hitstunSpinner.setToolTipText(hitstunTooltip);
            hitstunSpinner.setValue(toHold.hitstun);
            hitstunSpinner.addChangeListener(this);
            JLabel blockstunLabel = new JLabel("Block Stun");
            blockstunLabel.setToolTipText(blockstunTooltip);
            blockstunSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 99999, 1));
            blockstunSpinner.setToolTipText(blockstunTooltip);
            blockstunSpinner.setValue(toHold.blockstun);
            blockstunSpinner.addChangeListener(this);
            
            Float value = new Float(0.0);
            Float min = new Float(-99999.0);
            Float max = new Float(99999.0);
            Float stepSize = new Float(1);
            JLabel forceXLabel = new JLabel("Horizontal Force");
            forceXLabel.setToolTipText(forceXTooltip);
            forceXSpinner = new JSpinner(new SpinnerNumberModel(value, min, max, stepSize));
            forceXSpinner.setToolTipText(forceXTooltip);
            forceXSpinner.setValue(toHold.force.x);
            forceXSpinner.addChangeListener(this);
            JLabel forceYLabel = new JLabel("Vertical Force");
            forceYLabel.setToolTipText(forceYTooltip);
            forceYSpinner = new JSpinner(new SpinnerNumberModel(value, min, max, stepSize));
            forceYSpinner.setToolTipText(forceYTooltip);
            forceYSpinner.setValue(toHold.force.y);
            forceYSpinner.addChangeListener(this);
            
            JLabel tripsLabel = new JLabel("Trips");
            tripsLabel.setToolTipText(tripsTooltip);
            tripsCheck = new JCheckBox("", toHold.trips);
            tripsCheck.setToolTipText(tripsTooltip);
            tripsCheck.setActionCommand("fieldChanged");
            tripsCheck.addActionListener(this);
        
            hitSoundsButton = new JButton("Hit Sounds...");
            hitSoundsButton.setToolTipText(hitSoundsTooltip);
            hitSoundsButton.setActionCommand("hitSoundsButton");
            hitSoundsButton.addActionListener(this);

            terrainInterface = new JPanel(new GridLayout(5, gridColumns, gridHorizontalGap, gridVerticalGap));
            terrainInterface.setSize(gridWidth, gridRowHeight * 5);
            terrainInterface.setBorder(new TitledBorder("Attack Box Attributes"));
            terrainInterface.add(damageLabel);
            terrainInterface.add(damageSpinner);
            terrainInterface.add(hitstunLabel);
            terrainInterface.add(hitstunSpinner);
            terrainInterface.add(blockabilityLabel);
            terrainInterface.add(blockabilityCombo);
            terrainInterface.add(blockstunLabel);
            terrainInterface.add(blockstunSpinner);
            terrainInterface.add(forceXLabel);
            terrainInterface.add(forceXSpinner);
            terrainInterface.add(forceYLabel);
            terrainInterface.add(forceYSpinner);
            terrainInterface.add(tripsLabel);
            terrainInterface.add(tripsCheck);
            terrainInterface.add(directionBlockLabel);
            terrainInterface.add(directionBlockCheck);
            terrainInterface.add(reverseBlockLabel);
            terrainInterface.add(reverseBlockCheck);
            terrainInterface.add(new JLabel(""));
            terrainInterface.add(hitSoundsButton);
            setTerrainInterfaceEnabled();

            holdAttributesPane.add(terrainInterface);
        }
        
        JButton okButton = new JButton("OK");
        okButton.setActionCommand("okButton");
        okButton.addActionListener(this);
        JButton closeButton = new JButton("Close");
        closeButton.setActionCommand("closeButton");
        closeButton.addActionListener(this);
        applyButton = new JButton("Apply");
        applyButton.setActionCommand("applyButton");
        applyButton.addActionListener(this);
        applyButton.setEnabled(false);
        
        JPanel buttonPane = new JPanel(new FlowLayout(FlowLayout.TRAILING));
        buttonPane.add(okButton);
        buttonPane.add(closeButton);
        buttonPane.add(applyButton);
        
        holdAttributesPane.add(buttonPane);
        
        add(holdAttributesPane);
    }
    
    private void setTerrainInterfaceEnabled()
    {
        if(hold.IsTerrainObjectHold())
        {
            TerrainObjectHold toHold = (TerrainObjectHold)hold;
            
            terrainInterface.setEnabled(changeAttackBoxAttributesCheck.isSelected());
            
            Component[] components = terrainInterface.getComponents();
            for(int i = 0; i < components.length; i++)
            {
                components[i].setEnabled(changeAttackBoxAttributesCheck.isSelected());
            }
        }
    }
    
    public void applyChanges()
    {
        hold.name = nameField.getText();
        hold.duration = (int)durationSpinner.getValue();
        if(nextHoldCombo.getSelectedIndex() == 0)
        {
            hold.nextHold = null;
        }
        else
        {
            hold.nextHold = (HSObjectHold)nextHoldCombo.getSelectedItem();
        }
        
        if(hold.IsTerrainObjectHold())
        {
            ((TerrainObjectHold)hold).changeAttackBoxAttributes = changeAttackBoxAttributesCheck.isSelected();
            ((TerrainObjectHold)hold).damage = (int)damageSpinner.getValue();
            ((TerrainObjectHold)hold).hitstun = (int)hitstunSpinner.getValue();
            ((TerrainObjectHold)hold).blockstun = (int)blockstunSpinner.getValue();
            ((TerrainObjectHold)hold).force.x = (float)forceXSpinner.getValue();
            ((TerrainObjectHold)hold).force.y = (float)forceYSpinner.getValue();
            ((TerrainObjectHold)hold).trips = tripsCheck.isSelected();
            ((TerrainObjectHold)hold).blockability = (Blockability)blockabilityCombo.getSelectedItem();
            ((TerrainObjectHold)hold).horizontalDirectionBasedBlock = directionBlockCheck.isSelected();
            ((TerrainObjectHold)hold).reversedHorizontalBlock = reverseBlockCheck.isSelected();
        }
        
        parent.repaint();
        
        setTitle("Hold Attributes - " + hold.name);
        applyButton.setEnabled(false);
    }
    
    private void closeWindow()
    {
        this.dispose();
    }
    
    private void holdSoundsButtonPressed()
    {
        SoundsWindow window = new SoundsWindow(this, hold, "hold");
        window.setVisible(true);
    }
    
    private void okButtonPressed()
    {
        applyChanges();
        closeWindow();
    }
    
    private void closeButtonPressed()
    {
        closeWindow();
    }
    
    private void applyButtonPressed()
    {
        applyChanges();
    }
    
    public void fieldChanged()
    {
        applyButton.setEnabled(true);
    }
    
    private void changeAttackBoxAttributesChanged()
    {
        setTerrainInterfaceEnabled();
        fieldChanged();
    }
    
    private void hitSoundsButtonPressed()
    {
        SoundsWindow window = new SoundsWindow(this, hold, "hit");
        window.setVisible(true);
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        switch(e.getActionCommand())
        {
            case "holdSoundsButton": holdSoundsButtonPressed(); break;
            case "okButton": okButtonPressed(); break;
            case "closeButton": closeButtonPressed(); break;
            case "applyButton": applyButtonPressed(); break;
            case "fieldChanged": fieldChanged(); break;
            case "changeAttackBoxAttributesChanged": changeAttackBoxAttributesChanged(); break;
            case "hitSoundsButton": hitSoundsButtonPressed(); break;
        }
    }
    
    @Override
    public void stateChanged(ChangeEvent e)
    {
        fieldChanged();
    }
    
    @Override
    public void changedUpdate(DocumentEvent e)
    {
        fieldChanged();
    }
    
    @Override
    public void removeUpdate(DocumentEvent e)
    {
        fieldChanged();
    }
    
    @Override
    public void insertUpdate(DocumentEvent e)
    {
        fieldChanged();
    }
    
    @Override
    public void itemStateChanged(ItemEvent e)
    {
        fieldChanged();
    }
}
