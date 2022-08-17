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

public class ChoiceResponse extends Application {

	@Override public void start(Stage primaryStage) throws Exception {
		buildControls();
		SceneOne.set(sceneId, vbox, width, height).centered().onCloseEvent(e->SceneOne.remove(sceneId)).show();
	}

	private final String sceneId       = "YesNoTest";
	private final double width         = 300;
	private final double height        = 250;
	private final Label  responseLabel = new Label("Response:");
	private final Label  response      = new Label();
	private final Button btnAsk        = new Button("Ask");
	private       VBox   vbox;

	private void buildControls() {
		btnAsk.setOnAction(e -> askQuestion());
		HBox hbox = new HBox(15, responseLabel, response);
		response.setMinWidth(150);
		hbox.setAlignment(Pos.CENTER);
		vbox = new VBox(20, hbox, btnAsk);
		vbox.setPadding(new Insets(15));
		vbox.setMinWidth(width);
		vbox.setAlignment(Pos.CENTER);
	}

	private void askQuestion() {
		String answerDisplay = "";
		int answer = SceneOne.choiceResponse(sceneId,
											 "Which ice cream flavor do you want?",
											 300,
											 100,
											 Pos.BASELINE_LEFT,
											 false,
											 "Chocolate", "Vanilla", "Strawberry");
		switch(answer) {
			case 0:
				response.setText("User wants Chocolate");
				answerDisplay = "You chose Chocolate";
				break;
			case 1:
				response.setText("User wants Vanilla");
				answerDisplay = "You chose Vanilla";
				break;
			case 2:
				response.setText("User wants Strawberry");
				answerDisplay = "You chose Strawberry";
				break;
			default:
				response.setText("User did not respond");
				answerDisplay = "You decided not to decide";
		}
		SceneOne.showMessage(sceneId,300,80, answerDisplay);
	}
}
