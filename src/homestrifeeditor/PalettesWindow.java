/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homestrifeeditor;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author Darlos9D
 */
public class PalettesWindow extends JFrame implements ActionListener, ItemListener {
	private static final long serialVersionUID = 1L;
	
	private static int windowWidth = 720;
    private static int windowHeight = 140;
    private static int windowBorderBuffer = 10;
    
    private static int gridWidth = 700;
    private static int gridRowHeight = 45;
    private static int gridColumns = 4;
    private static int gridHorizontalGap = 10;
    private static int gridVerticalGap = 5;
    
    private HoldListWindow parent;
    
    private JComboBox<Object> paletteCombo;
    
    private JButton applyButton;
    private JLabel pathLabel;
    
    public PalettesWindow(HoldListWindow theParent)
    {
        parent = theParent;
        
        setTitle("Palettes");
        setSize(windowWidth, windowHeight);
        setLocationRelativeTo(null);
        this.setResizable(false);
        
        createWindowContents();
    }
    
    private void createWindowContents()
    {
        JLabel paletteLabel = new JLabel("Current Palette");
        paletteCombo = new JComboBox<Object>(parent.currentlyLoadedObject.palettes.toArray());
        paletteCombo.setRenderer(new PaletteComboBoxRenderer());
        if(paletteCombo.getItemCount() > 0) {
        	paletteCombo.setSelectedIndex(parent.currentlyLoadedObject.curPalette);
        }
        else {
        	
        }
        paletteCombo.addItemListener(this);
        
        JButton newPaletteButton = new JButton("New...");
        newPaletteButton.addActionListener(this);
        newPaletteButton.setActionCommand("newButton");
        newPaletteButton.setEnabled(false);
        
        JButton loadPaletteButton = new JButton("Load...");
        loadPaletteButton.addActionListener(this);
        loadPaletteButton.setActionCommand("loadButton");
        
        JButton clearPaletteButton = new JButton("Clear");
        clearPaletteButton.addActionListener(this);
        clearPaletteButton.setActionCommand("clearButton");
        
        pathLabel = new JLabel();
        setPathLabelText();
        
        JPanel paletteInterface = new JPanel(new GridLayout(1, gridColumns, gridHorizontalGap, gridVerticalGap));
        paletteInterface.setSize(gridWidth, gridRowHeight);
        paletteInterface.add(paletteLabel);
        paletteInterface.add(paletteCombo);
        paletteInterface.add(newPaletteButton);
        paletteInterface.add(loadPaletteButton);
        paletteInterface.add(clearPaletteButton);
        
        JPanel pathInterface = new JPanel();
        pathInterface.setSize(gridWidth, gridRowHeight);
        pathInterface.add(pathLabel);
        
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
        
        JPanel palettesPane = new JPanel();
        palettesPane.setLayout(new BoxLayout(palettesPane, BoxLayout.Y_AXIS));
        palettesPane.setBorder(new EmptyBorder(windowBorderBuffer, windowBorderBuffer, windowBorderBuffer, windowBorderBuffer));
        palettesPane.add(paletteInterface);
        palettesPane.add(pathInterface);
        palettesPane.add(buttonPane);
        
        add(palettesPane);
    }
    
    private void setPathLabelText()
    {
    	if(paletteCombo.getItemCount() == 0)
        {
            pathLabel.setText("NO FILE SELECTED");
        }
    	else if(((HSPalette)paletteCombo.getSelectedItem()).path.isEmpty())
        {
            pathLabel.setText("NO FILE SELECTED");
        }
        else
        {
            pathLabel.setText(((HSPalette)paletteCombo.getSelectedItem()).path);
        }
    }
    
    private void applyChanges()
    {
        parent.currentlyLoadedObject.curPalette = paletteCombo.getSelectedIndex();
        
        parent.textureHitboxPane.reloadTextures();
        
        applyButton.setEnabled(false);
    }
    
    private void closeWindow()
    {
        this.dispose();
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
    
    private void newButtonPressed() {
    	
    }
    
    private void loadButtonPressed()
    {
        JFileChooser fc = new JFileChooser();
        int returnVal = fc.showOpenDialog(null);
        File file;
        
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            file = fc.getSelectedFile();
        } else {
            return;
        }
        
        ((HSPalette)paletteCombo.getSelectedItem()).path = file.getPath();
        setPathLabelText();
    }
    
    private void clearButtonPressed()
    {
        ((HSPalette)paletteCombo.getSelectedItem()).path = "";
        setPathLabelText();
    }
    
    private void fieldChanged()
    {
        setPathLabelText();
        applyButton.setEnabled(true);
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        switch(e.getActionCommand())
        {
            case "okButton": okButtonPressed(); break;
            case "closeButton": closeButtonPressed(); break;
            case "applyButton": applyButtonPressed(); break;
            case "newButton": newButtonPressed(); break;
            case "loadButton": loadButtonPressed(); break;
            case "clearButton": clearButtonPressed(); break;
        }
    }
    
    @Override
    public void itemStateChanged(ItemEvent e)
    {
        fieldChanged();
    }
}
