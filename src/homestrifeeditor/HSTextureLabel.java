/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homestrifeeditor;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 *
 * @author Darlos9D
 */
public class HSTextureLabel extends JLabel implements MouseListener, MouseMotionListener {
	private static final long serialVersionUID = 1L;
	
	public TextureHitboxLayeredPane parent;
    public HSTexture texture;
    public ImageIcon icon;
    
    private int mouseStartX;
    private int mouseStartY;
    
    private int mouseMoveThreshold;
    private boolean moveBox;
    
    public boolean locked;
    
    public HSTextureLabel(TextureHitboxLayeredPane theParent, HSTexture theTexture)
    {
        super();
        setName("texture");
        parent = theParent;
        texture = theTexture;
        mouseStartX = 0;
        mouseStartY = 0;
        mouseMoveThreshold = 5;
        moveBox = false;
        locked = false;
        loadIcon();
        addMouseListener(this);
        addMouseMotionListener(this);
    }
    
    public void loadIcon()
    {
        icon = TGAReader.loadTGA(texture.filePath, parent.parent.parent.currentlyLoadedObject.palettes[parent.parent.parent.currentlyLoadedObject.curPalette].palFilePath);
        setIcon(icon);
        setText("");
        setName("texture");
        setMinimumSize(new Dimension(icon.getIconWidth(), icon.getIconHeight()));
        setMaximumSize(new Dimension(icon.getIconWidth(), icon.getIconHeight()));
        Point pos = parent.parent.getSwingOffset(texture.offset.x, texture.offset.y);
        setBounds(pos.x, pos.y, icon.getIconWidth(), icon.getIconHeight());
    }
    
    public void createTextureAttributesWindow()
    {
        TextureAttributesWindow newWindow = new TextureAttributesWindow(this);
        newWindow.setVisible(true);
    }
    
    @Override
    public void mouseEntered(MouseEvent e)
    {
        
    }
    
    @Override
    public void mouseExited(MouseEvent e)
    {
        moveBox = false;
    }
    
    @Override
    public void mousePressed(MouseEvent e)
    {
        if(e.getClickCount() == 2)
        {
            createTextureAttributesWindow();
        }
        else if(e.getClickCount() == 1)
        {
            boolean multiSelect = false;
            if(e.isControlDown())
            {
                if(parent.selectedItems.contains(this))
                {
                    parent.unselect(this);
                    return;
                }
                multiSelect = true;
            }
            parent.setSelected(this, multiSelect);
            mouseStartX = e.getX();
            mouseStartY = e.getY();
        }
    }
    
    @Override
    public void mouseReleased(MouseEvent e)
    {
        moveBox = false;
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
        if(!parent.selectedItems.contains(this)) { return; }
        if(locked) return;
        
        Component c = e.getComponent();
        
        if(c == null) { return ; }
        
        int xDiff = e.getX() - mouseStartX;
        int yDiff = e.getY() - mouseStartY;
        
        if(Math.abs(xDiff) >= mouseMoveThreshold || Math.abs(yDiff) >= mouseMoveThreshold)
        {
            moveBox = true;
        }
        
        if(!moveBox) { return; }
        
        texture.offset.x = texture.offset.x + xDiff;
        texture.offset.y = texture.offset.y + yDiff;
        
        Point pos = parent.parent.getSwingOffset(texture.offset.x, texture.offset.y);
        setBounds(pos.x, pos.y, icon.getIconWidth(), icon.getIconHeight());
        
        parent.repaint();
    }
}
