package com.simtechdata.sceneonefx;

import com.simtechdata.sceneonefx.id.Get;
import com.simtechdata.sceneonefx.id.RandomString;
import com.simtechdata.sceneonefx.id.SceneID;
import com.simtechdata.sceneonefx.id.StageID;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Window;
import javafx.stage.*;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.concurrent.ConcurrentHashMap;

public class SceneOne {

	enum DIMENSION {
		WIDTH,
		HEIGHT,
		ALL
	}

	private static final ConcurrentHashMap<SceneID, SceneObject> sceneMap               = new ConcurrentHashMap<>();
	private static final ConcurrentHashMap<Parent, Scene>        parentSceneMap         = new ConcurrentHashMap<>();
	private static       String                                  masterTitle            = "";
	private static       boolean                                 makeNewStageEveryScene = false;
	private static boolean debug = false;

	/**
	 * Builder Class
	 */

	public static class Builder {

		public Builder(String sceneID, Parent parent) {
			if (sceneExists(sceneID)) remove(sceneID);
			this.sceneID = Get.newSceneID(sceneID);
			this.parent  = parent;
		}

		public Builder(String sceneID, Parent parent, double width, double height) {
			if (sceneExists(sceneID)) remove(sceneID);
			this.sceneID = Get.newSceneID(sceneID);
			this.parent  = parent;
			this.width   = width;
			this.height  = height;
		}

		public Builder(String sceneID, Scene scene) {
			if (sceneExists(sceneID)) remove(sceneID);
			this.sceneID = Get.newSceneID(sceneID);
			this.scene   = scene;
		}

		public Builder(String sceneID, Scene scene, double width, double height) {
			if (sceneExists(sceneID)) remove(sceneID);
			this.sceneID = Get.newSceneID(sceneID);
			this.scene   = scene;
			this.width   = width;
			this.height  = height;
		}

		public Builder(boolean onDefault, String sceneID, Parent parent) {
			if (sceneExists(sceneID)) remove(sceneID);
			this.sceneID             = Get.newSceneID(sceneID);
			this.parent              = parent;
			this.useUserDefaultStage = true;
		}

		public Builder(boolean onDefault, String sceneID, Parent parent, double width, double height) {
			if (sceneExists(sceneID)) remove(sceneID);
			this.sceneID             = Get.newSceneID(sceneID);
			this.parent              = parent;
			this.width               = width;
			this.height              = height;
			this.useUserDefaultStage = true;
		}

		public Builder(boolean onDefault, String sceneID, Scene scene) {
			if (sceneExists(sceneID)) remove(sceneID);
			this.sceneID             = Get.newSceneID(sceneID);
			this.scene               = scene;
			this.useUserDefaultStage = true;
		}

		public Builder(boolean onDefault, String sceneID, Scene scene, double width, double height) {
			if (sceneExists(sceneID)) remove(sceneID);
			this.sceneID             = Get.newSceneID(sceneID);
			this.scene               = scene;
			this.width               = width;
			this.height              = height;
			this.useUserDefaultStage = true;
		}

		final SceneID sceneID;
		Scene  scene;
		Parent parent;
		final ObservableList<String> styleSheets = FXCollections.observableArrayList();
		double                          width                     = -1;
		double                          height                    = -1;
		StageStyle                      style                     = null;
		Modality                        modality                  = null;
		boolean                         centered                  = false;
		boolean                         alwaysOnTop               = false;
		boolean                         hideOnLostFocus           = false;
		boolean                         fullScreen                = false;
		String                          title                     = masterTitle;
		double                          posX                      = -1;
		double                          posY                      = -1;
		double                          splitFactorX              = -1;
		double                          splitFactorY              = -1;
		EventHandler<WindowEvent>       onShownEventHandler       = null;
		EventHandler<WindowEvent>       onHiddenEventHandler      = null;
		EventHandler<WindowEvent>       onShowingEventHandler     = null;
		EventHandler<WindowEvent>       onHidingEventHandler      = null;
		EventHandler<WindowEvent>       onCloseEventHandler       = null;
		EventHandler<WindowEvent>       onWindowCloseEventHandler = null;
		ChangeListener<? super Boolean> lostFocusListener         = null;
		EventHandler<? super KeyEvent>  keyPressedEventHandler    = null;
		EventHandler<? super KeyEvent>  keyReleasedEventHandler   = null;
		Stage                           owner                     = null;
		Stage                           stage                     = null;
		boolean                         addStage                  = false;
		boolean                         reSizable                 = true;
		boolean                         useUserDefaultStage       = false;

