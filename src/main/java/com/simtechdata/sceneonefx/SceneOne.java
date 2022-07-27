package com.simtechdata.sceneonefx;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.*;
import javafx.stage.Window;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import static com.simtechdata.sceneonefx.SceneOne.SIZE.*;

public class SceneOne {

	enum SIZE {
		WIDTH,
		HEIGHT
	}

	private static final Map<String, SceneObject> sceneMap       = new HashMap<>();
	private static       String                   lastSceneShown = "";
	private static       String                   masterTitle    = "";


	/*Builder Class*/

	public static class Builder {

		public Builder(String sceneId, Parent parent) {
			this.sceneId = sceneId;
			this.parent  = parent;
		}

		public Builder(String sceneId, Parent parent, double width, double height) {
			this.sceneId = sceneId;
			this.parent  = parent;
			this.width   = width;
			this.height  = height;
		}

		public Builder(String sceneId, Scene scene) {
			this.sceneId = sceneId;
			this.scene   = scene;
		}

		public Builder(String sceneId, Scene scene, double width, double height) {
			this.sceneId = sceneId;
			this.scene   = scene;
			this.width   = width;
			this.height  = height;
		}

		private final String                          sceneId;
		private       Scene                           scene;
		private       Parent                          parent;
		private final ObservableList<String>          styleSheets               = FXCollections.observableArrayList();
		private       double                          width                     = -1;
		private       double                          height                    = -1;
		private       StageStyle                      style                     = null;
		private       Modality                        modality                  = null;
		private       boolean                         centered                  = false;
		private       boolean                         alwaysOnTop               = false;
		private       boolean                         hideOnLostFocus           = false;
		private       String                          title                     = masterTitle;
		private       double                          posX                      = -1;
		private       double                          posY                      = -1;
		private       double                          splitFactorX              = -1;
		private       double                          splitFactorY              = -1;
		private       EventHandler<WindowEvent>       onShownEventHandler       = null;
		private       EventHandler<WindowEvent>       onHiddenEventHandler      = null;
		private       EventHandler<WindowEvent>       onShowingEventHandler     = null;
		private       EventHandler<WindowEvent>       onHidingEventHandler      = null;
		private       EventHandler<WindowEvent>       onCloseEventHandler       = null;
		private       EventHandler<WindowEvent>       onWindowCloseEventHandler = null;
		private       ChangeListener<? super Boolean> lostFocusListener         = null;
		private       EventHandler<? super KeyEvent>  keyPressedEventHandler    = null;
		private       EventHandler<? super KeyEvent>  keyReleasedEventHandler   = null;
		private       Stage                           owner                     = null;
		private       Stage                           stage                     = null;

		public Builder owner(Stage owner) {
			if (owner != null) {this.owner = owner;}
			return this;
		}

