/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homestrifeeditor.windows;

import homestrifeeditor.objects.holds.FighterHold;
import homestrifeeditor.objects.holds.HSObjectHold;
import homestrifeeditor.objects.holds.PhysicsObjectHold;
import homestrifeeditor.objects.holds.TerrainObjectHold;
import homestrifeeditor.objects.holds.properties.Blockability;
import homestrifeeditor.objects.holds.properties.Cancel;
import homestrifeeditor.objects.holds.properties.HitLevel;
import homestrifeeditor.objects.holds.properties.Invulnerability;
import homestrifeeditor.windows.panes.HoldListPane;
import homestrifeeditor.windows.renderers.HoldComboBoxRenderer;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

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
import javax.swing.tree.TreePath;

/**
 * The attributes of the currently selected hold
 * @author Darlos9D
 */
public class HoldAttributesWindow extends JFrame implements ActionListener, ChangeListener, DocumentListener, ItemListener {
	private static final long serialVersionUID = 1L;
	
	private static int windowWidth = 800;
    private static int windowHeightGeneral = 250;
    private static int windowHeightTerrain = 610;
    private static int windowHeightPhysics = 765;
    private static int windowHeightFighter = 1000;
    private static int windowBorderBuffer = 10;
    
    private static int gridWidth = 650;
    private static int gridRowHeight = 45;
    private static int gridColumns = 4;
    private static int gridHorizontalGap = 10;
    private static int gridVerticalGap = 5;
    
    public HoldListPane parent;
    private HSObjectHold hold;
    
    private JTextField nameField;           private static String nameTooltip = "<html>The name to be displayed in the hold list.</html>";
    private JSpinner durationSpinner;       private static String durationTooltip = "<html>How many frames this hold lasts. The game runs at 60 frames per second.</html>";
    private JComboBox<?> nextHoldCombo;        private static String nextHoldTooltip = "<html>Which hold this object should switch to once this hold's duration expires.<br>If this is set to NONE, the engine will determine the next hold some other way.</html>";
    private JButton holdSoundsButton;       private static String holdSoundsTooltip = "<html>Add/Edit sounds that occur in this hold</html>";
    private JButton spawnObjectsButton;     private static String spawnObjectsTooltip = "<html>Add/Edit objects that are spawned in this hold</html>";
    
    private JSpinner repositionXSpinner; private static String repositionTooltip = "<html>I'm not sure what this means Darlos just told me to put it in, I assume it has something to do with repositioning??</html>";
    private JSpinner repositionYSpinner;
    private JSpinner velocityXSpinner; private static String velocityTooltip = "<html>Probably something pertaining to velocity? I dunno I didn't make the engine</html>";
    private JSpinner velocityYSpinner;
    private JCheckBox overwriteVelocityCheck; private static String overwriteVelocityTooltip = "<html>Whether or not you should overwrite the velocity (duh?)</html>";
    
    private JCheckBox changeAttackBoxAttributesCheck; private static String changeAttackBoxAttributesTooltip = "<html>If this is unchecked, this hold will inherit the attack<br/>attributes of any hold that has this hold as its<br/>next hold.</html>";
    
    private JSpinner damageSpinner;         private static String damageTooltip = "<html>How much damage is done to another object with hurt boxes,<br>should one of this objects attack boxes collide with one of them.</html>";
    private JSpinner hitstunSpinner;        private static String hitstunTooltip = "<html>How many frames another fighter is rendered out-of-control<br>when struck by one of this object's attack boxes.</html>";
    private JSpinner blockstunSpinner;      private static String blockstunTooltip = "<html>How many frames another fighter is rendered out-of-control<br>when struck by one of this object's attack boxes while blocking.</html>";
        
    private JSpinner forceXSpinner;         private static String forceXTooltip = "<html>When another physics object is struck by one of this object's<br>attack boxes, its X velocity is set to this value.</html>";
    private JSpinner forceYSpinner;         private static String forceYTooltip = "<html>When another physics object is struck by one of this object's<br>attack boxes, its Y velocity is set to this value.</html>";
    private JCheckBox tripsCheck;           private static String tripsTooltip = "<html>If checked, another fighter will fall prone if struck by one of<br>this object's attack boxes.</html>";
    
    private JCheckBox resetHitsCheck;        private static String resetHitsTooltip = "<html>If checked, further attack boxes in this animation will strike<br>objects that have already been struck.</html>";
    
    private JComboBox<?> blockabilityCombo;    private static String blockabilityTooltip = "<html>Unblockable: This object's attack boxes cannot be blocked.<br>High: This object's attack boxes must be blocked while standing or jumping.<br>Low: This object's attack boxes must be blocked while crouching or jumping.<br>Mid: This object's attack boxes can be blocked while crouching, standing, or jumping.</html>";
    private JComboBox<?> hitLevelCombo;    private static String hitLevelTooltip = "<html>Ask Darlos</html>";
    private JCheckBox directionBlockCheck;  private static String directionBlockTooltip = "<html>If checked, another figher must be facing in the opposite<br>direction that this object is facing in order to block this<br>object's attack boxes.<br>Normally, whether this object's attack boxes must be blocked while<br>facing left or right is determined by which side of the defending<br>fighter this object is on, regardless of which way this object is facing.</html>";
    private JCheckBox reverseBlockCheck;    private static String reverseBlockTooltip = "<html>If checked, another fighter must face the direction opposite<br>of what they would normally need to in order to block this object's attack boxes.</html>";
    private JButton hitSoundsButton;        private static String hitSoundsTooltip = "<html>Add/Edit sounds that occur upon an attack box collision</html>";
    private JButton blockedSoundsButton;        private static String blockedSoundsTooltip = "<html>Add/Edit sounds that occur upon an attack being blocked</html>";

