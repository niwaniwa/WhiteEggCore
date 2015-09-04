package com.github.niwaniwa.we.core.json;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class JSONManager {


	public JSONManager() {
	}

	/**
	 * localにファイルを保存します
	 * @param path 保存フォルダ
	 * @param file ファイル名
	 * @param json json
	 * @return 成功したか
	 * @throws IOException
	 */
	public boolean writeJSON(File path, String file, JSONObject json) throws IOException {
		if(!path.exists()){ path.mkdirs(); }
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(path, file)));
		PrintWriter pw = new PrintWriter(bw);
		pw.write(json.toString());
		pw.close();

		return true;
	}

	public boolean writeJSON(String path, String file, JSONObject json) throws IOException {
		return writeJSON(new File(file), file, json);
	}

	/**
	 * local
	 *
	 * @return
	 * @throws IOException
	 */
	public JSONObject getJSON(File file) throws IOException {
		if (!file.exists()) { return null; }
		if (file.isDirectory()) { return null; }
		if (!file.getName().endsWith(".json")) { return null; }
		BufferedReader reader = new BufferedReader(new FileReader(file));
		StringBuilder b = new StringBuilder();
		String str = reader.readLine();
		while (str != null) {
			b.append(str);
			str = reader.readLine();
		}
		reader.close();
		if (b.length() == 0) { return null; }
		return JSONObject.fromObject(b.toString());

	}

	public  static <T> Map<String, Object> toMap(T o){

		Map<String, Object> map = new HashMap<>();

		if(!(o instanceof JSONObject)){
			if(!(o instanceof JSONArray)){ return map; }
		}

		JSONObject json = (JSONObject) o;
		@SuppressWarnings("unchecked")
		Iterator<String> ite = json.keys();
		while(ite.hasNext()){
			String key = ite.next();
			Object value = json.get(key);
			if(value instanceof JSONArray){
				value = toList((JSONArray) value);
			} else if (value instanceof JSONObject) {
				value = toMap((JSONObject) value);
			}
			map.put(key, value);
		}
		return map;
	}

	public static List<Object> toList(JSONArray array){
		List<Object> list = new ArrayList<Object>();
		for (int i = 0; i < array.size(); i++) {
			Object value = array.get(i);
			if (value instanceof JSONArray) {
				value = toList((JSONArray) value);
			}

			else if (value instanceof JSONObject) {
				value = toMap((JSONObject) value);
			}
			list.add(value);
		}
		return list;
	}

}
