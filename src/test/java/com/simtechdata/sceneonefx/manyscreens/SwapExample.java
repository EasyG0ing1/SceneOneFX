package com.simtechdata.sceneonefx.manyscreens;

import com.simtechdata.sceneonefx.SceneOne;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SwapExample extends Application {

	private final String sceneId = "SwapExample";
	Scene sceneOne = null;
	Scene sceneTwo = null;

	private void buildSceneOne() {
		Label label = new Label("Scene One");
		Button btnSwap = new Button("Swap Scene");
		btnSwap.setOnAction(e-> SceneOne.swapScene(sceneId, sceneTwo));
		VBox vbox = new VBox(20,label,btnSwap);
		vbox.setPadding(new Insets(20));
		vbox.setAlignment(Pos.CENTER);
		sceneOne = new Scene(vbox);
	}

	private void buildSceneTwo() {
		Label label = new Label("Scene Two");
		Button btnSwap = new Button("Swap Scene");
		btnSwap.setOnAction(e-> SceneOne.swapScene(sceneId, sceneOne));
		VBox vbox = new VBox(20,label,btnSwap);
		vbox.setPadding(new Insets(20));
		vbox.setAlignment(Pos.CENTER);
		sceneTwo = new Scene(vbox);
	}

	@Override public void start(Stage primaryStage) {
		buildSceneOne();
		buildSceneTwo();
		SceneOne.set(sceneId, sceneOne, 300, 300).show();
	}
}
