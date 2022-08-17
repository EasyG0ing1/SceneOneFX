![Login image](./img/LogoSocial.png)
# SceneOneFX

SceneOneFX is the rebuilt Switcher library and is a utility library designed for use in JavaFX
applications that makes adding and switching Scenes easy. SceneOneFX can be used to add Scenes
with ease and simplicity in your project. No more messing around with Scene and Stage objects,
let SceneOneFX handle all that for you.

With SceneOneFX, any Scene you create can be accessed by any class in your application.

There are also [completely runnable test applications](./src/test/java) that
show how to use SceneOneFX in different ways.

See [Single Stage Update](#single-stage-update) below for information about the recent update
to use a single Stage for all Scenes.

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
    <version>1.3.1</version>
</dependency>
```

Or, if using Gradle to build, add this to your Gradle build file

```groovy
compile group: 'com.simtechdata', name: 'SceneOneFX', version: 1.3.1
```

You can even use it from a Groovy script!

```groovy
@Grapes(
  @Grab(group='com.simtechdata', module='SceneOneFX', version=1.3.1)
)
```
## Additional Features
### Set Master Title

You can set the title once, and all of your Scenes will use that title
```java
SceneOne.setTitle(String);
```

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

## Show the Scene before the one currently showing
```java
if (SceneOne.lastSceneAvailable()) {
  SceneOne.showLastScene();
}
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

## Apply StyleSheets To All Scenes At Once
This is great for maintaining themes in your applications.
```Java
String styleSheetOne = Main.class.getResource("MyStylesheet1.css").toExternalForm();
String styleSheetTwo = Main.class.getResource("MyStylesheet2.css").toExternalForm();
SceneOne.setStyleSheetsForAll(styleSheetOne);
SceneOne.setStyleSheetsForAll(styleSheetOne, styleSheetTwo);
```
OR set a stylesheet(s) for just one Scene
```Java
String styleSheetOne = Main.class.getResource("MyStylesheet1.css").toExternalForm();
String styleSheetTwo = Main.class.getResource("MyStylesheet2.css").toExternalForm();
SceneOne.setStyleSheets(sceneId, styleSheetOne);
SceneOne.setStyleSheets(sceneId, styleSheetOne, styleSheetTwo);
```

## Using in larger applications
I have discovered that one of the best ways to employ SceneOneFX into an application that is more complex
is to first create an ENUM class that has all of your SceneId's, then simply reference that class 
when you use SceneOneFX
```Java
public enum SceneId {
  MAIN_SCENE,
  USER_OPTIONS,
  CHAT_WINDOW;
  
  public String get(SceneId this) {
    return switch(this) {
      CASE MAIN_SCENE -> "MainScene";
      CASE USER_OPTIONS -> "UserOptions";
      CASE CHAT_WINDOW -> "ChatWindow";
    };
  }
}
```
```Java
public class MainScene {
  public MainScene() {
    SceneOne.set(SceneId.MAIN_SCENE.get(), content()).build();
  }
  
  private VBox content() {
    //Code that adds the controls etc. to the VBox
  }
  
  public void showScene() {
    SceneOne.show(SceneId.MAIN_SCENE.get());
  }
}
```


## Builder Options

Here are some of the builder options that you can use to create your Scenes:

You first start out by calling a new instance of SceneOneFX's Builder class then give it a sceneID and a Parent object with
optional scene size dimensions.

```java
SceneOne.set(sceneID, parent)
SceneOne.set(sceneID, parent, width, height)
.size(width, height)
.position(X, Y)
.centered()
.centered(width, height) //Used when you want to call a showAndWait and you didn't specify the dimensions previously
.hideOnLostFocus()
.hideOnLostFocus(boolean)
.initStyle(StageStyle)
.modality(Modality)
.title(String)
.onShownEvent(e -> myMethod())
.onShowingEvent(e -> myMethod())
.onHiddenEvent(e -> myMethod())
.onHidingEvent(e -> myMethod())
.onCloseEvent(e -> myMethod())
.alwaysOnTop() //Literally puts the scene on top of EVERYTHING and keeps it there until you close it.
.show()
.showAndWait()
.owner(Stage) //Lets you assign the scene to another scene which will keep this scene on top of that scene
.owner(sceneId) //as opposed to keeping it on top of ALL windows - great for dialogue windows.
.build()
.show()
.showAndWait()
```

See the Javadocs for a full list of Builder options and also look at the example programs for good
examples on using these options. I will add more to the examples in the future, as time permits.

## Post Build

You can also change any of the above settings by simply calling SceneOne with the setting
you wish to change and passing in the sceneId (again, look at Javadocs for all available public methods
or use code completion in your IDE).

For example
```java
SceneOne.setTitle(sceneId, String);
SceneOne.reSize(sceneId, width, height);
SceneOne.setOnShownEvent(sceneId, e-> myShownMethod(e));
```

### Splitting the Anchor Point

I came across a situation where I wanted to have a Scene, which was more narrow than wide, drop
down from the MacOS System tray and I wanted it to drop right where the mouse pointer was when the
user clicked on the icon in the tray. What I needed, was a way for SceneOneFX to utilize the mouse
pointer coordinates at the moment the menu was clicked, to calculate and position the scene centered
horizontally at the mouse pointer. So I came up with these methods:

```java
SceneOne.showSplitXY(sceneId, posX, posY, splitFactorX, splitFactorY);
```

So in my particular use case, I would use the method like this
```java
SceneOne.showSplitXY(sceneId, mouseX, mousyY, .5, 1);
```
So that SceneOneFX then takes the scenes width, multiplies it by .5 and then changes the position of 
the upper left corner by an offset equal to half of the width, which centers it at the mouse pointer.

Of course, you can use any multiplication factor for horizontal (splitFactorX) and vertical(splitFactorY)
and it will position your scene accordingly.

These are all the methods that utilize scene position splitting:
```java
SceneOne.showSplitXY(sceneId, posX, posY, splitFactorX, splitFactorY);
SceneOne.showSplitX(sceneId, posX, splitFactorX);
SceneOne.showSplitY(sceneId, posY, splitFactorY);
```

Or via the Builder
```java
SceneOne.set(sceneId, parent, width, height).splitXY(posX, posY, splitFactorX, splitFactorY).show();
SceneOne.set(sceneId, parent, sceneWidth, sceneHeight).splitX(posX, splitFactorX).show();
SceneOne.set(sceneId, parent, sceneWidth, sceneHeight).splitY(posY, splitFactorY).show();
```
You must explicitly assign a width and a height to your Scene or else there will be no way for SceneFX
to calculate the offset.

In this example, we center the scene on the mouse pointer both horizontally and vertically.

```Java
SceneOne.showSplit(sceneID, mouseX, mouseY, .5, .5);
```

### Event Triggers
You can add event driven methods to your Scenes in both in the builder and after you have created your Scene

#### Builder
```java
SceneOne.set(sceneId, parent).onKeyPressed(e -> keyPressedMethod(e)).build();
SceneOne.set(sceneId, parent).onKeyReleased(e -> keyReleasedMethod(e)).build();
SceneOne.set(sceneId, parent).onShowingEvent(e -> showingEventMethod(e)).build();
SceneOne.set(sceneId, parent).onShownEvent(e -> shownEventMethod(e)).build();
SceneOne.set(sceneId, parent).onHidingEvent(e -> hidingMethod(e)).build();
SceneOne.set(sceneId, parent).onHiddenEvent(e -> hiddenMethod(e)).build();
SceneOne.set(sceneId, parent).onCloseEvent(e -> closeMethod(e)).build();
SceneOne.set(sceneId, parent).onWindowCloseEvent(e -> windowCloseMethod(e)).build();
SceneOne.set(sceneId, parent).onLostFocus(e -> lostFocusMethod(e)).build();
```

#### Post Build
Simply call SceneOne and preface any of the above with the word set and give it the SceneId.
```java
SceneOne.setOnKeyPressed(sceneId, e -> keyPressedMethod(e));
SceneOne.setOnLostFocuse(sceneId, e -> lostFocusMethod(e));
```

### Removing A Scene or a Stage

If you need to, you can also remove a scene from SceneOne.
```java
SceneOne.remove(sceneID);
```

## Quick User Responses
SceneOneFX now has some methods in it that let you get some "quick and dirty" feedback from your
users. Two different options exist; one where you can just pass in a yes or no question, then get
back a response, or an option where you can pass in a question along with any number of responses
that you wish to offer and SceneOneFX will create a button for each response, then pass back
an integer value that is proportional to the order in which you pass in the response options, with
0 being the first response.

```Java
int answer = SceneOne.askYesNo(sceneId,"Do you want ice cream?",300,100);
  switch(answer) {
  case 0: // 0 = No
    response.setText("User DOES NOT want ice cream");
    break;
  case 1: // 1 = Yes
    response.setText("User DOES want ice cream");
    break;
  default: // Anything else means the user closed the window without answering 
    response.setText("User did not respond");
}
```

See the two runnable classes under test under the ```choiceresponse``` package called: ChoiceResponse
and YesNo. Most IDEs will let you right click on the class and simply chose run. This works well
in IntelliJ.

### Quick Messages
You can also use SceneOneFX to show quick and dirty popup messages that only offer an OK button.
An example of doing this exists in the same test code, but here are the two methods that you can use
to display quick messages.
```Java
showMessage(sceneId, width, height, message) //Text wrapping defaults to true and alinment defaults to CENTERed
showMessage(sceneId, width, height, message, true, Pos.CENTER) //last two options are for wrapping the text in the label and the alignment of the text within the label
```

### Javadocs
* You can see all of the public methods in the Javadocs. If you're using IntelliJ, simply put the cursor over the word SceneOne and press F1 twice, then click on the link that says 'SceneOne' on localhost

### Support Options

* Any operating system that supports JavaFX will support SceneOneFX.
* Any version of JavaFX from 1.8 and up
* Modular projects merely need to add this to their ```module-info.java``` file
```java
requires com.simtechdata.sceneonefx;
```

---

## Projects using SceneOneFX
* [GistFX](https://www.github.com/redmondSims/GistFX)
* [AKnightsTale](https://github.com/maicol07/OOP21-knighttale)

If your project uses SceneOneFX, shoot me an email [sims.mike@gmail.com](mailto:sims.mike@gmail.com) - I'd love to hear how SceneOneFX is working for you.

To add your project to the list here, you can also submit a pull request after changing this README.

## Single Stage Update
SceneOneFX now follows conventional JavaFX theory by using a single Stage object to display all of the
Scenes that you create. You can override this behavior by calling newStage() in your build sentence.

```Java
SceneOne.set(sceneId, parent).newStage().build();
```


When you have a Scene that is currently showing, then you create a new Scene, that new Scene will be shown
on the default Stage. When you then close that Scene, the previously showing Scene will be shown. You can
chose to instead close the Stage that any Scene is currently showing on like this

```Java
SceneOne.closeStage(sceneId);
SceneOne.closeStageIfShowing(sceneId);
```

However, simply calling ```SceneOne.close(sceneId)``` will close the Stage, if that scene is the first
Scene that was created for that Stage.

Adding ```newStage()``` to all of your build sentences will cause SceneOneFX to behave as it has always behaved
prior to version 1.3.1

---

Version Update Notes
---

* **1.3.1**
  * Modified SceneOneFX so that all Scenes are shown on the same stage. See section above entitled 'Single Stage Update'

* **1.3.0**
  * Deprecated Builder option .centered(width, height) - instead of defining the size of the scene within this method for situations when you use showAndWait, SceneOneFX now uses the dimensions that you pass in from either the .set() option or the Builder.size(width, height) method. If you try and invoke showAndWait and also chose to center the scene, but you do not specify the dimensions of the scene, SceneOneFX will throw an error explaining what you must do.
  * Added the askYesNo() method
  * Added the choiceResponse() method
  * Added the showMessage() method

* **1.2.4**
  * Added setTitle method 

* **1.2.3**
  * Added stage(Stage) option to the Builder class. This allows you to pass in your own Stage, which will be used for that Scene.
  * Removed global variables that rely on java.awt.Toolkit and only reference that class in those methods that need it.  

* **1.2.2**
  * Added ability to start a scene by passing in a Scene object to the Builder instead of just a Parent object
  * Added swapScene() method in addition to the existing setParent() method where swapScene swaps the Scene passed out onto the existing Stage.
  * Added example program that demonstrates Scene swapping.

* **1.2.1**
  * Added lastSceneAvailable() method
  * Added showLastScene() method

* **1.2.0**
  * Added Split ability - see section above 'Splitting The Anchor Point'
  * Added more event setters to Builder and main class.
  * Bug fixes / code enhancements

* **1.0.16**
  * Added ```hideOnLostFocus()``` methods to the Builder and main class
  * Added ```hideIfShowing()```
  * Added ```reShow()``` method which takes a Scene that has been shown and is still in memory and simply re-shows it
  * Bug Fixes

* **1.0.12**
  * Modified relevant Builder class methods to check for null for pass in objects, making it easier for developers to create template code without needing to worry about verifying objects passed into those templates as the Builder class will handle it.

* **1.0.11**
  * Added owner(String sceneId) to the Builder class - letting you set the stages owner to a stage that has already been created by SceneOneFX.
  * Added closeIfShowing method - making it easy to close a scene that may or may not be showing. Does not throw an error if the scene does not already exist.

* **1.0.10**
  * Added more capabilities

* **1.0.1**
  * Fixed bug in .remove method so that it now destroys scene objects before removing.

* **1.0.0**
    * It's here!
