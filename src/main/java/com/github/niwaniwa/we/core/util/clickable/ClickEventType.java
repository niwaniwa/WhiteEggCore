package com.github.niwaniwa.we.core.util.clickable;

public enum ClickEventType {

	RUN_COMMAND("run_command"),
	SUGGEST_COMMAND("suggest_command"),
	OPEN_URL("open_url");
	private final String type;

	private ClickEventType(String type) {
        this.type = type;
    }

	public String getType() {
		return type;
	}
}