    private JSpinner ownHitstopSpinner;     private static String ownHitstopTooltip = "<html>How many frames this object is frozen when it strikes<br>something else. This is usually the same amount as victim hitstop.</html>";
    private JSpinner victimHitstopSpinner;  private static String victimHitstopTooltip = "<html>How many frames another object is frozen when struck<br>by this object. This is usually the same amount as own hitstop.</html>";
    private JCheckBox ownHitstopOverride;	private static String ownHitstopOverrideTooltip = "<html>If checked, the own hitstop value overrides a universal hitstop value.</html>";
    private JCheckBox victimHitstopOverride;private static String victimHitstopOverrideTooltip = "<html>If checked, the victim hitstop value overrides a universal hitstop value.</html>";
   
    private JCheckBox changeHurtBoxAttributesCheck; private static String changeHurtBoxAttributesTooltip = "<html>If this is unchecked, this hold will inherit the hurt<br/>attributes of any hold that has this hold as its<br/>next hold.</html>";
    private JComboBox<?> invulnerabilityCombo; 	private static String invulnerabilityTooltip = "<html>I dunno ask Darlos</html>";
    private JSpinner superArmorHitsSpinner;		private static String superArmorHitsTooltip = "<html>I dunno ask Darlos</html>";
    private JSpinner superArmorDamageSpinner;		private static String superArmorDamageTooltip = "<html>I dunno ask Darlos</html>";
    private JSpinner superArmorDamageScalingSpinner;		private static String superArmorDamageScalingTooltip = "<html>I dunno ask Darlos</html>";
    
    private JCheckBox changePhysicsCheck;   private static String changePhysicsTooltip = "<html>If this is unchecked, this hold will not modify the object's physics attributes</html>";
    private JCheckBox ignoreGravityCheck;   //private static String ignoreGravityTooltip = "<html>If this is unchecked, this hold will not modify the object's physics attributes</html>";

    private JCheckBox changeFighterAttributesCheck; private static String changeFighterAttributesTooltip = "<html>If this is checked, the fighter attributes will change on this hold</html>";
    private JCheckBox disableAirControlCheck; private static String disableAirControlTooltip = "<html>If this is checked, this will disable the player's ability to affect horizontal motion while jumping, either until the end of the current animation or until the next time a hold that changes this attribute</html>";
    private JCheckBox endAirDashCheck; private static String endAirDashTooltip = "<html>If this is checked, the air-dashing state will be terminated immediately upon reaching this hold</html>";
    
    private JCheckBox changeCancelsCheck;   private static String changeCancelsTooltip = "<html>If this is unchecked, this hold will inhereit the cancels<br>of any hold that has this hold as its next hold.</html>";
    private JComboBox<Cancel> dashCancelCombo; private static String cancelTooltip = "<html>Any Time: This action can be cancelled into at any time.<br>After Hit or Block: This action can be cancelled into after striking a hurt box.<br>After Hit: This action can ben cancelled into after striking a hurt box without being blocked.<br>After Block: This action can be cancelled into after striking a hurt box and beinb blocked.<br>Never: This action can never be cancelled into.</html>";
    private JComboBox<Cancel> jumpCancelCombo;
    private JComboBox<Cancel> lightNeutralCancelCombo;
    private JComboBox<Cancel> lightForwardCancelCombo;
    private JComboBox<Cancel> lightUpCancelCombo;
    private JComboBox<Cancel> lightDownCancelCombo;
    private JComboBox<Cancel> lightBackwardCancelCombo;
    private JComboBox<Cancel> lightQCFCancelCombo;
    private JComboBox<Cancel> heavyNeutralCancelCombo;
    private JComboBox<Cancel> heavyForwardCancelCombo;
    private JComboBox<Cancel> heavyUpCancelCombo;
    private JComboBox<Cancel> heavyDownCancelCombo;
    private JComboBox<Cancel> heavyBackwardCancelCombo;
    private JComboBox<Cancel> heavyQCFCancelCombo;

    private JPanel terrainInterface;
    private JPanel hitstopInterface;
    private JPanel hurtBoxInterface;
    private JPanel physicsInterface;
    private JPanel fighterAttributesInterface;
    private JPanel fighterInterface;
    
    private JButton applyButton;
    
    public TreePath holdPath;
    
