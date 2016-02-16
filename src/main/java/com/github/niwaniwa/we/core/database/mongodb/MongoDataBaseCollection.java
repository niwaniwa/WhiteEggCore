package com.github.niwaniwa.we.core.database.mongodb;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bson.Document;

import com.github.niwaniwa.we.core.database.mongodb.MongoDataBaseManager.MongoDataBase;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

public class MongoDataBaseCollection {

	private MongoDataBaseManager manager;
	private MongoDataBase database;
	private MongoCollection<Document> colletion;

	public MongoDataBaseCollection(MongoDataBaseManager manager, MongoDataBase database, String colletion) {
		this.manager = manager;
		this.database = database;
		this.colletion = this.database.getCollection(colletion);
	}

	public void update(Document document, Document document2) {
		this.getCollection().updateOne(document, document2);
	}

	public void insert(Document bson){
		this.getCollection().insertOne(bson);
	}

	public FindIterable<Document> find(Document bson){
		return this.getCollection().find(bson);
	}

	public Document findOne(Document bson){
		FindIterable<Document> lists = this.find(bson);
		return lists.first();
	}

	public boolean contains(Object obj){
		while (this.getDocuments().iterator().hasNext()) {
			Document doc = this.getDocuments().iterator().next();
			for(Map.Entry<String, Object> entry : doc.entrySet()){
				if(entry.getValue().equals(obj)){ return true; }
			}
		}
		return false;
	}

	public FindIterable<Document> getDocuments(){
		return colletion.find();
	}

	public Object get(String key){
		while (this.getDocuments().iterator().hasNext()) {
			Document doc = this.getDocuments().iterator().next();
			return doc.get(key);
		}
		return null;
	}

	public Object get(Document source, String key){
		MongoCursor<Document> cursor = this.find(source).iterator();
		if (cursor.hasNext()) {
			Document doc = cursor.next();
            return doc.get(key);
		}
		return null;
	}

	public List<Object> get(String key, int length){
		int size = (length != 0 ? length : Integer.MAX_VALUE);
		List<Object> values = new ArrayList<>();
		while (this.getDocuments().iterator().hasNext()) {
			if (values.size() == size) { break; }
			Document doc = this.getDocuments().iterator().next();
			Object obj = doc.get(key);
			if(obj != null){ values.add(obj); }
		}
		return values;
	}

	public String getString(String key){
		Object obj = get(key);
		if (obj == null) { return null; }
		return String.valueOf(get(key));
	}

	public String getString(Document source, String key){
		return (this.get(key) != null ? String.valueOf(this.get(key)) : null);
	}

	public boolean getBoolean(String key){
		return Boolean.getBoolean(getString(key));
	}

	public boolean getBoolean(Document source, String key){
		return Boolean.getBoolean(getString(source, key));
	}

	public MongoCollection<Document> getCollection(){
		return colletion;
	}

	public MongoDataBaseManager getManager() {
		return manager;
	}

}
