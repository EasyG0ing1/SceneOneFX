package com.simtechdata.sceneonefx.choiceresponse;

import com.simtechdata.sceneonefx.SceneOne;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class YesNo extends Application {

	@Override public void start(Stage primaryStage) {
		buildControls();
		SceneOne.set(sceneId, vbox, width, height).centered().onCloseEvent(e->SceneOne.remove(sceneId)).show();
	}

	private final String sceneId = "YesNoTest";
	private final double width = 300;
	private final double height = 250;
	private final Label responseLabel = new Label("Response:");
	private final Label response = new Label();
	private final Button btnAsk = new Button("Ask");
	private VBox vbox;

	private void buildControls() {
		btnAsk.setOnAction(e->askQuestion());
		HBox hbox = new HBox(15,responseLabel, response);
		response.setMinWidth(150);
		hbox.setAlignment(Pos.CENTER);
		vbox = new VBox(20,hbox,btnAsk);
		vbox.setPadding(new Insets(15));
		vbox.setMinWidth(width);
		vbox.setAlignment(Pos.CENTER);
	}

	private void askQuestion() {
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
	}
}
