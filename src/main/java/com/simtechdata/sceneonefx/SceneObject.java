package com.simtechdata.sceneonefx;

import com.simtechdata.sceneonefx.id.SceneID;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.simtechdata.sceneonefx.SceneOne.DIMENSION.*;

public class SceneObject {

	public SceneObject(@NotNull SceneOne.Builder build) {
		this.sceneId      = build.sceneID;
		this.scene        = build.scene;
		this.centered     = build.centered;
		this.posX         = build.posX;
		this.posY         = build.posY;
		this.splitFactorX = build.splitFactorX;
		this.splitFactorY = build.splitFactorY;
		this.stage        = build.stage;
		this.width        = build.width;
		this.height       = build.height;
		this.reSizable    = build.reSizable;

		stage.setScene(scene);
		stage.setTitle(build.title);
		scene.getStylesheets().addAll(build.styleSheets);

		if (build.onShownEventHandler != null) stage.setOnShown(build.onShownEventHandler);
		if (build.onShowingEventHandler != null) stage.setOnShowing(build.onShowingEventHandler);
		if (build.onHiddenEventHandler != null) stage.setOnHidden(build.onHiddenEventHandler);
		if (build.onHidingEventHandler != null) stage.setOnHiding(build.onHidingEventHandler);
		if (build.onCloseEventHandler != null) stage.setOnCloseRequest(build.onCloseEventHandler);
		if (build.onWindowCloseEventHandler != null) scene.getWindow().setOnCloseRequest(build.onWindowCloseEventHandler);
		if (build.keyPressedEventHandler != null) setOnKeyPressed(build.keyPressedEventHandler);
		if (build.keyReleasedEventHandler != null) setOnKeyReleased(build.keyReleasedEventHandler);
		stage.setAlwaysOnTop(build.alwaysOnTop);

		if (build.width > 0 && build.height > 0) {
			size = new Size(stage, build.width, build.height);
		}
		else if (build.width > 0) {
			size = new Size(stage, WIDTH, build.width);
		}
		else if (build.height > 0) {
			size = new Size(stage, HEIGHT, build.height);
		}

		if (build.hideOnLostFocus) {
			stage.focusedProperty().addListener(lostFocusListener);
		}
		else if (build.lostFocusListener != null) {
			stage.focusedProperty().addListener(build.lostFocusListener);
		}

		stage.setFullScreen(build.fullScreen);
	}

	protected static class Size {

		protected static class User {

			public User(SceneOne.DIMENSION dimension, double value) {
				switch (dimension) {
					case WIDTH:
						this.width = value;
					case HEIGHT:
						this.height = value;
				}
			}

			public User(double width, double height) {
				this.width  = width;
				this.height = height;
			}

			private double width  = -1;
			private double height = -1;

			public boolean setWidth() {
				return this.width > 0;
			}

			public boolean setHeight() {
				return this.height > 0;
			}

		}

		private final ConcurrentHashMap<SceneOne.DIMENSION, Double> VALUES = new ConcurrentHashMap<>();

		private boolean userSetAll    = false;
		private boolean userSetWidth  = false;
		private boolean userSetHeight = false;

		private final Stage stage;

		public Size(Stage stage, double width, double height) {
			VALUES.put(WIDTH, width);
			VALUES.put(HEIGHT, height);
			this.stage = stage;
			userSetAll = true;
		}

