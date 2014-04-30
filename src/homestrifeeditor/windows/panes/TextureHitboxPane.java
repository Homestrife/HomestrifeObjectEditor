/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homestrifeeditor.windows.panes;

import homestrifeeditor.objects.holds.properties.HSBox;
import homestrifeeditor.objects.holds.HSObjectHold;
import homestrifeeditor.objects.holds.TerrainObjectHold;
import homestrifeeditor.objects.holds.properties.HSBoxLabel;
import homestrifeeditor.objects.holds.properties.HSSpawnObject;
import homestrifeeditor.objects.holds.properties.HSTexture;
import homestrifeeditor.objects.holds.properties.HSTextureLabel;
import homestrifeeditor.objects.holds.properties.HSVect2D;
import homestrifeeditor.windows.EditorWindow;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.ScrollPaneConstants;

/**
 *
 * @author Darlos9D
 */
public class TextureHitboxPane extends JPanel implements ActionListener, ItemListener {
	private static final long serialVersionUID = 1L;
	
	public static int textureHitboxPaneWidth = 1920 * 8;
    public static int textureHitboxPaneHeight = 1080 * 8;
    
    public EditorWindow parent;
    public HSObjectHold hold;
    
    public TextureHitboxLayeredPane textureHitboxPane;
    public JScrollPane textureHitboxScrollPane;
    private JToolBar textureHitboxToolBar;
    
    private JLabel terrainBoxLabel;
    private JComboBox<?> terrainBoxCombo;
    
    private JButton addTerrainBoxButton;    
    private JButton addAttackBoxButton;
    private JButton addHurtBoxButton;
    public JButton removeButton;
    
    private JButton showHideTexturesButton;
    private JButton showHideTerrainBoxButton;
    private JButton showHideAttackBoxesButton;
    private JButton showHideHurtBoxesButton;
    private JButton lockBoxButton;
    
    //Currently cut/copied object
    public ArrayList<JLabel> clipboard;
    
    public TextureHitboxPane(EditorWindow theParent)
    {
        parent = theParent;
        hold = null;
        clipboard = new ArrayList<JLabel>();
        
        createPaneContents();
    }
    