		public Builder newStage() {
			addStage = true;
			return this;
		}

		public Builder owner(Stage owner) {
			if (owner != null) {
				this.owner = owner;
			}
			return this;
		}

		public Builder owner(String sceneId) {
			if (sceneId != null) {
				if (sceneMap.containsKey(sceneID(sceneId))) {
					this.owner = sceneMap.get(sceneID(sceneId)).getStage();
				}
			}
			return this;
		}

		public Builder stage(Stage stage) {
			this.stage = stage;
			return this;
		}

		public Builder centered() {
			this.centered = true;
			return this;
		}

		public Builder centered(boolean centered) {
			this.centered = centered;
			return this;
		}

		/**
		 * @deprecated
		 */
		public Builder centered(double width, double height) {
			this.width    = width;
			this.height   = height;
			this.centered = true;
			return this;
		}

		public Builder fullScreen() {
			this.fullScreen = true;
			return this;
		}

		public Builder hideOnLostFocus() {
			hideOnLostFocus = true;
			return this;
		}

		public Builder hideOnLostFocus(boolean hideOnLostFocus) {
			this.hideOnLostFocus = hideOnLostFocus;
			return this;
		}

		public Builder position(double posX, double posY) {
			this.posX = posX;
			this.posY = posY;
			return this;
		}

		public Builder initStyle(StageStyle style) {
			if (style != null) {this.style = style;}
			return this;
		}

		public Builder styleSheets(String... styleSheets) {
			if (styleSheets != null) {this.styleSheets.addAll(styleSheets);}
			return this;
		}

		public Builder modality(Modality modality) {
			if (modality != null) {this.modality = modality;}
			return this;
		}

		public Builder title(String title) {
			if (title != null) {if (!title.isEmpty()) {this.title = title;}}
			return this;
		}

		public Builder size(double width, double height) {
			this.width  = width;
			this.height = height;
			return this;
		}

		public Builder width(double width) {
			this.width = width;
			return this;
		}

		public Builder height(double height) {
			this.height = height;
			return this;
		}

		public Builder splitXY(double posX, double posY, double xFactor, double yFactor) {
			this.posX         = posX;
			this.posY         = posY;
			this.splitFactorX = xFactor;
			this.splitFactorY = yFactor;
			return this;
		}

		public Builder splitX(double posX, double splitFactorX) {
			this.posX         = posX;
			this.splitFactorX = splitFactorX;
			return this;
		}

		public Builder splitY(double posY, double splitFactorY) {
			this.splitFactorY = splitFactorY;
			this.posY         = posY;
			return this;
		}

		public Builder onKeyPressed(EventHandler<? super KeyEvent> keyPressedEventHandler) {
			if (keyPressedEventHandler != null) {this.keyPressedEventHandler = keyPressedEventHandler;}
			return this;
		}

		public Builder onKeyReleased(EventHandler<? super KeyEvent> keyReleasedEventHandler) {
			if (keyReleasedEventHandler != null) {this.keyReleasedEventHandler = keyReleasedEventHandler;}
			return this;
		}

		public Builder onShowingEvent(EventHandler<WindowEvent> onShowingEventHandler) {
			if (onShowingEventHandler != null) {this.onShowingEventHandler = onShowingEventHandler;}
			return this;
		}

		public Builder onShownEvent(EventHandler<WindowEvent> onShownEventHandler) {
			if (onShownEventHandler != null) {this.onShownEventHandler = onShownEventHandler;}
			return this;
		}

		public Builder onHidingEvent(EventHandler<WindowEvent> onHidingEventHandler) {
			if (onHidingEventHandler != null) {this.onHidingEventHandler = onHidingEventHandler;}
			return this;
		}

