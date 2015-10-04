package com.github.niwaniwa.we.core.util.message;

import java.util.HashMap;
import java.util.Map;

public class MessageReplace implements MessageExtension {

	private Map<String, String> replace;

	public MessageReplace() {
		replace  = new HashMap<>();
	}

	public void add(String key, String value){
		replace.put(key, value);
	}

	public Map<String, String> replaceFormat(){
		return replace;
	}

	public String execute(String message){
		String string = message;
		for(String key : replace.keySet()){
			string.replace(key, replace.get(key));
		}
		return string;
	}


}
