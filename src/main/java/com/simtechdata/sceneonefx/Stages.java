package com.simtechdata.sceneonefx;

import com.simtechdata.sceneonefx.id.Get;
import com.simtechdata.sceneonefx.id.SceneID;
import com.simtechdata.sceneonefx.id.StageID;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Stages {

	private final static ConcurrentHashMap<StageID, Stage>   stageMap               = new ConcurrentHashMap<>();
	private final static ConcurrentHashMap<SceneID, StageID> idMap                  = new ConcurrentHashMap<>();
	private final static ConcurrentHashMap<StageID, Boolean> modalityMap            = new ConcurrentHashMap<>();
	private final static ConcurrentHashMap<StageID, Boolean> styleMap               = new ConcurrentHashMap<>();
	private final static ConcurrentHashMap<StageID, Boolean> ownerMap               = new ConcurrentHashMap<>();
	private static       Stage                               userDefaultStage       = new Stage();
	private static final Stage                               defaultStage           = new Stage();
	private static       boolean                             defaultModalitySet     = false;
	private static       boolean                             defaultStyleSet        = false;
	private static       boolean                             defaultOwnerSet        = false;
	private static       boolean                             userDefaultModalitySet = false;
	private static       boolean                             userDefaultStyleSet    = false;
	private static       boolean                             userDefaultOwnerSet    = false;

	public static void initUserDefaultModality(Modality modality) {
		if (!userDefaultModalitySet) {
			userDefaultStage.initModality(modality);
			userDefaultModalitySet = true;
		}
		else {
			System.out.println("initModality has already been set for userDefaultStage");
		}
	}

	public static void initUserDefaultStyle(StageStyle style) {
		if (!userDefaultStyleSet) {
			userDefaultStage.initStyle(style);
			userDefaultStyleSet = true;
		}
		else {
			System.out.println("initStyle has already been set for userDefaultStage");
		}
	}

	public static void initUserDefaultOwner(Window window) {
		if (!userDefaultOwnerSet) {
			userDefaultStage.initOwner(window);
			userDefaultOwnerSet = true;
		}
		else {
			System.out.println("initOwner has already been set for userDefaultStage");
		}
	}

	public static void initDefaultModality(Modality modality) {
		if (!defaultModalitySet) {
			defaultStage.initModality(modality);
			defaultModalitySet = true;
		}
		else {
			System.out.println("initModality has already been set for defaultStage");
		}
	}

	public static void initDefaultStyle(StageStyle style) {
		if (!defaultStyleSet) {
			defaultStage.initStyle(style);
			defaultStyleSet = true;
		}
		else {
			System.out.println("initStyle has already been set for defaultStage");
		}
	}

	public static void initDefaultOwner(Window window) {
		if (!defaultOwnerSet) {
			defaultStage.initOwner(window);
			defaultOwnerSet = true;
		}
		else {
			System.out.println("initOwner has already been set for defaultStage");
		}
	}

	public static void setUserDefaultStage(Stage stage) {
		userDefaultStage       = stage;
		userDefaultStyleSet    = false;
		userDefaultModalitySet = false;
	}

	public static void setUserDefaultStage(Stage stage, boolean newStage) {
		userDefaultStage       = stage;
		userDefaultStyleSet    = !newStage;
		userDefaultModalitySet = !newStage;
		userDefaultOwnerSet    = !newStage;
	}

	public static Stage getUserDefaultStage() {
		return userDefaultStage;
	}

	public static Stage getDefaultStage() {
		return defaultStage;
	}


	public static void initModality(StageID stageID, Modality modality) {
		if(!modalityMap.containsKey(stageID)) {
			stageMap.get(stageID).initModality(modality);
			modalityMap.put(stageID, true);
		}
		else {
			System.out.println("StageID " + stageID.toString() + "'s initModality has already been set");
		}
	}

	public static void initStyle(StageID stageID, StageStyle stageStyle) {
		if(!styleMap.containsKey(stageID)) {
			stageMap.get(stageID).initStyle(stageStyle);
			styleMap.put(stageID, true);
		}
		else {
			System.out.println("StageID " + stageID.toString() + "'s initStyle has already been set");
		}
	}

	public static void initOwner(StageID stageID, Window window) {
		if(!ownerMap.containsKey(stageID)) {
			stageMap.get(stageID).initOwner(window);
			ownerMap.put(stageID, true);
		}
		else {
			System.out.println("StageID " + stageID.toString() + "'s initOwner has already been set");
		}
	}

	private static void removeSceneFromMaps(SceneID sceneID) {
		idMap.remove(sceneID);
		List<StageID> list = new ArrayList<>(idMap.values());
		for (StageID stageID : stageMap.keySet()) {
			if (!list.contains(stageID)) {stageMap.remove(stageID);}
		}
	}

	public static void addStage(SceneID sceneID, Stage stage) {
		removeSceneFromMaps(sceneID);
		StageID stageID = Get.randomStageID();
		stageMap.put(stageID, stage);
		idMap.put(sceneID, stageID);
	}

	public static void newStage(SceneID sceneID) {
		removeSceneFromMaps(sceneID);
		StageID stageID = Get.randomStageID();
		Stage   stage   = new Stage();
		stageMap.put(stageID, stage);
		idMap.put(sceneID, stageID);
	}

	public static Stage getStage(SceneID sceneID) {
		if (idMap.containsKey(sceneID)) {return stageMap.getOrDefault(idMap.get(sceneID), null);}
		return null;
	}

	public static StageID getStageId(SceneID sceneID) {
		return idMap.getOrDefault(sceneID, null);
	}

	public static boolean hasStage(SceneID sceneID) {
		if (idMap.containsKey(sceneID)) {
			return stageMap.containsKey(idMap.get(sceneID));
		}
		return false;
	}

	public static void removeScene(SceneID sceneID) {
		removeSceneFromMaps(sceneID);
	}
}
