/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homestrifeeditor.windows;

import homestrifeeditor.objects.holds.properties.HSBox;
import homestrifeeditor.TGAReader;
import homestrifeeditor.objects.FighterObject;
import homestrifeeditor.objects.HSObject;
import homestrifeeditor.objects.PhysicsObject;
import homestrifeeditor.objects.TerrainObject;
import homestrifeeditor.objects.holds.FighterHold;
import homestrifeeditor.objects.holds.HSObjectHold;
import homestrifeeditor.objects.holds.PhysicsObjectHold;
import homestrifeeditor.objects.holds.TerrainObjectHold;
import homestrifeeditor.objects.holds.properties.Blockability;
import homestrifeeditor.objects.holds.properties.Cancel;
import homestrifeeditor.objects.holds.properties.HSAudio;
import homestrifeeditor.objects.holds.properties.HSPalette;
import homestrifeeditor.objects.holds.properties.HSTexture;
import homestrifeeditor.objects.holds.properties.SpawnObject;
import homestrifeeditor.windows.panes.HoldListPane;
import homestrifeeditor.windows.panes.TextureHitboxPane;

import java.awt.Dimension;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;
import javax.swing.tree.TreePath;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * This is pretty much the main window of the editor. It not only contains the hold list,
 * but also displays the textures, hitboxes, and attributes of the currently selected hold.
 * @author Darlos9D
 */
