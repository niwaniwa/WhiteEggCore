package com.github.niwaniwa.we.core.command.toggle.type;

public enum ToggleType {

	SERVER("server"),
	MODERATOR("moderator"),
	DEFAULT("default"),
	PLUGIN("plugin");

	private String type;

	private ToggleType(String type){
		this.type = type;
	}

	public String getType(){
		return type;
	}

}
