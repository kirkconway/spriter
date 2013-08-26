Spriter
=======

A Generic Java library for Spriter animation files.



Basic usage
--------------------------
The library is meant to be generic, so to use it you have to implement some classes on your own:
*A specific file loader (FileLoader) class which loads all needed sprites into memory and stores them in a reference map.
*A specific drawer (AbstractDrawer) class which is repsonsible for drawing the animation data and also to debug draw an animation.
If you do not know how to implement them, have a look at the LibGDX demo.
