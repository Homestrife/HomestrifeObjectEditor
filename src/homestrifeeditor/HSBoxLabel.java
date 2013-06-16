/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homestrifeeditor;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.BorderFactory;
import javax.swing.JLabel;

/**
 *
 * @author Darlos9D
 */
public class HSBoxLabel extends JLabel implements MouseListener, MouseMotionListener {
	private static final long serialVersionUID = 1L;

	private static int resizeBoxSize = 10;
    
    public TextureHitboxLayeredPane parent;
    public HSBox box;
    
    private int mouseStartX;
    private int mouseStartY;
    
    private Rectangle topLeft;
    private Rectangle top;
    private Rectangle topRight;
    private Rectangle left;
    private Rectangle right;
    private Rectangle bottomLeft;
    private Rectangle bottom;
    private Rectangle bottomRight;
    
    private Rectangle resizeBoxPressed;
    
    private int mouseMoveThreshold;
    private boolean moveBox;
    
    public boolean locked;
    
    public HSBoxLabel(TextureHitboxLayeredPane theParent, HSBox theBox)
    {
        super();
        setOpaque(true);
        parent = theParent;
        box = theBox;
        mouseStartX = 0;
        mouseStartY = 0;
        topLeft = new Rectangle();
        top = new Rectangle();
        topRight = new Rectangle();
        left = new Rectangle();
        right = new Rectangle();
        bottomLeft = new Rectangle();
        bottom = new Rectangle();
        bottomRight = new Rectangle();
        resizeBoxPressed = null;
        mouseMoveThreshold = 5;
        moveBox = false;
        locked = false;
        setText("");
        Point pos = parent.parent.getSwingOffset(box.offset.x, box.offset.y);
        setMinimumSize(new Dimension((int)box.width, (int)box.height));
        setMaximumSize(new Dimension((int)box.width, (int)box.height));
        setBounds(pos.x, pos.y, (int)box.width, (int)box.height);
        setResizeBoxes();
        setAsHurtBox();
        addMouseListener(this);
        addMouseMotionListener(this);
    }
    
    public HSBoxLabel(HSBoxLabel l) {
    	//Deeeeeep copy
    	this(l.parent, l.box);
    	setBounds(l.getBounds());
    	box = new HSBox(box);
    	setName(l.getName());
    }
    
    private void setResizeBoxes()
    {
        topLeft.setBounds(0, 0, resizeBoxSize, resizeBoxSize);
        top.setBounds(((int)box.width / 2) - (resizeBoxSize / 2) - 1, 0, resizeBoxSize, resizeBoxSize);
        topRight.setBounds((int)box.width - resizeBoxSize - 1, 0, resizeBoxSize, resizeBoxSize);
        left.setBounds(0, ((int)box.height / 2) - (resizeBoxSize / 2) - 1, resizeBoxSize, resizeBoxSize);
        right.setBounds((int)box.width - resizeBoxSize - 1, ((int)box.height / 2) - (resizeBoxSize / 2) - 1, resizeBoxSize, resizeBoxSize);
        bottomLeft.setBounds(0, (int)box.height - resizeBoxSize - 1, resizeBoxSize, resizeBoxSize);
        bottom.setBounds(((int)box.width / 2) - (resizeBoxSize / 2) - 1, (int)box.height - resizeBoxSize - 1, resizeBoxSize, resizeBoxSize);
        bottomRight.setBounds((int)box.width - resizeBoxSize, (int)box.height - resizeBoxSize - 1, resizeBoxSize, resizeBoxSize);
    }
    
    public void setAsTerrainBox()
    {
        setName("terrain");
        updateColor();
    }
    
    public void setAsAttackBox()
    {
        setName("attack");
        updateColor();
    }
    
    public void setAsHurtBox()
    {
        setName("hurt");
        updateColor();
    }

	public void updateColor() {
        if(getName().compareTo("terrain") == 0)
        {
            setBackground(Color.green);
            setBorder(BorderFactory.createLineBorder(Color.green, 1, false));  	
        }
        else if(getName().compareTo("attack") == 0)
        {
            setBackground(Color.red);
            setBorder(BorderFactory.createLineBorder(Color.red, 1, false));
        }
        else if(getName().compareTo("hurt") == 0)
        {
            setBackground(Color.blue);
            setBorder(BorderFactory.createLineBorder(Color.blue, 1, false));
        }
        if(locked) {
        	setBackground(getBackground().darker());
        }
	}
    
    public void createHitboxAttributesWindow()
    {
        HitboxAttributesWindow newWindow = new HitboxAttributesWindow(this);
        newWindow.setVisible(true);
    }
    
