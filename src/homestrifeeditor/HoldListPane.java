/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homestrifeeditor;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

/**
 * TODO: New button inserts on selected hold (and properly named/numbered/linked(?))
 * TODO: Dragging holds (multiple?)
 * @author Darlos9D
 */
public class HoldListPane extends JPanel implements ActionListener, TreeSelectionListener, MouseListener {
	private static final long serialVersionUID = 1L;

	public HoldListWindow parent;
    
    public JTree tree;
    public DefaultMutableTreeNode root;
    
    private JToolBar holdListToolBar;
    
    public HoldListPane(HoldListWindow theParent)
    {
        parent = theParent;
        createPaneContents();
    }
    
    private void createPaneContents()
    {
        JLabel holdListLabel = new JLabel("Hold List");
        
        root = new DefaultMutableTreeNode("Holds");
        tree = new JTree(root);
        tree.setName("holdTree");
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
        tree.setEditable(true);
        tree.addTreeSelectionListener(this);
        tree.addMouseListener(this);
        
        JScrollPane holdListScrollPane = new JScrollPane(tree);
        
        JButton addHoldButton = new JButton("+");
        addHoldButton.setActionCommand("addHold");
        addHoldButton.setToolTipText("Create New Hold");
        addHoldButton.addActionListener(this);
        
        JButton removeHoldsButton = new JButton("-");
        removeHoldsButton.setActionCommand("removeHolds");
        removeHoldsButton.setToolTipText("Delete Selected Hold(s)");
        removeHoldsButton.addActionListener(this);
        
        JButton moveHoldUpButton = new JButton("/\\");
        moveHoldUpButton.setActionCommand("moveHoldUp");
        moveHoldUpButton.setToolTipText("Move Selected Hold Up");
        moveHoldUpButton.addActionListener(this);
        
        JButton moveHoldDownButton = new JButton("\\/");
        moveHoldDownButton.setActionCommand("moveHoldDown");
        moveHoldDownButton.setToolTipText("Move Selected Hold Down");
        moveHoldDownButton.addActionListener(this);
        
        JButton editHoldButton = new JButton("Edit");
        editHoldButton.setActionCommand("editHold");
        editHoldButton.setToolTipText("Edit Hold Attributes");
        editHoldButton.addActionListener(this);
        
        JButton massShiftHoldButton = new JButton("Mass Shift");
        massShiftHoldButton.setActionCommand("massShift");
        massShiftHoldButton.setToolTipText("Mass Shift Attributes");
        massShiftHoldButton.addActionListener(this);
        
        holdListToolBar = new JToolBar();
        holdListToolBar.setFloatable(false);
        holdListToolBar.add(addHoldButton);
        holdListToolBar.add(removeHoldsButton);
        //holdListToolBar.add(moveHoldUpButton);
        //holdListToolBar.add(moveHoldDownButton);
        holdListToolBar.add(editHoldButton);
        holdListToolBar.add(massShiftHoldButton);
        setToolBarEnabled(false);
        
        setLayout(new BorderLayout());
        add(holdListLabel, BorderLayout.PAGE_START);
        add(holdListScrollPane, BorderLayout.CENTER);
        add(holdListToolBar, BorderLayout.PAGE_END);
    }
    
    public void setToolBarEnabled(boolean enable)
    {
        for(Component c : holdListToolBar.getComponents())
        {
            c.setEnabled(enable);
        }
    }
    