		public Builder owner(String sceneId) {
			if (sceneId != null) {
				if (sceneMap.containsKey(sceneId)) {this.owner = sceneMap.get(sceneId).getStage();}
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

		public Builder centered(double width, double height) {
			this.centered = true;
			this.width    = width;
			this.height   = height;
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
			sceneMap.remove(sceneId);
			sceneMap.put(sceneId, new SceneObject(this));
		}

		public void show() {
			build();
			sceneMap.get(sceneId).show();
		}

		public void showAndWait() {
			if (centered) {
				if (width < 0 || height < 0) {
					throw centerOnWait();
				}
			}
			build();
			sceneMap.get(sceneId).showAndWait();
		}
	}

	/*SceneObject Class*/

	private static class SceneObject {

		protected static class Size {

			private final Map<SIZE, Double> VALUES = new HashMap<>();

			private final Stage stage;

			public Size(Stage stage, double width, double height) {
				VALUES.put(WIDTH, width);
				VALUES.put(HEIGHT, height);
				this.stage = stage;
			}

			public void set(Double width, Double height) {
				VALUES.replace(WIDTH, width);
				VALUES.replace(HEIGHT, height);
			}

			public void setWidth(Double width) {
				VALUES.replace(WIDTH, width);
			}

			public void setHeight(Double height) {
				VALUES.replace(HEIGHT, height);
			}

			public Double getWidth() {
				return VALUES.get(WIDTH);
			}

			public Double getHalfWidth() {
				return VALUES.get(WIDTH) / 2;
			}

			public Double getHeight() {
				return VALUES.get(HEIGHT);
			}

			public Double getHalfHeight() {
				return VALUES.get(HEIGHT) / 2;
			}

			private boolean XisGreaterThanZero() {
				return VALUES.get(WIDTH) > 0.0;
			}

			private boolean YisGreaterThanZero() {
				return VALUES.get(HEIGHT) > 0.0;
			}

			public boolean notSet() {
				return !isSet();
			}

			public boolean isSet() {
				return XisGreaterThanZero() && YisGreaterThanZero();
			}

			public void resize() {
				if (isSet()) {
					stage.setWidth(VALUES.get(WIDTH));
					stage.setHeight(VALUES.get(HEIGHT));
				}
			}
		}

		private       Stage                           stage;
		private       Scene                           scene;
		private final String                          sceneId;
		private final boolean                         centered;
		private       boolean                         hasShown          = false;
		private       double                          posX;
		private       double                          posY;
		private       double                          splitFactorX;
		private       double                          splitFactorY;
		private       Size                            size;
		private       boolean                         hidden            = false;
		private final ChangeListener<? super Boolean> lostFocusListener = (observable, oldValue, newValue) -> {
			if (!newValue) {
				this.hidden = true;
				Platform.runLater(() -> this.stage.hide());
			}
		};

		public SceneObject(@NotNull Builder build) {
			this.sceneId      = build.sceneId;
			this.scene        = build.scene;
			this.centered     = build.centered;
			this.posX         = build.posX;
			this.posY         = build.posY;
			this.splitFactorX = build.splitFactorX;
			this.splitFactorY = build.splitFactorY;
			this.stage        = build.stage;

			if (stage == null) {stage = new Stage();}

			if (scene == null) {scene = new Scene(build.parent);}
			stage.setScene(scene);
			stage.setTitle(build.title);
			scene.getStylesheets().addAll(build.styleSheets);

			if (build.style != null) stage.initStyle(build.style);
			if (build.modality != null) stage.initModality(build.modality);
			if (build.onShownEventHandler != null) stage.setOnShown(build.onShownEventHandler);
			if (build.onShowingEventHandler != null) stage.setOnShowing(build.onShowingEventHandler);
			if (build.onHiddenEventHandler != null) stage.setOnHidden(build.onHiddenEventHandler);
			if (build.onHidingEventHandler != null) stage.setOnHiding(build.onHidingEventHandler);
			if (build.onCloseEventHandler != null) stage.setOnCloseRequest(build.onCloseEventHandler);
			if (build.onWindowCloseEventHandler != null) scene.getWindow().setOnCloseRequest(build.onWindowCloseEventHandler);
			if (build.keyPressedEventHandler != null) setOnKeyPressed(build.keyPressedEventHandler);
			if (build.keyReleasedEventHandler != null) setOnKeyReleased(build.keyReleasedEventHandler);
			if (build.owner != null) stage.initOwner(build.owner);
			stage.setAlwaysOnTop(build.alwaysOnTop);
			if (build.width != -1 && build.height != -1) {
				size = new Size(stage, build.width, build.height);
			}
			if (build.hideOnLostFocus) {
				stage.focusedProperty().addListener(lostFocusListener);
			}
			else if (build.lostFocusListener != null) {
				stage.focusedProperty().addListener(build.lostFocusListener);
			}
		}

		public void setOnKeyPressedEvent(EventHandler<? super KeyEvent> keyPressedEventHandler) {
			scene.setOnKeyPressed(keyPressedEventHandler);
		}

		public void setOnKeyReleasedEvent(EventHandler<? super KeyEvent> keyReleasedEventHandler) {
			scene.setOnKeyReleased(keyReleasedEventHandler);
		}

		public void setOnShowingEvent(EventHandler<WindowEvent> onShowingEventHandler) {
			stage.setOnShowing(onShowingEventHandler);
		}

		public void setOnShownEvent(EventHandler<WindowEvent> onShownEventHandler) {
			stage.setOnShown(onShownEventHandler);
		}

		public void setOnHidingEvent(EventHandler<WindowEvent> onHidingEventHandler) {
			stage.setOnHiding(onHidingEventHandler);
		}

		public void setOnHiddenEvent(EventHandler<WindowEvent> onHiddenEventHandler) {
			stage.setOnHidden(onHiddenEventHandler);
		}

		public void setOnCloseEvent(EventHandler<WindowEvent> onCloseEventHandler) {
			stage.setOnCloseRequest(onCloseEventHandler);
		}

		public void setOnWindowCloseEvent(EventHandler<WindowEvent> onWindowCloseEventHandler) {
			scene.getWindow().setOnCloseRequest(onWindowCloseEventHandler);
		}

		public void setOnLostFocus(ChangeListener<? super Boolean> lostFocusListener) {
			stage.focusedProperty().removeListener(this.lostFocusListener);
			stage.focusedProperty().addListener(lostFocusListener);
		}

		public void setHideOnLostFocus(boolean hideOnLostFocus) {
			if (hideOnLostFocus) {
				this.stage.focusedProperty().addListener(lostFocusListener);
			}
			else {
				this.stage.focusedProperty().removeListener(lostFocusListener);
			}
		}

		private void findCenter() {
			if (size != null) {
				posX = (Toolkit.getDefaultToolkit().getScreenSize().getWidth() - size.getWidth()) / 2;
				posY = (Toolkit.getDefaultToolkit().getScreenSize().getHeight() - size.getHeight()) / 2;
			}
		}

		private void preProcess() {
			if (size != null) {
				size.resize();
				if (splitFactorX > 0 && splitFactorY > 0) {
					double xSplit = size.getWidth() * splitFactorX;
					double ySplit = size.getHeight() * splitFactorY;
					stage.setX(posX - xSplit);
					stage.setY(posY - ySplit);
				}
				else if (splitFactorX > 0) {
					double xSplit = size.getWidth() * splitFactorX;
					stage.setX(posX - xSplit);
				}
				else if (splitFactorY > 0) {
					double ySplit = size.getHeight() * splitFactorY;
					stage.setY(posY - ySplit);
				}
			}
			if (posX > 0 && posY > 0) {
				stage.setX(posX);
				stage.setY(posY);
			}
		}

		public void reCenterScene() {
			new Thread(() -> {
				double sceneWidth   = scene.getWidth();
				double sceneHeight  = scene.getHeight();
				double screenWidth  = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
				double screenHeight = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
				double posX         = (screenWidth - sceneWidth) - (.5 * (screenWidth - sceneWidth));
				double posY         = (screenHeight - sceneHeight) - (.5 * (screenHeight - sceneHeight));
				Platform.runLater(() -> {
					scene.getWindow().setX(posX);
					scene.getWindow().setY(posY);
				});
			}).start();
		}

		public void show() {
			if (hasShown) {
				reShow();
				return;
			}
			preProcess();
			stage.show();
			if (size == null) {stage.sizeToScene();}
			if (centered) {stage.centerOnScreen();}
			checkSplit();
			hasShown       = true;
			lastSceneShown = this.sceneId;
		}

		public void checkSplit() {
			if (splitFactorX > 0 && splitFactorY > 0) {
				double xSplit = size.getWidth() * splitFactorX;
				double ySplit = size.getHeight() * splitFactorY;
				stage.setX(posX - xSplit);
				stage.setY(posY - ySplit);
			}
			else if (splitFactorX > 0) {
				double xSplit = size.getWidth() * splitFactorX;
				stage.setX(posX - xSplit);
			}
			else if (splitFactorY > 0) {
				double ySplit = size.getHeight() * splitFactorY;
				stage.setY(posY - ySplit);
			}
		}

		public void showAndWait() {
			preProcess();
			if (centered) {findCenter();}
			lastSceneShown = this.sceneId;
			stage.showAndWait();
		}

		public void reShow() {
			if (hasShown) {
				lastSceneShown = this.sceneId;
				stage.show();
				stage.toFront();
			}
		}

		public void showSplit(double X, double Y, double splitFactorX, double splitFactorY) {
			this.posX         = X;
			this.posY         = Y;
			this.splitFactorX = splitFactorX;
			this.splitFactorY = splitFactorY;
			show();
		}

		public void showSplitX(double X, double splitFactorX) {
			this.posX         = X;
			this.splitFactorX = splitFactorX;
			this.splitFactorY = -1;
			show();
		}

		public void showSplitY(double Y, double splitFactorY) {
			this.posY         = Y;
			this.splitFactorY = splitFactorY;
			this.splitFactorX = -1;
			show();
		}

		public void resize(double width, double height) {
			if (size == null) {size = new Size(stage, width, height);}
			else {size.set(width, height);}
			size.resize();
		}

		public void setPosition(double posX, double posY) {
			this.posX = posX;
			this.posY = posY;
			stage.setX(posX);
			stage.setY(posY);
		}

		public void setParent(Parent parent) {
			scene = new Scene(parent);
			stage.setScene(scene);
		}

		public void setScene(Scene scene) {
			this.scene = scene;
			stage.setScene(this.scene);
		}

		public void setTitle(String title) {
			stage.setTitle(title);
		}

		public void setHeight(double height) {
			stage.setHeight(height);
		}

		public void setWidth(double width) {
			stage.setWidth(width);
		}

		public void setOnKeyPressed(EventHandler<? super KeyEvent> keyPressedEventHandler) {
			if (keyPressedEventHandler != null) {scene.setOnKeyPressed(keyPressedEventHandler);}
		}

		public void setOnKeyReleased(EventHandler<? super KeyEvent> keyReleasedEventHandler) {
			if (keyReleasedEventHandler != null) {scene.setOnKeyReleased(keyReleasedEventHandler);}
		}

		public void toggleFullScreen() {
			stage.setFullScreen(!stage.isFullScreen());
		}

		public void hide() {
			stage.hide();
			hidden = true;
		}

		public void unHide() {
			if (hidden) {
				stage.show();
				hidden = false;
			}
		}

		public void center() {
			stage.centerOnScreen();
		}

		public void close() {
			stage.close();
		}

		public void closeIfShowing() {
			if (stage.isShowing()) {stage.close();}
		}

		public void hideIfShowing() {
			if (stage.isShowing()) {stage.hide();}
		}

		public double getWidth() {
			return stage.getWidth();
		}

		public double getHeight() {
			return stage.getHeight();
		}

		public Stage getStage() {
			return stage;
		}

		public Scene getScene() {
			return scene;
		}

		public Window getWindow() {
			return scene.getWindow();
		}

		public void clearStyleSheets() {
			scene.getStylesheets().clear();
		}

		public void addStyleSheet(String styleSheet) {
			scene.getStylesheets().add(styleSheet);
			scene.getRoot().getStylesheets().add(styleSheet);
		}

		public void setStyleSheets(String... styleSheets) {
			scene.getStylesheets().clear();
			scene.getRoot().getStylesheets().clear();
			scene.getStylesheets().addAll(styleSheets);
			scene.getRoot().getStylesheets().addAll(styleSheets);
		}

		public void setStyleSheet(String... styleSheets) {
			if (stage != null) {
				if (stage.isShowing()) {
					scene.getStylesheets().setAll(styleSheets);
				}
			}
		}
	}


	/*Public Methods*/

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

	/*Actions*/

	public static void show(String sceneId) {
		checkScene(sceneId);
		sceneMap.get(sceneId).show();
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

	public static void close(String sceneId) {
		checkScene(sceneId);
		sceneMap.get(sceneId).close();
	}

	public static void closeIfShowing(String sceneId) {
		if (sceneMap.containsKey(sceneId)) {
			sceneMap.get(sceneId).closeIfShowing();
		}
	}

	public static void hideIfShowing(String sceneId) {
		if (sceneMap.containsKey(sceneId)) {
			sceneMap.get(sceneId).hideIfShowing();
		}
	}

	public static void remove(String sceneId) {
		sceneMap.remove(sceneId);
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
		if (!lastSceneShown.isEmpty()) {
			checkScene(lastSceneShown);
			sceneMap.get(lastSceneShown).reShow();
		}
	}

	/*Setters*/

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

	/*Getters*/

	public static boolean lastSceneAvailable() {
		return !lastSceneShown.isEmpty();
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


	/*Private Methods*/

	private static void checkScene(String sceneId) {
		if (!sceneMap.containsKey(sceneId)) {
			throw noSceneError(sceneId);
		}
	}

	private static @NotNull UnsupportedOperationException noSceneError(String sceneId) {
		String message      = "* Scene " + sceneId + " does not exist, you need to run SceneOne.set(sceneId, Parent).build(); to complete a Scene *";
		String frame        = getBoardersFor(message);
		String finalMessage = "\n\n" + frame + "\n" + message + "\n" + frame + "\n";
		return new UnsupportedOperationException(finalMessage);
	}

	private static @NotNull UnsupportedOperationException centerOnWait() {
		String message      = "* When you call showAndWait() while also choosing to center your Scene, you must use centered(width, height) in your build statement *";
		String frame        = getBoardersFor(message);
		String finalMessage = "\n\n" + frame + "\n" + message + "\n" + frame + "\n";
		return new UnsupportedOperationException(finalMessage);
	}

	private static @NotNull String getBoardersFor(@NotNull String message) {
		int           length = message.length();
		StringBuilder frame  = new StringBuilder();
		for (int x = 0; x < length; x++) {
			frame.append("*");
		}
		return frame.toString();
	}
}