    @Override
    public void paint(Graphics g) 
    {        
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        
        if(parent.selectedItems.size() == 1 && parent.selectedItems.contains(this))
        {
            setResizeBoxes();
            g2.draw(topLeft);
            g2.draw(top);
            g2.draw(topRight);
            g2.draw(left);
            g2.draw(right);
            g2.draw(bottomLeft);
            g2.draw(bottom);
            g2.draw(bottomRight);
        }
        
        super.paint(g2);
        g2.dispose();
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
            createHitboxAttributesWindow();
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
            
            resizeBoxPressed = null;
            if(topLeft.contains(mouseStartX, mouseStartY)) { resizeBoxPressed = topLeft; }
            else if(top.contains(mouseStartX, mouseStartY)) { resizeBoxPressed = top; }
            else if(topRight.contains(mouseStartX, mouseStartY)) { resizeBoxPressed = topRight; }
            else if(left.contains(mouseStartX, mouseStartY)) { resizeBoxPressed = left; }
            else if(right.contains(mouseStartX, mouseStartY)) { resizeBoxPressed = right; }
            else if(bottomLeft.contains(mouseStartX, mouseStartY)) { resizeBoxPressed = bottomLeft; }
            else if(bottom.contains(mouseStartX, mouseStartY)) { resizeBoxPressed = bottom; }
            else if(bottomRight.contains(mouseStartX, mouseStartY)) { resizeBoxPressed = bottomRight; }
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
        
        final int MINSIZE = 10;
        
        Component c = e.getComponent();
        
        if(c == null) { return ; }
        
        int xDiff = e.getX() - mouseStartX;
        int yDiff = e.getY() - mouseStartY;
        
        if(Math.abs(xDiff) >= mouseMoveThreshold || Math.abs(yDiff) >= mouseMoveThreshold)
        {
            moveBox = true;
        }
        
        if(!moveBox) { return; }
        
        if(resizeBoxPressed == null)
        {
            box.offset.x += xDiff;
            box.offset.y += yDiff;
        }
        else if(resizeBoxPressed == topLeft)
        {
            box.offset.x += xDiff;
            box.offset.y += yDiff;
            box.width -= xDiff;
            box.height -= yDiff;

            if(box.width < MINSIZE) {
            	box.offset.x -= -box.width + MINSIZE;
            	box.width = MINSIZE;
            }
            if(box.height < MINSIZE) {
            	box.offset.y -= -box.height + MINSIZE;
            	box.height = MINSIZE;
            }
        }
        else if(resizeBoxPressed == top)
        {
            box.offset.y += yDiff;
            box.height -= yDiff;
            
            if(box.height < MINSIZE) {
            	box.offset.y -= -box.height + MINSIZE;
            	box.height = MINSIZE;
            }
        }
        else if(resizeBoxPressed == topRight)
        {
            box.offset.y += yDiff;
            box.width += xDiff;
            mouseStartX = e.getX();
            box.height -= yDiff;
            
            if(box.width < MINSIZE) {
            	box.width = MINSIZE;
            }
            if(box.height < MINSIZE) {
            	box.offset.y -= -box.height + MINSIZE;
            	box.height = MINSIZE;
            }
        }
        else if(resizeBoxPressed == left)
        {
            box.offset.x += xDiff;
            box.width -= xDiff;

            if(box.width < MINSIZE) {
            	box.offset.x -= -box.width + MINSIZE;
            	box.width = MINSIZE;
            }
        }
        else if(resizeBoxPressed == right)
        {
            box.width += xDiff;
            mouseStartX = e.getX();
            
            if(box.width < MINSIZE) {
            	box.width = MINSIZE;
            }
        }
        else if(resizeBoxPressed == bottomLeft)
        {
            box.offset.x += xDiff;
            box.width -= xDiff;
            box.height += yDiff;
            mouseStartY = e.getY();

            if(box.width < MINSIZE) {
            	box.offset.x -= -box.width + MINSIZE;
            	box.width = MINSIZE;
            }
            if(box.height < MINSIZE) box.height = MINSIZE;
        }
        else if(resizeBoxPressed == bottom)
        {
            box.height += yDiff;
            mouseStartY = e.getY();
            
            if(box.height < MINSIZE) box.height = MINSIZE;
        }
        else if(resizeBoxPressed == bottomRight)
        {
            box.width += xDiff;
            mouseStartX = e.getX();
            box.height += yDiff;
            mouseStartY = e.getY();
            
            if(box.width < MINSIZE) {
            	box.width = MINSIZE;
            }
            if(box.height < MINSIZE) {
            	box.height = MINSIZE;
            }
        }
        
        Point pos = parent.parent.getSwingOffset(box.offset.x, box.offset.y);
        setBounds(pos.x, pos.y, (int)box.width, (int)box.height);
        parent.repaint();
    }
}
