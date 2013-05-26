/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homestrifeeditor;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
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
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author Darlos9D
 */
public class SoundsWindow extends JFrame implements ActionListener, ListSelectionListener, MouseListener, ChangeListener {
    private static int windowWidth = 800;
    private static int windowHeight = 400;
    
    private static int gridWidth = 650;
    private static int gridRowHeight = 45;
    private static int gridColumns = 2;
    private static int gridHorizontalGap = 10;
    private static int gridVerticalGap = 5;
    
    private HoldAttributesWindow parent;
    private HSObjectHold hold;
    private String mode;
    
    public DefaultListModel soundListModel;
    public JList soundList;
    
    private JToolBar soundListToolBar;
    
    private JLabel soundFile;
    private JButton loadSoundButton;    private static String loadSoundTooltip = "<html>Load a sound from the hard drive.</html>";
    private JSpinner delaySpinner;       private static String delayTooltip = "<html>Which frame of this hold the sound triggers on.</html>";
    
    public SoundsWindow(HoldAttributesWindow theParent, HSObjectHold theHold, String theMode)
    {
        parent = theParent;
        hold = theHold;
        mode = theMode;
        
        if(mode.compareTo("hold") == 0)
        {
            setTitle("Hold Sounds - " + hold.name);
        }
        else if(mode.compareTo("hit") == 0)
        {
            setTitle("Hit Sounds - " + hold.name);
        }
        setSize(windowWidth, windowHeight);
        setLocationRelativeTo(null);
        
        createWindowContents();
    }
    
