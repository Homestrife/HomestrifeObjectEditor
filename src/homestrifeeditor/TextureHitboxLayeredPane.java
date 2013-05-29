/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homestrifeeditor;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;

/**
 *
 * @author Darlos9D
 */
public class TextureHitboxLayeredPane extends JLayeredPane implements MouseListener, MouseMotionListener {
	//Stupid annoying warnings
	private static final long serialVersionUID = 1L;

	private static int defaultHitBoxSize = 200;
    
    public TextureHitboxPane parent;
    public ArrayList<JLabel> selectedItems;
    
    public boolean showTextures;
    public boolean showTerrainBox;
    public boolean showAttackBoxes;
    public boolean showHurtBoxes;
    
    //TODO: Undoing things
    //private ArrayList<> actions;
    
    public TextureHitboxLayeredPane(TextureHitboxPane theParent)
    {
        super();
        
        parent = theParent;
        selectedItems = new ArrayList<JLabel>();
        showTextures = true;
        showTerrainBox = true;
        showAttackBoxes = true;
        showHurtBoxes = true;
        
        addMouseListener(this);
    }
    
    public void unselect(JLabel label)
    {
        if(label.getName().compareTo("texture") == 0)
        {
            label.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        }
        else if(label.getName().compareTo("terrain") == 0)
        {
            ((HSBoxLabel)label).setAsTerrainBox();
        }
        else if(label.getName().compareTo("attack") == 0)
        {
            ((HSBoxLabel)label).setAsAttackBox();
        }
        else if(label.getName().compareTo("hurt") == 0)
        {
            ((HSBoxLabel)label).setAsHurtBox();
        }
        
        selectedItems.remove(label);
        
        repaint();
    }
    
    public void unselectAll()
    {
        for(Component c : getComponents())
        {
            if(c.getName().compareTo("texture") == 0)
            {
                ((JLabel)c).setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
            }
            else if(c.getName().compareTo("terrain") == 0)
            {
                ((HSBoxLabel)c).setAsTerrainBox();
            }
            else if(c.getName().compareTo("attack") == 0)
            {
                ((HSBoxLabel)c).setAsAttackBox();
            }
            else if(c.getName().compareTo("hurt") == 0)
            {
                ((HSBoxLabel)c).setAsHurtBox();
            }
        }
        
        selectedItems.clear();
        
        repaint();
    }
    
    public void setSelected(JLabel selectedLabel, boolean multiSelect)
    {
        if(!selectedLabel.isVisible()) { return; }
        
        if(!multiSelect)
        {
            unselectAll();
        }
        
        selectedItems.add(selectedLabel);
        selectedLabel.setBorder(BorderFactory.createDashedBorder(Color.BLACK));
        
        repaint();
    }
    
    public void selectAll()
    {
        for(Component c : getComponents())
        {
            if(c.getName().compareTo("texture") == 0 ||
                c.getName().compareTo("terrain") == 0 ||
                c.getName().compareTo("attack") == 0 ||
                c.getName().compareTo("hurt") == 0)
            {
                setSelected((JLabel)c, true);
            }
        }
    }
    
    public void clearTerrainBox()
    {
        for(Component c : getComponents())
        {
            if(c.getName().compareTo("terrain") == 0)
            {
                this.remove(c);
            }
        }
        
        repaint();
    }
    
    public void setTerrainBox()
    {
        clearTerrainBox();
        
        if(parent.parent.currentlyLoadedObject != null && !parent.parent.currentlyLoadedObject.IsTerrainObject())
        {
            return;
        }
        
        HSBoxLabel terrainBoxLabel = new HSBoxLabel(this, ((TerrainObject)parent.parent.currentlyLoadedObject).terrainBoxes.get(0));
        terrainBoxLabel.setAsTerrainBox();
        terrainBoxLabel.setVisible(showTerrainBox);
        
        add(terrainBoxLabel);
        
        repaint();
    }
    
    public void setUprightTerrainBox()
    {
        clearTerrainBox();
        
        if(parent.parent.currentlyLoadedObject != null && !parent.parent.currentlyLoadedObject.IsFighter())
        {
            setTerrainBox();
        }
        
        HSBoxLabel terrainBoxLabel = new HSBoxLabel(this, ((Fighter)parent.parent.currentlyLoadedObject).uprightTerrainBoxes.get(0));
        terrainBoxLabel.setAsTerrainBox();
        terrainBoxLabel.setVisible(showTerrainBox);
        
        add(terrainBoxLabel);
        
        repaint();
    }
    
