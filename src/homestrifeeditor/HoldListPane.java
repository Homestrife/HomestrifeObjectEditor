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
import java.util.Arrays;

import javax.swing.JPanel;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author Darlos9D
 */
public class HoldListPane extends JPanel implements ActionListener, ListSelectionListener, MouseListener {
	private static final long serialVersionUID = 1L;

	public HoldListWindow parent;
    
    public DefaultListModel<HSObjectHold> holdListModel;
    public JList<HSObjectHold> holdList;
    
    private JToolBar holdListToolBar;
    
    public HoldListPane(HoldListWindow theParent)
    {
        parent = theParent;
        createPaneContents();
    }
    
    private void createPaneContents()
    {
        JLabel holdListLabel = new JLabel("Hold List");
        holdListModel = new DefaultListModel<HSObjectHold>();
        holdList = new JList<HSObjectHold>(holdListModel);
        holdList.setName("holdList");
        holdList.setCellRenderer(new HoldListCellRenderer());
        holdList.addListSelectionListener(this);
        holdList.addMouseListener(this);
        
        JScrollPane holdListScrollPane = new JScrollPane(holdList);
        
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
        holdListToolBar.add(moveHoldUpButton);
        holdListToolBar.add(moveHoldDownButton);
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
    
    public void addHoldToHoldList(HSObjectHold hold, int index)
    {
        if(hold == null) { return; }
        
        if(index >= 0)
        {
            holdListModel.add(index + 1, hold);
        }
        else
        {
            holdListModel.addElement(hold);
        }
        moveIndexDown();
    }
    
    public void addHoldToHoldList(HSObjectHold hold)
    {
        addHoldToHoldList(hold, holdList.getSelectedIndex());
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
        
        addHoldToHoldList(newHold, holdList.getSelectedIndex());
    }
    
    public void addHoldsToHoldList(ArrayList<HSObjectHold> holds)
    {
        for (HSObjectHold hold : holds)
        {
            addHoldToHoldList(hold);
        }
    }
    
    public HSObjectHold removeHoldFromHoldList(int index)
    {
        return (HSObjectHold)holdListModel.remove(index);
    }
    
    public ArrayList<HSObjectHold> removeHoldsFromHoldList(int[] indices)
    {
        ArrayList<HSObjectHold> removedHolds = new ArrayList<>();
        
        Arrays.sort(indices, 0, indices.length - 1);
        for(int i = indices.length - 1; i >= 0; i--)
        {
            removedHolds.add(0, removeHoldFromHoldList(indices[i]));
        }
        
        return removedHolds;
    }
    
    public ArrayList<HSObjectHold> removeSelectedHoldsFromHoldList()
    {
        int n = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete the selected hold(s)?", "Delete Hold(s)", JOptionPane.YES_NO_OPTION);
        
        return n == 0 ? removeHoldsFromHoldList(holdList.getSelectedIndices()): null;
    }
    
    public void removeAllHoldsFromList()
    {
        setToolBarEnabled(false);
        holdListModel.removeAllElements();
    }
    
    public int getCurrentlySelectedIndex()
    {
        return holdList.getSelectedIndex();
    }
    
    public HSObjectHold getCurrentlySelectedHold()
    {
        if(holdList.getSelectedIndex() < 0) { return null; }
        
        return (HSObjectHold)holdListModel.get(holdList.getSelectedIndex());
    }
    
    public HSObjectHold[] getAllHolds()
    {
        HSObjectHold[] holds = new HSObjectHold[holdListModel.getSize()];
        
        for (int i = 0; i < holdListModel.getSize(); i++)
        {
            HSObjectHold hold = (HSObjectHold)holdListModel.get(i);
            hold.id = i + 1;
            holds[i] = hold;
        }
        
        return holds;
    }
    
    public void moveSelectedHoldUp()
    {
        int index = holdList.getSelectedIndex();
        
        if(index <= 0) { return; }
        
        HSObjectHold holdToMove = removeHoldFromHoldList(index);
        
        holdListModel.add(index - 1, holdToMove);
        
        holdList.setSelectedIndex(index - 1);
    }
    
    public void moveSelectedHoldDown()
    {
        int index = holdList.getSelectedIndex();
        
        if(index < 0 || index >= holdListModel.getSize() - 1) { return; }
        
        HSObjectHold holdToMove = removeHoldFromHoldList(index);
        
        holdListModel.add(index + 1, holdToMove);
        
        holdList.setSelectedIndex(index + 1);
    }
    
    public void loadObjectHolds(HSObject currentlyLoadedObject)
    {
        removeAllHoldsFromList();
        
        if(currentlyLoadedObject == null) { return; }
        
        setToolBarEnabled(true);
        
        addHoldsToHoldList(currentlyLoadedObject.holds);
    }
    
    public void applyHoldChanges(HSObjectHold hold, int index)
    {
        holdListModel.setElementAt(hold, index);
    }
    
    public void createHoldAttributesWindow(HSObjectHold hold)
    {
        HoldAttributesWindow window = new HoldAttributesWindow(this, hold);
        window.setVisible(true);
    }
    
    public void editHoldButtonPressed()
    {
        createHoldAttributesWindow(getCurrentlySelectedHold());
    }
    
    public void massShift(int shiftX, int shiftY)
    {
        int[] indices = holdList.getSelectedIndices();
        
        for(int i : indices)
        {
            HSObjectHold hold = (HSObjectHold)holdListModel.get(i);
            
            for(HSTexture tex : hold.textures)
            {
                tex.offset.x += shiftX;
                tex.offset.y += shiftY;
            }
            
            if(hold.IsTerrainObjectHold())
            {
                for(HSBox box : ((TerrainObjectHold)hold).attackBoxes)
                {
                    box.offset.x += shiftX;
                    box.offset.y += shiftY;
                }
                
                for(HSBox box : ((TerrainObjectHold)hold).hurtBoxes)
                {
                    box.offset.x += shiftX;
                    box.offset.y += shiftY;
                }
            }
        }
        
        if (holdList.getSelectedIndex() == -1)
        {
            parent.textureHitboxPane.unloadHoldData();
        }
        else
        {
            parent.textureHitboxPane.loadHoldData((HSObjectHold)holdList.getSelectedValue());
        }
    }
    
    public void massShiftButtonPressed()
    {
        MassShiftWindow window = new MassShiftWindow(this);
        window.setVisible(true);
    }
    
    public void moveIndexUp() {
        int index = holdList.getSelectedIndex();
       //if(index - 1 < holdList.getMinSelectionIndex()) return;
    	holdList.setSelectedIndex(index - 1);
    	holdList.ensureIndexIsVisible(index - 1);
    }
    
    public void moveIndexDown() {
        int index = holdList.getSelectedIndex();
        //if(index + 1 > holdList.getMaxSelectionIndex()) return;
    	holdList.setSelectedIndex(index + 1);
    	holdList.ensureIndexIsVisible(index + 1);
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        switch(e.getActionCommand())
        {
            case "addHold": addHoldToHoldList(); break;
            case "removeHolds": removeSelectedHoldsFromHoldList(); break;
            case "moveHoldUp": moveSelectedHoldUp(); break;
            case "moveHoldDown": moveSelectedHoldDown(); break;
            case "editHold": editHoldButtonPressed(); break;
            case "massShift": massShiftButtonPressed(); break;
        }
    }
    
    @Override
    public void valueChanged(ListSelectionEvent e)
    {
        if (e.getValueIsAdjusting() == false)
        {
            if (holdList.getSelectedIndex() == -1)
            {
                parent.textureHitboxPane.unloadHoldData();
            }
            else
            {
                parent.textureHitboxPane.loadHoldData((HSObjectHold)holdList.getSelectedValue());
            }
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
            case "holdList":
                int index = holdList.locationToIndex(e.getPoint());

                if(index == -1) { return; }

                if(!holdList.getCellBounds(index, index).contains(e.getPoint())) { return; }

                HSObjectHold hold = (HSObjectHold)holdListModel.get(index);
                holdList.ensureIndexIsVisible(index);
                
                createHoldAttributesWindow(hold);
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
