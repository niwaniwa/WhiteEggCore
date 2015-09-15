package com.github.niwaniwa.we.core.database;

public class Database {

	private static Database database;

	private boolean use;
	private String user;
	private String pass;

	private Database(String url, String user, String pass){
		this.use = false;
		if(url.startsWith("jcbc:")){
			this.user = user;
			this.pass = pass;
			this.use = true;
		}
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public boolean use(){
		return use;
	}

	public static Database getInstance(){
		return database;
	}

}
