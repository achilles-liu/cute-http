package org.johnny.http;

public enum StageMgr {
	INSTANCE;
	
	StageContainer stageContainer = new StageContainer();
	
	public StageContainer container() {
		return this.stageContainer;
	}
	
}
