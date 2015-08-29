package com.github.niwaniwa.we.core.command.toggle;

public class TogglePermission {

	private String permission;
	private boolean isDefault;

	public TogglePermission(String permission, boolean isDefault) {
		this.permission = permission;
		this.isDefault = isDefault;
	}

	public String getPermission(){
		return permission;
	}

	public boolean isDefault(){
		return isDefault;
	}

}