		public Builder onHiddenEvent(EventHandler<WindowEvent> onHiddenEventHandler) {
			if (onHiddenEventHandler != null) {this.onHiddenEventHandler = onHiddenEventHandler;}
			return this;
		}

		public Builder onCloseEvent(EventHandler<WindowEvent> onCloseEventHandler) {
			if (onCloseEventHandler != null) {this.onCloseEventHandler = onCloseEventHandler;}
			return this;
		}

		public Builder onWindowCloseEvent(EventHandler<WindowEvent> onWindowCloseEventHandler) {
			if (onWindowCloseEventHandler != null) {this.onWindowCloseEventHandler = onWindowCloseEventHandler;}
			return this;
		}

		public Builder onLostFocus(ChangeListener<? super Boolean> lostFocusListener) {
			this.lostFocusListener = lostFocusListener;
			return this;
		}

		public Builder alwaysOnTop() {
			this.alwaysOnTop = true;
			return this;
		}

		public Builder alwaysOnTop(boolean alwaysOnTop) {
			this.alwaysOnTop = alwaysOnTop;
			return this;
		}

		public Builder reSizable(boolean reSizable) {
			this.reSizable = reSizable;
			return this;
		}

		public void build() {
			boolean sceneHasStage = Stages.hasStage(sceneID);
			addStage = addStage || SceneOne.makeNewStageEveryScene;
			if (!addStage && sceneHasStage) {
				StageID stageID = Stages.getStageId(sceneID);
				if (style != null) {Stages.initStyle(stageID, style);}
				if (modality != null) {Stages.initModality(stageID, modality);}
				if (owner != null) {Stages.initOwner(stageID, owner.getScene().getWindow());}
				stage = Stages.getStage(sceneID);
			}
			else if (!addStage && !useUserDefaultStage) {
				if (style != null) {Stages.initDefaultStyle(style);}
				if (modality != null) {Stages.initDefaultModality(modality);}
				if (owner != null) {Stages.initDefaultOwner(owner.getScene().getWindow());}
				stage = Stages.getDefaultStage();
			}
			else if (useUserDefaultStage) {
				if (style != null) {Stages.initUserDefaultStyle(style);}
				if (modality != null) {Stages.initUserDefaultModality(modality);}
				if (owner != null) {Stages.initUserDefaultOwner(owner.getScene().getWindow());}
				stage = Stages.getUserDefaultStage();
			}
			else if (addStage || stage == null) {
				Stages.newStage(sceneID);
				StageID stageID = Stages.getStageId(sceneID);
				if (style != null) {Stages.initStyle(stageID, style);}
				if (modality != null) {Stages.initModality(stageID, modality);}
				if (owner != null) {Stages.initOwner(stageID, owner.getScene().getWindow());}
				stage = Stages.getStage(sceneID);
			}
			if (parentSceneMap.containsKey(this.parent)) {
				this.scene = parentSceneMap.get(this.parent);
			}
			else if (this.scene == null) {
				this.scene = new Scene(this.parent);
				parentSceneMap.put(this.parent, this.scene);
			}
			else {
				parentSceneMap.put(this.parent, this.scene);
			}
			sceneMap.remove(sceneID);
			sceneMap.put(sceneID, new SceneObject(this));
		}

		public void show() {
			build();
			sceneMap.get(sceneID).show();
		}

		public void showAndWait() {
			if (centered) {
				if (width < 0 || height < 0) throw centerOnWaitError();
			}
			build();
			sceneMap.get(sceneID).showAndWait();
		}
	}

	/**
	 * Public Methods
	 */

	public static void debug() {
		debug = true;
	}

	public static Builder set(String sceneId, Parent parent) {
		return new Builder(sceneId, parent);
	}

	public static Builder set(String sceneId, Parent parent, double width, double height) {
		return new Builder(sceneId, parent, width, height);
	}

	public static Builder set(String sceneId, Scene scene) {
		return new Builder(sceneId, scene);
	}

	public static Builder set(String sceneId, Scene scene, double width, double height) {
		return new Builder(sceneId, scene, width, height);
	}

	public static Builder setOnDefault(String sceneId, Parent parent) {
		return new Builder(true, sceneId, parent);
	}