    public void setCrouchingTerrainBox()
    {
        clearTerrainBox();
        
        if(parent.parent.currentlyLoadedObject != null && !parent.parent.currentlyLoadedObject.IsFighter())
        {
            setTerrainBox();
        }
        
        HSBoxLabel terrainBoxLabel = new HSBoxLabel(this, ((Fighter)parent.parent.currentlyLoadedObject).crouchingTerrainBoxes.get(0));
        terrainBoxLabel.setAsTerrainBox();
        terrainBoxLabel.setVisible(showTerrainBox);
        
        add(terrainBoxLabel);
        
        repaint();
    }
    
    public void setProneTerrainBox()
    {
        clearTerrainBox();
        
        if(parent.parent.currentlyLoadedObject != null && !parent.parent.currentlyLoadedObject.IsFighter())
        {
            setTerrainBox();
        }
        
        HSBoxLabel terrainBoxLabel = new HSBoxLabel(this, ((Fighter)parent.parent.currentlyLoadedObject).proneTerrainBoxes.get(0));
        terrainBoxLabel.setAsTerrainBox();
        terrainBoxLabel.setVisible(showTerrainBox);
        
        add(terrainBoxLabel);
        
        repaint();
    }
    
    public void setCompactTerrainBox()
    {
        clearTerrainBox();
        
        if(parent.parent.currentlyLoadedObject != null && !parent.parent.currentlyLoadedObject.IsFighter())
        {
            setTerrainBox();
        }
        
        HSBoxLabel terrainBoxLabel = new HSBoxLabel(this, ((Fighter)parent.parent.currentlyLoadedObject).compactTerrainBoxes.get(0));
        terrainBoxLabel.setAsTerrainBox();
        terrainBoxLabel.setVisible(showTerrainBox);
        
        add(terrainBoxLabel);
        
        repaint();
    }
    
    public void moveAllTextureDepthsDown()
    {
        for(Component c : getComponents())
        {
            if(c.getName().compareTo("texture") == 0)
            {
                ((HSTextureLabel)c).texture.depth--; 
                this.setLayer(c, ((HSTextureLabel)c).texture.depth);
            }
        }
    }
    
    public void addTexture(HSTexture tex)
    {
        HSTextureLabel texLabel = new HSTextureLabel(this, tex);
        if(texLabel.getIcon() == null) { return; }
        texLabel.setVisible(showTextures);
        
        add(texLabel, new Integer(tex.depth));
        
        setSelected(texLabel, false);
    }
    
    public void addTexture(String path) {        
        HSTexture newTex = new HSTexture(path);
        
        moveAllTextureDepthsDown();
        newTex.depth = 0;
        newTex.offset.x = 0;
        newTex.offset.y = 0;
        
        parent.hold.textures.add(newTex);
        
        addTexture(newTex);
    }
    
    public void addTexture()
    {
        int returnVal = HoldListWindow.fileChooser.showOpenDialog(null);
        File file;
        
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            file = HoldListWindow.fileChooser.getSelectedFile();
        } else {
            return;
        }
        