		public Size(Stage stage, SceneOne.DIMENSION dimension, double value) {
			VALUES.put(WIDTH, -1d);
			VALUES.put(HEIGHT, -1d);
			userSetWidth  = dimension == WIDTH;
			userSetHeight = dimension == HEIGHT;
			switch (dimension) {
				case WIDTH:
					VALUES.put(WIDTH, value);
				case HEIGHT:
					VALUES.put(HEIGHT, value);
			}
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

		public void setSize(Double width, Double height) {
			VALUES.replace(WIDTH, width);
			VALUES.replace(HEIGHT, height);
		}

		public Double getWidth() {
			if (VALUES.get(WIDTH) == -1) {
				if (stage.getScene().getWindow() != null) {
					VALUES.put(WIDTH, stage.getScene().getWindow().getWidth());
				}
				if (stage.getScene() != null) {
					VALUES.put(WIDTH, stage.getScene().getWidth());
				}
				else {
					VALUES.put(WIDTH, stage.getWidth());
				}
			}
			return VALUES.get(WIDTH);
		}

		public Double getHalfWidth() {
			return getWidth() / 2;
		}

		public Double getHeight() {
			if (VALUES.get(HEIGHT) == -1) {
				if (stage.getScene().getWindow() != null) {
					VALUES.put(HEIGHT, stage.getScene().getWindow().getHeight());
				}
				if (stage.getScene() != null) {
					VALUES.put(HEIGHT, stage.getScene().getHeight());
				}
				else {
					VALUES.put(HEIGHT, stage.getHeight());
				}
			}
			return VALUES.get(HEIGHT);
		}

		public Double getHalfHeight() {
			return getHeight() / 2;
		}

		private boolean widthSet() {
			return getWidth() > 0.0;
		}

		private boolean heightSet() {
			return getHeight() > 0.0;
		}

		public boolean notSet() {
			return !isFullySized();
		}

		public boolean isFullySized() {
			return widthSet() && heightSet();
		}

		private void sizeStage(SceneOne.DIMENSION dimension) {
			switch (dimension) {
				case WIDTH:
					stage.setWidth(getWidth());
				case HEIGHT:
					stage.setHeight(getHeight());
				case ALL: {
					stage.setWidth(getWidth());
					stage.setHeight(getHeight());
				}
			}
		}

		private void sizeWindow(SceneOne.DIMENSION dimension) {
			if (stage.getScene().getWindow() != null) {
				switch (dimension) {
					case WIDTH:
						stage.getScene().getWindow().setWidth(getWidth());
					case HEIGHT:
						stage.getScene().getWindow().setHeight(getHeight());
					case ALL: {
						stage.getScene().getWindow().setWidth(getWidth());
						stage.getScene().getWindow().setHeight(getHeight());
					}
				}
			}
		}

		public void resize() {
			if (isFullySized()) {
				Platform.runLater(() -> {
					sizeStage(ALL);
					sizeWindow(ALL);
				});
			}
			else if (widthSet()) {
				Platform.runLater(() -> {
					sizeStage(WIDTH);
					sizeWindow(WIDTH);
				});
			}
			else if (heightSet()) {
				Platform.runLater(() -> {
					sizeStage(HEIGHT);
					sizeWindow(HEIGHT);
				});
			}
		}
	}

	private       Stage                           stage;
	private       Scene                           scene;
	private final SceneID                         sceneId;
	private final boolean                         centered;
	private       boolean                         hasShown          = false;
	private       double                          posX;
	private       double                          posY;
	private       double                          splitFactorX;
	private       double                          splitFactorY;
	private final double                          width;
	private final double                          height;
	private       Size                            size;
	private       boolean                         hidden            = false;
	private final boolean                         reSizable;
	private final ChangeListener<? super Boolean> lostFocusListener = (observable, oldValue, newValue) -> {
		if (!newValue) {
			this.hidden = true;
			Platform.runLater(() -> this.stage.hide());
		}
	};

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
		double screenWidth  = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		double screenHeight = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		posX = (screenWidth / 2) - (size.getWidth() / 2);
		posY = (screenHeight / 2) - (size.getHeight() / 2);
	}

	private void preProcess() {
		if (size != null) {
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
		stage.setScene(scene);
		stage.setWidth(size.getWidth());
		stage.setHeight(size.getHeight());
		if (hasShown) {
			reShow();
			return;
		}
		preProcess();
		stage.show();
		if (size == null) {stage.sizeToScene();}
		if (centered) {stage.centerOnScreen();}
		checkSplit();
		hasShown = true;
		stage.setResizable(reSizable);
	}

	public void showAndWait() {
		stage.setScene(scene);
		preProcess();
		if (centered) {
			if (width < 0 || height < 0) {
				throw centerOnWaitError();
			}
			findCenter();
		}
		stage.showAndWait();
	}

	public void reShow() {
		if (hasShown) {
			stage.setScene(scene);
			if (centered) {
				stage.centerOnScreen();
			}
			stage.show();
			stage.toFront();
		}
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
		scene = null;
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

	public void setWidth(double width) {
		if (size == null) {size = new Size(stage, WIDTH, width);}
		else {size.setWidth(width);}
		size.resize();
	}

	public void setHeight(double height) {
		if (size == null) {size = new Size(stage, HEIGHT, height);}
		else {size.setHeight(height);}
		size.resize();
	}

	public void setSize(double width, double height) {
		if (size == null) {size = new Size(stage, width, height);}
		else {size.setSize(width, height);}
		size.resize();
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
		try {
			scene.getStylesheets().clear();
		}
		catch(IllegalStateException ignored) {}
	}

	public void addStyleSheet(String styleSheet) {
		try {
			scene.getStylesheets().add(styleSheet);
			scene.getRoot().getStylesheets().add(styleSheet);
		}
		catch(IllegalStateException ignored) {}
	}

	public void setStyleSheets(String... styleSheets) {
		try {
			scene.getStylesheets().clear();
			scene.getRoot().getStylesheets().clear();
			scene.getStylesheets().addAll(styleSheets);
			scene.getRoot().getStylesheets().addAll(styleSheets);
		}
		catch(IllegalStateException ignored) {}
	}

	public double getPosX() {
		return posX;
	}

	public double getPosY() {
		return posY;
	}

	public void test() {
		System.out.println("Test Successful");
	}

	private static @NotNull UnsupportedOperationException centerOnWaitError() {
		String message      = "* When you call showAndWait() while also choosing to center your Scene, you must declare the size(width, height) in your build or set statement *";
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
