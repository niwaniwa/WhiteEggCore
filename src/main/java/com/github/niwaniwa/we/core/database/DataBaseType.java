package com.github.niwaniwa.we.core.database;

public enum DataBaseType {

	MongoDB("mongodb"),
	MySQL("mysql");

	private final String type;

	private DataBaseType(String type) {
		this.type = type;
	}

	public String getType(){
		return type;
	}

}
