package com.simtechdata.sceneonefx.id;

public class StageID {

	public StageID(String stageId) {
		this.stageId = stageId;
	}

	private final String stageId;

	public boolean is(String stageId) {
		return this.stageId.equals(stageId);
	}

	@Override
	public String toString() {
		return stageId;
	}
}
