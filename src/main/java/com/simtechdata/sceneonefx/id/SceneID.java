package com.simtechdata.sceneonefx.id;

public class SceneID {

	public SceneID(String sceneId) {
		this.sceneId = sceneId;
	}

	private final String sceneId;

	public boolean is(String sceneId) {
		return this.sceneId.equals(sceneId);
	}

	@Override
	public String toString() {
		return  sceneId;
	}
}