    private void createPaneContents()
    {
        JLabel textureHitboxLabel = new JLabel("Textures and Hitboxes");
        
        textureHitboxPane = new TextureHitboxLayeredPane(this);
        textureHitboxPane.setOpaque(true);
        textureHitboxPane.setBackground(Color.magenta);
        textureHitboxPane.setPreferredSize(new Dimension(textureHitboxPaneWidth, textureHitboxPaneHeight));
        textureHitboxScrollPane = new JScrollPane(textureHitboxPane, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        textureHitboxScrollPane.getVerticalScrollBar().setUnitIncrement(10);
        
        terrainBoxLabel = new JLabel("Ter Box: ");
        String[] terrainBoxOptions = new String[4];
        terrainBoxOptions[0] = "Upright";
        terrainBoxOptions[1] = "Crouching";
        terrainBoxOptions[2] = "Prone";
        terrainBoxOptions[3] = "Compact";
        terrainBoxCombo = new JComboBox<Object>(terrainBoxOptions);
        terrainBoxCombo.addItemListener(this);
        terrainBoxCombo.setName("terrainBoxCombo");
        
        JPanel terrainBoxPanel = new JPanel();
        terrainBoxPanel.setLayout(new BoxLayout(terrainBoxPanel, BoxLayout.X_AXIS));
        terrainBoxPanel.add(terrainBoxLabel);
        terrainBoxPanel.add(terrainBoxCombo);
        
        JButton addTextureButton = new JButton("+Tex");
        addTextureButton.setActionCommand("addTexture");
        addTextureButton.setToolTipText("Create New Texture");
        addTextureButton.addActionListener(this);
        
        addTerrainBoxButton = new JButton("+Trn");
        addTerrainBoxButton.setActionCommand("addTerrainBox");
        addTerrainBoxButton.setToolTipText("Create New Terrain Box (Terrain Objects Only)");
        addTerrainBoxButton.addActionListener(this);
        
        addAttackBoxButton = new JButton("+Atk");
        addAttackBoxButton.setActionCommand("addAttackBox");
        addAttackBoxButton.setToolTipText("Create New Attack Hitbox");
        addAttackBoxButton.addActionListener(this);
        
        addHurtBoxButton = new JButton("+Hrt");
        addHurtBoxButton.setActionCommand("addHurtBox");
        addHurtBoxButton.setToolTipText("Create New Hurt Hitbox");
        addHurtBoxButton.addActionListener(this);
        
        removeButton = new JButton("-");
        removeButton.setActionCommand("remove");
        removeButton.setToolTipText("Delete Selected Texture(s) or Hitbox(es)");
        removeButton.addActionListener(this);
        
        JButton editButton = new JButton("Edit");
        editButton.setActionCommand("edit");
        editButton.setToolTipText("Edit Texture or Hitbox Attributes");
        editButton.addActionListener(this);
        
        showHideTexturesButton = new JButton("HideTex");
        showHideTexturesButton.setActionCommand("showHideTex");
        showHideTexturesButton.setToolTipText("Show/Hide Textures");
        showHideTexturesButton.addActionListener(this);
        
        showHideTerrainBoxButton = new JButton("HideTer");
        showHideTerrainBoxButton.setActionCommand("showHideTer");
        showHideTerrainBoxButton.setToolTipText("Show/Hide Terrain Box");
        showHideTerrainBoxButton.addActionListener(this);
        
        showHideAttackBoxesButton = new JButton("HideAtk");
        showHideAttackBoxesButton.setActionCommand("showHideAtk");
        showHideAttackBoxesButton.setToolTipText("Show/Hide Attack Boxes");
        showHideAttackBoxesButton.addActionListener(this);
        
        showHideHurtBoxesButton = new JButton("HideHrt");
        showHideHurtBoxesButton.setActionCommand("showHideHrt");
        showHideHurtBoxesButton.setToolTipText("Show/Hide Hurt Boxes");
        showHideHurtBoxesButton.addActionListener(this);
        
        lockBoxButton = new JButton("Lock");
        lockBoxButton.setActionCommand("lock");
        lockBoxButton.setToolTipText("Lock/Unlock selected boxes");
        lockBoxButton.addActionListener(this);
        
        textureHitboxToolBar = new JToolBar();
        textureHitboxToolBar.setFloatable(false);
        textureHitboxToolBar.add(addTextureButton);
        textureHitboxToolBar.add(addTerrainBoxButton);
        textureHitboxToolBar.add(addAttackBoxButton);
        textureHitboxToolBar.add(addHurtBoxButton);
        textureHitboxToolBar.add(removeButton);
        textureHitboxToolBar.add(editButton);
        textureHitboxToolBar.add(showHideTexturesButton);
        textureHitboxToolBar.add(showHideTerrainBoxButton);
        textureHitboxToolBar.add(showHideAttackBoxesButton);
        textureHitboxToolBar.add(showHideHurtBoxesButton);
        textureHitboxToolBar.add(lockBoxButton);
        
        JPanel textureHitboxToolPane = new JPanel(new BorderLayout());
        textureHitboxToolPane.add(terrainBoxPanel, BorderLayout.LINE_START);
        textureHitboxToolPane.add(textureHitboxToolBar, BorderLayout.CENTER);
        
        setLayout(new BorderLayout());
        add(textureHitboxLabel, BorderLayout.PAGE_START);
        add(textureHitboxScrollPane, BorderLayout.CENTER);
        add(textureHitboxToolPane, BorderLayout.PAGE_END);
        
        setAllEnabled(false);
    }
    
    public HSVect2D getHSOffset(int x, int y)
    {
        HSVect2D point = new HSVect2D();
        point.x = x - textureHitboxPaneWidth/2;
        point.y = y - textureHitboxPaneHeight/2;
        
        return point;
    }
    
    public Point getSwingOffset(float x, float y)
    {
        Point point = new Point(textureHitboxPaneWidth/2 + (int)x, textureHitboxPaneHeight/2 + (int)y);
        
        return point;
    }
    
    public void setAllEnabled(boolean enable)
    {
        for(Component c : textureHitboxScrollPane.getComponents())
        {
            c.setEnabled(enable);
        }
        textureHitboxScrollPane.setEnabled(enable);
        
        for(Component c : textureHitboxToolBar.getComponents())
        {
            c.setEnabled(enable);
            
            if(parent.currentlyLoadedObject != null && !parent.currentlyLoadedObject.IsTerrainObject())
            {
                if(c == addAttackBoxButton
                        || c == addHurtBoxButton
                        || c == showHideTerrainBoxButton
                        || c == showHideAttackBoxesButton
                        || c == showHideHurtBoxesButton)
                {
                    c.setEnabled(false);
                }
            }
            if(parent.currentlyLoadedObject != null && (!parent.currentlyLoadedObject.IsTerrainObject() || parent.currentlyLoadedObject.IsFighter() || parent.currentlyLoadedObject.IsPhysicsObject())) {
            	if(c == addTerrainBoxButton)
            		c.setEnabled(false);
            }
        }
        textureHitboxToolBar.setEnabled(enable);
        
        terrainBoxLabel.setEnabled(enable);
        terrainBoxCombo.setEnabled(enable);
        
        if(parent.currentlyLoadedObject != null && !parent.currentlyLoadedObject.IsFighter())
        {
            terrainBoxLabel.setEnabled(false);
            terrainBoxCombo.setEnabled(false);
        }
    }
    
    public void resetScrollBars()
    {
        textureHitboxScrollPane.revalidate();
        
        float horMin = textureHitboxScrollPane.getHorizontalScrollBar().getMinimum();
        float horMax = textureHitboxScrollPane.getHorizontalScrollBar().getMaximum();
        float horMid = (horMax - horMin) / 2f - textureHitboxScrollPane.getWidth() / 2;
        float verMin = textureHitboxScrollPane.getVerticalScrollBar().getMinimum();
        float verMax = textureHitboxScrollPane.getVerticalScrollBar().getMaximum();
        float verMid = (verMax - verMin) / 2f - textureHitboxScrollPane.getHeight();
        textureHitboxScrollPane.getHorizontalScrollBar().setValue((int)horMid);
        textureHitboxScrollPane.getVerticalScrollBar().setValue((int)verMid);
    }
    
    public void loadHoldData(HSObjectHold theHold)
    {
        unloadHoldData();
        
        hold = theHold;
        
        for(HSTexture tex : hold.textures)
        {
            textureHitboxPane.addTexture(tex, false);
        }
        
        if(hold.IsTerrainObjectHold())
        {
            TerrainObjectHold toHold = (TerrainObjectHold)hold;
            
            for(HSBox box : toHold.attackBoxes)
            {
                textureHitboxPane.addAttackBox(box, false);
            }
            
            for(HSBox box : toHold.hurtBoxes)
            {
                textureHitboxPane.addHurtBox(box, false);
            }
        }
        for(HSSpawnObject spawn : hold.spawnObjects) {
        	textureHitboxPane.addSpawnObject(spawn);
        }
        setAllEnabled(true);
        updateLockButton();
    }
    
    public void unloadHoldData()
    {
        hold = null;
        
        textureHitboxPane.removeAll();
        textureHitboxPane.repaint();
        setCorrectTerrainBox();
        
        setAllEnabled(false);
    }
    
    public void reloadTextures()
    {
        for(Component c : textureHitboxPane.getComponents())
        {
            if(c.getName().compareTo("texture") == 0)
            {
                ((HSTextureLabel)c).loadIcon();
            }
        }
    }
    
    private void showHideTexturesButtonClicked()
    {
        textureHitboxPane.showHideTextures();
        showHideTexturesButton.setText(textureHitboxPane.showTextures ? "HideTex" : "ShowTex");
    }
    
    private void showHideTerrainBoxButtonClicked()
    {
        textureHitboxPane.showHideTerrainBox();
        showHideTerrainBoxButton.setText(textureHitboxPane.showTerrainBox ? "HideTer" : "ShowTer");
    }
    
    private void showHideAttackBoxesButtonClicked()
    {
        textureHitboxPane.showHideAttackBoxes();
        showHideAttackBoxesButton.setText(textureHitboxPane.showAttackBoxes ? "HideAtk" : "ShowAtk");
    }
    
    private void showHideHurtBoxesButtonclicked()
    {
        textureHitboxPane.showHideHurtBoxes();
        showHideHurtBoxesButton.setText(textureHitboxPane.showHurtBoxes ? "HideHrt" : "ShowHrt");
    }
    
    public void updateLockButton() {
    	boolean allSame = true;
    	String newLabel = "";
    	
    	Boolean lastLocked = null;
		//First we must go through the list to get the first "locked" value
    	for(JLabel jl : textureHitboxPane.selectedItems) {
			if(jl.getName().compareTo("terrain") != 0 && jl.getName().compareTo("attack") != 0 && jl.getName().compareTo("hurt") != 0 && jl.getName().compareTo("texture") != 0) continue;
			if(jl.getName().compareTo("texture") == 0) {
				lastLocked = ((HSTextureLabel)jl).locked;
				break;				
			}
			else {
				lastLocked = ((HSBoxLabel)jl).locked;
				break;
			}
		}
    	if(lastLocked == null) {
    		lockBoxButton.setEnabled(false);
    		return;
    	}
		lockBoxButton.setEnabled(true);
		for(JLabel jl : textureHitboxPane.selectedItems) {
			if(jl.getName().compareTo("terrain") != 0 && jl.getName().compareTo("attack") != 0 && jl.getName().compareTo("hurt") != 0 && jl.getName().compareTo("texture") != 0) continue;
			if(jl.getName().compareTo("texture") == 0) {
				boolean isLocked = ((HSTextureLabel)jl).locked;
				if(isLocked != lastLocked.booleanValue()) {
					allSame = false;
				}
				lastLocked = isLocked;
			}
			else {
				boolean isLocked = ((HSBoxLabel)jl).locked;
				if(isLocked != lastLocked.booleanValue()) {
					allSame = false;
				}
				lastLocked = isLocked;
			}
		}
		
		if(!allSame) {
			newLabel = "(Un)Lock";
		}
		else if(lastLocked) {
			newLabel = "Unlock";
		}
		else if(!lastLocked) {
			newLabel = "Lock";
		}
		
		lockBoxButton.setText(newLabel);    	
    }
    
    private void lockedButtonClicked() {
    	boolean allSame = true;
    	String newLabel = "";
    	
    	boolean lastLocked = false;
    	boolean noBoxes = true;
		//First we must go through the list to get the first "locked" value
    	for(JLabel jl : textureHitboxPane.selectedItems) {
			if(jl.getName().compareTo("terrain") != 0 && jl.getName().compareTo("attack") != 0 && jl.getName().compareTo("hurt") != 0 && jl.getName().compareTo("texture") != 0) continue;
			if(jl.getName().compareTo("texture") == 0) {
				lastLocked = ((HSTextureLabel)jl).locked;
				noBoxes = false;
				break;				
			}
			else {
				lastLocked = ((HSBoxLabel)jl).locked;
				noBoxes = false;
				break;
			}
		}
    	if(noBoxes) return;
		for(JLabel jl : textureHitboxPane.selectedItems) {
			if(jl.getName().compareTo("terrain") != 0 && jl.getName().compareTo("attack") != 0 && jl.getName().compareTo("hurt") != 0 && jl.getName().compareTo("texture") != 0) continue;
			if(jl.getName().compareTo("texture") == 0) {
				boolean isLocked = ((HSTextureLabel)jl).locked;
				if(isLocked != lastLocked) {
					allSame = false;
				}
				((HSTextureLabel)jl).locked = !isLocked;
				lastLocked = isLocked;
				
		        //((HSTextureLabel)jl).updateColor();
		        //repaint();				
			}
			else {
				boolean isLocked = ((HSBoxLabel)jl).locked;
				if(isLocked != lastLocked) {
					allSame = false;
				}
				((HSBoxLabel)jl).locked = !isLocked;
				lastLocked = isLocked;
				
		        ((HSBoxLabel)jl).updateColor();
		        repaint();
			}
		}
		lastLocked = !lastLocked;
		
		if(!allSame) {
			newLabel = "(Un)Lock";
		}
		else if(lastLocked) {
			newLabel = "Unlock";
		}
		else if(!lastLocked) {
			newLabel = "Lock";
		}
		
		lockBoxButton.setText(newLabel);
	}
    
    public void setCorrectTerrainBox()
    {
        if(parent.currentlyLoadedObject.IsFighter())
        {
            int index = terrainBoxCombo.getSelectedIndex();
            
            switch(index)
            {
                case 0: textureHitboxPane.setUprightTerrainBox(); break;
                case 1: textureHitboxPane.setCrouchingTerrainBox(); break;
                case 2: textureHitboxPane.setProneTerrainBox(); break;
                case 3: textureHitboxPane.setCompactTerrainBox(); break;
            }
        }
        else if(parent.currentlyLoadedObject.IsTerrainObject())
        {
            textureHitboxPane.setTerrainBox();
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        switch(e.getActionCommand())
        {
	        case "addTexture": textureHitboxPane.addTexture(); break;
	        case "addTerrainBox": textureHitboxPane.addTerrainBox(); break;
            case "addAttackBox": textureHitboxPane.addAttackBox(); break;
            case "addHurtBox": textureHitboxPane.addHurtBox(); break;
            case "remove": textureHitboxPane.removeSelectedItems(); break;
            case "edit": textureHitboxPane.editSelectedItem(); break;
            case "showHideTex": showHideTexturesButtonClicked(); break;
            case "showHideTer": showHideTerrainBoxButtonClicked(); break;
            case "showHideAtk": showHideAttackBoxesButtonClicked(); break;
            case "showHideHrt": showHideHurtBoxesButtonclicked(); break;
            case "lock": lockedButtonClicked(); break;
        }
    }

	@Override
    public void itemStateChanged(ItemEvent e)
    {
        if(((Component)e.getSource()).getName().compareTo("terrainBoxCombo") == 0)
        {
            setCorrectTerrainBox();
        }
    }

	public void undo() {
		textureHitboxPane.undo();
	}

	public void redo() {
		textureHitboxPane.redo();		
	}

	public void cut() {
		textureHitboxPane.cut();
	}

	public void copy() {
		textureHitboxPane.copy();
	}

	public void paste() {
		textureHitboxPane.paste();
	}
}
