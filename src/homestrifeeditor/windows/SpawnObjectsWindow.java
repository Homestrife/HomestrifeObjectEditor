/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homestrifeeditor.windows;

import homestrifeeditor.objects.holds.HSObjectHold;
import homestrifeeditor.objects.holds.properties.SpawnObject;
import homestrifeeditor.windows.renderers.SpawnListCellRenderer;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author Darlos9D
 */
public class SpawnObjectsWindow extends JFrame implements ActionListener, ListSelectionListener, MouseListener, ChangeListener {
	private static final long serialVersionUID = 1L;
	
	private static int windowWidth = 400;
    private static int windowHeight = 300;
    /*
    private static int gridWidth = 650;
    private static int gridRowHeight = 20;
    private static int gridColumns = 2;
    private static int gridHorizontalGap = 10;
    private static int gridVerticalGap = 5;
    */
    private JFrame parent;
    private Object object;
    private boolean loading;
    
    public DefaultListModel<SpawnObject> spawnListModel;
    public JList<SpawnObject> spawnList;
    
    private JToolBar spawnListToolBar;
    
    private JLabel objectFile;
    private JButton changeObjectButton;    	private static String changeObjectTooltip = "<html>Load a sound from the hard drive.</html>";
    private JSpinner delaySpinner;       	private static String delayTooltip = "<html>Which frame of this hold the sound triggers on.</html>";
    private JSpinner numberSpinner;       	private static String numberTooltip = "<html>How many of the object should be spawned.</html>";
    private JCheckBox followParentCheck;    private static String followParentTooltip = "<html>Whether or not this object should follow the parent's motion.</html>";
    private JCheckBox collideParentCheck;   private static String collideParentTooltip = "<html>Whether or not this object should collide with its parent.</html>";
    private JSpinner offsetXSpinner;       	private static String offsetXTooltip = "<html>Horizontal spawn location in relation to the parent.</html>";
    private JSpinner offsetYSpinner;       	private static String offsetYTooltip = "<html>Vertical spawn location in relation to the parent.</html>";
    private JSpinner velXSpinner;       	private static String velXTooltip = "<html>Initial horizontal velocity.</html>";
    private JSpinner velYSpinner;       	private static String velYTooltip = "<html>Initial vertical velocity.</html>";
    private JCheckBox useParentPaletteCheck;private static String useParentPaletteTooltip = "<html>Change your colors along with your parent's palette</html>";
    
    public SpawnObjectsWindow(JFrame theParent, Object theObject)
    {
        parent = theParent;
        object = theObject;
        loading = false;
        
        setTitle("Spawn Objects - " + ((HSObjectHold)object).name);
        
        setSize(windowWidth, windowHeight);
        setLocationRelativeTo(null);
        
        createWindowContents();
    }
    