public class HoldListWindow extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;

    
    //0 - ended 9/1/13
    //1 - all paths now relative to game .exe - current
	//2 - Terrain Boxes are now nested under a <TerrainBoxes> tag
	public static final int XML_FORMAT_VERSION = 2;
	
	public static String BaseWindowTitle = "Homestrife Editor - ";
    public static int windowWidth = 1000;
    public static int windowHeight = 600;
    
    public static int newObjectTerrainBoxSize = 200;
    
    public HoldListPane holdListPane;
    public TextureHitboxPane textureHitboxPane;
    
    public HSObject currentlyLoadedObject;
    
    public String workingDirectory;
    public String exeDirectory;
    public String fileChooserDirectory;
    
    private JMenu palettesMenu;
    
    //File chooser is declared at the class level so that it remembers last folder location..
    public static JFileChooser fileChooser;
    
    public HoldListWindow()
    {
        currentlyLoadedObject = null;
        
        workingDirectory = "";
        
        loadSettings();
        
        addWindowListener(new WindowAdapter() {
        	@Override
        	public void windowClosing(WindowEvent e) {
        		saveSettings();
        		super.windowClosing(e);
        	}
		});
        
        setTitle(BaseWindowTitle + "No Object Loaded");
        setSize(windowWidth, windowHeight);
        setMinimumSize(new Dimension(windowWidth, windowHeight));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        createMenuBar();
        createWindowContents();
        
        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(new KeyDispatcher());
    }

	private void createMenuBar()
    {
        JMenuBar menuBar;
        JMenu file;
        JMenu newObject;
        JMenuItem newGraphic;
        JMenuItem newTerrain;
        JMenuItem newPhysicsObject;
        JMenuItem newFighter;
        JMenuItem generate;
        JMenuItem open;
        JMenuItem save;
        JMenuItem saveAs;
        JMenuItem importAnimation;
        JMenuItem setExeLocation;
        JMenu edit;
        JMenuItem undo;
        JMenuItem redo;
        JMenuItem cut;
        JMenuItem copy;
        JMenuItem paste;
        JMenuItem delete;
        JMenuItem selectAll;
        JMenu object;
        JMenuItem objectAttributes;
        JMenuItem eventHolds;
        JMenu help;
        JMenuItem helpContent;
        JMenuItem about;
        
        menuBar = new JMenuBar();
        
        file = new JMenu("File");
        newObject = new JMenu("New");
        //
        newGraphic = new JMenuItem("Graphic");
        newGraphic.setActionCommand("newGraphic");
        newGraphic.addActionListener(this);
        //
        newTerrain = new JMenuItem("Terrain");
        newTerrain.setActionCommand("newTerrain");
        newTerrain.addActionListener(this);
        //
        newPhysicsObject = new JMenuItem("Physics Object");
        newPhysicsObject.setActionCommand("newPhysicsObject");
        newPhysicsObject.addActionListener(this);
        //
        newFighter = new JMenuItem("Fighter");
        newFighter.setActionCommand("newFighter");
        newFighter.addActionListener(this);
        //
        generate = new JMenuItem("Generate...");
        generate.setActionCommand("generate");
        generate.addActionListener(this);
        //
        open = new JMenuItem("Open...");
        open.setActionCommand("open");
        open.addActionListener(this);
        //
        save = new JMenuItem("Save");
        save.setActionCommand("save");
        save.addActionListener(this);
        save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
        //
        saveAs = new JMenuItem("Save As...");
        saveAs.setActionCommand("saveAs");
        saveAs.addActionListener(this);
        //
        importAnimation = new JMenuItem("Import Animation");
        importAnimation.setActionCommand("importAnimation");
        importAnimation.addActionListener(this);
        //
        setExeLocation = new JMenuItem("Set Game Location");
        setExeLocation.setActionCommand("exeLocation");
        setExeLocation.addActionListener(this);
        //
        newObject.add(newGraphic);
        newObject.add(newTerrain);
        newObject.add(newPhysicsObject);
        newObject.add(newFighter);
        file.add(newObject);
        file.add(generate);
        file.add(open);
        file.add(save);
        file.add(saveAs);
        file.add(new JSeparator());
        file.add(importAnimation);
        file.add(new JSeparator());
        file.add(setExeLocation);
        menuBar.add(file);
        
        edit = new JMenu("Edit");
        //
        undo = new JMenuItem("Undo");
        undo.setActionCommand("undo");
        undo.addActionListener(this);
        undo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK));
        //
        redo = new JMenuItem("Redo");
        redo.setActionCommand("redo");
        redo.addActionListener(this);
        //
        cut = new JMenuItem("Cut");
        cut.setActionCommand("cut");
        cut.addActionListener(this);
        cut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK));
        //
        copy = new JMenuItem("Copy");
        copy.setActionCommand("copy");
        copy.addActionListener(this);
        copy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK));
        //
        paste = new JMenuItem("Paste");
        paste.setActionCommand("paste");
        paste.addActionListener(this);
        paste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK));
        //
        delete = new JMenuItem("Delete");
        delete.setActionCommand("delete");
        delete.addActionListener(this);
        delete.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
        //
        selectAll = new JMenuItem("Select All");
        selectAll.setActionCommand("selectAll");
        selectAll.addActionListener(this);
        selectAll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.ALT_DOWN_MASK));
        //
        
        edit.add(undo);
        edit.add(redo);
        edit.add(new JSeparator());
        edit.add(cut);
        edit.add(copy);
        edit.add(paste);
        edit.add(delete);
        edit.add(selectAll);
        menuBar.add(edit);
        
        object = new JMenu("Object");
        objectAttributes = new JMenuItem("Attributes");
        eventHolds = new JMenuItem("Event Holds");
        objectAttributes.addActionListener(this);
        eventHolds.addActionListener(this);
        objectAttributes.setActionCommand("objectAttributes");
        eventHolds.setActionCommand("eventHolds");
        object.add(objectAttributes);
        object.add(eventHolds);
        menuBar.add(object);
        
        palettesMenu = new JMenu("Palettes");
        menuBar.add(palettesMenu);
        updatePalettesMenu();        
        
        help = new JMenu("Help");
        helpContent = new JMenuItem("Help Content");
        about = new JMenuItem("About");
        help.add(helpContent);
        help.add(about);
        menuBar.add(help);
        
        setJMenuBar(menuBar);
    }
    
    private JComponent createHoldListPane()
    {
        holdListPane = new HoldListPane(this);
        return holdListPane;
    }
    
    private JComponent createHoldDataPane()
    {
        textureHitboxPane = new TextureHitboxPane(this);
        return textureHitboxPane;
    }
    
    private void createWindowContents()
    {
    	JSplitPane sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, createHoldListPane(), createHoldDataPane());
    	sp.setResizeWeight(.34);
        this.setContentPane(sp);
    }
    
    private void loadSettings() {
    	File file = new File("settings.xml");
        
        fileChooser = new JFileChooser(".");
        exeDirectory = "";
        fileChooserDirectory = ".";
        
    	System.out.println("Loading Settings...");
        
		try {
	        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	        Document doc = dBuilder.parse(file);
	        doc.getDocumentElement().normalize();
	        
	        Node root = doc.getDocumentElement();
	        if(root.getNodeName().compareTo("Settings") != 0)
	        {
	        	JOptionPane.showMessageDialog(this, "Settings file has invalid root", "Error loading settings", JOptionPane.ERROR_MESSAGE); 
	        	return; 
	        }
	        
	        NodeList nodes = root.getChildNodes();
	        for(int i=0; i < nodes.getLength(); i++) {
	        	Node node = nodes.item(i);
	        	System.out.println(node.getNodeName() + ": " + node.getTextContent());
	        	switch(node.getNodeName()) {
	        	case "FileChooserDir":
	        		if(node.getTextContent() != null)
	        			fileChooserDirectory = node.getTextContent();
	        		fileChooser = new JFileChooser(fileChooserDirectory);
	        		break;
	        	case "ExeDir":
	        		if(node.getTextContent() != null)
	        			exeDirectory = node.getTextContent();
	        		break;
	        	}
	        }
		} 
        catch(ParserConfigurationException e)
        {
        	JOptionPane.showMessageDialog(this, e.getMessage() + " | Using default settings", "Parser Configuration Exception", JOptionPane.ERROR_MESSAGE); 
        	return; 
        }
        catch(SAXException e)
        {
        	JOptionPane.showMessageDialog(this, e.getMessage() + " | Using default settings", "SAX Exception", JOptionPane.ERROR_MESSAGE);  
        	return;            
        }
        catch(IOException e)
        {
        	//JOptionPane.showMessageDialog(this, e.getMessage() + " | Using default settings", "IO Exception", JOptionPane.ERROR_MESSAGE); 
        	//return;
        }
    	System.out.println("Finished Loading Settings\n");
		
		if(exeDirectory.isEmpty()) {
        	if(JOptionPane.showConfirmDialog(this, "Set game .exe directory now?", ".exe Directory Not Set", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
        		setExeLocation();
        	}
        	else {
        		
        	}
		}
	}
    
    private void saveSettings() {
    	
    	System.out.println("\nSaving Settings...");
        try
        {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();
            
            Element root = doc.createElement("Settings");
            
            Element chooserDir = doc.createElement("FileChooserDir");
            chooserDir.setTextContent(fileChooser.getCurrentDirectory().getPath());
        	System.out.println("FileChooserDir: " + fileChooser.getCurrentDirectory().getPath());
            
            Element exeDir = doc.createElement("ExeDir");
            exeDir.setTextContent(exeDirectory);
        	System.out.println("ExeDir: " + exeDirectory);
            
            root.appendChild(chooserDir);
            root.appendChild(exeDir);
            doc.appendChild(root);
            
            //finally, save the file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("settings.xml"));
            transformer.transform(source, result);
        }
        catch(ParserConfigurationException e)
        {
        	JOptionPane.showMessageDialog(this, e.getMessage(), "Parser Configuration Exception", JOptionPane.ERROR_MESSAGE);  
            
        }
        catch(TransformerConfigurationException e)
        {
        	JOptionPane.showMessageDialog(this, e.getMessage(), "Transformer Configuration Exception", JOptionPane.ERROR_MESSAGE);
        }
        catch(TransformerException e)
        {
        	JOptionPane.showMessageDialog(this, e.getMessage(), "Transformer Exception", JOptionPane.ERROR_MESSAGE);   
        }
        catch(Exception e)
        {
        	
        }
    }
    
    private void setExeLocation() {
    	
    	File lastFile = fileChooser.getCurrentDirectory();
    	
    	fileChooser = new JFileChooser(exeDirectory);
    	fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    	int returnVal = fileChooser.showDialog(this, "Choose .exe Location");
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            exeDirectory = fileChooser.getSelectedFile().getAbsolutePath();
            System.out.println(exeDirectory);
        } else {
            
        }
        fileChooser = new JFileChooser(lastFile);
    }
    
    public void setCurrentlyLoadedObject(HSObject newObject)
    {
        currentlyLoadedObject = newObject;
        setTitle(BaseWindowTitle + newObject.name);
    }
    
    public ArrayList<HSObjectHold> getAllHolds()
    {
        return holdListPane.getAllHolds();
    }
    
    public void applyHoldChanges(HSObjectHold hold, TreePath path)
    {
        holdListPane.applyHoldChanges(hold, path);
    }
    
    public void newObject()
    {
        if(currentlyLoadedObject.IsTerrainObject())
        {
            HSBox terrainBox = new HSBox();
            terrainBox.width = newObjectTerrainBoxSize;
            terrainBox.height = newObjectTerrainBoxSize;
            terrainBox.offset.x = -terrainBox.width / 2;
            terrainBox.offset.y = -terrainBox.height / 2;
            terrainBox.depth = 1;
            ((TerrainObject)currentlyLoadedObject).terrainBoxes.add(terrainBox);
        }
        
        if(currentlyLoadedObject.IsFighter())
        {
            HSBox uprightBox = new HSBox();
            uprightBox.width = newObjectTerrainBoxSize;
            uprightBox.height = newObjectTerrainBoxSize * 2;
            uprightBox.offset.x = -uprightBox.width / 2;
            uprightBox.offset.y = -uprightBox.height;
            uprightBox.depth = 1;
            ((FighterObject)currentlyLoadedObject).uprightTerrainBoxes.add(uprightBox);

            HSBox crouchingBox = new HSBox();
            crouchingBox.width = newObjectTerrainBoxSize;
            crouchingBox.height = newObjectTerrainBoxSize;
            crouchingBox.offset.x = -crouchingBox.width / 2;
            crouchingBox.offset.y = -crouchingBox.height;
            crouchingBox.depth = 1;
            ((FighterObject)currentlyLoadedObject).crouchingTerrainBoxes.add(crouchingBox);

            HSBox proneBox = new HSBox();
            proneBox.width = newObjectTerrainBoxSize * 2;
            proneBox.height = newObjectTerrainBoxSize;
            proneBox.offset.x = -proneBox.width / 2;
            proneBox.offset.y = -proneBox.height;
            proneBox.depth = 1;
            ((FighterObject)currentlyLoadedObject).proneTerrainBoxes.add(proneBox);

            HSBox compactBox = new HSBox();
            compactBox.width = newObjectTerrainBoxSize;
            compactBox.height = newObjectTerrainBoxSize;
            compactBox.offset.x = -compactBox.width / 2;
            compactBox.offset.y = -compactBox.height * (float)1.5;
            compactBox.depth = 1;
            ((FighterObject)currentlyLoadedObject).compactTerrainBoxes.add(compactBox);
        }
        
        holdListPane.loadObjectHolds(currentlyLoadedObject);
        textureHitboxPane.resetScrollBars();
        textureHitboxPane.setCorrectTerrainBox();
        updatePalettesMenu();
    }
    
    public void newGraphic()
    {
    	workingDirectory = "";
        setCurrentlyLoadedObject(new HSObject());
        HSObjectHold newHold = new HSObjectHold();
        currentlyLoadedObject.holds.add(newHold);
        newObject();
        updatePalettesMenu();
    }
    
    public void newTerrain()
    {
    	workingDirectory = "";
        setCurrentlyLoadedObject(new TerrainObject());
        TerrainObjectHold newHold = new TerrainObjectHold();
        currentlyLoadedObject.holds.add(newHold);
        newObject();
        updatePalettesMenu();
    }
    
    public void newPhysicsObject()
    {
    	workingDirectory = "";
        setCurrentlyLoadedObject(new PhysicsObject());
        PhysicsObjectHold newHold = new PhysicsObjectHold();
        currentlyLoadedObject.holds.add(newHold);
        newObject();
        updatePalettesMenu();
    }
    
    public void newFighter()
    {
    	workingDirectory = "";
        setCurrentlyLoadedObject(new FighterObject());
        FighterHold newHold = new FighterHold();
        currentlyLoadedObject.holds.add(newHold);
        
        newObject();
        updatePalettesMenu();
    }
    
    private void generate()
    {
        int returnVal = fileChooser.showOpenDialog(this);
        File file;
        
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            file = fileChooser.getSelectedFile();
        } else {
            return;
        }
        
        try
        {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();
            
            if(doc.getDocumentElement().getNodeName().compareTo("HSAutoGenerateDefinition") != 0)
            {
                return;
            }
            
            String objectType = doc.getElementsByTagName("ObjectType").item(0).getTextContent();
            
            HSObject newObject;
            
            if(objectType.compareTo("Graphic") == 0)
            {
                newObject = new HSObject();
            }
            else if(objectType.compareTo("Terrain") == 0)
            {
                newObject = new TerrainObject();
            }
            else if(objectType.compareTo("PhysicsObject") == 0)
            {
                newObject = new PhysicsObject();
            }
            else if(objectType.compareTo("Fighter") == 0)
            {
                newObject = new FighterObject();
            }
            else
            {
                return;
            }
            
            //Now, start loading all the textures in all the directories located in the same directory as the auto generate definition.
            //Each individual texture will become its own hold. Holds from the same folder will be linked together as animations.
            //Also, the first hold of specially named folders will be marked as event holds
            File[] animDirectories = file.getParentFile().listFiles();
            
            //first get the palette folder and load any available palettes.
            //there needs to be at least one, or else indexed textures won't load
            for(File f : animDirectories)
            {
                if(!f.isDirectory() || f.getName().compareTo("palettes") != 0)  { continue; }
                
                File[] palettes = f.listFiles();
                
                int paletteIndex = 0;
                
                for(File p : palettes)
                {
                    String type = Files.probeContentType(p.toPath());
                    if(type != null || !p.getName().endsWith(".hsp")) { continue; }
                    //if the type is null but the file ends with .hsp then it's PROBABLY a homestrife palette
                    HSPalette pal = new HSPalette();
                    pal.path = p.getAbsolutePath();
                    newObject.palettes.add(pal);
                    //newObject.palettes[paletteIndex].palFilePath = p.getAbsolutePath();
                    
                    paletteIndex++;
                    
                    if(paletteIndex >= palettes.length) { break; }
                }
                
                if(paletteIndex >= palettes.length) { break; }
            }
            
            for(File f : animDirectories)
            {
                if(!f.isDirectory() || f.getName().compareTo("palettes") == 0)  { continue; }
                
                File[] holds = f.listFiles();
                
                HSObjectHold prevHold = null;
                
                for(File h : holds)
                {
                    if(!h.isFile()) { continue; }
                    
                    String type = Files.probeContentType(h.toPath());
                    if(type != null || !h.getName().endsWith(".tga")) { continue; }
                    //if the type is null but the file ends with .tga then it's PROBABLY a targa
                    
                    HSObjectHold newHold;
                    
                    if(newObject.IsFighter()) { newHold = new FighterHold(); }
                    else if(newObject.IsPhysicsObject()) { newHold = new PhysicsObjectHold(); }
                    else if(newObject.IsTerrainObject()) { newHold = new TerrainObjectHold(); }
                    else { newHold = new HSObjectHold(); }
                    
                    //see if we can actually load the file as a texture
                    ImageIcon icon = TGAReader.loadTGA(h.getAbsolutePath(), newObject.palettes.size() > 0 ? newObject.palettes.get(0).path : "");
                    
                    if(icon == null) { continue; } //whoops
                    
                    HSTexture newTex = new HSTexture(h.getAbsolutePath());
                    newTex.depth = 0;
                    newTex.offset.x = -icon.getIconWidth() / 2;
                    
                    if(newObject.IsFighter())
                    {
                        //Center the texture on the vertical axis, but
                        //move it completely above the horizontal axis
                        newTex.offset.y = -icon.getIconHeight();
                    }
                    else
                    {
                        //center the texture completely on both axes
                        newTex.offset.y = -icon.getIconHeight() / 2;
                    }
                    
                    newHold.duration = 4;
                    newHold.name = h.getName().replace(".tga", "");
                    newHold.textures.add(newTex);
                    
                    newObject.holds.add(newHold);
                    
                    if(prevHold == null)
                    {
                        //Check if the current directory represents an event animation. If so, make this the event hold
                        String directoryName = f.getName();
                        if(directoryName.compareTo("lifetime_death") == 0) { newObject.hsObjectEventHolds.lifetimeDeath = newHold; }
                        
                        if(newObject.IsTerrainObject())
                        {
                            if(directoryName.compareTo("health_death") == 0) { ((TerrainObject)newObject).terrainEventHolds.healthDeath = (TerrainObjectHold)newHold; }
                        }
                        
                        if(newObject.IsPhysicsObject())
                        {
                            
                        }
                        
                        if(newObject.IsFighter())
                        {
                            if(directoryName.compareTo("idle") == 0) { ((FighterObject)newObject).fighterEventHolds.standing = (FighterHold)newHold; }
                            if(directoryName.compareTo("idleturn") == 0) { ((FighterObject)newObject).fighterEventHolds.turn = (FighterHold)newHold; }
                            if(directoryName.compareTo("walkstart") == 0) { ((FighterObject)newObject).fighterEventHolds.walk = (FighterHold)newHold; }
                            if(directoryName.compareTo("walk") == 0) { ((FighterObject)newObject).fighterEventHolds.walking = (FighterHold)newHold; }
                            if(directoryName.compareTo("walkturn") == 0) { ((FighterObject)newObject).fighterEventHolds.walkingTurn = (FighterHold)newHold; }
                            if(directoryName.compareTo("crouchstart") == 0) { ((FighterObject)newObject).fighterEventHolds.crouch = (FighterHold)newHold; }
                            if(directoryName.compareTo("crouch") == 0) { ((FighterObject)newObject).fighterEventHolds.crouching = (FighterHold)newHold; }
                            if(directoryName.compareTo("crouchturn") == 0) { ((FighterObject)newObject).fighterEventHolds.crouchingTurn = (FighterHold)newHold; }
                            if(directoryName.compareTo("crouchend") == 0) { ((FighterObject)newObject).fighterEventHolds.stand = (FighterHold)newHold; }
                            if(directoryName.compareTo("dashstart") == 0) { ((FighterObject)newObject).fighterEventHolds.run = (FighterHold)newHold; }
                            if(directoryName.compareTo("dash") == 0) { ((FighterObject)newObject).fighterEventHolds.running = (FighterHold)newHold; }
                            if(directoryName.compareTo("dashstop") == 0) { ((FighterObject)newObject).fighterEventHolds.runningStop = (FighterHold)newHold; }
                            if(directoryName.compareTo("dashturn") == 0) { ((FighterObject)newObject).fighterEventHolds.runningTurn = (FighterHold)newHold; }
                            if(directoryName.compareTo("jumpstart") == 0) { ((FighterObject)newObject).fighterEventHolds.jumpNeutralStart = (FighterHold)newHold; }
                            if(directoryName.compareTo("jumpstart_air") == 0) { ((FighterObject)newObject).fighterEventHolds.jumpNeutralStartAir = (FighterHold)newHold; }
                            if(directoryName.compareTo("jumprise_back") == 0) { ((FighterObject)newObject).fighterEventHolds.jumpBackwardRising = (FighterHold)newHold; }
                            if(directoryName.compareTo("jumprise_fwdneu") == 0) { ((FighterObject)newObject).fighterEventHolds.jumpNeutralRising = (FighterHold)newHold; }
                            if(directoryName.compareTo("jumppeak_back") == 0) { ((FighterObject)newObject).fighterEventHolds.jumpBackwardFall = (FighterHold)newHold; }
                            if(directoryName.compareTo("jumppeak_fwdneu") == 0) { ((FighterObject)newObject).fighterEventHolds.jumpNeutralFall = (FighterHold)newHold; }
                            if(directoryName.compareTo("jumpfall") == 0) { ((FighterObject)newObject).fighterEventHolds.jumpNeutralFalling = (FighterHold)newHold; }
                            if(directoryName.compareTo("landsoft") == 0) { ((FighterObject)newObject).fighterEventHolds.jumpNeutralLand = (FighterHold)newHold; }
                            if(directoryName.compareTo("airdash_back") == 0) { ((FighterObject)newObject).fighterEventHolds.airDashBackward = (FighterHold)newHold; }
                            if(directoryName.compareTo("airdash_fwd") == 0) { ((FighterObject)newObject).fighterEventHolds.airDashForward = (FighterHold)newHold; }
                            if(directoryName.compareTo("block_stand") == 0) { ((FighterObject)newObject).fighterEventHolds.blockHigh = (FighterHold)newHold; }
                            if(directoryName.compareTo("block_crouch") == 0) { ((FighterObject)newObject).fighterEventHolds.blockLow = (FighterHold)newHold; }
                            if(directoryName.compareTo("block_aerial") == 0) { ((FighterObject)newObject).fighterEventHolds.blockAir = (FighterHold)newHold; }
                            if(directoryName.compareTo("hitstand_highlt") == 0) { ((FighterObject)newObject).fighterEventHolds.hitstunLightHighStanding = (FighterHold)newHold; }
                            if(directoryName.compareTo("hitstand_midlt") == 0) { ((FighterObject)newObject).fighterEventHolds.hitstunLightMidStanding = (FighterHold)newHold; }
                            if(directoryName.compareTo("hitstand_lowlt") == 0) { ((FighterObject)newObject).fighterEventHolds.hitstunLightLowStanding = (FighterHold)newHold; }
                            if(directoryName.compareTo("hitcrouch_midlt") == 0) { ((FighterObject)newObject).fighterEventHolds.hitstunLightMidCrouching = (FighterHold)newHold; }
                            if(directoryName.compareTo("hitcrouch_lowlt") == 0) { ((FighterObject)newObject).fighterEventHolds.hitstunLightLowCrouching = (FighterHold)newHold; }
                            if(directoryName.compareTo("hitaerial_lt") == 0) { ((FighterObject)newObject).fighterEventHolds.hitstunLightAir = (FighterHold)newHold; }
                            if(directoryName.compareTo("atklt") == 0) { ((FighterObject)newObject).fighterEventHolds.attackLightNeutralGround = (FighterHold)newHold; }
                            if(directoryName.compareTo("atklt_dwn") == 0) { ((FighterObject)newObject).fighterEventHolds.attackLightDownGround = (FighterHold)newHold; }
                            if(directoryName.compareTo("atklt_fwd") == 0) { ((FighterObject)newObject).fighterEventHolds.attackLightForwardGround = (FighterHold)newHold; }
                            if(directoryName.compareTo("atklt_up") == 0) { ((FighterObject)newObject).fighterEventHolds.attackLightUpGround = (FighterHold)newHold; }
                            if(directoryName.compareTo("atklt_air") == 0) { ((FighterObject)newObject).fighterEventHolds.attackLightNeutralAir = (FighterHold)newHold; }
                            if(directoryName.compareTo("special_fwdlt") == 0) { ((FighterObject)newObject).fighterEventHolds.attackLightQCFGround = (FighterHold)newHold; }
                            if(directoryName.compareTo("atklt_airdwn") == 0) { ((FighterObject)newObject).fighterEventHolds.attackLightDownAir = (FighterHold)newHold; }
                            if(directoryName.compareTo("atklt_airfwd") == 0) { ((FighterObject)newObject).fighterEventHolds.attackLightForwardAir = (FighterHold)newHold; }
                            if(directoryName.compareTo("atklt_airup") == 0) { ((FighterObject)newObject).fighterEventHolds.attackLightUpAir = (FighterHold)newHold; }
                            if(directoryName.compareTo("atklt_airback") == 0) { ((FighterObject)newObject).fighterEventHolds.attackLightBackwardAir = (FighterHold)newHold; }
                            if(directoryName.compareTo("special_airfwdlt") == 0) { ((FighterObject)newObject).fighterEventHolds.attackLightQCFAir = (FighterHold)newHold; }
                            
                            if(directoryName.compareTo("atkhvy") == 0) { ((FighterObject)newObject).fighterEventHolds.attackHeavyNeutralGround = (FighterHold)newHold; }
                            if(directoryName.compareTo("atkhvy_dwn") == 0) { ((FighterObject)newObject).fighterEventHolds.attackHeavyDownGround = (FighterHold)newHold; }
                            if(directoryName.compareTo("atkhvy_fwd") == 0) { ((FighterObject)newObject).fighterEventHolds.attackHeavyForwardGround = (FighterHold)newHold; }
                            if(directoryName.compareTo("atkhvy_up") == 0) { ((FighterObject)newObject).fighterEventHolds.attackHeavyUpGround = (FighterHold)newHold; }
                            if(directoryName.compareTo("atkhvy_air") == 0) { ((FighterObject)newObject).fighterEventHolds.attackHeavyNeutralAir = (FighterHold)newHold; }
                            if(directoryName.compareTo("special_fwdhvy") == 0) { ((FighterObject)newObject).fighterEventHolds.attackHeavyQCFGround = (FighterHold)newHold; }
                            if(directoryName.compareTo("atkhvy_airdwn") == 0) { ((FighterObject)newObject).fighterEventHolds.attackHeavyDownAir = (FighterHold)newHold; }
                            if(directoryName.compareTo("atkhvy_airfwd") == 0) { ((FighterObject)newObject).fighterEventHolds.attackHeavyForwardAir = (FighterHold)newHold; }
                            if(directoryName.compareTo("atkhvy_airup") == 0) { ((FighterObject)newObject).fighterEventHolds.attackHeavyUpAir = (FighterHold)newHold; }
                            if(directoryName.compareTo("atkhvy_airback") == 0) { ((FighterObject)newObject).fighterEventHolds.attackHeavyBackwardAir = (FighterHold)newHold; }
                            if(directoryName.compareTo("special_airfwdhvy") == 0) { ((FighterObject)newObject).fighterEventHolds.attackHeavyQCFAir = (FighterHold)newHold; }
                            
                            if(directoryName.compareTo("ko") == 0) { ((FighterObject)newObject).fighterEventHolds.knockout = (FighterHold)newHold; }
                        }
                    }
                    else
                    {
                        prevHold.nextHold = newHold;
                    }
                    
                    prevHold = newHold;
                }
            }
        
            setCurrentlyLoadedObject(newObject);
            newObject();
            
            workingDirectory = file.getParent();
            updatePalettesMenu();
        }
        catch(ParserConfigurationException e)
        {
        	JOptionPane.showMessageDialog(this, e.getMessage(), "Parser Configuration Exception", JOptionPane.ERROR_MESSAGE);    
        }
        catch(SAXException e)
        {
        	JOptionPane.showMessageDialog(this, e.getMessage(), "SAX Exception", JOptionPane.ERROR_MESSAGE);                
        }
        catch(IOException e)
        {
        	JOptionPane.showMessageDialog(this, e.getMessage(), "IO Exception", JOptionPane.ERROR_MESSAGE);                
        }
    }
    
    private HSObjectHold getHoldFromId(HSObject object, int id)
    {
        for(HSObjectHold hold : object.holds)
        {
            if(hold.id == id) { return hold; }
        }
        
        return null;
    }
    
    private String createAbsolutePath(String relPath)
    {
    	return createAbsolutePathFrom(relPath, workingDirectory);
    }
    
    private String createAbsolutePathFrom(String relPath, String fromPath)
    {
    	relPath = relPath.replace('\\', File.separatorChar);
    	relPath = relPath.replace('/', File.separatorChar);
    	if(!fromPath.endsWith(File.separator)) fromPath += File.separator;
    	File a = new File(fromPath);
	    File b = new File(a, relPath);
	    String absolute = "";
		try {
			absolute = b.getCanonicalPath();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	    return absolute;
    }
    
    private String createRelativePath(String absPath)
    {
    	return createPathRelativeTo(absPath, workingDirectory);
    }

    
    private String createPathRelativeTo(String absPath, String relativeTo)
    {
    	String[] relativeToPieces = relativeTo.split(File.separator.compareTo("\\") == 0 ? "\\\\" : "/");
        String[] absPathPieces = absPath.split(File.separator.compareTo("\\") == 0 ? "\\\\" : "/");
        
        //first, make sure they share the same drive
        if(!relativeToPieces[0].equals(absPathPieces[0]))
        {
            return "";
        }
        
        //compare each until either one ends or a point of divergeance is found
        int end = relativeToPieces.length > absPathPieces.length ? absPathPieces.length : relativeToPieces.length;
        int divergeancePoint = end;
        for(int i = 0; i < end; i++)
        {
            if(!relativeToPieces[i].equals(absPathPieces[i]))
            {
                divergeancePoint = i;
                break;
            }
        }
        
        //add double periods to signify parent directories
        String relativePath = "";
        for(int i = 0; i < end - divergeancePoint; i++)
        {
            relativePath += ".." + File.separator;
        }
        
        //add the absolute path starting with the divergeance point
        for(int i = divergeancePoint; i < absPathPieces.length; i++)
        {
            if(i > divergeancePoint)
            {
                relativePath += File.separator;
            }
            relativePath += absPathPieces[i];
        }
        
        return relativePath;
    }
    
    private void open()
    {
        int returnVal = fileChooser.showOpenDialog(this);
        File file;
        
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            file = fileChooser.getSelectedFile();
        } else {
            return;
        }
        
        try
        {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();
            
            int version = 0;
            
            if(doc.getDocumentElement().getNodeName().compareTo("HSObjects") != 0)
            {
                return;
            }
            
            if(!doc.getDocumentElement().getAttribute("version").isEmpty()) version = Integer.parseInt(doc.getDocumentElement().getAttribute("version"));
            
            System.out.println("Loading object with xml format version: " + version);
            
            Node object = doc.getDocumentElement().getFirstChild();
            
            HSObject loadObject;
            
            if(object.getNodeName().compareTo("Fighter") == 0)
            {
                loadObject = new FighterObject();
            }
            else if(object.getNodeName().compareTo("PhysicsObject") == 0)
            {
                loadObject = new PhysicsObject();
            }
            else if(object.getNodeName().compareTo("TerrainObject") == 0)
            {
                loadObject = new TerrainObject();
            }
            else if(object.getNodeName().compareTo("HSObject") == 0)
            {
                loadObject = new HSObject();
            }
            else
            {
                return;
            }
            
            //get the base path
            workingDirectory = file.getParent();
            
            String relativeDir = exeDirectory;
            
            if(version == 0) {
            	//If this is loading with version 0, we use the working directory to load
            	relativeDir = workingDirectory;
            }
            
            //get the definition sections
            NodeList defSecs = object.getChildNodes();
            LinkedList<NamedNodeMap> terrainBoxAttributes = new LinkedList<NamedNodeMap>();
            Node terrainBoxes = null;
            NamedNodeMap uprightTerrainBoxAttributes = null;
            NamedNodeMap crouchingTerrainBoxAttributes = null;
            NamedNodeMap proneTerrainBoxAttributes = null;
            NamedNodeMap compactTerrainBoxAttributes = null;
            NamedNodeMap eventHoldsAttributes = null;
            Node holds = null;
            Node palettes = null;
            for(int i = 0; i < defSecs.getLength(); i++)
            {
                Node defSec = defSecs.item(i);
                
                if(defSec.getNodeName().compareTo("TerrainBox") == 0) terrainBoxAttributes.add(defSec.getAttributes());
                if(defSec.getNodeName().compareTo("TerrainBoxes") == 0) terrainBoxes = defSec;
                if(defSec.getNodeName().compareTo("UprightTerrainBox") == 0) uprightTerrainBoxAttributes = defSec.getAttributes();
                if(defSec.getNodeName().compareTo("CrouchingTerrainBox") == 0) crouchingTerrainBoxAttributes = defSec.getAttributes();
                if(defSec.getNodeName().compareTo("ProneTerrainBox") == 0) proneTerrainBoxAttributes = defSec.getAttributes();
                if(defSec.getNodeName().compareTo("CompactTerrainBox") == 0) compactTerrainBoxAttributes = defSec.getAttributes();
                if(defSec.getNodeName().compareTo("EventHolds") == 0) eventHoldsAttributes = defSec.getAttributes();
                if(defSec.getNodeName().compareTo("Holds") == 0) holds = defSec;
                if(defSec.getNodeName().compareTo("Palettes") == 0) palettes = defSec;
            }
            
            //get holds
            NodeList holdList = holds.getChildNodes();
            for(int i = 0; i < holdList.getLength(); i++)
            {
                Node hold = holdList.item(i);
                
                if(hold.getNodeName().compareTo("Hold") != 0) { continue; }
                
                HSObjectHold loadHold;
                
                if(object.getNodeName().compareTo("Fighter") == 0)
                {
                    loadHold = new FighterHold();
                }
                else if(object.getNodeName().compareTo("PhysicsObject") == 0)
                {
                    loadHold = new PhysicsObjectHold();
                }
                else if(object.getNodeName().compareTo("TerrainObject") == 0)
                {
                    loadHold = new TerrainObjectHold();
                }
                else
                {
                    loadHold = new HSObjectHold();
                }
                
                //get hold definition sections
                NodeList holdSecs = hold.getChildNodes();
                Node textures = null;
                Node audioList = null;
                Node spawnObjects = null;
                Node attackBoxes = null;
                Node hurtBoxes = null;
                Node hitAudioList = null;
                Node blockedAudioList = null;
                for(int j = 0; j < holdSecs.getLength(); j++)
                {
                    Node holdSec = holdSecs.item(j);

                    if(holdSec.getNodeName().compareTo("Textures") == 0) textures = holdSec;
                    if(holdSec.getNodeName().compareTo("AudioList") == 0) audioList = holdSec;
                    if(holdSec.getNodeName().compareTo("SpawnObjects") == 0) spawnObjects = holdSec;
                    if(holdSec.getNodeName().compareTo("AttackBoxes") == 0) attackBoxes = holdSec;
                    if(holdSec.getNodeName().compareTo("HurtBoxes") == 0) hurtBoxes = holdSec;
                    if(holdSec.getNodeName().compareTo("HitAudioList") == 0) hitAudioList = holdSec;
                    if(holdSec.getNodeName().compareTo("BlockedAudioList") == 0) blockedAudioList = holdSec;
                }
                
                //get hold attributes
                NamedNodeMap holdAttributes = hold.getAttributes();
                
                //get hs object hold attributes
                if(holdAttributes.getNamedItem("name") != null) loadHold.name = holdAttributes.getNamedItem("name").getNodeValue();
                if(holdAttributes.getNamedItem("id") != null) loadHold.id = Integer.parseInt(holdAttributes.getNamedItem("id").getNodeValue());
                if(holdAttributes.getNamedItem("nextHoldId") != null) loadHold.nextHoldId = Integer.parseInt(holdAttributes.getNamedItem("nextHoldId").getNodeValue());
                if(holdAttributes.getNamedItem("duration") != null) loadHold.duration = Integer.parseInt(holdAttributes.getNamedItem("duration").getNodeValue());
                if(holdAttributes.getNamedItem("repositionX") != null) loadHold.reposition.x = Float.parseFloat(holdAttributes.getNamedItem("repositionX").getNodeValue());
                if(holdAttributes.getNamedItem("repositionY") != null) loadHold.reposition.y = Float.parseFloat(holdAttributes.getNamedItem("repositionY").getNodeValue());
                if(holdAttributes.getNamedItem("velocityX") != null) loadHold.velocity.x = Float.parseFloat(holdAttributes.getNamedItem("velocityX").getNodeValue());
                if(holdAttributes.getNamedItem("velocityY") != null) loadHold.velocity.y = Float.parseFloat(holdAttributes.getNamedItem("velocityY").getNodeValue());
                if(holdAttributes.getNamedItem("overwriteVelocity") != null) loadHold.overwriteVelocity = Boolean.parseBoolean(holdAttributes.getNamedItem("overwriteVelocity").getNodeValue());
                
                //get textures
                if(textures != null)
                {
                    NodeList textureList = textures.getChildNodes();
                    for(int j = 0; j < textureList.getLength(); j++)
                    {
                        if(textureList.item(j).getNodeName().compareTo("Texture") != 0) { continue; }
                        NamedNodeMap textureAttributes = textureList.item(j).getAttributes();
                        
                        String filePath = "";
                        if(textureAttributes.getNamedItem("textureFilePath") != null) filePath = createAbsolutePathFrom(textureAttributes.getNamedItem("textureFilePath").getNodeValue(), relativeDir);
                        filePath = filePath.replace('/', File.separatorChar);
                        filePath = filePath.replace('\\', File.separatorChar);
                        HSTexture tex = new HSTexture(filePath);
                        if(textureAttributes.getNamedItem("depth") != null) tex.depth = Integer.parseInt(textureAttributes.getNamedItem("depth").getNodeValue());
                        if(textureAttributes.getNamedItem("offsetX") != null) tex.offset.x = Float.parseFloat(textureAttributes.getNamedItem("offsetX").getNodeValue());
                        if(textureAttributes.getNamedItem("offsetY") != null) tex.offset.y = Float.parseFloat(textureAttributes.getNamedItem("offsetY").getNodeValue());
                        
                        loadHold.textures.add(tex);
                    }
                }
                
                //get audio
                if(audioList != null)
                {
                    NodeList audioListList = audioList.getChildNodes();
                    for(int j = 0; j < audioListList.getLength(); j++)
                    {
                        if(audioListList.item(j).getNodeName().compareTo("Audio") != 0) { continue; }
                        NamedNodeMap audioAttributes = audioListList.item(j).getAttributes();
                        
                        String filePath = "";
                        if(audioAttributes.getNamedItem("audioFilePath") != null) filePath = createAbsolutePathFrom(audioAttributes.getNamedItem("audioFilePath").getNodeValue(), relativeDir);
                        filePath = filePath.replace('/', File.separatorChar);
                        filePath = filePath.replace('\\', File.separatorChar);
                        HSAudio aud = new HSAudio(filePath);
                        if(audioAttributes.getNamedItem("delay") != null) aud.delay = Integer.parseInt(audioAttributes.getNamedItem("delay").getNodeValue());
                        if(audioAttributes.getNamedItem("exclusive") != null) aud.exclusive = Boolean.parseBoolean(audioAttributes.getNamedItem("exclusive").getNodeValue());
                        if(audioAttributes.getNamedItem("usePercentage") != null) aud.usePercentage = Boolean.parseBoolean(audioAttributes.getNamedItem("usePercentage").getNodeValue());
                        if(audioAttributes.getNamedItem("percentage") != null) aud.percentage = Integer.parseInt(audioAttributes.getNamedItem("percentage").getNodeValue());
                        
                        loadHold.audioList.add(aud);
                    }
                }
                
                //get spawn objects
                if(spawnObjects != null)
                {
                    NodeList spawnObjectsList = spawnObjects.getChildNodes();
                    for(int j = 0; j < spawnObjectsList.getLength(); j++)
                    {
                        if(spawnObjectsList.item(j).getNodeName().compareTo("SpawnObject") != 0) { continue; }
                        NamedNodeMap spawnObjectAttributes = spawnObjectsList.item(j).getAttributes();
                        
                        String filePath = "";
                        if(spawnObjectAttributes.getNamedItem("definitionFilePath") != null) filePath = createAbsolutePathFrom(spawnObjectAttributes.getNamedItem("definitionFilePath").getNodeValue(), relativeDir);
                        filePath = filePath.replace('/', File.separatorChar);
                        filePath = filePath.replace('\\', File.separatorChar);
                        SpawnObject sob = new SpawnObject(filePath);
                        if(spawnObjectAttributes.getNamedItem("delay") != null) sob.delay = Integer.parseInt(spawnObjectAttributes.getNamedItem("delay").getNodeValue());
                        if(spawnObjectAttributes.getNamedItem("number") != null) sob.number = Integer.parseInt(spawnObjectAttributes.getNamedItem("number").getNodeValue());
                        if(spawnObjectAttributes.getNamedItem("parentOffsetX") != null) sob.parentOffset.x = Float.parseFloat(spawnObjectAttributes.getNamedItem("parentOffsetX").getNodeValue());
                        if(spawnObjectAttributes.getNamedItem("parentOffsetY") != null) sob.parentOffset.y = Float.parseFloat(spawnObjectAttributes.getNamedItem("parentOffsetY").getNodeValue());
                        if(spawnObjectAttributes.getNamedItem("velocityX") != null) sob.vel.x = Float.parseFloat(spawnObjectAttributes.getNamedItem("velocityX").getNodeValue());
                        if(spawnObjectAttributes.getNamedItem("velocityY") != null) sob.vel.y = Float.parseFloat(spawnObjectAttributes.getNamedItem("velocityY").getNodeValue());
                        if(spawnObjectAttributes.getNamedItem("followParent") != null) sob.followParent = Boolean.parseBoolean(spawnObjectAttributes.getNamedItem("followParent").getNodeValue());
                        if(spawnObjectAttributes.getNamedItem("collideParent") != null) sob.collideParent = Boolean.parseBoolean(spawnObjectAttributes.getNamedItem("collideParent").getNodeValue());
                        if(spawnObjectAttributes.getNamedItem("useParentPalette") != null) sob.useParentPalette = Boolean.parseBoolean(spawnObjectAttributes.getNamedItem("useParentPalette").getNodeValue());
                        
                        loadHold.spawnObjects.add(sob);
                    }
                }
                
                if(loadHold.IsTerrainObjectHold())
                {
                    TerrainObjectHold toHold = (TerrainObjectHold)loadHold;
                    
                    //get terrain object hold attributes
                    if(holdAttributes.getNamedItem("blockability") != null) toHold.blockability = Blockability.valueOf(holdAttributes.getNamedItem("blockability").getNodeValue());
                    if(holdAttributes.getNamedItem("ownHitstop") != null) toHold.ownHitstop = Integer.parseInt(holdAttributes.getNamedItem("ownHitstop").getNodeValue());
                    if(holdAttributes.getNamedItem("victimHitstop") != null) toHold.victimHitstop = Integer.parseInt(holdAttributes.getNamedItem("victimHitstop").getNodeValue());
                    if(holdAttributes.getNamedItem("blockstun") != null) toHold.blockstun = Integer.parseInt(holdAttributes.getNamedItem("blockstun").getNodeValue());
                    if(holdAttributes.getNamedItem("changeAttackBoxAttributes") != null) toHold.changeAttackBoxAttributes = Boolean.parseBoolean(holdAttributes.getNamedItem("changeAttackBoxAttributes").getNodeValue());
                    if(holdAttributes.getNamedItem("damage") != null) toHold.damage = Integer.parseInt(holdAttributes.getNamedItem("damage").getNodeValue());
                    if(holdAttributes.getNamedItem("forceX") != null) toHold.force.x = Float.parseFloat(holdAttributes.getNamedItem("forceX").getNodeValue());
                    if(holdAttributes.getNamedItem("forceY") != null) toHold.force.y = Float.parseFloat(holdAttributes.getNamedItem("forceY").getNodeValue());
                    if(holdAttributes.getNamedItem("hitstun") != null) toHold.hitstun = Integer.parseInt(holdAttributes.getNamedItem("hitstun").getNodeValue());
                    if(holdAttributes.getNamedItem("horizontalDirectionBasedBlock") != null) toHold.horizontalDirectionBasedBlock = Boolean.parseBoolean(holdAttributes.getNamedItem("horizontalDirectionBasedBlock").getNodeValue());
                    if(holdAttributes.getNamedItem("reversedHorizontalBlock") != null) toHold.reversedHorizontalBlock = Boolean.parseBoolean(holdAttributes.getNamedItem("reversedHorizontalBlock").getNodeValue());
                    if(holdAttributes.getNamedItem("trips") != null) toHold.trips = Boolean.parseBoolean(holdAttributes.getNamedItem("trips").getNodeValue());
                    if(holdAttributes.getNamedItem("resetHits") != null) toHold.resetHits = Boolean.parseBoolean(holdAttributes.getNamedItem("resetHits").getNodeValue());
                    
                    //get attack boxes
                    if(attackBoxes != null)
                    {
                        NodeList attackBoxList = attackBoxes.getChildNodes();
                        for(int j = 0; j < attackBoxList.getLength(); j++)
                        {
                            if(attackBoxList.item(j).getNodeName().compareTo("Box") != 0) { continue; }
                            NamedNodeMap attackBoxAttributes = attackBoxList.item(j).getAttributes();
                            
                            HSBox box = new HSBox();
                            if(attackBoxAttributes.getNamedItem("width") != null) box.width = Float.parseFloat(attackBoxAttributes.getNamedItem("width").getNodeValue());
                            if(attackBoxAttributes.getNamedItem("height") != null) box.height = Float.parseFloat(attackBoxAttributes.getNamedItem("height").getNodeValue());
                            if(attackBoxAttributes.getNamedItem("offsetX") != null) box.offset.x = Float.parseFloat(attackBoxAttributes.getNamedItem("offsetX").getNodeValue());
                            if(attackBoxAttributes.getNamedItem("offsetY") != null) box.offset.y = Float.parseFloat(attackBoxAttributes.getNamedItem("offsetY").getNodeValue());
                            if(attackBoxAttributes.getNamedItem("depth") != null) box.depth = Integer.parseInt(attackBoxAttributes.getNamedItem("depth").getNodeValue());
                            
                            toHold.attackBoxes.add(box);
                        }
                    }
                    
                    //get hurt boxes
                    if(hurtBoxes != null)
                    {
                        NodeList hurtBoxList = hurtBoxes.getChildNodes();
                        for(int j = 0; j < hurtBoxList.getLength(); j++)
                        {
                            if(hurtBoxList.item(j).getNodeName().compareTo("Box") != 0) { continue; }
                            NamedNodeMap hurtBoxAttributes = hurtBoxList.item(j).getAttributes();
                            
                            HSBox box = new HSBox();
                            if(hurtBoxAttributes.getNamedItem("width") != null) box.width = Float.parseFloat(hurtBoxAttributes.getNamedItem("width").getNodeValue());
                            if(hurtBoxAttributes.getNamedItem("height") != null) box.height = Float.parseFloat(hurtBoxAttributes.getNamedItem("height").getNodeValue());
                            if(hurtBoxAttributes.getNamedItem("offsetX") != null) box.offset.x = Float.parseFloat(hurtBoxAttributes.getNamedItem("offsetX").getNodeValue());
                            if(hurtBoxAttributes.getNamedItem("offsetY") != null) box.offset.y = Float.parseFloat(hurtBoxAttributes.getNamedItem("offsetY").getNodeValue());
                            if(hurtBoxAttributes.getNamedItem("depth") != null) box.depth = Integer.parseInt(hurtBoxAttributes.getNamedItem("depth").getNodeValue());
                            
                            toHold.hurtBoxes.add(box);
                        }
                    }
                    
                    //get hit sounds
                    if(hitAudioList != null)
                    {
                        NodeList hitAudioListList = hitAudioList.getChildNodes();
                        for(int j = 0; j < hitAudioListList.getLength(); j++)
                        {
                            if(hitAudioListList.item(j).getNodeName().compareTo("HitAudio") != 0) { continue; }
                            NamedNodeMap hitAudioAttributes = hitAudioListList.item(j).getAttributes();

                            String filePath = "";
                            if(hitAudioAttributes.getNamedItem("hitAudioFilePath") != null) filePath = createAbsolutePathFrom(hitAudioAttributes.getNamedItem("hitAudioFilePath").getNodeValue(), relativeDir);
                            filePath = filePath.replace('/', File.separatorChar);
                            filePath = filePath.replace('\\', File.separatorChar);
                            HSAudio aud = new HSAudio(filePath);
                            if(hitAudioAttributes.getNamedItem("delay") != null) aud.delay = Integer.parseInt(hitAudioAttributes.getNamedItem("delay").getNodeValue());
                            if(hitAudioAttributes.getNamedItem("exclusive") != null) aud.exclusive = Boolean.parseBoolean(hitAudioAttributes.getNamedItem("exclusive").getNodeValue());
                            if(hitAudioAttributes.getNamedItem("usePercentage") != null) aud.usePercentage = Boolean.parseBoolean(hitAudioAttributes.getNamedItem("usePercentage").getNodeValue());
                            if(hitAudioAttributes.getNamedItem("percentage") != null) aud.percentage = Integer.parseInt(hitAudioAttributes.getNamedItem("percentage").getNodeValue());

                            toHold.hitAudioList.add(aud);
                        }
                    }
                    
                    //get blocked sounds
                    if(blockedAudioList != null)
                    {
                        NodeList blockedAudioListList = blockedAudioList.getChildNodes();
                        for(int j = 0; j < blockedAudioListList.getLength(); j++)
                        {
                            if(blockedAudioListList.item(j).getNodeName().compareTo("BlockedAudio") != 0) { continue; }
                            NamedNodeMap blockedAudioAttributes = blockedAudioListList.item(j).getAttributes();

                            String filePath = "";
                            if(blockedAudioAttributes.getNamedItem("blockedAudioFilePath") != null) filePath = createAbsolutePathFrom(blockedAudioAttributes.getNamedItem("blockedAudioFilePath").getNodeValue(), relativeDir);
                            filePath = filePath.replace('/', File.separatorChar);
                            filePath = filePath.replace('\\', File.separatorChar);
                            HSAudio aud = new HSAudio(filePath);
                            if(blockedAudioAttributes.getNamedItem("delay") != null) aud.delay = Integer.parseInt(blockedAudioAttributes.getNamedItem("delay").getNodeValue());
                            if(blockedAudioAttributes.getNamedItem("exclusive") != null) aud.exclusive = Boolean.parseBoolean(blockedAudioAttributes.getNamedItem("exclusive").getNodeValue());
                            if(blockedAudioAttributes.getNamedItem("usePercentage") != null) aud.usePercentage = Boolean.parseBoolean(blockedAudioAttributes.getNamedItem("usePercentage").getNodeValue());
                            if(blockedAudioAttributes.getNamedItem("percentage") != null) aud.percentage = Integer.parseInt(blockedAudioAttributes.getNamedItem("percentage").getNodeValue());

                            toHold.blockedAudioList.add(aud);
                        }
                    }
                }
                
                if(loadHold.IsPhysicsObjectHold())
                {
                    PhysicsObjectHold poHold = (PhysicsObjectHold)loadHold;
                    if(holdAttributes.getNamedItem("changePhysicsAttributes") != null) poHold.changePhysics = Boolean.parseBoolean(holdAttributes.getNamedItem("changePhysicsAttributes").getNodeValue());
                    if(holdAttributes.getNamedItem("ignoreGravity") != null) poHold.ignoreGravity = Boolean.parseBoolean(holdAttributes.getNamedItem("ignoreGravity").getNodeValue());
                }
                
                if(loadHold.IsFighterHold())
                {
                    FighterHold fHold = (FighterHold)loadHold;
                    
                    if(holdAttributes.getNamedItem("changeCancels") != null) fHold.changeCancels = Boolean.parseBoolean(holdAttributes.getNamedItem("changeCancels").getNodeValue());
                    if(holdAttributes.getNamedItem("dashCancel") != null) fHold.cancels.dash = Cancel.valueOf(holdAttributes.getNamedItem("dashCancel").getNodeValue());
                    if(holdAttributes.getNamedItem("jumpCancel") != null) fHold.cancels.jump = Cancel.valueOf(holdAttributes.getNamedItem("jumpCancel").getNodeValue());
                    if(holdAttributes.getNamedItem("lightNeutralCancel") != null) fHold.cancels.lightNeutral = Cancel.valueOf(holdAttributes.getNamedItem("lightNeutralCancel").getNodeValue());
                    if(holdAttributes.getNamedItem("lightForwardCancel") != null) fHold.cancels.lightForward = Cancel.valueOf(holdAttributes.getNamedItem("lightForwardCancel").getNodeValue());
                    if(holdAttributes.getNamedItem("lightUpCancel") != null) fHold.cancels.lightUp = Cancel.valueOf(holdAttributes.getNamedItem("lightUpCancel").getNodeValue());
                    if(holdAttributes.getNamedItem("lightDownCancel") != null) fHold.cancels.lightDown = Cancel.valueOf(holdAttributes.getNamedItem("lightDownCancel").getNodeValue());
                    if(holdAttributes.getNamedItem("lightBackwardCancel") != null) fHold.cancels.lightBackward = Cancel.valueOf(holdAttributes.getNamedItem("lightBackwardCancel").getNodeValue());
                    if(holdAttributes.getNamedItem("lightQCFCancel") != null) fHold.cancels.lightQCF = Cancel.valueOf(holdAttributes.getNamedItem("lightQCFCancel").getNodeValue());
                    if(holdAttributes.getNamedItem("heavyNeutralCancel") != null) fHold.cancels.heavyNeutral = Cancel.valueOf(holdAttributes.getNamedItem("heavyNeutralCancel").getNodeValue());
                    if(holdAttributes.getNamedItem("heavyForwardCancel") != null) fHold.cancels.heavyForward = Cancel.valueOf(holdAttributes.getNamedItem("heavyForwardCancel").getNodeValue());
                    if(holdAttributes.getNamedItem("heavyUpCancel") != null) fHold.cancels.heavyUp = Cancel.valueOf(holdAttributes.getNamedItem("heavyUpCancel").getNodeValue());
                    if(holdAttributes.getNamedItem("heavyDownCancel") != null) fHold.cancels.heavyDown = Cancel.valueOf(holdAttributes.getNamedItem("heavyDownCancel").getNodeValue());
                    if(holdAttributes.getNamedItem("heavyBackwardCancel") != null) fHold.cancels.heavyBackward = Cancel.valueOf(holdAttributes.getNamedItem("heavyBackwardCancel").getNodeValue());
                    if(holdAttributes.getNamedItem("heavyQCFCancel") != null) fHold.cancels.heavyQCF = Cancel.valueOf(holdAttributes.getNamedItem("heavyQCFCancel").getNodeValue());
                }
                
                loadObject.holds.add(loadHold);
            }
            
            //get attributes
            NamedNodeMap objectAttributes = object.getAttributes();
            
            //get hsobject attributes
            if(objectAttributes.getNamedItem("name") != null) loadObject.name = objectAttributes.getNamedItem("name").getNodeValue();
            if(objectAttributes.getNamedItem("lifetime") != null) loadObject.lifetime = Integer.parseInt(objectAttributes.getNamedItem("lifetime").getNodeValue());
            
            //Legacy palette loading
            for(int i=1;;i++) {
            	if(objectAttributes.getNamedItem("palette" + i + "FilePath") == null) {
            		break;
            	}
            	HSPalette pal = new HSPalette();
            	pal.path = createAbsolutePathFrom(objectAttributes.getNamedItem("palette" + i + "FilePath").getNodeValue(), relativeDir);
            	pal.path = pal.path.replace('/', File.separatorChar);
            	pal.path = pal.path.replace('\\', File.separatorChar);
                pal.name = "Palette " + i;
                pal.id = i;
            	loadObject.palettes.add(pal);
            }
            
            //New palette loading
            if(palettes != null) {
	            NodeList palettesList = palettes.getChildNodes();
	            for(int i = 0; i < palettesList.getLength(); i++)
	            {
	                Node palette = palettesList.item(i);
	                
	                HSPalette pal = new HSPalette();
	            	pal.path = createAbsolutePathFrom(palette.getAttributes().getNamedItem("path").getNodeValue(), relativeDir);
	            	pal.path = pal.path.replace('/', File.separatorChar);
	            	pal.path = pal.path.replace('\\', File.separatorChar);
	                if(palette.getAttributes().getNamedItem("name") != null) pal.name = palette.getAttributes().getNamedItem("name").getNodeValue();
	                else pal.name = "";
	                if(palette.getAttributes().getNamedItem("id") != null) pal.id = Integer.parseInt(palette.getAttributes().getNamedItem("id").getNodeValue());
	                else pal.id = i;
	                
	            	loadObject.palettes.add(pal);
	            }
            }
            else {
            	JOptionPane.showMessageDialog(this, "Using legacy palette loading, advise saving again to convert to new palette format", "Legacy Palette Loading", JOptionPane.WARNING_MESSAGE);
            }
            
            //get hs object event holds
            if(eventHoldsAttributes.getNamedItem("lifetimeDeath") != null)
                loadObject.hsObjectEventHolds.lifetimeDeath = getHoldFromId(loadObject, Integer.parseInt(eventHoldsAttributes.getNamedItem("lifetimeDeath").getNodeValue()));
            
            if(loadObject.IsTerrainObject())
            {
                TerrainObject tObject = (TerrainObject)loadObject;
                
                //get terrain object attributes
                if(objectAttributes.getNamedItem("bounce") != null) tObject.bounce = Float.parseFloat(objectAttributes.getNamedItem("bounce").getNodeValue());
                if(objectAttributes.getNamedItem("canBeJumpedThrough") != null) tObject.canBeJumpedThrough = Boolean.parseBoolean(objectAttributes.getNamedItem("canBeJumpedThrough").getNodeValue());
                if(objectAttributes.getNamedItem("friction") != null) tObject.friction = Float.parseFloat(objectAttributes.getNamedItem("friction").getNodeValue());
                if(objectAttributes.getNamedItem("health") != null) tObject.health = Integer.parseInt(objectAttributes.getNamedItem("health").getNodeValue());
                if(objectAttributes.getNamedItem("takesTerrainDamage") != null) tObject.takesTerrainDamage = Boolean.parseBoolean(objectAttributes.getNamedItem("takesTerrainDamage").getNodeValue());
                if(objectAttributes.getNamedItem("fragile") != null) tObject.fragile = Boolean.parseBoolean(objectAttributes.getNamedItem("fragile").getNodeValue());
                
                if(version < 2) {
	                //get terrain box (legacy)
	                for(NamedNodeMap attr : terrainBoxAttributes) {
		                HSBox terrainBox = new HSBox();
		                if(attr.getNamedItem("width") != null) terrainBox.width = Float.parseFloat(attr.getNamedItem("width").getNodeValue());
		                if(attr.getNamedItem("height") != null) terrainBox.height = Float.parseFloat(attr.getNamedItem("height").getNodeValue());
		                if(attr.getNamedItem("offsetX") != null) terrainBox.offset.x = Float.parseFloat(attr.getNamedItem("offsetX").getNodeValue());
		                if(attr.getNamedItem("offsetY") != null) terrainBox.offset.y = Float.parseFloat(attr.getNamedItem("offsetY").getNodeValue());
		                if(attr.getNamedItem("depth") != null) terrainBox.depth = Integer.parseInt(attr.getNamedItem("depth").getNodeValue());
		                tObject.terrainBoxes.add(terrainBox);
		                
		                //Only allow terrain objects to have more than one
		                if(loadObject.IsPhysicsObject() || loadObject.IsFighter()) break;
	                }
                }
                else {
                	//After change
	                //get terrain boxes
    	            NodeList terrainBoxNodeList = terrainBoxes.getChildNodes();
    	            for(int i = 0; i < terrainBoxNodeList.getLength(); i++) {
    	            	Node tBox = terrainBoxNodeList.item(i);
    	            	NamedNodeMap attr = tBox.getAttributes();
		                HSBox terrainBox = new HSBox();
		                if(attr.getNamedItem("width") != null) terrainBox.width = Float.parseFloat(attr.getNamedItem("width").getNodeValue());
		                if(attr.getNamedItem("height") != null) terrainBox.height = Float.parseFloat(attr.getNamedItem("height").getNodeValue());
		                if(attr.getNamedItem("offsetX") != null) terrainBox.offset.x = Float.parseFloat(attr.getNamedItem("offsetX").getNodeValue());
		                if(attr.getNamedItem("offsetY") != null) terrainBox.offset.y = Float.parseFloat(attr.getNamedItem("offsetY").getNodeValue());
		                if(attr.getNamedItem("depth") != null) terrainBox.depth = Integer.parseInt(attr.getNamedItem("depth").getNodeValue());
		                tObject.terrainBoxes.add(terrainBox);
		                
		                //Only allow terrain objects to have more than one
		                if(loadObject.IsPhysicsObject() || loadObject.IsFighter()) break;
	                }
                }
                
                //get terrain object event holds
                if(eventHoldsAttributes.getNamedItem("healthDeath") != null)
                    tObject.terrainEventHolds.healthDeath = (TerrainObjectHold)getHoldFromId(loadObject, Integer.parseInt(eventHoldsAttributes.getNamedItem("healthDeath").getNodeValue()));
            }
            
            if(loadObject.IsPhysicsObject())
            {
                PhysicsObject pObject = (PhysicsObject)loadObject;
                
                //get physics object attributes
                if(objectAttributes.getNamedItem("falls") != null) pObject.falls = Boolean.parseBoolean(objectAttributes.getNamedItem("falls").getNodeValue());
                if(objectAttributes.getNamedItem("mass") != null) pObject.mass = Float.parseFloat(objectAttributes.getNamedItem("mass").getNodeValue());
                if(objectAttributes.getNamedItem("maxFallSpeed") != null) pObject.maxFallSpeed = Float.parseFloat(objectAttributes.getNamedItem("maxFallSpeed").getNodeValue());
            }
            
            if(loadObject.IsFighter())
            {
                FighterObject fighter = (FighterObject)loadObject;
                
                //get fighter attributes
                if(objectAttributes.getNamedItem("airActions") != null) fighter.airActions = Integer.parseInt(objectAttributes.getNamedItem("airActions").getNodeValue());
                if(objectAttributes.getNamedItem("airControlAccel") != null) fighter.airControlAccel = Float.parseFloat(objectAttributes.getNamedItem("airControlAccel").getNodeValue());
                if(objectAttributes.getNamedItem("backwardAirDashDuration") != null) fighter.backwardAirDashDuration = Integer.parseInt(objectAttributes.getNamedItem("backwardAirDashDuration").getNodeValue());
                if(objectAttributes.getNamedItem("backwardAirDashSpeed") != null) fighter.backwardAirDashSpeed = Float.parseFloat(objectAttributes.getNamedItem("backwardAirDashSpeed").getNodeValue());
                if(objectAttributes.getNamedItem("forwardAirDashDuration") != null) fighter.forwardAirDashDuration = Integer.parseInt(objectAttributes.getNamedItem("forwardAirDashDuration").getNodeValue());
                if(objectAttributes.getNamedItem("forwardAirDashSpeed") != null) fighter.forwardAirDashSpeed = Float.parseFloat(objectAttributes.getNamedItem("forwardAirDashSpeed").getNodeValue());
                if(objectAttributes.getNamedItem("jumpSpeed") != null) fighter.jumpSpeed = Float.parseFloat(objectAttributes.getNamedItem("jumpSpeed").getNodeValue());
                if(objectAttributes.getNamedItem("maxAirControlSpeed") != null) fighter.maxAirControlSpeed = Float.parseFloat(objectAttributes.getNamedItem("maxAirControlSpeed").getNodeValue());
                if(objectAttributes.getNamedItem("runSpeed") != null) fighter.runSpeed = Float.parseFloat(objectAttributes.getNamedItem("runSpeed").getNodeValue());
                if(objectAttributes.getNamedItem("stepHeight") != null) fighter.stepHeight = Float.parseFloat(objectAttributes.getNamedItem("stepHeight").getNodeValue());
                if(objectAttributes.getNamedItem("walkSpeed") != null) fighter.walkSpeed = Float.parseFloat(objectAttributes.getNamedItem("walkSpeed").getNodeValue());
                
                //get upright terrain box
                HSBox uprightTerrainBox = new HSBox();
                if(uprightTerrainBoxAttributes.getNamedItem("width") != null) uprightTerrainBox.width = Float.parseFloat(uprightTerrainBoxAttributes.getNamedItem("width").getNodeValue());
                if(uprightTerrainBoxAttributes.getNamedItem("height") != null) uprightTerrainBox.height = Float.parseFloat(uprightTerrainBoxAttributes.getNamedItem("height").getNodeValue());
                if(uprightTerrainBoxAttributes.getNamedItem("offsetX") != null) uprightTerrainBox.offset.x = Float.parseFloat(uprightTerrainBoxAttributes.getNamedItem("offsetX").getNodeValue());
                if(uprightTerrainBoxAttributes.getNamedItem("offsetY") != null) uprightTerrainBox.offset.y = Float.parseFloat(uprightTerrainBoxAttributes.getNamedItem("offsetY").getNodeValue());
                if(uprightTerrainBoxAttributes.getNamedItem("depth") != null) uprightTerrainBox.depth = Integer.parseInt(uprightTerrainBoxAttributes.getNamedItem("depth").getNodeValue());
                fighter.uprightTerrainBoxes.add(uprightTerrainBox);
                
                //get crouching terrain box
                HSBox crouchingTerrainBox = new HSBox();
                if(crouchingTerrainBoxAttributes.getNamedItem("width") != null) crouchingTerrainBox.width = Float.parseFloat(crouchingTerrainBoxAttributes.getNamedItem("width").getNodeValue());
                if(crouchingTerrainBoxAttributes.getNamedItem("height") != null) crouchingTerrainBox.height = Float.parseFloat(crouchingTerrainBoxAttributes.getNamedItem("height").getNodeValue());
                if(crouchingTerrainBoxAttributes.getNamedItem("offsetX") != null) crouchingTerrainBox.offset.x = Float.parseFloat(crouchingTerrainBoxAttributes.getNamedItem("offsetX").getNodeValue());
                if(crouchingTerrainBoxAttributes.getNamedItem("offsetY") != null) crouchingTerrainBox.offset.y = Float.parseFloat(crouchingTerrainBoxAttributes.getNamedItem("offsetY").getNodeValue());
                if(crouchingTerrainBoxAttributes.getNamedItem("depth") != null) crouchingTerrainBox.depth = Integer.parseInt(crouchingTerrainBoxAttributes.getNamedItem("depth").getNodeValue());
                fighter.crouchingTerrainBoxes.add(crouchingTerrainBox);
                
                //get prone terrain box
                HSBox proneTerrainBox = new HSBox();
                if(proneTerrainBoxAttributes.getNamedItem("width") != null) proneTerrainBox.width = Float.parseFloat(proneTerrainBoxAttributes.getNamedItem("width").getNodeValue());
                if(proneTerrainBoxAttributes.getNamedItem("height") != null) proneTerrainBox.height = Float.parseFloat(proneTerrainBoxAttributes.getNamedItem("height").getNodeValue());
                if(proneTerrainBoxAttributes.getNamedItem("offsetX") != null) proneTerrainBox.offset.x = Float.parseFloat(proneTerrainBoxAttributes.getNamedItem("offsetX").getNodeValue());
                if(proneTerrainBoxAttributes.getNamedItem("offsetY") != null) proneTerrainBox.offset.y = Float.parseFloat(proneTerrainBoxAttributes.getNamedItem("offsetY").getNodeValue());
                if(proneTerrainBoxAttributes.getNamedItem("depth") != null) proneTerrainBox.depth = Integer.parseInt(proneTerrainBoxAttributes.getNamedItem("depth").getNodeValue());
                fighter.proneTerrainBoxes.add(proneTerrainBox);
                
                //get compact terrain box
                HSBox compactTerrainBox = new HSBox();
                if(compactTerrainBoxAttributes.getNamedItem("width") != null) compactTerrainBox.width = Float.parseFloat(compactTerrainBoxAttributes.getNamedItem("width").getNodeValue());
                if(compactTerrainBoxAttributes.getNamedItem("height") != null) compactTerrainBox.height = Float.parseFloat(compactTerrainBoxAttributes.getNamedItem("height").getNodeValue());
                if(compactTerrainBoxAttributes.getNamedItem("offsetX") != null) compactTerrainBox.offset.x = Float.parseFloat(compactTerrainBoxAttributes.getNamedItem("offsetX").getNodeValue());
                if(compactTerrainBoxAttributes.getNamedItem("offsetY") != null) compactTerrainBox.offset.y = Float.parseFloat(compactTerrainBoxAttributes.getNamedItem("offsetY").getNodeValue());
                if(compactTerrainBoxAttributes.getNamedItem("depth") != null) compactTerrainBox.depth = Integer.parseInt(compactTerrainBoxAttributes.getNamedItem("depth").getNodeValue());
                fighter.compactTerrainBoxes.add(compactTerrainBox);
                
                //get fighter event holds
                if(eventHoldsAttributes.getNamedItem("standing") != null)
                    fighter.fighterEventHolds.standing = (FighterHold)getHoldFromId(loadObject, Integer.parseInt(eventHoldsAttributes.getNamedItem("standing").getNodeValue()));
                if(eventHoldsAttributes.getNamedItem("turn") != null)
                    fighter.fighterEventHolds.turn = (FighterHold)getHoldFromId(loadObject, Integer.parseInt(eventHoldsAttributes.getNamedItem("turn").getNodeValue()));
                if(eventHoldsAttributes.getNamedItem("walk") != null)
                    fighter.fighterEventHolds.walk = (FighterHold)getHoldFromId(loadObject, Integer.parseInt(eventHoldsAttributes.getNamedItem("walk").getNodeValue()));
                if(eventHoldsAttributes.getNamedItem("walking") != null)
                    fighter.fighterEventHolds.walking = (FighterHold)getHoldFromId(loadObject, Integer.parseInt(eventHoldsAttributes.getNamedItem("walking").getNodeValue()));
                if(eventHoldsAttributes.getNamedItem("walkingTurn") != null)
                    fighter.fighterEventHolds.walkingTurn = (FighterHold)getHoldFromId(loadObject, Integer.parseInt(eventHoldsAttributes.getNamedItem("walkingTurn").getNodeValue()));
                if(eventHoldsAttributes.getNamedItem("run") != null)
                    fighter.fighterEventHolds.run = (FighterHold)getHoldFromId(loadObject, Integer.parseInt(eventHoldsAttributes.getNamedItem("run").getNodeValue()));
                if(eventHoldsAttributes.getNamedItem("running") != null)
                    fighter.fighterEventHolds.running = (FighterHold)getHoldFromId(loadObject, Integer.parseInt(eventHoldsAttributes.getNamedItem("running").getNodeValue()));
                if(eventHoldsAttributes.getNamedItem("runningTurn") != null)
                    fighter.fighterEventHolds.runningTurn = (FighterHold)getHoldFromId(loadObject, Integer.parseInt(eventHoldsAttributes.getNamedItem("runningTurn").getNodeValue()));
                if(eventHoldsAttributes.getNamedItem("runningStop") != null)
                    fighter.fighterEventHolds.runningStop = (FighterHold)getHoldFromId(loadObject, Integer.parseInt(eventHoldsAttributes.getNamedItem("runningStop").getNodeValue()));
                if(eventHoldsAttributes.getNamedItem("crouch") != null)
                    fighter.fighterEventHolds.crouch = (FighterHold)getHoldFromId(loadObject, Integer.parseInt(eventHoldsAttributes.getNamedItem("crouch").getNodeValue()));
                if(eventHoldsAttributes.getNamedItem("crouching") != null)
                    fighter.fighterEventHolds.crouching = (FighterHold)getHoldFromId(loadObject, Integer.parseInt(eventHoldsAttributes.getNamedItem("crouching").getNodeValue()));
                if(eventHoldsAttributes.getNamedItem("crouchingTurn") != null)
                    fighter.fighterEventHolds.crouchingTurn = (FighterHold)getHoldFromId(loadObject, Integer.parseInt(eventHoldsAttributes.getNamedItem("crouchingTurn").getNodeValue()));
                if(eventHoldsAttributes.getNamedItem("stand") != null)
                    fighter.fighterEventHolds.stand = (FighterHold)getHoldFromId(loadObject, Integer.parseInt(eventHoldsAttributes.getNamedItem("stand").getNodeValue()));
                if(eventHoldsAttributes.getNamedItem("jumpNeutralStart") != null)
                    fighter.fighterEventHolds.jumpNeutralStart = (FighterHold)getHoldFromId(loadObject, Integer.parseInt(eventHoldsAttributes.getNamedItem("jumpNeutralStart").getNodeValue()));
                if(eventHoldsAttributes.getNamedItem("jumpNeutralStartAir") != null)
                    fighter.fighterEventHolds.jumpNeutralStartAir = (FighterHold)getHoldFromId(loadObject, Integer.parseInt(eventHoldsAttributes.getNamedItem("jumpNeutralStartAir").getNodeValue()));
                if(eventHoldsAttributes.getNamedItem("jumpNeutralRising") != null)
                    fighter.fighterEventHolds.jumpNeutralRising = (FighterHold)getHoldFromId(loadObject, Integer.parseInt(eventHoldsAttributes.getNamedItem("jumpNeutralRising").getNodeValue()));
                if(eventHoldsAttributes.getNamedItem("jumpNeutralFall") != null)
                    fighter.fighterEventHolds.jumpNeutralFall = (FighterHold)getHoldFromId(loadObject, Integer.parseInt(eventHoldsAttributes.getNamedItem("jumpNeutralFall").getNodeValue()));
                if(eventHoldsAttributes.getNamedItem("jumpNeutralFalling") != null)
                    fighter.fighterEventHolds.jumpNeutralFalling = (FighterHold)getHoldFromId(loadObject, Integer.parseInt(eventHoldsAttributes.getNamedItem("jumpNeutralFalling").getNodeValue()));
                if(eventHoldsAttributes.getNamedItem("jumpNeutralLand") != null)
                    fighter.fighterEventHolds.jumpNeutralLand = (FighterHold)getHoldFromId(loadObject, Integer.parseInt(eventHoldsAttributes.getNamedItem("jumpNeutralLand").getNodeValue()));
                if(eventHoldsAttributes.getNamedItem("jumpBackwardRising") != null)
                    fighter.fighterEventHolds.jumpBackwardRising = (FighterHold)getHoldFromId(loadObject, Integer.parseInt(eventHoldsAttributes.getNamedItem("jumpBackwardRising").getNodeValue()));
                if(eventHoldsAttributes.getNamedItem("jumpBackwardFall") != null)
                    fighter.fighterEventHolds.jumpBackwardFall = (FighterHold)getHoldFromId(loadObject, Integer.parseInt(eventHoldsAttributes.getNamedItem("jumpBackwardFall").getNodeValue()));
                if(eventHoldsAttributes.getNamedItem("airDashForward") != null)
                    fighter.fighterEventHolds.airDashForward = (FighterHold)getHoldFromId(loadObject, Integer.parseInt(eventHoldsAttributes.getNamedItem("airDashForward").getNodeValue()));
                if(eventHoldsAttributes.getNamedItem("airDashBackward") != null)
                    fighter.fighterEventHolds.airDashBackward = (FighterHold)getHoldFromId(loadObject, Integer.parseInt(eventHoldsAttributes.getNamedItem("airDashBackward").getNodeValue()));
                if(eventHoldsAttributes.getNamedItem("blockHigh") != null)
                    fighter.fighterEventHolds.blockHigh = (FighterHold)getHoldFromId(loadObject, Integer.parseInt(eventHoldsAttributes.getNamedItem("blockHigh").getNodeValue()));
                if(eventHoldsAttributes.getNamedItem("blockLow") != null)
                    fighter.fighterEventHolds.blockLow = (FighterHold)getHoldFromId(loadObject, Integer.parseInt(eventHoldsAttributes.getNamedItem("blockLow").getNodeValue()));
                if(eventHoldsAttributes.getNamedItem("blockAir") != null)
                    fighter.fighterEventHolds.blockAir = (FighterHold)getHoldFromId(loadObject, Integer.parseInt(eventHoldsAttributes.getNamedItem("blockAir").getNodeValue()));
                if(eventHoldsAttributes.getNamedItem("hitstunLightHighStanding") != null)
                    fighter.fighterEventHolds.hitstunLightHighStanding = (FighterHold)getHoldFromId(loadObject, Integer.parseInt(eventHoldsAttributes.getNamedItem("hitstunLightHighStanding").getNodeValue()));
                if(eventHoldsAttributes.getNamedItem("hitstunLightMidStanding") != null)
                    fighter.fighterEventHolds.hitstunLightMidStanding = (FighterHold)getHoldFromId(loadObject, Integer.parseInt(eventHoldsAttributes.getNamedItem("hitstunLightMidStanding").getNodeValue()));
                if(eventHoldsAttributes.getNamedItem("hitstunLightLowStanding") != null)
                    fighter.fighterEventHolds.hitstunLightLowStanding = (FighterHold)getHoldFromId(loadObject, Integer.parseInt(eventHoldsAttributes.getNamedItem("hitstunLightLowStanding").getNodeValue()));
                if(eventHoldsAttributes.getNamedItem("hitstunLightMidCrouching") != null)
                    fighter.fighterEventHolds.hitstunLightMidCrouching = (FighterHold)getHoldFromId(loadObject, Integer.parseInt(eventHoldsAttributes.getNamedItem("hitstunLightMidCrouching").getNodeValue()));
                if(eventHoldsAttributes.getNamedItem("hitstunLightLowCrouching") != null)
                    fighter.fighterEventHolds.hitstunLightLowCrouching = (FighterHold)getHoldFromId(loadObject, Integer.parseInt(eventHoldsAttributes.getNamedItem("hitstunLightLowCrouching").getNodeValue()));
                if(eventHoldsAttributes.getNamedItem("hitstunLightAir") != null)
                    fighter.fighterEventHolds.hitstunLightAir = (FighterHold)getHoldFromId(loadObject, Integer.parseInt(eventHoldsAttributes.getNamedItem("hitstunLightAir").getNodeValue()));
                if(eventHoldsAttributes.getNamedItem("attackLightNeutralGround") != null)
                    fighter.fighterEventHolds.attackLightNeutralGround = (FighterHold)getHoldFromId(loadObject, Integer.parseInt(eventHoldsAttributes.getNamedItem("attackLightNeutralGround").getNodeValue()));
                if(eventHoldsAttributes.getNamedItem("attackLightDownGround") != null)
                    fighter.fighterEventHolds.attackLightDownGround = (FighterHold)getHoldFromId(loadObject, Integer.parseInt(eventHoldsAttributes.getNamedItem("attackLightDownGround").getNodeValue()));
                if(eventHoldsAttributes.getNamedItem("attackLightUpGround") != null)
                    fighter.fighterEventHolds.attackLightUpGround = (FighterHold)getHoldFromId(loadObject, Integer.parseInt(eventHoldsAttributes.getNamedItem("attackLightUpGround").getNodeValue()));
                if(eventHoldsAttributes.getNamedItem("attackLightForwardGround") != null)
                    fighter.fighterEventHolds.attackLightForwardGround = (FighterHold)getHoldFromId(loadObject, Integer.parseInt(eventHoldsAttributes.getNamedItem("attackLightForwardGround").getNodeValue()));
                if(eventHoldsAttributes.getNamedItem("attackLightQCFGround") != null)
                    fighter.fighterEventHolds.attackLightQCFGround = (FighterHold)getHoldFromId(loadObject, Integer.parseInt(eventHoldsAttributes.getNamedItem("attackLightQCFGround").getNodeValue()));
                if(eventHoldsAttributes.getNamedItem("attackLightNeutralAir") != null)
                    fighter.fighterEventHolds.attackLightNeutralAir = (FighterHold)getHoldFromId(loadObject, Integer.parseInt(eventHoldsAttributes.getNamedItem("attackLightNeutralAir").getNodeValue()));
                if(eventHoldsAttributes.getNamedItem("attackLightDownAir") != null)
                    fighter.fighterEventHolds.attackLightDownAir = (FighterHold)getHoldFromId(loadObject, Integer.parseInt(eventHoldsAttributes.getNamedItem("attackLightDownAir").getNodeValue()));
                if(eventHoldsAttributes.getNamedItem("attackLightUpAir") != null)
                    fighter.fighterEventHolds.attackLightUpAir = (FighterHold)getHoldFromId(loadObject, Integer.parseInt(eventHoldsAttributes.getNamedItem("attackLightUpAir").getNodeValue()));
                if(eventHoldsAttributes.getNamedItem("attackLightForwardAir") != null)
                    fighter.fighterEventHolds.attackLightForwardAir = (FighterHold)getHoldFromId(loadObject, Integer.parseInt(eventHoldsAttributes.getNamedItem("attackLightForwardAir").getNodeValue()));
                if(eventHoldsAttributes.getNamedItem("attackLightBackwardAir") != null)
                    fighter.fighterEventHolds.attackLightBackwardAir = (FighterHold)getHoldFromId(loadObject, Integer.parseInt(eventHoldsAttributes.getNamedItem("attackLightBackwardAir").getNodeValue()));
                if(eventHoldsAttributes.getNamedItem("attackLightQCFAir") != null)
                    fighter.fighterEventHolds.attackLightQCFAir = (FighterHold)getHoldFromId(loadObject, Integer.parseInt(eventHoldsAttributes.getNamedItem("attackLightQCFAir").getNodeValue()));
                
                if(eventHoldsAttributes.getNamedItem("attackHeavyNeutralGround") != null)
                    fighter.fighterEventHolds.attackHeavyNeutralGround = (FighterHold)getHoldFromId(loadObject, Integer.parseInt(eventHoldsAttributes.getNamedItem("attackHeavyNeutralGround").getNodeValue()));
                if(eventHoldsAttributes.getNamedItem("attackHeavyDownGround") != null)
                    fighter.fighterEventHolds.attackHeavyDownGround = (FighterHold)getHoldFromId(loadObject, Integer.parseInt(eventHoldsAttributes.getNamedItem("attackHeavyDownGround").getNodeValue()));
                if(eventHoldsAttributes.getNamedItem("attackHeavyUpGround") != null)
                    fighter.fighterEventHolds.attackHeavyUpGround = (FighterHold)getHoldFromId(loadObject, Integer.parseInt(eventHoldsAttributes.getNamedItem("attackHeavyUpGround").getNodeValue()));
                if(eventHoldsAttributes.getNamedItem("attackHeavyForwardGround") != null)
                    fighter.fighterEventHolds.attackHeavyForwardGround = (FighterHold)getHoldFromId(loadObject, Integer.parseInt(eventHoldsAttributes.getNamedItem("attackHeavyForwardGround").getNodeValue()));
                if(eventHoldsAttributes.getNamedItem("attackHeavyQCFGround") != null)
                    fighter.fighterEventHolds.attackHeavyQCFGround = (FighterHold)getHoldFromId(loadObject, Integer.parseInt(eventHoldsAttributes.getNamedItem("attackHeavyQCFGround").getNodeValue()));
                if(eventHoldsAttributes.getNamedItem("attackHeavyNeutralAir") != null)
                    fighter.fighterEventHolds.attackHeavyNeutralAir = (FighterHold)getHoldFromId(loadObject, Integer.parseInt(eventHoldsAttributes.getNamedItem("attackHeavyNeutralAir").getNodeValue()));
                if(eventHoldsAttributes.getNamedItem("attackHeavyDownAir") != null)
                    fighter.fighterEventHolds.attackHeavyDownAir = (FighterHold)getHoldFromId(loadObject, Integer.parseInt(eventHoldsAttributes.getNamedItem("attackHeavyDownAir").getNodeValue()));
                if(eventHoldsAttributes.getNamedItem("attackHeavyUpAir") != null)
                    fighter.fighterEventHolds.attackHeavyUpAir = (FighterHold)getHoldFromId(loadObject, Integer.parseInt(eventHoldsAttributes.getNamedItem("attackHeavyUpAir").getNodeValue()));
                if(eventHoldsAttributes.getNamedItem("attackHeavyForwardAir") != null)
                    fighter.fighterEventHolds.attackHeavyForwardAir = (FighterHold)getHoldFromId(loadObject, Integer.parseInt(eventHoldsAttributes.getNamedItem("attackHeavyForwardAir").getNodeValue()));
                if(eventHoldsAttributes.getNamedItem("attackHeavyBackwardAir") != null)
                    fighter.fighterEventHolds.attackHeavyBackwardAir = (FighterHold)getHoldFromId(loadObject, Integer.parseInt(eventHoldsAttributes.getNamedItem("attackHeavyBackwardAir").getNodeValue()));
                if(eventHoldsAttributes.getNamedItem("attackHeavyQCFAir") != null)
                    fighter.fighterEventHolds.attackHeavyQCFAir = (FighterHold)getHoldFromId(loadObject, Integer.parseInt(eventHoldsAttributes.getNamedItem("attackHeavyQCFAir").getNodeValue()));
                
                if(eventHoldsAttributes.getNamedItem("knockout") != null)
                    fighter.fighterEventHolds.knockout = (FighterHold)getHoldFromId(loadObject, Integer.parseInt(eventHoldsAttributes.getNamedItem("knockout").getNodeValue()));
                
            }
            
            //finally, get next holds
            for(HSObjectHold hold : loadObject.holds)
            {
                if(hold.nextHoldId != 0) hold.nextHold = getHoldFromId(loadObject, hold.nextHoldId);
            }
            
            setCurrentlyLoadedObject(loadObject);
            newObject();
            
            updatePalettesMenu(true);
        }
        catch(ParserConfigurationException e)
        {
        	JOptionPane.showMessageDialog(this, e.getMessage(), "Parser Configuration Exception", JOptionPane.ERROR_MESSAGE);  
        }
        catch(SAXException e)
        {
        	JOptionPane.showMessageDialog(this, e.getMessage(), "SAX Exception", JOptionPane.ERROR_MESSAGE);              
        }
        catch(IOException e)
        {
        	JOptionPane.showMessageDialog(this, e.getMessage(), "IO Exception", JOptionPane.ERROR_MESSAGE);              
        }
    }
    
    private void createDefinitionFile()
    {
        if(currentlyLoadedObject == null) { return; }
        
        //TODO: cry and complain if the path we're trying to save is outside of the game .exe location
        
        try
        {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();
            
            Element root = doc.createElement("HSObjects");
            root.setAttribute("version", "" + XML_FORMAT_VERSION);
            
            //set the proper object type
            Element object;
            if(currentlyLoadedObject.IsFighter())
            {
                object = doc.createElement("Fighter");
            }
            else if(currentlyLoadedObject.IsPhysicsObject())
            {
                object = doc.createElement("PhysicsObject");
            }
            else if(currentlyLoadedObject.IsTerrainObject())
            {
                object = doc.createElement("TerrainObject");
            }
            else
            {
                object = doc.createElement("HSObject");
            }
            
            //get the list of holds. this will set all of their IDs
            ArrayList<HSObjectHold> holdList = holdListPane.getAllHolds();
            Element eventHolds = doc.createElement("EventHolds");
            
            //get HSObject attributes
            object.setAttribute("name", currentlyLoadedObject.name);
            object.setAttribute("lifetime", "" + currentlyLoadedObject.lifetime);
            
            Element palElement = doc.createElement("Palettes");
            for(HSPalette p : currentlyLoadedObject.palettes)
            {
                if(p.path.isEmpty()) { continue; }
            	Element singlePalElement = doc.createElement("Palette");
                
            	singlePalElement.setAttribute("name", p.name);
            	singlePalElement.setAttribute("path", createPathRelativeTo(p.path, exeDirectory));
            	singlePalElement.setAttribute("id", "" + p.id);
            	
                palElement.appendChild(singlePalElement);
            }
            object.appendChild(palElement);
            
            //get HSObject event holds
            if(currentlyLoadedObject.hsObjectEventHolds.lifetimeDeath != null) { eventHolds.setAttribute("lifetimeDeath", "" + currentlyLoadedObject.hsObjectEventHolds.lifetimeDeath.id); }
            
            if(currentlyLoadedObject.IsTerrainObject())
            {
                //get TerrainObject attributes
                TerrainObject tObject = (TerrainObject)currentlyLoadedObject;
                
                object.setAttribute("bounce", "" + tObject.bounce);
                object.setAttribute("friction", "" + tObject.friction);
                object.setAttribute("health", "" + tObject.health);
                object.setAttribute("takesTerrainDamage", "" + tObject.takesTerrainDamage);
                object.setAttribute("fragile", "" + tObject.fragile);
                object.setAttribute("canBeJumpedThrough", "" + tObject.canBeJumpedThrough);
                
                Element terrainBoxesElement = doc.createElement("TerrainBoxes");
                //get TerrainObject terrain box
                for(HSBox tBox : tObject.terrainBoxes) {
	                Element terrainBox = doc.createElement("TerrainBox");
	                terrainBox.setAttribute("offsetX", "" + tBox.offset.x);
	                terrainBox.setAttribute("offsetY", "" + tBox.offset.y);
	                terrainBox.setAttribute("width", "" + tBox.width);
	                terrainBox.setAttribute("height", "" + tBox.height);
	                terrainBox.setAttribute("depth", "" + tBox.depth);
	                terrainBoxesElement.appendChild(terrainBox);
                }
                object.appendChild(terrainBoxesElement);
                
                //on hit sounds
                if(!tObject.onHitSounds.isEmpty())
                {
                    Element hitAudioList = doc.createElement("OnHitAudioList");
                    for(HSAudio a : tObject.onHitSounds)
                    {
                        Element hitAudio = doc.createElement("OnHitAudio");
                        hitAudio.setAttribute("delay", "" + a.delay);
                        hitAudio.setAttribute("hitAudioFilePath", createPathRelativeTo(a.filePath, exeDirectory));
                        hitAudio.setAttribute("exclusive", "" + a.exclusive);
                        hitAudio.setAttribute("percentage", "" + a.percentage);
                        hitAudio.setAttribute("usePercentage", "" + a.usePercentage);
                        hitAudioList.appendChild(hitAudio);
                    }
                    object.appendChild(hitAudioList);
                }
                
                //get TerrainObject event holds
                if(tObject.terrainEventHolds.healthDeath != null) { eventHolds.setAttribute("healthDeath", "" + tObject.terrainEventHolds.healthDeath.id); }
            } //End Is Terrain Object
            
            if(currentlyLoadedObject.IsPhysicsObject())
            {
                //get PhysicsObject attributes
                PhysicsObject pObject = (PhysicsObject)currentlyLoadedObject;
                
                object.setAttribute("falls", "" + pObject.falls);
                object.setAttribute("mass", "" + pObject.mass);
                object.setAttribute("maxFallSpeed", "" + pObject.maxFallSpeed);
            } //End Is Physics Object
            
            if(currentlyLoadedObject.IsFighter())
            {
                //get Fighter attributes
                FighterObject fighter = (FighterObject)currentlyLoadedObject;
                
                object.setAttribute("airActions", "" + fighter.airActions);
                object.setAttribute("airControlAccel", "" + fighter.airControlAccel);
                object.setAttribute("backwardAirDashDuration", "" + fighter.backwardAirDashDuration);
                object.setAttribute("backwardAirDashSpeed", "" + fighter.backwardAirDashSpeed);
                object.setAttribute("forwardAirDashDuration", "" + fighter.forwardAirDashDuration);
                object.setAttribute("forwardAirDashSpeed", "" + fighter.forwardAirDashSpeed);
                object.setAttribute("jumpSpeed", "" + fighter.jumpSpeed);
                object.setAttribute("maxAirControlSpeed", "" + fighter.maxAirControlSpeed);
                object.setAttribute("runSpeed", "" + fighter.runSpeed);
                object.setAttribute("stepHeight", "" + fighter.stepHeight);
                object.setAttribute("walkSpeed", "" + fighter.walkSpeed);
                
                //get Fighter upright terrain box
                Element uprightTerrainBox = doc.createElement("UprightTerrainBox");
                uprightTerrainBox.setAttribute("offsetX", "" + fighter.uprightTerrainBoxes.get(0).offset.x);
                uprightTerrainBox.setAttribute("offsetY", "" + fighter.uprightTerrainBoxes.get(0).offset.y);
                uprightTerrainBox.setAttribute("width", "" + fighter.uprightTerrainBoxes.get(0).width);
                uprightTerrainBox.setAttribute("height", "" + fighter.uprightTerrainBoxes.get(0).height);
                uprightTerrainBox.setAttribute("depth", "" + fighter.terrainBoxes.get(0).depth);
                object.appendChild(uprightTerrainBox);
                
                //get Fighter crouching terrain box
                Element crouchingTerrainBox = doc.createElement("CrouchingTerrainBox");
                crouchingTerrainBox.setAttribute("offsetX", "" + fighter.crouchingTerrainBoxes.get(0).offset.x);
                crouchingTerrainBox.setAttribute("offsetY", "" + fighter.crouchingTerrainBoxes.get(0).offset.y);
                crouchingTerrainBox.setAttribute("width", "" + fighter.crouchingTerrainBoxes.get(0).width);
                crouchingTerrainBox.setAttribute("height", "" + fighter.crouchingTerrainBoxes.get(0).height);
                crouchingTerrainBox.setAttribute("depth", "" + fighter.terrainBoxes.get(0).depth);
                object.appendChild(crouchingTerrainBox);
                
                //get Fighter upright terrain box
                Element proneTerrainBox = doc.createElement("ProneTerrainBox");
                proneTerrainBox.setAttribute("offsetX", "" + fighter.proneTerrainBoxes.get(0).offset.x);
                proneTerrainBox.setAttribute("offsetY", "" + fighter.proneTerrainBoxes.get(0).offset.y);
                proneTerrainBox.setAttribute("width", "" + fighter.proneTerrainBoxes.get(0).width);
                proneTerrainBox.setAttribute("height", "" + fighter.proneTerrainBoxes.get(0).height);
                proneTerrainBox.setAttribute("depth", "" + fighter.terrainBoxes.get(0).depth);
                object.appendChild(proneTerrainBox);
                
                //get Fighter compact terrain box
                Element compactTerrainBox = doc.createElement("CompactTerrainBox");
                compactTerrainBox.setAttribute("offsetX", "" + fighter.compactTerrainBoxes.get(0).offset.x);
                compactTerrainBox.setAttribute("offsetY", "" + fighter.compactTerrainBoxes.get(0).offset.y);
                compactTerrainBox.setAttribute("width", "" + fighter.compactTerrainBoxes.get(0).width);
                compactTerrainBox.setAttribute("height", "" + fighter.compactTerrainBoxes.get(0).height);
                compactTerrainBox.setAttribute("depth", "" + fighter.terrainBoxes.get(0).depth);
                object.appendChild(compactTerrainBox);
                
                //get Fighter event holds
                if(fighter.fighterEventHolds.standing != null) { eventHolds.setAttribute("standing", "" + fighter.fighterEventHolds.standing.id); }
                if(fighter.fighterEventHolds.turn != null) { eventHolds.setAttribute("turn", "" + fighter.fighterEventHolds.turn.id); }
                if(fighter.fighterEventHolds.crouch != null) { eventHolds.setAttribute("crouch", "" + fighter.fighterEventHolds.crouch.id); }
                if(fighter.fighterEventHolds.crouching != null) { eventHolds.setAttribute("crouching", "" + fighter.fighterEventHolds.crouching.id); }
                if(fighter.fighterEventHolds.crouchingTurn != null) { eventHolds.setAttribute("crouchingTurn", "" + fighter.fighterEventHolds.crouchingTurn.id); }
                if(fighter.fighterEventHolds.stand != null) { eventHolds.setAttribute("stand", "" + fighter.fighterEventHolds.stand.id); }
                if(fighter.fighterEventHolds.walk != null) { eventHolds.setAttribute("walk", "" + fighter.fighterEventHolds.walk.id); }
                if(fighter.fighterEventHolds.walking != null) { eventHolds.setAttribute("walking", "" + fighter.fighterEventHolds.walking.id); }
                if(fighter.fighterEventHolds.walkingTurn != null) { eventHolds.setAttribute("walkingTurn", "" + fighter.fighterEventHolds.walkingTurn.id); }
                if(fighter.fighterEventHolds.run != null) { eventHolds.setAttribute("run", "" + fighter.fighterEventHolds.run.id); }
                if(fighter.fighterEventHolds.running != null) { eventHolds.setAttribute("running", "" + fighter.fighterEventHolds.running.id); }
                if(fighter.fighterEventHolds.runningTurn != null) { eventHolds.setAttribute("runningTurn", "" + fighter.fighterEventHolds.runningTurn.id); }
                if(fighter.fighterEventHolds.runningStop != null) { eventHolds.setAttribute("runningStop", "" + fighter.fighterEventHolds.runningStop.id); }
                if(fighter.fighterEventHolds.jumpNeutralStart != null) { eventHolds.setAttribute("jumpNeutralStart", "" + fighter.fighterEventHolds.jumpNeutralStart.id); }
                if(fighter.fighterEventHolds.jumpNeutralStartAir != null) { eventHolds.setAttribute("jumpNeutralStartAir", "" + fighter.fighterEventHolds.jumpNeutralStartAir.id); }
                if(fighter.fighterEventHolds.jumpNeutralRising != null) { eventHolds.setAttribute("jumpNeutralRising", "" + fighter.fighterEventHolds.jumpNeutralRising.id); }
                if(fighter.fighterEventHolds.jumpNeutralFall != null) { eventHolds.setAttribute("jumpNeutralFall", "" + fighter.fighterEventHolds.jumpNeutralFall.id); }
                if(fighter.fighterEventHolds.jumpNeutralFalling != null) { eventHolds.setAttribute("jumpNeutralFalling", "" + fighter.fighterEventHolds.jumpNeutralFalling.id); }
                if(fighter.fighterEventHolds.jumpNeutralLand != null) { eventHolds.setAttribute("jumpNeutralLand", "" + fighter.fighterEventHolds.jumpNeutralLand.id); }
                if(fighter.fighterEventHolds.jumpBackwardRising != null) { eventHolds.setAttribute("jumpBackwardRising", "" + fighter.fighterEventHolds.jumpBackwardRising.id); }
                if(fighter.fighterEventHolds.jumpBackwardFall != null) { eventHolds.setAttribute("jumpBackwardFall", "" + fighter.fighterEventHolds.jumpBackwardFall.id); }
                if(fighter.fighterEventHolds.airDashForward != null) { eventHolds.setAttribute("airDashForward", "" + fighter.fighterEventHolds.airDashForward.id); }
                if(fighter.fighterEventHolds.airDashBackward != null) { eventHolds.setAttribute("airDashBackward", "" + fighter.fighterEventHolds.airDashBackward.id); }
                if(fighter.fighterEventHolds.blockHigh != null) { eventHolds.setAttribute("blockHigh", "" + fighter.fighterEventHolds.blockHigh.id); }
                if(fighter.fighterEventHolds.blockLow != null) { eventHolds.setAttribute("blockLow", "" + fighter.fighterEventHolds.blockLow.id); }
                if(fighter.fighterEventHolds.blockAir != null) { eventHolds.setAttribute("blockAir", "" + fighter.fighterEventHolds.blockAir.id); }
                if(fighter.fighterEventHolds.hitstunLightHighStanding != null) { eventHolds.setAttribute("hitstunLightHighStanding", "" + fighter.fighterEventHolds.hitstunLightHighStanding.id); }
                if(fighter.fighterEventHolds.hitstunLightMidStanding != null) { eventHolds.setAttribute("hitstunLightMidStanding", "" + fighter.fighterEventHolds.hitstunLightMidStanding.id); }
                if(fighter.fighterEventHolds.hitstunLightLowStanding != null) { eventHolds.setAttribute("hitstunLightLowStanding", "" + fighter.fighterEventHolds.hitstunLightLowStanding.id); }
                if(fighter.fighterEventHolds.hitstunLightMidCrouching != null) { eventHolds.setAttribute("hitstunLightMidCrouching", "" + fighter.fighterEventHolds.hitstunLightMidCrouching.id); }
                if(fighter.fighterEventHolds.hitstunLightLowCrouching != null) { eventHolds.setAttribute("hitstunLightLowCrouching", "" + fighter.fighterEventHolds.hitstunLightLowCrouching.id); }
                if(fighter.fighterEventHolds.hitstunLightAir != null) { eventHolds.setAttribute("hitstunLightAir", "" + fighter.fighterEventHolds.hitstunLightAir.id); }
                if(fighter.fighterEventHolds.attackLightNeutralGround != null) { eventHolds.setAttribute("attackLightNeutralGround", "" + fighter.fighterEventHolds.attackLightNeutralGround.id); }
                if(fighter.fighterEventHolds.attackLightDownGround != null) { eventHolds.setAttribute("attackLightDownGround", "" + fighter.fighterEventHolds.attackLightDownGround.id); }
                if(fighter.fighterEventHolds.attackLightUpGround != null) { eventHolds.setAttribute("attackLightUpGround", "" + fighter.fighterEventHolds.attackLightUpGround.id); }
                if(fighter.fighterEventHolds.attackLightForwardGround != null) { eventHolds.setAttribute("attackLightForwardGround", "" + fighter.fighterEventHolds.attackLightForwardGround.id); }
                if(fighter.fighterEventHolds.attackLightQCFGround != null) { eventHolds.setAttribute("attackLightQCFGround", "" + fighter.fighterEventHolds.attackLightQCFGround.id); }
                if(fighter.fighterEventHolds.attackLightNeutralAir != null) { eventHolds.setAttribute("attackLightNeutralAir", "" + fighter.fighterEventHolds.attackLightNeutralAir.id); }
                if(fighter.fighterEventHolds.attackLightDownAir != null) { eventHolds.setAttribute("attackLightDownAir", "" + fighter.fighterEventHolds.attackLightDownAir.id); }
                if(fighter.fighterEventHolds.attackLightUpAir != null) { eventHolds.setAttribute("attackLightUpAir", "" + fighter.fighterEventHolds.attackLightUpAir.id); }
                if(fighter.fighterEventHolds.attackLightForwardAir != null) { eventHolds.setAttribute("attackLightForwardAir", "" + fighter.fighterEventHolds.attackLightForwardAir.id); }
                if(fighter.fighterEventHolds.attackLightBackwardAir != null) { eventHolds.setAttribute("attackLightBackwardAir", "" + fighter.fighterEventHolds.attackLightBackwardAir.id); }
                if(fighter.fighterEventHolds.attackLightQCFAir != null) { eventHolds.setAttribute("attackLightQCFAir", "" + fighter.fighterEventHolds.attackLightQCFAir.id); }
                
                if(fighter.fighterEventHolds.attackHeavyNeutralGround != null) { eventHolds.setAttribute("attackHeavyNeutralGround", "" + fighter.fighterEventHolds.attackHeavyNeutralGround.id); }
                if(fighter.fighterEventHolds.attackHeavyDownGround != null) { eventHolds.setAttribute("attackHeavyDownGround", "" + fighter.fighterEventHolds.attackHeavyDownGround.id); }
                if(fighter.fighterEventHolds.attackHeavyUpGround != null) { eventHolds.setAttribute("attackHeavyUpGround", "" + fighter.fighterEventHolds.attackHeavyUpGround.id); }
                if(fighter.fighterEventHolds.attackHeavyForwardGround != null) { eventHolds.setAttribute("attackHeavyForwardGround", "" + fighter.fighterEventHolds.attackHeavyForwardGround.id); }
                if(fighter.fighterEventHolds.attackHeavyQCFGround != null) { eventHolds.setAttribute("attackHeavyQCFGround", "" + fighter.fighterEventHolds.attackHeavyQCFGround.id); }
                if(fighter.fighterEventHolds.attackHeavyNeutralAir != null) { eventHolds.setAttribute("attackHeavyNeutralAir", "" + fighter.fighterEventHolds.attackHeavyNeutralAir.id); }
                if(fighter.fighterEventHolds.attackHeavyDownAir != null) { eventHolds.setAttribute("attackHeavyDownAir", "" + fighter.fighterEventHolds.attackHeavyDownAir.id); }
                if(fighter.fighterEventHolds.attackHeavyUpAir != null) { eventHolds.setAttribute("attackHeavyUpAir", "" + fighter.fighterEventHolds.attackHeavyUpAir.id); }
                if(fighter.fighterEventHolds.attackHeavyForwardAir != null) { eventHolds.setAttribute("attackHeavyForwardAir", "" + fighter.fighterEventHolds.attackHeavyForwardAir.id); }
                if(fighter.fighterEventHolds.attackHeavyBackwardAir != null) { eventHolds.setAttribute("attackHeavyBackwardAir", "" + fighter.fighterEventHolds.attackHeavyBackwardAir.id); }
                if(fighter.fighterEventHolds.attackHeavyQCFAir != null) { eventHolds.setAttribute("attackHeavyQCFAir", "" + fighter.fighterEventHolds.attackHeavyQCFAir.id); }
                
                if(fighter.fighterEventHolds.knockout != null) { eventHolds.setAttribute("knockout", "" + fighter.fighterEventHolds.knockout.id); }
            } //End Is Fighter
            
            object.appendChild(eventHolds);
            
            //get holds
            Element holds = doc.createElement("Holds");
            for(HSObjectHold h : holdList)
            {
                Element hold = doc.createElement("Hold");
                
                //get HSObjectHold attributes
                hold.setAttribute("name", h.name);
                hold.setAttribute("duration", "" + h.duration);
                hold.setAttribute("id", "" + h.id);
                hold.setAttribute("repositionX", "" + h.reposition.x);
                hold.setAttribute("repositionY", "" + h.reposition.y);
                hold.setAttribute("velocityX", "" + h.velocity.x);
                hold.setAttribute("velocityY", "" + h.velocity.y);
                hold.setAttribute("overwriteVelocity", "" + h.overwriteVelocity);
                
                if(h.nextHold != null) { hold.setAttribute("nextHoldId", "" + h.nextHold.id); }
                
                if(h.IsTerrainObjectHold())
                {
                    //get TerrainObjectHold attributes
                    TerrainObjectHold th = (TerrainObjectHold)h;
                    
                    hold.setAttribute("blockability", "" + th.blockability);
                    hold.setAttribute("ownHitstop", "" + th.ownHitstop);
                    hold.setAttribute("victimHitstop", "" + th.victimHitstop);
                    hold.setAttribute("blockstun", "" + th.blockstun);
                    hold.setAttribute("changeAttackBoxAttributes", "" + th.changeAttackBoxAttributes);
                    hold.setAttribute("damage", "" + th.damage);
                    hold.setAttribute("forceX", "" + th.force.x);
                    hold.setAttribute("forceY", "" + th.force.y);
                    hold.setAttribute("hitstun", "" + th.hitstun);
                    hold.setAttribute("horizontalDirectionBasedBlock", "" + th.horizontalDirectionBasedBlock);
                    hold.setAttribute("reversedHorizontalBlock", "" + th.reversedHorizontalBlock);
                    hold.setAttribute("trips", "" + th.trips);
                    hold.setAttribute("resetHits", "" + th.resetHits);
                    
                    //get attack boxes
                    if(!th.attackBoxes.isEmpty())
                    {
                        Element attackBoxes = doc.createElement("AttackBoxes");
                        for(HSBox b : th.attackBoxes)
                        {
                            Element box = doc.createElement("Box");
                            box.setAttribute("offsetX", "" + b.offset.x);
                            box.setAttribute("offsetY", "" + b.offset.y);
                            box.setAttribute("width", "" + b.width);
                            box.setAttribute("height", "" + b.height);
                            box.setAttribute("depth", "" + b.depth);
                            attackBoxes.appendChild(box);
                        }
                        hold.appendChild(attackBoxes);
                    }
                    
                    //get hurt boxes
                    if(!th.hurtBoxes.isEmpty())
                    {
                        Element hurtBoxes = doc.createElement("HurtBoxes");
                        for(HSBox b : th.hurtBoxes)
                        {
                            Element box = doc.createElement("Box");
                            box.setAttribute("offsetX", "" + b.offset.x);
                            box.setAttribute("offsetY", "" + b.offset.y);
                            box.setAttribute("width", "" + b.width);
                            box.setAttribute("height", "" + b.height);
                            box.setAttribute("depth", "" + b.depth);
                            hurtBoxes.appendChild(box);
                        }
                        hold.appendChild(hurtBoxes);
                    }
                    
                    //get hit sounds
                    if(!th.hitAudioList.isEmpty())
                    {
                        Element hitAudioList = doc.createElement("HitAudioList");
                        for(HSAudio a : th.hitAudioList)
                        {
                            Element hitAudio = doc.createElement("HitAudio");
                            hitAudio.setAttribute("delay", "" + a.delay);
                            hitAudio.setAttribute("hitAudioFilePath", createPathRelativeTo(a.filePath, exeDirectory));
                            hitAudio.setAttribute("exclusive", "" + a.exclusive);
                            hitAudio.setAttribute("percentage", "" + a.percentage);
                            hitAudio.setAttribute("usePercentage", "" + a.usePercentage);
                            hitAudioList.appendChild(hitAudio);
                        }
                        hold.appendChild(hitAudioList);
                    }
                    
                    //get blocked sounds
                    if(!th.blockedAudioList.isEmpty())
                    {
                        Element blockedAudioList = doc.createElement("BlockedAudioList");
                        for(HSAudio a : th.blockedAudioList)
                        {
                            Element blockedAudio = doc.createElement("BlockedAudio");
                            blockedAudio.setAttribute("delay", "" + a.delay);
                            blockedAudio.setAttribute("blockedAudioFilePath", createPathRelativeTo(a.filePath, exeDirectory));
                            blockedAudio.setAttribute("exclusive", "" + a.exclusive);
                            blockedAudio.setAttribute("percentage", "" + a.percentage);
                            blockedAudio.setAttribute("usePercentage", "" + a.usePercentage);
                            blockedAudioList.appendChild(blockedAudio);
                        }
                        hold.appendChild(blockedAudioList);
                    }
                }
                
                if(h.IsPhysicsObjectHold())
                {
                    //get PhysicsObjectHold attributes
                    PhysicsObjectHold ph = (PhysicsObjectHold)h;
                    
                    hold.setAttribute("changePhysicsAttributes", "" + ph.changePhysics);
                    hold.setAttribute("ignoreGravity", "" + ph.ignoreGravity);
                }
                
                if(h.IsFighterHold())
                {
                    //get FighterHold attributes
                    FighterHold fh = (FighterHold)h;
                    
                    hold.setAttribute("changeCancels", "" + fh.changeCancels);
                    hold.setAttribute("dashCancel", "" + fh.cancels.dash);
                    hold.setAttribute("jumpCancel", "" + fh.cancels.jump);
                    hold.setAttribute("lightNeutralCancel", "" + fh.cancels.lightNeutral);
                    hold.setAttribute("lightForwardCancel", "" + fh.cancels.lightForward);
                    hold.setAttribute("lightUpCancel", "" + fh.cancels.lightUp);
                    hold.setAttribute("lightDownCancel", "" + fh.cancels.lightDown);
                    hold.setAttribute("lightBackwardCancel", "" + fh.cancels.lightBackward);
                    hold.setAttribute("lightQCFCancel", "" + fh.cancels.lightQCF);
                    hold.setAttribute("heavyNeutralCancel", "" + fh.cancels.heavyNeutral);
                    hold.setAttribute("heavyForwardCancel", "" + fh.cancels.heavyForward);
                    hold.setAttribute("heavyUpCancel", "" + fh.cancels.heavyUp);
                    hold.setAttribute("heavyDownCancel", "" + fh.cancels.heavyDown);
                    hold.setAttribute("heavyBackwardCancel", "" + fh.cancels.heavyBackward);
                    hold.setAttribute("heavyQCFCancel", "" + fh.cancels.heavyQCF);
                }
                
                //get textures
                if(!h.textures.isEmpty())
                {
                    Element textures = doc.createElement("Textures");
                    for(HSTexture t : h.textures)
                    {
                        Element texture = doc.createElement("Texture");
                        texture.setAttribute("depth", "" + t.depth);
                        texture.setAttribute("offsetX", "" + t.offset.x);
                        texture.setAttribute("offsetY", "" + t.offset.y);
                        texture.setAttribute("textureFilePath", createPathRelativeTo(t.filePath, exeDirectory));
                        textures.appendChild(texture);
                    }
                    hold.appendChild(textures);
                }
                
                //get sounds
                if(!h.audioList.isEmpty())
                {
                    Element audioList = doc.createElement("AudioList");
                    for(HSAudio a : h.audioList)
                    {
                        Element audio = doc.createElement("Audio");
                        audio.setAttribute("delay", "" + a.delay);
                        audio.setAttribute("audioFilePath", createPathRelativeTo(a.filePath, exeDirectory));
                        audio.setAttribute("exclusive", "" + a.exclusive);
                        audio.setAttribute("percentage", "" + a.percentage);
                        audio.setAttribute("usePercentage", "" + a.usePercentage);
                        audioList.appendChild(audio);
                    }
                    hold.appendChild(audioList);
                }
                
                //get spawn objects
                if(!h.spawnObjects.isEmpty())
                {
                    Element spawnObjects = doc.createElement("SpawnObjects");
                    for(SpawnObject s : h.spawnObjects)
                    {
                        Element spawnObject = doc.createElement("SpawnObject");
                        spawnObject.setAttribute("definitionFilePath", createPathRelativeTo(s.defFilePath, exeDirectory));
                        spawnObject.setAttribute("delay", "" + s.delay);
                        spawnObject.setAttribute("number", "" + s.number);
                        spawnObject.setAttribute("parentOffsetX", "" + s.parentOffset.x);
                        spawnObject.setAttribute("parentOffsetY", "" + s.parentOffset.y);
                        spawnObject.setAttribute("velocityX", "" + s.vel.x);
                        spawnObject.setAttribute("velocityY", "" + s.vel.y);
                        spawnObject.setAttribute("followParent", "" + s.followParent);
                        spawnObject.setAttribute("collideParent", "" + s.collideParent);
                        spawnObject.setAttribute("useParentPalette", "" + s.useParentPalette);
                        spawnObjects.appendChild(spawnObject);
                    }
                    hold.appendChild(spawnObjects);
                }
                
                holds.appendChild(hold);
            }
            object.appendChild(holds);
            root.appendChild(object);
            doc.appendChild(root);
            
            //finally, save the file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(workingDirectory + File.separatorChar + currentlyLoadedObject.name + ".xml"));
            transformer.transform(source, result);
        }
        catch(ParserConfigurationException e)
        {
        	JOptionPane.showMessageDialog(this, e.getMessage(), "Parser Configuration Exception", JOptionPane.ERROR_MESSAGE);  
            
        }
        catch(TransformerConfigurationException e)
        {
        	JOptionPane.showMessageDialog(this, e.getMessage(), "Transformer Configuration Exception", JOptionPane.ERROR_MESSAGE);
        }
        catch(TransformerException e)
        {
        	JOptionPane.showMessageDialog(this, e.getMessage(), "Transformer Exception", JOptionPane.ERROR_MESSAGE);   
        }
    }
    
    private void saveAs()
    {
        if(currentlyLoadedObject == null) {
        	JOptionPane.showMessageDialog(this, "No Object loaded", "Whoops", JOptionPane.ERROR_MESSAGE);
        	return;
        }
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnVal = fileChooser.showSaveDialog(this);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        File file;
        
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            file = fileChooser.getSelectedFile();
        } else {
            return;
        }

        workingDirectory = file.getAbsolutePath();
        
        //Now that we have a working directory, we can save
        save();
    }
    
    private void save()
    {
        //If we don't have a working directory or a loaded object, we should save as instead (save as can handle the lack of a loaded object as well)
        if(currentlyLoadedObject == null || workingDirectory.isEmpty()) { saveAs(); }
        
        File wd = new File(workingDirectory);
        if(!wd.exists()) { return; }
        
        setTitle(BaseWindowTitle + currentlyLoadedObject.name);
        
        createDefinitionFile();
    }
	
	private void importAnimation() {
        if(currentlyLoadedObject == null) {
        	JOptionPane.showMessageDialog(this, "No Object loaded", "Whoops", JOptionPane.ERROR_MESSAGE);
        	return;
        }
        fileChooser.setMultiSelectionEnabled(true);
        int returnVal = fileChooser.showOpenDialog(this);
        File[] files;
        
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            files = fileChooser.getSelectedFiles();
        } else {
            return;
        }
        
        if(currentlyLoadedObject.IsFighter()) {
        	FighterHold lastHold = null;
        	for(File f : files) {
        		FighterHold hold = new FighterHold();
        		hold.id = currentlyLoadedObject.getHighestHoldId() + 1;
        		hold.textures.add(new HSTexture(f.getAbsolutePath()));
        		hold.name = f.getName().split("\\.")[0];
        		holdListPane.addHoldToHoldList(hold);
        		if(lastHold != null) {
        			lastHold.nextHold = hold;
        			lastHold.nextHoldId = hold.id;
        		}
        		currentlyLoadedObject.holds.add(hold);
        		lastHold = hold;
        	}
        }
        else if(currentlyLoadedObject.IsPhysicsObject()) {
        	PhysicsObjectHold lastHold = null;
        	for(File f : files) {
        		PhysicsObjectHold hold = new FighterHold();
        		hold.id = currentlyLoadedObject.getHighestHoldId() + 1;
        		hold.textures.add(new HSTexture(f.getAbsolutePath()));
        		hold.name = f.getName().split("\\.")[0];
        		holdListPane.addHoldToHoldList(hold);
        		if(lastHold != null) {
        			lastHold.nextHold = hold;
        			lastHold.nextHoldId = hold.id;
        		}
        		currentlyLoadedObject.holds.add(hold);
        		lastHold = hold;
        	}        	
        }
        else if(currentlyLoadedObject.IsTerrainObject()) {
        	TerrainObjectHold lastHold = null;
        	for(File f : files) {
        		TerrainObjectHold hold = new FighterHold();
        		hold.id = currentlyLoadedObject.getHighestHoldId() + 1;
        		hold.textures.add(new HSTexture(f.getAbsolutePath()));
        		hold.name = f.getName().split("\\.")[0];
        		holdListPane.addHoldToHoldList(hold);
        		if(lastHold != null) {
        			lastHold.nextHold = hold;
        			lastHold.nextHoldId = hold.id;
        		}
        		currentlyLoadedObject.holds.add(hold);
        		lastHold = hold;
        	}        	
        }
        else {
        	HSObjectHold lastHold = null;
        	for(File f : files) {
        		HSObjectHold hold = new FighterHold();
        		hold.id = currentlyLoadedObject.getHighestHoldId() + 1;
        		hold.textures.add(new HSTexture(f.getAbsolutePath()));
        		hold.name = f.getName().split("\\.")[0];
        		holdListPane.addHoldToHoldList(hold);
        		if(lastHold != null) {
        			lastHold.nextHold = hold;
        			lastHold.nextHoldId = hold.id;
        		}
        		currentlyLoadedObject.holds.add(hold);
        		lastHold = hold;
        	}        	
        	
        }
        fileChooser.setMultiSelectionEnabled(false);
	}

	private void undo() {
		textureHitboxPane.undo();
	}

	private void redo() {
		textureHitboxPane.redo();		
	}

	private void cut() {
		textureHitboxPane.cut();
	}

	private void copy() {
		textureHitboxPane.copy();
	}

	private void paste() {
		textureHitboxPane.paste();
	}
    
    private void delete()
    {
        textureHitboxPane.textureHitboxPane.removeSelectedItems();
    }
    
    private void selectAll()
    {
        textureHitboxPane.textureHitboxPane.selectAll();
    }
    
    private void createObjectAttributesWindow()
    {
    	if(currentlyLoadedObject == null) {
    		JOptionPane.showMessageDialog(this, "No Object loaded", "Whoops", JOptionPane.ERROR_MESSAGE);  
    		return;
    	}
        ObjectAttributesWindow window = new ObjectAttributesWindow(this, currentlyLoadedObject);
        window.setVisible(true);
    }
    
    private void createEventHoldsWindow()
    {
    	if(currentlyLoadedObject == null) {
    		JOptionPane.showMessageDialog(this, "No Object loaded", "Whoops", JOptionPane.ERROR_MESSAGE);  
    		return;
    	}
        EventHoldsWindow window = new EventHoldsWindow(this, currentlyLoadedObject);
        window.setVisible(true);
    }
    
    private void createPalettesWindow()
    {
    	if(currentlyLoadedObject == null) {
    		JOptionPane.showMessageDialog(this, "No Object loaded", "Whoops", JOptionPane.ERROR_MESSAGE);  
    		return;
    	}
        PalettesWindow window = new PalettesWindow(this, currentlyLoadedObject);
        window.setVisible(true);
    }
    int curPal;
    public void updatePalettesMenu() {
    	updatePalettesMenu(false);
    }
    public void updatePalettesMenu(boolean warn) {
    	palettesMenu.removeAll();
		JMenuItem menuItem;
    	if(currentlyLoadedObject == null || currentlyLoadedObject.palettes.size() == 0) {
    		palettesMenu.add(new JMenuItem("NONE"));
    	}
    	else {
	    	for(curPal=0; curPal < currentlyLoadedObject.palettes.size(); curPal++) {
	    		boolean alreadyUsedId = false;
		    	for(HSPalette hsp : currentlyLoadedObject.palettes) {
		    		if(hsp.id != curPal) continue;
		    		if(alreadyUsedId) {
		    			if(warn)
		    				JOptionPane.showMessageDialog(this, "Duplicate palette IDs detected on: " + hsp.name, "Palette Loading", JOptionPane.ERROR_MESSAGE);  
		    			continue;
		    		}
		    		alreadyUsedId = true;
		    		//Set each palette menu item to load the correct palette when clicked
		    		menuItem = new JMenuItem(new AbstractAction(hsp.name) {
						private static final long serialVersionUID = 1L;
						int palNum = curPal;
						@Override
						public void actionPerformed(ActionEvent arg0) {
					        currentlyLoadedObject.curPalette = palNum;
					        textureHitboxPane.reloadTextures();	
						}
					});
			    	palettesMenu.add(menuItem);
		    	}
	    	}
    	}
    	palettesMenu.add(new JSeparator());
    	menuItem = new JMenuItem("Edit Palettes");
    	menuItem.setActionCommand("palettes");
    	menuItem.addActionListener(this);
    	palettesMenu.add(menuItem);
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
    	System.out.println(e.getActionCommand());
        switch(e.getActionCommand())
        {
            case "newGraphic": newGraphic(); break;
            case "newTerrain": newTerrain(); break;
            case "newPhysicsObject": newPhysicsObject(); break;
            case "newFighter": newFighter(); break;
            case "generate": generate(); break;
            case "open": open(); break;
            case "save": save(); break;
            case "saveAs": saveAs(); break;
            case "importAnimation": importAnimation(); break;
            case "exeLocation": setExeLocation(); break;
            case "undo": undo(); break;
            case "redo": redo(); break;
            case "cut": cut(); break;
            case "copy": copy(); break;
            case "paste": paste(); break;
            case "delete": delete(); break;
            case "selectAll": selectAll(); break;
            case "objectAttributes": createObjectAttributesWindow(); break;
            case "eventHolds": createEventHoldsWindow(); break;
            case "palettes": createPalettesWindow(); break;
        }
    }

	private class KeyDispatcher implements KeyEventDispatcher {
		@Override
		public boolean dispatchKeyEvent(KeyEvent e) {
			if(e.getID() == KeyEvent.KEY_PRESSED) {
				if ((e.getKeyCode() == KeyEvent.VK_X) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
					if(holdListPane.tree.isFocusOwner())
						cut();	
		        }
				else if ((e.getKeyCode() == KeyEvent.VK_C) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
					if(holdListPane.tree.isFocusOwner())
						copy();
		        }
				else if ((e.getKeyCode() == KeyEvent.VK_V) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
					if(holdListPane.tree.isFocusOwner())
						paste();
		        }
				else {
					
				}
				/*
				 * TODO: Fix Control + A
				else if ((e.getKeyCode() == KeyEvent.VK_A) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
					selectAll();
				}
				*/
			}
			return false;
		}
	}
}
