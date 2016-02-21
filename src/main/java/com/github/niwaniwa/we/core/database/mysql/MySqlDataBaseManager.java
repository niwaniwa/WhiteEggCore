package com.github.niwaniwa.we.core.database.mysql;

import com.github.niwaniwa.we.core.database.DataBase;

public class MySqlDataBaseManager extends DataBase {

    public MySqlDataBaseManager(String host, int port) {
        super(host, port);

    }

    @Override
    public String getName() {
        return null;
    }

}
