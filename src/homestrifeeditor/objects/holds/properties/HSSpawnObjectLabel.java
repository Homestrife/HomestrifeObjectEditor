package homestrifeeditor.objects.holds.properties;

import homestrifeeditor.windows.panes.TextureHitboxLayeredPane;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JLabel;

public class HSSpawnObjectLabel  extends JLabel implements MouseListener, MouseMotionListener{
	private static final long serialVersionUID = 1L;
	
	public TextureHitboxLayeredPane parent;
    public HSSpawnObject spawnObject;
    
    private int mouseStartX;
    private int mouseStartY;
    
    private int mouseMoveThreshold;
    private boolean moveBox;
    
    public boolean locked;
    
    public int width = 100;
    public int height = 50;
    
    public HSSpawnObjectLabel(TextureHitboxLayeredPane theParent, HSSpawnObject theSpawnObject) {
        super();
        setName("texture");
        parent = theParent;
        spawnObject = theSpawnObject;
        mouseStartX = 0;
        mouseStartY = 0;
        mouseMoveThreshold = 5;
        moveBox = false;
        locked = false;
        addMouseListener(this);
        addMouseMotionListener(this);
        Point pos = parent.parent.getSwingOffset(spawnObject.parentOffset.x, spawnObject.parentOffset.y);
        setBounds(pos.x-width/2, pos.y-height/2, width, height);
    }
    
    public HSSpawnObjectLabel(HSSpawnObjectLabel l) {
    	//Woo deep copy
    	this(l.parent, l.spawnObject);
    	setBounds(l.getBounds());
    	spawnObject = new HSSpawnObject(spawnObject.defFilePath);
    }
    
    @Override
    public void paint(Graphics g) {
    	Graphics2D g2 = (Graphics2D) g.create();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));

        g2.setColor(Color.GREEN);
        g2.fill(new Rectangle(0, 0, width, height));
        g2.setColor(Color.BLACK);
        g2.drawLine(0, height/2, width, height/2);
        g2.drawLine(width/2, 0, width/2, height);
        g2.drawChars(spawnObject.toString().toCharArray(), 0, spawnObject.toString().toCharArray().length, 0, height/3);
        
        super.paint(g2);
        g2.dispose();
    }

	@Override
	public void mouseDragged(MouseEvent e) {
        //if(!parent.selectedItems.contains(this)) { return; }
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
        
        spawnObject.parentOffset.x = spawnObject.parentOffset.x + xDiff;
        spawnObject.parentOffset.y = spawnObject.parentOffset.y + yDiff;
        
        Point pos = parent.parent.getSwingOffset(spawnObject.parentOffset.x, spawnObject.parentOffset.y);
        setBounds(pos.x-width/2, pos.y-height/2, width, height);
        
        parent.repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
        if(e.getClickCount() == 2)
        {

        }
        else if(e.getClickCount() == 1)
        {
        	
        }
        mouseStartX = e.getX();
        mouseStartY = e.getY();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
        moveBox = false;
	}

}
