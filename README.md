HomestrifeObjectEditor
================

Object editor for Homestrife. Pull requests welcome.


Howto
============
Before you do any editing, it is smart to set up the editor with the game's location. It will prompt you to give a game location if you don't have one set. You need to point this variable to the location that the Homestrife game .exe file is located in (this directory should also have a data/ subdirectory) so that it can properly save and load the assets.

In order to edit an already existing object, go to File > Open and find the "Object Name.xml" file (characters should be located under `$GAMEDIR/data/characters/name/Character Name.xml`). If you selected a valid character, it will load the object's holds into a tree and you can choose which one to edit, if you did not choose a valid xml, it will complain with an error message.

To create a new object, just go to File > New and choose the kind of object you want (See **Object Types** below for the different kinds of objects)

I'm not 100% sure what the File > Generate command does. I think it generates a character definition based on a generate definition that you give it. 

The File > Import Animation commands lets you choose a list of images and it parses them and puts them all into holds on the current object.

Object Types
======

## Graphic
 TODO
## Terrain
TODO
## Physics
TODO
## Fighter
What the characters are. Takes key input and uses it to choose holds and, you know, fight and stuff.
