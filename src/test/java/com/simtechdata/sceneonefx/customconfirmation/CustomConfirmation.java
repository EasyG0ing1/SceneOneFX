package com.simtechdata.sceneonefx.customconfirmation;

import com.simtechdata.sceneonefx.SceneOne;
import com.simtechdata.sceneonefx.alerts.ConfirmationClass;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CustomConfirmation extends Application {

	@Override public void start(Stage primaryStage) {
		makeControls();
		setControlActions();
		SceneOne.set(sceneId,vbox).newStage().centered().show();
	}


	private final String            sceneId      = "CustomAlert";
	private final ConfirmationClass confirmation = new ConfirmationClass(e->getAnswer());
	private final double            width        = 300;
	private final double            height       = 175;
	private       Button            btnGenerateAlert;
	private       Label             label;
	private       VBox              vbox;


	private void makeControls() {
		btnGenerateAlert = new Button("Question");
		label = new Label("Press button to ask question");
		vbox = new VBox(20,label,btnGenerateAlert);
		vbox.setPadding(new Insets(20, 20, 20, 20));
		vbox.setPrefWidth(width);
		vbox.setPrefHeight(height);
		vbox.setAlignment(Pos.CENTER);
	}

	private void setControlActions() {
		btnGenerateAlert.setOnAction(e-> confirmation.showAndWait("Do you like peas AND carrots?"));
	}

	private void getAnswer() {
		System.out.println("User answered: " + confirmation.getAnswer());
	}

}
