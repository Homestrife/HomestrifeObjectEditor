/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homestrifeeditor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
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
    public static int textureHitboxPaneWidth = 1920 * 4;
    public static int textureHitboxPaneHeight = 1080 * 4;
    
    public HoldListWindow parent;
    public HSObjectHold hold;
    
    public TextureHitboxLayeredPane textureHitboxPane;
    public JScrollPane textureHitboxScrollPane;
    private JToolBar textureHitboxToolBar;
    
    private JLabel terrainBoxLabel;
    private JComboBox terrainBoxCombo;
    
    private JButton addAttackBoxButton;
    private JButton addHurtBoxButton;
    public JButton removeButton;
    
    private JButton showHideTexturesButton;
    private JButton showHideTerrainBoxButton;
    private JButton showHideAttackBoxesButton;
    private JButton showHideHurtBoxesButton;
    
    public TextureHitboxPane(HoldListWindow theParent)
    {
        parent = theParent;
        hold = null;
        
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
        terrainBoxCombo = new JComboBox(terrainBoxOptions);
        terrainBoxCombo.addItemListener(this);
        terrainBoxCombo.setName("terrainBoxCombo");
        
        JPanel terrainBoxPanel = new JPanel();
        terrainBoxPanel.setLayout(new BoxLayout(terrainBoxPanel, BoxLayout.X_AXIS));
        terrainBoxPanel.add(terrainBoxLabel);
        terrainBoxPanel.add(terrainBoxCombo);
        
        JButton addTextureButton = new JButton("+ Tex");
        addTextureButton.setActionCommand("addTexture");
        addTextureButton.setToolTipText("Create New Texture");
        addTextureButton.addActionListener(this);
        
        addAttackBoxButton = new JButton("+ Atk");
        addAttackBoxButton.setActionCommand("addAttackBox");
        addAttackBoxButton.setToolTipText("Create New Attack Hitbox");
        addAttackBoxButton.addActionListener(this);
        
        addHurtBoxButton = new JButton("+ Hrt");
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
        
        showHideTexturesButton = new JButton("Hide Tex");
        showHideTexturesButton.setActionCommand("showHideTex");
        showHideTexturesButton.setToolTipText("Show/Hide Textures");
        showHideTexturesButton.addActionListener(this);
        
        showHideTerrainBoxButton = new JButton("Hide Ter");
        showHideTerrainBoxButton.setActionCommand("showHideTer");
        showHideTerrainBoxButton.setToolTipText("Show/Hide Terrain Box");
        showHideTerrainBoxButton.addActionListener(this);
        
        showHideAttackBoxesButton = new JButton("Hide Atk");
        showHideAttackBoxesButton.setActionCommand("showHideAtk");
        showHideAttackBoxesButton.setToolTipText("Show/Hide Attack Boxes");
        showHideAttackBoxesButton.addActionListener(this);
        
        showHideHurtBoxesButton = new JButton("Hide Hrt");
        showHideHurtBoxesButton.setActionCommand("showHideHrt");
        showHideHurtBoxesButton.setToolTipText("Show/Hide Hurt Boxes");
        showHideHurtBoxesButton.addActionListener(this);
        
        textureHitboxToolBar = new JToolBar();
        textureHitboxToolBar.setFloatable(false);
        textureHitboxToolBar.add(addTextureButton);
        textureHitboxToolBar.add(addAttackBoxButton);
        textureHitboxToolBar.add(addHurtBoxButton);
        textureHitboxToolBar.add(removeButton);
        textureHitboxToolBar.add(editButton);
        textureHitboxToolBar.add(showHideTexturesButton);
        textureHitboxToolBar.add(showHideTerrainBoxButton);
        textureHitboxToolBar.add(showHideAttackBoxesButton);
        textureHitboxToolBar.add(showHideHurtBoxesButton);
        
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
        float horMid = (horMax - horMin) / (float)2.5;
        float verMin = textureHitboxScrollPane.getVerticalScrollBar().getMinimum();
        float verMax = textureHitboxScrollPane.getVerticalScrollBar().getMaximum();
        float verMid = (verMax - verMin) / (float)2.5;
        textureHitboxScrollPane.getHorizontalScrollBar().setValue((int)horMid);
        textureHitboxScrollPane.getVerticalScrollBar().setValue((int)verMid);
    }
    
    public void loadHoldData(HSObjectHold theHold)
    {
        unloadHoldData();
        
        hold = theHold;
        
        for(HSTexture tex : hold.textures)
        {
            textureHitboxPane.addTexture(tex);
        }
        
        if(hold.IsTerrainObjectHold())
        {
            TerrainObjectHold toHold = (TerrainObjectHold)hold;
            
            for(HSBox box : toHold.attackBoxes)
            {
                textureHitboxPane.addAttackBox(box);
            }
            
            for(HSBox box : toHold.hurtBoxes)
            {
                textureHitboxPane.addHurtBox(box);
            }
        }
        
        setAllEnabled(true);
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
        showHideTexturesButton.setText(textureHitboxPane.showTextures ? "Hide Tex" : "Show Tex");
    }
    
    private void showHideTerrainBoxButtonClicked()
    {
        textureHitboxPane.showHideTerrainBox();
        showHideTerrainBoxButton.setText(textureHitboxPane.showTerrainBox ? "Hide Ter" : "Show Ter");
    }
    
    private void showHideAttackBoxesButtonClicked()
    {
        textureHitboxPane.showHideAttackBoxes();
        showHideAttackBoxesButton.setText(textureHitboxPane.showAttackBoxes ? "Hide Atk" : "Show Atk");
    }
    
    private void showHideHurtBoxesButtonclicked()
    {
        textureHitboxPane.showHideHurtBoxes();
        showHideHurtBoxesButton.setText(textureHitboxPane.showHurtBoxes ? "Hide Hrt" : "Show Hrt");
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
            case "addAttackBox": textureHitboxPane.addAttackBox(); break;
            case "addHurtBox": textureHitboxPane.addHurtBox(); break;
            case "remove": textureHitboxPane.removeSelectedItem(); break;
            case "edit": textureHitboxPane.editSelectedItem(); break;
            case "showHideTex": showHideTexturesButtonClicked(); break;
            case "showHideTer": showHideTerrainBoxButtonClicked(); break;
            case "showHideAtk": showHideAttackBoxesButtonClicked(); break;
            case "showHideHrt": showHideHurtBoxesButtonclicked(); break;
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
}
