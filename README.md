Spriter
=======

A Generic Java library for Spriter animation files.



Basic usage
-----------
The library is meant to be generic, so to use it you have to implement some classes on your own:
*   A specific file loader ([FileLoader](https://github.com/Trixt0r/spriter/blob/master/Spriter/src/com/brashmonkey/spriter/file/FileLoader.java "FileLoader")) class which loads all needed sprites into memory and stores them in a reference map.
*   A specific drawer ([AbstractDrawer](https://github.com/Trixt0r/spriter/blob/master/Spriter/src/com/brashmonkey/spriter/draw/AbstractDrawer.java "AbstractDrawer")) class which is repsonsible for drawing the animation data and also to debug draw an animation.

As you see, you have to implement all abstract methods.
If you do not know how to implement them, have a look at the LibGDX demo.

After implementing those classes, the remaining should be quite easy.
Create a Spriter instance which takes care of parsing the scml file and loading the necessary sprites:
```
Spriter spriter = Spriter.getSpriter("Path to your scml file", instanceOfYourLoader);
//You can also pass a java.io.File instance as the first argument.
```

After instantiating a Spriter object, you can create a SpriterAbstractPlayer instance:
```
SpriterAbstractPlayer player = new SpriterPlayer(spriter, entityIndexToAnimate, instanceOfYourLoader);
```
Where `entityIndexToAnimate` is the index of your entity you want to animate at runtime (usually 0).

If you have an instance of a SpriterAbstractPlayer class, you are now able to animate and draw the current set animation.
In you update method you should call:
```
player.update(x,y);
```
Where `x` and `y` are the coordinates you want to draw the animation later on.

In you rendering (or drawing) method you just call:
```
instanceOfYourDrawer.draw(player);
```
Which should draw the animation at `(x,y)` if your specific drawer is implemented in a proper way.

That's it! Now read further if you want to know what other abilities the library has.

Manipulate players at runtime
-----------------------------
Of course you have also the ability to change the animation and speed of your player.
Change animation:
```
//This method changes the animation immediately to the given animation index
player.setAnimationIndex(anAnimationIndex);
//This method does the same, but with a smooth transition from the current animation index the given one
player.setAnimationIndex(anAnimationIndex, 1, 10); //This will take 10 update steps to switch into anAnimationIndex
//This method does the same as the previous one, but this time you pass a name of the animation instead of an index
player.setAnimation("animationName", 1, 20); //This will take 20 update steps to switch into "animationName"
```
Note: All methods will throw a RuntimeException if the given animation index or name does not exist.
