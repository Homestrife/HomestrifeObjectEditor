/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homestrifeeditor.windows;

import homestrifeeditor.objects.FighterObject;
import homestrifeeditor.objects.HSObject;
import homestrifeeditor.objects.PhysicsObject;
import homestrifeeditor.objects.TerrainObject;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
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
 * This window shows the attributes for the entire object, such as lifetime and health
 * @author Darlos9D
 */
public class ObjectAttributesWindow extends JFrame implements ActionListener, ChangeListener, DocumentListener, ItemListener {
	private static final long serialVersionUID = 1L;
	
	private static int windowWidth = 720;
    private static int windowHeightGeneral = 155;
    private static int windowHeightTerrain = 230;
    private static int windowHeightPhysics = 300;
    private static int windowHeightFighter = 470;
    private static int windowBorderBuffer = 10;
    
    private static int gridWidth = 700;
    private static int gridRowHeight = 45;
    private static int gridColumns = 4;
    private static int gridHorizontalGap = 10;
    private static int gridVerticalGap = 5;
    
    private EditorWindow parent;
    private HSObject hsObject;
    
    private JTextField nameField;                       private static String nameTooltip = "<html>The name of the object.</html>";
    private JSpinner lifetimeSpinner;
    
    private JSpinner healthSpinner;
    private JSpinner bounceSpinner;
    private JSpinner frictionSpinner;
    private JCheckBox takesTerrainDamageCheck;
    private JCheckBox fragileCheck;                     private static String fragileTooltip = "<html>Whether or not this object is destroyed upon ANY impact.</html>";
    
    private JSpinner massSpinner;
    private JCheckBox fallsCheck;
    private JSpinner maxFallSpeedSpinner;
    
    private JSpinner walkSpeedSpinner;
    private JSpinner runSpeedSpinner;
    private JSpinner jumpSpeedSpinner;
    private JSpinner forwardAirDashSpeedSpinner;
    private JSpinner backwardAirDashSpeedSpinner;
    private JSpinner forwardAirDashDurationSpinner;
    private JSpinner backwardAirDashDurationSpinner;
    private JSpinner stepHeightSpinner;
    private JSpinner airActionsSpinner;
    private JSpinner airControlAccelSpinner;
    private JSpinner maxAirControlSpeedSpinner;
    
    private JButton applyButton;
    
    public ObjectAttributesWindow(EditorWindow theParent, HSObject theHSObject)
    {
        parent = theParent;
        hsObject = theHSObject;
        
        setTitle("Object Attributes - " + hsObject.name);
        setSize(windowWidth, windowHeightGeneral);
        setLocationRelativeTo(null);
        this.setResizable(false);

        createWindowContents();
    }
    
