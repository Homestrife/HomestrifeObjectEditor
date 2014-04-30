/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homestrifeeditor.windows;

import homestrifeeditor.objects.HSObject;
import homestrifeeditor.objects.TerrainObject;
import homestrifeeditor.objects.holds.HSObjectHold;
import homestrifeeditor.objects.holds.TerrainObjectHold;
import homestrifeeditor.objects.holds.properties.HSAudio;
import homestrifeeditor.windows.renderers.SoundListCellRenderer;

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
public class SoundsWindow extends JFrame implements ActionListener, ListSelectionListener, MouseListener, ChangeListener {
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
    private JFrame parent;
    private Object object;
    private String mode;
    private boolean loading;
    
    public DefaultListModel<HSAudio> soundListModel;
    public JList<HSAudio> soundList;
    
    private JToolBar soundListToolBar;
    
    private JLabel soundFile;
    private JButton changeSoundButton;    	private static String changeSoundTooltip = "<html>Load a sound from the hard drive.</html>";
    private JSpinner delaySpinner;       	private static String delayTooltip = "<html>Which frame of this hold the sound triggers on.</html>";
    private JCheckBox exclusiveCheckBox;	private static String exclusiveToolTip = "<html>If set, this sound cancels out all other exclusive sounds previously produced by this object</html>";		
    private JSpinner percentageSpinner; 	private static String percentageToolTip = "<html>When enabled, the % chance that this sound will play</html>";
    private JCheckBox percentageCheckBox;
    
    public SoundsWindow(JFrame theParent, Object theObject, String theMode)
    {
        parent = theParent;
        object = theObject;
        mode = theMode;
        loading = false;
        
        if(mode.compareTo("hold") == 0)
        {
            setTitle("Hold Sounds - " + ((HSObjectHold)object).name);
        }
        else if(mode.compareTo("hit") == 0)
        {
            setTitle("Hit Sounds - " + ((HSObjectHold)object).name);
        }
        else if(mode.compareTo("blocked") == 0)
        {
            setTitle("Blocked Sounds - " + ((HSObjectHold)object).name);
        }
        else if(mode.compareTo("onHit") == 0) {
        	setTitle("On Hit Sounds - " + ((HSObject)object).name);
        }
        setSize(windowWidth, windowHeight);
        setLocationRelativeTo(null);
        
        createWindowContents();
    }
    
