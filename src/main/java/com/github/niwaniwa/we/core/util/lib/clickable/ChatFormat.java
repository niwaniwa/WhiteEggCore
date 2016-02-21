package com.github.niwaniwa.we.core.util.lib.clickable;

public enum ChatFormat {
    BOLD("bold"),
    UNDERLINED("underlined"),
    STRIKETHROUGH("strikethrough"),
    ITALIC("italic"),
    OBFUSCATED("obfuscated");
    private final String format;

    private ChatFormat(String format) {
        this.format = format;
    }

    public String getFormat() {
        return format;
    }
}
