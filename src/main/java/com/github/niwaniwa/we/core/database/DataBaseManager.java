package com.github.niwaniwa.we.core.database;

import java.util.HashMap;
import java.util.Map;

public class DataBaseManager {

	private static Map<String, Class<? extends DataBase>> types = new HashMap<>();

	public static <T extends DataBase> void register(String name, Class<T> instance){
		types.put(name, instance);
	}

	public static <T extends DataBase> Map<String, Class<? extends DataBase>> getDatabaseClass(){
		return types;
	}


}
