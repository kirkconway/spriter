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
The method will loop the animation on its own.

In you rendering (or drawing) method you just call:
```
instanceOfYourDrawer.draw(player);
```
Which should draw the animation at `(x,y)` if your specific drawer is implemented in a proper way.

That's it! Now read further if you want to know what other abilities the library has.

Manipulate a SpriterPlayer at runtime
-----------------------------
Of course you have also the ability to change the animation and speed of your player.
Change animation:
```
player.setAnimationIndex(anAnimationIndex);
//This method changes the animation immediately to the given animation index

player.setAnimationIndex(anAnimationIndex, 1, 10);
/*This method does the same, but with a smooth transition from the current animation index the given one
*/This will take 10 update steps to switch into anAnimationIndex

player.setAnimation("animationName", 1, 20); 
/*This method does the same as the previous one, but this time you pass a name of the animation instead of an index
*The method will search only in the previous set entity. So if you have set the entity index to 0 in the constructor
*and you search for an animation which is in the entity with the index 1 it could happen that you get an error.
*/This will take 20 update steps to switch into "animationName"
```
Note: All methods will throw a RuntimeException if the given animation index or name does not exist.
Keep in mind that a smooth transition between two animations is only usefull if both animations have the same structure.

Change speed and current frame:
```
player.setFrameSpeed(15);
//This will set the frame speed to 15. This means that in every update step the frame will jump 15 frames further

player.setFrame(100);
/*This will set the current frame to 100 regardless what current frame we have now.
*/This can be handy if you want to stop the animation at a specific frame (you have to set the speed to 0 before)
```
Yes, this is everything what you need for a basic usage of this library.

Manipulating bones and objects at runtime
-----------------------------------------
This is also a cool topic. For example if you want your character to look always to the mouse or an enemy.
The library gives you access to all updated bones and objects. You have just to call:
```
player.getRuntimeBones();
//or
player.getRuntimeObjects();
```
those two methods will give you an array of all bones or objects which have been updated.
Accessing a specific instance in this way could be a little bit complicated, since you do not always know where in the
array the desired bone or object is.
So there are also methods which can give you a specific bone (index) or object (index) just by searching for its name:
```
SpriterBone bone = player.getBoneByName("boneName");
//or
SpriterBone bone = palyer.getRuntimeBones()[player.getBoneIndexByName("boneName")];
//This works for SpriterObject analogous
```
After accessing a specific bone or object instance you can apply translation, scaling or roation to it.
What you need to keep in mind is that applying those operations have to happen __after__ updating the player
and __before__ drawing it. Otherwise you will not see any changes.
```
bone.setAngle(yourAngle);
bone.setX(x); bone.setY(y);
bone.setScaleX(scaleX); bone.setScaleY(scaleY);
```
The method sould be self explanatory and this works for objects analogous.

Bounding boxes
--------------
Calculating bounding boxes is also possible with the library and it is also necessary to do so, for performance reasons.

Anyway, to calculate a bounding box for a player is quite easy:
```
player.calcBoundingBox(null);
//by setting the argument to null a bbox for all objects is calculated
//you can change the argument to an arbitary SpriterBone instance
//this enables the ability to cull sprites out, to increase performance
SpriterRectangle rectangle = player.getBoundingBox();
```

That is all what you need to know about bouding boxes.

Interpolate two running animations
----------------------------------
Another cool thing about this library is that you can create an instance of SpriterPlayerInterpolator which will enable
the ability to interpolate two, already tweened, animations (this works only in a proper way if both animatons have the
same structure).
What you need is two SpriterAbstractPlayer instances (usually two SpriterPlayer instances) which tween the keyframes of
two different animations and pass those instances to the constructor of SpriterPlayerInterpolator:
```
SpriterPlayer player1 = new SpriterPlayer(spriter, 0, myLoader);
SpriterPlayer player2 = new SpriterPlayer(spriter, 0, myLoader);
player2.setAnimation("otherAnimationThanPlayer1", 1,1): //Make sure the players have two different animations
SpriterPlayerInterpolator inter = new SpriterPlayerInterpolator(player1, player2);
```
After creating an instance of SpriterPlayerInterpolator you can use it as a normal SpriterPlayer instance
(except changing animations and changing playback speed will have no effect).

A SpriterPlayerInterpolator instance will update player1 and player2 on its own. You can change this, by setting
`inter.updatePlayers = false;` if you want to draw the other players on different places
(of course you have to update the players then on your own).

You can change the interpolation weight of a SpriterPlayerInterpolator instance with `inter.setWeight(.75f);`.
The value has to be between 0 and 1, where 0 means that the interpolator is playing the current animation of player1 back
and 1 means it plays the current animation of player2 back. All other cases will end up in a mix between the two current animations.

You could of course create a second interpolator, which interpolates `inter` and `player2`. Just play around with it.


