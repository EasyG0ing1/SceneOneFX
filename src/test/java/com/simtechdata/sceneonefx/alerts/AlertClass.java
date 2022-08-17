package com.simtechdata.sceneonefx.alerts;

import com.simtechdata.sceneonefx.SceneOne;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.StageStyle;

public class AlertClass {

	public AlertClass() {
		makeControls();
		setControlActions();
		SceneOne.disableNotice();
		SceneOne.set(sceneId,vbox)
				.newStage()
				.centered()
				.alwaysOnTop()
				.size(width,height)
				.onCloseEvent(Event::consume)
				.initStyle(StageStyle.UTILITY).build();
	}

	private final String sceneId = "CustomAlertClass";
	private final double width = 200;
	private final double height = 150;
	private Button btnOK;
	private Label label;
	private VBox vbox;

	private void makeControls() {
		btnOK = new Button("OK");
		label = new Label();
		vbox = new VBox(20,label,btnOK);
		vbox.setPadding(new Insets(20,20,20,20));
		vbox.setPrefWidth(width);
		vbox.setPrefHeight(height);
		vbox.setAlignment(Pos.CENTER);
	}

	private void setControlActions() {
		btnOK.setOnAction(e->SceneOne.close(sceneId));
	}

	public void showAlert(String message) {
		Platform.runLater(() -> label.setText(message));
		SceneOne.showAndWait(sceneId);
	}

}