	public static Builder setOnDefault(String sceneId, Parent parent, double width, double height) {
		return new Builder(true, sceneId, parent, width, height);
	}

	public static Builder setOnDefault(String sceneId, Scene scene) {
		return new Builder(true, sceneId, scene);
	}

	public static Builder setOnDefault(String sceneId, Scene scene, double width, double height) {
		return new Builder(true, sceneId, scene, width, height);
	}

	public static void toggleFullScreen(String sceneId) {
		checkScene(sceneID(sceneId));
		Platform.runLater(() -> sceneMap.get(sceneID(sceneId)).toggleFullScreen());
	}

	public static String getRandom(int size) {
		return RandomString.get(size);
	}

	public static String randomSceneId(){
		return RandomString.get(25);
	}

	/**
	 * @deprecated
	 */
	public static void disableNotice() {
		System.out.println("SceneOne.disableNotice() is no longer necessary and has been deprecated. It will be removed in the next release.");
	}

	/**
	 * @deprecated
	 */
	public static void showLastScene() {
		System.out.println("SceneOne.showLastScene() Was causing issues and had to be removed. I apologize for the abrupt cancellation of this method, but it was necessary for progress.");
	}

	/**
	 * @deprecated
	 */
	public static void lastSceneAvailable() {
		System.out.println("SceneOne.lastSceneAvailable() Was causing issues and had to be removed. I apologize for the abrupt cancellation of this method, but it was necessary for progress.");
	}

	/**
	 * Actions
	 */

	private static SceneID sceneID(String sceneId) {
		return Get.sceneID(sceneId);
	}

	public static void setDefaultStage(Stage stage) {
		Stages.setUserDefaultStage(stage);
	}

	public static void setDefaultStage(Stage stage, boolean newStage) {
		Stages.setUserDefaultStage(stage, newStage);
	}

	public static void show(String sceneId) {
		checkScene(sceneID(sceneId));
		Platform.runLater(() -> sceneMap.get(sceneID(sceneId)).show());
	}

	public static void showAndWait(String sceneId) {
		checkScene(sceneID(sceneId));
		sceneMap.get(sceneID(sceneId)).showAndWait();
	}

	public static void hide(String sceneId) {
		checkScene(sceneID(sceneId));
		Platform.runLater(() -> sceneMap.get(sceneID(sceneId)).hide());
	}

	public static void unHide(String sceneId) {
		checkScene(sceneID(sceneId));
		Platform.runLater(() -> sceneMap.get(sceneID(sceneId)).unHide());
	}

	public static void reSize(String sceneId, double width, double height) {
		checkScene(sceneID(sceneId));
		Platform.runLater(() -> sceneMap.get(sceneID(sceneId)).resize(width, height));
	}

	public static void closeStage(String sceneId) {
		checkScene(sceneID(sceneId));
		try {
			if (sceneMap.get(sceneID(sceneId)) != null) {
				Platform.runLater(() -> sceneMap.get(sceneID(sceneId)).close());
			}
		} catch(NullPointerException ignore){}
	}

	public static void close(String sceneId) {
		checkScene(sceneID(sceneId));
		try {
			if (sceneMap.get(sceneID(sceneId)) != null) {
				debug("SceneOneFX close()");
				Platform.runLater(() -> sceneMap.get(sceneID(sceneId)).close());
			}
		} catch(NullPointerException ignore){}
	}

	public static void closeIfShowing(String sceneId) {
		checkScene(sceneID(sceneId));
		if (isShowing(sceneId)) {close(sceneId);}
	}

	public static void closeStageIfShowing(String sceneId) {
		checkScene(sceneID(sceneId));
		if (isShowing(sceneId)) closeStage(sceneId);
	}

	public static void hideIfShowing(String sceneId) {
		if (sceneMap.containsKey(sceneID(sceneId))) {
			Platform.runLater(() -> sceneMap.get(sceneID(sceneId)).hideIfShowing());
		}
	}

	public static void remove(String sceneId) {
		if (sceneMap.containsKey(sceneID(sceneId))) {
			try {
				Platform.runLater(() -> {
					sceneMap.remove(sceneID(sceneId));
					Stages.removeScene(sceneID(sceneId));
				});
			} catch(NullPointerException ignore){}
		}
	}