    private void createWindowContents()
    {
        JLabel soundListLabel = new JLabel("Sound List");
        soundListModel = new DefaultListModel();
        soundList = new JList(soundListModel);
        soundList.setName("soundList");
        soundList.setCellRenderer(new SoundListCellRenderer());
        soundList.addListSelectionListener(this);
        soundList.addMouseListener(this);
        
        if(mode.compareTo("hold") == 0)
        {
            for (HSAudio a : hold.audioList)
            {
                soundListModel.addElement(a);
            }
        }
        else if(mode.compareTo("hit") == 0)
        {
            for (HSAudio a : ((TerrainObjectHold)hold).hitAudioList)
            {
                soundListModel.addElement(a);
            }
        }
        
        JScrollPane holdListScrollPane = new JScrollPane(soundList);
        
        JButton addSoundButton = new JButton("+");
        addSoundButton.setActionCommand("addSound");
        addSoundButton.setToolTipText("Add New Sound");
        addSoundButton.addActionListener(this);
        
        JButton removeSoundsButton = new JButton("-");
        removeSoundsButton.setActionCommand("removeSounds");
        removeSoundsButton.setToolTipText("Remove Selected Sound(s)");
        removeSoundsButton.addActionListener(this);
        
        soundListToolBar = new JToolBar();
        soundListToolBar.setFloatable(false);
        soundListToolBar.add(addSoundButton);
        soundListToolBar.add(removeSoundsButton);
        
        JPanel soundListPane = new JPanel();
        soundListPane.setLayout(new BorderLayout());
        soundListPane.add(soundListLabel, BorderLayout.PAGE_START);
        soundListPane.add(holdListScrollPane, BorderLayout.CENTER);
        soundListPane.add(soundListToolBar, BorderLayout.PAGE_END);
        
        JLabel soundFileLabel = new JLabel("File:");
        soundFile = new JLabel("");
        
        loadSoundButton = new JButton("Load...");
        loadSoundButton.setToolTipText(loadSoundTooltip);
        loadSoundButton.setActionCommand("loadSound");
        loadSoundButton.addActionListener(this);
        loadSoundButton.setEnabled(false);
        
        JLabel delayLabel = new JLabel("Delay");
        delayLabel.setToolTipText(delayTooltip);
        delaySpinner = new JSpinner(new SpinnerNumberModel(0, 0, 99999, 1));
        delaySpinner.setToolTipText(delayTooltip);
        delaySpinner.setValue(0);
        delaySpinner.addChangeListener(this);
        delaySpinner.setEnabled(false);
        
        JPanel soundDataPane = new JPanel(new GridLayout(2, gridColumns, gridHorizontalGap, gridVerticalGap));
        soundDataPane.setSize(gridWidth, gridRowHeight * 2);
        soundDataPane.setBorder(new TitledBorder("General Attributes"));
        soundDataPane.add(soundFileLabel);
        soundDataPane.add(soundFile);
        soundDataPane.add(loadSoundButton);
        soundDataPane.add(new JLabel(""));
        soundDataPane.add(delayLabel);
        soundDataPane.add(delaySpinner);
        
        this.setContentPane(new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, soundListPane, soundDataPane));
    }
    
    private void addSoundToSoundList()
    {
        JFileChooser fc = new JFileChooser();
        int returnVal = fc.showOpenDialog(null);
        File file;
        
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            file = fc.getSelectedFile();
        } else {
            return;
        }
        
        int index = soundList.getSelectedIndex();
        
        HSAudio newAudio = new HSAudio(file.getPath());
        
        if(mode.compareTo("hold") == 0)
        {
            hold.audioList.add(newAudio);
        }
        else if(mode.compareTo("hit") == 0)
        {
            ((TerrainObjectHold)hold).hitAudioList.add(newAudio);
        }
        
        if(index >= 0)
        {
            soundListModel.add(index, newAudio);
        }
        else
        {
            soundListModel.addElement(newAudio);
        }
    }
    
    public HSAudio removeSoundFromSoundList(int index)
    {
        HSAudio audio = (HSAudio)soundListModel.remove(index);
        if(mode.compareTo("hold") == 0)
        {
            hold.audioList.remove(audio);
        }
        else if(mode.compareTo("hit") == 0)
        {
            ((TerrainObjectHold)hold).hitAudioList.remove(audio);
        }
        return audio;
    }
    
    public ArrayList<HSAudio> removeSoundsFromSoundList(int[] indices)
    {
        ArrayList<HSAudio> removedSounds = new ArrayList<>();
        
        Arrays.sort(indices, 0, indices.length - 1);
        for(int i = indices.length - 1; i >= 0; i--)
        {
            removedSounds.add(0, removeSoundFromSoundList(indices[i]));
        }
        
        return removedSounds;
    }
    
    public ArrayList<HSAudio> removeSelectedSoundsFromSoundList()
    {
        int n = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete the selected sound(s)?", "Delete Sound(s)", JOptionPane.YES_NO_OPTION);
        
        if(n == 0)
        {
            unloadSoundData();
            return removeSoundsFromSoundList(soundList.getSelectedIndices());
        }
        else
        {
            return null;
        }
    }
    
    private void unloadSoundData()
    {
        soundFile.setText("");
        loadSoundButton.setEnabled(false);
        delaySpinner.setValue(0);
        delaySpinner.setEnabled(false);
    }
    
    private void loadSoundData(HSAudio sound)
    {
        File file = new File(sound.filePath);
        
        soundFile.setText(file.getPath());
        loadSoundButton.setEnabled(true);
        delaySpinner.setValue(sound.delay);
        delaySpinner.setEnabled(true);
    }
    
    private void loadSound()
    {
        JFileChooser fc = new JFileChooser();
        int returnVal = fc.showOpenDialog(null);
        File file;
        
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            file = fc.getSelectedFile();
        } else {
            return;
        }
        
        int index = soundList.getSelectedIndex();
        HSAudio sound = (HSAudio)soundListModel.get(index);
        
        if(sound != null)
        {
            sound.filePath = file.getPath();
            soundFile.setText(file.getPath());
        }
        
        soundList.repaint();
    }
    
    private void delayChanged()
    {
        int index = soundList.getSelectedIndex();
        HSAudio sound = (HSAudio)soundListModel.get(index);
        
        if(sound != null)
        {
            sound.delay = (int)delaySpinner.getValue();
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        switch(e.getActionCommand())
        {
            case "addSound": addSoundToSoundList(); break;
            case "removeSounds": removeSelectedSoundsFromSoundList(); break;
            case "loadSound": loadSound(); break;
        }
    }
    
    @Override
    public void valueChanged(ListSelectionEvent e)
    {
        if (e.getValueIsAdjusting() == false)
        {
            if (soundList.getSelectedIndex() == -1)
            {
                unloadSoundData();
            }
            else
            {
                loadSoundData((HSAudio)soundList.getSelectedValue());
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
                int index = soundList.locationToIndex(e.getPoint());

                if(index == -1) { return; }

                if(!soundList.getCellBounds(index, index).contains(e.getPoint())) { return; }

                HSAudio sound = (HSAudio)soundListModel.get(index);
                soundList.ensureIndexIsVisible(index);
                
                //createHoldAttributesWindow(hold);
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
    
    @Override
    public void stateChanged(ChangeEvent e)
    {
        if(e.getSource().getClass().getName().contains("JSpinner"))
        {
            delayChanged();
        }
    }
}
