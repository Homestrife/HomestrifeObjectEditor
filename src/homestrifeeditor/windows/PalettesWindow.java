/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homestrifeeditor.windows;

import homestrifeeditor.objects.HSObject;
import homestrifeeditor.objects.holds.properties.HSPalette;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author Darlos9D
 */
public class PalettesWindow extends JFrame implements KeyListener, ActionListener, ListSelectionListener, MouseListener, ChangeListener {
	private static final long serialVersionUID = 1L;
	
	private static int windowWidth = 400;
    private static int windowHeight = 200;
    /*
    private static int gridWidth = 650;
    private static int gridRowHeight = 20;
    private static int gridColumns = 2;
    private static int gridHorizontalGap = 10;
    private static int gridVerticalGap = 5;
    */
    private Object object;
    private JFrame parent;
    private boolean loading;
    
    public DefaultListModel<HSPalette> paletteListModel;
    public JList<HSPalette> paletteList;
    
    private JToolBar paletteListToolBar;
    
    private JLabel paletteFile;
    private JButton changePaletteButton;    	private static String changePaletteTooltip = "<html>Load a palette from the hard drive.</html>";
    private JTextField paletteNameTextBox;    	private static String paletteNameTooltip = "<html>The name of the palette</html>";
    
    
    public PalettesWindow(JFrame theParent, Object obj)
    {
    	object = obj;
        parent = theParent;
        loading = false;
        
        setTitle("Palettes");
        setSize(windowWidth, windowHeight);
        setLocationRelativeTo(null);
        
        createWindowContents();
    }
    
