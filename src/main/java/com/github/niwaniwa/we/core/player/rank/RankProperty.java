package com.github.niwaniwa.we.core.player.rank;

public enum RankProperty {

    LOWEST("lowest", 0),
    LOW("low", 1),
    NORMAL("normal", 2),
    HIGH("high", 3),
    HIGHEST("highest", 4);

    private final int id;
    private final String name;

    private RankProperty(String name, int id) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return name;
    }

    public static RankProperty valueOfString(String str) {
        for (RankProperty p : values()) {
            if (p.getName().equalsIgnoreCase(str)) {
                return p;
            }
        }
        return null;
    }

}
