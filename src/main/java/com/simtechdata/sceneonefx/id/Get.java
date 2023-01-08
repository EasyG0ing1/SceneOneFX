package com.simtechdata.sceneonefx.id;

import java.util.concurrent.ConcurrentHashMap;

public class Get {

	private static final ConcurrentHashMap<String, SceneID> sceneIDMap = new ConcurrentHashMap<>();
	private static final ConcurrentHashMap<String, StageID> stageIDMap = new ConcurrentHashMap<>();

	public static SceneID newSceneID(String sceneId) {
		if(sceneIDMap.containsKey(sceneId))
			return sceneIDMap.get(sceneId);
		sceneIDMap.put(sceneId, new SceneID(sceneId));
		return sceneIDMap.get(sceneId);
	}

	public static StageID newStageID(String stageId) {
		if(stageIDMap.containsKey(stageId))
			return stageIDMap.get(stageId);
		stageIDMap.put(stageId, new StageID(stageId));
		return stageIDMap.get(stageId);
	}

	public static SceneID sceneID(String sceneId) {
		return sceneIDMap.getOrDefault(sceneId,newSceneID(sceneId));
	}

	public static StageID randomStageID() {
		String stageId = RandomString.get(100);
		return newStageID(stageId);
	}
}