    private void createWindowContents()
    {
        JLabel paletteListLabel = new JLabel("Palette List");
        paletteListModel = new DefaultListModel<HSPalette>();
        paletteList = new JList<HSPalette>(paletteListModel);
        paletteList.setName("paletteList");
        paletteList.addListSelectionListener(this);
        paletteList.addMouseListener(this);
        
        JScrollPane holdListScrollPane = new JScrollPane(paletteList);
        
        JButton addPaletteButton = new JButton("+");
        addPaletteButton.setActionCommand("addPalette");
        addPaletteButton.setToolTipText("Add New Palette");
        addPaletteButton.addActionListener(this);
        
        JButton removePaletteButton = new JButton("-");
        removePaletteButton.setActionCommand("removePalette");
        removePaletteButton.setToolTipText("Remove Selected Palette(s)");
        removePaletteButton.addActionListener(this);
        
        paletteListToolBar = new JToolBar();
        paletteListToolBar.setFloatable(false);
        paletteListToolBar.add(addPaletteButton);
        paletteListToolBar.add(removePaletteButton);
        
        JPanel paletteListPane = new JPanel();
        paletteListPane.setLayout(new BorderLayout());
        paletteListPane.add(paletteListLabel, BorderLayout.PAGE_START);
        paletteListPane.add(holdListScrollPane, BorderLayout.CENTER);
        paletteListPane.add(paletteListToolBar, BorderLayout.PAGE_END);
        
        JLabel paletteFileLabel = new JLabel("File:");
        paletteFile = new JLabel("N/A");
        
        changePaletteButton = new JButton("Change Palette...");
        changePaletteButton.setToolTipText(changePaletteTooltip);
        changePaletteButton.setActionCommand("changePalette");
        changePaletteButton.setEnabled(false);
        changePaletteButton.addActionListener(this);
        
        paletteNameTextBox = new JTextField("");
        paletteNameTextBox.setEnabled(false);
        paletteNameTextBox.setActionCommand("changeName");
        paletteNameTextBox.addActionListener(this);
        paletteNameTextBox.addKeyListener(this);
        
        JLabel nameLabel = new JLabel("Name");
        nameLabel.setToolTipText(paletteNameTooltip);
        
        JPanel paletteDataPane = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        gbc.weightx = .1;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        paletteDataPane.add(paletteFileLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        paletteDataPane.add(paletteFile, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.ipady = 20;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        paletteDataPane.add(changePaletteButton, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.ipady = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        paletteDataPane.add(nameLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.ipady = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        paletteDataPane.add(paletteNameTextBox, gbc);
        
        JSplitPane sPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, paletteListPane, paletteDataPane);
        sPane.setDividerLocation(100);
        this.setContentPane(sPane);
        
    	for(HSPalette p : ((HSObject)object).palettes) {
    		paletteListModel.addElement(p);
    	}
    }
    
    private void addPaletteToPaletteList()
    {
        int returnVal = HoldListWindow.fileChooser.showOpenDialog(this);
        File file;
        
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            file = HoldListWindow.fileChooser.getSelectedFile();
        } else {
            return;
        }
        
        int index = paletteList.getSelectedIndex();
        paletteList.clearSelection();
        
        HSPalette newPalette = new HSPalette("New Palette", file.getPath());
        
        ((HSObject)object).palettes.add(newPalette);
        
        if(index >= 0)
        {
            paletteListModel.add(index, newPalette);
            paletteList.setSelectedIndex(index);
        }
        else
        {
            paletteListModel.addElement(newPalette);
            paletteList.setSelectedIndex(paletteListModel.getSize() - 1);
        }
    }
    
    public HSPalette removePaletteFromPaletteList(int index)
    {
    	HSPalette pal = (HSPalette)paletteListModel.remove(index);
    	((HSObject)object).palettes.remove(pal);
        
        return pal;
    }
    
    public ArrayList<HSPalette> removePalettesFromPaletteList(int[] indices)
    {
        ArrayList<HSPalette> removedPalettes = new ArrayList<HSPalette>();
        
        Arrays.sort(indices, 0, indices.length - 1);
        for(int i = indices.length - 1; i >= 0; i--)
        {
            removedPalettes.add(0, removePaletteFromPaletteList(indices[i]));
        }
        
        return removedPalettes;
    }
    
    public ArrayList<HSPalette> removeSelectedPaletteFromPaletteList()
    {
        int n = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete the selected palette(s)?", "Delete Palette(s)", JOptionPane.YES_NO_OPTION);
        
        if(n == 0)
        {
            unloadPaletteData();
            return removePalettesFromPaletteList(paletteList.getSelectedIndices());
        }
        else
        {
            return null;
        }
    }
    
    private void unloadPaletteData()
    {
        loading = true;
        
        paletteFile.setText("N/A");
        changePaletteButton.setEnabled(false);
        paletteNameTextBox.setText("");
        paletteNameTextBox.setEnabled(false);
        
        loading = false;
    }
    
    private void loadPaletteData(HSPalette pal)
    {
        loading = true;
        
        File file = new File(pal.path);

        paletteFile.setText(file.getName());
        paletteFile.setToolTipText(file.getPath());
        changePaletteButton.setEnabled(true);
        paletteNameTextBox.setText(pal.name);
        
        paletteNameTextBox.setEnabled(true);
        
        loading = false;
    }
    
    private void changePalette()
    {
        int returnVal = HoldListWindow.fileChooser.showOpenDialog(this);
        File file;
        
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            file = HoldListWindow.fileChooser.getSelectedFile();
        } else {
            return;
        }
        
        int index = paletteList.getSelectedIndex();
        HSPalette pal = (HSPalette)paletteListModel.get(index);
        
        if(pal != null)
        {
            pal.path = file.getPath();
            paletteFile.setText(file.getName());
            paletteFile.setToolTipText(file.getPath());
        }
        
        paletteList.repaint();
        ((HoldListWindow) parent).updatePalettesMenu();    
    }
    
    private void changeName() {
        int index = paletteList.getSelectedIndex();
        HSPalette pal = (HSPalette)paletteListModel.get(index);
        
        if(pal != null)
        {
            pal.name = paletteNameTextBox.getText();
        }
        
        paletteList.repaint();
        ((HoldListWindow) parent).updatePalettesMenu();        
	}    
    
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        switch(e.getActionCommand())
        {
            case "addPalette": addPaletteToPaletteList(); break;
            case "removePalette": removeSelectedPaletteFromPaletteList(); break;
            case "changePalette": changePalette(); break;
            case "changeName": changeName(); break;
        }
    }

	@Override
    public void valueChanged(ListSelectionEvent e)
    {
        if (e.getValueIsAdjusting() == false)
        {
            unloadPaletteData();
            if (paletteList.getSelectedIndex() >= 0)
            {
                loadPaletteData((HSPalette)paletteList.getSelectedValue());
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
    	
    }

	@Override
	public void keyTyped(KeyEvent e) {
		changeName();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}
}
