package com.github.niwaniwa.we.core.command.toggle.type;

public enum ToggleType {

	SERVER("server"),
	MODERATOR("moderator"),
	DEFAULT("default"),
	PLUGIN("plugin");

	private final String type;

	private ToggleType(String type){
		this.type = type;
	}

	public String getType(){
		return type;
	}

	public static ToggleType get(String name){
		for(ToggleType type : ToggleType.values()){
			if(type.getType().equals(name)){
				return type;
			}
		}
		return ToggleType.DEFAULT;
	}

}