    private void createWindowContents()
    {
        JLabel spawnListLabel = new JLabel("Object List");
        spawnListModel = new DefaultListModel<SpawnObject>();
        spawnList = new JList<SpawnObject>(spawnListModel);
        spawnList.setName("spawnList");
        spawnList.setCellRenderer(new SpawnListCellRenderer());
        spawnList.addListSelectionListener(this);
        spawnList.addMouseListener(this);
        
        for (SpawnObject a : ((HSObjectHold)object).spawnObjects)
        {
            spawnListModel.addElement(a);
        }
        
        JScrollPane spawnListScrollPane = new JScrollPane(spawnList);
        
        JButton addObjectButton = new JButton("+");
        addObjectButton.setActionCommand("addObject");
        addObjectButton.setToolTipText("Add New Object");
        addObjectButton.addActionListener(this);
        
        JButton removeObjectsButton = new JButton("-");
        removeObjectsButton.setActionCommand("removeObjects");
        removeObjectsButton.setToolTipText("Remove Selected Object(s)");
        removeObjectsButton.addActionListener(this);
        
        spawnListToolBar = new JToolBar();
        spawnListToolBar.setFloatable(false);
        spawnListToolBar.add(addObjectButton);
        spawnListToolBar.add(removeObjectsButton);
        
        JPanel spawnListPane = new JPanel();
        spawnListPane.setLayout(new BorderLayout());
        spawnListPane.add(spawnListLabel, BorderLayout.PAGE_START);
        spawnListPane.add(spawnListScrollPane, BorderLayout.CENTER);
        spawnListPane.add(spawnListToolBar, BorderLayout.PAGE_END);
        
        JLabel objectFileLabel = new JLabel("File:");
        objectFile = new JLabel("N/A");
        
        changeObjectButton = new JButton("Change Object...");
        changeObjectButton.setToolTipText(changeObjectTooltip);
        changeObjectButton.setActionCommand("changeObject");
        changeObjectButton.setEnabled(false);
        changeObjectButton.addActionListener(this);
        
        JLabel delayLabel = new JLabel("Delay");
        delayLabel.setToolTipText(delayTooltip);
        delaySpinner = new JSpinner(new SpinnerNumberModel(0, 0, 99999, 1));
        delaySpinner.setToolTipText(delayTooltip);
        delaySpinner.setValue(0);
        delaySpinner.addChangeListener(this);
        delaySpinner.setEnabled(false);
        
        JLabel numberLabel = new JLabel("Number");
        numberLabel.setToolTipText(numberTooltip);
        numberSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 99999, 1));
        numberSpinner.setToolTipText(numberTooltip);
        numberSpinner.setValue(1);
        numberSpinner.addChangeListener(this);
        numberSpinner.setEnabled(false);
        
        JLabel offsetXLabel = new JLabel("Offset X");
        offsetXLabel.setToolTipText(offsetXTooltip);
        offsetXSpinner = new JSpinner(new SpinnerNumberModel(0.0f, -99999.0f, 99999.0f, 1.0f));
        offsetXSpinner.setToolTipText(offsetXTooltip);
        offsetXSpinner.setValue(0);
        offsetXSpinner.addChangeListener(this);
        offsetXSpinner.setEnabled(false);
        
        JLabel offsetYLabel = new JLabel("Offset Y");
        offsetYLabel.setToolTipText(offsetYTooltip);
        offsetYSpinner = new JSpinner(new SpinnerNumberModel(0.0f, -99999.0f, 99999.0f, 1.0f));
        offsetYSpinner.setToolTipText(offsetYTooltip);
        offsetYSpinner.setValue(0);
        offsetYSpinner.addChangeListener(this);
        offsetYSpinner.setEnabled(false);
        
        JLabel velXLabel = new JLabel("Velocity X");
        velXLabel.setToolTipText(velXTooltip);
        velXSpinner = new JSpinner(new SpinnerNumberModel(0.0f, -99999.0f, 99999.0f, 1.0f));
        velXSpinner.setToolTipText(velXTooltip);
        velXSpinner.setValue(0);
        velXSpinner.addChangeListener(this);
        velXSpinner.setEnabled(false);
        
        JLabel velYLabel = new JLabel("Velocity Y");
        velYLabel.setToolTipText(velYTooltip);
        velYSpinner = new JSpinner(new SpinnerNumberModel(0.0f, -99999.0f, 99999.0f, 1.0f));
        velYSpinner.setToolTipText(velYTooltip);
        velYSpinner.setValue(0);
        velYSpinner.addChangeListener(this);
        velYSpinner.setEnabled(false);
        
        followParentCheck = new JCheckBox("Follow Parent");
        followParentCheck.setToolTipText(followParentTooltip);
        followParentCheck.setEnabled(false);
        followParentCheck.setActionCommand("changeFollowParent");
        followParentCheck.addActionListener(this);
        
        collideParentCheck = new JCheckBox("Collide Parent");
        collideParentCheck.setToolTipText(collideParentTooltip);
        collideParentCheck.setEnabled(false);
        collideParentCheck.setActionCommand("changeCollideParent");
        collideParentCheck.addActionListener(this);

        useParentPaletteCheck = new JCheckBox("Use Parent Palette");
        useParentPaletteCheck.setToolTipText(useParentPaletteTooltip);
        useParentPaletteCheck.setEnabled(false);
        useParentPaletteCheck.setActionCommand("useParentPalette");
        useParentPaletteCheck.addActionListener(this);
        
        /*
        JPanel soundDataPane = new JPanel(new GridLayout(2, gridColumns, gridHorizontalGap, gridVerticalGap));
        soundDataPane.setSize(gridWidth, gridRowHeight * 2);
        soundDataPane.setBorder(new TitledBorder("General Attributes"));
        soundDataPane.add(soundFileLabel);
        soundDataPane.add(soundFile);
        soundDataPane.add(loadSoundButton);
        soundDataPane.add(exclusiveCheckBox);
        soundDataPane.add(new JLabel(""));
        soundDataPane.add(delayLabel);
        soundDataPane.add(delaySpinner);
        */
        JPanel spawnDataPane = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        gbc.weightx = .1;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        spawnDataPane.add(objectFileLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        spawnDataPane.add(objectFile, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.ipady = 20;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        spawnDataPane.add(changeObjectButton, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.ipady = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        spawnDataPane.add(delayLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        spawnDataPane.add(delaySpinner, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.ipady = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        spawnDataPane.add(numberLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        spawnDataPane.add(numberSpinner, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.ipady = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        spawnDataPane.add(offsetXLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        spawnDataPane.add(offsetXSpinner, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        gbc.ipady = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        spawnDataPane.add(offsetYLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        spawnDataPane.add(offsetYSpinner, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 1;
        gbc.ipady = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        spawnDataPane.add(velXLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        spawnDataPane.add(velXSpinner, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 1;
        gbc.ipady = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        spawnDataPane.add(velYLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        spawnDataPane.add(velYSpinner, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        spawnDataPane.add(followParentCheck, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 8;
        gbc.gridwidth = 1;
        gbc.ipady = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        spawnDataPane.add(collideParentCheck, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 9;
        gbc.gridwidth = 1;
        gbc.ipady = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        spawnDataPane.add(useParentPaletteCheck, gbc);
        
        JSplitPane sPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, spawnListPane, spawnDataPane);
        sPane.setDividerLocation(100);
        this.setContentPane(sPane);
    }
    
    private void addObjectToSpawnList()
    {
        int returnVal = EditorWindow.fileChooser.showOpenDialog(this);
        File file;
        
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            file = EditorWindow.fileChooser.getSelectedFile();
        } else {
            return;
        }
        
        int index = spawnList.getSelectedIndex();
        spawnList.clearSelection();
        
        SpawnObject newObject = new SpawnObject(file.getPath());
        
        ((HSObjectHold)object).spawnObjects.add(newObject);
        
        if(index >= 0)
        {
            spawnListModel.add(index, newObject);
            spawnList.setSelectedIndex(index);
        }
        else
        {
            spawnListModel.addElement(newObject);
            spawnList.setSelectedIndex(spawnListModel.getSize() - 1);
        }
    }
    
    public SpawnObject removeObjectFromSpawnList(int index)
    {
        SpawnObject spawnObject = (SpawnObject)spawnListModel.remove(index);
        ((HSObjectHold)object).spawnObjects.remove(spawnObject);
        return spawnObject;
    }
    
    public ArrayList<SpawnObject> removeObjectsFromSpawnList(int[] indices)
    {
        ArrayList<SpawnObject> removedObjects = new ArrayList<>();
        
        Arrays.sort(indices, 0, indices.length - 1);
        for(int i = indices.length - 1; i >= 0; i--)
        {
            removedObjects.add(0, removeObjectFromSpawnList(indices[i]));
        }
        
        return removedObjects;
    }
    
    public ArrayList<SpawnObject> removeSelectedObjectsFromSpawnList()
    {
        int n = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete the selected object(s)?", "Delete Object(s)", JOptionPane.YES_NO_OPTION);
        
        if(n == 0)
        {
            unloadObjectData();
            return removeObjectsFromSpawnList(spawnList.getSelectedIndices());
        }
        else
        {
            return null;
        }
    }
    
    private void unloadObjectData()
    {
        loading = true;
        
        objectFile.setText("N/A");
        changeObjectButton.setEnabled(false);
        delaySpinner.setValue(0);
        delaySpinner.setEnabled(false);
        numberSpinner.setValue(0);
        numberSpinner.setEnabled(false);
        offsetXSpinner.setValue(0);
        offsetXSpinner.setEnabled(false);
        offsetYSpinner.setValue(0);
        offsetYSpinner.setEnabled(false);
        velXSpinner.setValue(0);
        velXSpinner.setEnabled(false);
        velYSpinner.setValue(0);
        velYSpinner.setEnabled(false);
        followParentCheck.setEnabled(false);
        collideParentCheck.setEnabled(false);
        useParentPaletteCheck.setEnabled(false);
        
        loading = false;
    }
    
    private void loadObjectData(SpawnObject spawnObject)
    {
        loading = true;
        
        File file = new File(spawnObject.defFilePath);

        objectFile.setText(file.getName());
        objectFile.setToolTipText(file.getPath());
        changeObjectButton.setEnabled(true);
        delaySpinner.setValue(spawnObject.delay);
        delaySpinner.setEnabled(true);
        numberSpinner.setValue(spawnObject.number);
        numberSpinner.setEnabled(true);
        offsetXSpinner.setValue(spawnObject.parentOffset.x);
        offsetXSpinner.setEnabled(true);
        offsetYSpinner.setValue(spawnObject.parentOffset.y);
        offsetYSpinner.setEnabled(true);
        velXSpinner.setValue(spawnObject.vel.x);
        velXSpinner.setEnabled(true);
        velYSpinner.setValue(spawnObject.vel.y);
        velYSpinner.setEnabled(true);
        followParentCheck.setSelected(spawnObject.followParent);
        followParentCheck.setEnabled(true);
        collideParentCheck.setSelected(spawnObject.collideParent);
        collideParentCheck.setEnabled(true);
        useParentPaletteCheck.setSelected(spawnObject.useParentPalette);
        useParentPaletteCheck.setEnabled(true);
        
        loading = false;
    }
    
    private void changeObject()
    {
        int returnVal = EditorWindow.fileChooser.showOpenDialog(this);
        File file;
        
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            file = EditorWindow.fileChooser.getSelectedFile();
        } else {
            return;
        }
        
        int index = spawnList.getSelectedIndex();
        SpawnObject spawnObject = (SpawnObject)spawnListModel.get(index);
        
        if(spawnObject != null)
        {
            spawnObject.defFilePath = file.getPath();
            objectFile.setText(file.getName());
            objectFile.setToolTipText(file.getPath());
        }
        
        spawnList.repaint();
    }
    
    private void valueChanged()
    {
        if(loading) { return; }
        
        int index = spawnList.getSelectedIndex();
        if(index == -1) { return; }
        SpawnObject spawnObject = (SpawnObject)spawnListModel.get(index);
        
        if(spawnObject != null)
        {
            spawnObject.delay = ((SpinnerNumberModel)delaySpinner.getModel()).getNumber().intValue();
            spawnObject.number = ((SpinnerNumberModel)numberSpinner.getModel()).getNumber().intValue();
            spawnObject.parentOffset.x = ((SpinnerNumberModel)offsetXSpinner.getModel()).getNumber().floatValue();
            spawnObject.parentOffset.y = ((SpinnerNumberModel)offsetYSpinner.getModel()).getNumber().floatValue();
            spawnObject.vel.x = ((SpinnerNumberModel)velXSpinner.getModel()).getNumber().floatValue();
            spawnObject.vel.y = ((SpinnerNumberModel)velYSpinner.getModel()).getNumber().floatValue();
            spawnObject.followParent = followParentCheck.isSelected();
            spawnObject.collideParent = collideParentCheck.isSelected();
            spawnObject.useParentPalette = useParentPaletteCheck.isSelected();
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        switch(e.getActionCommand())
        {
            case "addObject": addObjectToSpawnList(); break;
            case "removeObjects": removeSelectedObjectsFromSpawnList(); break;
            case "changeObject": changeObject(); break;
            case "followParentChanged": valueChanged(); break;
            case "collideParentChanged": valueChanged(); break;
            case "useParentPalette": valueChanged(); break;
        }
    }
    
    @Override
    public void valueChanged(ListSelectionEvent e)
    {
        if (e.getValueIsAdjusting() == false)
        {
            unloadObjectData();
            if (spawnList.getSelectedIndex() >= 0)
            {
                loadObjectData((SpawnObject)spawnList.getSelectedValue());
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
    public void stateChanged(ChangeEvent e)
    {
        if(e.getSource().getClass().getName().contains("JSpinner"))
        {
            valueChanged();
        }
    }
}
