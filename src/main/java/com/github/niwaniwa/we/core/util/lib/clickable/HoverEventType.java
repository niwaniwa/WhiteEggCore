package com.github.niwaniwa.we.core.util.lib.clickable;

public enum HoverEventType {
	SHOW_TEXT("show_text"),
	SHOW_ITEM("show_item"),
	SHOW_ACHIEVEMENT("show_achievement");
	private final String type;

	private HoverEventType(String type) {
        this.type = type;
    }

	public String getType() {
		return type;
	}
}