	public static void center(String sceneId) {
		checkScene(sceneID(sceneId));
		Platform.runLater(() -> sceneMap.get(sceneID(sceneId)).center());
	}

	public static void reCenterScene(String sceneId) {
		checkScene(sceneID(sceneId));
		Platform.runLater(() -> sceneMap.get(sceneID(sceneId)).reCenterScene());
	}

	public static void reShow(String sceneId) {
		checkScene(sceneID(sceneId));
		Platform.runLater(() -> sceneMap.get(sceneID(sceneId)).reShow());
	}

	public static void showSplitXY(String sceneId, double mouseX, double mouseY, double factorX, double factorY) {
		checkScene(sceneID(sceneId));
		Platform.runLater(() -> sceneMap.get(sceneID(sceneId)).showSplit(mouseX, mouseY, factorX, factorY));
	}

	public static void showSplitX(String sceneId, double mouseX, double factorX) {
		checkScene(sceneID(sceneId));
		Platform.runLater(() -> sceneMap.get(sceneID(sceneId)).showSplitX(mouseX, factorX));
	}

	public static void showSplitY(String sceneId, double mouseY, double factorY) {
		checkScene(sceneID(sceneId));
		Platform.runLater(() -> sceneMap.get(sceneID(sceneId)).showSplitY(mouseY, factorY));
	}

	public static Integer askYesNo(String parentSceneId, String question, double width, double height) {
		return askYesNo(parentSceneId, question, width, height, Pos.CENTER, true);
	}

	public static Integer askYesNo(String parentSceneId, String question, double width, double height, Pos textAlignment) {
		return askYesNo(parentSceneId, question, width, height, textAlignment, true);
	}

	public static Integer askYesNo(String parentSceneId, String question, double width, double height, Pos textAlignment, boolean wrapText) {
		checkScene(Get.sceneID(parentSceneId));
		String sid   = "SceneOneYesNo";
		Label  label = new Label(question);
		label.setWrapText(wrapText);
		label.setMinWidth(width * .85);
		label.setMaxWidth(width * .85);
		label.setPrefWidth(width * .85);
		label.setAlignment(textAlignment);
		IntegerProperty answer = new SimpleIntegerProperty(-1);
		Button          btnYes = new Button("Yes");
		btnYes.setOnAction(e -> {
			answer.setValue(1);
			SceneOne.close(sid);
		});
		Button btnNo = new Button("No");
		btnNo.setOnAction(e -> {
			answer.setValue(0);
			SceneOne.close(sid);
		});
		HBox hbox = new HBox(20, btnYes, btnNo);
		hbox.setAlignment(Pos.CENTER);
		VBox vbox = new VBox(10, label, hbox);
		vbox.setAlignment(Pos.CENTER);
		vbox.setMinWidth(width);
		vbox.setMinHeight(height);
		SceneOne.set(sid, vbox, width, height).newStage().centered().owner(parentSceneId).alwaysOnTop().build();
		SceneOne.showAndWait(sid);
		SceneOne.remove(sid);
		return answer.getValue();
	}

	public static void showMessage(String parentSceneId, double width, double height, String message) {
		showMessage(parentSceneId, width, height, message, true, Pos.CENTER);
	}

	public static void showMessage(double width, double height, String message) {
		showMessage("", width, height, message, true, Pos.CENTER);
	}

	public static void showMessage(double width, double height, String message, boolean wrapText, Pos textAlignment) {
		showMessage("", width, height, message, wrapText, textAlignment);
	}

	public static void showMessage(String parentSceneId, double width, double height, String message, boolean wrapText, Pos textAlignment) {
		String sid   = "SceneOneMessage" + getRandom(8);
		Label  label = new Label(message);
		label.setWrapText(wrapText);
		label.setMinWidth(width * .85);
		label.setMaxWidth(width * .85);
		label.setPrefWidth(width * .85);
		label.setAlignment(textAlignment);
		Button btnOk = new Button("OK");
		btnOk.setOnAction(e -> SceneOne.close(sid));
		VBox vbox = new VBox(10, label, btnOk);
		vbox.setAlignment(Pos.CENTER);
		vbox.setMinWidth(width);
		vbox.setMinHeight(height);
		if (parentSceneId.isEmpty()) {SceneOne.set(sid, vbox, width, height).newStage().centered().alwaysOnTop().showAndWait();}
		else {
			checkScene(Get.sceneID(parentSceneId));
			SceneOne.set(sid, vbox, width, height).newStage().centered().owner(parentSceneId).alwaysOnTop().showAndWait();
		}
		SceneOne.remove(sid);
	}

