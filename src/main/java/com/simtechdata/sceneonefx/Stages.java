package com.simtechdata.sceneonefx;

import javafx.stage.Stage;

import java.nio.file.Path;
import java.util.*;

public class Stages {

	private final static Map<String, Stage>             stageMap      = new HashMap<>();
	private final static Stage                          defaultStage  = new Stage();
	private final static Map<Stage, LinkedList<String>> shownSceneMap = new HashMap<>();

	public int getSceneCount(String sceneId) {
		Stage sceneStage = null;
		int count = 0;
		for(String sid : stageMap.keySet()) {
			if(sid.equals(sceneId)) {
				sceneStage = stageMap.get(sid);
				count++;
				break;
			}
		}
		if (sceneStage != null) {
			if (shownSceneMap.containsKey(sceneStage)) {
				LinkedList<String> scenes = shownSceneMap.get(sceneStage);
				for(String sid : scenes) {
					if(sid.equals(sceneId))
						count++;
				}
			}
		}
		return count;
	}

	public static void addStage(String sceneId, Stage stage) {
		if(!stageMap.containsKey(sceneId)) {
			stageMap.put(sceneId, stage);
		}
		if (!shownSceneMap.containsKey(stage)) {
			shownSceneMap.put(stage, new LinkedList<>());
		}
	}

	public static Stage getStage(String sceneId) {
		return stageMap.getOrDefault(sceneId, null);
	}

	public static boolean hasStage(String sceneId) {
		return stageMap.containsKey(sceneId);
	}

	public static Stage getDefaultStage() {
		return defaultStage;
	}

	public static void showingScene(String sceneId) {
		Stage stage = stageMap.get(sceneId);
		if (shownSceneMap.containsKey(stage)) {
			if (shownSceneMap.get(stage).contains(sceneId)) {
				LinkedList<String> list = shownSceneMap.get(stage);
				list.remove(sceneId);
				list.addLast(sceneId);
				shownSceneMap.replace(stage, list);
			}
			else
				shownSceneMap.get(stage).addLast(sceneId);
		}
	}

	public static boolean sceneHasHistory(String sceneId) {
		Stage stage = stageMap.get(sceneId);
		return shownSceneMap.get(stage).size() > 1;
	}

	public static String getLastShownScene(String sceneId) {
		Stage stage = stageMap.get(sceneId);
		if (shownSceneMap.get(stage).size() > 0) {
			shownSceneMap.get(stage).removeLast();
		}
		return shownSceneMap.get(stage).getLast();
	}

	private static Stage getStageForScene(String sceneId) {
		for (String id : stageMap.keySet()) {
			if (id.equals(sceneId)){
				return stageMap.get(sceneId);
			}
		}
		return null;
	}

	public static void removeShownScene(String sceneId) {
		Stage stage = stageMap.get(sceneId);
		if(shownSceneMap.containsKey(stage)) {
			LinkedList<String> list = shownSceneMap.get(stage);
			if(list.contains(sceneId)) {
				list.remove(sceneId);
				shownSceneMap.replace(stage, list);
			}
		}
	}

	public static void removeScene(String sceneId) {
		if(stageMap.containsKey(sceneId)) {
			Stage sceneStage = stageMap.get(sceneId);
			if(shownSceneMap.containsKey(sceneStage)) {
				LinkedList<String> sceneList = shownSceneMap.get(sceneStage);
				sceneList.remove(sceneId);
				if(sceneList.size() < 1) {
					shownSceneMap.remove(sceneStage);
				}
				else {
					shownSceneMap.replace(sceneStage, sceneList);
				}
			}
			stageMap.remove(sceneId);
		}
	}

}