        addTexture(file.getPath());
    }
    
    public void addAttackBox(HSBox box)
    {
        HSBoxLabel boxLabel = new HSBoxLabel(this, box);
        boxLabel.setAsAttackBox();
        boxLabel.setVisible(showAttackBoxes);
        
        add(boxLabel, new Integer(box.depth));
        
        setSelected(boxLabel, false);
    }
    
    public void addAttackBox()
    {
        if(!parent.hold.IsTerrainObjectHold()) { return; }
        
        HSBox newBox = new HSBox();
        newBox.depth = highestLayer() + 1;
        newBox.width = defaultHitBoxSize;
        newBox.height = defaultHitBoxSize;
        newBox.offset.x = 0;
        newBox.offset.y = 0;
        
        ((TerrainObjectHold)parent.hold).attackBoxes.add(newBox);
        
        addAttackBox(newBox);
    }
    
    public void addHurtBox(HSBox box)
    {
        HSBoxLabel boxLabel = new HSBoxLabel(this, box);
        boxLabel.setAsHurtBox();
        boxLabel.setVisible(showHurtBoxes);
        
        add(boxLabel, new Integer(box.depth));
        
        setSelected(boxLabel, false);
    }
    
    public void addHurtBox()
    {
        if(!parent.hold.IsTerrainObjectHold()) { return; }
        
        HSBox newBox = new HSBox();
        newBox.depth = highestLayer() + 1;
        newBox.width = defaultHitBoxSize;
        newBox.height = defaultHitBoxSize;
        newBox.offset.x = 0;
        newBox.offset.y = 0;
        
        ((TerrainObjectHold)parent.hold).hurtBoxes.add(newBox);
        
        addHurtBox(newBox);
    }
    
    public void removeSelectedItems()
    {
        ArrayList<JLabel> removedItems = new ArrayList<JLabel>();
        for(JLabel selectedItem : selectedItems)
        {
            if(selectedItem.getName().compareTo("texture") == 0)
            {
                parent.hold.textures.remove(((HSTextureLabel)selectedItem).texture);
            }
            else if(selectedItem.getName().compareTo("attack") == 0)
            {
                ((TerrainObjectHold)parent.hold).attackBoxes.remove(((HSBoxLabel)selectedItem).box);
            }
            else if(selectedItem.getName().compareTo("hurt") == 0)
            {
                ((TerrainObjectHold)parent.hold).hurtBoxes.remove(((HSBoxLabel)selectedItem).box);
            }
            else
            {
                return;
            }

            this.remove(selectedItem);
            removedItems.add(selectedItem);
        }
        
        selectedItems.removeAll(removedItems);
        
        repaint();
    }
    
    public void editSelectedItem()
    {
        if(selectedItems.isEmpty()) { return; }
        if(selectedItems.get(0).getName().compareTo("texture") == 0)
        {
            ((HSTextureLabel)selectedItems.get(0)).createTextureAttributesWindow();
        }
        else if(selectedItems.get(0).getName().compareTo("attack") == 0 || selectedItems.get(0).getName().compareTo("hurt") == 0)
        {
            ((HSBoxLabel)selectedItems.get(0)).createHitboxAttributesWindow();
        }
    }
    
    public void showHideTextures()
    {
        showTextures = showTextures ? false : true;
        
        for(Component c : getComponents())
        {
            if(c.getName().compareTo("texture") == 0)
            {
                c.setVisible(showTextures);
                unselect((JLabel)c);
            }
        }
        
        repaint();
    }
    
    public void showHideTerrainBox()
    {
        showTerrainBox = showTerrainBox ? false : true;
        
        for(Component c : getComponents())
        {
            if(c.getName().compareTo("terrain") == 0)
            {
                c.setVisible(showTerrainBox);
                unselect((JLabel)c);
            }
        }
        
        repaint();
    }
    
    public void showHideAttackBoxes()
    {
        showAttackBoxes = showAttackBoxes ? false : true;
        
        for(Component c : getComponents())
        {
            if(c.getName().compareTo("attack") == 0)
            {
                c.setVisible(showAttackBoxes);
                unselect((JLabel)c);
            }
        }
        
        repaint();
    }
    
    public void showHideHurtBoxes()
    {
        showHurtBoxes = showHurtBoxes ? false : true;
        
        for(Component c : getComponents())
        {
            if(c.getName().compareTo("hurt") == 0)
            {
                c.setVisible(showHurtBoxes);
                unselect((JLabel)c);
            }
        }
        
        repaint();
    }
    
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        
        g.drawLine(0, this.getHeight() / 2, this.getWidth(), this.getHeight() / 2);
        g.drawLine(this.getWidth() / 2, 0, this.getWidth() / 2, this.getHeight());
    }
    
    @Override
    public void mouseEntered(MouseEvent e)
    {
        
    }
    
    @Override
    public void mouseExited(MouseEvent e)
    {
        
    }
    
    @Override
    public void mousePressed(MouseEvent e)
    {
        if(e.getClickCount() == 2)
        {
        
        }
        else if(e.getClickCount() == 1)
        {
            unselectAll();
        }
    }
    
    @Override
    public void mouseReleased(MouseEvent e)
    {
        
    }
    
    @Override
    public void mouseClicked(MouseEvent e)
    {
        
    }
    
    @Override
    public void mouseMoved(MouseEvent e)
    {
        
    }
    
    @Override
    public void mouseDragged(MouseEvent e)
    {
        
    }

	public void undo() {
		
	}

	public void redo() {
		
	}

	public void cut() {
		parent.clipboard = new ArrayList<JLabel>(selectedItems);
		removeSelectedItems();
	}

	public void copy() {
		parent.clipboard = new ArrayList<JLabel>(selectedItems);
	}

	public void paste() {
        for(Component c : parent.clipboard)
        {
            if(c.getName().compareTo("texture") == 0)
            {
            	addTexture(((HSTextureLabel)c).texture);
            }
            else if(c.getName().compareTo("terrain") == 0)
            {
            	//Nothing yet
            }
            else if(c.getName().compareTo("attack") == 0)
            {
            	addAttackBox(((HSBoxLabel)c).box);
            }
            else if(c.getName().compareTo("hurt") == 0)
            {
            	addHurtBox(((HSBoxLabel)c).box);
            }
        }
	}
}
