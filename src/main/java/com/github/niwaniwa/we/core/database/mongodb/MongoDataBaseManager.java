package com.github.niwaniwa.we.core.database.mongodb;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bson.Document;

import com.github.niwaniwa.we.core.database.DataBase;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoDataBaseManager extends DataBase {

	private static final String name = "MongoDB";

	private MongoClient client;

	static {
		Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
		mongoLogger.setLevel(Level.OFF);
	}

	public MongoDataBaseManager(String host, int port) {
		super(host, port);
		this.client = new MongoClient(host, port);
	}

	public MongoDatabase getDatabase(String name){
		return client.getDatabase(name);
	}

	public MongoClient getClient() {
		return client;
	}


	public static Document toDocument(Map<String, Object> data){
		return new Document(data);
	}

	@Override
	public String getName() {
		return name;
	}

}