    private void createWindowContents()
    {
        JLabel nameLabel = new JLabel("Name");
        nameLabel.setToolTipText(nameTooltip);
        nameField = new JTextField(hsObject.name);
        nameField.setToolTipText(nameTooltip);
        nameField.getDocument().addDocumentListener(this);
        JLabel lifetimeLabel = new JLabel("Life Time");
        lifetimeSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 99999, 1));
        lifetimeSpinner.setValue(hsObject.lifetime);
        lifetimeSpinner.addChangeListener(this);
        
        JPanel graphicInterface = new JPanel(new GridLayout(1, gridColumns, gridHorizontalGap, gridVerticalGap));
        graphicInterface.setSize(gridWidth, gridRowHeight);
        graphicInterface.setBorder(new TitledBorder("General Attributes"));
        graphicInterface.add(nameLabel);
        graphicInterface.add(nameField);
        graphicInterface.add(lifetimeLabel);
        graphicInterface.add(lifetimeSpinner);
        
        JPanel objectAttributesPane = new JPanel();
        objectAttributesPane.setLayout(new BoxLayout(objectAttributesPane, BoxLayout.Y_AXIS));
        objectAttributesPane.setBorder(new EmptyBorder(windowBorderBuffer, windowBorderBuffer, windowBorderBuffer, windowBorderBuffer));
        objectAttributesPane.add(graphicInterface);
        
        if(hsObject.IsTerrainObject())
        {
            setSize(windowWidth, windowHeightTerrain);
            
            TerrainObject tObject = (TerrainObject)hsObject;
            
            Float value = new Float(0.0);
            Float min = new Float(0);
            Float max = new Float(1);
            Float stepSize = new Float(0.1);
            
            JLabel healthLabel = new JLabel("Health");
            healthSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 99999, 1));
            healthSpinner.setValue(tObject.health);
            healthSpinner.addChangeListener(this);
            //
            JLabel bounceLabel = new JLabel("Bounce");
            bounceSpinner = new JSpinner(new SpinnerNumberModel(value, min, max, stepSize));
            bounceSpinner.setValue(tObject.bounce);
            bounceSpinner.addChangeListener(this);
            //
            JLabel frictionLabel = new JLabel("Friction");
            frictionSpinner = new JSpinner(new SpinnerNumberModel(value, min, max, stepSize));
            frictionSpinner.setValue(tObject.friction);
            frictionSpinner.addChangeListener(this);
            //
            JLabel takesTerrainDamageLabel = new JLabel("Takes Impact Damage");
            takesTerrainDamageCheck = new JCheckBox("", tObject.takesTerrainDamage);
            takesTerrainDamageCheck.setActionCommand("fieldChanged");
            takesTerrainDamageCheck.addActionListener(this);
            //
            JLabel fragileLabel = new JLabel("Fragile");
            fragileCheck = new JCheckBox("", tObject.fragile);
            fragileCheck.setActionCommand("fieldChanged");
            fragileCheck.addActionListener(this);
            
            JButton getHitSounds = new JButton("Sounds on Hit...");
            getHitSounds.setActionCommand("onHit");
            getHitSounds.addActionListener(this);

            JPanel terrainInterface = new JPanel(new GridLayout(0, gridColumns, gridHorizontalGap, gridVerticalGap));
            terrainInterface.setSize(gridWidth, gridRowHeight * 3);
            terrainInterface.setBorder(new TitledBorder("Health & Impact Attributes"));
            terrainInterface.add(healthLabel);
            terrainInterface.add(healthSpinner);
            terrainInterface.add(bounceLabel);
            terrainInterface.add(bounceSpinner);
            terrainInterface.add(takesTerrainDamageLabel);
            terrainInterface.add(takesTerrainDamageCheck);
            terrainInterface.add(frictionLabel);
            terrainInterface.add(frictionSpinner);
            terrainInterface.add(fragileLabel);
            terrainInterface.add(fragileCheck);
            terrainInterface.add(new JLabel(""));
            terrainInterface.add(getHitSounds);

            objectAttributesPane.add(terrainInterface);
        }
        
        if(hsObject.IsPhysicsObject())
        {
            setSize(windowWidth, windowHeightPhysics);
            
            PhysicsObject pObject = (PhysicsObject)hsObject;
            
            Float value = new Float(0.0);
            Float min = new Float(0);
            Float max = new Float(99999);
            Float stepSize = new Float(1);
            
            JLabel massLabel = new JLabel("Mass");
            massSpinner = new JSpinner(new SpinnerNumberModel(value, min, max, stepSize));
            massSpinner.setValue(pObject.mass);
            massSpinner.addChangeListener(this);
            JLabel fallsLabel = new JLabel("Falls");
            fallsCheck = new JCheckBox("", pObject.falls);
            fallsCheck.setActionCommand("fieldChanged");
            fallsCheck.addActionListener(this);
            JLabel maxFallSpeedLabel = new JLabel("Max Fall Speed");
            maxFallSpeedSpinner = new JSpinner(new SpinnerNumberModel(value, min, max, stepSize));
            maxFallSpeedSpinner.setValue(pObject.maxFallSpeed);
            maxFallSpeedSpinner.addChangeListener(this);

            JPanel physicsInterface = new JPanel(new GridLayout(2, gridColumns, gridHorizontalGap, gridVerticalGap));
            physicsInterface.setSize(gridWidth, gridRowHeight * 3);
            physicsInterface.setBorder(new TitledBorder("Physics Attributes"));
            physicsInterface.add(massLabel);
            physicsInterface.add(massSpinner);
            physicsInterface.add(maxFallSpeedLabel);
            physicsInterface.add(maxFallSpeedSpinner);
            physicsInterface.add(fallsLabel);
            physicsInterface.add(fallsCheck);
            physicsInterface.add(new JLabel(""));
            physicsInterface.add(new JLabel(""));

            objectAttributesPane.add(physicsInterface);
        }
        
        if(hsObject.IsFighter())
        {
            setSize(windowWidth, windowHeightFighter);
            
            FighterObject fighter = (FighterObject)hsObject;
            
            Float value = new Float(0.0);
            Float min = new Float(0);
            Float max = new Float(99999);
            Float stepSize = new Float(1);
            
            JLabel walkSpeedLabel = new JLabel("Walk Speed");
            walkSpeedSpinner = new JSpinner(new SpinnerNumberModel(value, min, max, stepSize));
            walkSpeedSpinner.setValue(fighter.walkSpeed);
            walkSpeedSpinner.addChangeListener(this);
            JLabel runSpeedLabel = new JLabel("Run Speed");
            runSpeedSpinner = new JSpinner(new SpinnerNumberModel(value, min, max, stepSize));
            runSpeedSpinner.setValue(fighter.runSpeed);
            runSpeedSpinner.addChangeListener(this);
            JLabel jumpSpeedLabel = new JLabel("Jump Speed");
            jumpSpeedSpinner = new JSpinner(new SpinnerNumberModel(value, min, max, stepSize));
            jumpSpeedSpinner.setValue(fighter.jumpSpeed);
            jumpSpeedSpinner.addChangeListener(this);
            JLabel forwardAirDashSpeedLabel = new JLabel("Forward Air Dash Speed");
            forwardAirDashSpeedSpinner = new JSpinner(new SpinnerNumberModel(value, min, max, stepSize));
            forwardAirDashSpeedSpinner.setValue(fighter.forwardAirDashSpeed);
            forwardAirDashSpeedSpinner.addChangeListener(this);
            JLabel backwardAirDashSpeedLabel = new JLabel("Backward Air Dash Speed");
            backwardAirDashSpeedSpinner = new JSpinner(new SpinnerNumberModel(value, min, max, stepSize));
            backwardAirDashSpeedSpinner.setValue(fighter.backwardAirDashSpeed);
            backwardAirDashSpeedSpinner.addChangeListener(this);
            JLabel forwardAirDashDurationLabel = new JLabel("Forward Air Dash Duration");
            forwardAirDashDurationSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 99999, 1));
            forwardAirDashDurationSpinner.setValue(fighter.forwardAirDashDuration);
            forwardAirDashDurationSpinner.addChangeListener(this);
            JLabel backwardAirDashDurationLabel = new JLabel("Backward Air Dash Duration");
            backwardAirDashDurationSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 99999, 1));
            backwardAirDashDurationSpinner.setValue(fighter.backwardAirDashDuration);
            backwardAirDashDurationSpinner.addChangeListener(this);
            JLabel stepHeightLabel = new JLabel("Step Height");
            stepHeightSpinner = new JSpinner(new SpinnerNumberModel(value, min, max, stepSize));
            stepHeightSpinner.setValue(fighter.stepHeight);
            stepHeightSpinner.addChangeListener(this);
            JLabel airActionsLabel = new JLabel("# of Air Actions");
            airActionsSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 99999, 1));
            airActionsSpinner.setValue(fighter.airActions);
            airActionsSpinner.addChangeListener(this);
            JLabel airControlAccelLabel = new JLabel("Air Control Accel");
            airControlAccelSpinner = new JSpinner(new SpinnerNumberModel(value, min, max, stepSize));
            airControlAccelSpinner.setValue(fighter.airControlAccel);
            airControlAccelSpinner.addChangeListener(this);
            JLabel maxAirControlSpeedLabel = new JLabel("Max Air Control Speed");
            maxAirControlSpeedSpinner = new JSpinner(new SpinnerNumberModel(value, min, max, stepSize));
            maxAirControlSpeedSpinner.setValue(fighter.maxAirControlSpeed);
            maxAirControlSpeedSpinner.addChangeListener(this);

            JPanel fighterInterface = new JPanel(new GridLayout(6, gridColumns, gridHorizontalGap, gridVerticalGap));
            fighterInterface.setSize(gridWidth, gridRowHeight * 3);
            fighterInterface.setBorder(new TitledBorder("Fighter Attributes"));
            fighterInterface.add(walkSpeedLabel);
            fighterInterface.add(walkSpeedSpinner);
            fighterInterface.add(runSpeedLabel);
            fighterInterface.add(runSpeedSpinner);
            fighterInterface.add(jumpSpeedLabel);
            fighterInterface.add(jumpSpeedSpinner);
            fighterInterface.add(airActionsLabel);
            fighterInterface.add(airActionsSpinner);
            fighterInterface.add(airControlAccelLabel);
            fighterInterface.add(airControlAccelSpinner);
            fighterInterface.add(maxAirControlSpeedLabel);
            fighterInterface.add(maxAirControlSpeedSpinner);
            fighterInterface.add(forwardAirDashSpeedLabel);
            fighterInterface.add(forwardAirDashSpeedSpinner);
            fighterInterface.add(backwardAirDashSpeedLabel);
            fighterInterface.add(backwardAirDashSpeedSpinner);
            fighterInterface.add(forwardAirDashDurationLabel);
            fighterInterface.add(forwardAirDashDurationSpinner);
            fighterInterface.add(backwardAirDashDurationLabel);
            fighterInterface.add(backwardAirDashDurationSpinner);
            fighterInterface.add(stepHeightLabel);
            fighterInterface.add(stepHeightSpinner);
            fighterInterface.add(new JLabel(""));
            fighterInterface.add(new JLabel(""));

            objectAttributesPane.add(fighterInterface);
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
        
        objectAttributesPane.add(buttonPane);
        
        add(objectAttributesPane);
    }
    
    public void applyChanges()
    {
        hsObject.name = nameField.getText();
        hsObject.lifetime = (int)lifetimeSpinner.getValue();
        
        if(hsObject.IsTerrainObject())
        {
            ((TerrainObject)hsObject).health = (int)healthSpinner.getValue();
            ((TerrainObject)hsObject).bounce = (float)bounceSpinner.getValue();
            ((TerrainObject)hsObject).friction = (float)frictionSpinner.getValue();
            ((TerrainObject)hsObject).takesTerrainDamage = takesTerrainDamageCheck.isSelected();
            ((TerrainObject)hsObject).fragile = fragileCheck.isSelected();
        }
        
        if(hsObject.IsPhysicsObject())
        {
            ((PhysicsObject)hsObject).mass = (float)massSpinner.getValue();
            ((PhysicsObject)hsObject).falls = fallsCheck.isSelected();
            ((PhysicsObject)hsObject).maxFallSpeed = (float)maxFallSpeedSpinner.getValue();
        }
        
        if(hsObject.IsFighter())
        {
            ((FighterObject)hsObject).walkSpeed = (float)walkSpeedSpinner.getValue();
            ((FighterObject)hsObject).runSpeed = (float)runSpeedSpinner.getValue();
            ((FighterObject)hsObject).jumpSpeed = (float)jumpSpeedSpinner.getValue();
            ((FighterObject)hsObject).forwardAirDashSpeed = (float)forwardAirDashSpeedSpinner.getValue();
            ((FighterObject)hsObject).backwardAirDashSpeed = (float)backwardAirDashSpeedSpinner.getValue();
            ((FighterObject)hsObject).forwardAirDashDuration = (int)forwardAirDashDurationSpinner.getValue();
            ((FighterObject)hsObject).backwardAirDashDuration = (int)backwardAirDashDurationSpinner.getValue();
            ((FighterObject)hsObject).stepHeight = (float)stepHeightSpinner.getValue();
            ((FighterObject)hsObject).airActions = (int)airActionsSpinner.getValue();
            ((FighterObject)hsObject).airControlAccel = (float)airControlAccelSpinner.getValue();
            ((FighterObject)hsObject).maxAirControlSpeed = (float)maxAirControlSpeedSpinner.getValue();
        }
        
        parent.repaint();
        parent.setTitle("Homestrife Editor - " + hsObject.name);
        
        setTitle("Object Attributes - " + hsObject.name);
        applyButton.setEnabled(false);
    }
    
    private void closeWindow()
    {
        this.dispose();
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
    
    public void onHitSounds()
    {
        SoundsWindow window = new SoundsWindow(this, hsObject, "onHit");
        window.setVisible(true);
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        switch(e.getActionCommand())
        {
            case "okButton": okButtonPressed(); break;
            case "closeButton": closeButtonPressed(); break;
            case "applyButton": applyButtonPressed(); break;
            case "fieldChanged": fieldChanged(); break;
            case "onHit": onHitSounds(); break;
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
