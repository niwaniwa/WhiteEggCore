package com.github.niwaniwa.we.core.database;

public abstract class DataBase {

    private final String host;
    private final int port;

    public DataBase(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public abstract String getName();

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

}
