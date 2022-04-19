![Login image](./img/Logo.png)
# SceneOneFX

SceneOneFX is the rebuilt Switcher library and is a utility library designed for use in JavaFX applications that makes adding and switching Scenes easy.
SceneOneFX can be used to add Scenes with ease and simplicity in your project. No more messing around with 
Scene and Stage objects, let SceneOneFX handle all that for you.

There are also [completely runnable test applications](./src/test/java) that
show how to use SceneOneFX in different ways.

## Usage

SceneOneFX needs **two** items before a Scene can exist. Those items are:
* A sceneID - a String that is unique to each scene you create.
* A Parent - Any acceptable JavaFX Parent Container: HBox, VBox, Anchor Pane, etc.

You utilize SceneOneFX's Builder class (set) to create your Scenes like this:
```java
String sceneId = "MyScene";
SceneOne.set(sceneId, vbox).build();
```

You now have a Scene that you can show from ANY Class
simply by calling:
```java
SceneOne.show(sceneId);
```
It's that simple.

You can also build and show your scene in a single line:

```java
SceneOne.set("MyScene", vbox).show();
```

SceneOneFX handles all the back end mess that normally goes along with
managing Scenes in JavaFX.

Of course, there are more advanced options built into SceneOneFX giving
you full control on all levels with simplicity and common sense
ways to interact with your Scenes.

## How do I add SceneOneFX to my project

The project is available as a Maven dependency on Central. Add the following to your POM file:

```xml
<dependency>
    <groupId>com.simtechdata</groupId>
    <artifactId>SceneOneFX</artifactId>
    <version>1.0.1</version>
</dependency>
```

Or, if using Gradle to build, add this to your Gradle build file

```groovy
compile group: 'com.simtechdata', name: 'SceneOneFX', version: 1.0.1
```

You can even use it from a Groovy script!

```groovy
@Grapes(
  @Grab(group='com.simtechdata', module='SceneOneFX', version=1.0.1)
)
```

## Additional Features

SceneOneFX gives you control over every aspect of your Scene.

### Size and Position
You can also change the dimensions of the Scene in the Builder class or on the fly like so:
```java
SceneOne.set(sceneID, parent).size(width, height).build();
SceneOne.resize(sceneId, width, height);
```

If you want to specify the coordinates of the scene, you do this:
```java
SceneOne.set(sceneID, parent).position(X, Y).build();
SceneOne.setPosition(sceneId, X, Y);
```

Or if you want your scene centered automatically:
```java
SceneOne.set(sceneID, parent).size(width, height).centered().build();
```

Your show options can be stated in place of ```.build()```
```java
SceneOne.set(sceneID, parent).size(width, height).centered().show();
SceneOne.set(sceneID, parent).size(width, height).centered().showAndWait();
```

or after the scene is built
```java
SceneOne.show(sceneID);
SceneOne.showAndWait(sceneID);
```

## Core Objects

If needed, you can gain access to any of the core objects that are involved in a JavaFX Scene

```java
SceneOne.getStage(sceneId);
SceneOne.getScene(sceneId);
SceneOne.getWindow(sceneId);
```

So, for example, if you need to pass the Scenes window to the FileChooser class
```java
File file = fileChooser.showOpenDialog(SceneOne.getWindow(sceneId));
```

The example programs also show how to use SceneOneFX to create simple custom alerts or confirmation 
windows with the ability to easily access the user's response.

## Builder Options

Here are all the builder options that you can use to create your Scenes:

You first start out by calling a new instance of SceneOneFX's Builder class and
passing it a sceneID and either a Parent object or a Scene object.

```java
SceneOne.set(sceneID, parent)
SceneOne.set(sceneID, parent, width, height)
.size(width, height)
.position(X, Y)
.owner(Stage) //Assign an existing stage as this scenes owner Stage
.centered()
.centered(width, height) //Used when you want to call a showAndWait
.initStyle(StageStyle)
.modality(Modality)
.title(String)
.onShownEvent(e -> myMethod())
.onHiddenEvent(e -> myMethod())
.onShowingEvent(e -> myMethod())
.onHidingEvent(e -> myMethod())
.onCloseEvent(e -> myMethod())
.alwaysOnTop()
.show()
.showAndWait()
.build()
```

Check the example programs for good examples on using these options. 

## Post Build

You can also change any of the above settings by simply calling SceneOne with the setting
you wish to change and passing in the sceneId.

For example
```java
SceneOne.setTitle(sceneId, String);
SceneOne.reSize(sceneId, width, height);
SceneOne.setOnShownEvent(sceneId, e-> myMethod());
```

### Splitting the Anchor Point
#### This feature has not yet been implemented into SceneOneFX

I came across a situation where I wanted to have a Scene, which was more narrow than wide, drop
down from the MacOS System tray and I wanted it to drop right where the mouse pointer was when the
user clicked on the icon in the tray.

What I ended up doing, was getting the mouse pointers X and Y coordinataes at the moment it was clicked,
then I took those coordinates and sent them to SceneOneFX to set the X and Y position of the upper left
corner of the window. But that wasn't quite what I was looking for. What I needed was a way to have
SceneOneFX center the window (which was of a TRANSPARENT Style) on the mouse pointer, so I came up with the
showScene option of Split.X, Split.Y or Split.XY.

How it works is you send the X and Y values into the showScene method, then chose
whether to have SceneOneFX split the location on the X axis, the Y axis, or both, then you pass
in the split factor, which will be multiplied by the X or Y values. So if you need to split it
down the middle, you would pass in .5, like this.

Here is how it works:

```jave
SceneOne.showScene(sceneID, X, Y, SceneOne.Split.X, .5);
SceneOne.showScene(sceneID, X, Y, SceneOne.Split.Y, .5);
SceneOne.showScene(sceneID, X, Y, SceneOne.Split.XY,.5);
```
Where the values of X and Y are doubles.

### Removing A Scene or a Stage

If you need to, you can also remove a scene from SceneOne.
```java
SceneOne.remove(sceneID);
```

## Support Options

* Any operating system that supports JavaFX will support SceneOne.
* Any version of JavaFX from 1.8 and up
* Modular projects merely need to add this to their ```module-info.java``` file
```java
requires com.simtechdata.sceneonefx;
```

---

## Projects using `SceneOneFX`

If your project uses SceneOneFX, shoot me an email - sims.mike@gmail.com - I'd love to hear how SceneOneFX is working for you.

---
<h1 style="font-size:8vw"><p style="color:royalblue">Version Update Notes</p></h1>

## Version 1.0

* **1.0.1**
  * Fixed bug in .remove method so that it now destroys scene objects before removing.

* **1.0.0**
    * It's here!