    public HoldAttributesWindow(HoldListPane theParent, HSObjectHold theHold, TreePath path)
    {
        parent = theParent;
        hold = theHold;
        holdPath = path;
        
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
        ArrayList<HSObjectHold> allHolds = parent.getAllHolds();
        HSObjectHold[] allHoldsPlusNull = new HSObjectHold[allHolds.size() + 1];
        HSObjectHold nullHold = new HSObjectHold();
        nullHold.name = "NONE";
        allHoldsPlusNull[0] = nullHold;
        for (int i = 0; i < allHolds.size(); i++)
        {
            allHoldsPlusNull[i + 1] = allHolds.get(i);
        }
        nextHoldCombo = new JComboBox<Object>(allHoldsPlusNull);
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
        
        spawnObjectsButton = new JButton("Spawn Objects...");
        spawnObjectsButton.setToolTipText(spawnObjectsTooltip);
        spawnObjectsButton.setActionCommand("spawnObjectsButton");
        spawnObjectsButton.addActionListener(this);
        
        JLabel repositionLabel = new JLabel("Reposition X/Y");
        repositionLabel.setToolTipText(repositionTooltip);
        
        repositionXSpinner = new JSpinner(new SpinnerNumberModel(0.0f, -99999.0, 99999.0, 1.0));
        repositionXSpinner.setToolTipText(repositionTooltip);
        repositionXSpinner.setValue(hold.reposition.x);
        repositionXSpinner.addChangeListener(this);
        
        repositionYSpinner = new JSpinner(new SpinnerNumberModel(0.0, -99999.0, 99999.0, 1.0));
        repositionYSpinner.setToolTipText(repositionTooltip);
        repositionYSpinner.setValue(hold.reposition.y);
        repositionYSpinner.addChangeListener(this);

        
        JLabel velocityLabel = new JLabel("Velocity X/Y");
        velocityLabel.setToolTipText(velocityTooltip);
        
        overwriteVelocityCheck = new JCheckBox("Overwrite Velocity", hold.overwriteVelocity);
        overwriteVelocityCheck.setToolTipText(overwriteVelocityTooltip);
        overwriteVelocityCheck.setActionCommand("overwriteVelocityChanged");
        overwriteVelocityCheck.addActionListener(this);
        
        velocityXSpinner = new JSpinner(new SpinnerNumberModel(0, -99999, 99999, 1));
        velocityXSpinner.setToolTipText(velocityTooltip);
        velocityXSpinner.setValue(hold.velocity.x);
        velocityXSpinner.setEnabled(overwriteVelocityCheck.isSelected());
        velocityXSpinner.addChangeListener(this);
        
        velocityYSpinner = new JSpinner(new SpinnerNumberModel(0, -99999, 99999, 1));
        velocityYSpinner.setToolTipText(velocityTooltip);
        velocityYSpinner.setValue(hold.velocity.y);
        velocityYSpinner.setEnabled(overwriteVelocityCheck.isSelected());
        velocityYSpinner.addChangeListener(this);
        
        JPanel graphicInterface = new JPanel(new GridLayout(5, gridColumns, gridHorizontalGap, gridVerticalGap));
        graphicInterface.setSize(gridWidth, gridRowHeight * 6);
        graphicInterface.setBorder(new TitledBorder("General Attributes"));
        graphicInterface.add(nameLabel);
        graphicInterface.add(nameField);
        graphicInterface.add(durationLabel);
        graphicInterface.add(durationSpinner);
        graphicInterface.add(nextHoldLabel);
        graphicInterface.add(nextHoldCombo);
        graphicInterface.add(new JLabel(""));
        graphicInterface.add(holdSoundsButton);
        graphicInterface.add(new JLabel(""));
        graphicInterface.add(spawnObjectsButton);
        graphicInterface.add(new JLabel(""));
        graphicInterface.add(new JLabel(""));
        graphicInterface.add(repositionLabel);
        graphicInterface.add(repositionXSpinner);
        graphicInterface.add(repositionYSpinner);
        graphicInterface.add(new JLabel(""));
        graphicInterface.add(velocityLabel);
        graphicInterface.add(velocityXSpinner);
        graphicInterface.add(velocityYSpinner);
        graphicInterface.add(overwriteVelocityCheck);
        
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
            blockabilityCombo = new JComboBox<Object>(Blockability.values());
            blockabilityCombo.setToolTipText(blockabilityTooltip);
            blockabilityCombo.setSelectedItem(toHold.blockability);
            blockabilityCombo.setActionCommand("fieldChanged");
            blockabilityCombo.addItemListener(this);
            
            JLabel hitLevelLabel = new JLabel("Hit Level");
            hitLevelLabel.setToolTipText(hitLevelTooltip);
            hitLevelCombo = new JComboBox<Object>(HitLevel.values());
            hitLevelCombo.setToolTipText(hitLevelTooltip);
            hitLevelCombo.setSelectedItem(toHold.hitLevel);
            hitLevelCombo.setActionCommand("fieldChanged");
            hitLevelCombo.addItemListener(this);

            directionBlockCheck = new JCheckBox("Direction Block", toHold.horizontalDirectionBasedBlock);
            directionBlockCheck.setToolTipText(directionBlockTooltip);
            directionBlockCheck.setActionCommand("fieldChanged");
            directionBlockCheck.addActionListener(this);

            reverseBlockCheck = new JCheckBox("Reverse Block", toHold.reversedHorizontalBlock);
            reverseBlockCheck.setToolTipText(reverseBlockTooltip);
            reverseBlockCheck.setActionCommand("fieldChanged");
            reverseBlockCheck.addActionListener(this);
            
            JLabel damageLabel = new JLabel("Damage");
            damageLabel.setToolTipText(damageTooltip);
            damageSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 99999, 1));
            damageSpinner.setToolTipText(damageTooltip);
            damageSpinner.setValue(toHold.damage);
            damageSpinner.addChangeListener(this);
            
            ownHitstopSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 99999, 1));
            ownHitstopSpinner.setToolTipText(ownHitstopTooltip);
            ownHitstopSpinner.setValue(toHold.ownHitstop);
            ownHitstopSpinner.addChangeListener(this);
            
            ownHitstopOverride = new JCheckBox("Own Hit Stop", toHold.ownHitstopOverride);
            ownHitstopOverride.setToolTipText(ownHitstopOverrideTooltip);
            ownHitstopOverride.setActionCommand("ownHitstopOverrideChanged");
            ownHitstopOverride.addActionListener(this);
            
            victimHitstopSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 99999, 1));
            victimHitstopSpinner.setToolTipText(victimHitstopTooltip);
            victimHitstopSpinner.setValue(toHold.victimHitstop);
            victimHitstopSpinner.addChangeListener(this);
            
            victimHitstopOverride = new JCheckBox("Victim Hit Stop", toHold.victimHitstopOverride);
            victimHitstopOverride.setToolTipText(victimHitstopOverrideTooltip);
            victimHitstopOverride.setActionCommand("victimHitstopOverrideChanged");
            victimHitstopOverride.addActionListener(this);
            
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
            
            tripsCheck = new JCheckBox("Trips", toHold.trips);
            tripsCheck.setToolTipText(tripsTooltip);
            tripsCheck.setActionCommand("fieldChanged");
            tripsCheck.addActionListener(this);
            
            resetHitsCheck = new JCheckBox("Reset Hits", toHold.resetHits);
            resetHitsCheck.setToolTipText(resetHitsTooltip);
            resetHitsCheck.setActionCommand("fieldChanged");
            resetHitsCheck.addActionListener(this);
        
            hitSoundsButton = new JButton("Hit Sounds...");
            hitSoundsButton.setToolTipText(hitSoundsTooltip);
            hitSoundsButton.setActionCommand("hitSoundsButton");
            hitSoundsButton.addActionListener(this);
        
            blockedSoundsButton = new JButton("Blocked Sounds...");
            blockedSoundsButton.setToolTipText(blockedSoundsTooltip);
            blockedSoundsButton.setActionCommand("blockedSoundsButton");
            blockedSoundsButton.addActionListener(this);
            
            hitstopInterface = new JPanel(new GridLayout(2, 2, gridHorizontalGap, gridVerticalGap));
            hitstopInterface.setBorder(new TitledBorder("Hitstops"));
            
            hitstopInterface.add(ownHitstopOverride);
            hitstopInterface.add(ownHitstopSpinner);
            hitstopInterface.add(victimHitstopOverride);
            hitstopInterface.add(victimHitstopSpinner);

            terrainInterface = new JPanel(new GridLayout(5, gridColumns, gridHorizontalGap, gridVerticalGap));
            terrainInterface.setBorder(new TitledBorder("Attack Box Attributes"));
            terrainInterface.add(damageLabel);
            terrainInterface.add(damageSpinner);
            terrainInterface.add(hitstunLabel);
            terrainInterface.add(hitstunSpinner);
            terrainInterface.add(blockabilityLabel);
            terrainInterface.add(blockabilityCombo);
            terrainInterface.add(hitLevelLabel);
            terrainInterface.add(hitLevelCombo);
            terrainInterface.add(blockstunLabel);
            terrainInterface.add(blockstunSpinner);
            terrainInterface.add(forceXLabel);
            terrainInterface.add(forceXSpinner);
            terrainInterface.add(forceYLabel);
            terrainInterface.add(forceYSpinner);
            terrainInterface.add(tripsCheck);
            terrainInterface.add(resetHitsCheck);
            terrainInterface.add(directionBlockCheck);
            terrainInterface.add(reverseBlockCheck);
            terrainInterface.add(hitSoundsButton);
            terrainInterface.add(blockedSoundsButton);
            setTerrainInterfaceEnabled();

            holdAttributesPane.add(terrainInterface);
            holdAttributesPane.add(hitstopInterface);
            
            victimHitstopSpinner.setEnabled(toHold.victimHitstopOverride);
   		 	ownHitstopSpinner.setEnabled(toHold.ownHitstopOverride);
   		 	
   		 	// Hurt box stuffs
   		 	
            hurtBoxInterface = new JPanel(new GridLayout(2, gridColumns, gridHorizontalGap, gridVerticalGap));
            hurtBoxInterface.setBorder(new TitledBorder("Hurt Box Attributes"));
            
            changeHurtBoxAttributesCheck = new JCheckBox("Change hurt box attributes this hold", toHold.changeHurtBoxAttributes);
            changeHurtBoxAttributesCheck.setToolTipText(changeHurtBoxAttributesTooltip);
            changeHurtBoxAttributesCheck.setActionCommand("changeHurtBoxAttributesChanged");
            changeHurtBoxAttributesCheck.addActionListener(this);
            
            JPanel changeHurtBoxAttributesInterface = new JPanel(new GridLayout(1, gridColumns, gridHorizontalGap, gridVerticalGap));
            changeHurtBoxAttributesInterface.setSize(gridWidth, gridRowHeight);
            changeHurtBoxAttributesInterface.add(changeHurtBoxAttributesCheck);
            holdAttributesPane.add(changeHurtBoxAttributesInterface);

            JLabel invulnerabilityComboLabel = new JLabel("Invulnerability");
            invulnerabilityComboLabel.setToolTipText(invulnerabilityTooltip);
            invulnerabilityCombo = new JComboBox<Object>(Invulnerability.values());
            invulnerabilityCombo.setToolTipText(invulnerabilityTooltip);
            invulnerabilityCombo.setSelectedItem(toHold.invulnerability);
            invulnerabilityCombo.setActionCommand("fieldChanged");
            invulnerabilityCombo.addItemListener(this);
            
            JLabel superArmorHitsLabel = new JLabel("Super Armor Hits");
            superArmorHitsLabel.setToolTipText(superArmorHitsTooltip);
            superArmorHitsSpinner = new JSpinner(new SpinnerNumberModel(toHold.superArmorHits, 0, 99999, 1));
            superArmorHitsSpinner.setToolTipText(superArmorHitsTooltip);
            superArmorHitsSpinner.addChangeListener(this);
            
            JLabel superArmorDamageLabel = new JLabel("Super Armor Damage");
            superArmorDamageLabel.setToolTipText(superArmorDamageTooltip);
            superArmorDamageSpinner = new JSpinner(new SpinnerNumberModel(toHold.superArmorDamage, 0, 99999, 1));
            superArmorDamageSpinner.setToolTipText(superArmorDamageTooltip);
            superArmorDamageSpinner.addChangeListener(this);
            
            JLabel superArmorDamageScalingLabel = new JLabel("Super Armor Damage Scaling");
            superArmorDamageScalingLabel.setToolTipText(superArmorDamageScalingTooltip);
            superArmorDamageScalingSpinner = new JSpinner(new SpinnerNumberModel(toHold.superArmorDamageScaling, 0.0, 1.0, .1));
            superArmorDamageScalingSpinner.setToolTipText(superArmorDamageScalingTooltip);
            superArmorDamageScalingSpinner.addChangeListener(this);

            hurtBoxInterface.add(invulnerabilityComboLabel);
            hurtBoxInterface.add(invulnerabilityCombo);
            hurtBoxInterface.add(superArmorHitsLabel);
            hurtBoxInterface.add(superArmorHitsSpinner);
            hurtBoxInterface.add(superArmorDamageLabel);
            hurtBoxInterface.add(superArmorDamageSpinner);
            hurtBoxInterface.add(superArmorDamageScalingLabel);
            hurtBoxInterface.add(superArmorDamageScalingSpinner);
            
            holdAttributesPane.add(hurtBoxInterface);
            setHurtBoxInterfaceEnabled();
        }
        
        if(hold.IsPhysicsObjectHold()) {
            setSize(windowWidth, windowHeightPhysics);
            PhysicsObjectHold pHold = (PhysicsObjectHold)hold;

            changePhysicsCheck = new JCheckBox("Change Physics Attributes This Hold", pHold.changePhysics);
            changePhysicsCheck.setToolTipText(changePhysicsTooltip);
            changePhysicsCheck.setActionCommand("changePhysicsChanged");
            changePhysicsCheck.addActionListener(this);
            
            JPanel changePhysicsInterface = new JPanel(new GridLayout(1, gridColumns, gridHorizontalGap, gridVerticalGap));
            changePhysicsInterface.setSize(gridWidth, gridRowHeight);
            changePhysicsInterface.add(changePhysicsCheck);
            
            holdAttributesPane.add(changePhysicsInterface);
            
            ignoreGravityCheck = new JCheckBox("Ignore Gravity", pHold.ignoreGravity);
            //ignoreGravityCheck.setToolTipText(ignoreGravityTooltip);
            ignoreGravityCheck.setActionCommand("ignoreGravityChanged");
            ignoreGravityCheck.addActionListener(this); 
            
            physicsInterface = new JPanel(new GridLayout(1, gridColumns, gridHorizontalGap, gridVerticalGap));
            physicsInterface.setSize(gridWidth, gridRowHeight * 7);
            physicsInterface.setBorder(new TitledBorder("Physics Attributes"));
            physicsInterface.add(ignoreGravityCheck);
            setPhysicsInterfaceEnabled();

            holdAttributesPane.add(physicsInterface);
        }
        
        if(hold.IsFighterHold())
        {
            setSize(windowWidth, windowHeightFighter);
            
            FighterHold fHold = (FighterHold)hold;
            
            changeFighterAttributesCheck = new JCheckBox("Change fighter attributes this hold", fHold.changeFighterAttributes);
            changeFighterAttributesCheck.setToolTipText(changeFighterAttributesTooltip);
            changeFighterAttributesCheck.setActionCommand("changeFighterAttributesChanged");
            changeFighterAttributesCheck.addActionListener(this);
            
            JPanel changeFighterAttributesInterface = new JPanel(new GridLayout(1, gridColumns, gridHorizontalGap, gridVerticalGap));
            changeFighterAttributesInterface.setSize(gridWidth, gridRowHeight);
            changeFighterAttributesInterface.add(changeFighterAttributesCheck);
            
            holdAttributesPane.add(changeFighterAttributesInterface);
            
            disableAirControlCheck = new JCheckBox("Disable Air Control", fHold.disableAirControl);
            disableAirControlCheck.setToolTipText(disableAirControlTooltip);
            disableAirControlCheck.setActionCommand("disableAirControlChanged");
            disableAirControlCheck.addActionListener(this);
            
            endAirDashCheck = new JCheckBox("End Air Dash", fHold.endAirDash);
            endAirDashCheck.setToolTipText(endAirDashTooltip);
            endAirDashCheck.setActionCommand("endAirDashChanged");
            endAirDashCheck.addActionListener(this);
            
            fighterAttributesInterface = new JPanel(new GridLayout(1, gridColumns, gridHorizontalGap, gridVerticalGap));
            fighterAttributesInterface.setSize(gridWidth, gridRowHeight * 7);
            fighterAttributesInterface.setBorder(new TitledBorder("Fighter Attributes"));
            fighterAttributesInterface.add(disableAirControlCheck);
            fighterAttributesInterface.add(endAirDashCheck);
            setFighterAttributesInterfaceEnabled();

            holdAttributesPane.add(fighterAttributesInterface);
            
            //
            
            changeCancelsCheck = new JCheckBox("Change cancels this hold", fHold.changeCancels);
            changeCancelsCheck.setToolTipText(changeCancelsTooltip);
            changeCancelsCheck.setActionCommand("changeCancelsChanged");
            changeCancelsCheck.addActionListener(this);
            
            JPanel changeCancelsInterface = new JPanel(new GridLayout(1, gridColumns, gridHorizontalGap, gridVerticalGap));
            changeCancelsInterface.setSize(gridWidth, gridRowHeight);
            changeCancelsInterface.add(changeCancelsCheck);
            
            holdAttributesPane.add(changeCancelsInterface);
            
            JLabel dashCancelLabel = new JLabel("Dash");
            dashCancelLabel.setToolTipText(cancelTooltip);
            dashCancelCombo = new JComboBox<Cancel>(Cancel.values());
            dashCancelCombo.setToolTipText(cancelTooltip);
            dashCancelCombo.setSelectedItem(fHold.cancels.dash);
            dashCancelCombo.setActionCommand("fieldChanged");
            dashCancelCombo.addItemListener(this);
            
            JLabel jumpCancelLabel = new JLabel("Jump");
            jumpCancelLabel.setToolTipText(cancelTooltip);
            jumpCancelCombo = new JComboBox<Cancel>(Cancel.values());
            jumpCancelCombo.setToolTipText(cancelTooltip);
            jumpCancelCombo.setSelectedItem(fHold.cancels.jump);
            jumpCancelCombo.setActionCommand("fieldChanged");
            jumpCancelCombo.addItemListener(this);
            
            JLabel lightNeutralCancelLabel = new JLabel("Light Neutral");
            lightNeutralCancelLabel.setToolTipText(cancelTooltip);
            lightNeutralCancelCombo = new JComboBox<Cancel>(Cancel.values());
            lightNeutralCancelCombo.setToolTipText(cancelTooltip);
            lightNeutralCancelCombo.setSelectedItem(fHold.cancels.lightNeutral);
            lightNeutralCancelCombo.setActionCommand("fieldChanged");
            lightNeutralCancelCombo.addItemListener(this);
            
            JLabel lightForwardCancelLabel = new JLabel("Light Forward");
            lightForwardCancelLabel.setToolTipText(cancelTooltip);
            lightForwardCancelCombo = new JComboBox<Cancel>(Cancel.values());
            lightForwardCancelCombo.setToolTipText(cancelTooltip);
            lightForwardCancelCombo.setSelectedItem(fHold.cancels.lightForward);
            lightForwardCancelCombo.setActionCommand("fieldChanged");
            lightForwardCancelCombo.addItemListener(this);
            
            JLabel lightUpCancelLabel = new JLabel("Light Up");
            lightUpCancelLabel.setToolTipText(cancelTooltip);
            lightUpCancelCombo = new JComboBox<Cancel>(Cancel.values());
            lightUpCancelCombo.setToolTipText(cancelTooltip);
            lightUpCancelCombo.setSelectedItem(fHold.cancels.lightUp);
            lightUpCancelCombo.setActionCommand("fieldChanged");
            lightUpCancelCombo.addItemListener(this);
            
            JLabel lightDownCancelLabel = new JLabel("Light Down");
            lightDownCancelLabel.setToolTipText(cancelTooltip);
            lightDownCancelCombo = new JComboBox<Cancel>(Cancel.values());
            lightDownCancelCombo.setToolTipText(cancelTooltip);
            lightDownCancelCombo.setSelectedItem(fHold.cancels.lightDown);
            lightDownCancelCombo.setActionCommand("fieldChanged");
            lightDownCancelCombo.addItemListener(this);
            
            JLabel lightBackwardCancelLabel = new JLabel("Light Backward");
            lightBackwardCancelLabel.setToolTipText(cancelTooltip);
            lightBackwardCancelCombo = new JComboBox<Cancel>(Cancel.values());
            lightBackwardCancelCombo.setToolTipText(cancelTooltip);
            lightBackwardCancelCombo.setSelectedItem(fHold.cancels.lightBackward);
            lightBackwardCancelCombo.setActionCommand("fieldChanged");
            lightBackwardCancelCombo.addItemListener(this);
            
            JLabel lightQCFCancelLabel = new JLabel("Light QCF");
            lightQCFCancelLabel.setToolTipText(cancelTooltip);
            lightQCFCancelCombo = new JComboBox<Cancel>(Cancel.values());
            lightQCFCancelCombo.setToolTipText(cancelTooltip);
            lightQCFCancelCombo.setSelectedItem(fHold.cancels.lightQCF);
            lightQCFCancelCombo.setActionCommand("fieldChanged");
            lightQCFCancelCombo.addItemListener(this);
            
            JLabel heavyNeutralCancelLabel = new JLabel("Heavy Neutral");
            heavyNeutralCancelLabel.setToolTipText(cancelTooltip);
            heavyNeutralCancelCombo = new JComboBox<Cancel>(Cancel.values());
            heavyNeutralCancelCombo.setToolTipText(cancelTooltip);
            heavyNeutralCancelCombo.setSelectedItem(fHold.cancels.heavyNeutral);
            heavyNeutralCancelCombo.setActionCommand("fieldChanged");
            heavyNeutralCancelCombo.addItemListener(this);
            
            JLabel heavyForwardCancelLabel = new JLabel("Heavy Forward");
            heavyForwardCancelLabel.setToolTipText(cancelTooltip);
            heavyForwardCancelCombo = new JComboBox<Cancel>(Cancel.values());
            heavyForwardCancelCombo.setToolTipText(cancelTooltip);
            heavyForwardCancelCombo.setSelectedItem(fHold.cancels.heavyForward);
            heavyForwardCancelCombo.setActionCommand("fieldChanged");
            heavyForwardCancelCombo.addItemListener(this);
            
            JLabel heavyUpCancelLabel = new JLabel("Heavy Up");
            heavyUpCancelLabel.setToolTipText(cancelTooltip);
            heavyUpCancelCombo = new JComboBox<Cancel>(Cancel.values());
            heavyUpCancelCombo.setToolTipText(cancelTooltip);
            heavyUpCancelCombo.setSelectedItem(fHold.cancels.heavyUp);
            heavyUpCancelCombo.setActionCommand("fieldChanged");
            heavyUpCancelCombo.addItemListener(this);
            
            JLabel heavyDownCancelLabel = new JLabel("Heavy Down");
            heavyDownCancelLabel.setToolTipText(cancelTooltip);
            heavyDownCancelCombo = new JComboBox<Cancel>(Cancel.values());
            heavyDownCancelCombo.setToolTipText(cancelTooltip);
            heavyDownCancelCombo.setSelectedItem(fHold.cancels.heavyDown);
            heavyDownCancelCombo.setActionCommand("fieldChanged");
            heavyDownCancelCombo.addItemListener(this);
            
            JLabel heavyBackwardCancelLabel = new JLabel("Heavy Backward");
            heavyBackwardCancelLabel.setToolTipText(cancelTooltip);
            heavyBackwardCancelCombo = new JComboBox<Cancel>(Cancel.values());
            heavyBackwardCancelCombo.setToolTipText(cancelTooltip);
            heavyBackwardCancelCombo.setSelectedItem(fHold.cancels.heavyBackward);
            heavyBackwardCancelCombo.setActionCommand("fieldChanged");
            heavyBackwardCancelCombo.addItemListener(this);
            
            JLabel heavyQCFCancelLabel = new JLabel("Heavy QCF");
            heavyQCFCancelLabel.setToolTipText(cancelTooltip);
            heavyQCFCancelCombo = new JComboBox<Cancel>(Cancel.values());
            heavyQCFCancelCombo.setToolTipText(cancelTooltip);
            heavyQCFCancelCombo.setSelectedItem(fHold.cancels.heavyQCF);
            heavyQCFCancelCombo.setActionCommand("fieldChanged");
            heavyQCFCancelCombo.addItemListener(this);
            
            fighterInterface = new JPanel(new GridLayout(7, gridColumns, gridHorizontalGap, gridVerticalGap));
            fighterInterface.setSize(gridWidth, gridRowHeight * 7);
            fighterInterface.setBorder(new TitledBorder("Cancels"));
            fighterInterface.add(dashCancelLabel);
            fighterInterface.add(dashCancelCombo);
            fighterInterface.add(jumpCancelLabel);
            fighterInterface.add(jumpCancelCombo);
            fighterInterface.add(lightNeutralCancelLabel);
            fighterInterface.add(lightNeutralCancelCombo);
            fighterInterface.add(lightForwardCancelLabel);
            fighterInterface.add(lightForwardCancelCombo);
            fighterInterface.add(lightUpCancelLabel);
            fighterInterface.add(lightUpCancelCombo);
            fighterInterface.add(lightDownCancelLabel);
            fighterInterface.add(lightDownCancelCombo);
            fighterInterface.add(lightBackwardCancelLabel);
            fighterInterface.add(lightBackwardCancelCombo);
            fighterInterface.add(lightQCFCancelLabel);
            fighterInterface.add(lightQCFCancelCombo);
            fighterInterface.add(heavyNeutralCancelLabel);
            fighterInterface.add(heavyNeutralCancelCombo);
            fighterInterface.add(heavyForwardCancelLabel);
            fighterInterface.add(heavyForwardCancelCombo);
            fighterInterface.add(heavyUpCancelLabel);
            fighterInterface.add(heavyUpCancelCombo);
            fighterInterface.add(heavyDownCancelLabel);
            fighterInterface.add(heavyDownCancelCombo);
            fighterInterface.add(heavyBackwardCancelLabel);
            fighterInterface.add(heavyBackwardCancelCombo);
            fighterInterface.add(heavyQCFCancelLabel);
            fighterInterface.add(heavyQCFCancelCombo);
            setFighterInterfaceEnabled();

            holdAttributesPane.add(fighterInterface);
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
            components = hitstopInterface.getComponents();
            for(int i = 0; i < components.length; i++)
            {
                components[i].setEnabled(changeAttackBoxAttributesCheck.isSelected());
            }
        }
    }
    
    private void setHurtBoxInterfaceEnabled() {
        if(hold.IsTerrainObjectHold())
        {
            TerrainObjectHold toHold = (TerrainObjectHold)hold;
            
            hurtBoxInterface.setEnabled(changeHurtBoxAttributesCheck.isSelected());
            
            Component[] components = hurtBoxInterface.getComponents();
            for(int i = 0; i < components.length; i++)
            {
                components[i].setEnabled(changeHurtBoxAttributesCheck.isSelected());
            }
        }
	}
    
    private void setPhysicsInterfaceEnabled()
    {
        if(hold.IsPhysicsObjectHold())
        {
            PhysicsObjectHold toHold = (PhysicsObjectHold)hold;
            
            physicsInterface.setEnabled(changePhysicsCheck.isSelected());
            
            Component[] components = physicsInterface.getComponents();
            for(int i = 0; i < components.length; i++)
            {
                components[i].setEnabled(changePhysicsCheck.isSelected());
            }
        }
    }
    
    private void setFighterAttributesInterfaceEnabled()
    {
        if(hold.IsFighterHold())
        {
            FighterHold fHold = (FighterHold)hold;
            
            fighterAttributesInterface.setEnabled(changeFighterAttributesCheck.isSelected());
            
            Component[] components = fighterAttributesInterface.getComponents();
            for(int i = 0; i < components.length; i++)
            {
                components[i].setEnabled(changeFighterAttributesCheck.isSelected());
            }
        }
    }
    
    private void setFighterInterfaceEnabled()
    {
        if(hold.IsFighterHold())
        { 
            FighterHold fHold = (FighterHold)hold;
            
            fighterInterface.setEnabled(changeCancelsCheck.isSelected());
            
            Component[] components = fighterInterface.getComponents();
            for(int i = 0; i < components.length; i++)
            {
                components[i].setEnabled(changeCancelsCheck.isSelected());
            }
        }
    }
    
    public void applyChanges()
    {
        hold.name = nameField.getText();
        hold.duration = (int)durationSpinner.getValue();
        hold.reposition.x = ((Number)repositionXSpinner.getValue()).floatValue();
        hold.reposition.y = ((Number)repositionYSpinner.getValue()).floatValue();
        hold.velocity.x = ((Number)velocityXSpinner.getValue()).floatValue();
        hold.velocity.y = ((Number)velocityYSpinner.getValue()).floatValue();
        hold.overwriteVelocity = overwriteVelocityCheck.isSelected();
        
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
            ((TerrainObjectHold)hold).ownHitstop = (int)ownHitstopSpinner.getValue();
            ((TerrainObjectHold)hold).ownHitstopOverride = ownHitstopOverride.isSelected();
            ((TerrainObjectHold)hold).victimHitstop = (int)victimHitstopSpinner.getValue();
            ((TerrainObjectHold)hold).victimHitstopOverride = victimHitstopOverride.isSelected();
            ((TerrainObjectHold)hold).hitstun = (int)hitstunSpinner.getValue();
            ((TerrainObjectHold)hold).blockstun = (int)blockstunSpinner.getValue();
            ((TerrainObjectHold)hold).force.x = (float)forceXSpinner.getValue();
            ((TerrainObjectHold)hold).force.y = (float)forceYSpinner.getValue();
            ((TerrainObjectHold)hold).trips = tripsCheck.isSelected();
            ((TerrainObjectHold)hold).resetHits = resetHitsCheck.isSelected();
            ((TerrainObjectHold)hold).blockability = (Blockability)blockabilityCombo.getSelectedItem();
            ((TerrainObjectHold)hold).hitLevel = (HitLevel)hitLevelCombo.getSelectedItem();
            ((TerrainObjectHold)hold).horizontalDirectionBasedBlock = directionBlockCheck.isSelected();
            ((TerrainObjectHold)hold).reversedHorizontalBlock = reverseBlockCheck.isSelected();
            
            ((TerrainObjectHold)hold).changeHurtBoxAttributes = changeHurtBoxAttributesCheck.isSelected();
            ((TerrainObjectHold)hold).superArmorHits = (int)superArmorHitsSpinner.getValue();
            ((TerrainObjectHold)hold).superArmorDamage = (int)superArmorDamageSpinner.getValue();
            ((TerrainObjectHold)hold).superArmorDamageScaling = new Float((double)superArmorDamageScalingSpinner.getValue());
            ((TerrainObjectHold)hold).invulnerability = (Invulnerability)invulnerabilityCombo.getSelectedItem();
        }
        
        if(hold.IsPhysicsObjectHold()) {
        	((PhysicsObjectHold)hold).changePhysics = changePhysicsCheck.isSelected();
        	((PhysicsObjectHold)hold).ignoreGravity = ignoreGravityCheck.isSelected();
        }
        
        if(hold.IsFighterHold())
        {
            ((FighterHold)hold).changeFighterAttributes = changeFighterAttributesCheck.isSelected();
            ((FighterHold)hold).disableAirControl = disableAirControlCheck.isSelected();
            ((FighterHold)hold).endAirDash = endAirDashCheck.isSelected();
            ((FighterHold)hold).changeCancels = changeCancelsCheck.isSelected();
            ((FighterHold)hold).cancels.dash = (Cancel)dashCancelCombo.getSelectedItem();
            ((FighterHold)hold).cancels.jump = (Cancel)jumpCancelCombo.getSelectedItem();
            ((FighterHold)hold).cancels.lightNeutral = (Cancel)lightNeutralCancelCombo.getSelectedItem();
            ((FighterHold)hold).cancels.lightForward = (Cancel)lightForwardCancelCombo.getSelectedItem();
            ((FighterHold)hold).cancels.lightUp = (Cancel)lightUpCancelCombo.getSelectedItem();
            ((FighterHold)hold).cancels.lightDown = (Cancel)lightDownCancelCombo.getSelectedItem();
            ((FighterHold)hold).cancels.lightBackward = (Cancel)lightBackwardCancelCombo.getSelectedItem();
            ((FighterHold)hold).cancels.lightQCF = (Cancel)lightQCFCancelCombo.getSelectedItem();
            ((FighterHold)hold).cancels.heavyNeutral = (Cancel)heavyNeutralCancelCombo.getSelectedItem();
            ((FighterHold)hold).cancels.heavyForward = (Cancel)heavyForwardCancelCombo.getSelectedItem();
            ((FighterHold)hold).cancels.heavyUp = (Cancel)heavyUpCancelCombo.getSelectedItem();
            ((FighterHold)hold).cancels.heavyDown = (Cancel)heavyDownCancelCombo.getSelectedItem();
            ((FighterHold)hold).cancels.heavyBackward = (Cancel)heavyBackwardCancelCombo.getSelectedItem();
            ((FighterHold)hold).cancels.heavyQCF = (Cancel)heavyQCFCancelCombo.getSelectedItem();
        }
        
        parent.repaint();
        
        setTitle("Hold Attributes - " + hold.name);
        applyButton.setEnabled(false);
        
        //Remove it then re-add it to make sure it's in the right place
        parent.removeHoldFromHoldList(holdPath);
        holdPath = new TreePath(parent.addHoldToTree(hold));
        parent.tree.makeVisible(holdPath);
        parent.tree.setSelectionPath(holdPath);
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
    
    private void spawnObjectsButtonPressed()
    {
        SpawnObjectsWindow window = new SpawnObjectsWindow(this, hold);
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

	private void changeHurtBoxAttributesChanged() {
        setHurtBoxInterfaceEnabled();
        fieldChanged();
	}

	private void changePhysicsChanged() {
		setPhysicsInterfaceEnabled();
		fieldChanged();
	}
    
    private void changeCancelsChanged()
    {
        setFighterInterfaceEnabled();
        fieldChanged();
    }

	private void changeFighterAttributesChanged() {
		setFighterAttributesInterfaceEnabled();
		fieldChanged();
	}
    
    private void hitSoundsButtonPressed()
    {
        SoundsWindow window = new SoundsWindow(this, hold, "hit");
        window.setVisible(true);
    }
    
    private void blockedSoundsButtonPressed()
    {
        SoundsWindow window = new SoundsWindow(this, hold, "blocked");
        window.setVisible(true);
    }

	private void overwriteVelocityChanged() {
		velocityXSpinner.setEnabled(overwriteVelocityCheck.isSelected());
		velocityYSpinner.setEnabled(overwriteVelocityCheck.isSelected());
	}

	private void ownHitstopOverrideChanged() {
		 ownHitstopSpinner.setEnabled(ownHitstopOverride.isSelected());
	}

	private void victimHitstopOverrideChanged() {
		victimHitstopSpinner.setEnabled(victimHitstopOverride.isSelected());
	}
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        switch(e.getActionCommand())
        {
            case "holdSoundsButton": holdSoundsButtonPressed(); break;
            case "spawnObjectsButton": spawnObjectsButtonPressed(); break;
            case "okButton": okButtonPressed(); break;
            case "closeButton": closeButtonPressed(); break;
            case "applyButton": applyButtonPressed(); break;
            case "fieldChanged": fieldChanged(); break;
            case "overwriteVelocityChanged": overwriteVelocityChanged(); break;
            case "changeAttackBoxAttributesChanged": changeAttackBoxAttributesChanged(); break;
            case "changeHurtBoxAttributesChanged": changeHurtBoxAttributesChanged(); break;
            case "changePhysicsChanged": changePhysicsChanged();
            case "changeCancelsChanged": changeCancelsChanged(); break;
            case "changeFighterAttributesChanged": changeFighterAttributesChanged(); break;
            case "hitSoundsButton": hitSoundsButtonPressed(); break;
            case "blockedSoundsButton": blockedSoundsButtonPressed(); break;
            case "ownHitstopOverrideChanged": ownHitstopOverrideChanged(); break;
            case "victimHitstopOverrideChanged": victimHitstopOverrideChanged(); break;
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
