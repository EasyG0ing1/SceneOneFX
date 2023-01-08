package com.simtechdata.sceneonefx.properswitch;

import com.simtechdata.sceneonefx.SceneOne;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ProperSceneSwitching {

	/**
	 * It might be tempting to simply close out and remove Scenes before you open other Scenes, but doing this can be problematic.
	 * <p>
	 * This is the best way to switch Scenes out using the same stage. You can define Scenes that have different Stage properties,
	 * then just set the parent on any of those scenes and show it.
	 */

	public ProperSceneSwitching() {

	}

	private final String sceneId1 = "Scene1";
	private final String sceneId2 = "Scene2";

	private VBox              parent1;
	private VBox              parent2;
	private HBox buttonBox;
	private Label             lbl1;
	private Label             lbl12;
	private Label             lbl2;
	private Label             lbl22;
	private TextField         tf1;
	private ChoiceBox<String> choiceBox2;
	private Button            btnOK;
	private Button            btnCancel;

	private void makeControls() {
		lbl1 = new Label("Type your name and hit enter");
		lbl12 = new Label();
		lbl2 = new Label("Choose a color");
		lbl22 = new Label();
		tf1 = new TextField();
		choiceBox2 = new ChoiceBox<>(FXCollections.observableArrayList("Red", "Green", "Blue", "Orange", "Yellow"));
		btnOK = new Button("OK");
		btnCancel = new Button("Cancel");
		buttonBox = new HBox(20, btnCancel, btnOK);
	}

	private void setControlActions() {
		btnOK.setOnAction(e->{
			if(SceneOne.isShowing(sceneId1)) {

			}
		});
	}

	private VBox getParent(int index) {
		switch(index) {
			case 1: return new VBox(20,lbl1, tf1, lbl12,buttonBox);
			case 2: return new VBox(20,lbl2, choiceBox2, lbl22, buttonBox);
			default: return null;
		}
	}

}