    public void addHoldToTree(HSObjectHold hold) {
    	if(hold == null) { return; }
        DefaultTreeModel model = ((DefaultTreeModel)tree.getModel());
        
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(hold);
        
        try {
	        String[] split = hold.name.split("_");
	        String numberRemoved = "";
	        for(int i=0; i < split.length - 1; i++) {
	        	if(i != 0) numberRemoved += "_";
	        	numberRemoved += split[i];
	        }
	        
	        boolean found = false;
	        for(int i=0; i < root.getChildCount(); i++) {
	        	Object obj = model.getChild(root, i);
	        	Object userObject = ((DefaultMutableTreeNode)obj).getUserObject();
	        	if(userObject instanceof String && ((String)userObject).compareTo(numberRemoved) == 0) {
	        		//Position it correctly in numerical order
	        		DefaultMutableTreeNode child = (DefaultMutableTreeNode)model.getChild(root, i);
	        		for(int j=1; j < child.getChildCount(); j++) {
	        			DefaultMutableTreeNode child2 = (DefaultMutableTreeNode) child.getChildAt(j);
	        			if(!(child2.getUserObject() instanceof HSObjectHold)) {
	        				break;
	        			}
	        			
	        			HSObjectHold checkAgainst = (HSObjectHold) child2.getUserObject();
	        			String[] split2 = checkAgainst.name.split("_");
	        			String checkNum = split2[split2.length - 1];
	        			if(Integer.parseInt(split[split.length-1]) < Integer.parseInt(checkNum)) {
	        				//Insert before
	    	        		model.insertNodeInto(node, child, j-1);
	    	        		
	    	        		found = true;
	    	        		
	        				break;
	        			}
	        			else {
	        				continue;
	        			}
	        		}
	        		if(!found) {
    	        		model.insertNodeInto(node, child, child.getChildCount());
    	        		found = true;
	        		}
	        	}
	        }
	        if(!found) {
	        	DefaultMutableTreeNode ndmtn = new DefaultMutableTreeNode(numberRemoved.length() > 0 ? numberRemoved : hold.name);
	        	ndmtn.add(node);
	        	root.add(ndmtn);
	        }
	    	
        } catch(Exception e) {
        	root.add(node);
        }
        reload();
    }
    
    public void addHoldToHoldList(HSObjectHold hold, int index)
    {
        addHoldToHoldList(hold);
    }
    
    public void addHoldToHoldList(HSObjectHold hold)
    {
    	addHoldToTree(hold);
    }
    
    public void addHoldToHoldList()
    {
        HSObjectHold newHold;
        if(parent.currentlyLoadedObject.IsFighter())
        {
            newHold = new FighterHold();
        }
        else if(parent.currentlyLoadedObject.IsPhysicsObject())
        {
            newHold = new PhysicsObjectHold();
        }
        else if(parent.currentlyLoadedObject.IsTerrainObject())
        {
            newHold = new TerrainObjectHold();
        }
        else
        {
            newHold = new HSObjectHold();
        }

        addHoldToHoldList(newHold);
    }
    
    public void addHoldsToHoldList(ArrayList<HSObjectHold> holds)
    {
        for (HSObjectHold hold : holds)
        {
            addHoldToHoldList(hold);
        }
    }
    
    public HSObjectHold removeHoldFromHoldList(TreePath path)
    {    	
    	//TreePath path = tree.getPathForRow(row);
    	HSObjectHold hold = null;
    	try {
    		hold = (HSObjectHold)((DefaultMutableTreeNode)path.getPathComponent(path.getPathCount() - 1)).getUserObject();
    	} catch(Exception exc) {
    		//What is selected isn't a HSObjectHold...ignore
    	}
    	//root.remove(row);
    	System.out.println(path);
    	((DefaultTreeModel)tree.getModel()).removeNodeFromParent(((DefaultMutableTreeNode)path.getPathComponent(path.getPathCount() - 1)));
    	
    	//If there are no more children delete the node
    	if(((DefaultTreeModel)tree.getModel()).getChildCount(((DefaultMutableTreeNode)path.getPathComponent(path.getPathCount() - 2))) == 0)
    		((DefaultTreeModel)tree.getModel()).removeNodeFromParent(((DefaultMutableTreeNode)path.getPathComponent(path.getPathCount() - 2)));
    	//Geez that's a mouthful
    	
        reload();
        tree.makeVisible(path);
    	return hold;
    }
    
    public ArrayList<HSObjectHold> removeHoldsFromHoldList(TreePath[] paths)
    {
        ArrayList<HSObjectHold> removedHolds = new ArrayList<>();
        
        for(int i = paths.length - 1; i >= 0; i--)
        {
            removedHolds.add(0, removeHoldFromHoldList(paths[i]));
        }
        
        return removedHolds;
    }
    
