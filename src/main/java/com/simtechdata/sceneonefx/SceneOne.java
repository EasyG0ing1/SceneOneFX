package com.simtechdata.sceneonefx;

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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class SceneOne {

	enum DIMENSION {
		WIDTH,
		HEIGHT,
		ALL
	}

	private static final Map<String, SceneObject> sceneMap           = new HashMap<>();
	static final         LinkedList<String>       lastSceneShown     = new LinkedList<>();
	private static       String                   masterTitle        = "";
	private static       boolean disableNotice          = false;
	private static       boolean makeNewStageEveryScene = false;

	/**
	 * Builder Class
	 */

	public static class Builder {

		public Builder(String sceneId, Parent parent) {
			if(sceneExists(sceneId)) remove(sceneId);
			this.sceneId = sceneId;
			this.parent  = parent;
		}

		public Builder(String sceneId, Parent parent, double width, double height) {
			if(sceneExists(sceneId)) remove(sceneId);
			this.sceneId = sceneId;
			this.parent  = parent;
			this.width   = width;
			this.height  = height;
		}

		public Builder(String sceneId, Scene scene) {
			if(sceneExists(sceneId)) remove(sceneId);
			this.sceneId = sceneId;
			this.scene   = scene;
		}

		public Builder(String sceneId, Scene scene, double width, double height) {
			if(sceneExists(sceneId)) remove(sceneId);
			this.sceneId = sceneId;
			this.scene   = scene;
			this.width   = width;
			this.height  = height;
		}

		final String sceneId;
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
		private boolean addStage = false;

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
				if (sceneMap.containsKey(sceneId)) {
					this.owner = sceneMap.get(sceneId).getStage();
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

		public void build() {
			boolean sceneHasStage = Stages.hasStage(sceneId);
			sceneMap.remove(sceneId);
			if (!sceneHasStage && (addStage || SceneOne.makeNewStageEveryScene)) {
				stage = new Stage();
				Stages.addStage(sceneId, stage);
			}
			else if (sceneHasStage && (addStage || SceneOne.makeNewStageEveryScene)) {
				Stages.removeScene(sceneId);
				stage = Stages.getDefaultStage();
				Stages.addStage(sceneId, stage);
			}
			else if (sceneHasStage) {
				stage = Stages.getStage(sceneId);
			}
			else {
				stage = Stages.getDefaultStage();
				Stages.addStage(sceneId, stage);
			}
			sceneMap.put(sceneId, new SceneObject(this));
			if (!disableNotice) {
				System.out.println("SceneOneFX now switches out your Scenes onto a single stage. See README at https://github.com/EasyG0ing1/SceneOneFX\nDisable this notice by calling SceneOne.disableNotice() once.");
			}
		}

		public void show() {
			build();
			Platform.runLater(() -> sceneMap.get(sceneId).show());
		}

		public void showAndWait() {
			if (centered) {
				if (width < 0 || height < 0) throw centerOnWaitError();
			}
			build();
			Platform.runLater(() -> sceneMap.get(sceneId).showAndWait());
		}
	}

	/**
	 * Public Methods
	 */

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

	public static void toggleFullScreen(String sceneId) {
		checkScene(sceneId);
		sceneMap.get(sceneId).toggleFullScreen();
	}

	/**
	 * Actions
	 */

	public static void disableNotice() {
		disableNotice = true;
	}

	public static void show(String sceneId) {
		checkScene(sceneId);
		Platform.runLater(() -> sceneMap.get(sceneId).show());
	}

	public static void showAndWait(String sceneId) {
		checkScene(sceneId);
		sceneMap.get(sceneId).showAndWait();
	}

	public static void hide(String sceneId) {
		checkScene(sceneId);
		sceneMap.get(sceneId).hide();
	}

	public static void unHide(String sceneId) {
		checkScene(sceneId);
		sceneMap.get(sceneId).unHide();
	}

	public static void reSize(String sceneId, double width, double height) {
		checkScene(sceneId);
		sceneMap.get(sceneId).resize(width, height);
	}

	public static void closeStage(String sceneId) {
		checkScene(sceneId);
		sceneMap.get(sceneId).close();
	}

	public static void close(String sceneId) {
		checkScene(sceneId);
		if (Stages.sceneHasHistory(sceneId)) {sceneMap.get(Stages.getLastShownScene(sceneId)).reShow();}
		else {sceneMap.get(sceneId).close();}
	}

	public static void closeIfShowing(String sceneId) {
		checkScene(sceneId);
		if (isShowing(sceneId)) {close(sceneId);}
	}

	public static void closeStageIfShowing(String sceneId) {
		checkScene(sceneId);
		if (isShowing(sceneId)) {sceneMap.get(sceneId).close();}
	}

	public static void hideIfShowing(String sceneId) {
		if (sceneMap.containsKey(sceneId)) {
			sceneMap.get(sceneId).hideIfShowing();
		}
	}

	public static void remove(String sceneId) {
		if (sceneMap.containsKey(sceneId)) {
			sceneMap.get(sceneId).close();
			sceneMap.remove(sceneId);
			Stages.removeScene(sceneId);
		}
	}

	public static void center(String sceneId) {
		checkScene(sceneId);
		sceneMap.get(sceneId).center();
	}

	public static void reCenterScene(String sceneId) {
		checkScene(sceneId);
		sceneMap.get(sceneId).reCenterScene();
	}

	public static void reShow(String sceneId) {
		checkScene(sceneId);
		sceneMap.get(sceneId).reShow();
	}

	public static void showSplitXY(String sceneId, double mouseX, double mouseY, double factorX, double factorY) {
		checkScene(sceneId);
		sceneMap.get(sceneId).showSplit(mouseX, mouseY, factorX, factorY);
	}

	public static void showSplitX(String sceneId, double mouseX, double factorX) {
		checkScene(sceneId);
		sceneMap.get(sceneId).showSplitX(mouseX, factorX);
	}

	public static void showSplitY(String sceneId, double mouseY, double factorY) {
		checkScene(sceneId);
		sceneMap.get(sceneId).showSplitY(mouseY, factorY);
	}

	public static void showLastScene() {
		if (lastSceneShown.size() > 1) {
			lastSceneShown.removeLast();
			String sceneId = lastSceneShown.getLast();
			checkScene(sceneId);
			sceneMap.get(sceneId).reShow();
		}
		else {throw userError("No last Scene to show, verify first by using lastSceneAvailable()");}
	}

	public static Integer askYesNo(String parentSceneId, String question, double width, double height) {
		return askYesNo(parentSceneId, question, width, height, Pos.CENTER, true);
	}

	public static Integer askYesNo(String parentSceneId, String question, double width, double height, Pos textAlignment) {
		return askYesNo(parentSceneId, question, width, height, textAlignment, true);
	}

	public static Integer askYesNo(String parentSceneId, String question, double width, double height, Pos textAlignment, boolean wrapText) {
		checkScene(parentSceneId);
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

	public static void showMessage(String parentSceneId, double width, double height, String message, boolean wrapText, Pos textAlignment) {
		checkScene(parentSceneId);
		String sid   = "SceneOneMessage";
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
		SceneOne.set(sid, vbox, width, height).newStage().centered().owner(parentSceneId).alwaysOnTop().build();
		SceneOne.showAndWait(sid);
		SceneOne.remove(sid);
	}

	public static Integer choiceResponse(String parentSceneId, String question, double width, double height, String... buttonText) {
		return choiceResponse(parentSceneId, question, width, height, Pos.CENTER, true, buttonText);
	}

	public static Integer choiceResponse(String parentSceneId, String question, double width, double height, Pos textAlignment, boolean wrapText, String... buttonText) {
		checkScene(parentSceneId);
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
		for (String sceneId : sceneMap.keySet()) {
			SceneObject so = sceneMap.get(sceneId);
			if (so != null) {
				so.setStyleSheets(styleSheets);
			}
		}
	}

	public static void setHeight(String sceneId, double height) {
		checkScene(sceneId);
		sceneMap.get(sceneId).setHeight(height);
	}

	public static void setWidth(String sceneId, double width) {
		checkScene(sceneId);
		sceneMap.get(sceneId).setWidth(width);
	}

	public static void setSize(String sceneId, double width, double height) {
		checkScene(sceneId);
		sceneMap.get(sceneId).setSize(width, height);
	}

	public static void setPosition(String sceneId, double posX, double posY) {
		checkScene(sceneId);
		sceneMap.get(sceneId).setPosition(posX, posY);
	}

	public static void setParent(String sceneId, Parent parent) {
		checkScene(sceneId);
		sceneMap.get(sceneId).setParent(parent);
	}

	public static void setScene(String sceneId, Scene scene) {
		checkScene(sceneId);
		sceneMap.get(sceneId).setScene(scene);
	}

	public static void swapScene(String sceneId, Scene scene) {
		checkScene(sceneId);
		sceneMap.get(sceneId).setScene(scene);
	}

	public static void setTitle(String sceneId, String title) {
		checkScene(sceneId);
		sceneMap.get(sceneId).setTitle(title);
	}

	public static void setOnKeyPressedEvent(String sceneId, EventHandler<? super KeyEvent> keyPressedEventHandler) {
		checkScene(sceneId);
		sceneMap.get(sceneId).setOnKeyPressedEvent(keyPressedEventHandler);
	}

	public static void setOnKeyReleasedEvent(String sceneId, EventHandler<? super KeyEvent> keyReleasedEventHandler) {
		checkScene(sceneId);
		sceneMap.get(sceneId).setOnKeyReleasedEvent(keyReleasedEventHandler);
	}

	public static void setOnShowingEvent(String sceneId, EventHandler<WindowEvent> onShowingEventHandler) {
		checkScene(sceneId);
		sceneMap.get(sceneId).setOnShowingEvent(onShowingEventHandler);
	}

	public static void setOnShownEvent(String sceneId, EventHandler<WindowEvent> onShownEventHandler) {
		checkScene(sceneId);
		sceneMap.get(sceneId).setOnShownEvent(onShownEventHandler);
	}

	public static void setOnHidingEvent(String sceneId, EventHandler<WindowEvent> onHidingEventHandler) {
		checkScene(sceneId);
		sceneMap.get(sceneId).setOnHidingEvent(onHidingEventHandler);
	}

	public static void setOnHiddenEvent(String sceneId, EventHandler<WindowEvent> onHiddenEventHandler) {
		checkScene(sceneId);
		sceneMap.get(sceneId).setOnHiddenEvent(onHiddenEventHandler);
	}

	public static void setOnCloseEvent(String sceneId, EventHandler<WindowEvent> onCloseEventHandler) {
		checkScene(sceneId);
		sceneMap.get(sceneId).setOnCloseEvent(onCloseEventHandler);
	}

	public static void setOnWindowCloseEvent(String sceneId, EventHandler<WindowEvent> onWindowCloseEventHandler) {
		checkScene(sceneId);
		sceneMap.get(sceneId).setOnWindowCloseEvent(onWindowCloseEventHandler);
	}

	public static void setStyleSheets(String sceneId, String... styleSheets) {
		checkScene(sceneId);
		sceneMap.get(sceneId).setStyleSheets(styleSheets);
	}

	public static void addStyleSheet(String sceneId, String styleSheet) {
		checkScene(sceneId);
		sceneMap.get(sceneId).addStyleSheet(styleSheet);
	}

	public static void clearStyleSheets(String sceneId) {
		checkScene(sceneId);
		sceneMap.get(sceneId).clearStyleSheets();
	}

	public static void setOnLostFocus(String sceneId, ChangeListener<? super Boolean> lostFocusListener) {
		checkScene(sceneId);
		sceneMap.get(sceneId).setOnLostFocus(lostFocusListener);
	}

	public static void setHideOnLostFocus(String sceneId) {
		checkScene(sceneId);
		sceneMap.get(sceneId).setHideOnLostFocus(true);
	}

	public static void setHideOnLostFocus(String sceneId, boolean hideOnLostFocus) {
		checkScene(sceneId);
		sceneMap.get(sceneId).setHideOnLostFocus(hideOnLostFocus);
	}

	/**
	 * Getters
	 */

	public static boolean lastSceneAvailable() {
		LinkedList<String> newList = new LinkedList<>(lastSceneShown);
		if (newList.size() > 1) {
			newList.removeLast();
			return sceneMap.containsKey(newList.getLast());
		}
		return false;
	}

	public static Stage getStage(String sceneId) {
		checkScene(sceneId);
		return sceneMap.get(sceneId).getStage();
	}

	public static boolean isShowing(String sceneId) {
		checkScene(sceneId);
		return sceneMap.get(sceneId).getStage().isShowing();
	}

	public static Scene getScene(String sceneId) {
		checkScene(sceneId);
		return sceneMap.get(sceneId).getScene();
	}

	public static Window getWindow(String sceneId) {
		checkScene(sceneId);
		return sceneMap.get(sceneId).getWindow();
	}

	public static boolean sceneExists(String sceneId) {
		return sceneMap.containsKey(sceneId);
	}

	public static Window getOwner(String sceneId) {
		checkScene(sceneId);
		return sceneMap.get(sceneId).getStage().getOwner();
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
		checkScene(sceneId);
		return sceneMap.get(sceneId).getWidth();
	}

	public static @NotNull Double getHeight(String sceneId) {
		checkScene(sceneId);
		return sceneMap.get(sceneId).getHeight();
	}


	/**
	 * Private Methods
	 */

	private static void checkScene(String sceneId) {
		if (!sceneMap.containsKey(sceneId)) {
			throw noSceneError(sceneId);
		}
	}

	private static @NotNull UnsupportedOperationException noSceneError(String sceneId) {
		String message      = "* Scene " + sceneId + " does not exist, you need to run SceneOne.set(sceneId, Parent).build(); to complete a Scene *";
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
