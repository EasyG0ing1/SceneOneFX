package com.simtechdata.sceneonefx;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.*;
import javafx.stage.Window;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import static com.simtechdata.sceneonefx.SceneOne.SIZE.*;

public class SceneOne {

	enum SIZE {
		WIDTH,
		HEIGHT
	}

	private static final Dimension screenDimensions = Toolkit.getDefaultToolkit().getScreenSize();

	private static final Map<String,SceneObject> sceneMap = new HashMap<>();


	/**
	 * Public Methods
	 */

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

		private final String                         sceneId;
		private final Parent                         parent;
		private final ObservableList<String>         styleSheets             = FXCollections.observableArrayList();
		private       double                         width                   = -1;
		private       double                         height                  = -1;
		private       StageStyle                     style                   = null;
		private       Modality                       modality                = null;
		private       boolean                        centered                = false;
		private       boolean                        alwaysOnTop             = false;
		private       String                         title                   = "";
		private       double                         posX                    = -1;
		private       double                         posY                    = -1;
		private       EventHandler<WindowEvent>      onShownEventHandler     = null;
		private       EventHandler<WindowEvent>      onHiddenEventHandler    = null;
		private       EventHandler<WindowEvent>      onShowingEventHandler   = null;
		private       EventHandler<WindowEvent>      onHidingEventHandler    = null;
		private       EventHandler<WindowEvent>      onCloseEventHandler     = null;
		private       EventHandler<? super KeyEvent> keyPressedEventHandler  = null;
		private       EventHandler<? super KeyEvent> keyReleasedEventHandler = null;
		private       Stage                          owner                   = null;

		public Builder owner(Stage owner) {
			if(owner != null)
				this.owner = owner;
			return this;
		}

		public Builder owner(String sceneId) {
			if(sceneMap.containsKey(sceneId))
				this.owner = sceneMap.get(sceneId).getStage();
			else
				throw noSceneError(sceneId);
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
			this.width = width;
			this.height = height;
			return this;
		}

		public Builder position(double posX, double posY) {
			this.posX = posX;
			this.posY = posY;
			return this;
		}

		public Builder initStyle(StageStyle style) {
			if (style != null)
				this.style = style;
			return this;
		}

		public Builder styleSheets(String... styleSheets) {
			if(styleSheets != null)
				this.styleSheets.addAll(styleSheets);
			return this;
		}

		public Builder modality(Modality modality) {
			if(modality != null)
				this.modality = modality;
			return this;
		}

		public Builder title(String title) {
			if (title != null)
				if(!title.isEmpty())
					this.title = title;
			return this;
		}

		public Builder size(double width, double height) {
			this.width = width;
			this.height = height;
			return this;
		}

		public Builder onKeyPressed(EventHandler<? super KeyEvent> keyPressedEventHandler) {
			if(keyPressedEventHandler != null)
				this.keyPressedEventHandler = keyPressedEventHandler;
			return this;
		}

		public Builder onKeyReleased(EventHandler<? super KeyEvent> keyReleasedEventHandler) {
			if(keyReleasedEventHandler != null)
				this.keyReleasedEventHandler = keyReleasedEventHandler;
			return this;
		}

		public Builder onShownEvent(EventHandler<WindowEvent> onShownEventHandler) {
			if(onShownEventHandler != null)
				this.onShownEventHandler = onShownEventHandler;
			return this;
		}

		public Builder onHiddenEvent(EventHandler<WindowEvent> onHiddenEventHandler) {
			if(onHiddenEventHandler != null)
				this.onHiddenEventHandler = onHiddenEventHandler;
			return this;
		}

		public Builder onShowingEvent(EventHandler<WindowEvent> onShowingEventHandler) {
			if(onShowingEventHandler != null)
				this.onShowingEventHandler = onShowingEventHandler;
			return this;
		}

		public Builder onHidingEvent(EventHandler<WindowEvent> onHidingEventHandler) {
			if(onHidingEventHandler != null)
				this.onHidingEventHandler = onHidingEventHandler;
			return this;
		}

