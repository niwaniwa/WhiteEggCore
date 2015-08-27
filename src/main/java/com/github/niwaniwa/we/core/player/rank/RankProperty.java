package com.github.niwaniwa.we.core.player.rank;

public enum RankProperty {

	LOWEST(0),
	LOW(1),
	NORMAL(2),
	HIGH(3),
	HIGHEST(4);

	private final int id;

	private RankProperty(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}

}
