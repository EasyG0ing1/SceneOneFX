package com.simtechdata.sceneonefx.alerts;

import com.simtechdata.sceneonefx.SceneOne;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

public class ConfirmationClass {

	/**
	 * Notice that we set the stages onCloseEvent to be consumed. This way, the user
	 * cannot just close the alert window without answering the question.
	 */

	public ConfirmationClass(EventHandler<WindowEvent> onHiddenEventHandler) {
		makeControls();
		setControlActions();
		SceneOne.set(sceneId,vbox)
				.alwaysOnTop()
				.newStage()
				.size(width,height)
				.centered()
				.onHiddenEvent(onHiddenEventHandler)
				.initStyle(StageStyle.UTILITY)
				.onCloseEvent(Event::consume)
				.build();
	}

	private final String sceneId = "ConfirmationClass";
	private final double width   = 250;
	private final double height  = 150;
	private       Button btnYes;
	private       Button btnNo;
	private       Label  label;
	private       VBox   vbox;
	private       String answer  = "";

	private void makeControls() {
		btnYes = new Button("Yes");
		btnNo = new Button("No");
		label  = new Label();
		HBox hbox = new HBox(20,btnYes,btnNo);
		hbox.setAlignment(Pos.CENTER);
		vbox   = new VBox(20, label, hbox);
		vbox.setPadding(new Insets(20, 20, 20, 20));
		vbox.setPrefWidth(width);
		vbox.setPrefHeight(height);
		vbox.setAlignment(Pos.CENTER);
	}

	private void setControlActions() {
		btnYes.setOnAction(e-> {
			answer = "Yes";
			SceneOne.close(sceneId);
		});
		btnNo.setOnAction(e-> {
			answer = "No";
			SceneOne.close(sceneId);
		});
	}

	public void showAndWait(String message) {
		Platform.runLater(() -> label.setText(message));
		SceneOne.showAndWait(sceneId);
	}

	public String getAnswer() {
		return answer;
	}

}