		public Builder onCloseEvent(EventHandler<WindowEvent> onCloseEventHandler) {
			if(onCloseEventHandler != null)
				this.onCloseEventHandler = onCloseEventHandler;
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
			SceneObject sceneObject = new SceneObject(this);
			sceneMap.put(sceneId, sceneObject);
		}

		public void show() {
			build();
			sceneMap.get(sceneId).show();
		}

		public void showAndWait() {
			if(centered) {
				if(width < 0 || height < 0) {
					throw centerOnWait();
				}
			}
			build();
			sceneMap.get(sceneId).showAndWait();
		}
	}

	public static Builder set(String sceneId, Parent parent) {
		return new Builder(sceneId, parent);
	}

	public static Builder set(String sceneId, Parent parent, double width, double height) {
		return new Builder(sceneId, parent, width, height);
	}

	public static void toggleFullScreen(String sceneId) {
		checkScene(sceneId);
		sceneMap.get(sceneId).toggleFullScreen();
	}

	/**
	 * Actions
	 */

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
		sceneMap.get(sceneId).resize(width,height);
	}

	public static void close(String sceneId) {
		checkScene(sceneId);
		sceneMap.get(sceneId).close();
	}

	public static void closeIfShowing(String sceneId) {
		if(sceneMap.containsKey(sceneId)) {
			sceneMap.get(sceneId).closeIfShowing();
		}
	}

	public static void hideIfShowing(String sceneId) {
		if(sceneMap.containsKey(sceneId)) {
			sceneMap.get(sceneId).hideIfShowing();
		}
	}

	public static void remove(String sceneId) {
		checkScene(sceneId);
		sceneMap.get(sceneId).destroy();
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

	/**
	 * Setters
	 */

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

	public static void setTitle(String sceneId, String title) {
		checkScene(sceneId);
		sceneMap.get(sceneId).setTitle(title);
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

	public static void setOnKeyPressed(String sceneName, EventHandler<? super KeyEvent> keyPressedEventHandler) {
		checkScene(sceneName);
		sceneMap.get(sceneName).setOnKeyPressed(keyPressedEventHandler);
	}

	public static void setOnKeyReleased(String sceneName, EventHandler<? super KeyEvent> keyReleasedEventHandler) {
		checkScene(sceneName);
		sceneMap.get(sceneName).setOnKeyReleased(keyReleasedEventHandler);
	}

	public static void setStyleSheets(String sceneId, String... styleSheets) {
		checkScene(sceneId);
		sceneMap.get(sceneId).setStyleSheets(styleSheets);
	}

	public static boolean isShowing(String sceneId) {
		checkScene(sceneId);
		return sceneMap.get(sceneId).getStage().isShowing();
	}

	/**
	 * Getters
	 */

	public static Stage getStage(String sceneId) {
		checkScene(sceneId);
		return sceneMap.get(sceneId).getStage();
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
		return screenDimensions;
	}

	public static Double getScreenWidth() {
		return screenDimensions.getWidth();
	}

	public static Double getScreenHeight() {
		return screenDimensions.getHeight();
	}

	public static Double getWidth(String sceneId) {
		checkScene(sceneId);
		return sceneMap.get(sceneId).getWidth();
	}

	public static Double getHeight(String sceneId) {
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

	private static UnsupportedOperationException noSceneError(String sceneId) {
		String message = "* Scene " + sceneId + " does not exist, you need to run SceneOne.set(sceneId, Parent).build(); to complete a Scene *";
		String frame = getBoardersFor(message);
		String finalMessage = "\n\n"+ frame + "\n" + message + "\n" + frame + "\n";
		return new UnsupportedOperationException(finalMessage);
	}

	private static UnsupportedOperationException centerOnWait() {
		String message = "* When you call showAndWait() while also choosing to center your Scene, you must use centered(width, height) in your build statement *";
		String frame = getBoardersFor(message);
		String finalMessage = "\n\n"+ frame + "\n" + message + "\n" + frame + "\n";
		return new UnsupportedOperationException(finalMessage);
	}

	private static String getBoardersFor(String message) {
		int length = message.length();
		StringBuilder frame = new StringBuilder();
		for (int x = 0; x < length; x++) {
			frame.append("*");
		}
		return frame.toString();
	}

	private static class SceneObject {

		protected static class Size {

			private final Map<SIZE,Double> VALUES = new HashMap<>();

			public Size(double width, double height) {
				VALUES.put(WIDTH,width);
				VALUES.put(HEIGHT,height);
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

			public void resize(Stage stage) {
				if(isSet()) {
					stage.setWidth(VALUES.get(WIDTH));
					stage.setHeight(VALUES.get(HEIGHT));
				}
			}
		}

		Dimension screenDimensions = Toolkit.getDefaultToolkit().getScreenSize();
		private final double  screenWidth  = screenDimensions.getWidth();
		private final double  screenHeight = screenDimensions.getHeight();
		private       Stage   stage;
		private final boolean centered;
		private       double  posX;
		private       double  posY;
		private       Size    size;
		private       Scene   scene;
		private       boolean hidden       = false;

		public SceneObject(Builder build) {
			this.centered = build.centered;
			this.posX     = build.posX;
			this.posY     = build.posY;

			stage = new Stage();
			scene = new Scene(build.parent);
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
			if (build.keyPressedEventHandler != null) setOnKeyPressed(build.keyPressedEventHandler);
			if (build.keyReleasedEventHandler != null) setOnKeyReleased(build.keyReleasedEventHandler);

			if (build.owner != null) stage.initOwner(build.owner);
			stage.setAlwaysOnTop(build.alwaysOnTop);

			if (build.width != -1 && build.height != -1) {
				size = new Size(build.width, build.height);
			}
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

		private void findCenter() {
			if(size != null) {
				posX = (screenWidth - size.getWidth()) / 2;
				posY = (screenHeight - size.getHeight()) / 2;
			}
		}

		private void preProcess() {
			if (size != null) {
				size.resize(stage);
			}
			if(posX > 0 && posY > 0) {
				stage.setX(posX);
				stage.setY(posY);
			}
		}

		private void postProcess() {
			if(size == null) {
				stage.sizeToScene();
			}
			if(centered)
				stage.centerOnScreen();
		}

		public void reCenterScene() {
			new Thread(() -> {
				double sceneWidth = scene.getWidth();
				double sceneHeight = scene.getHeight();
				double screenWidth = screenDimensions.getWidth();
				double screenHeight = screenDimensions.getHeight();
				double posX = (screenWidth - sceneWidth) - (.5 * (screenWidth - sceneWidth));
				double posY = (screenHeight - sceneHeight) - (.5 * (screenHeight - sceneHeight));
				Platform.runLater(() -> {
					scene.getWindow().setX(posX);
					scene.getWindow().setY(posY);
				});
			}).start();
		}

		public void show() {
			preProcess();
			stage.show();
			postProcess();
		}

		public void showAndWait() {
			if (centered)
				findCenter();
			preProcess();
			stage.showAndWait();
		}

		public void resize(double width, double height) {
			if(size == null)
				size = new Size(width,height);
			else
				size.set(width,height);
			size.resize(stage);
			postProcess();
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
			postProcess();
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
			if(keyPressedEventHandler != null)
				scene.setOnKeyPressed(keyPressedEventHandler);
		}

		public void setOnKeyReleased(EventHandler<? super KeyEvent> keyReleasedEventHandler) {
			if(keyReleasedEventHandler != null)
				scene.setOnKeyReleased(keyReleasedEventHandler);
		}

		public void toggleFullScreen() {
			stage.setFullScreen(!stage.isFullScreen());
		}

		public void hide() {
			stage.hide();
			hidden = true;
		}

		public void unHide() {
			if(hidden) {
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
			if(stage.isShowing())
				stage.close();
		}

		public void hideIfShowing() {
			if(stage.isShowing())
				stage.hide();
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

		public void setStyleSheets(String... styleSheets) {
			scene.getStylesheets().clear();
			scene.getStylesheets().addAll(styleSheets);
		}

		public void destroy() {
			stage.close();
			stage = null;
			scene = null;
		}
	}
}