    public ArrayList<HSObjectHold> removeSelectedHoldsFromHoldList()
    {
        int n = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete the selected hold(s)?", "Delete Hold(s)", JOptionPane.YES_NO_OPTION);
        if(tree.getSelectionRows().length == 0) return null;
        
        return n == 0 ? removeHoldsFromHoldList(tree.getSelectionPaths()) : null;
    }
    
    public void removeAllHoldsFromList()
    {
        setToolBarEnabled(false);
       root.removeAllChildren();

       reload();
    }

    public HSObjectHold getCurrentlySelectedHold()
    {
        return (HSObjectHold)((DefaultMutableTreeNode)tree.getLastSelectedPathComponent()).getUserObject();
    }
    
    public ArrayList<HSObjectHold> getAllChildren(DefaultMutableTreeNode node, ArrayList<HSObjectHold> holds)
    {
    	if(holds == null) {
    		holds = new ArrayList<HSObjectHold>();
    	}
    	
    	if(node.getChildCount() == 0) {
    		holds.add((HSObjectHold) node.getUserObject());
    	}
    	else {
	    	for(int i=0; i < tree.getModel().getChildCount(node); i++) {
	    		//Recursion!
	    		DefaultMutableTreeNode e = (DefaultMutableTreeNode) tree.getModel().getChild(node, i);
	    		holds = getAllChildren(e, holds);
	    	}
    	}
    	
    	return holds;
    }
    
    public ArrayList<HSObjectHold> getAllHolds()
    {
    	return getAllChildren(root, null);
    }
    
    public void loadObjectHolds(HSObject currentlyLoadedObject)
    {
        removeAllHoldsFromList();
        
        if(currentlyLoadedObject == null) { return; }
        
        setToolBarEnabled(true);
        
        addHoldsToHoldList(currentlyLoadedObject.holds);
    }
    
    public void applyHoldChanges(HSObjectHold hold, TreePath path)
    {
    	//I don't think this is actually needed
    }
    
    public void createHoldAttributesWindow(HSObjectHold hold, TreePath path)
    {
        HoldAttributesWindow window = new HoldAttributesWindow(this, hold, path);
        window.setVisible(true);
    }
    
    public void editHoldButtonPressed()
    {
        createHoldAttributesWindow(getCurrentlySelectedHold(), tree.getSelectionPath());
    }
    public void massShiftButtonPressed()
    {
        MassShiftWindow window = new MassShiftWindow(this);
        window.setVisible(true);
    }
    
    public void reload() {
    	((DefaultTreeModel)tree.getModel()).reload();
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        switch(e.getActionCommand())
        {
            case "addHold": addHoldToHoldList(); break;
            case "removeHolds": removeSelectedHoldsFromHoldList(); break;
            case "editHold": editHoldButtonPressed(); break;
            case "massShift": massShiftButtonPressed(); break;
        }
    }
    
    @Override
    public void valueChanged(TreeSelectionEvent e)
    {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
        if (node == null || node.getUserObject() instanceof String)
        {
        	if(parent.currentlyLoadedObject != null)
        		parent.textureHitboxPane.unloadHoldData();
        }
        else
        {
            System.out.println("Loading hold");
            parent.textureHitboxPane.loadHoldData((HSObjectHold)node.getUserObject());
        }
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
        if(e.getClickCount() != 2) { return; }
        switch(e.getComponent().getName())
        {
	        case "holdTree":
	        	TreePath path = tree.getPathForLocation(e.getPoint().x, e.getPoint().y);
	        	HSObjectHold hold = null;
	        	try {
	        		hold = (HSObjectHold)((DefaultMutableTreeNode)path.getPathComponent(path.getPathCount() - 1)).getUserObject();
	        	} catch(Exception exc) {
	        		//What is selected isn't a HSObjectHold...ignore
	        		break;
	        	}
	        	if(hold == null) break;
	
	            tree.makeVisible(path);
	            
	            editHoldButtonPressed();
	            break;
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
}