	public static Integer choiceResponse(String parentSceneId, String question, double width, double height, String... buttonText) {
		return choiceResponse(parentSceneId, question, width, height, Pos.CENTER, true, buttonText);
	}

	public static Integer choiceResponse(String parentSceneId, String question, double width, double height, Pos textAlignment, boolean wrapText, String... buttonText) {
		checkScene(Get.sceneID(parentSceneId));
		String   sid         = "SceneOneChoiceResponse";
		int      buttonCount = buttonText.length;
		Button[] buttons     = new Button[buttonCount];
		Label    label       = new Label(question);
		label.setWrapText(wrapText);
		label.setMinWidth(width * .85);
		label.setMaxWidth(width * .85);
		label.setPrefWidth(width * .85);
		label.setAlignment(textAlignment);
		IntegerProperty answer = new SimpleIntegerProperty(-1);
		for (int x = 0; x < buttonCount; x++) {
			buttons[x] = new Button(buttonText[x]);
			final int btnAnswer = x;
			buttons[x].setOnAction(e -> {
				answer.setValue(btnAnswer);
				SceneOne.close(sid);
			});
		}
		HBox hbox = new HBox((buttonCount < 4) ? 20 : 15, buttons);
		hbox.setAlignment(Pos.CENTER);
		VBox vbox = new VBox(10, label, hbox);
		vbox.setAlignment(Pos.CENTER);
		vbox.setMinWidth(width);
		vbox.setMinHeight(height);
		SceneOne.set(sid, vbox, width, height).newStage().centered().owner(SceneOne.getStage(parentSceneId)).alwaysOnTop().build();
		SceneOne.showAndWait(sid);
		SceneOne.remove(sid);
		return answer.getValue();
	}

	/**
	 * Setters
	 */

	public static void newStageEveryScene() {
		makeNewStageEveryScene = true;
	}

	public static void setTitle(String title) {
		masterTitle = title;
	}

	public static void setStyleSheetsForAll(String... styleSheets) {
		for (SceneID sceneId : sceneMap.keySet()) {
			SceneObject so = sceneMap.get(sceneId);
			if (so != null) {
				so.setStyleSheets(styleSheets);
			}
		}
	}

	public static void setHeight(String sceneId, double height) {
		checkScene(sceneID(sceneId));
		Platform.runLater(() -> sceneMap.get(sceneID(sceneId)).setHeight(height));
	}

	public static void setWidth(String sceneId, double width) {
		checkScene(sceneID(sceneId));
		Platform.runLater(() -> sceneMap.get(sceneID(sceneId)).setWidth(width));
	}

	public static void setSize(String sceneId, double width, double height) {
		checkScene(sceneID(sceneId));
		Platform.runLater(() -> sceneMap.get(sceneID(sceneId)).setSize(width, height));
	}

	public static void setPosition(String sceneId, double posX, double posY) {
		checkScene(sceneID(sceneId));
		Platform.runLater(() -> sceneMap.get(sceneID(sceneId)).setPosition(posX, posY));
	}

	public static void setParent(String sceneId, Parent parent) {
		checkScene(sceneID(sceneId));
		Platform.runLater(() -> sceneMap.get(sceneID(sceneId)).setParent(parent));
	}

	public static void setScene(String sceneId, Scene scene) {
		checkScene(sceneID(sceneId));
		Platform.runLater(() -> sceneMap.get(sceneID(sceneId)).setScene(scene));
	}

	public static void swapScene(String sceneId, Scene scene) {
		checkScene(sceneID(sceneId));
		Platform.runLater(() -> sceneMap.get(sceneID(sceneId)).setScene(scene));
	}