    private void createWindowContents()
    {
        JLabel soundListLabel = new JLabel("Sound List");
        soundListModel = new DefaultListModel<HSAudio>();
        soundList = new JList<HSAudio>(soundListModel);
        soundList.setName("soundList");
        soundList.setCellRenderer(new SoundListCellRenderer());
        soundList.addListSelectionListener(this);
        soundList.addMouseListener(this);
        
        if(mode.compareTo("hold") == 0)
        {
            for (HSAudio a : ((HSObjectHold)object).audioList)
            {
                soundListModel.addElement(a);
            }
        }
        else if(mode.compareTo("hit") == 0)
        {
            for (HSAudio a : ((TerrainObjectHold)object).hitAudioList)
            {
                soundListModel.addElement(a);
            }
        }
        else if(mode.compareTo("blocked") == 0)
        {
            for (HSAudio a : ((TerrainObjectHold)object).blockedAudioList)
            {
                soundListModel.addElement(a);
            }
        }
        else if(mode.compareTo("onHit") == 0) {
        	for(HSAudio a : ((TerrainObject)object).onHitSounds) {
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
        soundFile = new JLabel("N/A");
        
        changeSoundButton = new JButton("Change Sound...");
        changeSoundButton.setToolTipText(changeSoundTooltip);
        changeSoundButton.setActionCommand("changeSound");
        changeSoundButton.setEnabled(false);
        changeSoundButton.addActionListener(this);
        
        JLabel delayLabel = new JLabel("Delay");
        delayLabel.setToolTipText(delayTooltip);
        delaySpinner = new JSpinner(new SpinnerNumberModel(0, 0, 99999, 1));
        delaySpinner.setToolTipText(delayTooltip);
        delaySpinner.setValue(0);
        delaySpinner.addChangeListener(this);
        delaySpinner.setEnabled(false);
        
        exclusiveCheckBox = new JCheckBox("Exclusive");
        exclusiveCheckBox.setToolTipText(exclusiveToolTip);
        exclusiveCheckBox.setEnabled(false);
        exclusiveCheckBox.setActionCommand("changeExclusive");
        exclusiveCheckBox.addActionListener(this);
        
        percentageSpinner = new JSpinner(new SpinnerNumberModel(100, 0, 100, 1));
        percentageSpinner.setToolTipText(percentageToolTip);
        percentageSpinner.addChangeListener(this);
        percentageSpinner.setEnabled(false);
        
        percentageCheckBox = new JCheckBox("Play Chance");
        percentageCheckBox.setToolTipText(percentageToolTip);
        percentageCheckBox.setEnabled(false);
        percentageCheckBox.setActionCommand("changeUsePercentage");
        percentageCheckBox.addActionListener(this);
        
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
        JPanel soundDataPane = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        gbc.weightx = .1;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        soundDataPane.add(soundFileLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        soundDataPane.add(soundFile, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.ipady = 20;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        soundDataPane.add(changeSoundButton, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.ipady = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        soundDataPane.add(delayLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        soundDataPane.add(delaySpinner, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        soundDataPane.add(exclusiveCheckBox, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        soundDataPane.add(percentageCheckBox, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.PAGE_END;
        soundDataPane.add(percentageSpinner, gbc);
        
        JSplitPane sPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, soundListPane, soundDataPane);
        sPane.setDividerLocation(100);
        this.setContentPane(sPane);
    }
    
    private void addSoundToSoundList()
    {
        int returnVal = EditorWindow.fileChooser.showOpenDialog(this);
        File file;
        
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            file = EditorWindow.fileChooser.getSelectedFile();
        } else {
            return;
        }
        
        int index = soundList.getSelectedIndex();
        soundList.clearSelection();
        
        HSAudio newAudio = new HSAudio(file.getPath());
        
        if(mode.compareTo("hold") == 0)
        {
        	((HSObjectHold)object).audioList.add(newAudio);
        }
        else if(mode.compareTo("hit") == 0)
        {
            ((TerrainObjectHold)object).hitAudioList.add(newAudio);
        }
        else if(mode.compareTo("blocked") == 0)
        {
            ((TerrainObjectHold)object).blockedAudioList.add(newAudio);
        }
        else if(mode.compareTo("onHit") == 0)
        {
            ((TerrainObject)object).onHitSounds.add(newAudio);
        }
        
        if(index >= 0)
        {
            soundListModel.add(index, newAudio);
            soundList.setSelectedIndex(index);
        }
        else
        {
            soundListModel.addElement(newAudio);
            soundList.setSelectedIndex(soundListModel.getSize() - 1);
        }
    }
    
    public HSAudio removeSoundFromSoundList(int index)
    {
        HSAudio audio = (HSAudio)soundListModel.remove(index);
        if(mode.compareTo("hold") == 0)
        {
        	((HSObjectHold)object).audioList.remove(audio);
        }
        else if(mode.compareTo("hit") == 0)
        {
            ((TerrainObjectHold)object).hitAudioList.remove(audio);
        }
        else if(mode.compareTo("blocked") == 0)
        {
            ((TerrainObjectHold)object).blockedAudioList.remove(audio);
        }
        else if(mode.compareTo("onHit") == 0)
        {
            ((TerrainObject)object).onHitSounds.remove(audio);
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
        loading = true;
        
        soundFile.setText("N/A");
        changeSoundButton.setEnabled(false);
        delaySpinner.setValue(0);
        delaySpinner.setEnabled(false);
        exclusiveCheckBox.setEnabled(false);
        percentageSpinner.setEnabled(false);
        percentageCheckBox.setEnabled(false);
        
        loading = false;
    }
    
    private void loadSoundData(HSAudio sound)
    {
        loading = true;
        
        File file = new File(sound.filePath);

        soundFile.setText(file.getName());
        soundFile.setToolTipText(file.getPath());
        changeSoundButton.setEnabled(true);
        delaySpinner.setValue(sound.delay);
        delaySpinner.setEnabled(true);
        exclusiveCheckBox.setSelected(sound.exclusive);
        exclusiveCheckBox.setEnabled(true);
        percentageSpinner.setValue(sound.percentage);
        
        if(sound.usePercentage)
        {
            percentageSpinner.setEnabled(true);
        }
        else
        {
            percentageSpinner.setEnabled(false);
        }
        
        percentageCheckBox.setSelected(sound.usePercentage);
        percentageCheckBox.setEnabled(true);
        
        loading = false;
    }
    
    private void changeSound()
    {
        int returnVal = EditorWindow.fileChooser.showOpenDialog(this);
        File file;
        
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            file = EditorWindow.fileChooser.getSelectedFile();
        } else {
            return;
        }
        
        int index = soundList.getSelectedIndex();
        HSAudio sound = (HSAudio)soundListModel.get(index);
        
        if(sound != null)
        {
            sound.filePath = file.getPath();
            soundFile.setText(file.getName());
            soundFile.setToolTipText(file.getPath());
        }
        
        soundList.repaint();
    }
    
    private void delayChanged()
    {
        if(loading) { return; }
        int index = soundList.getSelectedIndex();
        if(index == -1) { return; }
        HSAudio sound = (HSAudio)soundListModel.get(index);
        
        if(sound != null)
        {
            sound.delay = (int)delaySpinner.getValue();
        }
    }
    
    private void changeExclusive()
    {
        if(loading) { return; }
        int index = soundList.getSelectedIndex();
        if(index == -1) { return; }
        HSAudio sound = (HSAudio)soundListModel.get(index);
        
        if(sound != null)
        {
            sound.exclusive = exclusiveCheckBox.isSelected();
        }
    }
    
    private void percentageChanged()
    {
        if(loading) { return; }
        int index = soundList.getSelectedIndex();
        if(index == -1) { return; }
        HSAudio sound = (HSAudio)soundListModel.get(index);
        
        if(sound != null)
        {
            sound.percentage = (int)percentageSpinner.getValue();
        }
    }
    
    private void changeUsePercentage()
    {
        if(loading) { return; }
        int index = soundList.getSelectedIndex();
        if(index == -1) { return; }
        HSAudio sound = (HSAudio)soundListModel.get(index);
        
        if(sound != null)
        {
            sound.usePercentage = percentageCheckBox.isSelected();
        }
        
    	percentageSpinner.setEnabled(percentageCheckBox.isSelected());
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        switch(e.getActionCommand())
        {
            case "addSound": addSoundToSoundList(); break;
            case "removeSounds": removeSelectedSoundsFromSoundList(); break;
            case "changeSound": changeSound(); break;
            case "changeExclusive": changeExclusive(); break;
            case "changeUsePercentage": changeUsePercentage(); break;
        }
    }
    
    @Override
    public void valueChanged(ListSelectionEvent e)
    {
        if (e.getValueIsAdjusting() == false)
        {
            unloadSoundData();
            if (soundList.getSelectedIndex() >= 0)
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
            percentageChanged();
        }
    }
}
