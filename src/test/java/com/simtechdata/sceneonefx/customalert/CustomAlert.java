package com.simtechdata.sceneonefx.customalert;

import com.simtechdata.sceneonefx.SceneOne;
import com.simtechdata.sceneonefx.alerts.AlertClass;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CustomAlert extends Application {


	@Override public void start(Stage primaryStage) throws Exception {
		makeControls();
		setControlActions();
		SceneOne.set(sceneId,vbox).centered().show();
	}

	private final String     sceneId = "CustomAlert";
	private       AlertClass alert   = new AlertClass();
	private final double     width   = 300;
	private final double height = 175;
	private Button btnGenerateAlert;
	private Label  label;
	private VBox   vbox;


	private void makeControls() {
		btnGenerateAlert = new Button("Alert");
		label = new Label("Press button to generate alert");
		vbox = new VBox(20,label,btnGenerateAlert);
		vbox.setPadding(new Insets(20, 20, 20, 20));
		vbox.setPrefWidth(width);
		vbox.setPrefHeight(height);
		vbox.setAlignment(Pos.CENTER);
	}

	private void setControlActions() {
		btnGenerateAlert.setOnAction(e->alert.showAlert("You have been alerted"));
	}

}