	public static void setTitle(String sceneId, String title) {
		checkScene(sceneID(sceneId));
		Platform.runLater(() -> sceneMap.get(sceneID(sceneId)).setTitle(title));
	}

	public static void setOnKeyPressedEvent(String sceneId, EventHandler<? super KeyEvent> keyPressedEventHandler) {
		checkScene(sceneID(sceneId));
		sceneMap.get(sceneID(sceneId)).setOnKeyPressedEvent(keyPressedEventHandler);
	}

	public static void setOnKeyReleasedEvent(String sceneId, EventHandler<? super KeyEvent> keyReleasedEventHandler) {
		checkScene(sceneID(sceneId));
		sceneMap.get(sceneID(sceneId)).setOnKeyReleasedEvent(keyReleasedEventHandler);
	}

	public static void setOnShowingEvent(String sceneId, EventHandler<WindowEvent> onShowingEventHandler) {
		checkScene(sceneID(sceneId));
		sceneMap.get(sceneID(sceneId)).setOnShowingEvent(onShowingEventHandler);
	}

	public static void setOnShownEvent(String sceneId, EventHandler<WindowEvent> onShownEventHandler) {
		checkScene(sceneID(sceneId));
		sceneMap.get(sceneID(sceneId)).setOnShownEvent(onShownEventHandler);
	}

	public static void setOnHidingEvent(String sceneId, EventHandler<WindowEvent> onHidingEventHandler) {
		checkScene(sceneID(sceneId));
		sceneMap.get(sceneID(sceneId)).setOnHidingEvent(onHidingEventHandler);
	}

	public static void setOnHiddenEvent(String sceneId, EventHandler<WindowEvent> onHiddenEventHandler) {
		checkScene(sceneID(sceneId));
		sceneMap.get(sceneID(sceneId)).setOnHiddenEvent(onHiddenEventHandler);
	}

	public static void setOnCloseEvent(String sceneId, EventHandler<WindowEvent> onCloseEventHandler) {
		checkScene(sceneID(sceneId));
		sceneMap.get(sceneID(sceneId)).setOnCloseEvent(onCloseEventHandler);
	}

	public static void setOnWindowCloseEvent(String sceneId, EventHandler<WindowEvent> onWindowCloseEventHandler) {
		checkScene(sceneID(sceneId));
		sceneMap.get(sceneID(sceneId)).setOnWindowCloseEvent(onWindowCloseEventHandler);
	}

	public static void setStyleSheets(String sceneId, String... styleSheets) {
		checkScene(sceneID(sceneId));
		sceneMap.get(sceneID(sceneId)).setStyleSheets(styleSheets);
	}

	public static void addStyleSheet(String sceneId, String styleSheet) {
		checkScene(sceneID(sceneId));
		sceneMap.get(sceneID(sceneId)).addStyleSheet(styleSheet);
	}

	public static void clearStyleSheets(String sceneId) {
		checkScene(sceneID(sceneId));
		sceneMap.get(sceneID(sceneId)).clearStyleSheets();
	}

	public static void setOnLostFocus(String sceneId, ChangeListener<? super Boolean> lostFocusListener) {
		checkScene(sceneID(sceneId));
		sceneMap.get(sceneID(sceneId)).setOnLostFocus(lostFocusListener);
	}

	public static void setHideOnLostFocus(String sceneId) {
		checkScene(sceneID(sceneId));
		sceneMap.get(sceneID(sceneId)).setHideOnLostFocus(true);
	}

	public static void setHideOnLostFocus(String sceneId, boolean hideOnLostFocus) {
		checkScene(sceneID(sceneId));
		sceneMap.get(sceneID(sceneId)).setHideOnLostFocus(hideOnLostFocus);
	}

	/**
	 * Getters
	 */


	public static Integer getMouseX() {
		return MouseInfo.getPointerInfo().getLocation().x;
	}

	public static Integer getMouseY() {
		return MouseInfo.getPointerInfo().getLocation().y;
	}

	public static Integer[] getMousePointerXY() {
		Point     p  = getMousePointerLocation();
		Integer[] xy = new Integer[2];
		xy[0] = p.x;
		xy[1] = p.y;
		return xy;
	}

	public static Point getMousePointerLocation() {
		return MouseInfo.getPointerInfo().getLocation();
	}

	public static Stage getStage(String sceneId) {
		checkScene(sceneID(sceneId));
		return sceneMap.get(sceneID(sceneId)).getStage();
	}

	public static boolean isShowing(String sceneId) {
		if (sceneMap.containsKey(sceneID(sceneId))) {
			return sceneMap.get(sceneID(sceneId)).getStage().isShowing();
		}
		return false;
	}

	public static Scene getScene(String sceneId) {
		checkScene(sceneID(sceneId));
		return sceneMap.get(sceneID(sceneId)).getScene();
	}

	public static Window getWindow(String sceneId) {
		checkScene(sceneID(sceneId));
		return sceneMap.get(sceneID(sceneId)).getWindow();
	}

	public static boolean sceneExists(String sceneId) {
		return sceneMap.containsKey(sceneID(sceneId));
	}

	public static Window getOwner(String sceneId) {
		checkScene(sceneID(sceneId));
		return sceneMap.get(sceneID(sceneId)).getStage().getOwner();
	}

	public static Dimension getScreenDimensions() {
		return Toolkit.getDefaultToolkit().getScreenSize();
	}

	public static @NotNull Double getScreenWidth() {
		return Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	}

	public static @NotNull Double getScreenHeight() {
		return Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	}

	public static @NotNull Double getWidth(String sceneId) {
		checkScene(sceneID(sceneId));
		return sceneMap.get(sceneID(sceneId)).getWidth();
	}

	public static @NotNull Double getHeight(String sceneId) {
		checkScene(sceneID(sceneId));
		return sceneMap.get(sceneID(sceneId)).getHeight();
	}

	public static @NotNull Double getPosX(String sceneId) {
		checkScene(sceneID(sceneId));
		return sceneMap.get(sceneID(sceneId)).getPosX();
	}

	public static @NotNull Double getPosY(String sceneId) {
		checkScene(sceneID(sceneId));
		return sceneMap.get(sceneID(sceneId)).getPosY();
	}


	/**
	 * Private Methods
	 */

	private static void debug(String message) {
		if(debug) {
			System.out.println(message);
		}
	}

	private static void checkScene(SceneID sceneId) {
		if (!sceneMap.containsKey(sceneId)) {
			throw noSceneError(sceneId.toString());
		}
		else if (sceneMap.get(sceneId) == null) {throw sceneNullError(sceneId.toString());}
	}

	private static @NotNull UnsupportedOperationException noSceneError(String sceneId) {
		String message      = "* Scene " + sceneId + " does not exist, you need to run SceneOne.set(sceneId, Parent).build(); to complete a Scene *";
		String frame        = getFrameFor(message);
		String finalMessage = "\n\n" + frame + "\n" + message + "\n" + frame + "\n";
		return new UnsupportedOperationException(finalMessage);
	}

	private static @NotNull UnsupportedOperationException sceneNullError(String sceneId) {
		String message      = "* Scene " + sceneId + " is NULL, perhaps you removed it before making this call *";
		String frame        = getFrameFor(message);
		String finalMessage = "\n\n" + frame + "\n" + message + "\n" + frame + "\n";
		return new UnsupportedOperationException(finalMessage);
	}

	private static @NotNull UnsupportedOperationException userError(String message) {
		String tempMessage  = "* " + message + " *";
		String frame        = getFrameFor(tempMessage);
		String finalMessage = "\n\n" + frame + "\n" + tempMessage + "\n" + frame + "\n";
		return new UnsupportedOperationException(finalMessage);
	}

	private static @NotNull UnsupportedOperationException centerOnWaitError() {
		String message      = "* When you call showAndWait() while also choosing to center your Scene, you must declare the size(width, height) in your build or set statement *";
		String frame        = getFrameFor(message);
		String finalMessage = "\n\n" + frame + "\n" + message + "\n" + frame + "\n";
		return new UnsupportedOperationException(finalMessage);
	}

	private static @NotNull String getFrameFor(@NotNull String message) {
		int           length = message.length();
		StringBuilder frame  = new StringBuilder();
		for (int x = 0; x < length; x++) {
			frame.append("*");
		}
		return frame.toString();
	}
}
